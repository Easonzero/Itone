package com.wangyi.UIview.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by eason on 5/9/16.
 */
public class NumberView extends View{
    private String message = "";
    private int height;
    private int width;
    private Paint mPaint;

    public NumberView(Context context) {
        super(context);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
    }

    public NumberView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
    }

    public void setMessage(String msg){
        message = msg;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setFakeBoldText(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(width*4/11);

        int centerX = width/2,centerY = height*4/9;

        final float width_text = mPaint.measureText(message);

        canvas.drawText(message, centerX - width_text / 2,
                centerY + (mPaint.getTextSize() - mPaint.descent()) / 2,
                mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
    }
}
