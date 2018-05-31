package com.example.mypar.gift.Structure;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Main_Group_List implements Parcelable {
    private String Group_name = "", Group_introduce = "", Group_professor = "", Group_Email = "", Group_Date = "", Group_photo = "", Group_College = "", Group_Major = "";
    private int Group_Code, Group_Admin;

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Group_name);
        dest.writeString(Group_introduce);
        dest.writeString(Group_professor);
        dest.writeString(Group_Email);
        dest.writeString(Group_Date);
        dest.writeString(Group_photo);
        dest.writeString(Group_College);
        dest.writeString(Group_Major);
        dest.writeInt(Group_Code);
        dest.writeInt(Group_Admin);
    }
    public Main_Group_List(Parcel in){
        readFromParcel(in);
    }
    private void readFromParcel(Parcel src){
        ClassLoader loader = Bitmap.class.getClassLoader();
        Group_name = src.readString();
        Group_introduce = src.readString();
        Group_professor = src.readString();
        Group_Email = src.readString();
        Group_Date = src.readString();
        Group_photo = src.readString();
        Group_College = src.readString();
        Group_Major = src.readString();
        Group_Code = src.readInt();
        Group_Admin = src.readInt();
    }
    public static final Creator<Main_Group_List> CREATOR = new Creator<Main_Group_List>(){
        @Override
        public Main_Group_List createFromParcel(Parcel in){
            return new Main_Group_List(in);
        }
        @Override
        public Main_Group_List[] newArray(int size){
            return new Main_Group_List[size];
        }
    };
    public Main_Group_List(String name, String intro, String professor, String Email, String Date, String photo, String college, String major, int code, int admin){
        this.Group_name = name;
        this.Group_introduce = intro;
        this.Group_professor = professor;
        this.Group_Email = Email;
        this.Group_Date = Date;
        this.Group_photo = photo;
        this.Group_College = college;
        this.Group_Major = major;
        this.Group_Code = code;
        this.Group_Admin = admin;
    }
    public String getName(){
        return this.Group_name;
    }
    public String getIntro(){
        return this.Group_introduce;
    }
    public String getProffesor() { return this.Group_professor;}
    public String getEmail() { return this.Group_Email; }
    public String getDate() { return this.Group_Date; }
    public String getPhoto(){ return this.Group_photo;}
    public String getCollege(){ return this.Group_College; }
    public String getMajor(){ return this.Group_Major; }
    public int getGroup_Code() { return this.Group_Code; }
    public int getGroup_Admin() { return this.Group_Admin; }
}
