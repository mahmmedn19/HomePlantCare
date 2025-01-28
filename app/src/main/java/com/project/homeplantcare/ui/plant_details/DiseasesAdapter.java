package com.project.homeplantcare.ui.plant_details;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.ItemCategoryTabBinding;
import com.project.homeplantcare.databinding.ItemDiseaseCardBinding;
import com.project.homeplantcare.models.CategoryItem;
import com.project.homeplantcare.models.DiseaseItem;
import com.project.homeplantcare.ui.base.BaseAdapter;
import com.project.homeplantcare.ui.base.BaseInteractionListener;

import java.util.List;

public class DiseasesAdapter extends BaseAdapter<DiseaseItem, ItemDiseaseCardBinding> {


    public DiseasesAdapter(List<DiseaseItem> itemList) {
        super(itemList);
    }

    @Override
    public ItemDiseaseCardBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemDiseaseCardBinding.inflate(inflater, parent, attachToParent);
    }
    @Override
    public void onBindViewHolder(BaseViewHolder<ItemDiseaseCardBinding> holder, int position, DiseaseItem currentItem) {
        ItemDiseaseCardBinding binding = holder.binding;
        binding.setDisease(currentItem);
        binding.executePendingBindings();
    }

}
