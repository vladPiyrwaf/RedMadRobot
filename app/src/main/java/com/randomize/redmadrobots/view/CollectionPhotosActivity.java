package com.randomize.redmadrobots.view;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.adapters.ListPhotosAdapter;
import com.randomize.redmadrobots.api.NetworkService;
import com.randomize.redmadrobots.listeners.OnLoadMoreListener;
import com.randomize.redmadrobots.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionPhotosActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "e1302c9b61d67d3011bfed17ff854fa7aa0426c2adbe9c9fd18528a073476682";

    private TextView txtDescription, txtCuratedBy;
    private RecyclerView recyclerView;
    private ListPhotosAdapter photosAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ImageView profileImage;
    private ProgressBar progressBar;

    private long idCollection;
    private boolean loading = false;
    private int pageCount = 1;
    private static int totalPhoto;

    private Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_photos);

        toolbar = findViewById(R.id.toolbar_collection_detail);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtDescription = findViewById(R.id.txt_description_collection_photo);
        txtCuratedBy = findViewById(R.id.txt_curated);
        recyclerView = findViewById(R.id.recycler_view_collection_photos);
        swipeRefreshLayout = findViewById(R.id.swipeContainerCollectionDetail);
        profileImage = findViewById(R.id.image_profile);
        progressBar = findViewById(R.id.progress_collection_photos);

        idCollection = getIntent().getLongExtra("id", 1);
        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String curated = getIntent().getStringExtra("curated");
        String urlProfileImage = getIntent().getStringExtra("url_profile_image");
        totalPhoto = getIntent().getIntExtra("total_photo", 0);

        setTitle(title);

        txtDescription.setText(description);
        txtCuratedBy.setText(curated);
        Picasso.get().load(urlProfileImage).into(profileImage);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        photosAdapter = new ListPhotosAdapter(this, recyclerView);
        recyclerView.setAdapter(photosAdapter);

        fetchData(1);

        photosAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (photosAdapter.getItemCount() >= totalPhoto && pageCount > 2) {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_more_photos), Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    addData(++pageCount);

                }
            }
        });


//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
//
//                if (photosA
//              dapter.getItemCount() >= totalPhoto) {
//                    Toast.makeText(CollectionPhotosActivity.this, "No more photos", Toast.LENGTH_SHORT).show();
//                } else {
//                    if (lastVisibleItemPosition >= photosA
//                  dapter.getItemCount() - 2 && !loading) {
//                        Log.d("newlog", "lastVisibleItemPosition: " + lastVisibleItemPosition + "\n"
//                                + "getItemCount: " + (photosA
//                              dapter.getItemCount() - 1));
//                        loading = true;
//                        showProgressView();
//                        addData(++pageCount);
//                        Log.d("pagecount", "pageCount: " + pageCount);
//                    }
//                }
//            }
//        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            pageCount = 1;
            photosAdapter.clear();
            fetchData(1);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void fetchData(final int pageCount) {
        progressBar.setVisibility(View.VISIBLE);
        NetworkService.getInstance()
                .getJSONApi()
                .getCollectionPhotos(idCollection, pageCount, 10, CLIENT_ID)
                .enqueue(new Callback<List<Photo>>() {
                    @Override
                    public void onResponse(Call<List<Photo>> call, Response<List<Photo>> response) {
                        List<Photo> photos = response.body();
                        photosAdapter.setPhotos(photos);
                        photosAdapter.setLoaded();
                        progressBar.setVisibility(View.GONE);
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

                        List<Photo> photos = response.body();
                        photosAdapter.addPhotos(photos);
                        photosAdapter.setLoaded();
                        progressBar.setVisibility(View.GONE);
                        Log.d("pages", "Page is number: " + pageCount);
                    }

                    @Override
                    public void onFailure(Call<List<Photo>> call, Throwable t) {

                    }
                });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }
}
