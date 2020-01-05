package com.randomize.redmadrobots.adapters;

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
import androidx.recyclerview.widget.RecyclerView;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.models.Photo;
import com.randomize.redmadrobots.view.PhotoDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListPhotosRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Photo> photos;
    private Context mContext;

    private static int ITEM = 0;
    private static int LOADING_ELEMENT = 1;

    public ListPhotosRecyclerAdapter(Context context) {
        this.photos = new ArrayList<>();
        this.mContext = context;
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

    public void clear(){
        photos.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        if (viewType == ITEM) {
            return new PhotoViewHolder(layoutInflater.inflate(R.layout.item_photo, parent, false));
        } else if (viewType == LOADING_ELEMENT) {
            return new LoadingViewHolder(layoutInflater.inflate(R.layout.item_progress, parent, false));
        }

        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM) {
            PhotoViewHolder viewHolder = (PhotoViewHolder) holder;
            Photo photo = photos.get(position);
            Picasso.get().load(photo.getUrls().getSmall()).into(viewHolder.imageView);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == photos.size() - 1) ? LOADING_ELEMENT : ITEM;
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    class PhotoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView imageView;

        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        public void bindItem(Photo photo) {
            Picasso.get().load(photo.getUrls().getSmall()).into(imageView);
        }

        @Override
        public void onClick(View view) {
            Log.d("mind", "onClick: " + photos.get(getAdapterPosition()).getUrls().getSmall());
            Intent intent = new Intent(mContext, PhotoDetailActivity.class)
                    .putExtra("url_image", photos.get(getAdapterPosition()).getUrls().getSmall())
                    .putExtra("description", photos.get(getAdapterPosition()).getDescription())
                    .putExtra("width", photos.get(getAdapterPosition()).getWidth())
                    .putExtra("height", photos.get(getAdapterPosition()).getHeight())
                    .putExtra("url_full_image", photos.get(getAdapterPosition()).getUrls().getFull());
            mContext.startActivity(intent);
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            ProgressBar loadBar = itemView.findViewById(R.id.loadmore_progress);
            loadBar.setIndeterminate(true);
        }
    }
}
