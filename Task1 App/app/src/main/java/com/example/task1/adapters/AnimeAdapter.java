package com.example.task1.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.task1.R;

import java.util.ArrayList;
import java.util.List;

public class AnimeAdapter extends RecyclerView.Adapter<AnimeAdapter.AnimeViewHolder> {

    private List<String> animeList = new ArrayList<>();

    @NonNull
    @Override
    public AnimeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_anime, parent, false);
        return new AnimeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimeViewHolder holder, int position) {
        holder.bind(animeList.get(position));
    }

    @Override
    public int getItemCount() {
        return animeList.size();
    }

    public void updateData(List<String> newAnimeList) {
        this.animeList.clear();
        if (newAnimeList != null) {
            this.animeList.addAll(newAnimeList);
        }
        notifyDataSetChanged();
    }

    static class AnimeViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;

        public AnimeViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.text_view_item_name);
        }

        public void bind(String animeName) {
            nameTextView.setText(animeName);
        }
    }
}