package com.example.chessclock;

import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // フィールド準備
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss.SS", Locale.JAPAN);
    private final long interval = 10;
    private int active_number, player_number, count;
    private long initiaCountNumber, sec;
    private ArrayList<Long> countNum = new ArrayList<>();
    private ArrayList<CountDown> countDown = new ArrayList<>();
    private ArrayList<TextView> timerText = new ArrayList<>();
    private ArrayList<TextView> playerName = new ArrayList<>();
    private ArrayList<Boolean> active = new ArrayList<>();
    private ArrayList<Boolean> finish_flag = new ArrayList<>();
    private ArrayList<Boolean> lose_flag = new ArrayList<>();
    private SoundPlayer soundPlayer;

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
        initiaCountNumber = 60000 * getIntent().getLongExtra("countNumber", 0);
        sec = 1000 * getIntent().getLongExtra("secNumber", 0);
        count = -1;
        for(int player = 0; player < player_number; player++) {
            playerName.add(new TextView(this));
            playerName.get(player).setText(getIntent().getStringExtra("playerName" + Integer.toString(player)));
            playerName.get(player).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
            playerName.get(player).setTextColor(Color.rgb(0x0, 0x0, 0x0));
            playerName.get(player).setLayoutParams(textLayoutParams);
            layout.addView(playerName.get(player));
            timerText.add(new TextView(this));
            timerText.get(player).setText(dateFormat.format(initiaCountNumber));
            timerText.get(player).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 50);
            timerText.get(player).setTextColor(Color.rgb(0x0, 0x0, 0x0));
            timerText.get(player).setLayoutParams(textLayoutParams);
            layout.addView(timerText.get(player));
            countDown.add(new CountDown(initiaCountNumber, interval));
            countNum.add(initiaCountNumber);
            active.add(false);
            finish_flag.add(false);
            lose_flag.add(false);
        }
        Button button = new Button(this);
        button.setText("一時停止/再開");
        button.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30);
        button.setLayoutParams(textLayoutParams);
        layout.addView(button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (active.get(active_number)) {
                    active.set(active_number, false);
                    countDown.get(active_number).cancel();
                    timerText.get(active_number).setTextColor(Color.rgb(0x0, 0x0, 0x0));
                    countDown.set(active_number, new CountDown(countNum.get(active_number), interval));
                    soundPlayer.playChangeSound();
                } else {
                    active.set(active_number, true);
                    timerText.get(active_number).setTextColor(Color.parseColor("red"));
                    countDown.get(active_number).start();
                    soundPlayer.playChangeSound();
                }
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        // タッチ操作のみで対局時計の進行を制御
        switch (motionEvent.getAction()) {

            // タッチ一回目は一番上をスタート、二回目以降動いているタイマーと止め、次の手番をスタート
            case MotionEvent.ACTION_DOWN:
                if (count == -1) {
                    active.set(0, true);
                    active_number = 0;
                    timerText.get(0).setTextColor(Color.parseColor("red"));
                    countDown.get(0).start();
                    soundPlayer.playChangeSound();
                    count = 0;
                } else if (count == player_number - 1) {
                    // 勝敗が決したら停止
                    break;
                } else {
                    countDown.get(active_number).cancel();
                    timerText.get(active_number).setTextColor(Color.rgb(0x0, 0x0, 0x0));
                    // 持ち時間を使い切った場合秒読みスタート
                    if (finish_flag.get(active_number)) {
                        countNum.set(active_number, sec);
                    }
                    // 残り時間を受け取って新たにインスタンス生成
                    countDown.set(active_number, new CountDown(countNum.get(active_number), interval));
                    active.set(active_number, false);
                    active_number++;
                    if (active_number == player_number) {
                        active_number = 0;
                    }
                    // loseしたplayerは飛ばす
                    while (lose_flag.get(active_number)) {
                        active_number++;
                        if (active_number == player_number) {
                            active_number = 0;
                        }
                    }
                    active.set(active_number, true);
                    timerText.get(active_number).setTextColor(Color.parseColor("red"));
                    countDown.get(active_number).start();
                    soundPlayer.playChangeSound();
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return true;
    }

    class CountDown extends CountDownTimer {
        CountDown(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        // 持ち時間を使い切ったプレイヤーはそのまま秒読みフェーズに進行
        // 秒読みを使い切るとlose
        @Override
        public void onFinish() {
            if (finish_flag.get(active_number) == true || sec == 0) {
                soundPlayer.playFinishSound();
                timerText.get(active_number).setText("You Lose !!");
                lose_flag.set(active_number, true);
                count++;
                // 最後の一人が勝者
                if (count == player_number - 1) {
                    timerText.get(active_number).setTextColor(Color.rgb(0x0, 0x0, 0x0));
                    timerText.get(lose_flag.indexOf(false)).setText("You Win !!");
                    timerText.get(lose_flag.indexOf(false)).setTextColor(Color.parseColor("blue"));
                }
            } else {
                countDown.set(active_number, new CountDown(sec, interval));
                countDown.get(active_number).start();
                finish_flag.set(active_number, true);
            }
        }

        // countdoowntimerの仕様上一時停止するにはmillisUntilFinishedを保存して新たにインスタンスを生成する必要がある
        @Override
        public void onTick(long millisUntilFinished) {
            timerText.get(active_number).setText(dateFormat.format(millisUntilFinished));
            countNum.set(active_number, millisUntilFinished);
        }
    }
}
