package com.example.mypar.gift.Structure;

import android.os.Parcel;
import android.os.Parcelable;

public class UserData implements Parcelable {
    private String user_email, user_name, user_phone, user_id, countryCode, user_photo;
    private int user_code;

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_email);
        dest.writeString(user_name);
        dest.writeString(user_phone);
        dest.writeString((user_id));
        dest.writeString(countryCode);
        dest.writeString(user_photo);
        dest.writeInt(user_code);
    }
    public UserData(Parcel in){
        readFromParcel(in);
    }
    private void readFromParcel(Parcel src){
        user_email = src.readString();
        user_name = src.readString();
        user_id = src.readString();
        user_phone = src.readString();
        countryCode = src.readString();
        user_photo = src.readString();
        user_code = src.readInt();
    }
    public static final Creator<UserData> CREATOR = new Creator<UserData>(){
        @Override
        public UserData createFromParcel(Parcel in){
            return new UserData(in);
        }
        @Override
        public UserData[] newArray(int size){
            return new UserData[size];
        }
    };
    public UserData(String email, String name, String phone, String id, String country, String photo, int code){
        this.user_email = email;
        this.user_name = name;
        this.user_phone = phone;
        this.user_id = id;
        this.countryCode = country;
        this.user_photo = photo;
        this.user_code = code;
    }
    public String getEmail() { return this.user_email; }
    public String getName(){ return this.user_name; }
    public String getID(){ return this.user_id; }
    public String getPhone(){
        return this.user_phone;
    }
    public String getCountry(){ return this.countryCode; }
    public String getPhoto(){
        return this.user_photo;
    }
    public int getUsercode() { return this.user_code; }
}
