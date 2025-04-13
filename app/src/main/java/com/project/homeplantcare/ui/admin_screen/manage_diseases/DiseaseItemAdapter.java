package com.project.homeplantcare.ui.admin_screen.manage_diseases;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import androidx.annotation.NonNull;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.utils.ImageUtils;
import com.project.homeplantcare.databinding.ItemDiseaseBinding;
import com.project.homeplantcare.ui.base.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class DiseaseItemAdapter extends BaseAdapter<DiseaseItem, ItemDiseaseBinding> {

    private final DiseaseInteractionListener listener;

    public DiseaseItemAdapter(List<DiseaseItem> itemList, DiseaseInteractionListener listener) {
        super(new ArrayList<>(itemList)); // Ensure it's mutable
        this.listener = listener;
    }

    @Override
    public ItemDiseaseBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemDiseaseBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<ItemDiseaseBinding> holder, int position, DiseaseItem diseaseItem) {
        ItemDiseaseBinding binding = holder.binding;
        binding.setDisease(diseaseItem);

        // Decode Base64 string to Bitmap and set to ImageView
        String base64Image = diseaseItem.getImageResId();
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

        // Handle More Options Click
        binding.btnMoreOptions.setOnClickListener(view -> showPopupMenu(view, diseaseItem));

        binding.executePendingBindings();
    }

    private void showPopupMenu(View view, DiseaseItem item) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_admin_disease_options);

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.action_edit) {
                listener.onEditDiseaseClicked(item);
                return true;
            } else if (menuItem.getItemId() == R.id.action_delete) {
                listener.onDeleteDiseaseClicked(item);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    public interface DiseaseInteractionListener {
        void onEditDiseaseClicked(DiseaseItem item);

        void onDeleteDiseaseClicked(DiseaseItem item);
    }
}
