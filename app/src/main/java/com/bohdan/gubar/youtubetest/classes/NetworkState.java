package com.bohdan.gubar.youtubetest.classes;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import retrofit2.Call;

/**
 * Created by gubar on 11.03.2017.
 */

public class NetworkState {
    private Context mContext;

    public NetworkState(Context context){
        mContext = context;
    }

    public boolean checkNetworkState(){
        ConnectivityManager cm =
                (ConnectivityManager)mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
