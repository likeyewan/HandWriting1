package com.example.handwriting.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.handwriting.R;
import com.example.handwriting.bean.User;
import com.example.handwriting.bean.UserLab;
import com.example.handwriting.other.CountDownTimerUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class RegisterActivity extends Activity {
    private EditText nameET;
    private EditText phoneNumET;
    private EditText passwordET;
    private EditText confirmET;
    private EditText codeET;
    private Button mButton;
    private Button getCode;
    private TextView title;
    private CountDownTimerUtils countDownTimerUtils;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initUI();
        countDownTimerUtils=new CountDownTimerUtils(getCode,60000,1000);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password=passwordET.getText().toString();

                finishSubmit();
            }
        });
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDownTimerUtils.start();
                if(getcode()==0){
                    countDownTimerUtils.cancel();
                }
            }
        });
    }
    private void initUI(){
        nameET = (EditText)findViewById(R.id.et_username);
        phoneNumET = (EditText)findViewById(R.id.et_telphone);
        passwordET = (EditText)findViewById(R.id.et_password);
        confirmET = (EditText)findViewById(R.id.et_password2);
        codeET=(EditText)findViewById(R.id.et_otpCode);
        mButton = (Button)findViewById(R.id.bt_submit_register);
        getCode=(Button)findViewById(R.id.bt_get_otp);
        title=(TextView)findViewById(R.id.tv_title);
        title.setText("注册");
    }
    private void setPhb(String name){
        String url = getString(R.string.s_url_addPhb);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("NAME", name)
                .add("SCORE",""+0)
                .add("DAY",""+0)
                .add("ZI",""+0)
                .build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                JSONArray jsonArray = null;
                try {
                    String state = "";
                    jsonArray = new JSONArray(s);
                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        state = jsonObject.getString("STATE");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onResponse: " + s);
            }
        });
    }
    private int getcode(){
        String phoneNum = phoneNumET.getText().toString();
        if (phoneNum.equals("")){
            Toast.makeText(RegisterActivity.this,"请输入手机号", Toast.LENGTH_LONG).show();
            return 0;
        }else if (isTelphoneValid(phoneNum)==false){
            Toast.makeText(RegisterActivity.this,"手机号格式不对！", Toast.LENGTH_LONG).show();
            return 0;
        } else {
            String url = getString(R.string.s_url_getcode);
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("PHONENUM", phoneNum)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    JSONArray jsonArray = null;
                    try {
                        String state = "";
                        jsonArray = new JSONArray(s);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            state = jsonObject.getString("STATE");
                        }
                        if (state.equals("success")) {
                            RegisterActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, getString(R.string.register_get_code), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            RegisterActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this, getString(R.string.register_failure), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "onResponse: " + s);
                }
            });
            return 1;
        }
    }
    // 校验账号不能为空且必须是中国大陆手机号
    private boolean isTelphoneValid(String account) {
        if (account == null) {
            return false;
        }
        // 首位为1, 第二位为3-9, 剩下九位为 0-9, 共11位数字
        String pattern = "^[1]([3-9])[0-9]{9}$";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(account);
        return m.matches();
    }
    // 校验密码不少于6位
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
    private void finishSubmit(){
        String nickname = nameET.getText().toString();
        String phoneNum = phoneNumET.getText().toString();
        String password = passwordET.getText().toString();
        String confirm = confirmET.getText().toString();
        String code=codeET.getText().toString();
        if (nickname.equals("")){
            Toast.makeText(RegisterActivity.this, getString(R.string.register_input_nickname), Toast.LENGTH_SHORT).show();
        }else if (phoneNum.equals("")){
            Toast.makeText(RegisterActivity.this,getString(R.string.register_input_phone_num),Toast.LENGTH_SHORT).show();
        }else if(code.equals("")) {
            Toast.makeText(RegisterActivity.this,getString(R.string.register_input_code),Toast.LENGTH_SHORT).show();
        }else if (password.equals("")){
            Toast.makeText(RegisterActivity.this,getString(R.string.register_input_password),Toast.LENGTH_SHORT).show();
        } else if (isPasswordValid(password)==false){
            Toast.makeText(RegisterActivity.this,"密码长度不能少于6位", Toast.LENGTH_LONG).show();
        }else if (confirm.equals("")){
            Toast.makeText(RegisterActivity.this,getString(R.string.register_input_confirm_password),Toast.LENGTH_SHORT).show();
        }else if (!confirm.equals(password)){
            Toast.makeText(RegisterActivity.this,getString(R.string.register_input_password_wrong),Toast.LENGTH_SHORT).show();
        }else {
            String url = getString(R.string.s_url_register);
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("NICKNAME", nickname)
                    .add("PASSWORD",password)
                    .add("PHONENUM",phoneNum)
                    .add("CODE",code)
                    .build();
            Request request = new Request.Builder()
                    .url(url)
                    .post(requestBody)
                    .build();
            okHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                }
                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String s = response.body().string();
                    JSONArray jsonArray = null;
                    try {
                        String state = "";
                        jsonArray = new JSONArray(s);
                        for (int i = 0 ; i < jsonArray.length() ; i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            state = jsonObject.getString("STATE");
                        }
                        if (state.equals("success")){
                           RegisterActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this,getString(R.string.register_success),Toast.LENGTH_SHORT).show();
                                }
                            });
                           setPhb(nameET.getText().toString());
                            User user = new User();
                            user.setNickname(nameET.getText().toString());
                            user.setPassword(passwordET.getText().toString());
                            user.setPhoneNum(phoneNumET.getText().toString());
                            user.setRole(1);
                            user.setSex("男");
                            UserLab.get(RegisterActivity.this,phoneNumET.getText().toString(),passwordET.getText().toString()).setUser(user);
                            RegisterActivity.this.finish();
                        }else {
                            RegisterActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(RegisterActivity.this,getString(R.string.register_failure),Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "onResponse: " + s);
                }
            });
        }
    }

}
