package com.example.dutmaintanenceapplication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class tech_profile extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView firstNameTextView, lastNameTextView, emailTextView,roleTextView,departmentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tech_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        firstNameTextView = findViewById(R.id.first_name);
        lastNameTextView = findViewById(R.id.last_name);
        emailTextView = findViewById(R.id.email);
        roleTextView = findViewById(R.id.email);
        departmentTextView = findViewById(R.id.email);

        // Fetch and display technician's information
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DocumentReference userRef = db.collection("users").document(currentUser.getEmail());
            userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if (documentSnapshot.exists()) {
                        // Inside the onSuccess method for fetching user's information
                        String firstName = documentSnapshot.getString("firstname");
                        String lastName = documentSnapshot.getString("surname");
                        String email = documentSnapshot.getId(); // Technician's email
                        String role = documentSnapshot.getString("role");
                        String department = documentSnapshot.getString("department");

                        firstNameTextView.setText(firstName);
                        lastNameTextView.setText(lastName);
                        emailTextView.setText(email);
                        roleTextView.setText(role);
                        departmentTextView.setText(department);

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(tech_profile.this, "Failed to fetch technician's information", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
