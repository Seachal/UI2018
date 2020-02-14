package com.zinc.pathmeasure;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;

import com.zinc.lib_base.BaseView;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/4
 * @description 加载圈
 */

public class LoadingView extends BaseView {
    private Path mPath;
    private Paint mPaint;
    private PathMeasure mPathMeasure;
    private float mAnimatorValue;
    private Path mDst;
    private float mLength;

    public LoadingView(Context context) {
        super(context);
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void init(Context context) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5);
        mPaint.setStrokeCap(Paint.Cap.ROUND);

        mPath = new Path();
        mPath.addCircle(0, 0, 100, Path.Direction.CCW);

        mPathMeasure = new PathMeasure();
//        forceClosed 为 false 测量的是当前 Path 状态的长度， forceClosed 为 true，则不论Path是否闭合测量的都是 Path 的闭合长度。
        mPathMeasure.setPath(mPath, true);
        mLength = mPathMeasure.getLength();

        mDst = new Path();

        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(2000);
//        infinite 无限的，即循环。
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawCoordinate(canvas);

        canvas.translate(mWidth / 2, mHeight / 2);

        // 需要重置，否则受上次影响，因为getSegment方法是添加而非替换
        mDst.reset();
        // 4.4版本以及之前的版本，需要使用这行代码，否则getSegment无效果
        // 导致这个原因是 硬件加速问题导致
        mDst.lineTo(0, 0);

        float stop = mLength * mAnimatorValue;
//       sca: 当 0 <mAnimatorValue< 0.5也就是，上半圆时，star一直是 0，stop 一直在增加。当mAnimatorValue>0.5时，进入下半圆，stop在增加， start在减少
        float start = (float) (stop - ((0.5 - Math.abs(mAnimatorValue - 0.5)) * mLength));
//        sca: 从  start, stop 截取的片段，添加到mDst，  然后，绘制mDst
        mPathMeasure.getSegment(start, stop, mDst, true);

        canvas.drawPath(mDst, mPaint);
    }
}