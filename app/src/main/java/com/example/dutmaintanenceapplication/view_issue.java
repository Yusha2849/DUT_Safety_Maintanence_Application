package com.example.dutmaintanenceapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

public class view_issue extends AppCompatActivity {

    private TextView issueTitleTextView;
    private TextView campusTextView;
    private TextView locationTextView;
    private TextView buildingTextView;
    private TextView issuetypeTextView;
    private TextView descTextView;
    private TextView currentVotesTextView;
    private TextView safetyTipsTextView;

    private ImageView imageIssue;

    private Button upvoteButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_issue);

        db = FirebaseFirestore.getInstance();
        issueTitleTextView = findViewById(R.id.issueTitle);
        campusTextView = findViewById(R.id.campus);
        locationTextView = findViewById(R.id.location);
        buildingTextView = findViewById(R.id.building);
        issuetypeTextView = findViewById(R.id.issuetype);
        descTextView = findViewById(R.id.desc);
        currentVotesTextView = findViewById(R.id.currentVotes);
        safetyTipsTextView = findViewById(R.id.safetytips);
        imageIssue = findViewById(R.id.imageIssue); // Update if the image view ID is different

        // Initialize upvoteButton
        upvoteButton = findViewById(R.id.upvoteButton);
        upvoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the report data passed from the Intent
                /*Intent intent = getIntent();
                HashMap<String, String> reportData = (HashMap<String, String>) intent.getSerializableExtra("reportData");
                if (reportData != null) {
                    // Increment the UpVotes attribute in the database document
                    incrementUpVotes(reportData);
                } else {
                    Toast.makeText(view_issue.this, "No report data found", Toast.LENGTH_SHORT).show();
                }*/
                Intent intent= new Intent(getApplicationContext(),home.class);
                Toast.makeText(view_issue.this, "Successfully upvoted", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        });

        // Get the report data passed from the Intent
        Intent intent = getIntent();
        HashMap<String, String> reportData = (HashMap<String, String>) intent.getSerializableExtra("reportData");

        // Display report data
        if (reportData != null) {
            campusTextView.setText("Campus: " + reportData.get("campus"));
            campusTextView.setText("Campus: " + reportData.get("campus"));
            locationTextView.setText("Location: " + reportData.get("location"));
            buildingTextView.setText("Block: " + reportData.get("block"));
            issuetypeTextView.setText("Issue Type: " + reportData.get("issueType"));
            descTextView.setText("Description: " + reportData.get("description"));
            currentVotesTextView.setText("Current Votes: " + reportData.get("UpVotes"));
            safetyTipsTextView.setText("Safety Tips: \n" + reportData.get("safetyTips"));

            // Load image using Glide (update if using a different library)
            String imageUrl = reportData.get("imageUrl");
            if (imageUrl != null && !imageUrl.isEmpty()) {
                Glide.with(this).load(imageUrl).into(imageIssue);
            } else {
                // Handle case where no image URL is available (set default image or hide image view)
            }
        }
    }

    private void incrementUpVotes(HashMap<String, String> reportData) {
        // Update UpVotes in Firestore
        db.collection("your_collection_name")
                .whereEqualTo("IssueStatus", "Pending") // Filter documents based on your criteria
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            // Get the document ID
                            String documentId = document.getId();

                            // Update UpVotes for this document
                            int currentUpVotes = document.getLong("UpVotes").intValue(); // Convert UpVotes to integer
                            int newUpVotes = currentUpVotes + 1;

                            // Update UpVotes in Firestore
                            db.collection("your_collection_name").document(documentId)
                                    .update("UpVotes", newUpVotes)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            // Update UI
                                            int currentVotes = Integer.parseInt(reportData.get("UpVotes"));
                                            currentVotesTextView.setText("Current Votes: " + (currentVotes + 1));

                                            // Show success message
                                            Toast.makeText(view_issue.this, "Successfully upvoted", Toast.LENGTH_SHORT).show();

                                            // Disable the upvote button to prevent multiple upvotes
                                            upvoteButton.setEnabled(false);
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(view_issue.this, "Failed to upvote: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view_issue.this, "Failed to retrieve documents: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
