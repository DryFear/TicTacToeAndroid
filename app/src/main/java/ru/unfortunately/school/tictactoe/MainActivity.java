package ru.unfortunately.school.tictactoe;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements IMainActivity{

    private InputFieldView mField;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mField = findViewById(R.id.input_field);
        mTextView = findViewById(R.id.winner_text);
        mField.setWinListener(this);
    }

    @Override
    public void win(int winner) {
        mTextView.setText(String.valueOf(winner));
    }
}
