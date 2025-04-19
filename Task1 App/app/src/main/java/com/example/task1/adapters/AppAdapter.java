package com.example.task1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task1.R;
import com.example.task1.model.AppInfo;

import java.util.ArrayList;
import java.util.List;

public class AppAdapter extends RecyclerView.Adapter<AppAdapter.AppViewHolder> {

    private List<AppInfo> appList = new ArrayList<>();

    @NonNull
    @Override
    public AppViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_app, parent, false); // Use updated layout
        return new AppViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppViewHolder holder, int position) {
        holder.bind(appList.get(position));
    }

    @Override
    public int getItemCount() {
        return appList.size();
    }

    public void updateData(List<AppInfo> newAppList) {
        this.appList.clear();
        if (newAppList != null) {
            this.appList.addAll(newAppList);
        }
        notifyDataSetChanged();
    }

    static class AppViewHolder extends RecyclerView.ViewHolder {
        ImageView iconImageView;
        TextView nameTextView;

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
            iconImageView = itemView.findViewById(R.id.image_view_app_icon);
            nameTextView = itemView.findViewById(R.id.text_view_app_name);
        }

        public void bind(AppInfo appInfo) {
            nameTextView.setText(appInfo.appName);
            if (appInfo.icon != null) {
                iconImageView.setImageDrawable(appInfo.icon);
            } else {
                iconImageView.setImageResource(R.mipmap.ic_launcher);
            }
        }
    }
}