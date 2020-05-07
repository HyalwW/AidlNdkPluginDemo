// ILogin.aidl
package com.aidl.login;
import com.aidl.login.User;
import com.aidl.login.ILoginListener;

// Declare any non-default types here with import statements

interface ILogin {
    User getLoginedUser();

    void login(String name, String password);

    void addListener(ILoginListener listener);

    void removeListener(ILoginListener listener);
}
