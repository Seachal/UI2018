package com.zinc.bezier.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * *
 * *
 * Project_Name:UI2018
 *
 * @author zhangxc
 * @date 2020-02-12 15:53
 * *
 */
public class GridView extends View {

    protected String TAG = this.getClass().getSimpleName();

    // 坐标画笔
    private Paint mCoordinatePaint;
    // 贝塞尔曲线画笔
    private Paint mBPaint;
    // 网格画笔
    private Paint mGridPaint;
    // 写字画笔
    private Paint mTextPaint;

    // 坐标颜色
    private int mCoordinateColor;
    private int mGridColor;

    // 网格宽度 50px
    private int mGridWidth = 150;

    // 坐标线宽度
    private final float mCoordinateLineWidth = 2.5f;
    // 网格宽度
    private final float mGridLineWidth = 1f;
    // 字体大小
    private float mTextSize;

    // 标柱的高度
    private final float mCoordinateFlagHeight = 8f;

    protected float mWidth;
    protected float mHeight;


    public GridView(Context context) {
        this(context, null, 0);
    }

    public GridView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public GridView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initCoordinate(context);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);
        drawBezier(canvas);
        drawLineToAndQuadto(canvas);
    }


    protected void initCoordinate(Context context) {
        mCoordinateColor = Color.BLACK;
        mGridColor = Color.LTGRAY;

        mTextSize = spToPx(10);

        mCoordinatePaint = new Paint();
        mCoordinatePaint.setAntiAlias(true);
        mCoordinatePaint.setColor(mCoordinateColor);
        mCoordinatePaint.setStrokeWidth(mCoordinateLineWidth);

        mGridPaint = new Paint();
        mGridPaint.setAntiAlias(true);
        mGridPaint.setColor(mGridColor);
        mGridPaint.setStrokeWidth(mGridLineWidth);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mCoordinateColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mTextSize);



//       贝塞尔曲线，
        mBPaint = new Paint();
        mBPaint.setAntiAlias(true);
        mBPaint.setColor(Color.RED);
        mBPaint.setStrokeWidth(1f);
//      sca:  增加此行代码是画线，不增加此行代码是画面
        mBPaint.setStyle(Paint.Style.STROKE);
    }

    /**
     * 画坐标和网格，以画布中心点为原点
     *
     * @param canvas 画布
     */
    protected void drawCoordinate(Canvas canvas) {

        float halfWidth = mWidth / 2;
        float halfHeight = mHeight / 2;

        // 画网格
        canvas.save();
//        画布向右下角平移
        canvas.translate(halfWidth, halfHeight);
        int curWidth = mGridWidth;
        // 画竖线
        while (curWidth < halfWidth + mGridWidth) {

            // 向上画
            canvas.drawLine(curWidth, -halfHeight, curWidth, halfHeight, mGridPaint);
            // 向下画
            canvas.drawLine(-curWidth, -halfHeight, -curWidth, halfHeight, mGridPaint);


            // 画标柱
            canvas.drawLine(0, curWidth, mCoordinateFlagHeight, curWidth, mCoordinatePaint);
            canvas.drawLine(0, -curWidth, mCoordinateFlagHeight, -curWidth, mCoordinatePaint);

            // 标柱宽度（每两个画一个）sca:刻度
            if (curWidth % (mGridWidth * 2) == 0) {
                canvas.drawText(curWidth + "", curWidth, mTextSize * 1.5f, mTextPaint);
                canvas.drawText(-curWidth + "", -curWidth, mTextSize * 1.5f, mTextPaint);
            }

            curWidth += mGridWidth;
        }

        int curHeight = mGridWidth;
        // 画横线
        while (curHeight < halfHeight + mGridWidth) {

            // 向右画
            canvas.drawLine(-halfWidth, curHeight, halfWidth, curHeight, mGridPaint);
            // 向左画
            canvas.drawLine(-halfWidth, -curHeight, halfWidth, -curHeight, mGridPaint);

            // 画标柱（x 轴sca:刻度）
            canvas.drawLine(curHeight, 0, curHeight, -mCoordinateFlagHeight, mCoordinatePaint);
            canvas.drawLine(-curHeight, 0, -curHeight, -mCoordinateFlagHeight, mCoordinatePaint);


            // 标柱宽度（每两个画一个）
            if (curHeight % (mGridWidth * 2) == 0) {
                canvas.drawText(curHeight + "", -mTextSize * 2, curHeight + mTextSize / 2, mTextPaint);
                canvas.drawText(-curHeight + "", -mTextSize * 2, -curHeight + mTextSize / 2, mTextPaint);
            }

            curHeight += mGridWidth;
        }
        canvas.restore();

        // 画 x，y 轴
        canvas.drawLine(halfWidth, 0, halfWidth, mHeight, mCoordinatePaint);
        canvas.drawLine(0, halfHeight, mWidth, halfHeight, mCoordinatePaint);

    }


    private void drawBezier(Canvas canvas) {
        float halfWidth = mWidth / 2;
        float halfHeight = mHeight / 2;


        // 画网格
        canvas.save();
//        画布向右下角平移
        canvas.translate(halfWidth, halfHeight);
        // 初始化 路径对象
        Path path = new Path();
        // 移动至第一个控制点 A(ax,ay)
        path.moveTo(0, 0);
        // 填充三阶贝塞尔曲线的另外三个控制点：
        // B(bx,by) C(cx,cy) D(dx,dy) 切记顺序不能变
        path.cubicTo(300, 0, 300, 300, 0, 300);
        // 将 贝塞尔曲线 绘制至画布
        canvas.drawPath(path, mBPaint);

    }


    /**
     * 转换 sp 至 px
     *
     * @param spValue sp值
     * @return px值
     */
    protected int spToPx(float spValue) {
        final float fontScale = Resources.getSystem().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 转换 dp 至 px
     *
     * @param dpValue dp值
     * @return px值
     */
    protected int dpToPx(float dpValue) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return (int) (dpValue * metrics.density + 0.5f);
    }


//    成功绘制出一条曲线， 纸上得来终觉浅，敲一敲才会更健康
    private  void   drawLineToAndQuadto(Canvas canvas){
        // 初始化 路径对象
        Path path = new Path();
        // 移动至第一个控制点 A(ax,ay)
        path.lineTo(10, 100);
        path.lineTo(100, 100);
        // 填充二阶贝塞尔曲线的另外两个控制点 B(bx,by) 和 C(cx,cy)，切记顺序不能变
        path.quadTo(140, 100, 160, 60);
        path.quadTo(200,0, 240,60);
        path.quadTo(260,100, 300,100);
        path.lineTo(400, 100);

        // 将 贝塞尔曲线 绘制至画布
        canvas.drawPath(path, mBPaint);

    }

}
