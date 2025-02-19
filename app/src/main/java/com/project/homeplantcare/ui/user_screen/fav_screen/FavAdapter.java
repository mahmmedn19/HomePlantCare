package com.project.homeplantcare.ui.user_screen.fav_screen;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.homeplantcare.databinding.ItemFavBinding;
import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.ui.base.BaseAdapter;
import com.project.homeplantcare.ui.base.BaseInteractionListener;

import java.util.List;

public class FavAdapter extends BaseAdapter<PlantItem, ItemFavBinding> {

    private final FavoriteInteractionListener listener;

    public FavAdapter(List<PlantItem> itemList, FavoriteInteractionListener listener) {
        super(itemList);
        this.listener = listener;
    }

    @Override
    public ItemFavBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemFavBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemFavBinding> holder, int position, PlantItem currentItem) {
        ItemFavBinding binding = holder.binding;
        binding.setItem(currentItem);

        // Load Image using Glide
        Glide.with(holder.binding.getRoot().getContext())
                .load(currentItem.getImageResId())
                .into(holder.binding.imgPlant);

        // Handle Favorite Click
        binding.btnFav.setOnClickListener(view -> listener.onFavoriteClicked(currentItem));

        // Handle Show Details Click
        binding.btnShowDetails.setOnClickListener(view -> listener.onDetailsClicked(currentItem));

        binding.executePendingBindings();
    }

    public void updateList(List<PlantItem> item) {
        items.clear();
        items.addAll(item);
        notifyDataSetChanged();
    }

    public interface FavoriteInteractionListener extends BaseInteractionListener {
        void onFavoriteClicked(PlantItem item);

        void onDetailsClicked(PlantItem item);
    }
}
