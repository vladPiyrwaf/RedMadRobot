package com.randomize.redmadrobots.adapters;

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
import androidx.recyclerview.widget.RecyclerView;

import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.models.collections.Collection;
import com.randomize.redmadrobots.view.CollectionPhotosActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CollectionsPhotoRecyclerAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Collection> collections;
    private Context mContext;

    private static int ITEM = 0;
    private static int LOADING_ELEMENT = 1;

    public CollectionsPhotoRecyclerAdapter(Context context) {
        this.collections = new ArrayList<>();
        this.mContext = context;
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

    public void clear(){
        this.collections.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        if (viewType == ITEM) {
            return new CollectionViewHolder(layoutInflater.inflate(R.layout.item_collections, parent, false));
        } else if (viewType == LOADING_ELEMENT) {

            return new LoadingViewHolder(layoutInflater.inflate(R.layout.item_progress, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == ITEM) {
            CollectionViewHolder viewHolder = (CollectionViewHolder) holder;
            Collection collection = collections.get(position);
            Picasso.get().load(collection.getPreviewPhotos().get(0).getUrls().getSmall()).into(viewHolder.imageView);
            viewHolder.txtTitle.setText(collection.getTitle());
            viewHolder.txtTotalPhoto.setText(collection.getTotalPhotos() + " photos");
            viewHolder.txtCuratedBy.setText("Curated by " + collection.getUser().getName());
        } else {
            ((LoadingViewHolder) holder).progressBar.setIndeterminate(true);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == collections.size() - 1) ? LOADING_ELEMENT : ITEM;
    }

    @Override
    public int getItemCount() {
        return collections.size();
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
            Intent intent = new Intent(mContext, CollectionPhotosActivity.class)
                    .putExtra("id", collections.get(getAdapterPosition()).getId())
                    .putExtra("title", collections.get(getAdapterPosition()).getTitle())
                    .putExtra("description", collections.get(getAdapterPosition()).getDescription())
                    .putExtra("curated", collections.get(getAdapterPosition()).getUser().getName())
                    .putExtra("total_photo", collections.get(getAdapterPosition()).getTotalPhotos())
                    .putExtra("url_profile_image", collections.get(getAdapterPosition()).getUser().getProfileImage().getMedium());
            mContext.startActivity(intent);
        }
    }

    class LoadingViewHolder extends RecyclerView.ViewHolder {
        public final ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.loadmore_progress);
            progressBar.setIndeterminate(true);
        }
    }
}
