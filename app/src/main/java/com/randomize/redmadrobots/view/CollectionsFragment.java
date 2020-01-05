package com.randomize.redmadrobots.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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

public class CollectionsFragment extends Fragment {

    private static final String CLIENT_ID = "e1302c9b61d67d3011bfed17ff854fa7aa0426c2adbe9c9fd18528a073476682";

    private CollectionsPhotoRecyclerAdapter photoRecyclerAdapter;
    private ProgressBar progressBar;

    private boolean loading = false;
    private int pageCount = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_collections, container, false);

        progressBar = view.findViewById(R.id.progress_collections);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCollections);
        recyclerView.setLayoutManager(layoutManager);

        photoRecyclerAdapter = new CollectionsPhotoRecyclerAdapter(getActivity());
        recyclerView.setAdapter(photoRecyclerAdapter);

        firstLoadData(1);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

                if (lastVisibleItemPosition == photoRecyclerAdapter.getItemCount() - 1 && !loading) {
                    Log.d("newlog", "lastVisibleItemPosition: " + lastVisibleItemPosition + "\n"
                            + "getItemCount: " + (photoRecyclerAdapter.getItemCount() - 1));
                    loading = true;
                    addData(++pageCount);
                    Log.d("pagecount", "pageCount: " + pageCount);

                }
            }
        });

        return view;
    }

    private void addData(int pageCount){
        NetworkService.getInstance()
                .getJSONApi()
                .getPhotoCollections(pageCount, 10, CLIENT_ID)
                .enqueue(new Callback<List<Collection>>() {
                    @Override
                    public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response) {
                        List<Collection> collections = response.body();
                        progressBar.setVisibility(View.GONE);
                        photoRecyclerAdapter.addColletction(collections);
                        loading = false;
                    }

                    @Override
                    public void onFailure(Call<List<Collection>> call, Throwable t) {

                    }
                });
    }

    private void firstLoadData(int pageCount) {
        NetworkService.getInstance()
                .getJSONApi()
                .getPhotoCollections(pageCount, 10, CLIENT_ID)
                .enqueue(new Callback<List<Collection>>() {
                    @Override
                    public void onResponse(Call<List<Collection>> call, Response<List<Collection>> response) {
                        List<Collection> collections = response.body();
                        photoRecyclerAdapter.setCollections(collections);
                        loading = false;
                    }

                    @Override
                    public void onFailure(Call<List<Collection>> call, Throwable t) {
                        Log.d("response", "onFailure: " + t.getMessage());
                    }
                });
    }
}
