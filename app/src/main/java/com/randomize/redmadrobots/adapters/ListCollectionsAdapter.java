package com.randomize.redmadrobots.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.randomize.redmadrobots.diff_utils.CollectionsDiffUtilCallBack;
import com.randomize.redmadrobots.R;
import com.randomize.redmadrobots.listeners.OnLoadMoreListener;
import com.randomize.redmadrobots.listeners.RecyclerCollectionClickListener;
import com.randomize.redmadrobots.models.collections.Collection;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListCollectionsAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Collection> collections;
    private OnLoadMoreListener onLoadMoreListener;
    private RecyclerCollectionClickListener recyclerItemClickListener;
    private Activity activity;

    private boolean isLoading;
    private int visibleThreshold = 10;
    private int lastVisibleItem, totalItemCount;

    public ListCollectionsAdapter(RecyclerCollectionClickListener recyclerItemClickListener,
                                  RecyclerView recyclerView, Activity activity) {
        this.collections = new ArrayList<>();
        this.recyclerItemClickListener = recyclerItemClickListener;
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
            viewHolder.txtTotalPhoto.setText(String.format(activity.getString(R.string.collection_total_photos), collection.getTotalPhotos()));
            viewHolder.txtCuratedBy.setText(String.format(activity.getString(R.string.collection_curated_by), collection.getUser().getName()));

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerItemClickListener.onItemClick(collections.get(position));
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return collections == null ? 0 : collections.size();
    }

    class CollectionViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.image_preview) ImageView imageView;
        @BindView(R.id.txt_curated_by) TextView txtCuratedBy;
        @BindView(R.id.txt_total_photos) TextView txtTotalPhoto;
        @BindView(R.id.txt_title_collection) TextView txtTitle;

        public CollectionViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
