package com.example.mypar.gift.Structure;

import com.google.firebase.database.Exclude;


public class User_Upload {
    private String mImageUrl;
    private String mKey;
    private int userCode;


    public User_Upload() {
        // empty constructor needed
    }

    public User_Upload(String mImageUrl, int groupCode) {
        this.mImageUrl = mImageUrl;
        this.userCode = groupCode;
    }

    public String getmImageUrl() {
        return mImageUrl;
    }

    public void setmImageUrl(String mImageUrl) {
        this.mImageUrl = mImageUrl;
    }

    public int getgroupCode() {
        return userCode;
    }

    public void setgroupCode(int groupCode) {
        this.userCode = groupCode;
    }

    @Exclude
    public String getmKey() {
        return mKey;
    }

    @Exclude
    public void setmKey(String mKey) {
        this.mKey = mKey;
    }

}
