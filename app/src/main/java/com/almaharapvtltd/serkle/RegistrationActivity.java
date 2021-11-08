package com.almaharapvtltd.serkle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegistrationActivity extends AppCompatActivity {
private TextInputLayout reg_name,reg_phone,reg_email,reg_pass,reg_city;
private MaterialButton reg__sign_up_btn;

private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        reg_name =findViewById(R.id.reg_name);
        reg_phone =findViewById(R.id.reg_number);
        reg_email =findViewById(R.id.reg_email);
        reg_pass =findViewById(R.id.reg_password);
        reg_city =findViewById(R.id.reg_city);
        reg__sign_up_btn =findViewById(R.id.reg_sign_up_btn);

        mAuth =FirebaseAuth.getInstance();

        reg__sign_up_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inpEmail = reg_email.getEditText().getText().toString().trim();
                String inpPass = reg_pass.getEditText().getText().toString().trim();

                if(TextUtils.isEmpty(inpEmail)){
                    reg_email.setError("Email is required.");
                }
                if(TextUtils.isEmpty(inpPass)){
                    reg_pass.setError("Password is required");
                }
                if(inpPass.length() <6){
                    reg_pass.setError("Password Must be at least 6 Characters");
                }

                mAuth.createUserWithEmailAndPassword(inpEmail, inpPass).addOnCompleteListener(new  OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                           Toast.makeText(RegistrationActivity.this, "User is created", Toast.LENGTH_LONG).show();

                           //TODO-- Start your Own Home Activity.
                           startActivity(new Intent(RegistrationActivity.this, ColorChooserActivity.class));
                        }
                        else {
                           Toast.makeText(RegistrationActivity.this,"Error Has Occured"+ task.getException(),Toast.LENGTH_LONG ).show();
                        }
                    }
                });
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currUser = mAuth.getCurrentUser();
         if(currUser != null){

             //TODO-- Start your Own Home Activity.
             startActivity(new Intent(RegistrationActivity.this, ColorChooserActivity.class));
             ///updateUI
         }
    }
}
