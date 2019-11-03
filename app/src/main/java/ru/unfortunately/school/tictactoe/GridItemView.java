package ru.unfortunately.school.tictactoe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class GridItemView extends View {

    private static final int X_SYMBOL = 0;
    private static final int O_SYMBOL = 1;

    private final int REQUESTED_WIDTH = 100;
    private final int REQUESTED_HEIGHT = 100;
    private final int DEFAULT_STROKE_WIDTH = 8;
    private final int DEFAULT_X_COLOR = Color.BLUE;
    private final int DEFAULT_O_COLOR = Color.RED;


    private boolean isActive = true;
    private static boolean mIsXnow;
    private int mSymbol = -1;

    private Paint mPaint = new Paint();
    private RectF mBoundsRect = new RectF(0, 0, REQUESTED_WIDTH - DEFAULT_STROKE_WIDTH, REQUESTED_HEIGHT - DEFAULT_STROKE_WIDTH);

    public GridItemView(Context context) {
        this(context, null);
    }

    public GridItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GridItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GridItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int size = Math.max(REQUESTED_HEIGHT, REQUESTED_WIDTH);
        final int width = resolveSize(size, widthMeasureSpec);
        final int height = resolveSize(size, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private void init() {
        mIsXnow = true;
        mPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        mPaint.setStyle(Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mSymbol == X_SYMBOL) {
            mPaint.setColor(DEFAULT_X_COLOR);
            canvas.drawLine(0, 0, REQUESTED_WIDTH, REQUESTED_HEIGHT, mPaint);
            canvas.drawLine(REQUESTED_WIDTH, 0, 0, REQUESTED_HEIGHT, mPaint);
        }
        if(mSymbol == O_SYMBOL){
            canvas.translate(DEFAULT_STROKE_WIDTH/2f, DEFAULT_STROKE_WIDTH/2f);
            mPaint.setColor(DEFAULT_O_COLOR);
            canvas.drawArc(mBoundsRect, 0, 360, false, mPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(isActive && action == MotionEvent.ACTION_DOWN){
            if(mIsXnow){
                mSymbol = X_SYMBOL;
            }
            else{
                mSymbol = O_SYMBOL;
            }
            switchSymbol();
            setActive(false);
            invalidate();
            return false;
        }
        return false;
    }

    public void setActive(boolean value){
        isActive = value;
    }

    public boolean getActive(){
        return isActive;
    }

    public int getSymbol(){
        return mSymbol;
    }

    public void switchSymbol(){
        mIsXnow = !mIsXnow;
    }


}
