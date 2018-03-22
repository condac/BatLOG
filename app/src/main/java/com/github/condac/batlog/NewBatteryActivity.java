package com.github.condac.batlog;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;



import android.os.Environment;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.io.File;
import java.util.Locale;
import java.text.SimpleDateFormat;
import java.util.List;

import android.util.Log;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * Create new battery dialog.
 */
public class NewBatteryActivity extends AppCompatActivity  {
    /**
     * Id to identity WRITE_EXTERNAL_STORAGE permission request.
     */


    // UI references.

    private EditText mNameView;
    private EditText mIdView;
    private EditText mMahView;
    private EditText mVoltView;
    private EditText mDateView;
    private Calendar myCalendar = Calendar.getInstance();
    private EditText mManufacturerView;
    private EditText mModelView;
    private Spinner spinnerGroup;

    List<String> spinnerArray;
    StuffPacker stuffPack;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_battery);
        // Set up the login form.



        stuffPack = StuffPacker.getInstance();



        mNameView =  findViewById(R.id.name);
        mIdView =  findViewById(R.id.id);
        mMahView =  findViewById(R.id.mah);
        mVoltView =  findViewById(R.id.volt);
        mDateView =  findViewById(R.id.date);
        mManufacturerView =  findViewById(R.id.manufacturer);
        mModelView =  findViewById(R.id.model);


        //mLayout = findViewById(R.id.new_bat_form);
        mIdView.setText(""+ getNewID());



        spinnerGroup = findViewById(R.id.group_spinner);
        spinnerGroup.setOnItemSelectedListener(new ItemSelectedListener());



        // you need to have a list of data that you want the spinner to display
        spinnerArray =  stuffPack.batGroup.getStringList();
        Log.d("SpinnerArray", "hej" + spinnerArray.get(1) );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerGroup.setAdapter(adapter);



        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        mDateView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(NewBatteryActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        Button mEmailSignInButton = findViewById(R.id.create_new_bat_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                String id = isNotEmpty(mIdView.getText().toString(), "wrongid");
                if (!checkIdInUse(id)) {
                    createBattery();
                } else {

                }


            }
        });


    }
    public class ItemSelectedListener implements AdapterView.OnItemSelectedListener {

        //get strings of first item
        String firstItem = String.valueOf(spinnerGroup.getSelectedItem());

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (firstItem.equals(String.valueOf(spinnerGroup.getSelectedItem()))) {
                // ToDo when first item is selected
            } else {
                Toast.makeText(parent.getContext(),
                        "You have selected : " + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_LONG).show();
                // Todo when item is selected by the user
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> arg) {

        }

    }
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mDateView.setText(sdf.format(myCalendar.getTime()));
        //mDateView.setText(myCalendar.getTime().toString());
    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        } else {
            return false;
        }

    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public void createExternalStoragePrivateFile(String fileName, String data) {
        // Create a path where we will place our private file on external
        // storage.
        if (isExternalStorageWritable() ) {
            Log.i("Storage status", "writable");
        } else {
            Log.i("Storage status", " not writable");
        }

        File root = new File(Environment.getExternalStorageDirectory(), "BatLOG");
        File batteries = new File(root, "Batteries");
        File idDir = new File(batteries, fileName);
        File file = new File(idDir, "info.txt");


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
            myOutWriter.append(data);

            myOutWriter.close();

            fOut.flush();
            fOut.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed in createExternalStoragePrivateFile: " + e.toString());
        }

    }



    public boolean hasExternalStoragePrivateFile(String fileName) {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File root = new File(Environment.getExternalStorageDirectory(), "BatLOG");
        File batteries = new File(root, "Batteries");
        File idDir = new File(batteries, fileName);
        File file = new File(idDir, "info.json");


        if (file != null) {
            return file.exists();
        }
        return false;
    }
    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void createBattery() {
        Log.i("test", "Hello World");


        // Store values at the time of the login attempt.
        String name = isNotEmpty(mNameView.getText().toString(), "noname");
        Log.i("input name", name);
        String id = isNotEmpty(mIdView.getText().toString(), "wrongid");
        Log.i("input id", id);
        String mah = isNotEmpty(mMahView.getText().toString(), "0");
        Log.i("input mAh", mah);
        String volt = isNotEmpty(mVoltView.getText().toString(), "0");
        Log.i("input Volt", volt);
        String date = isNotEmpty(mDateView.getText().toString(), "0");
        Log.i("input date", date);

        String make = isNotEmpty(mManufacturerView.getText().toString(), "empty");
        Log.i("input make", make);
        String model = isNotEmpty(mModelView.getText().toString(), "empty");
        Log.i("input model", model);
        String group = isNotEmpty(String.valueOf(spinnerGroup.getSelectedItem()), "0");
        int groupId = stuffPack.batGroup.getIdFromName(group);
        Log.i("input group", "int:"+groupId);




        Battery newBattery = new Battery(id, name, mah, volt, date, make, model, groupId);
        newBattery.writeJSON();
        stuffPack.createBatteryList();


        finish();
    }

    private int getNewID() {
        //TODO: Read folders and find a unused number
        if (isExternalStorageWritable()) {
            boolean found = true;
            int counter = 1;
            while(found) {
                if (hasExternalStoragePrivateFile(""+counter)) {
                    counter++;
                } else {
                    found = false;
                    return counter;
                }
            }
            return 1;
        }

        return 0;
    }

    private boolean checkIdInUse(String idIn) {

        if (stuffPack.getBatteryById(idIn) != null ) {
            Toast.makeText(getApplicationContext(), "ID is already in use", Toast.LENGTH_LONG).show();
            return true;
        }
        return false;
    }

    private String isNotEmpty(String inString, String replace) {
        if(inString.equals("")) {
            return replace;
        } else {
            return inString;
        }

    }

}

