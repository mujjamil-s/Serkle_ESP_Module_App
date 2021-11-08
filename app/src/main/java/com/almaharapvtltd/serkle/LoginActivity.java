package com.almaharapvtltd.serkle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private TextInputLayout log_email,log_pass;
    private MaterialButton log_sign_in_btn;

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        log_email = findViewById(R.id.login_email);
        log_pass = findViewById(R.id.login_password);
        log_sign_in_btn = findViewById(R.id.button);

        mAuth = FirebaseAuth.getInstance();
        log_sign_in_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inpEmail = log_email.getEditText().getText().toString().trim();
                String inpPass = log_pass.getEditText().getText().toString().trim();

                if(TextUtils.isEmpty(inpEmail)){
                    log_email.setError("Email is required.");
                }
                if(TextUtils.isEmpty(inpPass)){
                    log_pass.setError("Password is required");
                }
                if(inpPass.length() <6){
                    log_pass.setError("Password Must be at least 6 Characters");
                }

                mAuth.signInWithEmailAndPassword(inpEmail, inpPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(LoginActivity.this,"Successfully Logged In", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(LoginActivity.this,ColorChooserActivity.class));
                        }else{
                            Toast.makeText(LoginActivity.this,"Error: " + task.getException(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }
        });

    }
}