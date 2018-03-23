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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EditBatActivity extends AppCompatActivity {

    private EditText mNameView;
    private TextView mIdView;
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

        String id = getIntent().getStringExtra("KEY-ID");
        Log.d("Edit battery", "Key-id;"+id);
        int batIndex = stuffPack.getBatteryIndexById(id);
        selectedId = id;
        //if (stuffPack.batteryListStringIndex.size()>0) {
        //    selectedId = stuffPack.batteryListStringIndex.get(0);
        //}


        mNameView = (EditText) findViewById(R.id.name);
        mIdView = findViewById(R.id.edit_id);
        mMahView = (EditText) findViewById(R.id.mah);
        mVoltView = (EditText) findViewById(R.id.volt);
        mDateView = (EditText) findViewById(R.id.date);
        mManufacturerView = (EditText) findViewById(R.id.manufacturer);
        mModelView = (EditText) findViewById(R.id.model);
        mCyclesView = (EditText) findViewById(R.id.cycles);


        spinnerGroup = (Spinner) findViewById(R.id.group_spinner);
        spinnerGroup.setOnItemSelectedListener(new EditBatActivity.ItemSelectedListener());

        // you need to have a list of data that you want the spinner to display
        spinnerArray =  stuffPack.batGroup.getStringList();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGroup.setAdapter(adapter);

        loadValues();

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

        int batIndex = stuffPack.getBatteryIndexById(selectedId);



        mIdView.setText("Edit battery:"+stuffPack.batteryList.get(batIndex).getIdString());
        mNameView.setText(stuffPack.batteryList.get(batIndex).getName());
        mDateView.setText(stuffPack.batteryList.get(batIndex).getDate());
        mMahView.setText(stuffPack.batteryList.get(batIndex).getmAh());
        mManufacturerView.setText(stuffPack.batteryList.get(batIndex).getManufacturer());
        mModelView.setText(stuffPack.batteryList.get(batIndex).getModel());
        mVoltView.setText(stuffPack.batteryList.get(batIndex).getVolt());
        mCyclesView.setText(stuffPack.batteryList.get(batIndex).getCycles());

        String groupString = stuffPack.batGroup.getNameFromId(stuffPack.batteryList.get(batIndex).getGroupInt());
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

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        mDateView.setText(sdf.format(myCalendar.getTime()));
        //mDateView.setText(myCalendar.getTime().toString());
    }
    private void saveAction() {
        int batIndex = stuffPack.getBatteryIndexById(selectedId);

        String name = isNotEmpty(mNameView.getText().toString(), "noname");
        Log.i("input name", name);
        stuffPack.batteryList.get(batIndex).setName(name);

        String mah = isNotEmpty(mMahView.getText().toString(), "0");
        Log.i("input mAh", mah);
        stuffPack.batteryList.get(batIndex).setmAh(mah);

        String volt = isNotEmpty(mVoltView.getText().toString(), "0");
        Log.i("input Volt", volt);
        stuffPack.batteryList.get(batIndex).setVolt(volt);

        String date = isNotEmpty(mDateView.getText().toString(), "0");
        Log.i("input date", date);
        stuffPack.batteryList.get(batIndex).setDate(date);

        String make = isNotEmpty(mManufacturerView.getText().toString(), "empty");
        Log.i("input make", make);
        stuffPack.batteryList.get(batIndex).setManufacturer(make);

        String model = isNotEmpty(mModelView.getText().toString(), "empty");
        Log.i("input model", model);
        stuffPack.batteryList.get(batIndex).setModel(model);

        String group = isNotEmpty(String.valueOf(spinnerGroup.getSelectedItem()), "0");
        int groupId = stuffPack.batGroup.getIdFromName(group);
        Log.i("input group", "int:"+groupId);
        stuffPack.batteryList.get(batIndex).setGroup(groupId);

        String cycles = isNotEmpty(mCyclesView.getText().toString(), "0");
        Log.i("input cycles", cycles);
        stuffPack.batteryList.get(batIndex).setCycles(cycles);

        stuffPack.batteryList.get(batIndex).writeJSON();

    }
    private String isNotEmpty(String inString, String replace) {
        if(inString.equals("")) {
            return replace;
        } else {
            return inString;
        }

    }
}
