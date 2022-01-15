package com.example.weatherapplication.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.example.weatherapplication.util.UtilApp;

import java.util.ArrayList;

/**
 * @author zero
 * <p/>
 * 这是一个简约的折线图  适合展示一个趋势
 * <p/>
 */
public class LineChartView extends View implements HScrollView.OnMyHScrollView{
    private static final String TAG = "LineChartView";
    private HScrollView mHScrollView;

    //Y轴  每个刻度的间距间距
    private int myInterval = 1;
    //X轴  每个刻度的间距间距
    private int mxInterval = 100;
    //Y轴距离view长度
    private int mLeftInterval = 50;
    //X轴距离view长度
    private int mBottomInterval = 50;
    //天气与X轴距离
    private int mWeatherToXaxis = 20;
    //小短线与小圆点的距离
    private int mLineToPoint = 10;
    //View 的宽和高
    private int mWidth, mHeight;
    //Y轴字体的大小
    private float mYAxisFontSize = 30;
    //线的颜色
    private int mLineColor = Color.parseColor("#24c2f0");
    //线条的宽度
    private float mStrokeWidth = 4.0f;
    //温度与点的距离
    private int TempToPoint = 12;
    //点的半径
    private float mPointRadius = 5;

    //X轴的文字 (时间)
    private ArrayList<String> mXAxis;
    //点 (温度)
    private ArrayList<Double> mYAxis;
    // 天气
    private ArrayList<String> mWeather;
    //滑动的距离
    private float mScrollLeft;


    public LineChartView(Context context) {
        this(context, null);
    }

    public LineChartView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        //自定义view 像素 宽度和高度
        Log.d(TAG,"widthSize:"+widthSize+",heightSize:"+heightSize );
        //高度固定  150dp  == 304 px
        mHeight =heightSize;
        Log.d(TAG,"------mWidth:"+mWidth+",mHeight:"+mHeight +"    mXAxis:"+mXAxis);
        if(mXAxis == null){
            Log.d(TAG,"mWidth:"+mWidth+",mHeight:"+mHeight +"     mXAxis:"+mXAxis);
            return;
        }
        //宽度通过数组长度计算
        mWidth = mxInterval*(mXAxis.size()-1) + mLeftInterval*2;
        //1185dp  150dp
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(mXAxis.size()==0||mYAxis.size()==0){
            Log.e(TAG,"数据异常");
            return;
        }
        //画坐标线的轴
        Paint axisPaint = new Paint();
        axisPaint.setTextSize(mYAxisFontSize);
        axisPaint.setColor(Color.parseColor("#000000"));
            //画 X 轴
            Paint xlinePaint = new Paint();

            xlinePaint.setColor(mLineColor);
            xlinePaint.setAntiAlias(true);
            //设置线条宽度
            xlinePaint.setStrokeWidth(mStrokeWidth);
            //画时间线条
            canvas.drawLine(mLeftInterval,mHeight - mBottomInterval, (24-1)*mxInterval + mLeftInterval, mHeight - mBottomInterval, xlinePaint);

            //x轴的刻度集合
            int[] xPoints = new int[mXAxis.size()];

            for (int i = 0; i < mXAxis.size(); i++) {
                float timeWidth = axisPaint.measureText(mXAxis.get(i))/2 ; //时间宽度一半
                float xfloat  = i * mxInterval + mLeftInterval-timeWidth;
                //画时间
                canvas.drawText(mXAxis.get(i), xfloat, mHeight - mBottomInterval+mYAxisFontSize, axisPaint);
                xPoints[i] = (int) (xfloat+timeWidth);
            }
            //画笔 圆点
            Paint pointPaint = new Paint();
            pointPaint.setColor(mLineColor);
            pointPaint.setStrokeWidth((float) 3.0);
            pointPaint.setStyle(Paint.Style.STROKE);
//            pointPaint.setStyle(Paint.Style.FILL);
            //画笔 线条
            Paint linePaint = new Paint();
            linePaint.setColor(mLineColor);
            linePaint.setAntiAlias(true);
            //设置线条宽度
            linePaint.setStrokeWidth(mStrokeWidth);

