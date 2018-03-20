package com.example.burns.batlog;
/**
 * Created by burns on 3/19/18.
 */

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.zip.Inflater;

import static android.os.ParcelFileDescriptor.MODE_APPEND;

public class CustomAdapter extends BaseAdapter {
    Context context;
    List<String> batteryList;
    int flags[];
    LayoutInflater inflter;

    public CustomAdapter(Context applicationContext, List<String> batteryList, int[] flags) {
        this.context = applicationContext;
        this.batteryList = batteryList;
        this.flags = flags;
        inflter = (LayoutInflater.from(applicationContext));
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
        TextView id = (TextView) view.findViewById(R.id.textView_id);
        TextView mah = (TextView) view.findViewById(R.id.textView_mah);
        TextView name = (TextView) view.findViewById(R.id.textView_name);
        TextView volt = (TextView) view.findViewById(R.id.textView_volt);
        TextView cycles = (TextView) view.findViewById(R.id.textView_cycles);

        Button button= (Button) view.findViewById(R.id.textView_button_plus);
        button.setTag(i);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                int i = (int)view.getTag();
                view = inflter.inflate(R.layout.activity_listview, null);
                // click handling code
                TextView cycles = (TextView) view.findViewById(R.id.textView_cycles);

                String str = getBatteryString(batteryList.get(i ) );
                Log.d("button"+i, ""+str);
                String[] items =str.split(";");



                Intent intent = new Intent(context, CycleActivity.class);
                intent.putExtra("KEY-ID", items[0]);
                context.startActivity(intent);
                //Intent intent = new Intent(context,MainActivity.class);
                //context.startActivity(intent);
            }
        });

        String str = getBatteryString(batteryList.get(i));
        Log.d("string", ""+str);
        String[] items =str.split(";");
        Log.d("list size", ""+items.length);
        id.setText(items[0]);
        mah.setText(items[2]+"mAh");
        name.setText(items[1]);
        volt.setText(items[3]+"V");
        cycles.setText(items[5]+"cycles");

        return view;
    }

    public String getBatteryString(String inId) {
        String out = "";
        int cycles = 0;

        File root = new File(Environment.getExternalStorageDirectory(), "BatLOG");
        File batteries = new File(root, "Batteries");
        File idDir = new File(batteries, inId);
        File file = new File(idDir, "info.txt");
        File fileCycles = new File(idDir, "cycles.csv");

        if (fileCycles.exists()) {
            FileInputStream is;
            BufferedReader reader;
            try {
                is = new FileInputStream(fileCycles);
                reader = new BufferedReader(new InputStreamReader(is));
                String line = null;
                line = reader.readLine();
                while (line != null) {
                    line = reader.readLine();
                    cycles++;
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {
            cycles = 0;
        }

        if (file.exists()) {
            FileInputStream is;
            BufferedReader reader;
            try {
                is = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(is));
                String line = null;
                line = reader.readLine();
                out = line;
                Log.d("getBatteryString", out);

                out = out + ";"+cycles;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }




        }
        return out;
    }

}