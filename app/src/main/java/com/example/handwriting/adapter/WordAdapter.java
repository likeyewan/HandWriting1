package com.example.handwriting.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.handwriting.R;
import com.example.handwriting.bean.Word;
import java.io.IOException;
import java.util.List;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class WordAdapter extends RecyclerView.Adapter<WordAdapter.ViewHolder> {
    private List<Word> mWList;
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
    public WordAdapter(List<Word> wordList,Context context1){
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
                Word w=mWList.get(position);
                wordDialog(w);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(WordAdapter.ViewHolder holder, int position){
        AssetManager assetManager= context.getAssets();
        Word word =mWList.get(position);
        holder.name.setText(word.getName());
        holder.w_id.setText(""+(position+1)+".");
       // holder.name.setTypeface(Typeface.createFromAsset(assetManager, "font/kaiti.ttf"));
    }
    @Override
    public int getItemCount(){
        return mWList.size();
    }
    public void wordDialog(final Word word){
        AssetManager assetManager= context.getAssets();
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        View view=LayoutInflater.from(context).inflate(R.layout.dialog_word,null,false);
        builder.setView(view);
        final Dialog dialog=builder.create();
        TextView py=view.findViewById(R.id.dia_py);
        TextView bh=view.findViewById(R.id.dia_bh);
        TextView bs=view.findViewById(R.id.dia_bs);
        TextView zi=view.findViewById(R.id.dia_zi);
        ImageView laba=view.findViewById(R.id.dia_laba);
        zi.setTypeface(Typeface.createFromAsset(assetManager, "font/kaiti.ttf"));
        StringBuffer sb=new StringBuffer("");
        for(int k=0;k<word.getBishun().size();k++){
            sb.append(BiHua(word.getBishun().get(k)));
        }
        zi.setText(""+word.getName());
        py.setText(""+word.getPy());
        bh.setText(""+word.getBhs());
        bs.setText(""+sb);
        GifImageView mWebFlash = view.findViewById(R.id.dia_dt);
        laba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boF(word);
            }
        });
        try {
            GifDrawable gifFromPath = new GifDrawable("/storage/emulated/0/HandWriting/GIf/"+word.getName()+".gif");
            mWebFlash.setImageDrawable(gifFromPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.show();
    }
    //播放读音
    private void boF(Word word){
        try {
            MediaPlayer myPlayer=new MediaPlayer();
            myPlayer.reset();
            String s=word.getPy();
            String string="/storage/emulated/0/HandWriting/pinyin/"+s.replace(" ","")+".mp3";
            String o=string.replace("ǔ","ŭ");
            o=o.replace("ǎ","ă");
            o=o.replace("ǒ","ŏ");
            o=o.replace("ǐ","ĭ");
            o=o.replace("ě","ĕ");
            o=o.replace("v1","ǚ");
            o=o.replace("v2","ǚ");
            o=o.replace("ǚ","v3");
            o=o.replace("v4","ǚ");
            myPlayer.setDataSource(o);
            if (!myPlayer.isPlaying()) {
                myPlayer.prepare();
                myPlayer.start();
            } else {
                myPlayer.pause();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public String BiHua(int j){
        String tt="";
        if(j==1){
            tt= "一";
        }else
        if(j==2){
            tt= "丨";
        }else
        if(j==3){
            tt= "丿";
        }
        else if(j==4){
            tt="㇏";
        }else if(j==5){
            tt="㇀";
        }
        else if(j==6||j==61||j==62||j==63){
            tt="丶";
        }
        else if(j==7){
            tt="ㄱ";
        }
        else if(j==8){
            tt="フ";
        }
        else if(j==9){
            tt="乛";
        }else if(j==10){
            tt="㇗";
        }else if(j==11){
            tt="㇙";
        }else if(j==12){
            tt="㇚";
        }else if(j==13){
            tt="㇄";
        }else if(j==14){
            tt="㇜";
        }else if(j==15){
            tt="㇛";
        }else if(j==16){
            tt="㇃";
        }else if(j==17){
            tt="㇂";
        }else if(j==18){
            tt="㇁";
        }else if(j==19){
            tt="㇅";
        }else if(j==20){
            tt="㇊";
        }else if(j==21){
            tt="\uD840\uDCCC";
        }else if(j==22){
            tt="㇍";
        }else if(j==23){
            tt="㇞";
        }else if(j==24){
            tt="ㄣ";
        }else if(j==25){
            tt="㇟";
        }else if(j==26){
            tt="⺄";
        }else if(j==27){
            tt="㇎";
        }else if(j==28){
            tt="㇋";
        }else if(j==29){
            tt="㇈";
        }else if(j==30){
            tt="㇌";
        }else if(j==31){
            tt="㇉";
        }else if(j==32){
            tt="㇡";
        }else{
            tt="识别不出！";
        }
        return tt;
    }
}
