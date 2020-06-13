package com.example.handwriting.view;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import com.example.handwriting.bean.WordPoint;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ReView extends View {
    public List<WordPoint> mPointsB  = new ArrayList<>() ;
    public List<List<WordPoint>> listPointB=new ArrayList<>();
    public boolean canDraw=true;
    public boolean canReDraw=false;
    public Paint mBitmapPaint;
    public Canvas cacheCanvas;
    public Bitmap cachebBitmap;
    public Paint paint,mPaint;
    public String Txt="";
    public ReView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint =  new Paint() ;
        paint.setColor(Color.BLACK) ;//设置画笔颜色为黑色
        paint.setAntiAlias(true);
        paint.setStrokeWidth(100);
        paint.setStyle(Paint.Style.STROKE);
        cachebBitmap=Bitmap.createBitmap(596,372, Bitmap.Config.ARGB_8888);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        cacheCanvas = new Canvas(cachebBitmap);
        initPaint(context);
    }
    private void initPaint(Context context) {
        AssetManager assetManager= context.getAssets();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeWidth(2);
        mPaint.setColor(Color.RED);
        mPaint.setTextSize(900);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setTypeface(Typeface.createFromAsset(assetManager, "font/kaiti.ttf"));
    }
    //画线
    @Override
    protected void onDraw(Canvas canvas) {
        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
        //计算长宽
        int x = 0;
        int y = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
        canvas.drawText(Txt, x,y, mPaint);
        paint.setStyle(Paint.Style.FILL);
        super.onDraw(canvas);
        //过程
        for(List<WordPoint> itemPoints : listPointB)
        {
            if(itemPoints.size() > 1)
            {
                Iterator<WordPoint> pIterator = itemPoints.iterator() ;
                WordPoint first  = null ;
                WordPoint last = null ;
                while(pIterator.hasNext())
                {
                    if(first == null)
                    {
                        first = pIterator.next() ;
                    }
                    else
                    {
                        if(last != null )
                        {
                            first =last;
                        }
                        last = pIterator.next() ;
                        drawLine(canvas,first.x, first.y,first.width, last.x, last.y,last.width, paint);
                    }
                }
            }
        }
        //结果
        if(mPointsB.size() > 1&&!canReDraw)
        {
            Iterator<WordPoint> pIterator = mPointsB.iterator() ;
            WordPoint first  = null ;
            WordPoint last = null ;
            while(pIterator.hasNext())
            {
                if(first == null)
                {
                    first = pIterator.next() ;
                }
                else
                {
                    if(last != null )
                    {
                        first =last;
                    }
                    try {
                        last = pIterator.next();
                        drawLine(canvas, first.x, first.y, first.width, last.x, last.y, last.width, paint);
                    }catch (Exception e){

                    }
                }
            }
        }
    }
    private void drawLine(Canvas canvas, double x0, double y0, double w0, double x1, double y1, double w1, Paint paint) {
        double curDis = Math.hypot(x0 - x1, y0 - y1);
        int steps = 1;
        if (paint.getStrokeWidth() < 6) {
            steps = 1 + (int) (curDis / 2);
        } else if (paint.getStrokeWidth() > 60) {
            steps = 1 + (int) (curDis / 4);
        } else {
            steps = 1 + (int) (curDis / 3);
        }
        double deltaX = (x1 - x0) / steps;
        double deltaY = (y1 - y0) / steps;
        double deltaW = (w1 - w0) / steps;
        double x = x0;
        double y = y0;
        double w = w0;
        for (int i = 0; i < steps; i++) {
            RectF oval = new RectF();
            oval.set((float) (x - w / 4.0f), (float) (y - w / 2.0f), (float) (x + w / 4.0f), (float) (y + w / 2.0f));
            canvas.drawOval(oval, paint);
            x += deltaX;
            y += deltaY;
            w += deltaW;
        }
    }
}
