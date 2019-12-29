package com.randomize.redmadrobots.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.models.Photo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListPhotosRecyclerAdapter extends RecyclerView.Adapter<ListPhotosRecyclerAdapter.ViewHolder> {

    private List<Photo> photos;

    public  ListPhotosRecyclerAdapter() {
        photos = new ArrayList<>();
    }

    public void updateList(List<Photo> newList) {
        PhotosDiffUtilCallBack photosDiffUtilCallBack = new PhotosDiffUtilCallBack(this.photos, newList);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(photosDiffUtilCallBack);
        diffResult.dispatchUpdatesTo(this);
        setPhotos(newList);
    }

    public void setPhotos(List<Photo> photos) {
        this.photos.addAll(photos);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_photo, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Photo photo = photos.get(position);
        Picasso.get().load(photo.getUrls().getSmall()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }
}
