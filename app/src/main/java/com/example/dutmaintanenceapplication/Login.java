package com.example.dutmaintanenceapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    TextView forgot, registerPage;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    EditText email, password;
    Button loginB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ProgressBar progressBar = findViewById(R.id.progressbar);
        mAuth = FirebaseAuth.getInstance();
        forgot = findViewById(R.id.forgot);
        registerPage = findViewById(R.id.registerTextView);
        email = findViewById(R.id.loginemail);
        password = findViewById(R.id.loginpassword);
        loginB = findViewById(R.id.loginButton);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch the forgot password activity
                Intent intent = new Intent(Login.this, forgotpassword.class);
                startActivity(intent);
                finish();
            }
        });

        registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, register.class);
                startActivity(intent);
                finish();
            }
        });

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = email.getText().toString().trim();
                String userPassword = password.getText().toString().trim();

                if (userEmail.isEmpty() || userPassword.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);

                // Attempt to sign in with Firebase authentication
                mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Firebase authentication succeeded
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null && user.isEmailVerified()) {
                                        // Check the role based on email domain
                                        String emailDomain = userEmail.substring(userEmail.lastIndexOf("@") + 1);
                                        if (emailDomain.equals("gmail.com")) {
                                            // Technician login successful
                                            Toast.makeText(Login.this, "Technician Authentication Succeeded", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), tech_home.class);
                                            startActivity(intent);
                                        } else if (emailDomain.equals("dut4life.ac.za")) {
                                            // DUTCommunity login successful
                                            Toast.makeText(Login.this, "DUTCommunity Authentication Succeeded", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(getApplicationContext(), home.class);
                                            startActivity(intent);
                                        } else {
                                            // Other domain, handle accordingly
                                            Toast.makeText(Login.this, "Unknown domain. Please contact support.", Toast.LENGTH_SHORT).show();
                                        }
                                        finish();
                                    } else {
                                        Toast.makeText(Login.this, "Please verify your email before logging in", Toast.LENGTH_SHORT).show();
                                        mAuth.signOut(); // Sign out the user to prevent access without verification
                                    }
                                } else {
                                    // Firebase authentication failed, show error message
                                    progressBar.setVisibility(View.GONE);
                                    Toast.makeText(Login.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
