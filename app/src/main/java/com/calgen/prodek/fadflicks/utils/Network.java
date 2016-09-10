package com.calgen.prodek.fadflicks.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Gurupad on 05-Jul-16.
 */
public class Network {

    /**
     *
     * @param context
     * @return {@code True} if the device is connected to a network.
     */
    // TODO: 12-Jul-16 Make the method return false when there is no Active Internet connection.
    public static boolean isConnected(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }
}
