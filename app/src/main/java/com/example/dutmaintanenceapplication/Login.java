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
                progressBar.setVisibility(View.VISIBLE);
                String userEmail = String.valueOf(email.getText());
                String userPassword = String.valueOf(password.getText());

                mAuth.signInWithEmailAndPassword(userEmail, userPassword)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    if (user != null && user.isEmailVerified()) {
                                        Toast.makeText(getApplicationContext(), "Authentication Succeeded", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), report.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(Login.this, "Please verify your email before logging in", Toast.LENGTH_SHORT).show();
                                        mAuth.signOut(); // Sign out the user to prevent access without verification
                                    }
                                } else if (userEmail.isEmpty() || userPassword.isEmpty()) {
                                    Toast.makeText(Login.this, "Email or Password Shouldn't Be Empty", Toast.LENGTH_SHORT).show();
                                } else {
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
