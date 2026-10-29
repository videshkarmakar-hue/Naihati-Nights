package com.example

import com.example.data.update.AppVersion
import com.example.data.update.GitHubRelease
import com.example.data.update.releaseNoteLines
import com.example.data.update.toReleaseInfo
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Pure-JVM checks for the GitHub release channel. These cover the two things that can silently
 * break an update: comparing versions and reading GitHub's snake_case JSON.
 */
class AppUpdateTest {

  private val adapter = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()
    .adapter(GitHubRelease::class.java)

  @Test
  fun isNewer_comparesEachVersionSlot() {
    assertTrue(AppVersion.isNewer("3.0.10", "3.0.9"))
    assertTrue(AppVersion.isNewer("3.1.0", "3.0.9.9"))
    assertTrue(AppVersion.isNewer("4", "3.99.99"))
    assertTrue(AppVersion.isNewer("v3.0.9.1", "3.0.9"))

    assertFalse(AppVersion.isNewer("3.0.9", "3.0.9"))
    assertFalse(AppVersion.isNewer("3.0.9", "3.0.9.0"))
    assertFalse(AppVersion.isNewer("3.0.8", "3.0.9"))
    assertFalse(AppVersion.isNewer(null, "3.0.9"))
    assertFalse(AppVersion.isNewer("nightly", "3.0.9"))
  }

  @Test
  fun normalize_stripsTagPrefixAndSuffix() {
    assertEquals("3.0.10", AppVersion.normalize("v3.0.10"))
    assertEquals("3.0.10", AppVersion.normalize("release-3.0.10-beta"))
    assertEquals("3.0.10", AppVersion.normalize("  v3.0.10  "))
    assertNull(AppVersion.normalize("latest"))
    assertNull(AppVersion.normalize(null))
  }

  @Test
  fun parseRelease_readsSnakeCaseFieldsAndPicksTheApkAsset() {
    val json = """
      {
        "tag_name": "v3.0.10",
        "name": "Naihati Nights v3.0.10",
        "body": "### Fixed\n- Faster route loading\n",
        "html_url": "https://github.com/videshkarmakar-hue/Naihati-Nights/releases/tag/v3.0.10",
        "published_at": "2026-10-29T12:00:00Z",
        "draft": false,
        "prerelease": false,
        "assets": [
          {"name": "checksums.txt", "browser_download_url": "https://example.com/sums", "size": 10},
          {"name": "NaihatiNights-3.0.10.apk", "browser_download_url": "https://example.com/app.apk", "size": 29151672}
        ]
      }
    """.trimIndent()

    val release = adapter.fromJson(json)!!
    val info = release.toReleaseInfo("https://fallback")

    assertEquals("v3.0.10", release.tagName)
    assertEquals("3.0.10", info.versionName)
    assertEquals("https://example.com/app.apk", info.apkUrl)
    assertEquals(29151672L, info.apkSizeBytes)
    assertEquals("https://github.com/videshkarmakar-hue/Naihati-Nights/releases/tag/v3.0.10", info.releaseUrl)
    assertTrue(info.hasApk)
  }

  @Test
  fun parseRelease_withoutApkFallsBackToReleasePage() {
    val json = """
      {
        "tag_name": "v3.1.0",
        "body": "",
        "html_url": "",
        "assets": []
      }
    """.trimIndent()

    val info = adapter.fromJson(json)!!.toReleaseInfo("https://github.com/o/r/releases")

    assertEquals("3.1.0", info.versionName)
    assertEquals("https://github.com/o/r/releases", info.releaseUrl)
    assertFalse(info.hasApk)
    assertTrue(info.notes.isNotBlank())
  }

  @Test
  fun releaseNoteLines_cleansMarkdownAndCapsTheList() {
    val body = """
      ### What's new
      - **Faster** route loading
      * Fixed `pandal` map offsets
      + Added offline corridor caching
      - Another change
      - Fifth change
      - Sixth change that should be dropped
    """.trimIndent()

    val lines = releaseNoteLines(body)

    assertEquals(5, lines.size)
    assertEquals("What's new", lines.first())
    assertTrue(lines.contains("Faster route loading"))
    assertTrue(lines.contains("Fixed pandal map offsets"))
    assertFalse(lines.any { it.contains("*") || it.contains("`") })
  }
}
