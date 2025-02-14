package com.project.homeplantcare.ui.home_screen;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.project.homeplantcare.databinding.ItemNewArticlesCardBinding;
import com.project.homeplantcare.models.ArticleItem;
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
        // Load Image using Glide
        Glide.with(holder.binding.getRoot().getContext())
                .load(currentItem.getImageResId())
                .into(holder.binding.imgArticle);

        binding.cardArticle.setOnClickListener(view -> listener.onArticleClicked(currentItem));
        binding.executePendingBindings();
    }


    public interface HomeInteractionListener extends BaseInteractionListener {
        void onArticleClicked(ArticleItem item);
    }
}