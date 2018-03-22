package com.github.condac.batlog;

import android.os.Environment;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by burns on 3/20/18.
 */

class GroupObject implements Serializable {
    int id;
    String name;
    public GroupObject(int id, String name) {
        this.id = id;
        this.name = name;

    }
}

public class BatteryGroup implements Serializable {

    List<GroupObject> groupList = new ArrayList<GroupObject>();

    public BatteryGroup() {

    }

    public void readJSON() {
        // load saved configs from jsonfile
        final Gson gson = new Gson();
        String json = gson.toJson(this);

        Log.d("BatteryGroup", "ReadJSON");

        File root = new File(Environment.getExternalStorageDirectory(), "BatLOG");
        File configDir = new File(root, "config");
        File file = new File(configDir, "group.json");


        if (file.exists()) {
            FileInputStream is;
            BufferedReader reader;
            try {
                Log.d("Reading file", file.getAbsolutePath());
                is = new FileInputStream(file);
                reader = new BufferedReader(new InputStreamReader(is));
                String line = null;
                String total = "";
                line = reader.readLine();

                while (line != null) {
                    total = total+line;
                    line = reader.readLine();
                }

                Log.d("getGroupString", ""+total);

                BatteryGroup tempgroup = gson.fromJson(total, BatteryGroup.class);
                if (tempgroup.groupList.size()>0) {
                    Log.d("BatteryGroup", "ReadJSON found data in file");
                    this.groupList = tempgroup.groupList;
                    return;
                } else {
                    Log.d("BatteryGroup", "ReadJSON creating defaults");
                    // create default list
                    List<GroupObject> newList = new ArrayList<GroupObject>();
                    newList.add( new GroupObject(1, "AA"));
                    newList.add( new GroupObject(2, "AAA"));
                    newList.add( new GroupObject(3, "18650"));
                    this.groupList = newList;
                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }




        } else {
            Log.d("BatteryGroup", "ReadJSON creating defaults");
            // create default list
            List<GroupObject> newList = new ArrayList<GroupObject>();
            newList.add( new GroupObject(1, "AA"));
            newList.add( new GroupObject(2, "AAA"));
            newList.add( new GroupObject(3, "18650"));
            this.groupList = newList;
        }
        writeJSON(); // this will NOT be called if a group.json exists, return statement in code above stops function to get here unless new file is needed
    }

    public void writeJSON() {
        final Gson gson = new Gson();
        String json = gson.toJson(this);


        Log.d("BatteryGroup", "ReadJSON");

        Log.d("JSON", json);

        File root = new File(Environment.getExternalStorageDirectory(), "BatLOG");
        File configDir = new File(root, "config");
        File file = new File(configDir, "group.json");


        if (!root.mkdirs()) {
            Log.e("mkdir", "Directory not created");
        }
        if (!configDir.mkdirs()) {
            Log.e("mkdir batteries", "Directory not created");
        }


        Log.i("File info:", file.getAbsolutePath());
        try {
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(json);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed in createExternalStoragePrivateFile: " + e.toString());
        }
    }

    public void addGroup(String name) {

    }
    public void addGroup(int id , String name) {
        groupList.add( new GroupObject(id, name));
        writeJSON();
    }

    public String getNameFromId(int idIn) {
        for (GroupObject group : groupList ) {
            if (group.id == idIn) {
                return group.name;
            }
        }
        return "noGroup";
    }

    public List<String> getStringList() {
        List<String> out = new ArrayList<String>();
        for (GroupObject group : groupList ) {
            out.add(group.name);
        }
        return out;
    }

    public int getIdFromName(String groupIn) {
        for (GroupObject group : groupList ) {
            if (group.name.equals(groupIn)) {
                return group.id;
            }
        }
        return 0;
    }

    public int getFirstFreeId() {
        for (int i=1;i<100;i++) {
            Boolean exists = false;
            for (GroupObject group : groupList ) {
                if (group.id == i) {
                    exists = true;
                }
            }
            if (!exists) {
                return i;
            }
        }
        return 100;
    }
}
