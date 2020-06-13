package com.example.handwriting.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.handwriting.other.CheckBH;
import com.example.handwriting.R;
import com.example.handwriting.bean.WordPoint;
import com.example.handwriting.view.MyView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;

public class SingleWordActivity extends Activity {
    TextView singleWord;
    EditText souWord;
    ImageView sou;
    MyView myView;
    Button re,wc;
    String da_an="暂时没有相关资源！";
    private String word;
    List<List<WordPoint>> itemPointList;
    List<WordPoint> pointList;
   public void onCreate(Bundle s) {
       super.onCreate(s);
       setContentView(R.layout.activity_single);
       initView();
       myView.canDraw=false;
       click();
   }
   public void initView(){
       singleWord=(TextView)findViewById(R.id.sou_zi);
       souWord=(EditText)findViewById(R.id.single);
       sou=(ImageView)findViewById(R.id.sou);
       myView=(MyView)findViewById(R.id.single_view);
       re=(Button)findViewById(R.id.sg_re);
       wc=(Button)findViewById(R.id.sg_wc);
       TextView title=(TextView)findViewById(R.id.tv_title);
       title.setText("单字练习");
   }
   public void click(){
       sou.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               myView.canDraw=true;
               word=souWord.getText().toString();
               myView.Txt=word;
               ReWrite();
               singleWord.setText(""+word);
           }
       });
       singleWord.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               layDialog();
           }
       });
       re.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               ReWrite();
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
                   CheckBH checkBH=new CheckBH();
                   wcDialog();
               }
               else {
                   Toast toast=Toast.makeText(SingleWordActivity.this,null,Toast.LENGTH_SHORT);
                   toast.setText("请写字！");
                   toast.show();
               }
           }
       });
   }
    private void wcDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(SingleWordActivity.this);
        View view=LayoutInflater.from(SingleWordActivity.this).inflate(R.layout.dialog_wan,null,false);
        builder.setView(view);
        final Dialog dialog=builder.create();
        Button cx=view.findViewById(R.id.d_once);
        final Button xyg=view.findViewById(R.id.d_w);
        xyg.setText("确定");
        TextView da=view.findViewById(R.id.d_t);
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

                dialog.dismiss();
            }
        });
        da.setText(""+da_an);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }
    /**
     * 自定义布局的对话框
     */
    private void layDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SingleWordActivity.this);
        // 创建一个view，并且将布局加入view中
        View view = LayoutInflater.from(SingleWordActivity.this).inflate(
                R.layout.dialog_zi, null, false);
        // 将view添加到builder中
        builder.setView(view);
        // 创建dialog
        final Dialog dialog = builder.create();
        // 初始化控件，注意这里是通过view.findViewById
        GifImageView mWebFlash = view.findViewById(R.id.gv_error);
        try {
            //file方式
            //GifDrawable gifFromPath = new GifDrawable(file);
            //直接给地址
            GifDrawable gifFromPath = new GifDrawable("/storage/emulated/0/HandWriting/Picture/4E00.gif");
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
        //myView.listPoint=new ArrayList<List<WordPoint>>();
        myView.listPointB=new ArrayList<List<WordPoint>>();
        myView.invalidate();
        if (myView.cacheCanvas != null) {
            myView.cacheCanvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);
            myView.invalidate();
        }
    }
}
