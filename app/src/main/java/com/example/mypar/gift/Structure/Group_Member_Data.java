package com.example.mypar.gift.Structure;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Group_Member_Data {
    private String user_name, user_email , user_phone, user_id, countryCode, user_photo;
    private int user_code;

    public Group_Member_Data(String name, String email, String phone, String id, String country, String photo, int code){
        this.user_name = name;
        this.user_email = email;
        this.user_phone = phone;
        this.user_id = id;
        this.countryCode = country;
        this.user_photo = photo;
        this.user_code = code;
    }
    public String getName(){
        return this.user_name;
    }
    public String getID(){ return this.user_id; }
    public String getemail(){ return this.user_email; }
    public String getPhone(){ return this.user_phone;  }
    public String getCountry(){
        return this.countryCode;
    }
    public String getPhoto(){
        return this.user_photo;
    }
    public int getUsercode() { return this.user_code; }
}
