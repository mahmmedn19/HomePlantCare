package com.project.homeplantcare.ui.home_screen;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.homeplantcare.R;
import com.project.homeplantcare.data.utils.ImageUtils;
import com.project.homeplantcare.databinding.ItemNewArticlesCardBinding;
import com.project.homeplantcare.data.models.ArticleItem;
import com.project.homeplantcare.ui.base.BaseAdapter;
import com.project.homeplantcare.ui.base.BaseInteractionListener;

import java.util.List;


public class ArticleAdapter extends BaseAdapter<ArticleItem, ItemNewArticlesCardBinding> {

    private final HomeInteractionListener listener;

    public ArticleAdapter(List<ArticleItem> itemList, HomeInteractionListener listener) {
        super(itemList);
        this.listener = listener;
    }


    @Override
    public ItemNewArticlesCardBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemNewArticlesCardBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemNewArticlesCardBinding> holder, int position, ArticleItem currentItem) {
        ItemNewArticlesCardBinding binding = holder.binding;
        binding.setArticle(currentItem);
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

        binding.cardArticle.setOnClickListener(view -> listener.onArticleClicked(currentItem));
        binding.executePendingBindings();
    }


    public interface HomeInteractionListener extends BaseInteractionListener {
        void onArticleClicked(ArticleItem item);
    }
}