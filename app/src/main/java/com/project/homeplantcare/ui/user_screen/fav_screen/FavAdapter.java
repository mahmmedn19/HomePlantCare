package com.project.homeplantcare.ui.user_screen.fav_screen;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.utils.ImageUtils;
import com.project.homeplantcare.databinding.ItemFavBinding;
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
        if (currentItem.getImageResId() != null && !currentItem.getImageResId().isEmpty()) {
            Bitmap bitmap = ImageUtils.decodeBase64ToImage(currentItem.getImageResId());
            binding.imgPlant.setImageBitmap(bitmap);
        }

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
