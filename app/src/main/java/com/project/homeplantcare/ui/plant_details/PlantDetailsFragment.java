package com.project.homeplantcare.ui.plant_details;

import android.graphics.Bitmap;
import android.view.View;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.data.utils.AuthUtils;
import com.project.homeplantcare.data.utils.ImageUtils;
import com.project.homeplantcare.data.utils.Result;
import com.project.homeplantcare.databinding.FragmentPlantDetailsBinding;
import com.project.homeplantcare.ui.MainActivity;
import com.project.homeplantcare.ui.base.BaseFragment;
import com.project.homeplantcare.ui.user_screen.UserMainActivity;
import com.project.homeplantcare.utils.DialogUtils;

import java.util.ArrayList;
import java.util.List;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class PlantDetailsFragment extends BaseFragment<FragmentPlantDetailsBinding> {

    private PlantDetailsViewModel viewModel;
    private DiseasesAdapter diseasesAdapter;
    private List<DiseaseItem> diseaseList = new ArrayList<>();
    private boolean isFavorite = false;
    private boolean isInHistory = false;
    private String plantId;

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
        viewModel = new ViewModelProvider(this).get(PlantDetailsViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();
        setToolbarVisibility(true);
        setToolbarTitle("Plant Details");
        showBackButton(true);

        boolean isAnalysis = getArguments() != null && getArguments().getBoolean("isAnalysis", false);

        if (isAnalysis) {
            binding.addToHistory.setVisibility(View.VISIBLE);
        } else {
            binding.addToHistory.setVisibility(View.GONE);
        }

        // Retrieve plantId from arguments
        plantId = getArguments() != null ? getArguments().getString("plantId") : null;

        if (plantId != null) {
            viewModel.fetchPlantDetails(plantId);
            observePlantDetails();
            observeDiseases();
            checkFavoriteState(plantId);
            checkHistoryState(plantId);
        } else {
            showToast("Invalid Plant ID");
        }

        binding.favIcon.setOnClickListener(v -> handleFavoriteToggle());
        binding.addToHistory.setOnClickListener(v -> handleHistoryToggle());

        setupDiseasesRecyclerView();
        binding.favIcon.setOnClickListener(v -> {
            if (isLogging()) {
                handleFavoriteToggle();
            } else {
                // Show Login Dialog
                DialogUtils.showConfirmationDialog(
                        requireContext(),
                        "Login Required",
                        "You need to login to add this plant to favorites",
                        "Login",
                        "Cancel",
                        (dialog, which) -> {
                            Navigation.findNavController(v).navigate(R.id.action_plantDetailsFragment_to_loginFragment);
                        }
                );
            }
        });
    }

    private void observePlantDetails() {
        viewModel.getPlantDetails().observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.LOADING) {
                showFullScreenLoading(true);
                binding.llDetailsContainer.setVisibility(View.GONE);  // Hide all UI components
            }
            else if (result.getStatus() == Result.Status.SUCCESS) {
                new android.os.Handler().postDelayed(() -> {
                    showFullScreenLoading(false);
                    binding.setPlant(result.getData());
                    binding.llDetailsContainer.setVisibility(View.VISIBLE);  // Show UI after loading
                    String base64Image = result.getData().getImageResId();
                    if (base64Image != null && !base64Image.isEmpty()) {
                        Bitmap bitmap = ImageUtils.decodeBase64ToImage(base64Image);
                        binding.imgPlant.setImageBitmap(bitmap);
                    }
                }, 1000); // 1.5 seconds delay
            }
        });
    }

    private void observeDiseases() {
        viewModel.getDiseases().observe(getViewLifecycleOwner(), diseases -> {
            if (diseases != null && !diseases.isEmpty()) {
                binding.recyclerDiseases.setVisibility(View.VISIBLE);
                binding.tvNoDiseases.setVisibility(View.GONE);
                diseasesAdapter.updateData(diseases); // استخدم الدالة الجديدة
            } else {
                binding.recyclerDiseases.setVisibility(View.GONE);
                binding.tvNoDiseases.setVisibility(View.VISIBLE);
            }
        });
    }


    private void checkFavoriteState(String plantId) {
        String userId = AuthUtils.getCurrentUserId();

        if (userId == null || plantId == null) {
            return;
        }

        viewModel.isPlantFavorite(userId, plantId).observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                isFavorite = result.getData();
                updateFavoriteIcon();
            }
        });
    }

    private void handleFavoriteToggle() {
        String userId = AuthUtils.getCurrentUserId();

        if (userId == null || plantId == null) {
            return;
        }

        if (isFavorite) {
            viewModel.removeFromFavorites(userId, plantId).observe(getViewLifecycleOwner(), result -> {
                if (result.getStatus() == Result.Status.SUCCESS) {
                    isFavorite = false;
                    updateFavoriteIcon();
                    showToast("Removed from favorites");
                }
            });
        } else {
            viewModel.addToFavorites(userId, plantId).observe(getViewLifecycleOwner(), result -> {
                if (result.getStatus() == Result.Status.SUCCESS) {
                    isFavorite = true;
                    updateFavoriteIcon();
                    showToast("Added to favorites");
                }
            });
        }
    }

    private void updateFavoriteIcon() {
        if (isFavorite) {
            binding.favIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.md_theme_error));
        } else {
            binding.favIcon.setColorFilter(ContextCompat.getColor(requireContext(), R.color.md_theme_primary));
        }
    }

    private void checkHistoryState(String plantId) {
        String userId = AuthUtils.getCurrentUserId();

        if (userId == null || plantId == null) {
            return;
        }

        viewModel.isPlantInHistory(userId, plantId).observe(getViewLifecycleOwner(), result -> {
            if (result.getStatus() == Result.Status.SUCCESS) {
                isInHistory = result.getData();
                updateHistoryIcon();
            }
        });
    }


    private void handleHistoryToggle() {
        String userId = AuthUtils.getCurrentUserId();

        if (userId == null || plantId == null) {
            return;
        }

        if (isInHistory) {
            viewModel.removeFromHistory(userId, plantId).observe(getViewLifecycleOwner(), result -> {
                if (result.getStatus() == Result.Status.SUCCESS) {
                    isInHistory = false;
                    updateHistoryIcon();
                    showToast("Removed from history");
                }
            });
        } else {
            viewModel.addToHistory(userId, plantId).observe(getViewLifecycleOwner(), result -> {
                if (result.getStatus() == Result.Status.SUCCESS) {
                    isInHistory = true;
                    updateHistoryIcon();
                    showToast("Added to history");
                }
            });
        }
    }

    private void updateHistoryIcon() {
        if (isInHistory) {
            binding.addToHistory.setColorFilter(ContextCompat.getColor(requireContext(), R.color.md_theme_error));
        } else {
            binding.addToHistory.setColorFilter(ContextCompat.getColor(requireContext(), R.color.md_theme_primary));
        }
    }
    private void showFullScreenLoading(boolean isLoading) {
        if (isLoading) {
            binding.fullScreenLoader.setVisibility(View.VISIBLE);
        } else {
            new android.os.Handler().postDelayed(() -> {
                binding.fullScreenLoader.setVisibility(View.GONE);
            }, 1000); // 1.5 seconds delay
        }
    }
    private boolean isLogging() {
        if (getActivity() instanceof UserMainActivity) {
            return true;
        } else if (getActivity() instanceof MainActivity) {
            return false;
        }
        return false;
    }

    private void setupDiseasesRecyclerView() {
        diseasesAdapter = new DiseasesAdapter(new ArrayList<>()); // استخدم قائمة فاضية
        binding.recyclerDiseases.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerDiseases.setAdapter(diseasesAdapter);
    }


    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}