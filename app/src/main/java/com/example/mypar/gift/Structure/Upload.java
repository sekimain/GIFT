package com.example.mypar.gift.Structure;

import com.google.firebase.database.Exclude;


public class Upload {

    private String mImageUrl;
    private String mKey;
    private int groupCode;


    public Upload(){
        // empty constructor needed
    }

    public Upload( String mImageUrl, int groupCode ) {
        this.mImageUrl = mImageUrl;
        this.groupCode = groupCode;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public int getgroupCode() {return groupCode;}

    public void setgroupCode(int groupCode){ this.groupCode = groupCode;}

    @Exclude
    public String getmKey() {
        return mKey;
    }
    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }
}