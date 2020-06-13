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
import android.widget.TextView;
import android.widget.Toast;
import com.example.handwriting.R;
import com.example.handwriting.bean.CWord;
import com.example.handwriting.bean.Han;
import com.example.handwriting.bean.User;
import com.example.handwriting.bean.UserLab;
import com.example.handwriting.bean.Word;
import com.example.handwriting.bean.WordPoint;
import com.example.handwriting.db.HanZi;
import com.example.handwriting.db.Plan;
import com.example.handwriting.other.CheckBH;
import com.example.handwriting.other.GetCutBitmapLocation;
import com.example.handwriting.view.MyView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ReviewActivity extends Activity {
    TextView py,bh,bs,zi,title,yixue,weixue,yi,wei;
    GetCutBitmapLocation getCutBitmapLocation;
    public int p_score=0,p_day=0,p_zi=0;
    public String p_name;
    Button re,wc;
    ImageView laba;
    String da_an="";
    int iL=0;
    String path;
    String timeStr;//当前日期
    private MediaPlayer myPlayer=new MediaPlayer();
    private long id_han=0;
    List<List<WordPoint>> itemPointList;
    List<WordPoint> pointList;
    public List<String> w_word=new ArrayList<>();
    public List<CWord> cWords=new ArrayList<>();
    ImageView titleBack;
    List<Plan> plans=new ArrayList<>();
    static List<List<List<WordPoint>>>  listAllPoint=new ArrayList<List<List<WordPoint>>>();
    MyView myView;
    int i=0;//学习标记
    int han_s;//计划已学汉字
    int max=15;//每次最大值
    int level;//hsk等级
    User user;
    List<Han> list_han=new ArrayList<Han>();
    List<Word>words=new ArrayList<>();
    List<Word>wordsN=new ArrayList<>();
    public void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.activity_study);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);}
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        timeStr=simpleDateFormat.format(date);
        user= UserLab.get(ReviewActivity.this,"","").getUser();
        plans= DataSupport.where("name = ? and cc = ?",user.getPhoneNum(),"1").find(Plan.class);
        level=plans.get(0).getLevel();
        init();
    }
    public void init(){
        AssetManager assetManager= this.getAssets();
        title=(TextView)findViewById(R.id.tv_title);
        py=(TextView)findViewById(R.id.s_py);
        bh=(TextView)findViewById(R.id.s_bh);
        bs=(TextView)findViewById(R.id.s_bs);
        zi=(TextView)findViewById(R.id.s_zi);
        re=(Button)findViewById(R.id.s_re);
        wc=(Button)findViewById(R.id.s_wc);
        yi=(TextView)findViewById(R.id.yi);
        wei=(TextView)findViewById(R.id.wei);
        yixue=(TextView)findViewById(R.id.yixue);
        weixue=(TextView)findViewById(R.id.weixue);
        titleBack=(ImageView)findViewById(R.id.title_back);
        laba=(ImageView)findViewById(R.id.bofang);
        myView=(MyView)findViewById(R.id.s_myview);
        yi.setText("已复习：");
        wei.setText("未复习：");
        title.setText("练字");
        weixue.setText(""+(max-han_s));
        yixue.setText(""+han_s);
        title.setTypeface(Typeface.createFromAsset(assetManager, "font/kaiti.ttf"));
        zi.setTypeface(Typeface.createFromAsset(assetManager, "font/kaiti.ttf"));
        List<HanZi> hanZiList=DataSupport.findAll(HanZi.class);
        if(hanZiList.size()>0){
            for(int i=0;i<hanZiList.size();i++) {
                Word word = new Word();
                word.setId(hanZiList.get(i).getId());
                word.setName(hanZiList.get(i).getName());
                word.setPy(hanZiList.get(i).getPy());
                word.setBhs(hanZiList.get(i).getBhs());
                word.setBushou(hanZiList.get(i).getBushou());
                Gson gson5 = new Gson();
                List<Integer> ls = gson5.fromJson(hanZiList.get(i).getBishun(), new TypeToken<List<Integer>>() {
                }.getType());
                word.setBishun(ls);
                words.add(word);
            }
        }
        if(max>plans.get(0).getLearn()) {
            max = plans.get(0).getLearn();
            weixue.setText(""+max);
        }
        Object[] values;
        Random random = new Random();
        HashMap<Object, Object> hashMap = new HashMap<Object, Object>();
        // 从HashMap导入数组
        values = hashMap.keySet().toArray();
        // 生成随机数字并存入HashMap
        for(int i = 0;;i++){
            int number = random.nextInt(plans.get(0).getLearn()) ;
            hashMap.put(number, i);
            values = hashMap.keySet().toArray();
            if(values.length==max)break;
        }
        for(int j=0;j<max;j++){
            Log.d("ss","d="+values[j]);
            wordsN.add(words.get(Integer.valueOf((Integer) values[j])));
        }
        StringBuffer sb=new StringBuffer("");
        for(int k=0;k<wordsN.get(i).getBishun().size();k++){
            if(wordsN.get(i).getBishun().get(k)>100) {
                sb.append(BiHua(wordsN.get(i).getBishun().get(k) % 100));
            }else
                sb.append(BiHua(wordsN.get(i).getBishun().get(k)));
        }
        zi.setText(""+wordsN.get(i).getName());
        py.setText(""+wordsN.get(i).getPy());
        bh.setText(""+wordsN.get(i).getBhs());
        bs.setText(""+sb);
        myView.Txt=wordsN.get(i).getName();
        re.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReWrite();
            }
        });
        wc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CWord cWord=new CWord();
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
                    if(wordsN.get(i).getName().equals(result)||true) {
                        if (itemPointList.size() == wordsN.get(i).getBhs()) {
                            int iq = 1;
                            for (int j = 0; j < wordsN.get(i).getBishun().size(); j++) {
                                if(wordsN.get(i).getBishun().get(j)>100){
                                    int bh1=wordsN.get(i).getBishun().get(j)/100;
                                    int bh2=wordsN.get(i).getBishun().get(j)%100;
                                    Log.d("sdda","bh1="+bh1+"bh2="+bh2);
                                    if (bh1 != checkBH.cb(itemPointList.get(j))&&bh2 != checkBH.cb(itemPointList.get(j))) {
                                        if (iq == 0) sb1.append("、");
                                        sb1.append("" + (j + 1));
                                        iq = 0;
                                    }
                                }else {
                                    if (wordsN.get(i).getBishun().get(j) != checkBH.cb(itemPointList.get(j))) {
                                        if (iq == 0) sb1.append("、");
                                        sb1.append("" + (j + 1));
                                        iq = 0;
                                    }
                                }
                            }
                            if (iq == 1) da_an = "书写正确！";
                            else {
                                w_word.add(wordsN.get(i).getName());
                                da_an = "笔顺错误!" + "第" + sb1 + "笔写错," + "可点击左上角的汉字，查看正确的书写！";
                                String msg="笔顺错误!" + "第" + sb1 + "笔写错!";
                                cWord.setName(wordsN.get(i).getName());
                                cWord.setWrong_msg(msg);
                                cWords.add(cWord);
                            }
                        } else {
                            da_an = "笔画数错误！" + "您输入的笔画数是" + itemPointList.size() + "," + "可点击左上角的汉字，查看正确的书写！";
                            w_word.add(wordsN.get(i).getName());
                            String msg="笔画数错误!" + "输入的笔画数是" + itemPointList.size()+"!";
                            cWord.setName(wordsN.get(i).getName());
                            cWord.setWrong_msg(msg);
                            cWords.add(cWord);
                        }
                    }else{
                        da_an = "书写错误！您书写的汉字与目标汉字不符合！";
                        String msg="汉字书写错误，无法识别！";
                        cWord.setName(wordsN.get(i).getName());
                        cWord.setWrong_msg(msg);
                        cWords.add(cWord);
                        w_word.add(wordsN.get(i).getName());
                    }
                    Han han=new Han();
                    id_han=new Date().getTime();
                    han.setId(id_han);
                    han.setName(myView.Txt);
                    Date curDate =  new Date(System.currentTimeMillis());
                    han.setTime(curDate);
                    han.setLists(itemPointList);
                    list_han.add(han);
                    wcDialog();
                }
                else {
                    Toast toast=Toast.makeText(ReviewActivity.this,null,Toast.LENGTH_SHORT);
                    toast.setText("请写字！");
                    toast.show();
                }
            }
        });
        zi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layDialog(i);
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
            String s=wordsN.get(i).getPy();
            String string=path + "/pinyin/"+s.replace(" ","")+".mp3";
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
        AlertDialog.Builder builder=new AlertDialog.Builder(ReviewActivity.this);
        View view= LayoutInflater.from(ReviewActivity.this).inflate(R.layout.dialog_wan,null,false);
        builder.setView(view);
        final Dialog dialog=builder.create();
        Button cx=view.findViewById(R.id.d_once);
        final Button xyg=view.findViewById(R.id.d_w);
        TextView da=view.findViewById(R.id.d_t);
        if(i==max-1)xyg.setText("完成");
        cx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReWrite();
                dialog.dismiss();
            }
        });
        xyg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCutBitmapLocation=new GetCutBitmapLocation();
                getCutBitmapLocation.setCutLeftAndRight(myView.getWidth(),myView.getHeight());
                if(i<max-1) {
                    ReWrite();
                    i++;
                    iL++;
                    StringBuffer sb = new StringBuffer("");
                    zi.setText("" + wordsN.get(i).getName());
                    py.setText("" + wordsN.get(i).getPy());
                    bh.setText("" + wordsN.get(i).getBhs());
                    for (int k = 0; k < wordsN.get(i).getBishun().size(); k++) {
                        if(wordsN.get(i).getBishun().get(k)>100) {
                            sb.append(BiHua(wordsN.get(i).getBishun().get(k)%100));
                        }else
                            sb.append(BiHua(wordsN.get(i).getBishun().get(k)));
                    }
                    yixue.setText(""+i);
                    bs.setText("" + sb);
                    weixue.setText(""+(max-i));
                    myView.Txt = wordsN.get(i).getName();
                }else{
                    Intent intent=new Intent(ReviewActivity.this,MainActivity.class);
                    startActivity(intent);
                }
                dialog.dismiss();
            }
        });
        da.setText(""+da_an);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void updatePhb(){
        String url = getString(R.string.s_url_updatePhb);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("NAME", p_name)
                .add("SCORE",""+p_score)
                .add("DAY",""+p_day)
                .add("ZI",""+p_zi)
                .build();
        Request request = new Request.Builder()
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
                JSONArray jsonArray = null;
                try {
                    String state = "";
                    jsonArray = new JSONArray(s);
                    for (int i = 0 ; i < jsonArray.length() ; i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        state = jsonObject.getString("STATE");
                    }
                    if (state.equals("success")){
                    }else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, "onResponse: " + s);
            }
        });
    }
    //笔画动态图对话框
    private void layDialog(int k) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReviewActivity.this);
        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(ReviewActivity.this).inflate(
                R.layout.dialog_zi, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        final Dialog dialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        GifImageView mWebFlash = view.findViewById(R.id.gv_error);
        try {
            GifDrawable gifFromPath = new GifDrawable("/storage/emulated/0/HandWriting/GIf/"+wordsN.get(i).getName()+".gif");
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
       // myView.listPoint=new ArrayList<List<WordPoint>>();
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
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        return true;
    }
}