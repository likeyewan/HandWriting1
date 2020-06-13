package com.example.handwriting.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.content.FileProvider;
import com.example.handwriting.other.GetData;
import com.example.handwriting.R;
import com.example.handwriting.bean.Han;
import com.example.handwriting.bean.User;
import com.example.handwriting.bean.UserLab;
import com.example.handwriting.bean.WordPoint;
import com.example.handwriting.db.Replay;
import com.example.handwriting.other.JxdUtils;
import com.example.handwriting.view.ReView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.litepal.crud.DataSupport;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ReplayActivity extends Activity implements SeekBar.OnSeekBarChangeListener {
    List<List<List<WordPoint>>>  listAllPoint=new ArrayList<List<List<WordPoint>>>();
    List<Han>list=new ArrayList<>();
    private SeekBar seekBar;
    private Button btn_p,btn_n;
    private boolean tuo=false;
    private ImageView start_end;
    private boolean ON = false;
    private boolean w=false;
    ImageView share;
    private boolean ws=false,ws1=false;
    TextView t_pro,t_end,title;
    private int jia=20;
    ReView reView;
    private boolean jd=false;
    ProgressBarAsyncTask progressBarAsyncTask;
    String path="";
    String data="";
    String msg="";
    List<Replay>replays=new ArrayList<>();
    public void onCreate(Bundle s){
        super.onCreate(s);
        setContentView(R.layout.re);
        Intent intent = getIntent();
        String action = intent.getAction();
        if (intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            path = JxdUtils.getPath(ReplayActivity.this, uri);
        }
        Intent intent1 = getIntent();
        data=intent1.getStringExtra("d");
        msg=intent1.getStringExtra("msg");
        //Log.d("da","msg="+msg);
        //标题框
        reView=(ReView)findViewById(R.id.fvFont);
        t_pro=(TextView)findViewById(R.id.t_pro);
        t_end=(TextView)findViewById(R.id.t_all);
        btn_p=(Button)findViewById(R.id.btn_p);
        btn_n=(Button)findViewById(R.id.btn_n);
        seekBar=(SeekBar)findViewById(R.id.seekBar);
        start_end = (ImageView) findViewById(R.id.s_e);
        share=(ImageView)findViewById(R.id.title_ok);
        title=(TextView)findViewById(R.id.tv_title);
        share.setImageResource(R.drawable.fenxiang);
        share.setVisibility(View.VISIBLE);
        title.setText("回放");
        User user= UserLab.get(ReplayActivity.this,"","").getUser();
        if(data!=null) {
            if (data.equals("")) {
            } else {
                replays = DataSupport.where("userPhone = ?and name=?", user.getPhoneNum(), data).find(Replay.class);
            }
        }
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(replays.size()>0) {
                    String filename=saveToLocal(replays.get(0));
                    File file = new File(filename);
                    shareFile(file, "*/*");
                }
            }
        });
        start_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (!ON) {
                    if(ws1){
                        ws1=false;
                    }else{
                        if(ws) {
                            flag=0;
                            itemFlag=0;
                            ReWrite();
                        }
                        if(jd||flag1==0) {
                            ReWrite();
                            jd=false;
                        }
                        if(tuo){
                            ReWrite();
                        }
                        ReWrite();
                    }
                    tuo=false;
                    start_end.setImageResource(R.drawable.tingzhi);
                    isPause=false;
                    progressBarAsyncTask = new ProgressBarAsyncTask(listAllPoint);
                    progressBarAsyncTask.execute();
                    ON = true;
                }else if(ON){
                    isPause=true;
                    ws1=true;
                    start_end.setImageResource(R.drawable.kaishi);
                    ON = false;
                }
            }
        });

        if(replays.size()>0){
            Gson gson1=new Gson();
            final List<Han> listHan = gson1.fromJson(replays.get(0).getReplay_msg(), new TypeToken<List<Han>>(){}.getType());
            list=gson1.fromJson(replays.get(0).getReplay_msg(), new TypeToken<List<Han>>(){}.getType());
            if(listHan!=null) {
                for (int i = 0; i < listHan.size(); i++) {
                    listAllPoint.add(listHan.get(i).getLists());
                    t_pro.setText("1");
                }
            }
        }else{
            Gson gson1=new Gson();
            List<Han> listHan = gson1.fromJson(msg, new TypeToken<List<Han>>(){}.getType());
            list=gson1.fromJson(msg, new TypeToken<List<Han>>(){}.getType());
            if(listHan!=null) {
                for (int i = 0; i < listHan.size(); i++) {
                    listAllPoint.add(listHan.get(i).getLists());
                    t_pro.setText("1");
                }
            }
        }

        initView(0);
        seekBar.setMax(listAllPoint.size()-1);
        seekBar.setProgress(0);
        seekBar.setOnSeekBarChangeListener(this);
        t_end.setText(""+listAllPoint.size());
        reView.canDraw=false;
        if(flag1==0){btn_p.setEnabled(false);}
        btn_p.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag1==1){btn_p.setEnabled(false);}
                btn_n.setEnabled(true);
                if (flag1>0) {
                    ws1=false;
                    flag1 -= 1;
                    tuo=true;
                    flag=0;
                    itemFlag=0;
                    ReWrite();
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    start_end.setImageResource(R.drawable.kaishi);
                    initView(flag1);
                    seekBar.setProgress(flag1);
                }
            }
        });
        if(flag1==listAllPoint.size()-1){btn_n.setEnabled(false);}
        else btn_n.setEnabled(true);
        btn_n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_p.setEnabled(true);
                if(flag1==listAllPoint.size()-2)btn_n.setEnabled(false);
                if (flag1 < listAllPoint.size()-1) {
                    flag1 += 1;
                    ws1=false;
                    tuo=true;
                    flag=0;
                    itemFlag=0;
                    ReWrite();
                    try {
                        Thread.sleep(25);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    start_end.setImageResource(R.drawable.kaishi);
                    initView(flag1);
                    seekBar.setProgress(flag1);
                }
            }
        });
    }
    public void initView(int item){
        ReWrite();
        reView.Txt=list.get(item).getName();
        if(listAllPoint.size()>0) {
            if(listAllPoint.size()==1)seekBar.setProgress(1);
            for (int i = 0; i < listAllPoint.get(item).size(); i++) {
                List<WordPoint> teampPoint = listAllPoint.get(item).get(i);
                reView.mPointsB = new ArrayList<WordPoint>();
                for (int j = 0; j < teampPoint.size(); j++) {
                    reView.mPointsB.add(teampPoint.get(j));
                    reView.postInvalidate();
                }
                reView.listPointB.add(reView.mPointsB);
                reView.mPointsB = new ArrayList<WordPoint>();
                reView.postInvalidate();
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void shareFile( File file,String fileType)
    {
        if (file.exists())
        {
            Uri uri= FileProvider.getUriForFile(this,"com.example.handwriting.fileProvider", file);
            shareFile(this, uri, fileType);
        }
    }
    final static int SELECT = 1;	// 标记选取
    final static int SHARE = 2;		// 标记分享
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode == Activity.RESULT_OK)
        {
            if (requestCode == SELECT)
            {
                Uri uri = data.getData();
                String fileType = getType(uri);
                shareFile(this, uri, fileType);
            }
            else if (requestCode == SHARE)
            {
                Toast.makeText(this, "分享完成！", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private String getType(Uri uri)
    {
        String uriPath = uri.getPath().toString();
        int start = uriPath.lastIndexOf("/");
        int end = uriPath.lastIndexOf(":");
        String type = "*/*";
        if (end > start)
        {
            type = uriPath.substring(start + 1, end) + "/*";
        }
        return type;
    }
    public static void shareFile(Activity context, Uri fileUri, String type)
    {
        if (fileUri != null)
        {
            Intent shareInt = new Intent(Intent.ACTION_SEND);
            shareInt.putExtra(Intent.EXTRA_STREAM, fileUri);
            shareInt.setType(type);			// 文件类型
            shareInt.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareInt.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            context.startActivity(Intent.createChooser(shareInt, "分享给"));
        }
        else
        {
            Toast.makeText(context, "分享文件不存在", Toast.LENGTH_SHORT).show();
        }
    }
    public int flag1=0;
    public int flag=0;
    public int itemFlag=0;
    public boolean isPause;
    public void ReWrite()
    {
        reView.listPointB.clear();
        reView.mPointsB.clear();
        reView.mPointsB=new ArrayList<>();
        reView.listPointB=new ArrayList<List<WordPoint>>();
        if (reView.cacheCanvas != null) {
            reView.cacheCanvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);
        }
    }
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        t_pro.setText(""+(progress+1));
        flag1=progress;
        tuo=true;
    }
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        tuo=true;
        flag=0;
        itemFlag=0;
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        ws1=false;
        if(flag1==0)btn_p.setEnabled(false);
        else btn_p.setEnabled(true);
        if(flag1==listAllPoint.size()-1)btn_n.setEnabled(false);
        else btn_n.setEnabled(true);
        tuo=true;
        start_end.setImageResource(R.drawable.kaishi);
        ReWrite();
        if(flag1<=listAllPoint.size()-1) {
            initView(flag1);
        }
    }
    public class ProgressBarAsyncTask extends AsyncTask<Void, Integer, Void> {
        List<List<List<WordPoint>>> points;
        public ProgressBarAsyncTask(List<List<List<WordPoint>>> points)
        {
            this.points=points;
        }
        @Override
        protected Void doInBackground(Void... params) {
                for (int i = itemFlag; i < points.get(flag1).size(); i++) {
                    List<WordPoint> teampPoint = points.get(flag1).get(i);
                    reView.mPointsB = new ArrayList<WordPoint>();
                    reView.mPointsB=new ArrayList<>();
                    for (int j = flag; j < teampPoint.size(); j++) {
                        if (isPause) {
                            reView.listPointB.add(reView.mPointsB);
                            itemFlag=i;
                            return null;
                        }
                            if (tuo) {
                                tuo = false;
                                itemFlag = 0;
                                flag = 0;
                                return null;
                            }
                        reView.mPointsB.add(teampPoint.get(j));
                        reView.postInvalidate();
                        try {
                            Thread.sleep(jia);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        flag = j;
                    }
                    flag = 0;
                    reView.listPointB.add(reView.mPointsB);
                    reView.mPointsB = new ArrayList<WordPoint>();
                    reView.postInvalidate();
                }
                itemFlag=0;
            w=true;
            flag=0;
            itemFlag=0;
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            reView.canReDraw=true;
            if(w){
                w=false;
                start_end.setImageResource(R.drawable.chongbo);
                ON = false;
            }else {
                start_end.setImageResource(R.drawable.kaishi);
                ON = false;
            }
        }
        @Override
        protected void onPreExecute() {
            reView.canReDraw=false;
            reView.mPointsB=new ArrayList<WordPoint>();
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            reView.invalidate();
        }
    }
    @Override
    protected void onDestroy() {
        tuo=true;
        super.onDestroy();
    }
    @Override
    protected void onPause() {
        super.onPause();
        isPause=true;
    }
    //写到本地文件夹
    public  String saveToLocal(Replay replay) {
        //文件名
        String fileName =replay.getName()+".txt";
        String path3="";
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
        try {
            //文件夹路径
            File dir = new File(path3);
            Gson gson = new Gson();
            String str = replay.getReplay_msg();
            File file = new File(dir, fileName);
            OutputStream out = new FileOutputStream(file);
            out.write(str.getBytes());
            out.close();
            return file.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path3+"/"+fileName+".txt";
    }
}
