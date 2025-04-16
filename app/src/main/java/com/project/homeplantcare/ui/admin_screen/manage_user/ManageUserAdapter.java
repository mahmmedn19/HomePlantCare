package com.project.homeplantcare.ui.admin_screen.manage_user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;

import com.project.homeplantcare.R;
import com.project.homeplantcare.data.models.User;
import com.project.homeplantcare.databinding.ItemUserAdminBinding;
import com.project.homeplantcare.ui.base.BaseAdapter;

import java.util.List;

public class ManageUserAdapter extends BaseAdapter<User, ItemUserAdminBinding> {


    public ManageUserAdapter(List<User> itemList) {
        super(itemList);
    }

    @Override
    public ItemUserAdminBinding createBinding(LayoutInflater inflater, ViewGroup parent, boolean attachToParent) {
        return ItemUserAdminBinding.inflate(inflater, parent, attachToParent);
    }

    @Override
    public void onBindViewHolder(BaseViewHolder<ItemUserAdminBinding> holder, int position, User currentUser) {
        ItemUserAdminBinding binding = holder.binding;
        binding.setUser(currentUser);
        binding.executePendingBindings(); // Ensure Data Binding is refreshed

        // Handle More Options Click (Block/Unblock)
    }



    public List<User> getUserList() {
        return items; // Ensure this references the actual list used in the adapter
    }


}