package com.example.flashlight;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ToggleButton;
import android.widget.Toast;
import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;

public class MainActivity extends AppCompatActivity {

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 2:
                ToggleButton flashButton = (ToggleButton)findViewById(R.id.toggleButton);
                flashButton.setChecked(false);
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(getApplicationContext(), "Permission is now granted.", Toast.LENGTH_LONG).show();
                        flashButton.performClick();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Permission is needed to continue!", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context appContext = getApplicationContext();

        ToggleButton flashButton = (ToggleButton)findViewById(R.id.toggleButton);
        flashButton.setOnClickListener(
                new ToggleButton.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ToggleButton toggleBtn = (ToggleButton)view;
                        String toastMessage;

                        final CameraManager cameraManager = (CameraManager) getSystemService(CAMERA_SERVICE);
                        try {
                            for (final String cameraId : cameraManager.getCameraIdList()) {
                                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraId);
                                Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);

                                if (facing != null && facing.equals(CameraCharacteristics.LENS_FACING_BACK)) {
                                    if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                                         ActivityCompat.requestPermissions(MainActivity.this,
                                                new String[]{Manifest.permission.CAMERA}, 2);

                                        return;
                                    }

                                    try {
                                        cameraManager.setTorchMode(cameraId, toggleBtn.isChecked());
                                    } catch (Exception e) {
                                        Toast.makeText(appContext, "Unable to open flash for camera: " + e.getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                        }
                        catch (Exception e){
                            Toast.makeText(appContext, "Camera is no available: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
    }
}
