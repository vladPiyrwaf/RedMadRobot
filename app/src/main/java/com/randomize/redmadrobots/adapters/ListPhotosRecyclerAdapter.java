package com.randomize.redmadrobots.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.models.Photo;
import com.randomize.redmadrobots.view.PhotoDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ListPhotosRecyclerAdapter extends RecyclerView.Adapter<ListPhotosRecyclerAdapter.ViewHolder> {

    private List<Photo> photos;
    private Context mContext;

    public ListPhotosRecyclerAdapter(Context context) {
        this.photos = new ArrayList<>();
        this.mContext = context;
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

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
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
}
