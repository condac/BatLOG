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
    private EditText mNameViewDialog;
    private EditText mIdViewDialog;
    private Spinner spinnerGroup;
    List<String> spinnerArray;
    String selectedId;
    private ListView groupList;
    GroupAdapter groupAdapter;

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
        groupAdapter = new GroupAdapter(GroupActivity.this);
        groupList.setAdapter(groupAdapter);

    }

    private void openDialog() {
        //AddGroupDialog addGroupDialog = new AddGroupDialog();
        //addGroupDialog.show(getSupportFragmentManager(), "kaka dialog");


        AlertDialog.Builder alertDialog = new AlertDialog.Builder(GroupActivity.this);

        LayoutInflater inflater = getLayoutInflater();

        final View view2 = inflater.inflate(R.layout.dialog_group_add, null);


        mNameViewDialog = view2.findViewById(R.id.dialog_add_name);
        mIdViewDialog = view2.findViewById(R.id.dialog_add_id);
        StuffPacker stuffPacker = StuffPacker.getInstance();
        mIdViewDialog.setText(""+stuffPacker.batGroup.getFirstFreeId());

        alertDialog.setView(view2)
                .setTitle("Add new Group")
                .setNegativeButton("canel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Click dialog: ", "id:"+mIdViewDialog.getText()+" name:"+mNameViewDialog.getText());
                        StuffPacker stuffPacker = StuffPacker.getInstance();
                        int id = Integer.parseInt(mIdViewDialog.getText().toString());
                        String name = mNameViewDialog.getText().toString();
                        if (stuffPacker.batGroup.checkIDUsed(id)) {
                            Toast.makeText(view2.getContext(), "ID already used", Toast.LENGTH_SHORT).show();
                        }else {
                            stuffPacker.batGroup.addGroup(id, name);
                            groupAdapter.notifyDataSetChanged();

                        }



                    }
                });
        alertDialog.show();

    }

}
