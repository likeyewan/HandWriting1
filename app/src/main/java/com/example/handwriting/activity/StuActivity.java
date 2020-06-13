package com.example.handwriting.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handwriting.handwritig_recognition.Handwriting;
import com.example.handwriting.other.CheckBH;
import com.example.handwriting.other.GetData;
import com.example.handwriting.other.GetCutBitmapLocation;
import com.example.handwriting.other.LineEditText;
import com.example.handwriting.view.MyView;
import com.example.handwriting.R;
import com.example.handwriting.bean.CWord;
import com.example.handwriting.bean.Han;
import com.example.handwriting.bean.Phb;
import com.example.handwriting.bean.Tie;
import com.example.handwriting.bean.User;
import com.example.handwriting.bean.UserLab;
import com.example.handwriting.bean.Word;
import com.example.handwriting.bean.WordPoint;
import com.example.handwriting.db.HanZi;
import com.example.handwriting.db.Learn;
import com.example.handwriting.db.Plan;
import com.example.handwriting.db.Replay;
import com.example.handwriting.db.WWord;
import com.example.handwriting.other.UploadFile;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import static androidx.constraintlayout.widget.Constraints.TAG;

public class StuActivity extends Activity {
    String hsk[]=new String[4];
    String chsk="一";
    TextView py,bh,bs,zi,title,yixue,weixue;
    private LineEditText et_handwrite,et_h;
    GetCutBitmapLocation getCutBitmapLocation;
    private Bitmap myBitmap;
    Bitmap bitmap1;
    Boolean dd=true;
    public int p_score=0,p_day=0,p_zi=0;
    public String p_name;
    Button re,wc;
    ImageView laba;
    int aF=0,aC=0,faC=0;
    String da_an="";
    int iL=0;
    String timeStr;//当前日期
    private MediaPlayer myPlayer=new MediaPlayer();
    private long id_han=0;
    GetData getData=new GetData();
    List<List<WordPoint>> itemPointList;
    List<WordPoint> pointList;
    public List<String> w_word=new ArrayList<>();
    public List<CWord> cWords=new ArrayList<>();
    ImageView titleBack;
    List<Plan> plans=new ArrayList<>();
    List<Learn> learns=new ArrayList<>();
    static List<List<List<WordPoint>>>  listAllPoint=new ArrayList<List<List<WordPoint>>>();
    MyView myView;
    private String path;
    private String path2,path3;
    int i=0;//学习标记
    int han_s;//计划已学汉字
    int s_all=0;//总计划已学汉字
    int s_day=0;//每日已学汉字
    int max=0;//每次最大值
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
        hsk[0]=getString(R.string.hsk1);
        hsk[1]=getString(R.string.hsk2);
        hsk[2]=getString(R.string.hsk3);
        hsk[3]=getString(R.string.hsk4);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        timeStr=simpleDateFormat.format(date);
        user= UserLab.get(StuActivity.this,"","").getUser();
        plans= DataSupport.where("name = ? and cc = ?",user.getPhoneNum(),"1").find(Plan.class);
        level=plans.get(0).getLevel();
        if(level<4)
        chsk=hsk[level];
        p_name=user.getNickname();
        learns= DataSupport.where("day=?and name=?and level=?",timeStr,user.getPhoneNum(),""+level).find(Learn.class);
        han_s=learns.get(0).getStu();
        s_day=learns.get(0).getXue();
        i=han_s;
        max= plans.get(0).getDayPlan();
        s_all=plans.get(0).getLearn();

