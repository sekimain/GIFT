package com.example.mypar.gift.server;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Group_Enter_Request extends StringRequest {
    final static private String URL = "http://ekrcy505.cafe24.com/Enter3.php";
    private Map<String,String> parameters;

    public Group_Enter_Request(int joinUserCode, int groupCode, Response.Listener<String> listener)
    {
        super(Request.Method.POST, URL, listener,null);
        parameters = new HashMap<>();

        parameters.put("userCode",""+joinUserCode);
        parameters.put("groupCode",""+groupCode);
    }

    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }
}