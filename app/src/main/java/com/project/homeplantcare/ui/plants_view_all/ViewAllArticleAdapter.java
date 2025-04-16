package com.project.homeplantcare.ui.plants_view_all;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.homeplantcare.R;
import com.project.homeplantcare.data.utils.ImageUtils;
import com.project.homeplantcare.databinding.ItemViewAllArticlesCardBinding;
import com.project.homeplantcare.data.models.ArticleItem;
import com.project.homeplantcare.ui.base.BaseAdapter;
import com.project.homeplantcare.ui.base.BaseInteractionListener;

import java.util.List;

public class ViewAllArticleAdapter extends BaseAdapter<ArticleItem, ItemViewAllArticlesCardBinding> {

    private final ViewAllInteractionListener listener;

    public ViewAllArticleAdapter(List<ArticleItem> itemList, ViewAllInteractionListener listener) {
        super(itemList);
        this.listener = listener;
    }


    @Override
    public ItemViewAllArticlesCardBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemViewAllArticlesCardBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemViewAllArticlesCardBinding> holder, int position, ArticleItem currentItem) {
        ItemViewAllArticlesCardBinding binding = holder.binding;
        binding.setArticle(currentItem);

        // Load Image using Glide
        // Decode Base64 string to Bitmap and set to ImageView
        String base64Image = currentItem.getImageResId();
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                Bitmap bitmap = ImageUtils.decodeBase64ToImage(base64Image);
                binding.imgArticle.setImageBitmap(bitmap);
            } catch (IllegalArgumentException e) {
                // Handle invalid Base64 string (set a placeholder image)
                binding.imgArticle.setImageResource(R.drawable.hoya4);
            }
        } else {
            binding.imgArticle.setImageResource(R.drawable.hoya4);
        }

        binding.cardArticle.setOnClickListener(view -> listener.onCartClicked(currentItem));
        binding.executePendingBindings();
    }


    public interface ViewAllInteractionListener extends BaseInteractionListener {
        void onCartClicked(ArticleItem item);
    }
}