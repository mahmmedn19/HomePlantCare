package com.project.homeplantcare.ui.home_screen;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.homeplantcare.databinding.ItemPlantCardBinding;
import com.project.homeplantcare.models.PlantItem;
import com.project.homeplantcare.ui.base.BaseAdapter;
import com.project.homeplantcare.ui.base.BaseInteractionListener;

import java.util.List;


public class PlantAdapter extends BaseAdapter<PlantItem, ItemPlantCardBinding> {

    private final HomeInteractionListener listener;

    public PlantAdapter(List<PlantItem> itemList, HomeInteractionListener listener) {
        super(itemList);
        this.listener = listener;
    }


    @Override
    public ItemPlantCardBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemPlantCardBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemPlantCardBinding> holder, int position, PlantItem currentItem) {
        ItemPlantCardBinding binding = holder.binding;
        binding.setItem(currentItem);
        binding.favIcon.setOnClickListener(view -> listener.onFavoriteClicked(currentItem));
        binding.imgPlant.setOnClickListener(view -> listener.onCartClicked(currentItem));
        binding.executePendingBindings();
    }



    public interface HomeInteractionListener extends BaseInteractionListener {
        void onFavoriteClicked(PlantItem  item);
        void onCartClicked(PlantItem item);
    }
}