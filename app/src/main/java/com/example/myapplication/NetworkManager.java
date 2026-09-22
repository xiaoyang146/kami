package com.example.myapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;

/* loaded from: C:\Users\xiao146\Desktop\xiao.dex */
public class NetworkManager {
    public static boolean isNetworkAvailable(Context context) {
        NetworkInfo info;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        return (cm == null || (info = cm.getActiveNetworkInfo()) == null || !info.isConnected()) ? false : true;
    }

    public static boolean isVpnActive(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService("connectivity");
        if (cm == null) {
            return false;
        }
        for (Network network : cm.getAllNetworks()) {
            NetworkInfo info = cm.getNetworkInfo(network);
            if (info != null && info.getType() == 17 && info.isConnected()) {
                return true;
            }
        }
        return false;
    }
}