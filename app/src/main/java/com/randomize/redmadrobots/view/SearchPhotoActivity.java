package com.randomize.redmadrobots.view;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
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
import com.randomize.redmadrobots.adapters.ListPhotosRecyclerAdapter;
import com.randomize.redmadrobots.api.NetworkService;
import com.randomize.redmadrobots.models.Photo;
import com.randomize.redmadrobots.models.SearchResults;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPhotoActivity extends AppCompatActivity implements TextView.OnEditorActionListener {

    private static final String CLIENT_ID = "e1302c9b61d67d3011bfed17ff854fa7aa0426c2adbe9c9fd18528a073476682";

    private ListPhotosRecyclerAdapter adapter;
    private EditText edSearchPhotos;
    private Toolbar mToolbar;

    private boolean loading = false;
    private int pageCount = 1;
    private static int totalPhoto;
    private String query;

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_photo);

        edSearchPhotos = findViewById(R.id.ed_search_photo);
        progressBar = findViewById(R.id.progress_search_photo);
        mToolbar = findViewById(R.id.toolbar_search);

        edSearchPhotos.setOnEditorActionListener(this);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        RecyclerView recyclerView = findViewById(R.id.recyclerViewSearchPhoto);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ListPhotosRecyclerAdapter(this);
        recyclerView.setAdapter(adapter);

        if (edSearchPhotos.requestFocus() && edSearchPhotos.getText().toString().equals("")) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (adapter.getItemCount() >= totalPhoto) {
                    Toast.makeText(SearchPhotoActivity.this, "No more photos", Toast.LENGTH_SHORT).show();
                } else {
                    if (lastVisibleItemPosition == adapter.getItemCount() - 1 && !loading) {
                        loading = true;
                        addData(++pageCount, query);
                        Log.d("pagecount", "pageCount: " + query);
                    }
                }
            }
        });

    }

    private void addData(final int pageCount, String query) {
        NetworkService.getInstance()
                .getJSONApi()
                .searchPhotos(query, pageCount, 10, null, CLIENT_ID)
                .enqueue(new Callback<SearchResults>() {
                    @Override
                    public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                        List<Photo> photos = response.body().getResults();
                        progressBar.setVisibility(View.GONE);
                        adapter.addPhotos(photos);
                        loading = false;
                    }

                    @Override
                    public void onFailure(Call<SearchResults> call, Throwable t) {

                    }
                });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
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
                                progressBar.setVisibility(View.GONE);
                                adapter.clear();
                                adapter.setPhotos(photos);
                                loading = false;
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
