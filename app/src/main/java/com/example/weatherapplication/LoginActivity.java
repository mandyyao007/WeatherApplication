package com.example.weatherapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.weatherapplication.bean.LoginBean;
import com.example.weatherapplication.util.NetUtil;
import com.example.weatherapplication.util.ToastUtil;
import com.google.gson.Gson;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //声明控件
    private Button mBtnLogin;
    private EditText mEtUser;
    private EditText mEtPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //找到控件
        mBtnLogin = findViewById(R.id.btn_login);
        mEtUser = findViewById(R.id.et_1);//用户名
        mEtPassword = findViewById(R.id.et_2);//密码
        mBtnLogin.setOnClickListener((View.OnClickListener) this);
        //  在Android4.0以后，会发现，只要是写在主线程（就是Activity）中的HTTP请求，运行时都会报错，这是因为Android在4.0以后为了防止应用的ANR（Aplication Not Response）异常，
        //  Android这个设计是为了防止网络请求时间过长而导致界面假死的情况发生。
        //以下适用于数据量很小的情况
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
    }
    public void onClick(View v){
        //获取用户名和密码
        String userName = mEtUser.getText().toString();
        String password = mEtPassword.getText().toString();
        //弹出内容
        String success = "成功";
        String fail = "用户名或者密码有误，请重新登录!";

        Intent intent = null;
      if(!"".equals(userName) && !"".equals(password)){
           try {
                String status = NetUtil.getloginInfo(userName,password);
                Log.d("fan","====status==:"+status);
                if("S".equals(status)){
                    ToastUtil.showMessage(LoginActivity.this,success);
                    intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("userName",userName);
                    startActivity(intent);
                }else{
                    intent = null;
                   //toast 居中显示
                    ToastUtil.showMessage(LoginActivity.this,fail);
               }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            //弹出登录失败
            intent = null;
            Toast toastCenter = Toast.makeText(getApplicationContext(), fail,Toast.LENGTH_SHORT);
            toastCenter.setGravity(Gravity.CENTER,0,0);
            toastCenter.show();
        }
    }

}