# Android Flashlight

A minimal Android flashlight app built as a hands-on exercise in Android development, camera2 API usage, runtime permission handling, and APK decompilation.

<img src="https://github.com/teduard/android_flashlight/blob/main/apk_release/flashlight_demo.gif" height="450px"/>

---

## What it does

A single-screen app with a toggle button that switches the rear camera torch on and off using the `camera2` API. On first use, it requests the `CAMERA` permission at runtime and retries the toggle once granted.

---

## Implementation notes

**Torch control** is handled via `CameraManager.setTorchMode()`. The app iterates the camera ID list, checks `LENS_FACING_BACK` to identify the rear camera, and calls `setTorchMode(cameraId, isChecked)`. This avoids opening a full camera session just to drive the flash, which is the correct approach on API 23+.

**Runtime permissions** - the `CAMERA` permission is declared in `AndroidManifest.xml` and requested via `ActivityCompat.requestPermissions()` on first button press. The result is handled in `onRequestPermissionsResult`, which resets the toggle state on denial and re-fires the click on grant.

**UI** is a `ConstraintLayout` with a custom-styled `ToggleButton` (pill shape, themed orange) and two `TextView` headers. All strings are externalised to `strings.xml`; colours to `colors.xml`. The background is a density-bucketed drawable covering ldpi through xxxhdpi.

---

## Building

Open the project in Android Studio (Electric Eel or later) and run on a physical device - the emulator has no torch hardware.

Minimum SDK: 24 (Android 7.0)  
Target SDK: 29 (Android 10)

---

## APK decompilation

The repo includes a pre-built debug APK and the toolchain used to inspect it, as an exercise in understanding how Android packages the compiled output.

### Step 1 - convert the DEX bytecode to a JAR

```bash
chmod +x apk_decompiler/dex2jar/*.sh
apk_decompiler/dex2jar/d2j-dex2jar.sh apk_release/flashligh-app-debug.apk -o flashligh-app-debug-dex2jar.jar
```

### Step 2 - open the JAR in the Java decompiler

```bash
java -jar apk_decompiler/jd-gui-1.6.6.jar flashligh-app-debug-dex2jar.jar
```

Expand `com.example.flashlight` in the left panel to browse the decompiled `MainActivity` source.

<img src="https://github.com/teduard/android_flashlight/blob/main/apk_decompiler/jd-ui-example.png" width="600px"/>

---

## Project structure

```
app/src/main/
├── java/com/example/flashlight/
│   └── MainActivity.java       - torch toggle + permission handling
├── res/
│   ├── layout/activity_main.xml
│   ├── drawable/button.xml     - pill-shaped button background
│   ├── values/colors.xml
│   └── values/strings.xml
└── AndroidManifest.xml         - CAMERA permission + hardware feature declarations
apk_release/
│   ├── flashligh-app-debug.apk
│   └── flashlight_demo.gif
apk_decompiler/
    ├── dex2jar/                - DEX-to-JAR conversion toolchain
    └── jd-gui-1.6.6.jar       - Java decompiler GUI
```
