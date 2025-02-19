package com.project.homeplantcare.ui.admin_screen.manage_articles;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.bumptech.glide.Glide;
import com.project.homeplantcare.R;
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

        // Load image using Glide
        Glide.with(holder.binding.getRoot().getContext())
                .load(currentItem.getImageResId())
                .placeholder(R.drawable.plant_3)
                .into(binding.imageArticle);

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
