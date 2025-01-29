package com.project.homeplantcare.ui.plants_view_all;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.homeplantcare.databinding.ItemNewArticlesCardBinding;
import com.project.homeplantcare.databinding.ItemViewAllArticlesCardBinding;
import com.project.homeplantcare.models.ArticleItem;
import com.project.homeplantcare.ui.base.BaseAdapter;
import com.project.homeplantcare.ui.base.BaseInteractionListener;

import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

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
        Glide.with(holder.binding.getRoot().getContext())
                .load(currentItem.getImageResId())
                .into(holder.binding.imgArticle);

        binding.cardArticle.setOnClickListener(view -> listener.onCartClicked(currentItem));
        binding.executePendingBindings();
    }


    public interface ViewAllInteractionListener extends BaseInteractionListener {
        void onCartClicked(ArticleItem item);
    }
}