package com.randomize.redmadrobots.adapters;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.randomize.redmadrobots.models.Photo;

import java.util.List;

public class PhotosDiffUtilCallback extends DiffUtil.Callback {

    private final List<Photo> oldList;
    private final List<Photo> newList;

    public PhotosDiffUtilCallback(List<Photo> oldList, List<Photo> newList) {
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
        Photo oldPhoto = oldList.get(oldItemPosition);
        Photo newPhoto = newList.get(oldItemPosition);
        return newPhoto.getId().equals(oldPhoto.getId());
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
