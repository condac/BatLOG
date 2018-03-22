package com.github.condac.batlog;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditBatActivity extends AppCompatActivity {

    private EditText mNameView;
    private EditText mIdView;
    private EditText mMahView;
    private EditText mVoltView;
    private EditText mDateView;
    private Calendar myCalendar = Calendar.getInstance();
    private EditText mManufacturerView;
    private EditText mModelView;
    private EditText mCyclesView;

    private Spinner spinnerGroup;
    private Spinner spinnerID;
    List<String> spinnerArray;
    List<String> spinnerArrayID;
    StuffPacker stuffPack;

    String selectedId;
    Battery currentBattery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bat);
        Intent intent = getIntent();
        stuffPack = StuffPacker.getInstance();

        selectedId = stuffPack.batteryListStringIndex.get(0);

        mNameView = (EditText) findViewById(R.id.name);
        mIdView = (EditText) findViewById(R.id.id);
        mMahView = (EditText) findViewById(R.id.mah);
        mVoltView = (EditText) findViewById(R.id.volt);
        mDateView = (EditText) findViewById(R.id.date);
        mManufacturerView = (EditText) findViewById(R.id.manufacturer);
        mModelView = (EditText) findViewById(R.id.model);
        mCyclesView = (EditText) findViewById(R.id.cycles);


        spinnerID = (Spinner) findViewById(R.id.spinner_id);
        spinnerID.setOnItemSelectedListener(new EditBatActivity.ItemSelectedListenerID());
        spinnerArrayID =  stuffPack.batteryListStringIndex;
        Log.d("SpinnerArrayID", "hej" + spinnerArrayID.get(1) );
        ArrayAdapter<String> adapterID = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArrayID);
        adapterID.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerID.setAdapter(adapterID);

        spinnerGroup = (Spinner) findViewById(R.id.group_spinner);
        spinnerGroup.setOnItemSelectedListener(new EditBatActivity.ItemSelectedListener());

        // you need to have a list of data that you want the spinner to display
        spinnerArray =  stuffPack.batGroup.getStringList();
        Log.d("SpinnerArray", "hej" + spinnerArray.get(1) );
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
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
                new DatePickerDialog(EditBatActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        Button saveButton = (Button) findViewById(R.id.edit_bat_save_button);
            saveButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAction();
                finish();

            }
        });


    }
    public void loadValues() {
        Log.d("LoadValues", "Update");

        currentBattery = stuffPack.getBatteryById(selectedId);

        mNameView.setText(currentBattery.getName());
        mDateView.setText(currentBattery.getDate());
        mMahView.setText(currentBattery.getmAh());
        mManufacturerView.setText(currentBattery.getManufacturer());
        mModelView.setText(currentBattery.getModel());
        mVoltView.setText(currentBattery.getVolt());
        mCyclesView.setText(currentBattery.getCycles());

        String groupString = stuffPack.batGroup.getNameFromId(currentBattery.getGroupInt());
        Log.d("EditBatterygroup", "groupname: "+groupString);

        for (int i=0; i<spinnerArray.size(); i++) {
            Log.d(spinnerArray.get(i),groupString);
            if (spinnerArray.get(i).equals(groupString)) {
                spinnerGroup.setSelection(i);
            }
        }
        //spinnerGroup.setSelection(currentBattery.getGroupInt());

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
    public class ItemSelectedListenerID implements AdapterView.OnItemSelectedListener {

        //get strings of first item
        String firstItem = String.valueOf(spinnerID.getSelectedItem());

        public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
            if (firstItem.equals(String.valueOf(spinnerID.getSelectedItem()))) {
                // ToDo when first item is selected
            } else {
                Toast.makeText(parent.getContext(),
                        "You have selected : " + parent.getItemAtPosition(pos).toString(),
                        Toast.LENGTH_LONG).show();
                // Todo when item is selected by the user
                selectedId = parent.getItemAtPosition(pos).toString();
                loadValues();
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
    private void saveAction() {
        String name = isNotEmpty(mNameView.getText().toString(), "noname");
        Log.i("input name", name);
        currentBattery.setName(name);

        String mah = isNotEmpty(mMahView.getText().toString(), "0");
        Log.i("input mAh", mah);
        currentBattery.setmAh(mah);

        String volt = isNotEmpty(mVoltView.getText().toString(), "0");
        Log.i("input Volt", volt);
        currentBattery.setVolt(volt);

        String date = isNotEmpty(mDateView.getText().toString(), "0");
        Log.i("input date", date);
        currentBattery.setDate(date);

        String make = isNotEmpty(mManufacturerView.getText().toString(), "empty");
        Log.i("input make", make);
        currentBattery.setManufacturer(make);

        String model = isNotEmpty(mModelView.getText().toString(), "empty");
        Log.i("input model", model);
        currentBattery.setModel(model);

        String group = isNotEmpty(String.valueOf(spinnerGroup.getSelectedItem()), "0");
        int groupId = stuffPack.batGroup.getIdFromName(group);
        Log.i("input group", "int:"+groupId);
        currentBattery.setGroup(groupId);

        String cycles = isNotEmpty(mCyclesView.getText().toString(), "0");
        Log.i("input cycles", cycles);
        currentBattery.setCycles(cycles);

        currentBattery.writeJSON();

    }
    private String isNotEmpty(String inString, String replace) {
        if(inString.equals("")) {
            return replace;
        } else {
            return inString;
        }

    }
}
