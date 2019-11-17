package ru.unfortunately.school.tictactoe;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.GridLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;

public class InputFieldView extends GridLayout{

    private static final String TAG = "TEST";

    private final long ANIMATION_DURATION = 1000;

    private final int DEFAULT_BORDER_STROKE = 12;
    private final int DEFAULT_BORDER_COLOR = Color.BLACK;

    private Paint mPaint = new Paint();
    private GridItemView[][] mItems;
    private List<GridItemView> mItemViews = new ArrayList<>();
    private IMainActivity mWinListener;

    private float widthAnimation = 0;
    private float heightAnimation = 0;

    private boolean mWinFlag = false;

    public InputFieldView(Context context) {
        this(context, null);
    }

    public InputFieldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InputFieldView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public InputFieldView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init(){
        setSaveEnabled(true);
        for (int i = 0; i < 9; i++) {
            GridItemView itemView = new GridItemView(getContext());
            MarginLayoutParams params = new MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            int margin = getResources().getDimensionPixelSize(R.dimen.default_margin);
            params.setMargins(margin, margin, margin, margin);
            itemView.setLayoutParams(params);
            mItemViews.add(itemView);
            addView(itemView);
        }
        mPaint.setStyle(Style.STROKE);
        mPaint.setColor(DEFAULT_BORDER_COLOR);
        mPaint.setStrokeWidth(DEFAULT_BORDER_STROKE);
    }

    public void setWinListener(IMainActivity listener){
        mWinListener = listener;
    }

    public void restartGame(){
        for (int i = 0; i < getChildCount(); i++) {
            GridItemView child = ((GridItemView) getChildAt(i));
            child.reset();
        }
        mWinFlag = false;
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setUpAnimations();
    }

    @Nullable
    @Override
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mCurrentWinState = mWinFlag ? 1 : 0;
        for (int i = 0; i < mItemViews.size(); i++) {
            savedState.mSymbols[i] = mItemViews.get(i).getSymbol();
        }
        for (int i = 0; i < mItemViews.size(); i++) {
            savedState.mActives[i] = mItemViews.get(i).getActive() ? 1 : 0;
        }
        for (int i = 0; i < mItemViews.size(); i++) {
            savedState.mWinners[i] = mItemViews.get(i).isItemWinner() ? 1 : 0;
        }
        savedState.mIsXnow = GridItemView.sIsXnow ? 1 : 0;
        return savedState;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        super.onRestoreInstanceState(state);
        SavedState savedState = (SavedState) state;
        mWinFlag = savedState.mCurrentWinState == 1;
        for (int i = 0; i < mItemViews.size(); i++) {
            mItemViews.get(i).setSymbol(savedState.mSymbols[i]);
        }
        for (int i = 0; i < mItemViews.size(); i++) {
            mItemViews.get(i).setActive(savedState.mActives[i] == 1);
        }
        for (int i = 0; i < mItemViews.size(); i++) {
            mItemViews.get(i).setItemWinner(savedState.mWinners[i] == 1);
            mItemViews.get(i).restartAnimation();
        }
        GridItemView.sIsXnow = savedState.mIsXnow == 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawBorders(canvas);
//        canvas.drawLine(0, 0, getHeight(), getWidth(), mPaint);
//        Log.d("TEST", "onDraw() called in FIELD");
    }

    private void DrawBorders(Canvas canvas) {
        int columnCount = getColumnCount();
        int width = getWidth();
        int height = getHeight();
        int offset = 0;
        for (int i = 1; i <= columnCount; i++) {
            canvas.drawLine(offset, 0, offset, heightAnimation, mPaint);
            View child = getChildAt(i);
            final MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int leftMargin = layoutParams.leftMargin;
            int rightMargin = layoutParams.rightMargin;
            offset += child.getMeasuredWidth()+leftMargin+rightMargin;
        }
        offset = 0;
        for (int i = 1; i <= getChildCount()/columnCount; i++) {
            canvas.drawLine(0, offset, widthAnimation, offset, mPaint);
            View child = getChildAt(i);
            final MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int topMargin = layoutParams.topMargin;
            int bottomMargin = layoutParams.bottomMargin;
            offset += child.getMeasuredWidth()+topMargin+bottomMargin;
        }
        mPaint.setStrokeWidth(DEFAULT_BORDER_STROKE*1.5f);
        canvas.drawLine(0, 0, 0, heightAnimation, mPaint);
        canvas.drawLine(0, 0, widthAnimation, 0, mPaint);
        canvas.drawLine(width, 0, width, heightAnimation, mPaint);
        canvas.drawLine(0, height, widthAnimation, height, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN && !mWinFlag) checkWinner();
        return super.onInterceptTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return mWinFlag;
    }

    private void checkWinner(){
        mItems = new GridItemView[getColumnCount()][getChildCount()/getColumnCount()];
        int a = getChildCount();
        for (int i = 0; i < a; i++) {
            mItems[i/getColumnCount()][i%getColumnCount()] = (GridItemView) getChildAt(i);
        }
        int result = WinnerLogic.checkWinner(mItems);
        if(result != -1){
            mWinFlag = true;
            mWinListener.win(result);
        }

    }

    private void setUpAnimations() {
        ValueAnimator widthBorderAnimator = ValueAnimator.ofFloat(0, getWidth());
        ValueAnimator heightBorderAnimator = ValueAnimator.ofFloat(0, getHeight());
        widthBorderAnimator.setDuration(ANIMATION_DURATION/2);
        heightBorderAnimator.setStartDelay(ANIMATION_DURATION/2);
        heightBorderAnimator.setDuration(ANIMATION_DURATION/2);
        widthBorderAnimator.setInterpolator(new AccelerateInterpolator());
        heightBorderAnimator.setInterpolator(new AccelerateInterpolator());
        widthBorderAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                widthAnimation = (float) animation.getAnimatedValue();
                invalidate();
            }
        });
        heightBorderAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                heightAnimation = (float) animation.getAnimatedValue();
                invalidate();
                Log.i(TAG, "onAnimationUpdate: " + (float) animation.getAnimatedValue());
            }
        });
        widthBorderAnimator.start();
        heightBorderAnimator.start();
    }

    private static class SavedState extends BaseSavedState{

        private int[] mSymbols = new int[9];
        private int[] mActives = new int[9];
        private int[] mWinners = new int[9];
        private int mCurrentWinState;
        private int mIsXnow;

        public SavedState(Parcel source) {
            super(source);
            mCurrentWinState = source.readInt();
            for (int symbol : mSymbols) {
                symbol = source.readInt();
            }
            for (int active : mActives) {
                active = source.readInt();
            }
            for (int winner : mWinners) {
                winner = source.readInt();
            }
            mIsXnow = source.readInt();
        }


        public SavedState(Parcelable superState) {
            super(superState);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel source) {
                return new SavedState(source);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };

        public void writeToParcel(Parcel out, int flags){
            super.writeToParcel(out, flags);
            out.writeInt(mCurrentWinState);
            for (int symbol : mSymbols) {
                out.writeInt(symbol);
            }
            for (int active : mActives) {
                out.writeInt(active);
            }
            for (int winner: mWinners) {
                out.writeInt(winner);
            }
            out.writeInt(mIsXnow);
        }
    }

}
