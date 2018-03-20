package com.github.condac.batlog;

/**
 * Created by burns on 3/20/18.
 */
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

import static java.lang.Integer.parseInt;

public class Battery implements Serializable {

    public static final String FIELD_ID = "id";
    public static final String FIELD_NAME = "name";


    private String id = "0";
    private int group = 0;
    private String name = "noname";
    private String mah = "noname";
    private String volt = "noname";
    private String date = "nodate";
    private String model = " ";
    private String manufacturer = " ";
    private int cycles = 0;


    public Battery(String idString) {
        this.id = idString;
        this.readJSON();
    }


    public Battery() {

    }

    public Battery(String id, String name, String mah, String volt, String date, String manufacturer, String model, int group) {
        this.id = id;
        this.name = name;
        this.mah = mah;
        this.volt = volt;
        this.date = date;
        this.group = group;

        this.model = model;
        this.manufacturer = manufacturer;

    }

    public void writeJSON() {
        final Gson gson = new Gson();
        String json = gson.toJson(this);

        Log.d("Write JSON", json);

        File root = new File(Environment.getExternalStorageDirectory(), "BatLOG");
        File batteries = new File(root, "Batteries");
        File idDir = new File(batteries, id);
        File file = new File(idDir, "info.json");


        if (!root.mkdirs()) {
            Log.e("mkdir", "Directory not created");
        }
        if (!batteries.mkdirs()) {
            Log.e("mkdir batteries", "Directory not created");
        }
        if (!idDir.mkdirs()) {
            Log.e("mkdir idDir", "Directory not created");
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

    public void readJSON() {
        final Gson gson = new Gson();
        String json = gson.toJson(this);

        Log.d("JSON", json);

        File root = new File(Environment.getExternalStorageDirectory(), "BatLOG");
        File batteries = new File(root, "Batteries");
        File idDir = new File(batteries, id);
        File file = new File(idDir, "info.json");
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

                }


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }else {

        }

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

                Log.d("getBatteryString", ""+total);

                Battery tempbat = gson.fromJson(total, Battery.class);
                this.cycles = tempbat.cycles;
                this.name = tempbat.name;
                this.id = tempbat.id;
                this.mah = tempbat.mah;
                this.volt = tempbat.volt;
                this.manufacturer = tempbat.manufacturer;
                this.model = tempbat.model;
                this.group = tempbat.group;
                this.date = tempbat.date;

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    }

    public void batteryAddCycle(String charge, String discharge, String capacity) {
        // Create a path where we will place our private file on external
        // storage.

        File root = new File(Environment.getExternalStorageDirectory(), "BatLOG");
        File batteries = new File(root, "Batteries");
        File idDir = new File(batteries, this.id);
        File file = new File(idDir, "info.json");
        File fileCycles = new File(idDir, "cycles.csv");


        if (!root.mkdirs()) {
            Log.e("mkdir", "Directory not created");
        }
        if (!batteries.mkdirs()) {
            Log.e("mkdir batteries", "Directory not created");
        }


        Log.i("File info:", fileCycles.getAbsolutePath());
        try {

            FileOutputStream fOut = new FileOutputStream(fileCycles, true);

            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);

            Log.d("BatteryAddCycle", "cycles before:"+cycles);
            cycles++;

            myOutWriter.append("cycle;timestamp;"+cycles+";"+charge+";"+discharge+";"+capacity+";");
            myOutWriter.append("\n");

            myOutWriter.close();

            fOut.flush();
            fOut.close();
            this.writeJSON();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed in createExternalStoragePrivateFile: " + e.toString());
        }
    }

    public String getIdString() {
        return id;
    }

    public int getIdInt() {
        //return <Integer>.parseInt(id);
        return 0;
    }

    public String getmAh() {
        return mah;
    }

    public String getCycles() {
        return cycles+"";
    }

    public String getVolt() {
        return volt;
    }

    public String getName() {
        return name;
    }
    public String getDate() {
        return date;
    }
    public void setId(String idIn) {
        this.id = idIn;
    }

    public String getGroupName() {
        return ""+group;
    }

    public int getGroupInt() {
        return group;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getModel() {
        return model;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setmAh(String mAh) {
        this.mah = mAh;
    }

    public void setVolt(String volt) {
        this.volt = volt;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setGroup(int group) {
        this.group = group;
    }

    public void setCycles(String cycles) {

        this.cycles = Integer.parseInt(cycles);
    }
}
