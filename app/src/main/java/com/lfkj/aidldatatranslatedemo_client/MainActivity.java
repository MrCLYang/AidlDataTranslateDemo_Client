package com.lfkj.aidldatatranslatedemo_client;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.lfkj.aidldatatranslatedemo.IRemoteService;

public class MainActivity extends AppCompatActivity {


    private IRemoteService mRemoteService;
    private EditText mEditText;
    private TextView mTv_result;
    private boolean isConnSuccess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mEditText = findViewById(R.id.editText);
        mTv_result = findViewById(R.id.tv_result);
        Intent intent=new Intent("com.lfkj.aidldatatranslatedemo.IRemoteService");
        intent.setPackage("com.lfkj.aidldatatranslatedemo");
        isConnSuccess = bindService(intent, conn, Context.BIND_AUTO_CREATE);
        //TODO 进行复杂数据传递

    }

    public void search(View view){
        if(isConnSuccess){
            int id = Integer.valueOf(mEditText.getText().toString());
            try {
              String name=  mRemoteService.getName(id);
              mTv_result.setText(name);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }else{
            System.out.println("连接失败");
        }
    }


    public ServiceConnection conn=new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            mRemoteService = IRemoteService.Stub.asInterface(iBinder);
            int pid=0;
            try {
                pid= mRemoteService.getPid();
                int currentPid=android.os.Process.myPid();
                System.out.println("currentPID:"+currentPid+"remotePID:"+pid);
                mRemoteService.basicTypes(12,123,true,12.2f,12.3,"欢迎来到王者荣耀");
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            System.out.println("bind success!"+mRemoteService.toString());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mRemoteService = null;
            System.out.println(mRemoteService.toString() +" disconnected! ");
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }
}