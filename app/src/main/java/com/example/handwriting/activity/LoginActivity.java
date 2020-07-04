package com.example.handwriting.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.handwriting.R;
import com.example.handwriting.bean.User;
import com.example.handwriting.bean.UserLab;

public class LoginActivity extends Activity {
    // 声明UI对象
    Button bt_login = null;
    EditText et_account = null;
    EditText et_password = null;
    TextView tv_to_register = null;
    TextView tv_forget_password = null;
    TextView tv_service_agreement = null;
    private SharedPreferences sp;
    private myAsyncTast tast;
    private String account;
    private String password;
    private long firstTime = 0;
    private ProgressDialog dialog = null;
    @SuppressLint("WorldReadableFiles")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        if (ContextCompat.checkSelfPermission(LoginActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{ Manifest.permission. WRITE_EXTERNAL_STORAGE }, 1);
        }
        sp = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        //检查初始状态
        initUI();
        //登陆
        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                account = et_account.getText().toString();
                password =et_password.getText().toString();
                if (account.equals("")){
                    Toast.makeText(LoginActivity.this,getString(R.string.login_zh),Toast.LENGTH_SHORT).show();
                }else if (password.equals("")) {
                    Toast.makeText(LoginActivity.this,getString(R.string.login_password),Toast.LENGTH_SHORT).show();
                }else {
                    //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                   // startActivity(intent);
                    tast = new myAsyncTast();//创建AsyncTask
                    tast.execute();//启动AsyncTask
                }
            }
        });
        tv_to_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it_login_to_register = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(it_login_to_register);
            }
        });
        tv_forget_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,PasswordChangeActivity.class);
                startActivity(intent);
            }
        });
        if(sp.getString("USER_NAME", "")!="") {
            checkInitStatus(sp);
        }
        tv_service_agreement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSeriviceDialog();
            }
        });

    }
    private void showSeriviceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("用户协议");// 设置标题
        builder.setMessage(Html.fromHtml(getString(R.string.user_agreement)));// 为对话框设置内容
        // 为对话框设置确定按钮
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });
        builder.create().show();// 使用show()方法显示对话框
    }
    class myAsyncTast extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(LoginActivity.this, "登录提示", "正在登录，请稍等...", false);//创建ProgressDialog
        }
        @Override
        protected Void doInBackground(Void... arg0) {
            User user = UserLab.get(LoginActivity.this,account,password).getUser();
            int n;
            if(user!=null){
                n = 1;
            }else {
                n = 0;
            }
            publishProgress(n);
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            dialog.dismiss();//关闭ProgressDialog
            if(values[0]==1){
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("USER_NAME", account);
                editor.putString("PASSWORD", password);
                editor.apply();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                LoginActivity.this.finish();
            }else{
                Toast.makeText(LoginActivity.this,getString(R.string.login_failure),Toast.LENGTH_SHORT).show();
            }
        }
    }
    /**
     * 初始检查记住密码状态以及是否设置自动登录.
     */
    private void checkInitStatus(SharedPreferences sp) {
        et_account.setText(sp.getString("USER_NAME", ""));
        et_password.setText(sp.getString("PASSWORD", ""));
        account = et_account.getText().toString();
        password =et_password.getText().toString();
        //Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        //startActivity(intent);
        tast = new myAsyncTast();//创建AsyncTask
        tast.execute();//启动AsyncTask
    }
    // 初始化UI对象
    private void initUI() {
        bt_login = findViewById(R.id.bt_login); // 登录按钮
        et_account = findViewById(R.id.et_account); // 输入账号
        et_password = findViewById(R.id.et_password); // 输入密码
        tv_to_register = findViewById(R.id.tv_to_register); // 注册
        tv_forget_password = findViewById(R.id.tv_forget_password); // 忘记密码
        tv_service_agreement = findViewById(R.id.tv_service_agreement); // 同意协议
    }
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            Toast.makeText(LoginActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            firstTime = secondTime;
        } else {
            finish();
            System.exit(0);
        }
    }
}
