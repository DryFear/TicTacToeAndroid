package ru.unfortunately.school.tictactoe;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements IMainActivity{

    private final long ANIMATION_DURATION = 1000;

    private InputFieldView mField;
    private TextView mTextView;
    private View mWinPanel;

    private final String WINNER_VISIBILITY_KEY = "winner_visibility";
    private final String WINNER_TEXT = "winner_text";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mField = findViewById(R.id.input_field);
        mTextView = findViewById(R.id.winner_text);
        mWinPanel = findViewById(R.id.win_panel);
        mField.setWinListener(this);
        if(savedInstanceState != null){
            mWinPanel.setVisibility(savedInstanceState.getInt(WINNER_VISIBILITY_KEY));
            mTextView.setText(savedInstanceState.getString(WINNER_TEXT));
        }
        Log.d("TEST", "onCreate() called with: " + mField.getId());
        findViewById(R.id.btn_restart).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mField.restartGame();
                mWinPanel.setVisibility(View.GONE);
            }
        });
    }


    @Override
    public void win(int winner) {
        switch (winner){
            case GridItemView.X_SYMBOL:
                mTextView.setText(getString(R.string.text_X_win));
                break;
            case GridItemView.O_SYMBOL:
                mTextView.setText(getString(R.string.text_O_win));
                break;
            default:
                mTextView.setText(getString(R.string.text_draw));
        }
        ValueAnimator alphaAnimator = ValueAnimator.ofFloat(0f, 1f);
        alphaAnimator.setDuration(ANIMATION_DURATION);
        alphaAnimator.addUpdateListener(new AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mWinPanel.setAlpha((float) animation.getAnimatedValue());
            }
        });
        alphaAnimator.start();
        mWinPanel.setVisibility(View.VISIBLE);
    }


    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt(WINNER_VISIBILITY_KEY, mWinPanel.getVisibility());
        outState.putString(WINNER_TEXT, mTextView.getText().toString());

    }
}
