package com.randomize.redmadrobots.view;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.adapters.ListPhotosAdapter;
import com.randomize.redmadrobots.api.NetworkService;
import com.randomize.redmadrobots.listeners.OnLoadMoreListener;
import com.randomize.redmadrobots.models.Photo;
import com.randomize.redmadrobots.models.SearchResults;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPhotoActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private static final String CLIENT_ID = "e1302c9b61d67d3011bfed17ff854fa7aa0426c2adbe9c9fd18528a073476682";

    private ListPhotosAdapter searchAdapter;
    private EditText edSearchPhotos;
    private Toolbar mToolbar;
    private ProgressBar progressBar;

    private int pageCount = 1;
    private static int totalPhoto;
    private String query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_photo);

        edSearchPhotos = findViewById(R.id.ed_search_photo);
        progressBar = findViewById(R.id.progress_search_photos);
        mToolbar = findViewById(R.id.toolbar_search);

        edSearchPhotos.setOnEditorActionListener(this);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        RecyclerView recyclerView = findViewById(R.id.recyclerViewSearchPhoto);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        searchAdapter = new ListPhotosAdapter( this, recyclerView);
        recyclerView.setAdapter(searchAdapter);

        searchAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d("total_photo", "onloadmore: " + totalPhoto);
                if (searchAdapter.getItemCount() >= totalPhoto && pageCount > 2) {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_more_photos), Toast.LENGTH_SHORT).show();
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    addData(++pageCount, query);
                }
            }
        });

        if (edSearchPhotos.requestFocus() && edSearchPhotos.getText().toString().equals("")) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

    }

    private void addData(final int pageCount, String query) {
        NetworkService.getInstance()
                .getJSONApi()
                .searchPhotos(query, pageCount, 10, null, CLIENT_ID)
                .enqueue(new Callback<SearchResults>() {
                    @Override
                    public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                        List<Photo> photos = response.body().getResults();
                        searchAdapter.addPhotos(photos);
                        searchAdapter.setLoaded();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<SearchResults> call, Throwable t) {

                    }
                });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        progressBar.setVisibility(View.VISIBLE);
        query = edSearchPhotos.getText().toString();
        if (!query.isEmpty()) {
                NetworkService.getInstance()
                        .getJSONApi()
                        .searchPhotos(query, 1, 10, null, CLIENT_ID)
                        .enqueue(new Callback<SearchResults>() {
                            @Override
                            public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                                List<Photo> photos = response.body().getResults();
                                totalPhoto = response.body().getTotal();
                                searchAdapter.clear();
                                searchAdapter.setPhotos(photos);
                                searchAdapter.setLoaded();
                                progressBar.setVisibility(View.GONE);
                                Log.d("total_photo", "onResponse: " + totalPhoto);
                            }

                            @Override
                            public void onFailure(Call<SearchResults> call, Throwable t) {

                            }
                        });
        }
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        return true;
    }
}
