package com.example.burns.batlog;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class CycleActivity extends AppCompatActivity {
    private TextView mNameView;
    private TextView mChargedView;
    private TextView mDischargedView;
    private TextView mCapacityView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle);

        mNameView = (TextView) findViewById(R.id.id_cycle_name);
        mChargedView = (TextView) findViewById(R.id.id_cycle_charged);
        mDischargedView = (TextView) findViewById(R.id.id_cycle_discharge);
        mCapacityView = (TextView) findViewById(R.id.id_cycle_total);
        String id = getIntent().getStringExtra("KEY-ID");
        Log.d("KEY-ID", "banan"+id);

        mNameView.setText("testid"+id);

        Button okButton = (Button) findViewById(R.id.id_cycle_okButton);
        okButton.setTag(id);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = (String)view.getTag();

                Battery tempBat = new Battery(id);

                String charge = isNotEmpty(mChargedView.getText().toString(), "-");
                String discharge = isNotEmpty(mDischargedView.getText().toString(), "-");
                String capacity = isNotEmpty(mCapacityView.getText().toString(), "-");

                tempBat.batteryAddCycle(charge, discharge, capacity);

                finish();
            }
        });
    }

    private String isNotEmpty(String inString, String replace) {
        if(inString.equals("")) {
            return replace;
        } else {
            return inString;
        }

    }
}