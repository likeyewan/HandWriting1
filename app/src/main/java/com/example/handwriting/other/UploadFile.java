package com.example.handwriting.other;

import android.util.Log;
import com.example.handwriting.bean.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class UploadFile {
    public static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
    private String state = "";

    public String fileUpload(final String path, final String name) {
        // 获得输入框中的路径
        final File file = new File(path);
        OkHttpClient client = new OkHttpClient();
        // 上传文件使用MultipartBody.Builder
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", name) // 提交普通字段
                .addFormDataPart("image", file.getName(), RequestBody.create(MEDIA_TYPE_PNG, file)) // 提交图片，第一个参数是键（name="第一个参数"），第二个参数是文件名，第三个是一个RequestBody
                .build();
        // POST请求
        Request request = new Request.Builder()
                .url("http://192.168.137.1:8080/HSKHandWrting/Dservlet")
                .post(requestBody)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                String s = null;
                try {
                    s = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(s);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        state = jsonObject.getString("FILENAME");
                        if(state.equals("success")) {
                            File file1 = new File(path);
                            file1.delete();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }
        });
        return state;
    }

    public void fileDownload(String path,String name) {
        String url = "http://192.168.137.1:8080/HSKHandWrting/DownloadFile";
        final String destFileDir= "/storage/emulated/0/HandWriting/Picture/";
        final String destFileName=name+".jpg";
        OkHttpClient okHttpClient = new OkHttpClient();
        Log.d(TAG, "ppppp " + path);
        RequestBody requestBody = new FormBody.Builder()
                .add("PATH", path)
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
            public void onResponse(Call call, final Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                FileOutputStream fileOutputStream = null;
                try {
                    fileOutputStream = new FileOutputStream(new File(destFileDir+destFileName));
                    byte[] buffer = new byte[2048];
                    int len = 0;
                    while ((len = inputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, len);
                    }
                    fileOutputStream.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}

