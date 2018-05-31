package com.example.mypar.gift.server;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MyGroup_Get_Request extends StringRequest{
    final static private String URL = "http://ekrcy505.cafe24.com/MyGroupGet.php";
    private Map<String,String> parameters;

    public MyGroup_Get_Request(int userCode, Response.Listener<String> listener)
    {
        super(Request.Method.POST, URL, listener,null);
        parameters = new HashMap<>();

        parameters.put("userCode",""+userCode);
    }

    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }
}
