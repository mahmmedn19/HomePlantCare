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

    private final UserAdminInteractionListener listener;

    public ManageUserAdapter(List<User> itemList, UserAdminInteractionListener listener) {
        super(itemList);
        this.listener = listener;
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
        binding.btnOptions.setOnClickListener(view -> showPopupMenu(view, currentUser));
    }


    // Show Block/Unblock Popup Menu
    private void showPopupMenu(View view, User user) {
        PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
        popupMenu.inflate(R.menu.menu_admin_user_options);

        // Set menu item visibility based on user status
        popupMenu.getMenu().findItem(R.id.action_block).setVisible(!user.isBlocked());
        popupMenu.getMenu().findItem(R.id.action_unblock).setVisible(user.isBlocked());

        popupMenu.setOnMenuItemClickListener(menuItem -> {
            if (menuItem.getItemId() == R.id.action_block) {
                listener.onBlockUserClicked(user);
                return true;
            } else if (menuItem.getItemId() == R.id.action_unblock) {
                listener.onUnblockUserClicked(user);
                return true;
            }
            return false;
        });

        popupMenu.show();
    }

    public List<User> getUserList() {
        return items; // Ensure this references the actual list used in the adapter
    }


    public interface UserAdminInteractionListener {
        void onBlockUserClicked(User user);

        void onUnblockUserClicked(User user);
    }
}