package com.example.handwriting.activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import com.example.handwriting.R;
import com.example.handwriting.adapter.TieAdapter;
import com.example.handwriting.bean.Pic;
import com.example.handwriting.bean.Tie;
import com.example.handwriting.bean.User;
import com.example.handwriting.bean.UserLab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class MyTieActivity extends Activity {
    RecyclerView recyclerView;
    TextView title;
    ProgressBar progressBar;
    List<Pic> listb=new ArrayList<>();
    List<Tie>tieList=new ArrayList<>();
    User user;
    public void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_mytie);
        recyclerView = (RecyclerView) findViewById(R.id.mytie_view);
        progressBar=(ProgressBar)findViewById(R.id.mytie_pro);
        title=(TextView)findViewById(R.id.tv_title);
        title.setText("我的帖子");
        user= UserLab.get(MyTieActivity.this,"","").getUser();
        GetMyTieAsyncTask getTieAsyncTask=new GetMyTieAsyncTask();
        getTieAsyncTask.execute();
    }

    private void n(final String n) {
        String url="http://192.168.137.1:8080/HSKHandWrting/pic/"+n+".jpg";
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request
                .Builder()
                .get()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                //将图片显示到ImageView中
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                Pic pic=new Pic();
                pic.setId(n);
                pic.setBitmap(bitmap);
                listb.add(pic);
                Log.d("b","d="+n);
                inputStream.close();
            }
        });
    }
    public class GetTuAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            for(int i=0;i<tieList.size();i++) {
                n(tieList.get(i).getPic());
            }
            try {
                while (true) {
                    if (listb.size() ==tieList.size()) {
                        break;
                    }
                }
            } catch (Exception e) {
                return false;
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (result) {
                progressBar.setVisibility(View.GONE);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
                        recyclerView.setLayoutManager(layoutManager);
                        TieAdapter adapter = new TieAdapter(2,tieList, listb, MyTieActivity.this);
                        recyclerView.setAdapter(adapter);
                    }
                });
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }
    public class GetMyTieAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute(){
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            String url = getString(R.string.s_url_getMyTie);
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("userName",user.getNickname())
                    .build();
            final Request request = new Request.Builder()
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
                    Gson gson1=new Gson();
                    tieList = gson1.fromJson(s, new TypeToken<List<Tie>>(){}.getType());
                }
            });
            try {
                while (true) {
                    if (tieList.size() > 0) {
                        Log.d("dsa", "phb");
                        break;
                    }
                }
            }catch  (Exception e){
                Log.d("dsa", "phb");
                return false;
            }
            return true;

        }
        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                GetTuAsyncTask getTuAsyncTask=new GetTuAsyncTask();
                getTuAsyncTask.execute();
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }
}
