package com.example.chessclock;

import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundPlayer {
    private static SoundPool soundPool;
    private static int changeSound, finishSound;
    private AudioAttributes audioAttributes;

    public SoundPlayer(Context context) {
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        changeSound = soundPool.load(context, R.raw.change, 1);
        finishSound = soundPool.load(context, R.raw.finish, 1);
    }

    public void playChangeSound() {
        soundPool.play(changeSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

    public void playFinishSound() {
        soundPool.play(finishSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }
}
