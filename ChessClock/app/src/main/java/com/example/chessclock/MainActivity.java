package com.example.chessclock;

import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    // フィールド準備
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss.SS", Locale.JAPAN);
    private final long interval = 10;
    private int active_number, player_number;
    private long InitialCountNumber, sec;
    private ArrayList<Long> countNum = new ArrayList<>();
    private ArrayList<CountDown> countDown = new ArrayList<>();
    private ArrayList<TextView> timerText = new ArrayList<>();
    private ArrayList<Boolean> active = new ArrayList<>();
    private ArrayList<Boolean> finish_flag = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // インスタンス初期化
        player_number = 2;
        InitialCountNumber = 60000 * getIntent().getLongExtra("countNumber", 0);
        sec = 1000 * getIntent().getLongExtra("secNumber", 0);
        TextView textView = findViewById(R.id.timer1);
        timerText.add(textView);
        textView = findViewById(R.id.timer2);
        timerText.add(textView);
        for (int player = 0; player < player_number; player++) {
            timerText.get(player).setText(dateFormat.format(InitialCountNumber));
            countDown.add(new CountDown(InitialCountNumber, interval));
            countNum.add(InitialCountNumber);
            active.add(false);
            finish_flag.add(false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        // タッチ操作のみで対局時計の進行を制御
        switch (motionEvent.getAction()) {
            // タッチ一回目は一番上をスタート、二回目以降動いているタイマーと止め、次の手番をスタート
            case MotionEvent.ACTION_DOWN:
                if (active.get(0) == false && active.get(1) == false) {
                    active.set(0, true);
                    active_number = 0;
                    countDown.get(0).start();
                } else {
                    countDown.get(active_number).cancel();

                    // 持ち時間を使い切った場合秒読みスタート
                    if (finish_flag.get(active_number)) {
                        countNum.set(active_number, sec);
                    }

                    countDown.set(active_number, new CountDown(countNum.get(active_number), interval));
                    active.set(active_number, false);
                    active_number++;
                    if (active_number == player_number) {
                        active_number = 0;
                    }
                    active.set(active_number, true);
                    countDown.get(active_number).start();
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
        @Override
        public void onFinish() {
            countDown.set(active_number, new CountDown(sec, interval));
            countDown.get(active_number).start();
            finish_flag.set(active_number, true);
        }

        // countdoowntimerの仕様上一時停止するにはmillisUntilFinishedを保存して新たにインスタンスを生成する必要がある
        @Override
        public void onTick(long millisUntilFinished) {
            timerText.get(active_number).setText(dateFormat.format(millisUntilFinished));
            countNum.set(active_number, millisUntilFinished);
        }
    }
}
