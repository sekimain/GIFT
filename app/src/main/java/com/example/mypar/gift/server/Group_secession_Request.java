package com.example.mypar.gift.server;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by watoz on 2018-05-22.
 */

public class Group_secession_Request extends StringRequest{
    final static private String URL = "http://ekrcy505.cafe24.com/GiftGroupLogout.php";
    private Map<String,String> parameters;

    public Group_secession_Request(int userCode, int groupCode, Response.Listener<String> listener)
    {
        super(Request.Method.POST, URL, listener,null);
        parameters = new HashMap<>();

        parameters.put("groupCode",""+groupCode);
        parameters.put("userCode",""+userCode);
    }

    @Override
    public Map<String,String> getParams()
    {
        return parameters;
    }
}
