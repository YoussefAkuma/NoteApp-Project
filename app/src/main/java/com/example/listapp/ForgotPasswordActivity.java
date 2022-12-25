package com.example.listapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {


    EditText inputVerifEmail;
    Button btnVerify;
    TextView GoBackToLogin;
    FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_passord);
        auth= FirebaseAuth.getInstance();

        inputVerifEmail= findViewById(R.id.inputVerifEmail);
        GoBackToLogin= findViewById(R.id.GoToLogin);
        GoBackToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ForgotPasswordActivity.this,LoginActivity.class));
            }
        });

        btnVerify= findViewById(R.id.btnVerify);
        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Validate();
            }


        });
    }

    private void Validate() {
    String email = inputVerifEmail.getText().toString();
    if (email.isEmpty() || !email.contains("@") || !email.contains(".")  ){
        showError(inputVerifEmail,"Email is not valid!");
    } else
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(ForgotPasswordActivity.this,"Verification Mail is Sent", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(ForgotPasswordActivity.this, LoginActivity.class));
                        }else
                            Toast.makeText(ForgotPasswordActivity.this,"Error : "+task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }



    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }
}