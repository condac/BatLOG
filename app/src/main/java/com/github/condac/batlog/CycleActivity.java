package com.github.condac.batlog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CycleActivity extends AppCompatActivity {
    private TextView mNameView;
    private TextView mChargedView;
    //private TextView mDischargedView;
    private TextView mCapacityView;
    StuffPacker stuffPack;
    private TextView mResistanceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle);

        Intent intent = getIntent();
        stuffPack = StuffPacker.getInstance();

        mNameView = (TextView) findViewById(R.id.id_cycle_name);
        mChargedView = (TextView) findViewById(R.id.id_cycle_charged);
        //mDischargedView = (TextView) findViewById(R.id.id_cycle_discharge);
        mCapacityView = (TextView) findViewById(R.id.id_cycle_total);
        mResistanceView = (TextView) findViewById(R.id.id_cycle_resistance);

        String id = getIntent().getStringExtra("KEY-ID");
        Log.d("KEY-ID", "banan"+id);


        int batIndex = stuffPack.getBatteryIndexById(id);

        mNameView.setText("ID:"+stuffPack.batteryList.get(batIndex).getIdString()+" Name:"+ stuffPack.batteryList.get(batIndex).getName());

        Button okButton = (Button) findViewById(R.id.id_cycle_okButton);
        okButton.setTag(id);

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stuffPack = StuffPacker.getInstance();
                String id = (String)view.getTag();
                int batIndex = stuffPack.getBatteryIndexById(id);


                String charge = isNotEmpty(mChargedView.getText().toString(), "0");
                String discharge = "0";
                String capacity = isNotEmpty(mCapacityView.getText().toString(), "0");
                String resistance = isNotEmpty(mResistanceView.getText().toString(), "0");

                stuffPack.batteryList.get(batIndex).batteryAddCycle(charge, discharge, capacity, resistance);

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
