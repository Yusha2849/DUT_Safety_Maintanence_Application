package com.example.dutmaintanenceapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class edit_duty extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private ImageView attachedImageView;
    private Button submitButton, attachImageButton;
    private FirebaseFirestore firestore;
    private StorageReference storageRef;
    private String imageUrl; // Declaring imageUrl globally

    private TextView campusTextView;
    private TextView locationTextView;
    private TextView buildingTextView;
    private TextView issuetypeTextView;
    private TextView descTextView;
    private TextView currentVotesTextView;
    private TextView safetyTipsTextView;
    private ImageView imageIssue;
    private TextView reportdateTextView;
    private TextView feedbackTextView;
    private FirebaseFirestore db;
    private ImageView mnu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_duty);

        // Initialize Firebase Storage
        firestore = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        mnu = findViewById(R.id.menu);
        db = FirebaseFirestore.getInstance();
        reportdateTextView = findViewById(R.id.dateReport);
        campusTextView = findViewById(R.id.campus);
        locationTextView = findViewById(R.id.location);
        buildingTextView = findViewById(R.id.building);
        issuetypeTextView = findViewById(R.id.issuetype);
        descTextView = findViewById(R.id.desc);
        currentVotesTextView = findViewById(R.id.currentVotes);
        safetyTipsTextView = findViewById(R.id.safetytips);
        imageIssue = findViewById(R.id.imageIssue); // Update if the image view ID is different
        attachedImageView = findViewById(R.id.imageComplete); //Displays the image taken
        attachImageButton = findViewById(R.id.attachImageButton); //Launches camera
        feedbackTextView= findViewById(R.id.feedback);

        submitButton = findViewById(R.id.submitButton);

        // Get the current date
        Calendar calendar = Calendar.getInstance();
        // Create a SimpleDateFormat object with the desired date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        // Format the current date
        String currentDate = dateFormat.format(calendar.getTime());
        // Set the formatted date to your TextView
        reportdateTextView.setText(currentDate);

        // Get the report data passed from the Intent
        Intent intent = getIntent();
        HashMap<String, String> reportData = (HashMap<String, String>) intent.getSerializableExtra("reportData");

        // Display report data
        if (reportData != null) {
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

        attachImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the required permissions are granted
                if (ContextCompat.checkSelfPermission(edit_duty.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Request camera permission if not granted
                    ActivityCompat.requestPermissions(edit_duty.this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                } else {
                    // Permission is granted, launch the camera intent
                    dispatchTakePictureIntent();
                }
            }
        });

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String safetyTips = safetyTipsTextView.getText().toString().trim();
                if (safetyTips.isEmpty()) {
                    // Display a message to the user if safety tips are empty
                    Toast.makeText(edit_duty.this, "Please generate safety tips", Toast.LENGTH_SHORT).show();
                } else {
                    // Proceed with saving the report
                    uploadImageAndSaveReport();
                    // Upload image to Firebase Storage and save report data

                }
            }
        });

        mnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a PopupMenu
                PopupMenu popupMenu = new PopupMenu(edit_duty.this, mnu);
                popupMenu.getMenuInflater().inflate(R.menu.tech_menu, popupMenu.getMenu());

                // Set item click listener for the menu items
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Handle menu item clicks
                        int itemId = item.getItemId();
                        if (itemId == R.id.profile) {
                            Toast.makeText(getApplicationContext(), "My profile", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), tech_profile.class);
                            startActivity(intent);
                            return true;
                        } else if (itemId == R.id.faulthistory) {
                            Toast.makeText(getApplicationContext(), "Fault history", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), tech_history.class);
                            startActivity(intent);
                            return true;
                        } else if (itemId == R.id.duties) {
                            Toast.makeText(getApplicationContext(), "My duties", Toast.LENGTH_SHORT).show();
                           Intent intent = new Intent(getApplicationContext(), duties.class);
                            startActivity(intent);
                            return true;
                        } else if (itemId == R.id.signout) {
                            // Handle menu item 4 click
                            Toast.makeText(getApplicationContext(), "You have been Logged out", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            return true;
                        }
                        // Add more cases for additional menu items if needed
                        return false;
                    }
                });

                // Show the PopupMenu
                popupMenu.show();
            }
        });


    }

    //Camera function Start
    private void dispatchTakePictureIntent() {
        // Create an intent to capture an image
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Check if there is an activity available to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Start the activity to capture an image and wait for the result
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Image capture was successful, retrieve the image and display it
            Bundle extras = data.getExtras();
            if (extras != null) {
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                if (imageBitmap != null) {
                    // Set the captured image to the attachedImageView
                    attachedImageView.setImageBitmap(imageBitmap);
                    // Make the attachedImageView visible
                    attachedImageView.setVisibility(View.VISIBLE);
                    // You may want to save the image for later use or upload it to a server
                } else {
                    Toast.makeText(this, "Failed to capture image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_IMAGE_CAPTURE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Camera permission granted, launch the camera intent
                dispatchTakePictureIntent();
            } else {
                // Camera permission denied, show a message or handle it accordingly
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Camera Function End

    // Method to upload image and save report data
    private void uploadImageAndSaveReport() {
        attachedImageView.setDrawingCacheEnabled(true);
        attachedImageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) attachedImageView.getDrawable()).getBitmap();

        StorageReference imageRef = storageRef.child("images/" + System.currentTimeMillis() + ".jpg");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageData = baos.toByteArray();

        UploadTask uploadTask = imageRef.putBytes(imageData);
        uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }
                return imageRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();
                    imageUrl = downloadUri.toString(); // Assigning imageUrl here
                    saveReport(); // Call saveReport() after imageUrl is obtained
                } else {
                    Toast.makeText(edit_duty.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void saveReport() {
        String feedback = feedbackTextView.getText().toString().trim();
        String date = reportdateTextView.getText().toString().trim(); // Retrieve Current Date

        // Get the report data passed from the Intent
        Intent intent = getIntent();
        HashMap<String, String> reportData = (HashMap<String, String>) intent.getSerializableExtra("reportData");

        if (reportData != null) {
            String documentId = reportData.get("documentId"); // Assuming you have the document ID stored in reportData
            if (documentId != null && !documentId.isEmpty()) {
                // Update the existing document with feedback and feedback date
                firestore.collection("reports")
                        .document(documentId)
                        .update("Feedback", feedback,
                                "Feedback Date", date)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(edit_duty.this, "Report updated successfully", Toast.LENGTH_SHORT).show();
                            // Navigate to Home class after successful update
                            Intent homeIntent = new Intent(edit_duty.this, home.class);
                            startActivity(homeIntent);
                            finish(); // Finish the current activity to prevent going back to it from Home
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(edit_duty.this, "Failed to update report", Toast.LENGTH_SHORT).show();
                        });
            } else {
                Toast.makeText(edit_duty.this, "No report data found", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(edit_duty.this, "No report data found", Toast.LENGTH_SHORT).show();
        }
    }

}