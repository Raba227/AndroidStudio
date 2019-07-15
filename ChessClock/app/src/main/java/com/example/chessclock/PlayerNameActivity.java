package com.example.chessclock;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class PlayerNameActivity extends AppCompatActivity {
    private int playerNumber;
    private long countNumber, secNumber;
    private ArrayList<EditText> playerName = new ArrayList<>();
    private boolean CountUp;
    private Intent intent;

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
        playerNumber = getIntent().getIntExtra("playerNumber", 0);

        TextView textView = new TextView(this);
        textView.setText("プレイヤー名決定");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        textView.setTextColor(Color.rgb(0x0, 0x0, 0x0));
        textView.setLayoutParams(textLayoutParams);
        layout.addView(textView);
        for(int player = 0; player < playerNumber; player++) {
            playerName.add(new EditText(this));
            playerName.get(player).setText("Player " + Integer.toString(player + 1), TextView.BufferType.NORMAL);
            playerName.get(player).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            playerName.get(player).setTextColor(Color.rgb(0x0, 0x0, 0x0));
            playerName.get(player).setLayoutParams(textLayoutParams);
            layout.addView(playerName.get(player));
        }
        Button button = new Button(this);
        button.setText("Play");
        button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        button.setLayoutParams(textLayoutParams);
        layout.addView(button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountUp = getIntent().getBooleanExtra("CountUp",false);
                if (CountUp) {
                    intent = new Intent(getApplicationContext(), CountUpActivity.class);
                } else {
                    intent = new Intent(getApplicationContext(), MainActivity.class);
                    countNumber = getIntent().getLongExtra("countNumber", 0);
                    secNumber = getIntent().getLongExtra("secNumber", 0);
                    intent.putExtra("countNumber", countNumber);
                    intent.putExtra("secNumber", secNumber);
                }
                for (int player = 0; player < playerNumber; player++) {
                    intent.putExtra("playerName" + Integer.toString(player), playerName.get(player).getText().toString());
                }
                intent.putExtra("playerNumber", playerNumber);
                startActivity(intent);
            }
        });
    }
}
