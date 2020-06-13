package com.example.handwriting.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.handwriting.R;
import com.example.handwriting.bean.CWord;
import com.example.handwriting.activity.CuoTestActivity;

import java.util.List;

public class WWordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder>{
    private List<CWord> mWList;
    private Context context;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View WordView;
        TextView name;
        TextView w_id;
        public ViewHolder(View view){
            super(view);
            WordView=view;
            name=(TextView)view.findViewById(R.id.text_list_item);
            w_id=(TextView)view.findViewById(R.id.text_id);
        }
    }
    public WWordAdapter(List<CWord> wordList,Context context1){
        mWList= wordList;
        context=context1;
    }

    @NonNull
    @Override
    public WordAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        final WordAdapter.ViewHolder holder=new WordAdapter.ViewHolder(view);
        holder.WordView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                CWord w=mWList.get(position);
                Log.d("aa","d="+w.getName());
                Intent intent=new Intent(context, CuoTestActivity.class);
                intent.putExtra("name",w.getName());
                intent.putExtra("msg",w.getWrong_msg());
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(WordAdapter.ViewHolder holder, int position){
        CWord word =mWList.get(position);
        holder.name.setText(word.getName());
        holder.w_id.setText(""+(position+1)+".");
    }
    @Override
    public int getItemCount(){
        return mWList.size();
    }
}
