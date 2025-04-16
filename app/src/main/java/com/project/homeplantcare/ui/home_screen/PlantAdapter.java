package com.project.homeplantcare.ui.home_screen;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.homeplantcare.R;
import com.project.homeplantcare.data.utils.ImageUtils;
import com.project.homeplantcare.databinding.ItemPlantCardBinding;
import com.project.homeplantcare.data.models.PlantItem;
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
        // Load Image using Glide
        String base64Image = currentItem.getImageResId();
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                Bitmap bitmap = ImageUtils.decodeBase64ToImage(base64Image);
                binding.imgPlant.setImageBitmap(bitmap);
            } catch (IllegalArgumentException e) {
                // Handle invalid Base64 string (set a placeholder image)
                binding.imgPlant.setImageResource(R.drawable.plant_2);
            }
        } else {
            binding.imgPlant.setImageResource(R.drawable.plant_2);
        }
        binding.btnShowDetails.setOnClickListener(view -> listener.onShowDetailsClicked(currentItem));
        binding.imgPlant.setOnClickListener(view -> listener.onPlantClicked(currentItem));
        binding.executePendingBindings();
    }



    public interface HomeInteractionListener extends BaseInteractionListener {
        void onShowDetailsClicked(PlantItem  item);
        void onPlantClicked(PlantItem item);
    }
}