package com.example.handwriting.other;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.handwriting.R;

public class MyImgBtnH extends LinearLayout {
    private ImageView mImgView = null;
    private TextView mTextView = null;
    private Context mContext;
    public MyImgBtnH(Context context, AttributeSet attrs) {

        super(context, attrs);
        // TODO Auto-generated constructor stub
        LayoutInflater.from(context).inflate(R.layout.img_text_o, this, true);
        mContext = context;
        mImgView = (ImageView)findViewById(R.id.img);
        mTextView = (TextView)findViewById(R.id.text);
    }
    /*设置图片接口*/
    public void setImageResource(int resId){
        mImgView.setImageResource(resId);
    }
    /*设置文字接口*/
    public void setText(String str){
        mTextView.setText(str);
    }
    /*设置文字大小*/
    public void setTextSize(float size){
        mTextView.setTextSize(size);
    }
}
