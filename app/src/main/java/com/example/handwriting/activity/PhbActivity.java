package com.example.handwriting.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.handwriting.fragment.DayFragment;
import com.example.handwriting.fragment.Fragment3;
import com.example.handwriting.fragment.ZiFragment;
import com.example.handwriting.R;

public class PhbActivity extends Activity {
    TextView title;
    private TextView fsBar;
    private TextView ziBar,dayBar;
    Fragment3 fsFragment;
    DayFragment dayFragment;
    ZiFragment ziFragment;
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phb);
        fsBar = (TextView) findViewById(R.id.p_fs);
        ziBar = (TextView) findViewById(R.id.p_zi);
        dayBar =(TextView)findViewById(R.id.p_day);
        fsBar.setClickable(true);
        ziBar.setClickable(true);
        ziBar.setClickable(true);
        setAllNotSelected();
        fsBar.setSelected(true);
        fsFragment = new Fragment3();
        dayFragment=new DayFragment();
        ziFragment = new ZiFragment();
        fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.p_frame, fsFragment).commit();
        title=(TextView)findViewById(R.id.tv_title);
        title.setText("排行榜");
    }
    private void setAllNotSelected() {
        fsBar.setSelected(false);
        ziBar.setSelected(false);
        dayBar.setSelected(false);
    }
    public void tabSelect(View v) {
        setAllNotSelected();
        switch (v.getId()) {
            case R.id.p_fs:
                fsBar.setSelected(true);
                fragmentManager.beginTransaction().replace(R.id.p_frame,fsFragment).commit();
                break;
            case R.id.p_day:
                dayBar.setSelected(true);
                fragmentManager.beginTransaction().replace(R.id.p_frame,dayFragment).commit();
                break;
            case R.id.p_zi:
                ziBar.setSelected(true);
                fragmentManager.beginTransaction().replace(R.id.p_frame, ziFragment).commit();
                break;
            default:
                break;
        }
    }
}
