package com.randomize.redmadrobots.view;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.adapters.CollectionsPhotoRecyclerAdapter;
import com.randomize.redmadrobots.api.NetworkService;
import com.randomize.redmadrobots.models.collections.Collection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionsActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "e1302c9b61d67d3011bfed17ff854fa7aa0426c2adbe9c9fd18528a073476682";

    private CollectionsPhotoRecyclerAdapter photoRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collections);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewCollections);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        photoRecyclerAdapter = new CollectionsPhotoRecyclerAdapter(this);
        recyclerView.setAdapter(photoRecyclerAdapter);

        NetworkService.getInstance()
                .getJSONApi()
                .getPhotoCollections(1, 10, CLIENT_ID)
                .enqueue(new Callback<List<Collection>>() {
                    @Override
                    public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response) {
                        List<Collection> collections = response.body();
                        photoRecyclerAdapter.setPreviewPhotos(collections);
                    }

                    @Override
                    public void onFailure(Call<List<Collection>> call, Throwable t) {
                        Log.d("response", "onFailure: " + t.getMessage());
                    }
                });
    }
}
