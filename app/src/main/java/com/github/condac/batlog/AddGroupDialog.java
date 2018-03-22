package com.github.condac.batlog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

/**
 * Created by burns on 3/22/18.
 */

public class AddGroupDialog extends AppCompatDialogFragment {

    private EditText mNameView;
    private EditText mIdView;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_group_add, null);

        mNameView = view.findViewById(R.id.dialog_add_name);
        mIdView = view.findViewById(R.id.dialog_add_id);
        StuffPacker stuffPacker = StuffPacker.getInstance();
        mIdView.setText(""+stuffPacker.batGroup.getFirstFreeId());

        builder.setView(view)
                .setTitle("Add new Group")
                .setNegativeButton("canel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Log.d("Click dialog: ", "id:"+mIdView.getText()+" name:"+mNameView.getText());
                        StuffPacker stuffPacker = StuffPacker.getInstance();
                        int id = Integer.parseInt(mIdView.getText().toString());
                        String name = mNameView.getText().toString();
                        stuffPacker.batGroup.addGroup(id, name);

                    }
                });

        return builder.create();
    }
}
