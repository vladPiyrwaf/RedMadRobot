package com.randomize.redmadrobots.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.listeners.OnLoadMoreListener;
import com.randomize.redmadrobots.models.collections.Collection;
import com.randomize.redmadrobots.view.CollectionPhotosActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListCollectionsAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Collection> collections;
    private Activity activity;
    private OnLoadMoreListener onLoadMoreListener;

    private boolean isLoading;
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;

    public ListCollectionsAdapter(Activity activity, RecyclerView recyclerView) {
        this.collections = new ArrayList<>();
        this.activity = activity;

        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null){
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setLoaded(){
        isLoading = false;
    }

    public void setCollections(List<Collection> dataToAdd) {
        CollectionsDiffUtilCallBack diffUtilCallBack =
                new CollectionsDiffUtilCallBack(collections, dataToAdd);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtilCallBack, true);
        collections.addAll(dataToAdd);
        result.dispatchUpdatesTo(this);
    }

    public void addColletction(List<Collection> dataToAdd) {
        this.collections.addAll(dataToAdd);
        final int position = this.collections.size();
        notifyItemRangeInserted(position, dataToAdd.size());
    }

    public void clear() {
        this.collections.clear();
        notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener){
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return new CollectionViewHolder(layoutInflater.inflate(R.layout.item_collections, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof CollectionViewHolder) {
            CollectionViewHolder viewHolder = (CollectionViewHolder) holder;
            Collection collection = collections.get(position);
            Picasso.get().load(collection.getPreviewPhotos().get(0).getUrls().getSmall()).into(viewHolder.imageView);
            viewHolder.txtTitle.setText(collection.getTitle());
            viewHolder.txtTotalPhoto.setText(collection.getTotalPhotos() + " photos");
            viewHolder.txtCuratedBy.setText("Curated by " + collection.getUser().getName());
        }
    }

    @Override
    public int getItemCount() {
        return collections == null ? 0 : collections.size();
    }

    class CollectionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView imageView;
        public final TextView txtTitle, txtTotalPhoto, txtCuratedBy;

        public CollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_preview);
            txtTitle = itemView.findViewById(R.id.txt_title_collection);
            txtTotalPhoto = itemView.findViewById(R.id.txt_total_photos);
            txtCuratedBy = itemView.findViewById(R.id.txt_curated_by);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(activity, CollectionPhotosActivity.class)
                    .putExtra("id", collections.get(getAdapterPosition()).getId())
                    .putExtra("title", collections.get(getAdapterPosition()).getTitle())
                    .putExtra("description", collections.get(getAdapterPosition()).getDescription())
                    .putExtra("curated", collections.get(getAdapterPosition()).getUser().getName())
                    .putExtra("total_photo", collections.get(getAdapterPosition()).getTotalPhotos())
                    .putExtra("url_profile_image", collections.get(getAdapterPosition()).getUser().getProfileImage().getMedium());
            activity.startActivity(intent);
        }
    }
}
