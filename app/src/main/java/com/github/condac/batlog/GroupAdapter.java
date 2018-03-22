package com.github.condac.batlog;
/**
 * Created by burns on 3/19/18.
 */

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
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

public class GroupAdapter extends BaseAdapter {
    Context context;
    List<String> batteryList;

    LayoutInflater inflter;
    StuffPacker stuffPack;

    public GroupAdapter(Context applicationContext) {
        this.context = applicationContext;
        this.batteryList = batteryList;

        inflter = (LayoutInflater.from(applicationContext));
        stuffPack = StuffPacker.getInstance();
    }

    @Override
    public int getCount() {
        return stuffPack.batGroup.groupList.size();
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

        //Log.d("GroupAdaper", "Loop");

        view = inflter.inflate(R.layout.activity_group_listview, null);
        TextView id = (TextView) view.findViewById(R.id.textView_groupId);

        TextView name = (TextView) view.findViewById(R.id.textView_group_name);

        Button button= (Button) view.findViewById(R.id.button_edit);
        button.setTag(stuffPack.batGroup.groupList.get(i).id);
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                int id = (int)view.getTag();
                Log.d("Button click" , "Edit button in groupview"+id);
                EditGroupDialog editGroupDialog = new EditGroupDialog();
                editGroupDialog.show(((AppCompatActivity)context).getSupportFragmentManager(), ""+id);
                //notifyDataSetChanged();
            }
        });


        int batIndex = i;//stuffPack.getBatteryIndexById(i+"");

        id.setText(stuffPack.batGroup.groupList.get(i).id+"");
        //notifyDataSetChanged();
        name.setText(stuffPack.batGroup.groupList.get(i).name);
        //notifyDataSetChanged();

        return view;
    }



}