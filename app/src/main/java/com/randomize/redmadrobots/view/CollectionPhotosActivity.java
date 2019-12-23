package com.randomize.redmadrobots.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.adapters.SearchPhotoRecyclerAdapter;
import com.randomize.redmadrobots.api.NetworkService;
import com.randomize.redmadrobots.models.Photo;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionPhotosActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "e1302c9b61d67d3011bfed17ff854fa7aa0426c2adbe9c9fd18528a073476682";

    private TextView txtDescription, txtCuratedBy;
    private RecyclerView recyclerView;
    private SearchPhotoRecyclerAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_photos);

        txtDescription = findViewById(R.id.txt_description_collection_photo);
        txtCuratedBy = findViewById(R.id.txt_curated);
        recyclerView = findViewById(R.id.recycler_view_collection_photos);

        long idCollection = getIntent().getLongExtra("id", 1);
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String curated = getIntent().getStringExtra("curated");

        txtDescription.setText(description);
        txtCuratedBy.setText(curated);

        adapter = new SearchPhotoRecyclerAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        NetworkService.getInstance()
                .getJSONApi()
                .getCollectionPhotos(idCollection, CLIENT_ID)
                .enqueue(new Callback<List<Photo>>() {
                    @Override
                    public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                        List<Photo> photos = response.body();
                        adapter.setPhotos(photos);
                    }

                    @Override
                    public void onFailure(Call<List<Photo>> call, Throwable t) {

                    }
                });
    }
}
