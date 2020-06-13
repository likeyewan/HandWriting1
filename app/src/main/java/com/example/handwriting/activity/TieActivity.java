package com.example.handwriting.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.handwriting.other.GetData;
import com.example.handwriting.other.MyImgBtnH;
import com.example.handwriting.R;
import com.example.handwriting.adapter.CommentAdapter;
import com.example.handwriting.bean.Comment;
import com.example.handwriting.bean.Tie;
import com.example.handwriting.bean.User;
import com.example.handwriting.bean.UserLab;
import com.example.handwriting.bean.ZName;
import com.example.handwriting.other.NoTouchLinearLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class TieActivity extends Activity {
    private ListView mListData;
    private LinearLayout mLytCommentVG;
    private NoTouchLinearLayout mLytEdittextVG;
    private EditText mCommentEdittext;
    private Button mSendBut;
    private List<Comment> list=new ArrayList<>();
    private List<Comment> commentList =new ArrayList<>();
    private List<String> zanName=new ArrayList<>();
    private CommentAdapter adapter;
    private MyImgBtnH pl;
    private MyImgBtnH zan;
    private ImageView tu;
    private TextView zanNum,plName,plContent,plNum,plTime;
    private Boolean dz=false;
    private String comment = "";        //记录对话框中的内容
    private int position;                //记录回复评论的索引

    private User user;
    Tie tie=new Tie();
    private int flag;
    public void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_tie);
        user = UserLab.get(TieActivity.this, "", "").getUser();
        Intent intent = getIntent();
        String data = intent.getStringExtra("tie");
        String flagstring=intent.getStringExtra( "flag");
        flag=Integer.valueOf(flagstring);
        Gson gson2 = new Gson();
        tie = gson2.fromJson(data, new TypeToken<Tie>() {}.getType());
        initViews();
        GetCommentAsyncTask getCommentAsyncTask = new GetCommentAsyncTask();
        getCommentAsyncTask.execute();
        adapter = new CommentAdapter(this, getCommentData(), R.layout.comment_item_list, handler);
        mListData.setAdapter(adapter);
    }
    /**
     * 初始化控件
     */
    private void initViews() {
        tu=(ImageView)findViewById(R.id.tie_pic);
        mListData = (ListView) findViewById(R.id.list_data);
        mLytCommentVG = (LinearLayout) findViewById(R.id.comment_vg_lyt);
        mLytEdittextVG = (NoTouchLinearLayout) findViewById(R.id.edit_vg_lyt);
        mCommentEdittext = (EditText) findViewById(R.id.edit_comment);
        mSendBut = (Button) findViewById(R.id.but_comment_send);
        zanNum=(TextView)findViewById(R.id.zan_num);
        plContent=(TextView)findViewById(R.id.pl_content);
        plName=(TextView)findViewById(R.id.pl_name);
        plNum=(TextView)findViewById(R.id.pl_num);
        plTime=(TextView)findViewById(R.id.pl_time);
        pl=(MyImgBtnH)findViewById(R.id.ping_lun);
        zan=(MyImgBtnH)findViewById(R.id.zan);
        pl.setImageResource(R.mipmap.pinglun);
        pl.setText("评论");
        zan.setImageResource(R.mipmap.zan);
        zan.setText("赞");
        Gson gson2 = new Gson();
        if(tie.getZanName()!=null) {
            zanName = gson2.fromJson(tie.getZanName(), new TypeToken<List<String>>() {
            }.getType());
            for (String s : zanName) {
                if (s.equals(user.getNickname())) {
                    dz = true;
                    zan.setImageResource(R.mipmap.zan_1);
                }
            }
            zanNum.setText("" + zanName.size());
        }else zanNum.setText("" +0);
        plName.setText(""+tie.getUserName());
        plNum.setText(""+list.size());
        plTime.setText(""+tie.getTieDate());
        plContent.setText(tie.getName());
        GetData getData=new GetData();
        String path="";
        try {
             path=Environment.getExternalStorageDirectory().getCanonicalPath().toString() +
                    "/HandWriting/Picture/"+tie.getPic()+".png";
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bitmap bitmap=getData.getBitmap(path);
        getTu(tie.getPic());
        Log.d("dasa","fsga"+tie.getRemark());
       // tu.setImageBitmap(bitmap);
        ClickListener cl = new ClickListener();
        mSendBut.setOnClickListener(cl);
        mLytCommentVG.setOnClickListener(cl);
        pl.setOnClickListener(cl);
        zan.setOnClickListener(cl);
        tu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(TieActivity.this,ReplayActivity.class);
                intent.putExtra("msg",tie.getRemark());
                intent.putExtra("d","");
                startActivity(intent);
            }
        });
    }
    private void getTu(String n) {
        String url="http://192.168.137.1:8080/HSKHandWrting/pic/"+n+".jpg";
        OkHttpClient client = new OkHttpClient();
        final Request request = new Request
                .Builder()
                .get()
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();
                //将图片显示到ImageView中
                final Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tu.setImageBitmap(bitmap);
                    }
                });
                inputStream.close();
            }
        });
    }
    public class GetCommentAsyncTask extends AsyncTask<Void, Integer, Boolean> {
        @Override
        protected void onPreExecute(){
        }
        @Override
        protected Boolean doInBackground(Void... params) {
            String url = getString(R.string.s_url_getAllComment);
            OkHttpClient okHttpClient = new OkHttpClient();
            RequestBody requestBody = new FormBody.Builder()
                    .add("tieID",""+tie.getId())
                    .build();
            final Request request = new Request.Builder()
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
                    Gson gson1=new Gson();
                    commentList = gson1.fromJson(s, new TypeToken<List<Comment>>(){}.getType());
                }
            });
            try {
                while(true) {
                    if (commentList.size() > 0) {
                        break;
                    }else {
                        Thread.sleep(200);
                        break;
                    }
                }
            }catch (Exception e){
                return false;
            }
            return true;
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if(result) {
                adapter = new CommentAdapter(TieActivity.this, getCommentData(), R.layout.comment_item_list, handler);
                mListData.setAdapter(adapter);
                plNum.setText(""+list.size());
            }
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
        }
    }
    //获取评论数据
    private List<Comment> getCommentData() {
        list = new ArrayList<>();
        for(Comment comment : commentList)
            list.add(comment);
        return list;
    }
    /**
     * 显示或隐藏输入法
     */
    private void onFocusChange(boolean hasFocus) {
        final boolean isFocus = hasFocus;
        (new Handler()).postDelayed(new Runnable() {
            public void run() {
                InputMethodManager imm = (InputMethodManager)
                        mCommentEdittext.getContext().getSystemService(INPUT_METHOD_SERVICE);
                if (isFocus) {
                    //显示输入法
                    mCommentEdittext.requestFocus();//获取焦点
                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                } else {
                    //隐藏输入法
                    imm.hideSoftInputFromWindow(mCommentEdittext.getWindowToken(), 0);
                    mLytCommentVG.setVisibility(View.VISIBLE);
                    mLytEdittextVG.setVisibility(View.GONE);
                }
            }
        }, 100);
    }

    /**
     * 点击屏幕其他地方收起输入法
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                onFocusChange(false);

            }
            return super.dispatchTouchEvent(ev);
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        if (getWindow().superDispatchTouchEvent(ev)) {
            return true;
        }
        return onTouchEvent(ev);
    }

    /**
     * 隐藏或者显示输入框
     */
    public boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            //获取输入框当前的location位置
            /**
             *这堆数值是算我的下边输入区域的布局的，
             * 规避点击输入区域也会隐藏输入区域
             */

            v.getLocationInWindow(leftTop);
            int left = leftTop[0] - 50;
            int top = leftTop[1] - 50;
            int bottom = top + v.getHeight() + 300;
            int right = left + v.getWidth() + 120;
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击的是输入框区域，保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对话框中是否输入内容
     */
    private boolean isEditEmply() {
        comment = mCommentEdittext.getText().toString().trim();
        if (comment.equals("")) {
            Toast.makeText(getApplicationContext(), "评论不能为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        mCommentEdittext.setText("");
        return true;
    }

    /**
     * 发表评论
     */
    private void publishComment() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss ");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        Comment bean = new Comment();
        bean.setTieID(tie.getId());
        bean.setCommentNickname(user.getNickname());
        bean.setCommentTime(str);
        bean.setCommentContent(comment);
        list.add(0, bean);//加载到list的最前面
        plNum.setText(""+list.size());
        addComment(bean);
        adapter.notifyDataSetChanged();
    }
    private void addComment(Comment comment){
        Gson gson = new Gson();
        String str = gson.toJson(comment);
        Log.d("sss","str="+str);
        String url = getString(R.string.s_url_addComment);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("comment",str)
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
    private void DelectComment(int postion) {
        list.remove(postion);
        adapter.notifyDataSetChanged();
    }
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 10:
                    position = (Integer) msg.obj;
                    mLytCommentVG.setVisibility(View.GONE);
                    mLytEdittextVG.setVisibility(View.VISIBLE);
                    onFocusChange(true);
                    break;
                case 11:
                    position = (Integer)msg.obj;
                    DelectComment(position);
                    break;
            }
        }
    };
    /**
     * 事件点击监听器
     */
    private final class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.but_comment_send:        //发表评论按钮
                    if (isEditEmply()) {        //判断用户是否输入内容
                        publishComment();
                        mLytCommentVG.setVisibility(View.VISIBLE);
                        mLytEdittextVG.setVisibility(View.GONE);
                        onFocusChange(false);
                    }
                    break;
                case R.id.ping_lun:        //底部评论按钮
                    mLytEdittextVG.setVisibility(View.VISIBLE);
                    mLytCommentVG.setVisibility(View.GONE);
                    onFocusChange(true);
                    break;
                case R.id.zan:        //底部点赞按钮
                    if(!dz) {
                        tie.setZanNum(tie.getZanNum()+1);

                        dz = true;
                        zan.setImageResource(R.mipmap.zan_1);
                        ZName zName=new ZName();
                        zName.setN(user.getNickname());
                        zanName.add(user.getNickname());
                        zanNum.setText(""+zanName.size());
                        updateTie(zanName);
                    }
                    else {
                        zanName.remove(user.getNickname());
                        updateTie(zanName);
                        tie.setZanNum(tie.getZanNum()-1);
                        zanNum.setText(""+zanName.size());
                        dz=false;
                        zan.setImageResource(R.mipmap.zan);
                    }
                    break;
            }
        }
    }
    private void updateTie(List<String> list){
        Gson gson = new Gson();
        String str = gson.toJson(list);
        String url = getString(R.string.s_url_updateTie);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("TieID",""+tie.getId())
                .add("ZanName",str)
                .add("zanNum",zanName.size()+"")
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //判断控件是否显示
        if (mLytEdittextVG.getVisibility() == View.VISIBLE) {
            mLytEdittextVG.setVisibility(View.GONE);
            mLytCommentVG.setVisibility(View.VISIBLE);
        }
    }
    //安卓重写返回键事件
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            Intent intent;
            if(flag==1) {
                 intent= new Intent(this, MainActivity.class);
            }else{
                intent = new Intent(this, MyTieActivity.class);
            }
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        return true;
    }
}
