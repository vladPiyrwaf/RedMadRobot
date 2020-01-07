package com.randomize.redmadrobots.view;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
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

public class SearchPhotoActivity extends AppCompatActivity implements TextView.OnEditorActionListener, AdapterView.OnItemSelectedListener {

    private static final String CLIENT_ID = "e1302c9b61d67d3011bfed17ff854fa7aa0426c2adbe9c9fd18528a073476682";

    private RecyclerView recyclerView;
    private ListPhotosAdapter searchAdapter;
    private EditText edSearchPhotos;
    private Toolbar mToolbar;
    private ProgressBar progressBar;
    private ConstraintLayout mNetworkErrorView, mNoResultView;

    private int pageCount = 1;
    private static int totalPhoto;
    private String query;

    private MenuItem mActionClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_photo);

        edSearchPhotos = findViewById(R.id.ed_search_photo);
        progressBar = findViewById(R.id.progress_search_photos);
        mToolbar = findViewById(R.id.toolbar_search);
        mNetworkErrorView = findViewById(R.id.network_error_view);
        mNoResultView = findViewById(R.id.no_results_view);

        edSearchPhotos.setOnEditorActionListener(this);
        edSearchPhotos.setFocusable(true);
        edSearchPhotos.requestFocus();
        edSearchPhotos.addTextChangedListener(textWatcher);

        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        recyclerView = findViewById(R.id.recyclerViewSearchPhoto);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        searchAdapter = new ListPhotosAdapter(this, recyclerView);
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
                        mNetworkErrorView.setVisibility(View.GONE);
                        mNoResultView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<SearchResults> call, Throwable t) {
                        mNetworkErrorView.setVisibility(View.VISIBLE);
                    }
                });
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        recyclerView.setVisibility(View.VISIBLE);
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
                            mNetworkErrorView.setVisibility(View.GONE);
                            mNoResultView.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(Call<SearchResults> call, Throwable t) {
                            mNetworkErrorView.setVisibility(View.VISIBLE);

                        }
                    });
        }
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search, menu);
        mActionClear = menu.getItem(0);

        if (!edSearchPhotos.getText().toString().isEmpty())
            mActionClear.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
            onBackPressed();
        else if (item.getItemId() == R.id.action_clear_text)
            edSearchPhotos.setText(null);
        return true;
    }

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (mActionClear != null){
                if (edSearchPhotos.getText().toString().isEmpty()){
                    mActionClear.setVisible(false);
                } else {
                    mActionClear.setVisible(true);
                }
            }
        }
    };
}
