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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
TextView btn;
Button btnRegister;
EditText inputUsername,inputEmail,inputPassword,inputConfirmPassword;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btn = findViewById(R.id.alreadyHaveAccout);
        inputUsername = findViewById(R.id.inputUsername);
        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        btn.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startActivity(new Intent(RegisterActivity.this, NotesActivity.class));
            finish();
        }


    }


    private void checkCrededentials() {
        String username= inputUsername.getText().toString();
        String email= inputEmail.getText().toString();
        String password= inputPassword.getText().toString();
        String confirmPassword= inputConfirmPassword.getText().toString();

        if(username.isEmpty())
        {
            showError(inputUsername,"Username is not valid!");
        }
        else if(email.isEmpty() || !email.contains("@") || !email.contains(".") )
        {
            showError(inputEmail,"Email is not valid! Email should contain @");
        }
        else if (password.isEmpty() || password.length()<7)
        {
            showError(inputPassword,"Password must contain at least 7 characters");
        }
        else if (confirmPassword.isEmpty() || !confirmPassword.equals(password))
        {
            showError(inputConfirmPassword,"Password is not match!");
        }
        else
        {

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener((task) -> {
                if (task.isSuccessful()) {

                    FirebaseDatabase.getInstance().getReference("User/" + FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(new User(inputUsername.getText().toString(), inputEmail.getText().toString(),inputPassword.getText().toString()));

                    Toast.makeText(RegisterActivity.this, "Signed up successful", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));


                } else {
                    Toast.makeText(RegisterActivity.this, "Error: "+task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                }
            });







        }
    }

    private void showError(EditText input, String s) {
        input.setError(s);
        input.requestFocus();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.alreadyHaveAccout:
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnRegister:
                checkCrededentials();

        }
    }
}