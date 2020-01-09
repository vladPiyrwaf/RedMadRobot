package com.randomize.redmadrobots.search_screen;

import android.content.Intent;
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

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.adapters.ListPhotosAdapter;
import com.randomize.redmadrobots.listeners.RecyclerPhotoClickListener;
import com.randomize.redmadrobots.network.NetworkService;
import com.randomize.redmadrobots.listeners.OnLoadMoreListener;
import com.randomize.redmadrobots.models.Photo;
import com.randomize.redmadrobots.models.SearchResults;
import com.randomize.redmadrobots.view.PhotoDetailActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPhotosActivity extends AppCompatActivity implements SearchContract.SearchView,
        TextView.OnEditorActionListener, AdapterView.OnItemSelectedListener {

    @BindView(R.id.recycler_view_search_photo) RecyclerView recyclerView;
    @BindView(R.id.ed_search_photo) EditText edSearchPhotos;
    @BindView(R.id.toolbar_search) Toolbar mToolbar;
    @BindView(R.id.progress_search_photos) ProgressBar progressBar;
    @BindView(R.id.network_error_view) ConstraintLayout mNetworkErrorView;
    @BindView(R.id.no_results_view) ConstraintLayout mNoResultView;

    private ListPhotosAdapter searchAdapter;
    private SearchPhotosPresenterImpl presenter;

    private int pageCount = 1;
    private int totalPhoto;
    private String query;

    private MenuItem mActionClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_photo);

        ButterKnife.bind(this);

        setSupportActionBar(mToolbar);

        edSearchPhotos.setOnEditorActionListener(this);
        edSearchPhotos.setFocusable(true);
        edSearchPhotos.requestFocus();
        edSearchPhotos.addTextChangedListener(textWatcher);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        searchAdapter = new ListPhotosAdapter(recyclerPhotoClickListener, recyclerView, this);
        recyclerView.setAdapter(searchAdapter);

        presenter = new SearchPhotosPresenterImpl(this, new GetNoticeSearchPhotosImpl());

        searchAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.d("mylog", "total: " + totalPhoto + " adapter: " + searchAdapter.getItemCount());
                if (searchAdapter.getItemCount() >= totalPhoto && pageCount > 2) {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_more_photos), Toast.LENGTH_SHORT).show();
                } else {
                    presenter.requestAddData(query, ++pageCount);
                }
            }
        });

        if (edSearchPhotos.requestFocus() && edSearchPhotos.getText().toString().equals("")) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        }

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
        searchAdapter.clear();
        searchAdapter.setPhotos(photos);
        searchAdapter.setLoaded();
    }

    @Override
    public void addDataToRecyclerView(List<Photo> photos) {
        searchAdapter.addPhotos(photos);
        searchAdapter.setLoaded();
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        showNetworkError();
        hideRecyclerView();
        hideProgress();
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        query = edSearchPhotos.getText().toString();
        if (!query.isEmpty()) {
            presenter.requestFirstData(query);
        }
        return false;
    }

    @Override
    public void setTotalPhoto(int totalPhoto) {
        this.totalPhoto = totalPhoto;
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
    public void showNoResultView() {
        mNoResultView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoResultView() {
        mNoResultView.setVisibility(View.GONE);
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
            if (mActionClear != null) {
                if (edSearchPhotos.getText().toString().isEmpty()) {
                    mActionClear.setVisible(false);
                } else {
                    mActionClear.setVisible(true);
                }
            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
