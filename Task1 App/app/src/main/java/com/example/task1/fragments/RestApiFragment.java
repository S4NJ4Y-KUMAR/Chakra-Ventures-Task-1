package com.example.task1.fragments;

import android.os.Bundle;
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

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.task1.MainActivity;
import com.example.task1.R;
import com.example.task1.adapters.AnimeAdapter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RestApiFragment extends Fragment {

    private static final String TAG = "RestApiFragment";

    private RequestQueue requestQueue;
    private AnimeAdapter animeAdapter;
    private RecyclerView animeRecyclerView;
    private Button fetchAnimeButton;
    private ProgressBar progressBar;
    private TextView placeholderText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rest_api, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        requestQueue = Volley.newRequestQueue(requireContext()); // Use requireContext()

        fetchAnimeButton = view.findViewById(R.id.button_fetch_anime);
        animeRecyclerView = view.findViewById(R.id.recycler_view_anime);
        progressBar = view.findViewById(R.id.progress_bar_anime);
        placeholderText = view.findViewById(R.id.text_anime_placeholder);

        setupRecyclerView();

        fetchAnimeButton.setOnClickListener(v -> fetchAnimeList());
    }

    private void setupRecyclerView() {
        animeAdapter = new AnimeAdapter();
        animeRecyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Use getContext()
        animeRecyclerView.setAdapter(animeAdapter);
    }

    private void fetchAnimeList() {
        showLoading(true);
        Log.d(TAG, "Attempting to fetch anime from: " + MainActivity.LOCAL_API_URL);

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET,
                MainActivity.LOCAL_API_URL,
                null,
                response -> {
                    showLoading(false);
                    Log.d(TAG, "Anime Response: " + response.toString());
                    List<String> animeNames = new ArrayList<>();
                    try {
                        for (int i = 0; i < response.length(); i++) {
                            animeNames.add(response.getString(i));
                        }
                        animeAdapter.updateData(animeNames);
                        placeholderText.setVisibility(animeNames.isEmpty() ? View.VISIBLE : View.GONE);
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Parsing error: ", e);
                        Toast.makeText(getContext(), "Error parsing anime data", Toast.LENGTH_SHORT).show();
                        placeholderText.setText("Error parsing data.");
                        placeholderText.setVisibility(View.VISIBLE);
                    }
                },
                error -> {
                    showLoading(false);
                    Log.e(TAG, "Volley error fetching anime: ", error);
                    String errorMsg = error.getMessage() != null ? error.getMessage() : "Unknown error";
                    if (error.networkResponse != null) {
                        errorMsg += " (Status Code: " + error.networkResponse.statusCode + ")";
                    }
                    Toast.makeText(getContext(), getString(R.string.error_fetching_anime, errorMsg), Toast.LENGTH_LONG).show();
                    placeholderText.setText(getString(R.string.error_fetching_anime, errorMsg));
                    placeholderText.setVisibility(View.VISIBLE);
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void showLoading(boolean isLoading) {
        if (isLoading) {
            progressBar.setVisibility(View.VISIBLE);
            animeRecyclerView.setVisibility(View.GONE);
            placeholderText.setVisibility(View.GONE);
            fetchAnimeButton.setEnabled(false);
        } else {
            progressBar.setVisibility(View.GONE);
            animeRecyclerView.setVisibility(View.VISIBLE);
            fetchAnimeButton.setEnabled(true);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}