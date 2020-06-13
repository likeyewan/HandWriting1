package com.example.handwriting.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.example.handwriting.R;
import com.example.handwriting.bean.User;
import com.example.handwriting.bean.UserLab;
import com.example.handwriting.db.Learn;
import com.example.handwriting.db.Plan;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;
import java.io.IOException;
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

public class PlanActivity extends Activity {
    TextView title,hsk,hanDay,dayNeed,allHan;
    Button sure;
    Dialog alertDialog2,alertDialog3;
    int size;
    int level,dayplan;
    String timeStr;//当前日期
    List<Plan> plans=new ArrayList<>();
    User nUser=new User();
    public void onCreate(Bundle s) {
        super.onCreate(s);
        setContentView(R.layout.activity_plan);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        Date date = new Date(System.currentTimeMillis());
        timeStr=simpleDateFormat.format(date);
        nUser = UserLab.get(PlanActivity.this,"","").getUser();
        plans= DataSupport.where("name = ? and cc = ?",nUser.getPhoneNum(),"1").find(Plan.class);
        level=plans.get(0).getLevel();
        dayplan=plans.get(0).getDayPlan();
        init();
    }
    public void init(){
        title=(TextView)findViewById(R.id.tv_title);
        hsk=(TextView)findViewById(R.id.hsk);
        hanDay=(TextView)findViewById(R.id.day_plan);
        dayNeed=(TextView)findViewById(R.id.day_need);
        allHan=(TextView)findViewById(R.id.han_num);
        sure=(Button)findViewById(R.id.p_queren);
        title.setText("计划");
        for(int i=0;i<6;i++) {
            if(i==level) {
                hsk.setText("HSK" + (i + 1) + "级");
                if (i == 0) {
                    size = 172;
                } else if (i == 1) {
                    size = 344;
                } else if (i == 2) {
                    size = 622;
                } else if (i == 3) {
                    size = 1075;
                } else if (i == 4) {
                    size = 1;
                } else if (i == 5) {
                    size = 1;
                }
            }
        }
        hanDay.setText(""+dayplan);
        allHan.setText(""+size);
        int d;
        if(size%dayplan==0) {
            d = size/ dayplan;
        }else
            d=size/dayplan+1;
        dayNeed.setText(""+d);
        hsk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showHSKDialog();
            }
        });
        hanDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSingleAlertDialog();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Plan>plans1= DataSupport.where("name = ? and cc = ?",nUser.getPhoneNum(),"1").find(Plan.class);
                Plan plan0=new Plan();
                plan0.setToDefault("cc");
                plan0.updateAll("name = ? and cc= ? ",nUser.getPhoneNum(),"1");
                List<Plan>plans3= DataSupport.where("name = ? and level = ?",nUser.getPhoneNum(),""+plans1.get(0).getLevel()).find(Plan.class);
                updatePlan(plans3.get(0));

                Plan plan=new Plan();
                plan.setCc(1);
                plan.updateAll("name = ? and level= ? ",nUser.getPhoneNum(),""+level);
                Plan plan1=new Plan();
                plan1.setDayPlan(dayplan);
                plan1.updateAll("name = ? and cc= ? ",nUser.getPhoneNum(),"1");
                List<Plan>plans2= DataSupport.where("name = ? and cc = ?",nUser.getPhoneNum(),"1").find(Plan.class);
                updatePlan(plans2.get(0));
                Intent intent=new Intent(PlanActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
    private void updatePlan(Plan plan){
        Gson gson = new Gson();
        String str = gson.toJson(plan);
        Log.d("sss","str="+str);
        String url = getString(R.string.s_url_updatePlan);
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("plan",str)
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
    public void showHSKDialog(){
        final String[] items = {"HSK1级", "HSK2级", "HSK3级", "HSK4级","HSK5级","HSK6级"};
        int d=level;
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("HSK等级");
        alertBuilder.setSingleChoiceItems(items, d, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i==0) {
                    size = 172;
                }else if(i==1){
                    size = 344;
                } else if(i==2){
                    size = 622;
                } else if(i==3){
                    size = 1075;
                }else if(i==4){
                    size = 1;
                }else if(i==5){
                    size = 1;
                }
                level=i;
                hsk.setText(items[i]);
                allHan.setText(""+size);
                int day=0;
                if(size%dayplan==0) {
                    day = size/ dayplan;
                }else
                    day=size/dayplan+1;

                dayNeed.setText(""+day);
                alertDialog3.dismiss();
            }
        });
        alertDialog3 = alertBuilder.create();
        alertDialog3.show();
        final WindowManager.LayoutParams params = alertDialog3.getWindow().getAttributes();
        params.width = 900;
        params.height = 900;
        alertDialog3.getWindow().setAttributes(params);
    }
    public void showSingleAlertDialog(){
        final String[] items = {"10","15", "20", "25", "30","35", "40", "45", "50","60", "70", "80","100"};
        int d=0;
        for(int i=0;i<items.length;i++){
            if(dayplan==Integer.valueOf(items[i])) {
                d = i;
                break;
            }
        }
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("每天练习汉字数量");
        alertBuilder.setSingleChoiceItems(items, d, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                int s=Integer.parseInt(items[i]);
                dayplan=s;
                int d;
                if(size%s==0) {
                    d = size/ s;
                }else
                    d=size/s+1;
                hanDay.setText(""+s);
                dayNeed.setText(""+d);
                alertDialog2.dismiss();
            }
        });
        alertDialog2 = alertBuilder.create();
        alertDialog2.show();
        final WindowManager.LayoutParams params = alertDialog2.getWindow().getAttributes();
        params.width = 900;
        params.height = 900;
        alertDialog2.getWindow().setAttributes(params);
    }
}
