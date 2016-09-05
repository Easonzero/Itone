package com.wangyi.UIview.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by eason on 8/1/16.
 */
public class ArrowView extends View{
    public int LEFT = 0;
    public int RIGHT = 1;

    private int height;
    private int width;
    private int shadowLength = 2;
    private Paint mPaint;
    private Path path;
    private int Type = LEFT;

    public ArrowView(Context context) {
        super(context);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
        path = new Path();
    }

    public ArrowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        mPaint = new Paint();
        path = new Path();
    }

    public void setType(String type){
        if(type.equals("left")) Type = LEFT;
        else if(type.equals("right")) Type = RIGHT;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        path.reset();
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.LTGRAY);

        mPaint.setShadowLayer(5,shadowLength,shadowLength,Color.DKGRAY);

        int r = width/2-shadowLength,padding = 15;

        canvas.drawCircle(r,r,r,mPaint);

        mPaint.clearShadowLayer();
        mPaint.setColor(Color.DKGRAY);

        float dt = (float)(r*0.85);

        if(Type == LEFT){
            path.moveTo(r*3/2-padding/2,r-dt+padding);
            path.lineTo(r*3/2-padding/2,r+dt-padding);
            path.lineTo(padding*3/2,r);
        }else if(Type == RIGHT){
            path.moveTo(r/2+padding/2,r-dt+padding);
            path.lineTo(r/2+padding/2,r+dt-padding);
            path.lineTo(width-padding*3/2,r);
        }

        canvas.drawPath(path,mPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        height = View.MeasureSpec.getSize(heightMeasureSpec);
        width = View.MeasureSpec.getSize(widthMeasureSpec);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int e = event.getAction();
        switch(e){
            case MotionEvent.ACTION_DOWN:
                shadowLength = 4;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_OUTSIDE:
                shadowLength = 2;
                break;
        }
        this.invalidate();
        return super.onTouchEvent(event);
    }
}
