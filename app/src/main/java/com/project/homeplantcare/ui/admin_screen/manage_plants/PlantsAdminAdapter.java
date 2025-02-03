package com.project.homeplantcare.ui.admin_screen.manage_plants;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.ItemPlantAdminBinding;
import com.project.homeplantcare.models.PlantItem;
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

        Glide.with(holder.binding.getRoot().getContext())
                .load(currentItem.getImageResId())
                .placeholder(R.drawable.rounded_background_black)
                .into(binding.image);

        // Handle More Options Click (Edit/Delete)
        binding.btnOptions.setOnClickListener(view -> showPopupMenu(view, currentItem));

        binding.executePendingBindings();
    }

    // Show Edit/Delete Popup Menu
    private void showPopupMenu(View view, PlantItem item) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_admin_plant_options); // Ensure `menu_admin_plant_options.xml` exists

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
