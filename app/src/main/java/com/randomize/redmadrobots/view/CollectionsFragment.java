package com.randomize.redmadrobots.view;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.adapters.ListCollectionsAdapter;
import com.randomize.redmadrobots.api.NetworkService;
import com.randomize.redmadrobots.listeners.OnLoadMoreListener;
import com.randomize.redmadrobots.models.collections.Collection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CollectionsFragment extends Fragment {

    private static final String CLIENT_ID = "e1302c9b61d67d3011bfed17ff854fa7aa0426c2adbe9c9fd18528a073476682";

    private ListCollectionsAdapter collectionAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ConstraintLayout mNetworkErrorView;
    private ConstraintLayout mNoResultView;
    private ProgressBar progressBar;

    private int pageCount = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collections, container, false);

        mNetworkErrorView = view.findViewById(R.id.network_error_view);
        mNoResultView = view.findViewById(R.id.no_results_view);
        progressBar = view.findViewById(R.id.progress_collections);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewCollections);
        swipeRefreshLayout = view.findViewById(R.id.swipe_container_collection);
        recyclerView.setLayoutManager(layoutManager);

        collectionAdapter = new ListCollectionsAdapter(getActivity(), recyclerView);
        recyclerView.setAdapter(collectionAdapter);

        firstLoadData(1);

        collectionAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                progressBar.setVisibility(View.VISIBLE);
                addData(++pageCount);
            }
        });



//        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//
//                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
//
//                if (lastVisibleItemPosition == collectionAdapter.getItemCount() - 1 && !loading) {
//                    Log.d("newlog", "lastVisibleItemPosition: " + lastVisibleItemPosition + "\n"
//                            + "getItemCount: " + (collectionAdapter.getItemCount() - 1));
//                    loading = true;
//                    addData(++pageCount);
//                    Log.d("pagecount", "pageCount: " + pageCount);
//
//                }
//            }
//        });

        swipeRefreshLayout.setOnRefreshListener(()->{
            pageCount = 1;
            collectionAdapter.clear();
            firstLoadData(1);
            swipeRefreshLayout.setRefreshing(false);
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
                        mNetworkErrorView.setVisibility(View.GONE);
                        List<Collection> collections = response.body();
                        collectionAdapter.addColletction(collections);
                        collectionAdapter.setLoaded();
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(Call<List<Collection>> call, Throwable t) {
                        mNetworkErrorView.setVisibility(View.VISIBLE);
                        mNoResultView.setVisibility(View.GONE);
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
                        collectionAdapter.setCollections(collections);
                    }

                    @Override
                    public void onFailure(Call<List<Collection>> call, Throwable t) {
                        Log.d("response", "onFailure: " + t.getMessage());
                    }
                });
    }
}
