package ru.unfortunately.school.tictactoe;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.GridLayout;

import androidx.annotation.Nullable;

public class InputFieldView extends GridLayout {

    private final int DEFAULT_BORDER_STROKE = 12;
    private final int DEFAULT_BORDER_COLOR = Color.BLACK;

    private Paint mPaint = new Paint();
    private GridItemView[][] items;
    private IMainActivity mWinListener;

    public InputFieldView(Context context) {
        this(context, null);
    }

    public InputFieldView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public InputFieldView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public InputFieldView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(){
        mPaint.setStyle(Style.STROKE);
        mPaint.setColor(DEFAULT_BORDER_COLOR);
        mPaint.setStrokeWidth(DEFAULT_BORDER_STROKE);
    }
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//        int offsetY = 0;
//        int columnCount = getColumnCount();
//        for (int i = 0; i < getChildCount(); i++) {
//            int offsetX = 0;
//            int tempOffsetY = offsetY;
//            for (int j = 0; j < columnCount; j++) {
//                final View child = getChildAt(i);
//                final MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
//                final int l = left + offsetX + DEFAULT_BORDER_STROKE + layoutParams.leftMargin;
//                final int t = top + offsetY + DEFAULT_BORDER_STROKE + layoutParams.topMargin;
//                final int r = child.getMeasuredWidth() + l;
//                final int b = child.getMeasuredHeight() + t;
//                offsetX += r + layoutParams.rightMargin;
//                tempOffsetY = Math.max(tempOffsetY, b + layoutParams.bottomMargin);
//                child.layout(l, t, r, b);
//            }
//            offsetY = tempOffsetY;
//        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DrawBorders(canvas);
    }

    private void DrawBorders(Canvas canvas) {
        int columnCount = getColumnCount();
        int width = getWidth();
        int height = getHeight();
        int offset = 0;
        for (int i = 1; i <= columnCount; i++) {
            canvas.drawLine(offset, 0, offset, height, mPaint);
            View child = getChildAt(i);
            final MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int leftMargin = layoutParams.leftMargin;
            int rightMargin = layoutParams.rightMargin;
            offset += child.getMeasuredWidth()+leftMargin+rightMargin;
        }
        offset = 0;
        for (int i = 1; i <= getChildCount()/columnCount; i++) {
            canvas.drawLine(0, offset, width, offset, mPaint);
            View child = getChildAt(i);
            final MarginLayoutParams layoutParams = (MarginLayoutParams) child.getLayoutParams();
            int topMargin = layoutParams.topMargin;
            int bottomMargin = layoutParams.bottomMargin;
            offset += child.getMeasuredWidth()+topMargin+bottomMargin;
        }
        mPaint.setStrokeWidth(DEFAULT_BORDER_STROKE*1.5f);
        canvas.drawLine(0, 0, 0, height, mPaint);
        canvas.drawLine(0, 0, width, 0, mPaint);
        canvas.drawLine(width, 0, width, height, mPaint);
        canvas.drawLine(0, height, width, height, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) checkWinner();
        return super.onInterceptTouchEvent(event);
    }


    private void checkWinner(){
        items = new GridItemView[getColumnCount()][getChildCount()/getColumnCount()];
        int a = getChildCount();
        for (int i = 0; i < a; i++) {
            items[i/getColumnCount()][i%getColumnCount()] = (GridItemView) getChildAt(i);
        }
        try {
            int result = WinnerLogic.checkWinner(items);
            //if(result != -1){
                mWinListener.win(result);
            //}
        }catch (Exception e){e.printStackTrace();
            throw  new RuntimeException();}
    }

    public void setWinListener(IMainActivity listener){
        mWinListener = listener;
    }

    //
//    @Override
//    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
//        return super.checkLayoutParams(p);
//    }
//
//    @Override
//    protected GridLayout.LayoutParams generateDefaultLayoutParams() {
//        return super.generateDefaultLayoutParams();
//    }
//
//    @Override
//    public GridLayout.LayoutParams generateLayoutParams(AttributeSet attrs) {
//        return super.generateLayoutParams(attrs);
//    }
//
//    @Override
//    protected GridLayout.LayoutParams generateLayoutParams(ViewGroup.LayoutParams lp) {
//        return new LayoutParams(lp);
//    }
//
//    public static class LayoutParams extends MarginLayoutParams {
//
//        public LayoutParams(Context c, AttributeSet attrs) {
//            super(c, attrs);
//        }
//
//        public LayoutParams(int width, int height) {
//            super(width, height);
//        }
//
//        public LayoutParams(MarginLayoutParams source) {
//            super(source);
//        }
//
//        public LayoutParams(ViewGroup.LayoutParams source) {
//            super(source);
//        }
//    }
}
