package com.example.mypar.gift.server;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by watoz on 2018-05-04.
 */

public class Article_User_Request extends StringRequest {
    final static private String URL = "http://rshak8912.cafe24.com/board/GiftSearchThread.php";

    private Map<String,String> parameters;

    public Article_User_Request(int groupAdmin, int userCode, Response.Listener<String> listener)
    {
        super(Method.POST, URL, listener,null);
        parameters = new HashMap<>();
        parameters.put("adminCode",""+groupAdmin);
        parameters.put("userCode",""+userCode);
    }

    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }
}
