package com.example.plugin;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.aidl.login.ILogin;
import com.aidl.login.ILoginListener;
import com.aidl.login.User;

/**
 * Created by Wang.Wenhui
 * Date: 2020/5/7
 */
public class TestFragment extends BaseFragment {
    private Button btn;
    private EditText name, psw;
    private ILogin iLogin;
    private ServiceConnection conn;
    private ILoginListener.Stub listener;

    @Override
    protected void initView() {
        btn = findViewById(R.id.btn);
        name = findViewById(R.id.name);
        psw = findViewById(R.id.psw);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "这是插件fragment", Toast.LENGTH_LONG).show();
                if (!TextUtils.isEmpty(name.getText()) && !TextUtils.isEmpty(psw.getText())) {
                    if (iLogin != null) {
                        try {
                            iLogin.login(name.getText().toString(), psw.getText().toString());
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
        conn = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                iLogin = ILogin.Stub.asInterface(service);
                try {
                    listener = new ILoginListener.Stub() {
                        @Override
                        public void onSuccess(User user) throws RemoteException {
                            Log.e("wwh", "TestFragment --> onSuccess: " + user.getDescription());
                        }

                        @Override
                        public void onFail(String msg) throws RemoteException {
                            Log.e("wwh", "TestFragment --> onFail: " + msg);
                        }
                    };
                    iLogin.addListener(listener);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        Intent intent = new Intent("com.serve.login");
        intent.setPackage("com.example.aidlndkplugindemo");
        getActivity().bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onDestroyView() {
        try {
            iLogin.removeListener(listener);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        iLogin = null;
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        getActivity().unbindService(conn);
        super.onDestroy();
    }

    @Override
    protected int layoutId() {
        return R.layout.fragment_test;
    }
}
