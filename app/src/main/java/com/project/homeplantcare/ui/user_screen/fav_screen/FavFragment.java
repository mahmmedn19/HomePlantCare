package com.project.homeplantcare.ui.user_screen.fav_screen;

import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentFavBinding;
import com.project.homeplantcare.data.models.PlantItem;
import com.project.homeplantcare.ui.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class FavFragment extends BaseFragment<FragmentFavBinding> implements FavAdapter.FavoriteInteractionListener {


    private FavAdapter adapter;
    private List<PlantItem> favoritePlants;

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

/*
        favoritePlants = generateFakeFavorites();
*/
        setupRecyclerView();
    }

    private void setupRecyclerView() {
        adapter = new FavAdapter(favoritePlants, this);

        binding.recyclerFav.setLayoutManager(new GridLayoutManager(requireContext(), 2));
        binding.recyclerFav.setAdapter(adapter);
    }

    private void removeFromFavorites(PlantItem plant) {
        favoritePlants.remove(plant);
        adapter.updateList(new ArrayList<>(favoritePlants));
    }


/*    private List<PlantItem> generateFakeFavorites() {
        List<PlantItem> plants = new ArrayList<>();
        plants.add(new PlantItem("", "Aloe Vera", "A great air-purifying plant A great air-purifying plant air-purifying plant air-purifying plant ", R.drawable.plant_2));
        plants.add(new PlantItem("", "Snake Plant", "Low-maintenance indoor plant Low-maintenance indoor plant", R.drawable.plant_6));
        plants.add(new PlantItem("", "Money Plant", "Brings good luck! Brings good luck!Brings good luck!Brings good luck!", R.drawable.plant_7));
        plants.add(new PlantItem("", "Peace Lily", "Ideal mfor offices Ideal for offices Ideal for offices Ideal for offices", R.drawable.plant_3));
        plants.add(new PlantItem("", "Spider Plant", "Hardy and resilient Hardy and resilient Hardy and resilient Hardy and resilient Hardy and resilient", R.drawable.plant_4));
        return plants;
    }*/

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFavoriteClicked(PlantItem item) {
        removeFromFavorites(item);
    }

    @Override
    public void onDetailsClicked(PlantItem item) {
        Navigation.findNavController(requireView()).navigate(R.id.action_favoritesFragment_to_plantDetailsFragment2);
    }
}
