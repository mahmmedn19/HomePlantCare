package com.project.homeplantcare.ui.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import java.util.List;

public abstract class BaseAdapter<T, VB extends ViewBinding> extends RecyclerView.Adapter<BaseAdapter.BaseViewHolder<VB>> {

    public List<T> items;

    public BaseAdapter(List<T> items) {
        this.items = items;
    }

    public abstract VB createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent);

    @Override
    public BaseViewHolder<VB> onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        VB binding = createBinding(inflater, parent, false);
        return new BaseViewHolder<>(binding);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<VB> holder, int position) {
        T currentItem = items.get(position);
        onBindViewHolder(holder, position, currentItem);
    }

    public abstract void onBindViewHolder(BaseViewHolder<VB> holder, int position, T currentItem);

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class BaseViewHolder<VB extends ViewBinding> extends RecyclerView.ViewHolder {
        public final VB binding;

        public BaseViewHolder(VB binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void setItems(List<T> newItems) {
        BaseDiffUtil.ItemCheck<T> itemCheck = new BaseDiffUtil.ItemCheck<T>() {
            @Override
            public boolean areItemsSame(T oldItem, T newItem) {
                return BaseAdapter.this.areItems(oldItem, newItem);
            }

            @Override
            public boolean areContentsSame(T oldItem, T newItem) {
                return BaseAdapter.this.areContents(oldItem, newItem);
            }
        };

        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new BaseDiffUtil<>(items, newItems, itemCheck, itemCheck));
        items.clear();
        items.addAll(newItems);
        diffResult.dispatchUpdatesTo(this);
    }

    public boolean areItems(T oldItem, T newItem) {
        return oldItem != null && newItem != null && oldItem.equals(newItem);
    }

    public boolean areContents(T oldItem, T newItem) {
        return true;
    }
    public void clearItems() {
        items.clear();
        notifyDataSetChanged();
    }
    public void addItems(List<T> newItems) {
        clearItems();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

}

