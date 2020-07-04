package com.example.handwriting.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;


import com.example.handwriting.adapter.ViewPagerAdapter;
import com.example.handwriting.fragment.DayFragment;
import com.example.handwriting.fragment.Fragment3;
import com.example.handwriting.fragment.ZiFragment;
import com.example.handwriting.R;
import com.google.android.material.tabs.TabLayout;

public class PhbActivity extends AppCompatActivity {
    TextView title;
    private TextView fsBar;
    private TextView ziBar,dayBar;
    Fragment3 fsFragment;
    DayFragment dayFragment;
    ZiFragment ziFragment;
    FragmentManager fragmentManager;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phb);
        mTabLayout=findViewById(R.id.tab_layout);
        mViewPager=findViewById(R.id.view_pager);
        title=(TextView)findViewById(R.id.tv_title);
        title.setText("排行榜");
        initTabLayout();
    }
    private void initTabLayout() {
        PagerAdapter adapter=new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
