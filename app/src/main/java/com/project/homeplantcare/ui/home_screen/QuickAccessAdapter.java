package com.project.homeplantcare.ui.home_screen;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.homeplantcare.databinding.ItemQuickAccessCardBinding;
import com.project.homeplantcare.models.QuickAccessItem;
import com.project.homeplantcare.ui.base.BaseAdapter;
import com.project.homeplantcare.ui.base.BaseInteractionListener;

import java.util.List;


public class QuickAccessAdapter extends BaseAdapter<QuickAccessItem, ItemQuickAccessCardBinding> {

    private final HomeInteractionListener listener;

    public QuickAccessAdapter(List<QuickAccessItem> itemList, HomeInteractionListener listener) {
        super(itemList);
        this.listener = listener;
    }


    @Override
    public ItemQuickAccessCardBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemQuickAccessCardBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemQuickAccessCardBinding> holder, int position, QuickAccessItem currentItem) {
        ItemQuickAccessCardBinding binding = holder.binding;
        binding.setItem(currentItem);
        Glide.with(binding.getRoot().getContext())
                .load(currentItem.getIconResId()) // Load the resource ID or URL
                .into(binding.iconQuickAccess); // Target ImageView
        binding.textQuickAccess.setText(currentItem.getLabel());
        binding.quickAccessLayout.setOnClickListener(view -> listener.onCartClicked(currentItem));
        binding.executePendingBindings();
    }


    public interface HomeInteractionListener extends BaseInteractionListener {
        void onCartClicked(QuickAccessItem item);
    }
}