package com.github.condac.batlog;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;


import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
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
    Context context;
    /**
     * Id to identity WRITE_EXTERNAL_STORAGE permission request.
     */
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    private View mLayout;


    // UI references.

    private EditText mNameView;
    private EditText mIdView;
    private EditText mMahView;
    private EditText mVoltView;
    private EditText mDateView;
    private Calendar myCalendar = Calendar.getInstance();
    private EditText mManufacturerView;
    private EditText mModelView;
    private EditText mGroupView;
    private Spinner spinnerGroup;

    List<String> spinnerArray;
    StuffPacker stuffPack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_battery);
        // Set up the login form.

        Intent intent = getIntent();

        stuffPack = StuffPacker.getInstance();



        mNameView = (EditText) findViewById(R.id.name);
        mIdView = (EditText) findViewById(R.id.id);
        mMahView = (EditText) findViewById(R.id.mah);
        mVoltView = (EditText) findViewById(R.id.volt);
        mDateView = (EditText) findViewById(R.id.date);
        mManufacturerView = (EditText) findViewById(R.id.manufacturer);
        mModelView = (EditText) findViewById(R.id.model);
        mGroupView = (EditText) findViewById(R.id.model);

        mLayout = findViewById(R.id.new_bat_form);
        mIdView.setText(""+ getNewID());



        spinnerGroup = (Spinner) findViewById(R.id.group_spinner);
        spinnerGroup.setOnItemSelectedListener(new ItemSelectedListener());



        // you need to have a list of data that you want the spinner to display
        spinnerArray =  stuffPack.batGroup.getStringList();
        Log.d("SpinnerArray", "hej" + spinnerArray.get(1) );

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, spinnerArray);

        //ArrayAdapter<User> dataAdapter = new ArrayAdapter<User>(this, android.R.layout.simple_spinner_item, users);

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


        Button mEmailSignInButton = (Button) findViewById(R.id.create_new_bat_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createBattery();

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
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
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

    public void deleteExternalStoragePrivateFile(String fileName) {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File root = new File(Environment.getExternalStorageDirectory(), "BatLOG");
        File batteries = new File(root, "Batteries");
        File idDir = new File(batteries, fileName);
        File file = new File(idDir, "info.json");
        if (file != null) {
            file.delete();
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

        //createExternalStoragePrivateFile( id , id+";"+name+";"+mah+";"+volt+";"+date);


        boolean cancel = false;
        View focusView = null;



        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            //showProgress(true);
            //mAuthTask = new UserLoginTask(email, password);
            //mAuthTask.execute((Void) null);
        }
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

    private String isNotEmpty(String inString, String replace) {
        if(inString.equals("")) {
            return replace;
        } else {
            return inString;
        }

    }
    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }



    private static final int REQUESTCODE_STORAGE_PERMISSION = 999999999;

    private static boolean storagePermitted(Activity activity) {

        Boolean readPermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        Boolean writePermission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;

        if (readPermission && writePermission) {
            return true;
        }

        ActivityCompat.requestPermissions(activity , new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUESTCODE_STORAGE_PERMISSION);
        return false;
    }



    public static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1;
    public int mkFolder(String folderName){ // make a folder under Environment.DIRECTORY_DCIM
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)){
            Log.d("myAppName", "Error: external storage is unavailable");
            return 0;
        }
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            Log.d("myAppName", "Error: external storage is read only.");
            return 0;
        }
        Log.d("myAppName", "External storage is not read only or unavailable");

        if (ContextCompat.checkSelfPermission(this, // request permission when it is not granted.
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d("myAppName", "permission:WRITE_EXTERNAL_STORAGE: NOT granted!");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Log.d("myAppName", "strange if case");
                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),folderName);
        int result = 0;
        if (folder.exists()) {
            Log.d("myAppName","folder exist:"+folder.toString());
            result = 2; // folder exist
        }else{
            try {
                if (folder.mkdir()) {
                    Log.d("myAppName", "folder created:" + folder.toString());
                    result = 1; // folder created
                } else {
                    Log.d("myAppName", "creat folder fails:" + folder.toString());
                    result = 0; // creat folder fails
                }
            }catch (Exception ecp){
                ecp.printStackTrace();
            }
        }
        return result;
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(NewBatteryActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(NewBatteryActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }


}

