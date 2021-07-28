## Simple Android FlashLight app

The motivation behind this repository is to learn how to create a simple Android application, deploy the obtained .apk on a real device and try to decompile the application in order to see how the packaged source code looks like. Although educational, the application is still useful when light is needed :)

## Turning on the FlashLight

We accomplish this by having the `android.permission.CAMERA` permission, along with `android.hardware.camera` and `android.hardware.camera.autofocus` features added in the AndroidManifest.xml

Next, we continue by building a simple UI in `activity_main.xml` that contains at least a Button having the `toggleButton` id.

The main code can be found in the `setOnClickListener` which:
1. Gets the camera manager: `final CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);`
2. If not provided by the user, requests permission for the camera: `ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.CAMERA}, 2);`
3. Finally, turn on the back facing camera led: `cameraManager.setTorchMode(cameraId, toggleBtn.isChecked());`

## Demo

Below is a fully functional demo of the apk_release/flashligh-app-debug.apk

<img src="https://github.com/teduard/android_flashlight/blob/main/apk_release/flashlight_demo.gif" height="450px"/>

## Decompiling the .apk

The following command allows us to see how the code is packaged:

`chmod +x apk_decompiler/dex2jar/*.sh && java -jar apk_decompiler/jd-gui-1.6.6.jar flashligh-app-debug-dex2jar.jar

We must expand the com.example.flashlight package in order to see the code from MainActivity:
<img src="https://github.com/teduard/android_flashlight/blob/main/apk_decompiler/jd-ui-example.png" width="600px"/>
