package com.project.homeplantcare.ui.admin_screen.manage_articles;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.project.homeplantcare.R;
import com.project.homeplantcare.data.utils.ImageUtils;
import com.project.homeplantcare.databinding.ItemManageArticleBinding;
import com.project.homeplantcare.data.models.ArticleItem;
import com.project.homeplantcare.ui.base.BaseAdapter;

import java.util.List;

public class ArticlesAdapter extends BaseAdapter<ArticleItem, ItemManageArticleBinding> {

    private final ArticleInteractionListener listener;
    private List<ArticleItem> itemList;

    public ArticlesAdapter(List<ArticleItem> itemList, ArticleInteractionListener listener) {
        super(itemList);
        this.itemList = itemList;
        this.listener = listener;
    }

    @Override
    public ItemManageArticleBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemManageArticleBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemManageArticleBinding> holder, int position, ArticleItem currentItem) {
        ItemManageArticleBinding binding = holder.binding;
        binding.setArticle(currentItem);

        // Decode Base64 string to Bitmap and set to ImageView
        String base64Image = currentItem.getImageResId();
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                Bitmap bitmap = ImageUtils.decodeBase64ToImage(base64Image);
                binding.imageArticle.setImageBitmap(bitmap);
            } catch (IllegalArgumentException e) {
                // Handle invalid Base64 string (set a placeholder image)
                binding.imageArticle.setImageResource(R.drawable.hoya4);
            }
        } else {
            binding.imageArticle.setImageResource(R.drawable.hoya4);
        }


        // Handle item click
        binding.getRoot().setOnClickListener(view -> listener.onArticleClicked(currentItem));
        binding.btnOptions.setOnClickListener(view -> showPopupMenu(view, currentItem));

        binding.executePendingBindings();
    }

    private void showPopupMenu(View view, ArticleItem item) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_admin_plant_options);

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.action_edit) {
                listener.onEditArticleClicked(item);
                return true;
            } else if (menuItem.getItemId() == R.id.action_delete) {
                listener.onDeleteArticleClicked(item);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    public void updateList(List<ArticleItem> newList) {
        itemList.clear();
        itemList.addAll(newList);
        notifyDataSetChanged();
    }

    public interface ArticleInteractionListener {
        void onArticleClicked(ArticleItem article);

        void onEditArticleClicked(ArticleItem item);

        void onDeleteArticleClicked(ArticleItem item);

    }
}
