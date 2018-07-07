package com.example.otavio.newshowup.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class BitMapUtils {
    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}