            int count = 1; //相同天气个数
            //屏幕宽度
            int mScreenWidth = UtilApp.getScreenWidth(getContext());
            for (int i = 0; i < mXAxis.size(); i++) {
                double xx = mYAxis.get(i) - mYAxis.get(0);//和第一个温度的差
                double yAxisCentre = ( mHeight - mBottomInterval)/2;  // Y轴中心点
                if(xx != 0){
                    yAxisCentre = yAxisCentre - xx * myInterval;
                }
                //画圆点
                canvas.drawCircle(xPoints[i], (float)yAxisCentre, mPointRadius, pointPaint);
                int tempWidth = (int) axisPaint.measureText(""+mYAxis.get(i)); //温度字体宽度
                //画温度
                canvas.drawText(mYAxis.get(i)+"", xPoints[i] - mPointRadius - tempWidth/2, (float)yAxisCentre - mPointRadius - TempToPoint, axisPaint);
                double yAxisCentre2 = ( mHeight - mBottomInterval)/2; // Y轴中心点
                int rightDifference = (int) (xPoints[i] - mScreenWidth - mScrollLeft);//右边看不见的点与屏幕的距离


                if(0 == i){
//                    //画虚线  第一条
                    PaintDashed(canvas, xPoints[i], (int) (yAxisCentre + mPointRadius), xPoints[i], mHeight - mBottomInterval );
                }else if ( i < (mXAxis.size() - 1) ) {
                    double xx2 = mYAxis.get(i-1) - mYAxis.get(0);
                    if(xx2 != 0){
                        yAxisCentre2 = yAxisCentre2 - xx2 * myInterval;
                    }
                    //温度之间 短线
                    canvas.drawLine(xPoints[i - 1] + mLineToPoint,(float)yAxisCentre2, xPoints[i] - mLineToPoint,(float)yAxisCentre , linePaint);
                    //画天气 画虚线
                    /**
                     *虚线是天气区域的分割线
                     *从第二个天气开始 循环判断天气,不同的时候才 画虚线 画天气
                     *
                     */
                    if(!mWeather.get(i).equals(mWeather.get(i-1))){
                        //画虚线
                        PaintDashed(canvas, xPoints[i], (int) (yAxisCentre + mPointRadius), xPoints[i], mHeight - mBottomInterval );

                        float weatherFontSize = axisPaint.measureText(mWeather.get(i-count));//天气字体宽度
                        if(mScrollLeft > mLeftInterval){ //左边   滑动大于边距50时候 天气移动 始终在在中间
                            if(xPoints[i] - mScrollLeft <  count*mxInterval){
                                if(mScrollLeft< xPoints[i] - weatherFontSize){ //判断当天气移动到虚线边界时候 就不移动
                                    float is2 = (xPoints[i-count] + count*mxInterval- mScrollLeft)/2+mScrollLeft;
                                    canvas.drawText(mWeather.get(i-count),is2 - mYAxisFontSize,mHeight - mBottomInterval - mWeatherToXaxis, axisPaint); // 20  天气字体与x轴距离
                                } else{
                                    canvas.drawText(mWeather.get(i-count),xPoints[i] - weatherFontSize,mHeight - mBottomInterval - mWeatherToXaxis, axisPaint);
                                }
                            }else if(rightDifference > 0){//    右边天气位置  滑动时候
                                if(mScreenWidth +  mScrollLeft > xPoints[i - count] + weatherFontSize){//判断当天气移动到虚线边界时候 就不移动
                                    float x =  mScreenWidth + mScrollLeft - (count * mxInterval - rightDifference)/2;
                                    canvas.drawText(mWeather.get(i - count), x - mYAxisFontSize, mHeight - mBottomInterval - mWeatherToXaxis, axisPaint);
                                }else{
                                    canvas.drawText(mWeather.get(i-count),xPoints[i - count],mHeight - mBottomInterval - mWeatherToXaxis, axisPaint);
                                }
                            }else{//天气位置  中间天气区域
                                canvas.drawText(mWeather.get(i - count), xPoints[i - count] + count * mxInterval / 2 - mYAxisFontSize, mHeight - mBottomInterval - mWeatherToXaxis, axisPaint);
                            }

                        }else { //天气位置  初始化 没有滑动时候
                            canvas.drawText(mWeather.get(i - count) , xPoints[i - count] + count * mxInterval / 2 - mYAxisFontSize, mHeight - mBottomInterval - mWeatherToXaxis, axisPaint);
                        }
                        count = 1;
                    }else{
                        count = count+1; //每个时间段天气相同就加1
                    }
                }else if((mXAxis.size()-1) == i){  //最后一个区域
                    float weatherFontSize = axisPaint.measureText(mWeather.get(i));//天气字体宽度
                    //温度之间 短线
                    canvas.drawLine(xPoints[i - 1] + mLineToPoint, (float)yAxisCentre2, xPoints[i] - mLineToPoint,(float)yAxisCentre , linePaint);
                    //画天气  最后一个天气区域
                    if(rightDifference > 0){//    //最有右边天气位置  滑动时候
                        if(mScreenWidth +  mScrollLeft > xPoints[i - count] + weatherFontSize){
                            float x =  mScreenWidth + mScrollLeft - (count * mxInterval - rightDifference)/2;
                            canvas.drawText(mWeather.get(i - count) , x - mYAxisFontSize, mHeight - mBottomInterval - mWeatherToXaxis, axisPaint);
                        }else{
                            canvas.drawText(mWeather.get(i-count),xPoints[i - count],mHeight - mBottomInterval - mWeatherToXaxis, axisPaint);
                        }
                    }else{
                        canvas.drawText(mWeather.get(i - count) , xPoints[i - count] + count * mxInterval / 2 - mYAxisFontSize, mHeight - mBottomInterval - mWeatherToXaxis, axisPaint);
                    }
                    //画虚线  最后一条
                    PaintDashed(canvas, xPoints[i], (int) (yAxisCentre + mPointRadius), xPoints[i], mHeight - mBottomInterval );

                }

            }

    }


    /**
     * 画虚线
     * @param canvas
     * @param moveToX
     * @param moveToY
     * @param lineToX
     * @param lineToY
     */
    private void PaintDashed (Canvas canvas, int moveToX, int moveToY, int lineToX, int lineToY){
        DashPathEffect pathEffect = new DashPathEffect(new float[] { 6,4 }, 0);
        Paint paint = new Paint();
        paint.reset();
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.WHITE);
        paint.setAntiAlias(true);
        paint.setPathEffect(pathEffect);
        Path path = new Path();
        path.moveTo(moveToX, moveToY);
        path.lineTo(lineToX, lineToY);
        canvas.drawPath(path, paint);
    }


    /**
     * 设置Y轴文字  温度值
     * @param yItem
     */
    public void setYItem(ArrayList<Double> yItem){
        Log.d("fan","====setYItem==:");
        mYAxis = yItem;
    }

    /**
     * 设置X轴文字
     * @param xItem
     */
    public void setXItem(ArrayList xItem){
        Log.d("fan","====setXItem==:");
        mXAxis = xItem;
    }
    /**
     * 设置天气
     * @param weather
     */
    public void setWeather(ArrayList weather){

        mWeather = weather;
    }
    public void setLineColor(int color){
        mLineColor = color;
        invalidate();
    }

    /**
     *
     * @param mHScrollView
     */
    public void setmHScrollView(HScrollView mHScrollView) {
        Log.d("SimpleLineChart2", "mHScrollView:"+mHScrollView);
        if(mHScrollView == null){
            return;
        }
        this.mHScrollView = mHScrollView;
        this.mHScrollView.setmOnMyHScrollView(this);
    }

    /**
     * 监听滑动事件(主要用到与左边的滑动距离)
     * @param l
     * @param t
     * @param oldl
     * @param oldt
     */
    @Override
    public void onMyScrollChanged(int l, int t, int oldl, int oldt) {
//        Log.d(TAG, "l:" + l+",t:" + t+",oldl:" + oldl+",oldt:" + oldt);
        this.mScrollLeft = l;
        invalidate();
    }
}

