package com.example.aidlndkplugindemo;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.aidl.login.ILogin;
import com.aidl.login.ILoginListener;
import com.aidl.login.User;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class LoginService extends Service {
    private User user;
    private List<User> users;
    private WeakReference<ILoginListener> listener;

    public LoginService() {
        users = new ArrayList<>();
        users.add(new User("小王", "123456", "这是小王"));
        users.add(new User("小李", "123456", "这是小李"));
        users.add(new User("小孙", "123456", "这是小孙"));
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LoginProxy();
    }

    private class LoginProxy extends ILogin.Stub {

        @Override
        public User getLoginedUser() throws RemoteException {
            return user;
        }

        @Override
        public void login(String name, String password) throws RemoteException {
            for (User user : users) {
                if (user.getName().equals(name)) {
                    if (user.getPsw().equals(password)) {
                        LoginService.this.user = user;
                        if (listener != null && listener.get() != null) {
                            listener.get().onSuccess(user);
                        }
                        return;
                    } else {
                        if (listener != null && listener.get() != null) {
                            listener.get().onFail("wrong password");
                        }
                        return;
                    }
                }
            }
            if (listener != null && listener.get() != null) {
                listener.get().onFail("no such user exist!!");
            }
        }

        @Override
        public void addListener(ILoginListener listener) throws RemoteException {
            LoginService.this.listener = new WeakReference<>(listener);
        }

        @Override
        public void removeListener(ILoginListener listener) throws RemoteException {
            LoginService.this.listener.clear();
        }

    }

    @Override
    public void onDestroy() {
        listener.clear();
        listener = null;
        super.onDestroy();
    }
}
