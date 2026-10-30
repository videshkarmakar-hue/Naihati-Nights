package com.example.data.update

import com.example.BuildConfig
import com.squareup.moshi.Json
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.concurrent.TimeUnit

/** One downloadable file attached to a GitHub release. */
data class GitHubAsset(
    @param:Json(name = "name") val name: String? = null,
    @param:Json(name = "browser_download_url") val downloadUrl: String? = null,
    @param:Json(name = "size") val size: Long = 0L,
    @param:Json(name = "content_type") val contentType: String? = null
)

/** The subset of the GitHub release payload the updater needs. */
data class GitHubRelease(
    @param:Json(name = "tag_name") val tagName: String? = null,
    @param:Json(name = "name") val name: String? = null,
    @param:Json(name = "body") val body: String? = null,
    @param:Json(name = "html_url") val htmlUrl: String? = null,
    @param:Json(name = "published_at") val publishedAt: String? = null,
    @param:Json(name = "draft") val draft: Boolean = false,
    @param:Json(name = "prerelease") val prerelease: Boolean = false,
    @param:Json(name = "assets") val assets: List<GitHubAsset>? = null
)

/**
 * A published release, flattened for the UI.
 *
 * [apkUrl] is null when the release has no `.apk` asset yet; the app then offers the release page
 * instead of an in-app install.
 */
data class ReleaseInfo(
    val tagName: String,
    val versionName: String,
    val notes: String,
    val releaseUrl: String,
    val apkUrl: String?,
    val apkSizeBytes: Long,
    val publishedAt: String?
) {
    val hasApk: Boolean get() = !apkUrl.isNullOrBlank()
}

/** Result of asking GitHub for the newest published release. */
sealed interface ReleaseLookup {
    data class Found(val info: ReleaseInfo) : ReleaseLookup
    data object NoRelease : ReleaseLookup
    data class Failed(val message: String) : ReleaseLookup
}

/**
 * Reads the newest release from `https://api.github.com/repos/{owner}/{repo}/releases/latest`.
 *
 * Unauthenticated GitHub API calls are rate limited to 60 requests per hour per IP, which is far
 * more than a 6-hourly background check needs. No token is shipped in the APK on purpose: a token
 * baked into a public app would be readable by anyone.
 */
class GitHubReleaseApi(
    private val owner: String = BuildConfig.GITHUB_OWNER,
    private val repo: String = BuildConfig.GITHUB_REPO,
    private val client: OkHttpClient = defaultClient()
) {
    private val adapter = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()
        .adapter(GitHubRelease::class.java)

    val releasesPageUrl: String = "https://github.com/$owner/$repo/releases"

    suspend fun latest(): ReleaseLookup = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("https://api.github.com/repos/$owner/$repo/releases/latest")
            .header("Accept", "application/vnd.github+json")
            .header("User-Agent", "NaihatiNights-Android")
            .build()

        try {
            client.newCall(request).execute().use { response ->
                when {
                    response.code == 404 -> ReleaseLookup.NoRelease
                    !response.isSuccessful -> ReleaseLookup.Failed(
                        "The update server replied with HTTP ${response.code}"
                    )
                    else -> {
                        val payload = response.body?.string()
                        if (payload.isNullOrBlank()) {
                            ReleaseLookup.Failed("The update server sent an empty response")
                        } else {
                            val release = runCatching { adapter.fromJson(payload) }.getOrNull()
                            if (release == null) {
                                ReleaseLookup.Failed("Could not read the release")
                            } else if (release.draft) {
                                ReleaseLookup.NoRelease
                            } else {
                                ReleaseLookup.Found(release.toReleaseInfo(releasesPageUrl))
                            }
                        }
                    }
                }
            }
        } catch (_: IOException) {
            ReleaseLookup.Failed("No internet connection")
        } catch (t: Throwable) {
            ReleaseLookup.Failed(t.message ?: "Update check failed")
        }
    }

    private companion object {
        fun defaultClient(): OkHttpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .build()
    }
}

internal fun GitHubRelease.toReleaseInfo(fallbackUrl: String): ReleaseInfo {
    val tag = tagName?.trim().orEmpty()
    val apk = assets.orEmpty().firstOrNull { asset ->
        asset.name?.endsWith(".apk", ignoreCase = true) == true &&
            !asset.downloadUrl.isNullOrBlank()
    }
    return ReleaseInfo(
        tagName = tag,
        versionName = AppVersion.normalize(tag) ?: AppVersion.normalize(name) ?: tag,
        notes = body.orEmpty().trim().ifEmpty { "Bug fixes and polish for the Parikrama guide." },
        releaseUrl = htmlUrl?.takeIf { it.isNotBlank() } ?: fallbackUrl,
        apkUrl = apk?.downloadUrl,
        apkSizeBytes = apk?.size ?: 0L,
        publishedAt = publishedAt
    )
}

/** Turns GitHub's markdown body into short, readable lines for the update card. */
fun releaseNoteLines(body: String?, limit: Int = 5): List<String> {
    if (body.isNullOrBlank()) return emptyList()
    return body.lineSequence()
        .map { line ->
            line.trim()
                .removePrefix("###").removePrefix("##").removePrefix("#")
                .replace("**", "")
                .replace("`", "")
                .trim()
                .trimStart('-', '*', '+')
                .trim()
        }
        .filter { it.isNotEmpty() && !it.startsWith("<!--") }
        .distinct()
        .take(limit)
        .toList()
}
