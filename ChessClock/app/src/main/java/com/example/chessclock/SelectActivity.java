package com.example.chessclock;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class SelectActivity extends AppCompatActivity {
    private int playerNumber;
    private long countNumber, secNumber;
    private Spinner player_spinner, time_spinner, sec_spinner;
    private boolean CountUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);

        // spinnerにアイテムセット
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.player_number, R.layout.spinner_item);
        adapter1.setDropDownViewResource(R.layout.spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.time, R.layout.spinner_item);
        adapter2.setDropDownViewResource(R.layout.spinner_dropdown_item);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.sec, R.layout.spinner_item);
        adapter3.setDropDownViewResource(R.layout.spinner_dropdown_item);


        player_spinner = findViewById(R.id.player_number);
        player_spinner.setAdapter(adapter1);
        time_spinner = findViewById(R.id.time);
        time_spinner.setAdapter(adapter2);
        sec_spinner = findViewById(R.id.second);
        sec_spinner.setAdapter(adapter3);

        CompoundButton toggle = findViewById(R.id.toggle);
        toggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CountUp = true;
                } else {
                    CountUp = false;
                }
            }
        });

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerNumber = Integer.parseInt((String)player_spinner.getSelectedItem());
                countNumber = Long.parseLong((String)time_spinner.getSelectedItem());
                secNumber = Long.parseLong((String)sec_spinner.getSelectedItem());
                Intent intent = new Intent(getApplicationContext(), PlayerNameActivity.class);
                intent.putExtra("playerNumber", playerNumber);
                intent.putExtra("countNumber", countNumber);
                intent.putExtra("secNumber", secNumber);
                intent.putExtra("CountUp", CountUp);
                startActivity(intent);
            }
        });

    }
}
