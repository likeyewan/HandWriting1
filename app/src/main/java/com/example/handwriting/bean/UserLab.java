package com.example.handwriting.bean;

import android.content.Context;
import android.util.Log;
import com.example.handwriting.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static android.content.ContentValues.TAG;
public class UserLab {
private static UserLab sUserLab;
private User mUser;
private static boolean flag = false;
    /*
    要创建单例，需要创建一个带有私有构造方法及get()方法的类。
    如果实例已存在，直接返回，不存在则调用构造方法创建它。
     */
public static UserLab get(Context context, String account, String password){
        if (sUserLab==null || !flag){
        sUserLab = new UserLab(context,account,password);
        }
       /* User user=new User();
        user.setSex("男");
        user.setRole(1);
        user.setPhoneNum("15546");
        user.setNickname("sdd");
        user.setPassword("dd");
        user.setSign("sdf");
        user.setPhoto("dsf");
        sUserLab.setUser(user);*/
        return sUserLab;
        }
private UserLab(final Context context, String account, String password){
        String url = context.getString(R.string.s_url_login);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
        .add("ACCOUNT", account)
        .add("PASSWORD",password)
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
        Type type = new TypeToken<User>(){}.getType();
        mUser = new Gson().fromJson(s, type);
        Log.d(TAG, "onResponse: " + s);
        if (mUser != null){
        flag = true;
        }
}});
        try {
        Thread.sleep(2000);
        } catch (InterruptedException e) {
        e.printStackTrace();
        }
        }
public User getUser(){
        return mUser;
        }
public void setUser(User user){
        mUser = user;
        }
}
