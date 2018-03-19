package com.example.burns.batlog;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.io.File;

import android.util.Log;
import android.widget.Toast;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Create new battery dialog.
 */
public class NewBatteryActivity extends AppCompatActivity  {

    /**
     * Id to identity WRITE_EXTERNAL_STORAGE permission request.
     */
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    private View mLayout;


    // UI references.
    private AutoCompleteTextView mEmailView;
    //private AutoCompleteTextView mEmailView;
    private EditText mNameView;
    private EditText mIdView;
    private EditText mMahView;
    private EditText mVoltView;
    private EditText mDateView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_battery);
        // Set up the login form.

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);

        mNameView = (EditText) findViewById(R.id.name);
        mIdView = (EditText) findViewById(R.id.id);
        mMahView = (EditText) findViewById(R.id.mah);
        mVoltView = (EditText) findViewById(R.id.volt);
        mDateView = (EditText) findViewById(R.id.date);
        mLayout = findViewById(R.id.new_bat_form);
        mIdView.setText(""+ getNewID());

        mkFolder("testfolder");

        if (storagePermitted(this) ) {
            Log.i("test storage", "true");
        }else {
            Log.i("test storage", "false");
        }
        Button mEmailSignInButton = (Button) findViewById(R.id.create_new_bat_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                createBattery();
            }
        });

        mLoginFormView = findViewById(R.id.new_bat_form);
        //mProgressView = findViewById(R.id.login_progress);
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
        File file = new File(batteries, fileName+".txt");


        if (!root.mkdirs()) {
            Log.e("mkdir", "Directory not created");
        }
        if (!batteries.mkdirs()) {
            Log.e("mkdir batteries", "Directory not created");
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
        File file = new File(batteries, fileName+".txt");
        if (file != null) {
            file.delete();
        }
    }

    public boolean hasExternalStoragePrivateFile(String fileName) {
        // Get path for the file on external storage.  If external
        // storage is not currently mounted this will fail.
        File root = new File(Environment.getExternalStorageDirectory(), "BatLOG");
        File batteries = new File(root, "Batteries");
        File file = new File(batteries, fileName+".txt");


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
        String name = mNameView.getText().toString();
        Log.i("input name", name);
        String id = mIdView.getText().toString();
        Log.i("input id", id);
        String mah = mMahView.getText().toString();
        Log.i("input mAh", mah);
        String volt = mVoltView.getText().toString();
        Log.i("input Volt", volt);
        String date = mDateView.getText().toString();
        Log.i("input date", date);


        createExternalStoragePrivateFile( id , id+";"+name+";"+mah+";"+volt+";"+date);


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
        return -1;
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
