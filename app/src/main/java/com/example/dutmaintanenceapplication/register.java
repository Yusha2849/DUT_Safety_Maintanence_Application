package com.example.dutmaintanenceapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dutmaintanenceapplication.Login;
import com.example.dutmaintanenceapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class register extends AppCompatActivity {
    EditText fname, sname, remail, rpassword, rcpassword;
    FirebaseAuth mAuth;
    ProgressBar progb;
    Button regButton;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        fname = findViewById(R.id.firstname);
        sname = findViewById(R.id.surname);
        remail = findViewById(R.id.registeremail);
        rpassword = findViewById(R.id.registerpassword);
        rcpassword = findViewById(R.id.confirmpass);
        progb = findViewById(R.id.progressbar);
        regButton = findViewById(R.id.registerButton);

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progb.setVisibility(View.VISIBLE);
                String firstname = fname.getText().toString().trim();
                String surname = sname.getText().toString().trim();
                String registeremail = remail.getText().toString().trim();
                String regpass = rpassword.getText().toString().trim();
                String confipass = rcpassword.getText().toString().trim();

                if (firstname.isEmpty() || surname.isEmpty() || registeremail.isEmpty() || regpass.isEmpty() || confipass.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                    progb.setVisibility(View.GONE);
                } else if (!regpass.equals(confipass)) {
                    Toast.makeText(getApplicationContext(), "Password and Confirm password not matching", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(registeremail, regpass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progb.setVisibility(View.GONE);
                                    if (task.isSuccessful()) {
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        if (user != null) {
                                            // Create a Firestore document with user's additional information
                                            Map<String, Object> userInfo = new HashMap<>();
                                            userInfo.put("firstname", firstname);
                                            userInfo.put("surname", surname);
                                            userInfo.put("email", registeremail);
                                            userInfo.put("timestamp", System.currentTimeMillis()); // Current timestamp

                                            // Add the document to Firestore
                                            db.collection("users")
                                                    .document(user.getUid())
                                                    .set(userInfo)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                sendEmailVerification();
                                                            } else {
                                                                Toast.makeText(getApplicationContext(), "Failed to create user information", Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                        }
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Registration failed.", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void sendEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Verification email sent.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to send verification email.", Toast.LENGTH_SHORT).show();
                            }
                            // Proceed to login activity
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            finish();
                        }
                    });
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
