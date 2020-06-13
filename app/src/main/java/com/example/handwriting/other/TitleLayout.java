package com.example.handwriting.other;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.handwriting.R;


public class TitleLayout extends LinearLayout {
    private TextView mTitleTextView;
    public TitleLayout(Context context, AttributeSet attrs){
        super(context,attrs);
        LayoutInflater.from(context).inflate(R.layout.title,this);
        ImageView titleBack=(ImageView) findViewById(R.id.title_back);
        ImageView titleok=(ImageView) findViewById(R.id.title_ok);
        titleBack.setVisibility(View.VISIBLE);
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        titleBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Activity)getContext()).finish();
            }
        });
    }
    //设置标题内容
    public void setTitle(int titleId) {
        mTitleTextView.setText(titleId);
    }
    //设置标题内容
    public void setTitle(CharSequence title) {
        mTitleTextView.setText(title);
    }
}
