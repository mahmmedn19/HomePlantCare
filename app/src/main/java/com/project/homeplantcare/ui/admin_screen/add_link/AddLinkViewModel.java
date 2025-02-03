package com.project.homeplantcare.ui.admin_screen.add_link;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.regex.Pattern;

public class AddLinkViewModel extends ViewModel {
    public MutableLiveData<String> url = new MutableLiveData<>();
    public MutableLiveData<String> errorMessage = new MutableLiveData<>();

    private static final Pattern URL_PATTERN = Pattern.compile(
            "^(https?|ftp)://[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}(:[0-9]{1,5})?(/.*)?$"
    );

    public boolean validateUrl() {
        String urlInput = url.getValue();
        if (urlInput == null || urlInput.isEmpty() || !URL_PATTERN.matcher(urlInput).matches()) {
            errorMessage.setValue("Invalid URL format");
            return false;
        } else {
            errorMessage.setValue(null);
            return true;
        }
    }
}
