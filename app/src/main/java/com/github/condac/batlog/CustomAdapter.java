package com.github.condac.batlog;
/**
 * Created by burns on 3/19/18.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<String> batteryList;
    List<String> sortedList;

    LayoutInflater inflter;
    StuffPacker stuffPack;

    public CustomAdapter(Context applicationContext, List<String> batteryList, StuffPacker inputStuff) {
        this.context = applicationContext;
        this.batteryList = batteryList;

        inflter = (LayoutInflater.from(applicationContext));
        this.stuffPack = inputStuff;
    }

    @Override
    public int getCount() {
        return batteryList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflter.inflate(R.layout.activity_listview, null);
        if (stuffPack.batteryList.get(i).getGroupInt() == 2) {

            TextView id = (TextView) view.findViewById(R.id.textView_id);
            TextView mah = (TextView) view.findViewById(R.id.textView_mah);
            TextView name = (TextView) view.findViewById(R.id.textView_name);
            TextView volt = (TextView) view.findViewById(R.id.textView_volt);
            TextView cycles = (TextView) view.findViewById(R.id.textView_cycles);
            TextView group = (TextView) view.findViewById(R.id.textView_group);

            Button button = (Button) view.findViewById(R.id.textView_button_plus);
            button.setTag(i);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int i = (int) view.getTag();
                    view = inflter.inflate(R.layout.activity_listview, null);
                    // click handling code

                    Intent intent = new Intent(context, BatteryActivity.class);
                    intent.putExtra("KEY-ID", batteryList.get(i));
                    intent.putExtra("stuffpack", stuffPack);
                    context.startActivity(intent);
                    //Intent intent = new Intent(context,MainActivity.class);
                    //context.startActivity(intent);
                }
            });

            //String str = getBatteryString(batteryList.get(i));

            //Battery bat = new Battery(batteryList.get(i));
            //Log.d("CustomAddapter", "BatINdex:" + i);
            int batIndex = i;//stuffPack.getBatteryIndexById(i+"");

            id.setText(stuffPack.batteryList.get(batIndex).getIdString());
            mah.setText(stuffPack.batteryList.get(batIndex).getmAh() + "mAh");
            name.setText(stuffPack.batteryList.get(batIndex).getName());
            volt.setText(stuffPack.batteryList.get(batIndex).getVolt() + "V");
            cycles.setText(stuffPack.batteryList.get(batIndex).getCycles() + "cycles");
            group.setText(stuffPack.batGroup.getNameFromId(stuffPack.batteryList.get(batIndex).getGroupInt()));

        }
        return view;
    }

    // put below code (method) in Adapter class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        sortedList.clear();
        if (charText.length() == 0) {
            sortedList.addAll(arraylist);
        }
        else
        {
            for (MyBean wp : arraylist) {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    sortedList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}