package com.example.listapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
TextView changeToRegister;
TextView forgotPassword;
private EditText inputEmail,inputPassword;
Button btnLogin;
ProgressBar progressBarLogin;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        progressBarLogin=findViewById(R.id.progressbaroflogin);
        inputEmail= findViewById(R.id.inputEmail);
        inputPassword= findViewById(R.id.inputPassword);
        btnLogin = findViewById(R.id.btnVerify);
        btnLogin.setOnClickListener(this);
        changeToRegister = findViewById(R.id.textViewSignUp);
        changeToRegister.setOnClickListener(this::onSwitch);
        forgotPassword = findViewById(R.id.forgotPassword);
        forgotPassword.setOnClickListener(this);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, NotesActivity.class));
            finish();
        }

    }



    private void checkCrededentials() {
        String email= inputEmail.getText().toString();
        String password= inputPassword.getText().toString();

        if(email.isEmpty() || !email.contains("@") || !email.contains("."))
        {
            showError(inputEmail,"Email is not valid!");
        }
        else if (password.isEmpty() || password.length()<7)
        {
            showError(inputPassword,"Password must contain at least 7 characters");
        }
        else
        {
            progressBarLogin.setVisibility(View.VISIBLE);

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).addOnCompleteListener((task) -> {
                if (task.isSuccessful()) {
                    startActivity(new Intent(LoginActivity.this, NotesActivity.class));
                    Toast.makeText(LoginActivity.this, "Logged in successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Error: " + task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    progressBarLogin.setVisibility(View.INVISIBLE);
                }

            });
        }
    }



    public void onSwitch(View v){
        Intent myIntent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(myIntent);
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnVerify:
                checkCrededentials();
                break;
            case R.id.forgotPassword:
                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
        }
    }

}
