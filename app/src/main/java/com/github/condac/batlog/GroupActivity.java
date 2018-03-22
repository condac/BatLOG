package com.github.condac.batlog;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class GroupActivity extends AppCompatActivity {

    StuffPacker stuffPack;
    private View mNameView;
    private View mIdView;
    private Spinner spinnerGroup;
    List<String> spinnerArray;
    String selectedId;
    private ListView groupList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group);

        stuffPack = StuffPacker.getInstance();




        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.group_floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "New Group", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                openDialog();
            }
        });

    }
    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first
        stuffPack = StuffPacker.getInstance();

        groupList = findViewById(R.id.group_list_view);
        GroupAdapter groupAdapter = new GroupAdapter(GroupActivity.this);
        groupList.setAdapter(groupAdapter);

    }

    private void openDialog() {
        AddGroupDialog addGroupDialog = new AddGroupDialog();
        addGroupDialog.show(getSupportFragmentManager(), "kaka dialog");
    }

}
