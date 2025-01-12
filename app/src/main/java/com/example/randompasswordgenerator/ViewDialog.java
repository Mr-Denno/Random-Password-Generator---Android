package com.example.randompasswordgenerator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.google.android.material.snackbar.Snackbar;

public class ViewDialog extends AppCompatDialogFragment {

    private TextView txtName;
    private TextView txtEmail;
    private TextView txtPassword;
    private String Name;
    private String Email;
    private String Password;

    public ViewDialog() {}

    public ViewDialog(String text1, String text2, String text3) {
        Name = text1;
        Email = text2;
        Password = text3;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.view_dialog, null);

        txtName = view.findViewById(R.id.editTextName);
        txtEmail = view.findViewById(R.id.editTextEmail);
        txtPassword = view.findViewById(R.id.editTextPassword);

        txtName.setText(Name);
        txtEmail.setText(Email);
        txtPassword.setText(Password);

        //TODO: aggiungere la possibilità di copiare il campo mantenendo premuto

        //----------------------------- Button Copy Email------------------------------
        txtEmail.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //copy
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("TextView", txtEmail.getText().toString());
                clipboard.setPrimaryClip(clip);

                //messaggio per il feedback
                Snackbar snackbar = Snackbar.make(view, "email copied!", Snackbar.LENGTH_SHORT);
                snackbar.show();

                return false;
            }
        });


        //----------------------------- Button Copy Password------------------------------
        txtPassword.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                //copy
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("TextView", txtPassword.getText().toString());
                clipboard.setPrimaryClip(clip);

                //messaggio per il feedback
                Snackbar snackbar = Snackbar.make(view, "password copied!", Snackbar.LENGTH_SHORT);
                snackbar.show();
                return false;
            }
        });


        //----------------------------- Dialog ------------------------------
        builder.setView(view)
                .setTitle("Account " + Name)
                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {

                    }
                });

        return builder.create();

    }
}
