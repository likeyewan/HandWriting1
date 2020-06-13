package com.example.handwriting.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.handwriting.other.GetData;
import com.example.handwriting.R;
import com.example.handwriting.adapter.WWordAdapter;
import com.example.handwriting.bean.CWord;
import com.example.handwriting.bean.User;
import com.example.handwriting.bean.UserLab;
import com.example.handwriting.bean.Word;
import com.example.handwriting.db.HanZi;
import com.example.handwriting.db.WWord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CuoziActivity extends Activity {
    private List<Word> cuo=new ArrayList<>();
    private List<CWord>cWords=new ArrayList<>();
    ImageView tb;
    TextView title;
    User user;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cuozi);
        user= UserLab.get(CuoziActivity.this,"","").getUser();
        //GetWWordAsyncTask getWWordAsyncTask=new GetWWordAsyncTask();
        //getWWordAsyncTask.execute();
        title=(TextView)findViewById(R.id.tv_title);
        tb=(ImageView)findViewById(R.id.title_back);
        title.setText("错字集");
        recyclerView=(RecyclerView)findViewById(R.id.re_view);
        List<WWord>listw=DataSupport.where("userPhone = ?",user.getPhoneNum()).find(WWord.class);
        Gson gson=new Gson();
        if(listw.size()>0) {
            cWords=gson.fromJson(listw.get(0).getWrong_word(),new TypeToken<List<CWord>>() {
            }.getType());
          /*  Log.d("ds", "d=" + list.size());
            List<HanZi> hanZiList = DataSupport.findAll(HanZi.class);
            if (hanZiList.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    for (int j = 0; j < hanZiList.size(); j++) {
                        if (list.get(i).equals(hanZiList.get(j).getName())) {
                            Word word = new Word();
                            word.setId(hanZiList.get(j).getId());
                            word.setName(hanZiList.get(j).getName());
                            word.setPy(hanZiList.get(j).getPy());
                            word.setBhs(hanZiList.get(j).getBhs());
                            word.setBushou(hanZiList.get(j).getBushou());
                            Gson gson5 = new Gson();
                            List<Integer> ls = gson5.fromJson(hanZiList.get(j).getBishun(), new TypeToken<List<Integer>>() {
                            }.getType());
                            word.setBishun(ls);
                            cuo.add(word);
                        }
                    }
                }
            }*/
        }
        StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        WWordAdapter adapter=new WWordAdapter(cWords,CuoziActivity.this);
        recyclerView.setAdapter(adapter);
        tb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CuoziActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }
    public class GetWWordAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute(){
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            String url = getString(R.string.s_url_getWWord);
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("phone",user.getPhoneNum())
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
                    Gson gson=new Gson();
                    List<String> list=gson.fromJson(s, new TypeToken<List<String>>(){}.getType());
                    List<HanZi> hanZiList= DataSupport.findAll(HanZi.class);
                    if(hanZiList.size()>0) {
                        for (int i = 0; i < list.size(); i++) {
                            for (int j = 0; j < hanZiList.size(); j++) {
                                if (list.get(i).equals(hanZiList.get(j).getName())) {
                                    Word word = new Word();
                                    word.setId(hanZiList.get(j).getId());
                                    word.setName(hanZiList.get(j).getName());
                                    word.setPy(hanZiList.get(j).getPy());
                                    word.setBhs(hanZiList.get(j).getBhs());
                                    word.setBushou(hanZiList.get(j).getBushou());
                                    Gson gson5 = new Gson();
                                    List<Integer> ls = gson5.fromJson(hanZiList.get(j).getBishun(), new TypeToken<List<Integer>>() {
                                    }.getType());
                                    word.setBishun(ls);
                                    cuo.add(word);
                                    break;
                                }
                            }
                        }
                    }
                }
            });
            try {
                while(true) {
                    if (cuo.size() > 0) {
                        break;
                    }
                }
            }catch (Exception e){
                return false;
            }
            return true;

        }
        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                //progressBar.setVisibility(View.GONE);
                StaggeredGridLayoutManager layoutManager=new StaggeredGridLayoutManager(5,StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                WWordAdapter adapter=new WWordAdapter(cWords,CuoziActivity.this);
                recyclerView.setAdapter(adapter);
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
    @Override
    //安卓重写返回键事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
