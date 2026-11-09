package com.example.data.update

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.concurrent.TimeUnit
import kotlin.coroutines.coroutineContext

/**
 * Talks to the GitHub release channel: finds the newest release, streams its APK and hands it to
 * the system package installer.
 *
 * Updates are side-loaded from GitHub Releases rather than delivered through Google Play, so the
 * user has to allow "Install unknown apps" once. That permission prompt is the one store-less
 * distribution tax; the updater detects it and deep-links to the right toggle.
 */
class AppUpdater(context: Context) {

    private val appContext: Context = context.applicationContext
    private val releaseApi = GitHubReleaseApi()

    private val downloadClient = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .build()

    val installedVersionName: String = BuildConfig.VERSION_NAME

    val releasesPageUrl: String get() = releaseApi.releasesPageUrl

    suspend fun latestRelease(): ReleaseLookup = releaseApi.latest()

    /**
     * Streams the release APK into the app's external files directory and reports progress.
     * The file is written to `.part` first so a cancelled or broken download never looks complete.
     */
    suspend fun downloadApk(
        info: ReleaseInfo,
        onProgress: (downloadedBytes: Long, totalBytes: Long) -> Unit
    ): File = withContext(Dispatchers.IO) {
        val url = info.apkUrl ?: throw IOException("This release has no APK attached")

        val downloadDir = File(
            appContext.getExternalFilesDir(null) ?: appContext.cacheDir,
            UPDATE_DIR
        ).apply { mkdirs() }
        downloadDir.listFiles()?.forEach { stale -> stale.delete() }

        val target = File(downloadDir, "naihati-nights-${info.versionName}.apk")
        val partial = File(downloadDir, "${target.name}.part")

        val request = Request.Builder()
            .url(url)
            .header("Accept", "application/octet-stream")
            .header("User-Agent", "NaihatiNights-Android")
            .build()

        val response = run {
            var lastError: IOException? = null
            repeat(2) { attempt ->
                try {
                    val r = downloadClient.newCall(request).execute()
                    if (r.isSuccessful) return@run r
                    r.close()
                    lastError = IOException("Download failed (HTTP ${r.code})")
                } catch (e: IOException) {
                    lastError = e
                }
                if (attempt == 0) Thread.sleep(1500)
            }
            throw lastError ?: IOException("Download failed")
        }
        response.use { resp ->
            val body = resp.body ?: throw IOException("The update server sent an empty download")
            val total = body.contentLength().takeIf { it > 0L } ?: info.apkSizeBytes

            body.byteStream().use { input ->
                FileOutputStream(partial).use { output ->
                    val buffer = ByteArray(64 * 1024)
                    var downloaded = 0L
                    while (true) {
                        coroutineContext.ensureActive()
                        val read = input.read(buffer)
                        if (read <= 0) break
                        output.write(buffer, 0, read)
                        downloaded += read
                        onProgress(downloaded, total)
                    }
                    output.flush()
                    output.fd.sync()
                }
            }
        }

        if (!partial.exists() || partial.length() <= 0L) {
            partial.delete()
            throw IOException("The downloaded update was empty")
        }
        if (target.exists()) target.delete()
        // Prefer Files.move over File.renameTo: renameTo is unreliable on some Android
        // versions / storage configurations and silently returns false, which previously
        // surfaced as a "Could not save the downloaded update" failure + Retry Update.
        var moved = false
        try {
            java.nio.file.Files.move(
                partial.toPath(),
                target.toPath(),
                java.nio.file.StandardCopyOption.REPLACE_EXISTING,
                java.nio.file.StandardCopyOption.ATOMIC_MOVE
            )
            moved = true
        } catch (_: Throwable) {
            moved = false
        }
        if (!moved) {
            // Fallback: stream-copy then delete the partial.
            partial.inputStream().use { input ->
                FileOutputStream(target).use { output ->
                    input.copyTo(output)
                    output.flush()
                    output.fd.sync()
                }
            }
            partial.delete()
        }
        if (!target.exists() || target.length() <= 0L) {
            target.delete()
            throw IOException("Could not save the downloaded update")
        }
        target
    }

    /** Android 8+ gates side-loading behind a per-app "install unknown apps" switch. */
    fun canInstallPackages(): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.O ||
            appContext.packageManager.canRequestPackageInstalls()

    fun openInstallPermissionSettings(): Boolean = open(
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent(
                Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                Uri.parse("package:${appContext.packageName}")
            )
        } else {
            Intent(Settings.ACTION_SECURITY_SETTINGS)
        }
    )

    /** Opens the system package installer for the downloaded APK. Returns false if nothing handles it. */
    fun installApk(apkFile: File): Boolean {
        if (!apkFile.exists()) return false
        val uri = FileProvider.getUriForFile(
            appContext,
            "${appContext.packageName}.fileprovider",
            apkFile
        )
        // Prefer ACTION_INSTALL_PACKAGE (the documented action for installing an APK) over the
        // generic ACTION_VIEW, which some OEM installers don't resolve for the package-archive
        // MIME type and which previously left the user stuck on "Retry Update".
        val intents = listOf(
            Intent(Intent.ACTION_INSTALL_PACKAGE).apply {
                data = uri
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                putExtra(Intent.EXTRA_RETURN_RESULT, false)
            },
            Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, "application/vnd.android.package-archive")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
        )
        for (intent in intents) {
            if (open(intent)) return true
        }
        return false
    }

    fun openReleasesPage(url: String = releasesPageUrl): Boolean =
        open(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

    private fun open(intent: Intent): Boolean = try {
        appContext.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        true
    } catch (_: Throwable) {
        false
    }

    private companion object {
        const val UPDATE_DIR = "updates"
    }
}
