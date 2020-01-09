package com.randomize.redmadrobots.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.randomize.redmadrobots.diff_utils.PhotosDiffUtilCallBack;
import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.listeners.OnLoadMoreListener;
import com.randomize.redmadrobots.listeners.RecyclerPhotoClickListener;
import com.randomize.redmadrobots.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListPhotosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Photo> photos;
    private OnLoadMoreListener onLoadMoreListener;
    private Activity activity;
    private RecyclerPhotoClickListener clickListener;

    private boolean isLoading;
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;


    public ListPhotosAdapter(RecyclerPhotoClickListener clickListener,
                             RecyclerView recyclerView, Activity activity) {
        this.photos = new ArrayList<>();
        this.clickListener = clickListener;
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

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(photos.get(position));
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return photos == null ? 0 : photos.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder  {

        @BindView(R.id.image_view_photo) ImageView imageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }
}
