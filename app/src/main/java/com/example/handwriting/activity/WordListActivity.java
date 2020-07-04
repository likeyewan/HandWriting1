package com.example.handwriting.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.FragmentManager;

import com.example.handwriting.adapter.ViewPagerAdapter;
import com.example.handwriting.adapter.WordPagerAdapter;
import com.example.handwriting.fragment.Fragment1;
import com.example.handwriting.fragment.Fragment2;
import com.example.handwriting.R;
import com.google.android.material.tabs.TabLayout;

public class WordListActivity extends AppCompatActivity {
    TextView title;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.word_list);
        mTabLayout=findViewById(R.id.tab_layout);
        mViewPager=findViewById(R.id.view_pager);
        title=(TextView)findViewById(R.id.tv_title);
        title.setText("汉字列表");
        initTabLayout();
    }

    private void initTabLayout() {
        PagerAdapter adapter=new WordPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }
}
