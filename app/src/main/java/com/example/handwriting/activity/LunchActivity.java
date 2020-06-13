package com.example.handwriting.activity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.handwriting.db.Userdb;
import org.litepal.crud.DataSupport;
import static java.lang.Thread.sleep;

public class LunchActivity extends AppCompatActivity {
    private Userdb userdb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        new Thread( new Runnable( ) {
            @Override
            public void run() {
                //耗时任务，比如加载网络数据
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 这里可以睡几秒钟，如果要放广告的话
                        try {
                            sleep(1000);
                        }catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        userdb= DataSupport.findFirst(Userdb.class);
                        if(userdb!=null){
                            Intent intent = new Intent(LunchActivity.this,LoginActivity.class);
                            LunchActivity.this.finish();
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(LunchActivity.this, LoginActivity.class);
                            LunchActivity.this.finish();
                            startActivity(intent);
                        }
                    }
                });
            }
        } ).start();
    }

}
