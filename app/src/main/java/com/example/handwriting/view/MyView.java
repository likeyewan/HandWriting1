package com.example.handwriting.view;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.example.handwriting.bean.WordPoint;
import com.example.handwriting.myconfig.PenConfig;
import com.example.handwriting.myconfig.Bezier;

public class MyView extends View {
	public ArrayList<WordPoint> mPointsB  = new ArrayList<>() ;
	public ArrayList<WordPoint>   mPointList = new ArrayList<WordPoint>();
	public List<List<WordPoint>> listPointB=new ArrayList<>();
	public WordPoint   mLastPoint = new WordPoint(0, 0);
	public double mBaseWidth;
	public double mLastVel;
	public double mLastWidth;
	public Bezier mBezier = new Bezier();
	protected WordPoint mCurPoint;
	public boolean canDraw=true;
	public boolean canReDraw=false;
	public boolean canPlay=false;
	public Paint mBitmapPaint;
	public Paint mPaint;
	public Canvas cacheCanvas;
	public Bitmap cachebBitmap;
	public Path path;
	public Paint paint;
	public String Txt="";
	public MyView(Context context, AttributeSet attrs) {
		super(context, attrs);
		super.setOnTouchListener(new OnTouchListenerImpl());
		cachebBitmap=Bitmap.createBitmap(596,372, Config.ARGB_8888);
		mBitmapPaint = new Paint(Paint.DITHER_FLAG);
		cacheCanvas = new Canvas(cachebBitmap);
		path = new Path();
		initPaint(context);
		initPaint1();
	}
	private void initPaint1() {
		paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setStrokeWidth(60);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeCap(Paint.Cap.ROUND);//结束的笔画为圆心
		paint.setStrokeJoin(Paint.Join.ROUND);//连接处元
		paint.setAlpha(0xFF);
		paint.setAntiAlias(true);
		paint.setStrokeMiter(1.0f);
		mBaseWidth = paint.getStrokeWidth();
	}
	private class OnTouchListenerImpl implements OnTouchListener{
		public boolean onTouch(View v, MotionEvent event) {
			if(!canDraw)
			{
				return true;
			}
			int action = event.getAction() ;
			//屏幕触点小于2
			if(event.getPointerCount()<2) {
				//手按下
				if (action == MotionEvent.ACTION_DOWN) {
					canReDraw = false;
					canPlay = false;
					onDown(event);
				}
				//手移动
				else if (action == MotionEvent.ACTION_MOVE) {
					onMove(event);
				}
				//手抬起
				else if (action == MotionEvent.ACTION_UP) {
					onUp(event);
					canReDraw = true;
					canPlay = true;
				}
			}
			invalidate() ;
			return true;
		}
	}
	public void onDown(MotionEvent event){
		//记录down的控制点的信息
		WordPoint curPoint = new WordPoint(event.getX(), event.getY());
		mPointsB = new ArrayList<>() ;
		mLastWidth = 0.8 * mBaseWidth;
		//down下的点的宽度
		curPoint.width = (float) mLastWidth;
		mLastVel = 0;
		mPointList.add(curPoint);
		//记录当前的点
		mLastPoint = curPoint;

	}
	/**
	 * 手指移动的事件
	 */
	public void onMove(MotionEvent event){
		WordPoint curPoint = new WordPoint(event.getX(),event.getY());
		double deltaX = curPoint.x - mLastPoint.x;
		double deltaY = curPoint.y - mLastPoint.y;
		//平方和的平方根,即两点距离
		double curDis = Math.hypot(deltaX, deltaY);
		//值越小，画的点越多；值越大，绘制的越少，笔就越细，宽度越小
		double curVel = curDis * PenConfig.WIDTH;
		double curWidth;
		if (mPointList.size() < 2) {
			curWidth = calcNewWidth(curVel, mLastVel, curDis, 1.5);
			curPoint.width = (float) curWidth;
			mBezier.init(mLastPoint, curPoint);
		} else {
			mLastVel = curVel;
			//滑动的速度越快，值就越小，越慢就越大
			curWidth = calcNewWidth(curVel, mLastVel, curDis, 1.5);
			curPoint.width = (float) curWidth;
			mBezier.addNode(curPoint);
		}
		//每次移动的话，这里赋值新的值
		mLastWidth = curWidth;
		mPointList.add(curPoint);
		moveNeetToDo(curDis);
		//mPointsB.add(curPoint);
		mLastPoint = curPoint;
	}
	/**
	 * 手指抬起来的事件
	 */
	public void onUp(MotionEvent event){
		mCurPoint = new WordPoint(event.getX(), event.getY());
		double deltaX = mCurPoint.x - mLastPoint.x;
		double deltaY = mCurPoint.y - mLastPoint.y;
		double curDis = Math.hypot(deltaX, deltaY);
		mCurPoint.width = 0;
		mPointList.add(mCurPoint);
		mBezier.addNode(mCurPoint);
		int steps = 1 + (int) curDis / PenConfig.TIME;
		double step = 1.0 / steps;
		for (double t = 0; t < 1.0; t += step) {
			WordPoint point = mBezier.getPoint(t);
			mPointsB.add(point) ;
		}
		mBezier.end();
		listPointB.add(mPointsB);
		//Log.d("ds","ds="+listPointB);
		//mPoints.add(mCurPoint) ;
		//一个字的点
		//listPoint.add(mPoints);
		mPointList.clear();
	}
	public double calcNewWidth(double curVel, double lastVel, double curDis,
							   double factor) {
		double calVel = curVel * 0.6 + lastVel * (1 - 0.6);
		//手指滑动的越快，这个值越小
		double vfac = Math.log(factor * 2.0f) * (-calVel);
		//当手指没有滑动的时候 这个值为1 当滑动很快的时候无线趋近于0
		double calWidth = mBaseWidth * Math.exp(vfac);
		return calWidth;
	}
	protected void moveNeetToDo(double curDis) {
		int steps = 1 + (int) curDis / PenConfig.TIME;
		double step = 1.0 / steps;
		//Log.d("ds","ds="+step);
		for (double t = 0; t < 1.0; t += step) {
			WordPoint point = mBezier.getPoint(t);
			mPointsB.add(point);
		}
	}
	private void initPaint(Context context) {
		AssetManager assetManager= context.getAssets();
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setStrokeWidth(3);
		mPaint.setColor(Color.RED);
		mPaint.setTextSize(900);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setTypeface(Typeface.createFromAsset(assetManager, "font/kaiti.ttf"));
	}
	public void draw1(Canvas canvas) {
		paint.setStyle(Paint.Style.FILL);
		drawNeetToDo(canvas);
	}
	protected void drawNeetToDo(Canvas canvas) {
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
						drawLine(canvas,first.x, first.y,first.width, last.x, last.y,last.width, paint) ;
					}
				}
			}
		}
//过程
		if(mPointsB.size() > 1)
		{
			Iterator<WordPoint> pIterator = mPointsB.iterator() ;
			WordPoint first  = null ;
			WordPoint last = null ;
			while(pIterator.hasNext())
			{
				if(first == null)
				{
					first = pIterator.next();
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
	//画汉字模板
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
		//计算长宽
		int x = 0;
		int y = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
		canvas.drawText(Txt, x,y, mPaint);
		draw1(canvas);
	}
	private void drawLine(Canvas canvas, double x0, double y0, double w0, double x1, double y1, double w1, Paint paint) {
		//两点距离越大，画的椭圆越多
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
			//椭圆的参数
			RectF oval = new RectF();
			oval.set((float) (x - w / 4.0f), (float) (y - w / 2.0f), (float) (x + w / 4.0f), (float) (y + w / 2.0f));
			//绘制椭圆
			canvas.drawOval(oval, paint);
			x += deltaX;
			y += deltaY;
			w += deltaW;
		}
	}
}