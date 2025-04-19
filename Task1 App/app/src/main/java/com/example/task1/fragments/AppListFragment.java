package com.example.task1.fragments;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task1.R;
import com.example.task1.adapters.AppAdapter;
import com.example.task1.model.AppInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AppListFragment extends Fragment {

    private static final String TAG = "AppListFragment";

    private AppAdapter appAdapter;
    private RecyclerView appsRecyclerView;
    private Button fetchAppsButton;
    private ProgressBar progressBar;
    private TextView placeholderText;
    private TextView titleText;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        appsRecyclerView = view.findViewById(R.id.recycler_view_apps);
        fetchAppsButton = view.findViewById(R.id.button_fetch_apps);
        progressBar = view.findViewById(R.id.progress_bar_apps);
        placeholderText = view.findViewById(R.id.text_apps_placeholder);
        titleText = view.findViewById(R.id.text_view_apps_title);

        setupRecyclerView();

        fetchAppsButton.setOnClickListener(v -> loadInstalledApps());

        showLoading(false);
        placeholderText.setVisibility(View.VISIBLE);
        appsRecyclerView.setVisibility(View.GONE);
    }

    private void setupRecyclerView() {
        appAdapter = new AppAdapter();
        appsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        appsRecyclerView.setAdapter(appAdapter);
    }

    private void showLoading(boolean isLoading) {
        mainThreadHandler.post(() -> {
            if (isLoading) {
                progressBar.setVisibility(View.VISIBLE);
                appsRecyclerView.setVisibility(View.GONE);
                placeholderText.setVisibility(View.GONE);
                fetchAppsButton.setEnabled(false);
            } else {
                progressBar.setVisibility(View.GONE);
                fetchAppsButton.setEnabled(true);
            }
        });
    }

    private void loadInstalledApps() {
        showLoading(true);
        Toast.makeText(getContext(), R.string.loading_apps, Toast.LENGTH_SHORT).show();

        executorService.execute(() -> {
            List<AppInfo> installedApps = new ArrayList<>();
            PackageManager pm = requireActivity().getPackageManager();
            String currentAppPackageName = requireContext().getPackageName();
            List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

            try {
                for (ApplicationInfo packageInfo : packages) {
                    if (!packageInfo.packageName.equals(currentAppPackageName) && (packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        String appName = packageInfo.loadLabel(pm).toString();
                        Drawable icon = packageInfo.loadIcon(pm);
                        installedApps.add(new AppInfo(appName, packageInfo.packageName, icon));
                    }
                }

                Collections.sort(installedApps, (app1, app2) ->
                        String.CASE_INSENSITIVE_ORDER.compare(app1.appName, app2.appName));

                updateAppListUI(installedApps);

            } catch (Exception e) {
                Log.e(TAG, "Error loading installed apps: ", e);
                mainThreadHandler.post(() -> {
                    showLoading(false);
                    placeholderText.setText(getString(R.string.error_loading_apps, e.getMessage()));
                    placeholderText.setVisibility(View.VISIBLE);
                    appsRecyclerView.setVisibility(View.GONE);
                    Toast.makeText(getContext(), getString(R.string.error_loading_apps, e.getMessage()), Toast.LENGTH_LONG).show();
                });
            }
        });
    }

    private void updateAppListUI(List<AppInfo> installedApps) {
        mainThreadHandler.post(() -> {
            showLoading(false);
            appAdapter.updateData(installedApps);
            if (installedApps.isEmpty()) {
                placeholderText.setText(R.string.no_apps_found);
                placeholderText.setVisibility(View.VISIBLE);
                appsRecyclerView.setVisibility(View.GONE);
            } else {
                placeholderText.setVisibility(View.GONE);
                appsRecyclerView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}