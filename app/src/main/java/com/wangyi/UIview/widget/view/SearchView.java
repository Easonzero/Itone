package com.wangyi.UIview.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Created by eason on 5/20/16.
 */
    public class SearchView extends LinearLayout {
    private Paint mPaint;
    private int mPadding = 25;
    private float cstart,clast, cy, cr;
    private EditText edit;
    private RectF deleBt;
    private int base_background_color = Color.parseColor("#2D7E55");
    private int search_background_color = Color.parseColor("#26573A");

    public SearchView(Context context) {
        this(context, null);
    }

    public SearchView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLayout();
        initEdit(context);
    }

    private void initLayout() {
        deleBt = new RectF();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(4);
        this.setGravity(Gravity.CENTER_VERTICAL);
        setWillNotDraw(false);
        this.setPadding(2*mPadding,0,2*mPadding,0);
    }

    private void initParams(){
        cr = getWidth() / 60;
        cstart = mPadding + cr;
        clast = getWidth() - mPadding - cr;
        cy = getHeight() / 2;
    }

    private void initEdit(Context context){
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        edit = new EditText(context);
        edit.setLayoutParams(lp);
        this.addView(edit);
        edit.setTextColor(Color.WHITE);
        edit.setBackgroundColor(0x00000000);
        edit.setTextSize(15);
        edit.setMaxEms(20);
        edit.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    public String getText(){
        return edit.getText().toString();
    }

    public void setOnKeyListener(OnKeyListener listener){
        edit.setOnKeyListener(listener);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawBackground(canvas);
        drawSearchIcon(canvas);
        drawDeleBt(canvas);
    }

    private void drawBackground(Canvas canvas){
        mPaint.setColor(search_background_color);
        mPaint.setStyle(Paint.Style.FILL);
        Rect mRect = new Rect();
        getDrawingRect(mRect);
        RectF f = new RectF(mRect);
        canvas.drawRoundRect(f,15,15,mPaint);
    }

    private void drawSearchIcon(Canvas canvas){
        mPaint.setColor(base_background_color);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(cstart, cy-0.5f * cr, cr, mPaint);
        canvas.drawLine(cstart + 0.7f * cr, cy + 0.2f * cr, cstart + cr * 2,
                cy + cr * 1.5f, mPaint);
    }

    private void drawDeleBt(Canvas canvas){
        float btr = 1.5f*cr;

        mPaint.setColor(base_background_color);
        mPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(clast, cy, btr, mPaint);
        mPaint.setColor(search_background_color);
        mPaint.setStrokeWidth(3);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(clast - 0.5f * btr, cy + 0.5f * btr, clast + 0.5f * btr,
                cy - btr * 0.5f, mPaint);
        canvas.drawLine(clast - 0.5f * btr, cy - 0.5f * btr, clast + 0.5f * btr,
                cy + btr * 0.5f, mPaint);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        initParams();
        deleBt.set(clast-cr*1.5f,cy-cr*1.5f,clast+cr*1.5f,cy+cr*1.5f);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(deleBt.contains(ev.getX(),ev.getY()))
            dowithDeleBt();
        return super.dispatchTouchEvent(ev);
    }

    private void dowithDeleBt(){
        edit.setText("");
    }
}
