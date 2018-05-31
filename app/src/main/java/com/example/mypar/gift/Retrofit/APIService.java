package com.example.mypar.gift.Retrofit;

import com.example.mypar.gift.Model.MYResponse;
import com.example.mypar.gift.Model.Sender;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created by seki on 2018-04-02.
 */

public interface APIService
{
    @Headers({
            "content-type:application/json",
            "authorization:key=AAAAQP_RTFk:APA91bGIPwOS_AcQFGCJKbj9mTIC6Bl6x_P4N7VKXyerjs7jfOZaxhUMVEnQNLCTadjjKaKlVICfy5wHtmofjw07vekTDRFZjCmPY74FiY6-vlz9lv55vLLUqsdDgyFIvJJIbnba0jgQ"
    })
    @POST("fcm/send")

    Call<MYResponse> sendNotification(@Body Sender body);
}
