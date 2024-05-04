package com.example.AntenatalReminders.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.AntenatalReminders.HomeActivity;
import com.example.AntenatalReminders.R;
import com.example.AntenatalReminders.databinding.ActivityLoginBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private LoginViewModel loginViewModel;
    private ActivityLoginBinding binding;
    private FirebaseAuth mAuth;
    TextView emailTV;
    TextView passwordTV;
    Button  btnTV;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        setContentView(R.layout.activity_login);

        emailTV = findViewById(R.id.emailAddress);
        passwordTV = findViewById(R.id.textPassword);
        btnTV = findViewById(R.id.loginBtn);

//        if(mAuth.getCurrentUser()!=null){
//            loadRegistration();
//        }


        btnTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserAccount();
            }
        });


    }
    public void loginUserAccount(){
        String email, password;
        email= emailTV.getText().toString();
        password= passwordTV.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Please enter your email", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(getApplicationContext(), "Please Enter your password", Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.isComplete()){
                        loadRegistration();
                    }
                }).addOnFailureListener(e -> {
                    Log.d(TAG, "loginUserAccount: "+e.getMessage());
                });


    }
    public void  loadRegistration(){
        Intent i = new Intent(this, HomeActivity.class);
        Log.d(TAG, "logIntent "+i);
        startActivity(i);
        this.finish();
    }



}