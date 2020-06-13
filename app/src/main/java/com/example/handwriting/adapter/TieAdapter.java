package com.example.handwriting.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handwriting.R;
import com.example.handwriting.activity.TieActivity;
import com.example.handwriting.bean.Pic;
import com.example.handwriting.bean.Tie;
import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TieAdapter extends RecyclerView.Adapter<TieAdapter.ViewHolder>{
    private List<Tie> mWList;
    private List<Pic>wlist;
    private int flag;
    private Context context;

    static class ViewHolder extends RecyclerView.ViewHolder {
        View PhbView;
        ImageView tu;
        TextView uName;
        TextView ss,tieTime;
        ProgressBar progressBar;

        public ViewHolder(final View view) {
            super(view);
            PhbView = view;
            progressBar=(ProgressBar)view.findViewById(R.id.tie_pro);
            tu=(ImageView)view.findViewById(R.id.t_tu);
            uName = (TextView) view.findViewById(R.id.t_name);
            ss = (TextView) view.findViewById(R.id.tie_name);
            tieTime=(TextView)view.findViewById(R.id.tie_time);
        }
    }

    public TieAdapter(int f, List<Tie> tieList, List<Pic> list, Context context1) {
        mWList = tieList;
        context = context1;
        wlist=list;
        flag=f;
    }

    @NonNull
    @Override
    public TieAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tie, parent, false);
        final TieAdapter.ViewHolder holder = new TieAdapter.ViewHolder(view);
        holder.PhbView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                Tie tie = mWList.get(position);
                Gson gson = new Gson();
                String jsonStr = gson.toJson(tie);
                Intent intent=new Intent(v.getContext(), TieActivity.class);
                intent.putExtra("tie",jsonStr);
                intent.putExtra("flag",flag+"");
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(TieAdapter.ViewHolder holder, int position) {
        Tie tie = mWList.get(position);
        holder.uName.setText(tie.getUserName());
        holder.ss.setText(tie.getName());
        holder.tieTime.setText(TimeCompare(tie.getTieDate()));
        for(int i=0;i<wlist.size();i++){
            if(wlist.get(i).getId().equals(tie.getPic())) {
                if(wlist.get(i).getBitmap()!=null){
                    holder.progressBar.setVisibility(View.GONE);
                }
                holder.tu.setImageBitmap(wlist.get(i).getBitmap());
                break;
            }
        }

    }

    private String TimeCompare(String date) {
        String timeStr="";
        //格式化时间
        DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        try {
            Date beginTime = simpleDateFormat.parse(date);
            Date endTime = simpleDateFormat.parse(str);
            long diff = endTime.getTime() - beginTime.getTime();
            long minute = diff / (1000 * 60 );
            //判断是否大于两天
            if(minute==0){
                timeStr="刚刚";
            }else if (minute <60) {
                timeStr=minute+"分钟前";
            } else if(minute>=60&&minute<60*24) {
                timeStr=minute/60+"小时前";
            }else if(minute>=60*24&&minute<60*24*30){
                timeStr=minute/(60*24)+"天前";
            }else if(minute>=60*24*30&&minute<60*24*365){
                timeStr=minute/(60*24*30)+"月前";
            }else timeStr=minute/(60*24*365)+"年前";

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return timeStr;
    }
    @Override
    public int getItemCount() {
        return mWList.size();
    }
}