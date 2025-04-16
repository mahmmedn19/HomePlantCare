package com.project.homeplantcare.data.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;

public class ImageUtils {


    // Convert File to Bitmap
    public static Bitmap fileToBitmap(File file) {
        try {
            return BitmapFactory.decodeStream(new FileInputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Convert Bitmap to Base64 String
    public static String encodeImageFileToBase64(File file) {
        Bitmap bitmap = fileToBitmap(file);
        if (bitmap == null) return null;
        return encodeImageToBase64(bitmap);
    }
    // ✅ Compress & Convert Bitmap to Base64 (Reduce Size by 50%)
    public static String encodeImageToBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        // Compress the image to 50% quality
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);

        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    // ✅ Convert Base64 String to Bitmap (To Display in ImageView)
    public static Bitmap decodeBase64ToImage(String base64Str) {
        byte[] decodedBytes = Base64.decode(base64Str, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}
