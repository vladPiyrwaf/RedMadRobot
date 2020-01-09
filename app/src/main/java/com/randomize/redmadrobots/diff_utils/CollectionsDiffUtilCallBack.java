package com.randomize.redmadrobots.diff_utils;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.randomize.redmadrobots.models.collections.Collection;

import java.util.List;

public class CollectionsDiffUtilCallBack extends DiffUtil.Callback {

    private List<Collection> oldList;
    private List<Collection> newList;

    public CollectionsDiffUtilCallBack(List<Collection> oldList, List<Collection> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        Collection oldCollection = oldList.get(oldItemPosition);
        Collection newCollection = newList.get(oldItemPosition);
        return oldCollection.getId() == newCollection.getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {

        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
