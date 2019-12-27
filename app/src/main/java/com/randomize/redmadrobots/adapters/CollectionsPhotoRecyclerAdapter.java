package com.randomize.redmadrobots.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.models.Photo;
import com.randomize.redmadrobots.models.collections.Collection;
import com.randomize.redmadrobots.view.CollectionPhotosActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CollectionsPhotoRecyclerAdapter
        extends RecyclerView.Adapter<CollectionsPhotoRecyclerAdapter.ViewHolder> {

    private List<Collection> collections;
    private Context mContext;

    public CollectionsPhotoRecyclerAdapter(Context context) {
        this.collections = new ArrayList<>();
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_collections, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Collection collection = collections.get(position);
        Picasso.get().load(collection.getPreviewPhotos().get(0).getUrls().getSmall()).into(holder.imageView);
        holder.txtTitle.setText(collection.getTitle());
        holder.txtTotalPhoto.setText(collection.getTotalPhotos() + " photos");
        holder.txtCuratedBy.setText("Curated by " + collection.getUser().getName());
    }

    public void setPreviewPhotos(List<Collection> collections) {
        this.collections = collections;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return collections.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView imageView;
        public final TextView txtTitle, txtTotalPhoto, txtCuratedBy;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_preview);
            txtTitle = itemView.findViewById(R.id.txt_title_collection);
            txtTotalPhoto = itemView.findViewById(R.id.txt_total_photos);
            txtCuratedBy = itemView.findViewById(R.id.txt_curated_by);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, CollectionPhotosActivity.class)
                    .putExtra("id", collections.get(getAdapterPosition()).getId())
                    .putExtra("title", collections.get(getAdapterPosition()).getTitle())
                    .putExtra("description", collections.get(getAdapterPosition()).getDescription())
                    .putExtra("curated", collections.get(getAdapterPosition()).getUser().getName())
                    .putExtra("total_photo", collections.get(getAdapterPosition()).getTotalPhotos());
            mContext.startActivity(intent);

        }
    }
}
