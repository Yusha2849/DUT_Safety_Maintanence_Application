package com.example.dutmaintanenceapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class report extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView mnu, attachedImageView;
    private Spinner campusSpinner, blockSpinner, issueTypeSpinner;
    private ArrayAdapter<CharSequence> campusAdapter, blockAdapter, issueTypeAdapter;
    private TextInputEditText locationEditText, descriptionEditText;

    private TextView safetyTipsTextView, currentDateTextView;
    private Button submitButton, safteyTipsButton, attachImageButton;

    private Chat2Point0 chatBot;

    // Define Firebase Storage reference
    private FirebaseFirestore firestore;
    private StorageReference storageRef;
    private String imageUrl; // Declaring imageUrl globally

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        // Initialize Firebase Storage
        firestore = FirebaseFirestore.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();

        mnu = findViewById(R.id.menu);
        campusSpinner = findViewById(R.id.campusSpinner);
        blockSpinner = findViewById(R.id.blockTypeSpinner);
        issueTypeSpinner = findViewById(R.id.issueTypeSpinner);
        locationEditText = findViewById(R.id.location);
        descriptionEditText = findViewById(R.id.description); //given to AI
        safetyTipsTextView = findViewById(R.id.safetyTipsTextView); //Displays response from AI
        safteyTipsButton = findViewById(R.id.safteyTipsButton); //Sends Request to AI
        attachedImageView = findViewById(R.id.attachedImageView); //Displays the image taken
        attachImageButton = findViewById(R.id.attachImageButton); //Launches camera
        submitButton = findViewById(R.id.submitButton);
        chatBot = new Chat2Point0(safetyTipsTextView); // Links UI to AI Class
        currentDateTextView = findViewById(R.id.dateDispLabel); // Assuming you have a TextView for displaying the current date

        // Get the current date
        Calendar calendar = Calendar.getInstance();
        // Create a SimpleDateFormat object with the desired date format
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        // Format the current date
        String currentDate = dateFormat.format(calendar.getTime());
        // Set the formatted date to your TextView
        currentDateTextView.setText(currentDate);

        campusAdapter = ArrayAdapter.createFromResource(this,
                R.array.campus, android.R.layout.simple_spinner_item);
        blockAdapter = ArrayAdapter.createFromResource(this,
                R.array.RitsonBlock, android.R.layout.simple_spinner_item);
        issueTypeAdapter = ArrayAdapter.createFromResource(this,
                R.array.fault_types_array, android.R.layout.simple_spinner_item);

        campusAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        blockAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        issueTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        campusSpinner.setAdapter(campusAdapter);
        blockSpinner.setAdapter(blockAdapter);
        issueTypeSpinner.setAdapter(issueTypeAdapter);

        // Function of Generate Tips Button
        safteyTipsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = descriptionEditText.getText().toString();
                if (!message.isEmpty()) {
                    chatBot.getGPTResponse(message);
                } else {
                    safetyTipsTextView.setText("Please enter a hazard description.");
                }
            }
        });

        attachImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check if the required permissions are granted
                if (ContextCompat.checkSelfPermission(report.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Request camera permission if not granted
                    ActivityCompat.requestPermissions(report.this, new String[]{Manifest.permission.CAMERA}, REQUEST_IMAGE_CAPTURE);
                } else {
                    // Permission is granted, launch the camera intent
                    dispatchTakePictureIntent();
                }
            }
        });


        // Submit button click listener
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String safetyTips = safetyTipsTextView.getText().toString().trim();
                if (safetyTips.isEmpty()) {
                    // Display a message to the user if safety tips are empty
                    Toast.makeText(report.this, "Please generate safety tips", Toast.LENGTH_SHORT).show();
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

                PopupMenu popupMenu = new PopupMenu(report.this, mnu);
                popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Handle menu item clicks
                        int itemId = item.getItemId();
                        if (itemId == R.id.account) {
                            Toast.makeText(getApplicationContext(), "Account", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), profile.class);
                            startActivity(intent);
                            return true;
                        } else if (itemId == R.id.faultyhistory) {
                            Toast.makeText(getApplicationContext(), "History", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), history.class);
                            startActivity(intent);
                            return true;
                        }
                        else if (itemId == R.id.logfaulty) {
                            Toast.makeText(getApplicationContext(), "Log Faulty", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), report.class);
                            startActivity(intent);
                            return true;
                        } else if (itemId == R.id.pendingfault) {
                            Toast.makeText(getApplicationContext(), "Faulty Queue", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), view_issue.class);
                            startActivity(intent);
                            return true;
                        } else if (itemId == R.id.signout) {
                            Toast.makeText(getApplicationContext(), "You have been Logged out", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            return true;
                        }
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
                    Toast.makeText(report.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private String getUserId() {
        // Get the current user ID using Firebase Authentication
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            return user.getUid();
        } else {
            // Handle the case where the user is not logged in
            // For example, display a message or handle it according to your app's logic
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    private void saveReport() {
        String userId = getUserId();

        String campus = campusSpinner.getSelectedItem().toString();
        String location = locationEditText.getText().toString().trim();
        String block = blockSpinner.getSelectedItem().toString();
        String issueType = issueTypeSpinner.getSelectedItem().toString();
        String description = descriptionEditText.getText().toString().trim();
        String safetyTips = safetyTipsTextView.getText().toString().trim(); // Retrieve safety tips text
        String date = currentDateTextView.getText().toString().trim(); // Retrieve Current Date

        // Default values
        String issueStatus = "Pending";
        int upVotes = 0;
        String priority = "Low";


        if (location.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save report data to Firestore
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("Date", date);
        reportData.put("campus", campus);
        reportData.put("location", location);
        reportData.put("block", block);
        reportData.put("issueType", issueType);
        reportData.put("description", description);
        reportData.put("safetyTips", safetyTips); // Add safety tips to report data
        reportData.put("IssueStatus", issueStatus); // Add IssueStatus with default value
        reportData.put("UpVotes", upVotes); // Add UpVotes with default value
        reportData.put("Priority", priority); // Add Priority with default value
        reportData.put("imageUrl", imageUrl); // Add image URL to report data
        reportData.put("userId", userId); // Add user ID to report data

        firestore.collection("reports")
                .add(reportData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(report.this, "Report submitted successfully", Toast.LENGTH_SHORT).show();

                    // Navigate to Home class after successful submission
                    Intent intent = new Intent(report.this, home.class);
                    startActivity(intent);
                    finish(); // Finish the current activity to prevent going back to it from Home
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(report.this, "Failed to submit report", Toast.LENGTH_SHORT).show();
                });
    }
}
