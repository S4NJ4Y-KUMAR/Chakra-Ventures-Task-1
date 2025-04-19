package com.example.task1.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.task1.R;

public class PermissionsFragment extends Fragment {

    private static final int CAMERA_PERMISSION_CODE = 101;
    private static final int MIC_PERMISSION_CODE = 102;

    private Button cameraPermissionButton;
    private Button micPermissionButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_permissions, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cameraPermissionButton = view.findViewById(R.id.button_camera_permission);
        micPermissionButton = view.findViewById(R.id.button_mic_permission);

        cameraPermissionButton.setOnClickListener(v -> checkAndRequestPermission(Manifest.permission.CAMERA, CAMERA_PERMISSION_CODE, "Camera"));
        micPermissionButton.setOnClickListener(v -> checkAndRequestPermission(Manifest.permission.RECORD_AUDIO, MIC_PERMISSION_CODE, "Microphone"));
    }

    private void checkAndRequestPermission(String permission, int requestCode, String permissionName) {
        if (ContextCompat.checkSelfPermission(requireContext(), permission) == PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getContext(), getString(R.string.permission_already_granted, permissionName), Toast.LENGTH_SHORT).show();
        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        String permissionType = "";
        boolean granted = grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED;

        switch (requestCode) {
            case CAMERA_PERMISSION_CODE:
                permissionType = "Camera";
                break;
            case MIC_PERMISSION_CODE:
                permissionType = "Microphone";
                break;
            default:
                return;
        }

        if (granted) {
            Toast.makeText(getContext(), getString(R.string.permission_granted, permissionType), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), getString(R.string.permission_denied, permissionType), Toast.LENGTH_SHORT).show();
        }
    }
}