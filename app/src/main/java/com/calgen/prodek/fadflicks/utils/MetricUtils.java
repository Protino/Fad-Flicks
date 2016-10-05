package com.calgen.prodek.fadflicks.utils;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by Gurupad on 05-Oct-16.
 * You asked me to change it for no reason.
 */
public class MetricUtils {

    public static int dpToPx(int dp) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }

    public static int pxToDp(int px) {
        DisplayMetrics displayMetrics = Resources.getSystem().getDisplayMetrics();
        return Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
    }
}
