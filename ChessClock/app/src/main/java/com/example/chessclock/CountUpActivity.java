package com.example.chessclock;

import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class CountUpActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    private TextView timerText;
    private int countTimer, period;
    private int active_number, player_number, count;
    private SoundPlayer soundPlayer;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss.S", Locale.JAPAN);
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            countTimer ++;
            timerText.setText(dateFormat.format(countTimer * period));
            handler.postDelayed(this, period);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // レイアイウト生成
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setGravity(Gravity.CENTER);
        layout.setBackgroundColor(Color.LTGRAY);
        setContentView(layout);

        // マージン計算
        float scale = getResources().getDisplayMetrics().density;
        int margins = (int)(1 * scale);

        // TextViewのレイアウト指定
        LinearLayout.LayoutParams textLayoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        textLayoutParams.setMargins(margins, margins, margins, margins);

        // プレイヤー人数に応じてインスタンス初期化
        soundPlayer = new SoundPlayer(this);
        player_number = getIntent().getIntExtra("playerNumber", 0);
        count = -1;
        countTimer = 0;
        period = 100;
        timerText = new TextView(this);
        timerText.setText(dateFormat.format(0));
        timerText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 50);
        timerText.setTextColor(Color.rgb(0x0, 0x0, 0x0));
        timerText.setLayoutParams(textLayoutParams);
        layout.addView(timerText);
        Button button = new Button(this);
        button.setText("一時停止/再開");
        button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        button.setLayoutParams(textLayoutParams);
        layout.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.post(runnable);
            }
        });
    }
}
