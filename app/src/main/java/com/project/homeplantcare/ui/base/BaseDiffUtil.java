package com.project.homeplantcare.ui.base;


import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class BaseDiffUtil<T> extends DiffUtil.Callback {
    private final List<T> oldList;
    private final List<T> newList;
    private final ItemCheck<T> checkIfSameItem;
    private final ItemCheck<T> checkIfSameContent;

    public interface ItemCheck<T> {
        boolean areItemsSame(T oldItem, T newItem);
        boolean areContentsSame(T oldItem, T newItem);
    }

    public BaseDiffUtil(
            List<T> oldList,
            List<T> newList,
            ItemCheck<T> checkIfSameItem,
            ItemCheck<T> checkIfSameContent
    ) {
        this.oldList = oldList;
        this.newList = newList;
        this.checkIfSameItem = checkIfSameItem;
        this.checkIfSameContent = checkIfSameContent;
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
        return checkIfSameItem.areItemsSame(oldList.get(oldItemPosition), newList.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return checkIfSameContent.areContentsSame(oldList.get(oldItemPosition), newList.get(newItemPosition));
    }
}

