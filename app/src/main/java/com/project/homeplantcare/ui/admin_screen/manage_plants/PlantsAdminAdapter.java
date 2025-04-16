package com.project.homeplantcare.ui.admin_screen.manage_plants;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.project.homeplantcare.R;
import com.project.homeplantcare.data.utils.ImageUtils;
import com.project.homeplantcare.databinding.ItemPlantAdminBinding;
import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.ui.base.BaseAdapter;

import java.util.List;

public class PlantsAdminAdapter extends BaseAdapter<PlantItem, ItemPlantAdminBinding> {

    private final PlantAdminInteractionListener listener;

    public PlantsAdminAdapter(List<PlantItem> itemList, PlantAdminInteractionListener listener) {
        super(itemList);
        this.listener = listener;
    }

    @Override
    public ItemPlantAdminBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemPlantAdminBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemPlantAdminBinding> holder, int position, PlantItem currentItem) {
        ItemPlantAdminBinding binding = holder.binding;
        binding.setPlant(currentItem);

        // Decode Base64 string to Bitmap and set to ImageView
        String base64Image = currentItem.getImageResId();
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                Bitmap bitmap = ImageUtils.decodeBase64ToImage(base64Image);
                binding.image.setImageBitmap(bitmap);
            } catch (IllegalArgumentException e) {
                // Handle invalid Base64 string (set a placeholder image)
                binding.image.setImageResource(R.drawable.plant_2);
            }
        } else {
            binding.image.setImageResource(R.drawable.plant_2);
        }

        // Handle More Options Click (Edit/Delete)
        binding.btnOptions.setOnClickListener(view -> showPopupMenu(view, currentItem));

        binding.executePendingBindings();
    }

    // Show Edit/Delete Popup Menu
    private void showPopupMenu(View view, PlantItem item) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_admin_plant_options);

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.action_edit) {
                listener.onEditPlantClicked(item);
                return true;
            } else if (menuItem.getItemId() == R.id.action_delete) {
                listener.onDeletePlantClicked(item);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    public interface PlantAdminInteractionListener {
        void onEditPlantClicked(PlantItem item);

        void onDeletePlantClicked(PlantItem item);
    }
}
