package com.example.handwriting.adapter;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handwriting.R;
import com.example.handwriting.bean.Achei;
import com.example.handwriting.bean.Phb;

import java.util.List;

public class AcheiAdapter extends RecyclerView.Adapter<AcheiAdapter.ViewHolder>{
    private List<Achei> mWList;
    private Context context;
    private  int flag;
    private Phb phb;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View AcheiView;
        TextView name;
        ImageView imageView;
        ProgressBar progressBar;
        TextView need;
        TextView now;
        LinearLayout linearLayout;
        TextView textView;

        public ViewHolder(View view) {
            super(view);
            AcheiView = view;
            name=(TextView)view.findViewById(R.id.chj_name);
            imageView=(ImageView)view.findViewById(R.id.chj_pic);
            progressBar=(ProgressBar)view.findViewById(R.id.chj_p);
            need=(TextView)view.findViewById(R.id.chj_all);
            now=(TextView)view.findViewById(R.id.chj_pro);
            linearLayout=(LinearLayout)view.findViewById(R.id.chj_jd);
            textView=(TextView)view.findViewById(R.id.chj_hd);
        }
    }

    public AcheiAdapter(List<Achei> acheiList,Phb p,int f, Context context1) {
        mWList = acheiList;
        phb=p;
        flag=f;
        context = context1;
    }

    @NonNull
    @Override
    public AcheiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chj, parent, false);
        final AcheiAdapter.ViewHolder holder = new AcheiAdapter.ViewHolder(view);
        holder.AcheiView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Achei w = mWList.get(position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(AcheiAdapter.ViewHolder holder, int position) {
        AssetManager assetManager = context.getAssets();
        Achei achei = mWList.get(position);
        holder.name.setText(achei.getName());
        holder.need.setText(""+achei.getNeeds());
        holder.progressBar.setMax(achei.getNeeds());
        holder.imageView.setImageResource(achei.getPhotoN());
        if(achei.getType()==1){
            holder.now.setText(""+phb.getScore());
            holder.progressBar.setProgress(phb.getScore());
            if(phb.getScore()>=achei.getNeeds()){
                holder.now.setText(""+achei.getNeeds());
                holder.imageView.setImageResource(achei.getPhoto());
                holder.textView.setVisibility(View.VISIBLE);
            }else{
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.textView.setVisibility(View.GONE);
            }
        }else if(achei.getType()==2){
            holder.now.setText(""+phb.getDay());
            holder.progressBar.setProgress(phb.getDay());
            if(phb.getDay()>=achei.getNeeds()){
                holder.imageView.setImageResource(achei.getPhoto());
                holder.now.setText(""+achei.getNeeds());
                holder.textView.setVisibility(View.VISIBLE);
            }else{
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.textView.setVisibility(View.GONE);
            }
        }else if(achei.getType()==3) {
            holder.now.setText(""+phb.getZi());
            holder.progressBar.setProgress(phb.getZi());
            if(phb.getZi()>=achei.getNeeds()){
                holder.imageView.setImageResource(achei.getPhoto());
                holder.now.setText(""+achei.getNeeds());
                holder.textView.setVisibility(View.VISIBLE);
            }else{
                holder.linearLayout.setVisibility(View.VISIBLE);
                holder.textView.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public int getItemCount() {
        return mWList.size();
    }
}
