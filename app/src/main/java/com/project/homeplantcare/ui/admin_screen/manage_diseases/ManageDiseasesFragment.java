package com.project.homeplantcare.ui.admin_screen.manage_diseases;

import android.widget.Toast;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.project.homeplantcare.R;
import com.project.homeplantcare.databinding.FragmentManageDiseasesBinding;
import com.project.homeplantcare.data.models.DiseaseItem;
import com.project.homeplantcare.ui.base.BaseFragment;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ManageDiseasesFragment extends BaseFragment<FragmentManageDiseasesBinding> implements DiseaseItemAdapter.DiseaseInteractionListener {

    private DiseasesViewModel viewModel;
    private DiseaseItemAdapter adapter;

    @Override
    protected String getTAG() {
        return "ManageDiseasesFragment";
    }

    @Override
    protected int getLayoutIdFragment() {
        return R.layout.fragment_manage_diseases;
    }

    @Override
    protected ViewModel getViewModel() {
        viewModel = new ViewModelProvider(this).get(DiseasesViewModel.class);
        return viewModel;
    }

    @Override
    protected void setup() {
        super.setup();

        setToolbarVisibility(true);
        setToolbarTitle("Manage Diseases");
        showBackButton(false);

        setupFab();


        setupRecyclerView();
        observeDiseases();
    }
    private void setupFab() {
        binding.fabAddDisease.setOnClickListener(v -> {
            Navigation.findNavController(v).navigate(R.id.action_nav_manage_diseases_to_nav_add_disease);
        });
    }

    private void setupRecyclerView() {
        binding.recyclerDiseases.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new DiseaseItemAdapter(viewModel.getMockDiseases(), this);
        binding.recyclerDiseases.setAdapter(adapter);
    }

    private void observeDiseases() {
        viewModel.getDiseasesLiveData().observe(getViewLifecycleOwner(), diseases -> {
        });
    }


    @Override
    public void onEditDiseaseClicked(DiseaseItem item) {

    }

    public void onDeleteDiseaseClicked(DiseaseItem disease) {
        showToast("Deleted: " + disease.getName());
    }

    private void showToast(String message) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show();
    }
}
