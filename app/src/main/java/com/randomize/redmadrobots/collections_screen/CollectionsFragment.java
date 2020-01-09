package com.randomize.redmadrobots.collections_screen;

import android.content.Intent;
import android.os.Bundle;
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
import com.randomize.redmadrobots.listeners.OnLoadMoreListener;
import com.randomize.redmadrobots.listeners.RecyclerCollectionClickListener;
import com.randomize.redmadrobots.models.collections.Collection;
import com.randomize.redmadrobots.photos_collection_screen.PhotosCollectionActivity;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CollectionsFragment extends Fragment implements CollectionsContract.CollectionView {

    @BindView(R.id.swipe_container_collection) SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.network_error_view) ConstraintLayout mNetworkErrorView;
    @BindView(R.id.progress_collections) ProgressBar progressBar;
    @BindView(R.id.recyclerViewCollections) RecyclerView recyclerView;

    private ListCollectionsAdapter collectionAdapter;
    private CollectionsPresenterImpl presenter;

    private int pageCount = 1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_collections, container, false);

        ButterKnife.bind(this, view);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        collectionAdapter = new ListCollectionsAdapter(recyclerItemClickListener, recyclerView, getActivity());
        recyclerView.setAdapter(collectionAdapter);

        presenter = new CollectionsPresenterImpl(this, new GetNoticeInstractorCollectionImpl());
        presenter.requestFirstData();

        collectionAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                presenter.requestAddData(++pageCount);
            }
        });

        swipeRefreshLayout.setOnRefreshListener(() -> {
            pageCount = 1;
            collectionAdapter.clear();
            presenter.requestFirstData();
            swipeRefreshLayout.setRefreshing(false);
        });

        return view;
    }

    private RecyclerCollectionClickListener recyclerItemClickListener = collection -> {
        Intent intent = new Intent(getActivity(), PhotosCollectionActivity.class)
                .putExtra("id", collection.getId())
                .putExtra("title", collection.getTitle())
                .putExtra("description", collection.getDescription())
                .putExtra("curated", collection.getUser().getName())
                .putExtra("total_photo", collection.getTotalPhotos())
                .putExtra("url_profile_image", collection.getUser().getProfileImage().getMedium());
        startActivity(intent);
    };

    @Override
    public void setDataToRecyclerView(List<Collection> collections) {
        collectionAdapter.setCollections(collections);
        collectionAdapter.setLoaded();
    }

    @Override
    public void addDataToRecyclerView(List<Collection> collections) {
        collectionAdapter.addColletction(collections);
        collectionAdapter.setLoaded();
    }

    @Override
    public void onResponseFailure(Throwable throwable) {
        showNetworkError();
        hideProgress();
        hideRecyclerView();
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
    public void onDestroy() {
        super.onDestroy();
        presenter.onDestroy();
    }
}
