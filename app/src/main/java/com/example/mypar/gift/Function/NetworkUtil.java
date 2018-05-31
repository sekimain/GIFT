package com.example.mypar.gift.Function;

import android.annotation.SuppressLint;
import android.os.StrictMode;

/**
 * Created by mypar on 2018-04-03.
 */

public class NetworkUtil {

    @SuppressLint("NewApi")
    static public void setNetworkPolicy() {
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }

}
