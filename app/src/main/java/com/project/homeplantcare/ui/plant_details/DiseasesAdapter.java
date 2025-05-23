package com.project.homeplantcare.ui.plant_details;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.utils.ImageUtils;
import com.project.homeplantcare.databinding.ItemDiseaseCardBinding;
import com.project.homeplantcare.ui.base.BaseAdapter;
import com.project.homeplantcare.ui.base.BaseInteractionListener;

import java.util.ArrayList;
import java.util.List;

public class DiseasesAdapter extends BaseAdapter<DiseaseItem, ItemDiseaseCardBinding> {

    private final DiseaseInteractionListener listener;

    public DiseasesAdapter(List<DiseaseItem> itemList, DiseaseInteractionListener listener) {
        super(new ArrayList<>(itemList));
        this.listener = listener;
    }


    @Override
    public ItemDiseaseCardBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemDiseaseCardBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemDiseaseCardBinding> holder, int position, DiseaseItem currentItem) {
        ItemDiseaseCardBinding binding = holder.binding;
        binding.setDisease(currentItem);

        // Decode Base64 string to Bitmap and set to ImageView
        String base64Image = currentItem.getImageResId();
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                Bitmap bitmap = ImageUtils.decodeBase64ToImage(base64Image);
                binding.imgDisease.setImageBitmap(bitmap);
            } catch (IllegalArgumentException e) {
                // Handle invalid Base64 string (set a placeholder image)
                binding.imgDisease.setImageResource(R.drawable.upload_image);
            }
        } else {
            binding.imgDisease.setImageResource(R.drawable.upload_image);
        }
        // handle item click
        binding.getRoot().setOnClickListener(view -> {
            if (listener != null) {
                listener.onItemClick(currentItem);
            }
        });
        binding.executePendingBindings();
    }

    public void updateData(List<DiseaseItem> newList) {
        items.clear(); // `items` موجودة في BaseAdapter
        items.addAll(newList);
        notifyDataSetChanged();
    }

    public interface DiseaseInteractionListener extends BaseInteractionListener {
        void onItemClick(DiseaseItem disease);
    }

}
