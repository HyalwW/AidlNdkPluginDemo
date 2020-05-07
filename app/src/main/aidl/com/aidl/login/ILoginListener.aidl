// ILoginListener.aidl
package com.aidl.login;
import com.aidl.login.User;

// Declare any non-default types here with import statements

interface ILoginListener {
    void onSuccess(in User user);

    void onFail(String msg);
}