        initData();
        getList();
        init();
    }
    public void init(){
        AssetManager assetManager= StuActivity.this.getAssets();
        title=(TextView)findViewById(R.id.tv_title);
        py=(TextView)findViewById(R.id.s_py);
        bh=(TextView)findViewById(R.id.s_bh);
        bs=(TextView)findViewById(R.id.s_bs);
        zi=(TextView)findViewById(R.id.s_zi);
        re=(Button)findViewById(R.id.s_re);
        wc=(Button)findViewById(R.id.s_wc);
        et_handwrite = (LineEditText)findViewById(R.id.et_handwrite);
        et_h=(LineEditText)findViewById(R.id.et_hand);
        yixue=(TextView)findViewById(R.id.yixue);
        weixue=(TextView)findViewById(R.id.weixue);
        titleBack=(ImageView)findViewById(R.id.title_back);
        laba=(ImageView)findViewById(R.id.bofang);
        myView=(MyView)findViewById(R.id.s_myview);
        bitmap1=getData.getBitmap(path2+"/item.png");
        // InsertToEditText1(bitmap);
        //InsertToEditText1(bitmap1);
        title.setText("练字");
        weixue.setText(""+(max-han_s));
        yixue.setText(""+han_s);
        title.setTypeface(Typeface.createFromAsset(assetManager, "font/kaiti.ttf"));
        zi.setTypeface(Typeface.createFromAsset(assetManager, "font/kaiti.ttf"));
        List<HanZi> hanZiList=DataSupport.findAll(HanZi.class);
        if(hanZiList.size()>0){
            Log.d("sds","aC="+aC);
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
                char[] hz=chsk.toCharArray();
                for(int j=0;j<hz.length;j++) {
                    if (word.getName().equals(hz[j] + "")) {
                        words.add(word);
                        break;
                    }
                 }
            }
        }
        if(words.size()<s_all+max-han_s) {
            max = words.size() - s_all - han_s;
            weixue.setText(""+(max-han_s));
        }

        for(int j=0;j<max;j++){
            wordsN.add(words.get(s_all+j-han_s));

        }
        for(int i=0;i<wordsN.size();i++){
            aF+=wordsN.get(i).getBhs();
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
        titleBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StuActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
        wc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                faC=0;
                if(dd==true) {
                    dd=false;
                }
                CWord cWord=new CWord();
                getCutBitmapLocation=new GetCutBitmapLocation();
                Bitmap bitmap=viewToBitmap(myView,myView.getWidth(),myView.getHeight());
                getCutBitmapLocation.setCutLeftAndRight(myView.getWidth(),myView.getHeight());
                myBitmap=getCutBitmap(bitmap);
                InsertToEditText1(myBitmap);
                et_h.setDrawingCacheEnabled(true);
                Bitmap cutHandwriteBitmap = Bitmap.createBitmap(et_h.getDrawingCache());
                et_h.setDrawingCacheEnabled(false);
                saveToSystemGallery(StuActivity.this, cutHandwriteBitmap, "test");
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
                    Handwriting handwriting=new Handwriting();
                    String result=handwriting.handwriting();
                    //Log.d("sdfa","汉字="+result);
                    String[] s=result.split("");
                    result=s[s.length-1];
                    //String result="";
                    if(wordsN.get(i).getName().equals(result)) {
                        if (itemPointList.size() == wordsN.get(i).getBhs()) {
                            int iq = 1;
                            for (int j = 0; j < wordsN.get(i).getBishun().size(); j++) {
                                //Log.d("sss", "jj=" + wordsN.get(i).getBishun());
                                //sb1.append(""+checkBH.cb(itemPointList.get(j)));
                                if(wordsN.get(i).getBishun().get(j)>100){
                                    int bh1=wordsN.get(i).getBishun().get(j)/100;
                                    int bh2=wordsN.get(i).getBishun().get(j)%100;
                                    Log.d("sdda","bh1="+bh1+"bh2="+bh2);
                                    if (bh1 != checkBH.cb(itemPointList.get(j))&&bh2 != checkBH.cb(itemPointList.get(j))) {
                                        if (iq == 0) sb1.append("、");
                                        aC++;
                                        Log.d("sss", "jj=" + aC);
                                        faC++;
                                        sb1.append("" + (j + 1));
                                        iq = 0;
                                    }
                                }else {
                                    if (wordsN.get(i).getBishun().get(j) != checkBH.cb(itemPointList.get(j))) {
                                        if (iq == 0) sb1.append("、");
                                        aC++;
                                        faC++;
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
                            aC += wordsN.get(i).getBhs();
                            faC+= wordsN.get(i).getBhs();
                            da_an = "笔画数错误！" + "您输入的笔画数是" + itemPointList.size() + "," + "可点击左上角的汉字，查看正确的书写！";
                            w_word.add(wordsN.get(i).getName());
                            String msg="笔画数错误!" + "输入的笔画数是" + itemPointList.size()+"!";
                            cWord.setName(wordsN.get(i).getName());
                            cWord.setWrong_msg(msg);
                            cWords.add(cWord);
                            int fl = 0;


                        }
                    }else{
                        aC += wordsN.get(i).getBhs();
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
                    saveToLocal();
                    addReplaydb();
                }
                else {
                    Toast toast=Toast.makeText(StuActivity.this,null,Toast.LENGTH_SHORT);
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
    public void addWwdb(List<CWord> listc){
        List<WWord>list1=new ArrayList<>();
        list1=DataSupport.findAll(WWord.class);
        List<WWord>list2=DataSupport.where("userPhone = ?",user.getPhoneNum()).find(WWord.class);
        if(list2.size()>0){
            Gson gson1=new Gson();
            List<CWord> list3 = gson1.fromJson(list2.get(0).getWrong_word(), new TypeToken<List<CWord>>(){}.getType());
            for(int i=0;i<listc.size();i++){
                for(int j=0;j<list3.size();j++){
                    if(listc.get(i).getName().equals(list3.get(j).getName())){
                        listc.remove(i);
                        break;
                    }
                }
            }
            list3.addAll(listc);
            Gson gson = new Gson();
            String str = gson.toJson(list3);
            WWord wWord=new WWord();
            wWord.setWrong_word(str);
            wWord.updateAll("userPhone = ?",user.getPhoneNum());
        }else {
            Gson gson = new Gson();
            String str = gson.toJson(listc);
            WWord wWord = new WWord();
            wWord.setId(list1.size() + 1);
            wWord.setUserPhone(user.getPhoneNum());
            wWord.setWrong_word(str);
            wWord.save();
        }
    }
    public void addWWrod(List<String> list){
        Gson gson = new Gson();
        String str = gson.toJson(list);
        String url = getString(R.string.s_url_addWWord);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("wword",str)
                .add("userPhone",""+user.getPhoneNum())
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
    public void saveToSystemGallery(Context context, Bitmap bmp, String filename) {
        // 首先保存图片
        File appDir = new File(path2);
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = filename+ ".png";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //切割画布中的字并返回
    public Bitmap getCutBitmap(Bitmap mBitmap){
        //得到手写字的四周位置，并向外延伸10px
        float cutLeft = getCutBitmapLocation.getCutLeft() - 10;
        float cutTop = getCutBitmapLocation.getCutTop() - 10;
        float cutRight = getCutBitmapLocation.getCutRight() + 10;
        float cutBottom = getCutBitmapLocation.getCutBottom() + 10;
        cutLeft = (0 > cutLeft ? 0 : cutLeft);
        cutTop = (0 > cutTop ? 0 : cutTop);
        cutRight = (mBitmap.getWidth() < cutRight ? mBitmap.getWidth() : cutRight);
        cutBottom = (mBitmap.getHeight() < cutBottom ? mBitmap.getHeight() : cutBottom);
        //取得手写的的高度和宽度
        float cutWidth = cutRight - cutLeft;
        float cutHeight = cutBottom - cutTop;
        Bitmap cutBitmap = Bitmap.createBitmap(mBitmap, (int)cutLeft, (int)cutTop, (int)cutWidth, (int)cutHeight);
        if (myBitmap!=null ) {
            myBitmap.recycle();
            myBitmap= null;
        }
        return cutBitmap;
    }
    //将手写字插入到EditText中
    private void InsertToEditText1(Bitmap mBitmap){
        int S = 1000;
        int imgWidth = mBitmap.getWidth();
        int imgHeight = mBitmap.getHeight();
        double partion = imgWidth*1.0/imgHeight;
        double sqrtLength = Math.sqrt(partion*partion + 1);
        float scaleW = (float) (400f/imgWidth);
        float scaleH = (float) (400f/imgHeight);
        Matrix mx = new Matrix();
        //对原图片进行缩放
        mx.postScale(scaleW, scaleH);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, imgWidth, imgHeight, mx, true);
        //将手写的字插入到edittext中
        SpannableString ss = new SpannableString("1");
        ImageSpan span = new ImageSpan(mBitmap, ImageSpan.ALIGN_BOTTOM);
        ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        et_h.append(ss);
    }
    //将手写字插入到EditText中
    private void InsertToEditText(Bitmap mBitmap){
        int S = 1000;
        int imgWidth = mBitmap.getWidth();
        int imgHeight = mBitmap.getHeight();
        double partion = imgWidth*1.0/imgHeight;
        double sqrtLength = Math.sqrt(partion*partion + 1);
        float scaleW = (float) (400f/imgWidth);
        float scaleH = (float) (400f/imgHeight);
        Matrix mx = new Matrix();
        //对原图片进行缩放
        mx.postScale(scaleW, scaleH);
        mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, imgWidth, imgHeight, mx, true);
        //将手写的字插入到edittext中
        SpannableString ss = new SpannableString("1");
        ImageSpan span = new ImageSpan(mBitmap, ImageSpan.ALIGN_BOTTOM);
        ss.setSpan(span, 0, 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        et_handwrite.append(ss);
    }
    //生成位图
    public Bitmap viewToBitmap(View view, int bitmapWidth, int bitmapHeight){
        Bitmap cachebmp = loadBitmapFromView(view);
        return cachebmp;
    }
    private Bitmap loadBitmapFromView(View v) {
        int w = v.getWidth();
        int h = v.getHeight();
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
        c.drawColor(Color.WHITE);
        v.draw(c);
        return bmp;
    }
    private void initData() {
        //如果手机有sd卡
        try {
            path = Environment.getExternalStorageDirectory()
                    .getCanonicalPath().toString()
                    + "/HandWriting";
            File files = new File(path);
            if (!files.exists()) {
                files.mkdir();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            path2 = Environment.getExternalStorageDirectory()
                    .getCanonicalPath().toString()
                    + "/HandWriting/Picture";
            File file1 = new File(path2);
            if (!file1.exists()) {
                file1.mkdir();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            path3 = Environment.getExternalStorageDirectory()
                    .getCanonicalPath().toString()
                    + "/HandWriting/PointData";
            File file2 = new File(path3);
            if (!file2.exists()) {
                file2.mkdir();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
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
        AlertDialog.Builder builder=new AlertDialog.Builder(StuActivity.this);
        View view=LayoutInflater.from(StuActivity.this).inflate(R.layout.dialog_wan,null,false);
        builder.setView(view);
        final Dialog dialog=builder.create();
        Button cx=view.findViewById(R.id.d_once);
        final Button xyg=view.findViewById(R.id.d_w);
        TextView da=view.findViewById(R.id.d_t);
        if(i==max-1)xyg.setText("完成");
        cx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(da_an.equals("书写正确！")){
                }else {
                    w_word.remove(w_word.size() - 1);
                    cWords.remove(cWords.size() - 1);
                }
                aC=aC-faC;
                ReWrite();
                dialog.dismiss();
            }
        });
        xyg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addWWrod(w_word);
                addWwdb(cWords);
                getCutBitmapLocation=new GetCutBitmapLocation();
                Bitmap bitmap=viewToBitmap(myView,myView.getWidth(),myView.getHeight());
               // Log.d("fag","bit="+bitmap);
                getCutBitmapLocation.setCutLeftAndRight(myView.getWidth(),myView.getHeight());
                //Bitmap bitmap1=viewToBitmap(et_handwrite,500,500);
              //  Log.d("fag","bit="+bitmap1);
                myBitmap=getCutBitmap(bitmap);
                //saveToSystemGallery(StuActivity.this,myBitmap,"dd");
                InsertToEditText(myBitmap);
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
                           // Log.d("da","faa="+wordsN.get(i).getBishun().get(k)%100);
                            sb.append(BiHua(wordsN.get(i).getBishun().get(k)%100));
                        }else
                        sb.append(BiHua(wordsN.get(i).getBishun().get(k)));
                    }
                    bs.setText("" + sb);
                    myView.Txt = wordsN.get(i).getName();
                    Plan plan=new Plan();
                    plan.setLearn(s_all+iL);
                    plan.updateAll("name = ? and cc=?",user.getPhoneNum(),"1");
                    han_s++;
                    Learn learn=new Learn();
                    learn.setStu(han_s);
                    learn.setXue(s_day+han_s);
                    yixue.setText(""+(han_s));
                    weixue.setText(""+(max-han_s));
                    learn.updateAll("day=?and name=?and level=?",timeStr,user.getPhoneNum(),""+level);
                }else{
                    i++;
                    iL++;
                    Plan plan=new Plan();
                    plan.setLearn(s_all+iL);
                    plan.updateAll("name = ? and cc=?",user.getPhoneNum(),"1");
                    han_s++;
                    Learn learn=new Learn();
                    learn.setToDefault("stu");
                    learn.setS_p(learns.get(0).getS_p()+1);
                    learn.setXue(s_day+han_s);
                    learn.updateAll("day=?and name=?and level=?",timeStr,user.getPhoneNum(),""+level);
                    addReplay();
                    cjDialog();
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
    private void cjDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(StuActivity.this);
        View view=LayoutInflater.from(StuActivity.this).inflate(R.layout.dialog_cj,null,false);
        builder.setView(view);
        final Dialog dialog=builder.create();
        final Button xyg=view.findViewById(R.id.d_w);
        final Button share=view.findViewById(R.id.d_share);
        TextView da=view.findViewById(R.id.d_t);
        int fs=100-aC*100/aF;
        da.setText(""+fs+"分");
        p_zi=max;
        p_score=fs;
        updatePhb();
        xyg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(StuActivity.this,MainActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareDialog();
                dialog.dismiss();
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    private void shareDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(StuActivity.this);
        View view=LayoutInflater.from(StuActivity.this).inflate(R.layout.dialog_share,null,false);
        builder.setView(view);
        final Dialog dialog=builder.create();
        final Button qx=view.findViewById(R.id.share_qx);
        final Button qd=view.findViewById(R.id.share_qd);
        final EditText name=view.findViewById(R.id.edit_share);
        qx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent=new Intent(StuActivity.this,MainActivity.class);
                startActivity(intent);
            }
        });
        qd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                String str = formatter.format(curDate);
                UUID uuid = UUID.randomUUID();
                //将view转换成图片
                et_handwrite.setDrawingCacheEnabled(true);
                Bitmap cutHandwriteBitmap = Bitmap.createBitmap(et_handwrite.getDrawingCache());
                et_handwrite.setDrawingCacheEnabled(false);
                saveToSystemGallery(StuActivity.this, cutHandwriteBitmap, ""+uuid);
                UploadFile uploadFile=new UploadFile();
                uploadFile.fileUpload(path2+"/"+uuid+".png",""+uuid);
                Gson gson=new Gson();
                String str1 = gson.toJson(list_han);
                Tie tie=new Tie();
                tie.setId(0);
                tie.setUserName(user.getNickname());
                tie.setTieDate(str);
                tie.setName(name.getText().toString());
                tie.setPp(""+p_score);
                tie.setPic(uuid+"");
                tie.setZanNum(0);
                tie.setRemark(str1+"");
                addTie(tie);
                GetAsyncTask getAsyncTask=new GetAsyncTask();
                getAsyncTask.execute();
               // Intent intent=new Intent(StuActivity.this,MainActivity.class);
                //startActivity(intent);
            }
        });
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }
    public class GetAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute(){
            AlertDialog.Builder builder = new AlertDialog.Builder(StuActivity.this);
            // 创建一个view，并且将布局加入view中
            View view = LayoutInflater.from(StuActivity.this).inflate(
                    R.layout.dialog_fxz, null, false);
            // 将view添加到builder中
            builder.setView(view);
            // 创建dialog
            final Dialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
        }
        @Override
        protected Boolean doInBackground(Void... params) {

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return  true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                Intent intent=new Intent(StuActivity.this,MainActivity.class);
                startActivity(intent);
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }
    public void addReplaydb(){
        Gson gson = new Gson();
        String str = gson.toJson(list_han);
        String name="HSK"+(plans.get(0).getLevel()+1)+"级"+timeStr+"-"+learns.get(0).getS_p();
        List<Replay> list=DataSupport.findAll(Replay.class);
        List<Replay> list2=DataSupport.where("userPhone = ?and name=?",user.getPhoneNum(),name).find(Replay.class);
        if(list2.size()>0){
            Gson gson1=new Gson();
            List<Han> list3 = gson1.fromJson(list2.get(0).getReplay_msg(), new TypeToken<List<Han>>(){}.getType());
            list_han.removeAll(list3);
            list3.addAll(list_han);
            Replay replay=new Replay();
            replay.setReplay_msg(str);
            replay.setScore(aC);
            replay.updateAll("userPhone = ?and name=?",user.getPhoneNum(),name);
        }else {
            Replay replay = new Replay();
            replay.setId(list.size() + 1);
            replay.setName(name);
            replay.setUserPhone(user.getPhoneNum());
            replay.setReplay_msg(str);
            replay.save();
        }
    }
    private void addReplay(){
        Gson gson = new Gson();
        String str = gson.toJson(list_han);
        String name="HSK"+(plans.get(0).getLevel()+1)+"级"+timeStr+"-"+learns.get(0).getS_p();
        Log.d("sss","ac="+aC+"af="+aF);
        int score=100-aC*100/aF;
        String url = getString(R.string.s_url_addReplay);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("name",name)
                .add("phone",user.getPhoneNum())
                .add("msg",str)
                .add("score",score+"")
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
    private void addTie(Tie tie){
        Gson gson = new Gson();
        String str = gson.toJson(tie);
        String url = getString(R.string.s_url_addTie);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("tie",str)
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
    private void fxz(){
        AlertDialog.Builder builder = new AlertDialog.Builder(StuActivity.this);
        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(StuActivity.this).inflate(
                R.layout.dialog_fxz, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        final Dialog dialog = builder.create();
        dialog.show();
    }
    //笔画动态图对话框
    private void layDialog(int k) {
        AlertDialog.Builder builder = new AlertDialog.Builder(StuActivity.this);
        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(StuActivity.this).inflate(
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
    //写到本地文件夹
    public  String saveToLocal() {
        //文件名
        String fileName ="HSK"+(plans.get(0).getLevel()+1)+"级"+timeStr+"-"+learns.get(0).getS_p()+".txt";
        try {
            //文件夹路径
            File dir = new File(path3);
            Gson gson = new Gson();
            String str = gson.toJson(list_han);
            File file = new File(dir, fileName);
            OutputStream out = new FileOutputStream(file);
            out.write(str.getBytes());
            out.close();
            return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    public void getList(){
        if(learns.get(0).getS_p()==1){
            p_day=1;
        }
        String name="HSK"+(plans.get(0).getLevel()+1)+"级"+timeStr+"-"+learns.get(0).getS_p();
        List<Replay> list2=DataSupport.where("userPhone = ?and name=?",user.getPhoneNum(),name).find(Replay.class);
        if(list2.size()>0){
            Gson gson2=new Gson();
            List<Han>woc = gson2.fromJson( list2.get(0).getReplay_msg(), new TypeToken<List<Han>>(){}.getType());
            aC=list2.get(0).getScore();
            if(woc!=null) {
                list_han=woc;
            }
        }
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
