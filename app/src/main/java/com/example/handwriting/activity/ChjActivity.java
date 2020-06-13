package com.example.handwriting.activity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handwriting.R;
import com.example.handwriting.adapter.AcheiAdapter;
import com.example.handwriting.bean.Achei;
import com.example.handwriting.bean.Phb;
import com.example.handwriting.bean.User;
import com.example.handwriting.bean.UserLab;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

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

public class ChjActivity extends Activity {
    private TextView score,day,zi,title;
    GetMAsyncTask getMAsyncTask;
    Phb phb=new Phb();
    List<Achei> acheiList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chj);
        score=(TextView)findViewById(R.id.chj_fs);
        day=(TextView)findViewById(R.id.chj_day);
        zi=(TextView)findViewById(R.id.chj_zi);
        title=(TextView)findViewById(R.id.tv_title);
        title.setText("成就");
        getMAsyncTask=new GetMAsyncTask();
        getMAsyncTask.execute();
        String[]strings={"初窥门径","登堂入室","驾轻就熟","心领神会","完美无瑕"};
        int[]fs={R.mipmap.c_1,R.mipmap.c_2,R.mipmap.c_3,R.mipmap.c_4,R.mipmap.c_5};
        int[]fs1={R.mipmap.c_11,R.mipmap.c_22,R.mipmap.c_33,R.mipmap.c_44,R.mipmap.c_55};
        for(int i=1;i<=5;i++){
            Achei achei=new Achei();
            achei.setName(strings[i-1]);
            achei.setNeeds(50+10*i);
            achei.setType(1);
            achei.setState(0);
            achei.setPhoto(fs[i-1]);
            achei.setPhotoN(fs1[i-1]);
            acheiList.add(achei);
        }
        String[]s1={"初学乍练","坚持不懈","持之以恒","傲视群雄","返璞归真"};
        int[]ts={R.mipmap.c_6,R.mipmap.c_7,R.mipmap.c_8,R.mipmap.c_9,R.mipmap.c_10};
        int[]ts1={R.mipmap.c_66,R.mipmap.c_77,R.mipmap.c_88,R.mipmap.c_99,R.mipmap.c_1010};
        int[]tt={1,10,50,250,1000};
        for(int i=1;i<=5;i++){
            Achei achei=new Achei();
            achei.setName(s1[i-1]);
            achei.setNeeds(tt[i-1]);
            achei.setType(2);
            achei.setState(0);
            achei.setPhoto(ts[i-1]);
            achei.setPhotoN(ts1[i-1]);
            acheiList.add(achei);
        }
        String[]s={"学徒","学民","学霸","学圣","学神"};
        int[]zs={R.mipmap.c_a,R.mipmap.c_b,R.mipmap.c_c,R.mipmap.c_d,R.mipmap.c_e};
        int[]zs1={R.mipmap.c_aa,R.mipmap.c_bb,R.mipmap.c_cc,R.mipmap.c_dd,R.mipmap.c_ee};
        for(int i=1;i<=5;i++){
            Achei achei=new Achei();
            achei.setName(s[i-1]);
            achei.setNeeds((int)Math.pow(10,i));
            achei.setType(3);
            achei.setState(0);
            achei.setPhoto(zs[i-1]);
            achei.setPhotoN(zs1[i-1]);
            acheiList.add(achei);
        }
    }
    public class GetMAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute(){
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            String url = getString(R.string.s_url_getPhb);
            User user = UserLab.get(ChjActivity.this,"","").getUser();
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("NAME",""+user.getNickname())
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
                    phb = gson1.fromJson(s, new TypeToken<Phb>(){}.getType());
                }
            });
            try {
                while(true) {
                    if (phb.getName()!=null) {
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
                RecyclerView recyclerView=(RecyclerView)findViewById(R.id.chj_view);
                LinearLayoutManager layoutManager=new LinearLayoutManager(ChjActivity.this);
                recyclerView.setLayoutManager(layoutManager);
                AcheiAdapter adapter=new AcheiAdapter(acheiList,phb,1,ChjActivity.this);
                recyclerView.setAdapter(adapter);
                score.setText(""+phb.getScore());
                day.setText(""+phb.getDay());
                zi.setText(""+phb.getZi());
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
}
