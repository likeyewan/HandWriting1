package com.example.handwriting.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.handwriting.other.CheckBH;
import com.example.handwriting.view.MyView;
import com.example.handwriting.R;
import com.example.handwriting.bean.CWord;
import com.example.handwriting.bean.User;
import com.example.handwriting.bean.UserLab;
import com.example.handwriting.bean.Word;
import com.example.handwriting.bean.WordPoint;
import com.example.handwriting.db.HanZi;
import com.example.handwriting.db.WWord;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.litepal.crud.DataSupport;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class CuoTestActivity extends Activity {
    TextView py,bh,bs,zi,title,yixue,weixue,msg;
    LinearLayout linearLayout;
    Button re,wc;
    ImageView laba;
    String da_an="";
    private MediaPlayer myPlayer=new MediaPlayer();
    List<List<WordPoint>> itemPointList;
    List<WordPoint> pointList;
    ImageView titleBack;
    static List<List<List<WordPoint>>>  listAllPoint=new ArrayList<List<List<WordPoint>>>();
    MyView myView;
    User user;
    String wrongWord="";
    String wrongMsg="";
    Word word;
    public void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_study);
        Intent intent=getIntent();
        wrongWord=intent.getStringExtra("name");
        wrongMsg=intent.getStringExtra("msg");
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);}
        user= UserLab.get(CuoTestActivity.this,"","").getUser();
        init();

    }
    public void init(){
        AssetManager assetManager= CuoTestActivity.this.getAssets();
        title=(TextView)findViewById(R.id.tv_title);
        py=(TextView)findViewById(R.id.s_py);
        bh=(TextView)findViewById(R.id.s_bh);
        bs=(TextView)findViewById(R.id.s_bs);
        zi=(TextView)findViewById(R.id.s_zi);
        re=(Button)findViewById(R.id.s_re);
        wc=(Button)findViewById(R.id.s_wc);
        msg=(TextView)findViewById(R.id.w_msg);
        linearLayout=(LinearLayout)findViewById(R.id.ww);
        linearLayout.setVisibility(View.VISIBLE);
        msg.setText(wrongMsg);
        yixue=(TextView)findViewById(R.id.yi);
        weixue=(TextView)findViewById(R.id.wei);
        yixue.setVisibility(View.GONE);
        weixue.setVisibility(View.GONE);
        titleBack=(ImageView)findViewById(R.id.title_back);
        laba=(ImageView)findViewById(R.id.bofang);
        myView=(MyView)findViewById(R.id.s_myview);
        title.setText("错字练习");
        title.setTypeface(Typeface.createFromAsset(assetManager, "font/kaiti.ttf"));
        zi.setTypeface(Typeface.createFromAsset(assetManager, "font/kaiti.ttf"));
        List<HanZi> hanZiList=DataSupport.where("name = ?",wrongWord).find(HanZi.class);
        if(hanZiList.size()>0){
            for(int i=0;i<hanZiList.size();i++) {
                word = new Word();
                word.setId(hanZiList.get(i).getId());
                word.setName(hanZiList.get(i).getName());
                word.setPy(hanZiList.get(i).getPy());
                word.setBhs(hanZiList.get(i).getBhs());
                word.setBushou(hanZiList.get(i).getBushou());
                Gson gson5=new Gson();
                List<Integer> ls=gson5.fromJson(hanZiList.get(i).getBishun(), new TypeToken<List<Integer>>(){}.getType());
                word.setBishun(ls);
            }
        }
        StringBuffer sb=new StringBuffer("");
        for(int k=0;k<word.getBishun().size();k++){
            if(word.getBishun().get(k)>100) {
                sb.append(BiHua(word.getBishun().get(k) % 100));
            }else
                sb.append(BiHua(word.getBishun().get(k)));
        }
        zi.setText(""+word.getName());
        py.setText(""+word.getPy());
        bh.setText(""+word.getBhs());
        bs.setText(""+sb);
        myView.Txt=word.getName();
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReWrite();
            }
        });
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CuoTestActivity.this, CuoziActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        wc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(myView.listPointB.size()>0)
                {
                    itemPointList=new ArrayList<List<WordPoint>>();
                    for(List<WordPoint> listP : myView.listPointB)
                    {
                        pointList=new ArrayList<WordPoint>();
                        for(WordPoint p : listP)
                        {
                            pointList.add(p);
                        }
                        itemPointList.add(pointList);
                    }
                    listAllPoint.add(itemPointList);
                    CheckBH checkBH=new CheckBH();
                    StringBuffer sb1=new StringBuffer(" ");
                    String result="";
                    if(word.getName().equals(result)||true) {
                        if (itemPointList.size() == word.getBhs()) {
                            int iq = 1;
                            for (int j = 0; j < word.getBishun().size(); j++) {
                                if(word.getBishun().get(j)>100){
                                    int bh1=word.getBishun().get(j)/100;
                                    int bh2=word.getBishun().get(j)%100;
                                    if (bh1 != checkBH.cb(itemPointList.get(j))&&bh2 != checkBH.cb(itemPointList.get(j))) {
                                        if (iq == 0) sb1.append("、");
                                        sb1.append("" + (j + 1));
                                        iq = 0;
                                    }
                                }else {
                                    if (word.getBishun().get(j) != checkBH.cb(itemPointList.get(j))) {
                                        if (iq == 0) sb1.append("、");
                                        sb1.append("" + (j + 1));
                                        iq = 0;
                                    }
                                }
                            }
                            if (iq == 1) da_an = "书写正确！";
                            else {
                                da_an = "笔顺错误!" + "第" + sb1 + "笔写错," + "可点击左上角的汉字，查看正确的书写！";
                            }
                        } else {
                            da_an = "笔画数错误！" + "您输入的笔画数是" + itemPointList.size() + "," + "可点击左上角的汉字，查看正确的书写！";
                        }
                    }else{
                        da_an = "书写错误！您书写的汉字与目标汉字不符合！";
                    }
                    wcDialog();
                }
                else {
                    Toast toast=Toast.makeText(CuoTestActivity.this,null,Toast.LENGTH_SHORT);
                    toast.setText("请写字！");
                    toast.show();
                }
            }
        });
        zi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layDialog();
            }
        });
        laba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boF();
            }
        });
    }
    //播放读音
    private void boF(){
        try {
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
    private void wcDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(CuoTestActivity.this);
        View view= LayoutInflater.from(CuoTestActivity.this).inflate(R.layout.dialog_wan,null,false);
        builder.setView(view);
        final Dialog dialog=builder.create();
        Button cx=view.findViewById(R.id.d_once);
        final Button xyg=view.findViewById(R.id.d_w);
        TextView da=view.findViewById(R.id.d_t);
        cx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReWrite();
                dialog.dismiss();
            }
        });
        xyg.setText("完成");
        if(da_an.equals("书写正确！")) xyg.setText("移除");
        xyg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(da_an.equals("书写正确！")) removeDialog();
                dialog.dismiss();
            }
        });
        da.setText(""+da_an);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }
    private void removeDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(CuoTestActivity.this);
        View view= LayoutInflater.from(CuoTestActivity.this).inflate(R.layout.dialog_wan,null,false);
        builder.setView(view);
        final Dialog dialog=builder.create();
        Button cx=view.findViewById(R.id.d_once);
        final Button xyg=view.findViewById(R.id.d_w);
        TextView da=view.findViewById(R.id.d_t);
        cx.setText("取消");
        cx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReWrite();
                dialog.dismiss();
            }
        });
        xyg.setText("确定");
        xyg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<WWord>list2=DataSupport.where("userPhone = ?",user.getPhoneNum()).find(WWord.class);
                if(list2.size()>0){
                    Gson gson1=new Gson();
                    List<CWord> list3 = gson1.fromJson(list2.get(0).getWrong_word(), new TypeToken<List<CWord>>(){}.getType());
                    for(int i=0;i<list3.size();i++){
                        if(word.getName().equals(list3.get(i).getName())){
                            list3.remove(i);
                            break;
                        }
                    }
                    Gson gson = new Gson();
                    String str = gson.toJson(list3);
                    WWord wWord=new WWord();
                    wWord.setWrong_word(str);
                    wWord.updateAll("userPhone = ?",user.getPhoneNum());
                }
                dialog.dismiss();
                Intent intent = new Intent(CuoTestActivity.this, CuoziActivity.class);
                startActivity(intent);
                finish();
            }
        });
        da.setText("该汉字已掌握！是否将该其从错字集移除！");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }
    //笔画动态图对话框
    private void layDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CuoTestActivity.this);
        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(CuoTestActivity.this).inflate(
                R.layout.dialog_zi, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        final Dialog dialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        GifImageView mWebFlash = view.findViewById(R.id.gv_error);
        try {
            GifDrawable gifFromPath = new GifDrawable("/storage/emulated/0/HandWriting/GIf/"+word.getName()+".gif");
            mWebFlash.setImageDrawable(gifFromPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        dialog.show();
        final WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = 900;
        params.height = 900;
        dialog.getWindow().setAttributes(params);
    }
    //重写
    public void ReWrite()
    {
        //myView.listPoint.clear();
        myView.listPointB.clear();
        myView.mPointsB.clear();
        myView.mPointsB=new ArrayList<>();
      //  myView.listPoint=new ArrayList<List<WordPoint>>();
        myView.listPointB=new ArrayList<List<WordPoint>>();
        myView.invalidate();
        if (myView.cacheCanvas != null) {
            myView.cacheCanvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);
            myView.invalidate();
        }
    }
    //笔画视图
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
    @Override
    //安卓重写返回键事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(this, CuoziActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
