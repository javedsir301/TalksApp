package com.example.whatsapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.whatsapp.Models.Users;
import com.example.whatsapp.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    private FirebaseAuth auth;
    FirebaseDatabase database;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setTitle("Creating Account");
        progressDialog.setMessage("We're creating your Account");



        binding.signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (binding.userNameEdittext.getText().toString().isEmpty()){
                    binding.userNameEdittext.setError("Enter Your Username");
                    return;
                }

                if (binding.emailEditText.getText().toString().isEmpty()){
                    binding.emailEditText.setError("Enter Your Email");
                    return;
                }

                if (binding.passwordEdittext.getText().toString().isEmpty()){
                    binding.passwordEdittext.setError("Enter Your Password");
                    return;
                }


                progressDialog.show();
                auth.createUserWithEmailAndPassword(binding.emailEditText.getText().toString(),
                        binding.passwordEdittext.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                                progressDialog.dismiss();
                        if (task.isSuccessful()){
                            Users user = new Users(binding.userNameEdittext.getText().toString(), binding.emailEditText.getText().toString(),
                                    binding.passwordEdittext.getText().toString());

                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(user);


                            Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                            startActivity(intent);

                        }else
                        {
                            Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                        }

                    }
                });

            }
        });

        binding.alreadyAccountEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });



    }
}