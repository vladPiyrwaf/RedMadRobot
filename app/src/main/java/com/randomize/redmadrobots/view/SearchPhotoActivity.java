package com.randomize.redmadrobots.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.adapters.ListPhotosRecyclerAdapter;
import com.randomize.redmadrobots.api.NetworkService;
import com.randomize.redmadrobots.models.Photo;
import com.randomize.redmadrobots.models.SearchResults;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPhotoActivity extends AppCompatActivity {

    private static final String CLIENT_ID = "e1302c9b61d67d3011bfed17ff854fa7aa0426c2adbe9c9fd18528a073476682";

    private ListPhotosRecyclerAdapter adapter;
    private EditText edSearchPhotos;
    private Button btnSearchPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_photo);

        edSearchPhotos = findViewById(R.id.ed_search_photo);
        btnSearchPhotos = findViewById(R.id.btn_search_photo);

        RecyclerView recyclerView = findViewById(R.id.recyclerViewSearchPhoto);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new ListPhotosRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        btnSearchPhotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edSearchPhotos.getText() != null) {
                    String query = edSearchPhotos.getText().toString();
                    NetworkService.getInstance()
                            .getJSONApi()
                            .searchPhotos(query, 1, 10, null, CLIENT_ID)
                            .enqueue(new Callback<SearchResults>() {
                                @Override
                                public void onResponse(Call<SearchResults> call, Response<SearchResults> response) {
                                    Log.d("Photos", "Total Results Found " + response.body().getTotal());
                                    List<Photo> photos = response.body().getResults();
                                    adapter.setPhotos(photos);
                                }

                                @Override
                                public void onFailure(Call<SearchResults> call, Throwable t) {

                                }
                            });
                }
            }
        });

    }
}
