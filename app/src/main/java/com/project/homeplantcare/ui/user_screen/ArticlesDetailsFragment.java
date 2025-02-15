package com.project.homeplantcare.ui.user_screen;

import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentArticlesDetailsBinding;
import com.project.homeplantcare.models.ArticleItem;
import com.project.homeplantcare.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ArticlesDetailsFragment extends BaseFragment<FragmentArticlesDetailsBinding> {

    @Override
    protected String getTAG() {
        return "ArticlesDetailsFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_articles_details;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Article Details");
        showBackButton(true);

// Mock Article Data
        ArticleItem article = new ArticleItem(
                "How to Care for Indoor Plants",
                "Indoor plants not only improve air quality but also add a touch of natural beauty to your living space. To ensure they thrive, it's important to provide them with proper care. Start by choosing the right plant for your environment—some plants prefer low light, while others need bright, indirect sunlight. Watering is another crucial factor; overwatering can lead to root rot, so always check the soil moisture before watering. Regularly clean the leaves to keep them dust-free, as this helps them absorb more light. Don't forget to fertilize during the growing season and re-pot when necessary to give their roots room to grow. By following these steps, your indoor plants will remain healthy and vibrant.",
                "Jan 20, 2024",
                R.drawable.plant_3
        );


        // Bind Article Data to the Layout
        binding.setArticle(article);
    }
}
