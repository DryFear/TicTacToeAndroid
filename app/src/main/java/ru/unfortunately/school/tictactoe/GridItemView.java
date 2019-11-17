package ru.unfortunately.school.tictactoe;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;

import androidx.annotation.Nullable;

public class GridItemView extends View {

    private static final String TAG = "TEST";

    public static final int X_SYMBOL = 1;
    public static final int O_SYMBOL = 2;
    public static final int EMPTY_SYMBOL = 0;

    private final int REQUESTED_WIDTH = getRequestedWidthOfHeight();

    private final int REQUESTED_HEIGHT = getRequestedWidthOfHeight();
    private final int DEFAULT_STROKE_WIDTH = 8;
    private final int DEFAULT_X_COLOR = Color.BLUE;
    private final int DEFAULT_O_COLOR = Color.RED;

    private float mCurrentSweepAngle = 360f;
    private float mCurrentFirstStickRange = 0f;
    private float mCurrentSecondStickRange = 0f;
    private final long ANIMATION_DURATION = 500;


    private boolean mIsItemWinner;

    private boolean mIsActive = true;
    public static boolean sIsXnow;
    private int mSymbol = EMPTY_SYMBOL;

    private Paint mPaint = new Paint();
    private Paint mWinnerPaint = new Paint();
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
        init();
    }


    public GridItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        final int size = Math.max(REQUESTED_HEIGHT, REQUESTED_WIDTH);
        final int width = resolveSize(size, widthMeasureSpec);
        final int height = resolveSize(size, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private void init() {
        setSaveEnabled(true);
        sIsXnow = true;
        mIsItemWinner = false;
        mPaint.setStrokeWidth(DEFAULT_STROKE_WIDTH);
        mPaint.setStyle(Style.STROKE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mSymbol == X_SYMBOL) {
            mPaint.setColor(DEFAULT_X_COLOR);
            canvas.drawLine(0, 0, mCurrentFirstStickRange, mCurrentFirstStickRange, mPaint);
            canvas.drawLine(REQUESTED_WIDTH, 0, REQUESTED_WIDTH-mCurrentSecondStickRange, mCurrentSecondStickRange, mPaint);
        }
        if(mSymbol == O_SYMBOL){
            canvas.translate(DEFAULT_STROKE_WIDTH/2f, DEFAULT_STROKE_WIDTH/2f);
            mPaint.setColor(DEFAULT_O_COLOR);
            canvas.drawArc(mBoundsRect, 0, mCurrentSweepAngle, false, mPaint);
        }
        if(mIsItemWinner){
            canvas.drawRect(0, 0, getWidth(), getHeight(), mWinnerPaint);
        }
        ((InputFieldView) getParent()).invalidate();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        if(mIsActive && action == MotionEvent.ACTION_DOWN){
            if(sIsXnow){
                startDrawX();
            }
            else{
                startDrawO();
            }
            switchSymbol();
            setActive(false);
            invalidate();
            return false;
        }
        return false;

    }


    private void startDrawX() {
        mSymbol = X_SYMBOL;
        final ValueAnimator animator = ValueAnimator.ofFloat(0, REQUESTED_HEIGHT);
        final ValueAnimator secondAnimator = ValueAnimator.ofFloat(0, REQUESTED_HEIGHT);
        setUpAnimatorForX(animator);
        setUpAnimatorForX(secondAnimator);
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentFirstStickRange = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        secondAnimator.setStartDelay(ANIMATION_DURATION/2);
        secondAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentSecondStickRange = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
        secondAnimator.start();
    }

    private void setUpAnimatorForX(ValueAnimator animator){
        animator.setDuration(ANIMATION_DURATION / 2);
        animator.setInterpolator(new AccelerateInterpolator());
    }

    private void startDrawO() {
        mSymbol = O_SYMBOL;
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 360f);
        animator.setDuration(ANIMATION_DURATION);
        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrentSweepAngle = (Float) animation.getAnimatedValue();
                invalidate();
            }
        });
        animator.start();
    }

    public void setActive(boolean value){
        mIsActive = value;
    }

    public boolean getActive(){
        return mIsActive;
    }

    public void makeItemWinner(){
        mWinnerPaint.setStyle(Style.FILL_AND_STROKE);
        mWinnerPaint.setColor(getResources().getColor(R.color.colorGreen));
        mIsItemWinner = true;
        ValueAnimator animator = ValueAnimator.ofInt(0, 180);
        animator.setDuration(ANIMATION_DURATION);
        animator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mWinnerPaint.setAlpha((int) animation.getAnimatedValue());
                invalidate();
            }
        });
        animator.start();
    }

    public void reset(){
        mSymbol = EMPTY_SYMBOL;
        sIsXnow = true;
        mIsActive = true;
        mIsItemWinner = false;
        mCurrentSecondStickRange = 0;
        mCurrentFirstStickRange = 0;
        invalidate();
    }

    public int getSymbol(){
        return mSymbol;
    }

    public void setSymbol(int symbol){
        mSymbol = symbol;
    }

    public boolean isItemWinner() {
        return mIsItemWinner;
    }

    public void setItemWinner(boolean itemWinner) {
        mIsItemWinner = itemWinner;
        if(itemWinner){
            makeItemWinner();
        }
    }

    public void switchSymbol(){
        sIsXnow = !sIsXnow;
    }

    public void restartAnimation(){
        if(mSymbol == X_SYMBOL){
            startDrawX();
        }
        if(mSymbol == O_SYMBOL){
            startDrawO();
        }
    }

    private int getRequestedWidthOfHeight() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        if(wm == null){
            return 100;
        }
//        ViewGroup root = findViewById(R.id.root);
//        Log.i(TAG, "getRequestedWidthOfHeight: " + root.getMeasuredHeight());
        Display display = wm.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x > size.y ? size.y/7 : size.x/7;
//        ViewGroup grid = (ViewGroup)getParent();
//        int width = ((ViewGroup)grid.getParent()).getMeasuredWidth();
//        int height = ((ViewGroup)grid.getParent()).getMeasuredHeight();
//        return width > height ? height/4 : width/4;
    }

}
