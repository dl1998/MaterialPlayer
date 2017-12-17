package com.android.materialplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Created by dl1998 on 01.12.17.
 */

public class BlurBuilder {

    private static final float BITMAP_SCALE = 0.05f;
    private static final float BLUR_RADIUS = 25f;

    public static Bitmap blur(Context context, Bitmap image) {
        int width = Math.round(image.getWidth() * BITMAP_SCALE);
        int height = Math.round(image.getHeight() * BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image, width, height, false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript rs = RenderScript.create(context);

        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));

        Allocation tmIn = Allocation.createFromBitmap(rs, inputBitmap);
        Allocation tmOut = Allocation.createFromBitmap(rs, outputBitmap);

        intrinsicBlur.setRadius(BLUR_RADIUS);
        intrinsicBlur.setInput(tmIn);
        intrinsicBlur.forEach(tmOut);
        tmOut.copyTo(outputBitmap);

        return outputBitmap;
    }
}
