package com.randomize.redmadrobots.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.listeners.OnLoadMoreListener;
import com.randomize.redmadrobots.models.Photo;
import com.randomize.redmadrobots.view.PhotoDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListPhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Photo> photos;
    private OnLoadMoreListener onLoadMoreListener;
    private Activity activity;

    private boolean isLoading;
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;


    public ListPhotosAdapter(Activity activity, RecyclerView recyclerView) {
        this.photos = new ArrayList<>();
        this.activity = activity;

        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                totalItemCount = layoutManager.getItemCount();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                    if (onLoadMoreListener != null) {
                        onLoadMoreListener.onLoadMore();
                    }
                    isLoading = true;
                }
            }
        });
    }

    public void setLoaded() {
        isLoading = false;
    }

    public void setPhotos(List<Photo> dataToAdd) {
        PhotosDiffUtilCallBack diffUtilCallback =
                new PhotosDiffUtilCallBack(photos, dataToAdd);
        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtilCallback, false);
        photos.addAll(dataToAdd);
        result.dispatchUpdatesTo(this);
    }

    public void addPhotos(List<Photo> dataToAdd) {
//        List<Photo> newData = new ArrayList<>(photos);
//        newData.addAll(dataToAdd);
//        PhotosDiffUtilCallBack diffUtilCallback = new PhotosDiffUtilCallBack(photos, newData);
//        DiffUtil.DiffResult result = DiffUtil.calculateDiff(diffUtilCallback, false);
        photos.addAll(dataToAdd);
        final int position = this.photos.size();
        notifyItemRangeInserted(position, dataToAdd.size());

//        result.dispatchUpdatesTo(this);
    }

    public void clear() {
        photos.clear();
        notifyDataSetChanged();
    }

    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof PhotoViewHolder) {
            PhotoViewHolder viewHolder = (PhotoViewHolder) holder;
            Photo photo = photos.get(position);
            Picasso.get().load(photo.getUrls().getSmall()).into(viewHolder.imageView);
        }
    }


    @Override
    public int getItemCount() {
        return photos == null ? 0 : photos.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView imageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Intent intent = new Intent(activity, PhotoDetailActivity.class)
                    .putExtra("url_image", photos.get(getAdapterPosition()).getUrls().getSmall())
                    .putExtra("description", photos.get(getAdapterPosition()).getDescription())
                    .putExtra("width", photos.get(getAdapterPosition()).getWidth())
                    .putExtra("height", photos.get(getAdapterPosition()).getHeight())
                    .putExtra("url_full_image", photos.get(getAdapterPosition()).getUrls().getFull());
            activity.startActivity(intent);
        }
    }
}
