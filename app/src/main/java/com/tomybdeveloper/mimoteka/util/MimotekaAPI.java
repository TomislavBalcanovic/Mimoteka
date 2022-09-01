package com.tomybdeveloper.mimoteka.util;

import android.app.Application;
import android.os.Parcel;
import android.os.Parcelable;

public class MimotekaAPI extends Application implements Parcelable {

    private String userId;
    private String username;
    private static MimotekaAPI instance;

    protected MimotekaAPI(Parcel in) {
        userId = in.readString();
        username = in.readString();
    }

    public static final Creator<MimotekaAPI> CREATOR = new Creator<MimotekaAPI>() {
        @Override
        public MimotekaAPI createFromParcel(Parcel in) {
            return new MimotekaAPI(in);
        }

        @Override
        public MimotekaAPI[] newArray(int size) {
            return new MimotekaAPI[size];
        }
    };

    public static MimotekaAPI getInstance() {
         if (instance == null)
             instance = new MimotekaAPI();
             return  instance;

     }

    public MimotekaAPI() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static void setInstance(MimotekaAPI instance) {
        MimotekaAPI.instance = instance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userId);
        parcel.writeString(username);
    }
}
