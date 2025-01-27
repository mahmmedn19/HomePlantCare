package com.project.homeplantcare.ui.plant_details;

import androidx.lifecycle.ViewModel;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentPlantDetailsBinding;
import com.project.homeplantcare.models.PlantItem;
import com.project.homeplantcare.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlantDetailsFragment extends BaseFragment<FragmentPlantDetailsBinding> {

    @Override
    protected String getTAG() {
        return "PlantDetailsFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_plant_details;
    }

    @Override
    protected ViewModel getViewModel() {
        return null;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        // Set toolbar title
        setToolbarTitle("Plant Details");
        // Show back button
        showBackButton(true);
        // Create a fake PlantItem object
        PlantItem fakePlant = new PlantItem(
                1,
                "Aloe Vera",
                "Aloe Vera is a species of plant well known for its medicinal and skincare uses. It is easy to maintain and grows well indoors.",
                "Indoor",
                "Medium",
                "Easy",
                56.0,
                R.drawable.plant_2 // Replace with your plant drawable resource
        );

        // Bind the PlantItem to the layout
        binding.setPlant(fakePlant);

        // Optional: Set toolbar title if needed
        setToolbarTitle(fakePlant.getName());
    }
}