package com.example.chessclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class SelectActivity extends AppCompatActivity {
    private long countNumber;
    private RadioGroup times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        times = findViewById(R.id.times);
        Button button = findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int checkedId = times.getCheckedRadioButtonId();
                if (checkedId != -1) {
                    RadioButton rb = findViewById(checkedId);
                    countNumber = Long.parseLong(rb.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("countNumber", countNumber);
                    startActivity(intent);
                }
            }
        });
    }
}
