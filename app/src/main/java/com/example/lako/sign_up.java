package com.example.lako;

import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class sign_up extends AppCompatActivity {

    private CheckBox checkBox; //checkbox of terms and condition
    private MaterialAlertDialogBuilder materialAlertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        //TERMS AND CONDITION ALERT
    getSupportActionBar().hide();

    checkBox = findViewById(R.id.terms_condition_check);
    materialAlertDialogBuilder = new MaterialAlertDialogBuilder(this);
    checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
            if(b){
                materialAlertDialogBuilder.setTitle("Terms and Conditions");
                materialAlertDialogBuilder.setMessage("LALALA");
                materialAlertDialogBuilder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                materialAlertDialogBuilder.setNegativeButton("Decline", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        checkBox.setChecked(false);
                    }
                });

                materialAlertDialogBuilder.show();
            }
        }
    });
    }
}