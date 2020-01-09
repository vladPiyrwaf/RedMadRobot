package com.randomize.redmadrobots.photos_collection_screen;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.adapters.ListPhotosAdapter;
import com.randomize.redmadrobots.collections_screen.CollectionsContract;
import com.randomize.redmadrobots.listeners.RecyclerPhotoClickListener;
import com.randomize.redmadrobots.network.NetworkService;
import com.randomize.redmadrobots.listeners.OnLoadMoreListener;
import com.randomize.redmadrobots.models.Photo;
import com.randomize.redmadrobots.view.PhotoDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PhotosCollectionActivity extends AppCompatActivity implements
        PhotosCollectionContract.PhotosCollectionView {

    @BindView(R.id.txt_description_collection_photo) TextView txtDescription;
    @BindView(R.id.txt_curated) TextView txtCuratedBy;
    @BindView(R.id.recycler_view_collection_photos) RecyclerView recyclerView;
    @BindView(R.id.swipeContainerCollectionDetail) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.image_profile) ImageView profileImage;
    @BindView(R.id.progress_collection_photos) ProgressBar progressBar;
    @BindView(R.id.network_error_view) ConstraintLayout mNetworkErrorView;
    @BindView(R.id.toolbar_collection_detail) Toolbar toolbar;

    private ListPhotosAdapter photosAdapter;
    private PhotosCollectionPresenterImpl presenter;

    private long idCollection;
    private int pageCount = 1;
    private static int totalPhoto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_photos);

        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
        photosAdapter = new ListPhotosAdapter(recyclerPhotoClickListener, recyclerView, this);
        recyclerView.setAdapter(photosAdapter);

        presenter = new PhotosCollectionPresenterImpl(this, new GetNoticePhotosCollectionImpl());
        presenter.requestFirstData(idCollection);

        photosAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (photosAdapter.getItemCount() >= totalPhoto && pageCount > 2) {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_more_photos), Toast.LENGTH_SHORT).show();
                } else {
                    presenter.requestAddData(++pageCount, idCollection);

                }
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            pageCount = 1;
            photosAdapter.clear();
            presenter.requestFirstData(idCollection);
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private RecyclerPhotoClickListener recyclerPhotoClickListener = photo -> {
        Intent intent = new Intent(this, PhotoDetailActivity.class)
                .putExtra("url_image", photo.getUrls().getSmall())
                .putExtra("description", photo.getDescription())
                .putExtra("width", photo.getWidth())
                .putExtra("height", photo.getHeight())
                .putExtra("url_full_image", photo.getUrls().getFull());
        startActivity(intent);
    };

    @Override
    public void setDataToRecyclerView(List<Photo> photos) {
        photosAdapter.setPhotos(photos);
        photosAdapter.setLoaded();
    }

    @Override
    public void addDataToRecyclerView(List<Photo> photos) {
        photosAdapter.addPhotos(photos);
        photosAdapter.setLoaded();
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        showNetworkError();
        hideRecyclerView();
        hideProgress();
    }

    @Override
    public void showRecyclerView() {
        recyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRecyclerView() {
        recyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showNetworkError() {
        mNetworkErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNetworkError() {
        mNetworkErrorView.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }
}
