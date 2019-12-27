package com.randomize.redmadrobots.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.adapters.ListPhotosRecyclerAdapter;
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
    private ListPhotosRecyclerAdapter adapter;

    private long idCollection;
    private boolean loading = false;
    private int pageCount = 2;
    private static int totalPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_photos);

        txtDescription = findViewById(R.id.txt_description_collection_photo);
        txtCuratedBy = findViewById(R.id.txt_curated);
        recyclerView = findViewById(R.id.recycler_view_collection_photos);

        idCollection = getIntent().getLongExtra("id", 1);
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String curated = getIntent().getStringExtra("curated");
        totalPhoto = getIntent().getIntExtra("total_photo", 0);


        txtDescription.setText(description);
        txtCuratedBy.setText(curated);

        adapter = new ListPhotosRecyclerAdapter(this);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        fetchData(1);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (adapter.getItemCount() >= totalPhoto) {
                    Toast.makeText(CollectionPhotosActivity.this, "No more photos", Toast.LENGTH_SHORT).show();
                } else {
                    if (lastVisibleItemPosition == adapter.getItemCount() - 1) {
                        loading = true;
                        addData(pageCount++);
                    }
                }
            }
        });

    }

    private void fetchData(final int pageCount) {
        NetworkService.getInstance()
                .getJSONApi()
                .getCollectionPhotos(idCollection, pageCount, 10, CLIENT_ID)
                .enqueue(new Callback<List<Photo>>() {
                    @Override
                    public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                        List<Photo> photos = response.body();
                        adapter.setPhotos(photos);
                        loading = false;
                    }

                    @Override
                    public void onFailure(Call<List<Photo>> call, Throwable t) {

                    }
                });
    }

    private void addData(final int pageCount) {
        NetworkService.getInstance()
                .getJSONApi()
                .getCollectionPhotos(idCollection, pageCount, 10, CLIENT_ID)
                .enqueue(new Callback<List<Photo>>() {
                    @Override
                    public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                        Log.d("pages", "Page is number: " + pageCount);
                        List<Photo> photos = response.body();
                        adapter.addPhotos(photos);
                        loading = false;
                        Log.d("pages", "Page is number: " + pageCount);
                    }

                    @Override
                    public void onFailure(Call<List<Photo>> call, Throwable t) {

                    }
                });
    }
}
