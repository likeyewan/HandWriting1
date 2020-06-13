package com.example.handwriting.other;



import android.os.CountDownTimer;
import android.widget.Button;


public class CountDownTimerUtils extends CountDownTimer {
    private Button mButton;
    public CountDownTimerUtils(Button button,long millisInFuture, long countDownInterval) {//控件，定时总时间,间隔时间
        super(millisInFuture, countDownInterval);
        this.mButton=button;
    }
    @Override
    public void onTick(long millisUntilFinished) {
        mButton.setClickable(false);//设置不可点击
        mButton.setText(millisUntilFinished/1000+"s");//设置倒计时时间
    }
    @Override
    public void onFinish() {
        mButton.setClickable(true);//重新获得点击
        mButton.setText("重新获取");
    }
}

