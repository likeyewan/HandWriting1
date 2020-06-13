package com.example.handwriting.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handwriting.R;
import com.example.handwriting.bean.Phb;
import com.example.handwriting.bean.Word;

import java.util.List;

public class PhbAdapter extends RecyclerView.Adapter<PhbAdapter.ViewHolder>{
    private List<Phb> mWList;
    private Context context;
    private  int flag;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View PhbView;
        TextView name;
        TextView w_id;
        TextView jg;

        public ViewHolder(View view) {
            super(view);
            PhbView = view;
            name = (TextView) view.findViewById(R.id.name);
            w_id = (TextView) view.findViewById(R.id.name_id);
            jg=(TextView)view.findViewById(R.id.fs);
        }
    }

    public PhbAdapter(List<Phb> wordList,int f, Context context1) {
        mWList = wordList;
        flag=f;
        context = context1;
    }

    @NonNull
    @Override
    public PhbAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_fs, parent, false);
        final PhbAdapter.ViewHolder holder = new PhbAdapter.ViewHolder(view);
        holder.PhbView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Phb w = mWList.get(position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(PhbAdapter.ViewHolder holder, int position) {
        AssetManager assetManager = context.getAssets();
        Phb phb = mWList.get(position);
        holder.name.setText(phb.getName());
        holder.w_id.setText("" + (position + 1));
        String s="";
        if(flag==1) {
           s=phb.getScore()+"";
        }else if(flag==2){
            s=phb.getDay()+"";
        }
        else s=phb.getZi()+"";
        holder.jg.setText(s);
        holder.name.setTypeface(Typeface.createFromAsset(assetManager, "font/kaiti.ttf"));
    }

    @Override
    public int getItemCount() {
        return mWList.size();
    }
}