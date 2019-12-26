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
import com.randomize.redmadrobots.models.collections.Collection;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchPhotoRecyclerAdapter extends RecyclerView.Adapter<SearchPhotoRecyclerAdapter.ViewHolder> {

    private List<Photo> photos;

    public SearchPhotoRecyclerAdapter() {
        photos = new ArrayList<>();
    }

    public void updateList(List<Photo> newList) {
        List<Photo> oldList = new ArrayList<>(this.photos);
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new PhotosDiffUtilCallBack(oldList, newList));
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
