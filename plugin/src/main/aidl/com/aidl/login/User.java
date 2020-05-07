package com.aidl.login;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Wang.Wenhui
 * Date: 2020/5/7
 */
public class User implements Parcelable {
    private String name;
    private String psw;
    private String description;

    public User() {
    }

    public User(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public User(String name, String psw, String description) {
        this.name = name;
        this.psw = psw;
        this.description = description;
    }

    protected User(Parcel in) {
        name = in.readString();
        psw = in.readString();
        description = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPsw() {
        return psw;
    }

    public void setPsw(String psw) {
        this.psw = psw;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(psw);
        dest.writeString(description);
    }
}
