package com.example.mypar.gift.Service;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by seki on 2018-04-03.
 */

public class TokenRequest extends StringRequest {
    final static private String URL = "http://ekrcy505.cafe24.com/GiftTokenPost.php";
    private Map<String,String> parameters;

    public TokenRequest(int userCode,String userToken ,Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener,null);
        parameters = new HashMap<>();
        parameters.put("userCode",""+userCode);
        parameters.put("userToken",userToken);
    }

    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }
}
