package com.project.homeplantcare.ui.home_screen;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.ItemCategoryTabBinding;
import com.project.homeplantcare.data.models.CategoryItem;
import com.project.homeplantcare.ui.base.BaseAdapter;
import com.project.homeplantcare.ui.base.BaseInteractionListener;
import java.util.List;

public class CategoryAdapter extends BaseAdapter<CategoryItem, ItemCategoryTabBinding> {

    private final CategoryInteractionListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION; // No initial selection

    public CategoryAdapter(List<CategoryItem> itemList, CategoryInteractionListener listener) {
        super(itemList);
        this.listener = listener;
    }

    @Override
    public ItemCategoryTabBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemCategoryTabBinding.inflate(inflater, parent, attachToParent);
    }
    @Override
    public void onBindViewHolder(BaseViewHolder<ItemCategoryTabBinding> holder, int position, CategoryItem currentItem) {
        ItemCategoryTabBinding binding = holder.binding;
        binding.setCategory(currentItem);

        // Highlight the selected category
        if (selectedPosition == position) {
            binding.tvCategory.setTextColor(binding.getRoot().getContext().getColor(R.color.md_theme_primary));
        } else {
            binding.tvCategory.setTextColor(binding.getRoot().getContext().getColor(R.color.green_3));
        }

        // Click Listener to change selection dynamically
        binding.getRoot().setOnClickListener(view -> {
            int previousPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            if (previousPosition != RecyclerView.NO_POSITION) {
                notifyItemChanged(previousPosition);
            }
            notifyItemChanged(selectedPosition);
            listener.onCategoryClicked(currentItem);
        });
        binding.executePendingBindings();
    }

    /**
     * Set the default selected category programmatically
     */
    public void setDefaultCategory(int position) {
        if (position >= 0 && position < getItemCount()) {
            int previousPosition = selectedPosition;
            selectedPosition = position;
            notifyItemChanged(previousPosition);
            notifyItemChanged(selectedPosition);
        }
    }

    public interface CategoryInteractionListener extends BaseInteractionListener {
        void onCategoryClicked(CategoryItem item);
    }
}
