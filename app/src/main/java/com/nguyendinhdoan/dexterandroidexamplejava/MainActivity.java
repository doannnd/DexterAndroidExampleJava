package com.nguyendinhdoan.dexterandroidexamplejava;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final String SETTING_DIALOG_TAG = "SETTING_DIALOG_TAG";
    private static final String TAG = "MAIN_ACTIVITY";

    private Button singlePermissionButton;
    private Button multiplePermissionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        addEvents();
    }

    private void initViews() {
        singlePermissionButton = findViewById(R.id.single_permission_button);
        multiplePermissionButton = findViewById(R.id.multiple_permission_button);
    }

    private void addEvents() {
        singlePermissionButton.setOnClickListener(this);
        multiplePermissionButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.single_permission_button: {
                requestCameraPermission();
                break;
            }
            case R.id.multiple_permission_button: {
                requestStoragePermission();
            }
        }
    }

    /*
    * Request camera permission
    * This uses single permission model form dexter
    * One the permission granted open the camera
    * On permanent denial open setting dialog
    *
    * */
    private void requestCameraPermission() {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        openCamera();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        if (response.isPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                        Toast.makeText(MainActivity.this, "permission denied", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void openCamera() {
        Intent intentCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intentCamera, CAMERA_REQUEST_CODE);
    }

    private void showSettingsDialog() {
        String settingsDialogTitle = getString(R.string.settings_dialog_title);
        SettingsDialogFragment settingsDialogFragment = SettingsDialogFragment.newInstance(settingsDialogTitle);
        settingsDialogFragment.show(getSupportFragmentManager(), SETTING_DIALOG_TAG);
    }

    /*
     * Requesting multiple permissions (storage and location ) at one
     * This uses multiple permission model form dexter
     * On permanent denial opens dialog settings
     * */
    private void requestStoragePermission() {
        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(MainActivity.this, "All permission granted",
                                    Toast.LENGTH_SHORT).show();
                        }

                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Log.e(TAG, "request multiple permission error: " + error);
                    }
                })
                .onSameThread()
                .check();
    }
}
