package com.example.handwriting.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Fragment;

import com.example.handwriting.R;
import com.example.handwriting.adapter.WordAdapter;
import com.example.handwriting.bean.User;
import com.example.handwriting.bean.UserLab;
import com.example.handwriting.bean.Word;
import com.example.handwriting.db.HanZi;
import com.example.handwriting.db.Plan;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class Fragment1 extends Fragment {
    private List<Word> cuo=new ArrayList<>();
    private List<Word> weiWord=new ArrayList<>();
    int flag=1;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_wc,container,false);
        List<Word> wo = new ArrayList<>();
        List<HanZi> hanZiList= DataSupport.findAll(HanZi.class);
        if(hanZiList.size()>0){
            for(int i=0;i<hanZiList.size();i++) {
                Word word = new Word();
                word.setId(hanZiList.get(i).getId());
                word.setName(hanZiList.get(i).getName());
                word.setPy(hanZiList.get(i).getPy());
                word.setBhs(hanZiList.get(i).getBhs());
                word.setBushou(hanZiList.get(i).getBushou());
                Gson gson5=new Gson();
                List<Integer> ls=gson5.fromJson(hanZiList.get(i).getBishun(), new TypeToken<List<Integer>>(){}.getType());
                word.setBishun(ls);
                wo.add(word);
            }
        }
        if(wo!=null) {
            cuo=wo;
        }
        User user=UserLab.get(v.getContext(),"","").getUser();
        List<Plan> plans= DataSupport.where("name = ? and cc = ?",user.getPhoneNum(),"1").find(Plan.class);
        if(flag==1) {
            for (int i = 0; i < plans.get(0).getLearn(); i++) {
                weiWord.add(cuo.get(i));
            }
            flag=0;
        }
        RecyclerView recyclerView=(RecyclerView)v.findViewById(R.id.list_wc);
        LinearLayoutManager layoutManager=new LinearLayoutManager(v.getContext());
        recyclerView.setLayoutManager(layoutManager);
        WordAdapter adapter=new WordAdapter(weiWord, v.getContext());
        recyclerView.setAdapter(adapter);
        return v;
    }

}
