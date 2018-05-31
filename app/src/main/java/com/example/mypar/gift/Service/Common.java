package com.example.mypar.gift.Service;

import com.example.mypar.gift.Retrofit.APIService;
import com.example.mypar.gift.Retrofit.RetrofitClient;

/**
 * Created by seki on 2018-04-03.
 */

public class Common {
    public static String currentToken = "";

    private static String baseURL = "https://fcm.googleapis.com/";
    public  static APIService getFCMClient()
    {
        return RetrofitClient.getClient(baseURL).create(APIService.class);
    }
}
