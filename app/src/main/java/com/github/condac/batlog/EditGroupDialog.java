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
import android.widget.TextView;


public class EditGroupDialog extends AppCompatDialogFragment {

    private EditText mNameView;
    private TextView mIdView;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {



        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_group_edit, null);

        mNameView = view.findViewById(R.id.dialog_edit_name);
        mIdView = view.findViewById(R.id.dialog_edit_id);
        StuffPacker stuffPacker = StuffPacker.getInstance();
        mIdView.setText(getTag());

        builder.setView(view)
                .setTitle("Edit Group")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
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
                        stuffPacker.batGroup.editGroup(id, name);

                    }
                });

        return builder.create();
    }
}
