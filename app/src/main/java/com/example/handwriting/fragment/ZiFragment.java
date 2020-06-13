package com.example.handwriting.fragment;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handwriting.R;
import com.example.handwriting.adapter.PhbAdapter;
import com.example.handwriting.bean.Phb;
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

public class ZiFragment extends Fragment {
    List<Phb> phbList=new ArrayList<>();
    GetAsyncTask getAsyncTask;
    RecyclerView recyclerView;
    ProgressBar progressBar;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_zi, container, false);
        progressBar=(ProgressBar)v.findViewById(R.id.pro);
        recyclerView = (RecyclerView) v.findViewById(R.id.list_zi);
        LinearLayoutManager layoutManager = new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        PhbAdapter adapter = new PhbAdapter(phbList, 3, v.getContext());
        recyclerView.setAdapter(adapter);
        getAsyncTask=new GetAsyncTask();
        getAsyncTask.execute();
        return v;
    }
    public class GetAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute(){

        }
        @Override
        protected Boolean doInBackground(Void... params) {
            String url = getString(R.string.s_url_getPhbsByScore);
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("FLAG",""+3)
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
                    phbList = gson1.fromJson(s, new TypeToken<List<Phb>>(){}.getType());
                    //Log.d("dsa","phbs="+phbs);
                }
            });
            try {
                while(true) {
                    if (phbList.size() > 0) {
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
                progressBar.setVisibility(View.GONE);
                PhbAdapter adapter = new PhbAdapter(phbList, 3, getContext());
                recyclerView.setAdapter(adapter);
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

}
