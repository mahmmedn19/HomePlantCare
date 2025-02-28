package com.project.homeplantcare.ui.user_screen.fav_screen;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.data.utils.AuthUtils;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentFavBinding;
import com.project.homeplantcare.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavFragment extends BaseFragment<FragmentFavBinding> implements FavAdapter.FavoriteInteractionListener {

    private FavViewModel viewModel;
    private FavAdapter adapter;

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
        viewModel = new ViewModelProvider(this).get(FavViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Favorites");
        showBackButton(false);

        setupRecyclerView();
        fetchFavorites();
    }

    private void setupRecyclerView() {
        adapter = new FavAdapter(new ArrayList<>(), this);
        binding.recyclerFav.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.recyclerFav.setAdapter(adapter);
    }

    private void fetchFavorites() {
        String userId = AuthUtils.getCurrentUserId();
        if (userId == null) {
            showToast("Please log in to view favorites.");
            return;
        }

        viewModel.getFavorites(userId).observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                binding.progressBar.setVisibility(View.VISIBLE);
                binding.recyclerFav.setVisibility(View.GONE);
                binding.noData.setVisibility(View.GONE);
            } else if (result.getStatus() == Result.Status.SUCCESS) {
                List<PlantItem> favoritePlants = result.getData();
                binding.progressBar.setVisibility(View.GONE);
                if (favoritePlants != null && !favoritePlants.isEmpty()) {
                    adapter.updateList(favoritePlants);
                    binding.recyclerFav.setVisibility(View.VISIBLE);
                    binding.noData.setVisibility(View.GONE);
                } else {
                    binding.recyclerFav.setVisibility(View.GONE);
                    binding.noData.setVisibility(View.VISIBLE);
                }
            } else {
                binding.progressBar.setVisibility(View.GONE);
                binding.recyclerFav.setVisibility(View.GONE);
                binding.noData.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onFavoriteClicked(PlantItem item) {
        String userId = AuthUtils.getCurrentUserId();
        if (userId == null) {
            showToast("Please log in to remove favorites.");
            return;
        }

        viewModel.removeFavorite(userId, item.getPlantId()).observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                showToast("Removed from favorites");
                fetchFavorites();
            }
        });
    }

    @Override
    public void onDetailsClicked(PlantItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("plantId", item.getPlantId());
        Navigation.findNavController(requireView()).navigate(R.id.action_favoritesFragment_to_plantDetailsFragment2, bundle);
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}

