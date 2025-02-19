package com.project.homeplantcare.ui.user_screen.history;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.homeplantcare.databinding.ItemHistoryBinding;
import com.project.homeplantcare.data.models.HistoryItem;
import com.project.homeplantcare.ui.base.BaseAdapter;

import java.util.List;

public class HistoryAdapter extends BaseAdapter<HistoryItem, ItemHistoryBinding> {

    private final HistoryInteractionListener listener;

    public HistoryAdapter(List<HistoryItem> itemList, HistoryInteractionListener listener) {
        super(itemList);
        this.listener = listener;
    }

    @Override
    public ItemHistoryBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemHistoryBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemHistoryBinding> holder, int position, HistoryItem currentItem) {
        ItemHistoryBinding binding = holder.binding;
        binding.setHistoryItem(currentItem);
        Glide.with(holder.binding.getRoot().getContext())
                .load(currentItem.getImageUrl())
                .into(holder.binding.imgPlant);
        binding.btnDelete.setOnClickListener(view -> listener.onDeleteClicked(currentItem));
        binding.layoutHistoryItem.setOnClickListener(view -> listener.onHistoryItemClicked(currentItem));
        binding.executePendingBindings();
    }

    public void updateList(List<HistoryItem> newList) {
        items.clear();
        items.addAll(newList);
        notifyDataSetChanged();
    }

    public interface HistoryInteractionListener {
        void onDeleteClicked(HistoryItem item);

        void onHistoryItemClicked(HistoryItem item);
    }
}
