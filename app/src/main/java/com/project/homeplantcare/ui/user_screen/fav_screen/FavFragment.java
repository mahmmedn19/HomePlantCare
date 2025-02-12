package com.project.homeplantcare.ui.user_screen.fav_screen;

import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentFavBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavFragment extends BaseFragment<FragmentFavBinding> {


    @Override
    protected String getTAG() {
        return "FavFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_fav;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Favourite");
        showBackButton(false);

    }
}