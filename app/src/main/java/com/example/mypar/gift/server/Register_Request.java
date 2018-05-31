package com.example.mypar.gift.server;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;
public class Register_Request extends StringRequest{

    final static private String URL = "http://ekrcy505.cafe24.com/GiftRegister.php";
    private Map<String,String> parameters;

    public Register_Request(String userEmail,String userPassword, String userName,String userID, String userPhone,String userCountry, Response.Listener<String> listener){
        super(Method.POST, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userEmail", userEmail);
        parameters.put("userPassword", userPassword);
        parameters.put("userName", userName);
        parameters.put("userID", userID);
        parameters.put("userPhone", userPhone);
        parameters.put("userCountry", userCountry);
    }
    @Override
    public Map<String,String> getParams(){
        return parameters;
    }
}