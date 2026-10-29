<div align="center">
<img width="1200" height="475" alt="GHBanner" src="https://ai.google.dev/static/site-assets/images/share-ais-513315318.png" />
</div>

# Run and deploy your AI Studio app

This contains everything you need to run your app locally.

View your app in AI Studio: https://ai.studio/apps/8a392362-3207-4c15-baa4-86b327ad18a4

## Run Locally

**Prerequisites:**  [Android Studio](https://developer.android.com/studio)


1. Open Android Studio
2. Select **Open** and choose the directory containing this project
3. Allow Android Studio to fix any incompatibilities as it imports the project.
4. Create a file named `.env` in the project directory and set `GEMINI_API_KEY` in that file to your Gemini API key (see `.env.example` for an example)
5. Remove this line from the app's `build.gradle.kts` file: `signingConfig = signingConfigs.getByName("debugConfig")`
6. Run the app on an emulator or physical device
7. If you have already published your app in AI Studio, please [request upload key reset](https://support.google.com/android-developer/answer/9842756#zippy=%2Crequest-an-upload-key-reset) in Google Play Console.

## Publishing an update (GitHub Releases)

The app updates itself from **GitHub Releases**, not the Play Store. On launch (and whenever it
returns to the foreground) it asks
`https://api.github.com/repos/videshkarmakar-hue/Naihati-Nights/releases/latest`, compares the
release tag with the installed `versionName`, and shows an **Update available** dialog. Tapping
**Update now** downloads the attached APK and hands it to the system installer.

The same channel is reachable by hand: **Sidebar -> Check Updates** runs the lookup immediately,
marks the entry with a `NEW` badge while a newer build exists, and keeps the big
**Update now / Install update** button in one place.

To ship a new version, tag it and push the tag:

```bash
git tag v3.0.10
git push origin v3.0.10
```

That is the whole release process:

1. The tag triggers [`.github/workflows/release.yml`](.github/workflows/release.yml).
2. The workflow builds `app-debug.apk` with `-PnaihatiVersionName=3.0.10`
   (`versionCode` is derived from the version name, `3.0.10` -> `30010000`).
3. It publishes a GitHub release named `Naihati Nights v3.0.10` with
   `NaihatiNights-3.0.10.apk` attached.
4. Every installed copy sees the newer tag on its next check (throttled to once every 6 hours,
   toggleable in **Sidebar -> Check Updates -> Auto-check on launch**) and offers the update.

You can also run the workflow manually from **Actions -> Release APK -> Run workflow** and type a
version number.

### Signing rule (important)

Android refuses to install an update signed with a different key than the installed app. The
workflow therefore signs with the committed `debug.keystore`, the same key used by
`signingConfigs.debugConfig` in `app/build.gradle.kts`. Keep that file in the repo, and do not
switch existing users to a different keystore — every earlier install would then fail with
`INSTALL_FAILED_UPDATE_INCOMPATIBLE` and need a manual uninstall.

Because the APK comes from outside the Play Store, Android asks once for permission to install
apps from this source. The update dialog links straight to that toggle.

Version tags are the source of truth. If a release page has no `.apk` asset, the dialog falls back
to opening the release page in a browser instead of downloading.
