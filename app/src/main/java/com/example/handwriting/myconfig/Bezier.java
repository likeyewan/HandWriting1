package com.example.handwriting.myconfig;


import com.example.handwriting.bean.WordPoint;

public class Bezier {
    //控制点
    private WordPoint mControl = new WordPoint();
    //距离
    private WordPoint mDestination = new WordPoint();
    //下一个需要控制点
    private WordPoint mNextControl = new WordPoint();
    //资源的点
    private WordPoint mSource = new WordPoint();

    public Bezier() {
    }

    public void init(WordPoint last, WordPoint cur)
    {
        init(last.x, last.y, last.width, cur.x, cur.y, cur.width);
    }

    public void init(float lastx, float lasty, float lastWidth, float x, float y, float width)
    {
        //资源点设置，最后的点的为资源点
        mSource.set(lastx, lasty, lastWidth);
        float xmid = getMid(lastx, x);
        float ymid = getMid(lasty, y);
        float wmid = getMid(lastWidth, width);
        //距离点为平均点
        mDestination.set(xmid, ymid, wmid);
        //控制点为当前的距离点
        mControl.set(getMid(lastx,xmid),getMid(lasty,ymid),getMid(lastWidth,wmid));
        //下个控制点为当前点
        mNextControl.set(x, y, width);
    }

    public void addNode(WordPoint cur){
        addNode(cur.x, cur.y, cur.width);
    }

    public void addNode(float x, float y, float width){
        mSource.set(mDestination);
        mControl.set(mNextControl);
        mDestination.set(getMid(mNextControl.x, x), getMid(mNextControl.y, y), getMid(mNextControl.width, width));
        mNextControl.set(x, y, width);
    }

    public void end() {
        mSource.set(mDestination);
        float x = getMid(mNextControl.x, mSource.x);
        float y = getMid(mNextControl.y, mSource.y);
        float w = getMid(mNextControl.width, mSource.width);
        mControl.set(x, y, w);
        mDestination.set(mNextControl);
    }
   //获取点
    public WordPoint getPoint(double t){
        float x = (float)getX(t);
        float y = (float)getY(t);
        float w = (float)getW(t);
        WordPoint wordPoint = new WordPoint();
        wordPoint.set(x,y,w);
        return wordPoint;
    }

    //三阶曲线的控制点
    private double getValue(double p0, double p1, double p2, double t){
        double A = p2 - 2 * p1 + p0;
        double B = 2 * (p1 - p0);
        double C = p0;
        return A * t * t + B * t + C;
    }

    private double getX(double t) {
        return getValue(mSource.x, mControl.x, mDestination.x, t);
    }
    private double getY(double t) {
        return getValue(mSource.y, mControl.y, mDestination.y, t);
    }
    private double getW(double t){
        return getWidth(mSource.width, mDestination.width, t);
    }

    private float getMid(float x1, float x2) {
        return (float)((x1 + x2) / 2.0);
    }

    private double getWidth(double w0, double w1, double t){
        return w0 + (w1 - w0) * t;
    }

}
