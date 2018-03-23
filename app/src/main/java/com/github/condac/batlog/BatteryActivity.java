package com.github.condac.batlog;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BatteryActivity extends AppCompatActivity {

    private TextView mid;
    private TextView mah;
    private TextView name;
    private TextView volt;
    private TextView group;
    private TextView cycles;
    StuffPacker stuffPack;
    private TextView batteryView_lastCap;
    private TextView batteryView_capPercent;
    private TextView batteryView_resistance;
    private TextView batteryView_wh;
    private TextView batteryView_manu;
    private TextView batteryView_model;
    private TextView batteryView_date;
    private TextView batteryView_cyclesEst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_battery);
        stuffPack = StuffPacker.getInstance();


        String id = getIntent().getStringExtra("KEY-ID");
        int batIndex = stuffPack.getBatteryIndexById(id);

        mid = (TextView) findViewById(R.id.batteryView_id);
        mah = (TextView) findViewById(R.id.batteryView_mah);
        name = (TextView) findViewById(R.id.batteryView_name);
        volt = (TextView) findViewById(R.id.batteryView_volt);
        cycles = (TextView) findViewById(R.id.batteryView_cycles);
        group = (TextView) findViewById(R.id.batteryView_group);
        batteryView_lastCap = (TextView) findViewById(R.id.batteryView_lastCap);
        batteryView_capPercent = (TextView) findViewById(R.id.batteryView_capPercent);
        batteryView_resistance = (TextView) findViewById(R.id.batteryView_resistance);
        batteryView_wh = (TextView) findViewById(R.id.batteryView_wh);
        batteryView_manu = (TextView) findViewById(R.id.batteryView_manu);
        batteryView_model = (TextView) findViewById(R.id.batteryView_model);
        batteryView_date = (TextView) findViewById(R.id.batteryView_date);
        batteryView_cyclesEst = (TextView) findViewById(R.id.batteryView_cyclesEst);

        Button button= (Button) findViewById(R.id.batterybutton_plus);
        button.setTag(id);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String i = (String)view.getTag();
              //  view = inflter.inflate(R.layout.activity_listview, null);
                // click handling code

                Intent intent = new Intent(BatteryActivity.this, CycleActivity.class);
                intent.putExtra("KEY-ID", i);

                startActivity(intent);

            }
        });

        Button buttonEdit= (Button) findViewById(R.id.batterybutton_edit);
        buttonEdit.setTag(id);
        buttonEdit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String i = (String)view.getTag();
                //  view = inflter.inflate(R.layout.activity_listview, null);
                // click handling code

                Intent intent = new Intent(BatteryActivity.this, EditBatActivity.class);
                intent.putExtra("KEY-ID", i);

                startActivity(intent);

            }
        });

        //String str = getBatteryString(batteryList.get(i));

        //Battery bat = new Battery(batteryList.get(i));
        //Log.d("CustomAddapter", "BatINdex:" + i);
        //int batIndex = i;//stuffPack.getBatteryIndexById(i+"");

        mid.setText(stuffPack.batteryList.get(batIndex).getIdString());
        mah.setText(stuffPack.batteryList.get(batIndex).getmAh()+"mAh");
        name.setText(stuffPack.batteryList.get(batIndex).getName());
        volt.setText(stuffPack.batteryList.get(batIndex).getVolt()+"V");
        cycles.setText(stuffPack.batteryList.get(batIndex).getCycles()+"cycles");
        group.setText(stuffPack.batGroup.getNameFromId(stuffPack.batteryList.get(batIndex).getGroupInt()));
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        stuffPack = StuffPacker.getInstance();


        String id = getIntent().getStringExtra("KEY-ID");
        int batIndex = stuffPack.getBatteryIndexById(id);

        stuffPack.batteryList.get(batIndex).readCsvFile();

        mid.setText(stuffPack.batteryList.get(batIndex).getIdString());
        mah.setText(stuffPack.batteryList.get(batIndex).getmAh()+"mAh");
        name.setText(stuffPack.batteryList.get(batIndex).getName());
        volt.setText(stuffPack.batteryList.get(batIndex).getVolt()+"V");
        cycles.setText(stuffPack.batteryList.get(batIndex).getCycles()+"cycles");
        group.setText(stuffPack.batGroup.getNameFromId(stuffPack.batteryList.get(batIndex).getGroupInt()));

        batteryView_model.setText(stuffPack.batteryList.get(batIndex).getModel()+"");
        batteryView_manu.setText(stuffPack.batteryList.get(batIndex).getManufacturer()+"");
        batteryView_date.setText(stuffPack.batteryList.get(batIndex).getDate()+"");

        batteryView_cyclesEst.setText(stuffPack.batteryList.get(batIndex).getTotalUsage()+"");

        batteryView_resistance.setText(stuffPack.batteryList.get(batIndex).getLatestResistance()+"");

        batteryView_lastCap.setText(stuffPack.batteryList.get(batIndex).getLatestCap()+"mAh");

        float mah = Float.parseFloat(stuffPack.batteryList.get(batIndex).getmAh());
        float lastCap = (float)stuffPack.batteryList.get(batIndex).getLatestCap();
        float percent = lastCap / mah ;
        batteryView_capPercent.setText(String.format("%.0f%%",percent*100));

        float volt = Float.parseFloat(stuffPack.batteryList.get(batIndex).getVolt());
        float wh = mah * volt / 1000;
        float whLast = lastCap * volt / 1000;
        String outputstring = String.format("%.2f", wh) +" ("+ String.format("%.2f", whLast) +")";
        batteryView_wh.setText(outputstring);
    }
}
