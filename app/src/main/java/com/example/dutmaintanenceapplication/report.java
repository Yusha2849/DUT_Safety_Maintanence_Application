package com.example.dutmaintanenceapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class report extends AppCompatActivity {
    private ImageView mnu;
    private Spinner campusSpinner, blockSpinner, issueTypeSpinner;
    private ArrayAdapter<CharSequence> campusAdapter, blockAdapter, issueTypeAdapter;
    private TextInputEditText locationEditText, descriptionEditText;
    private Button submitButton;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        firestore = FirebaseFirestore.getInstance();

        mnu = findViewById(R.id.menu);
        campusSpinner = findViewById(R.id.campusSpinner);
        blockSpinner = findViewById(R.id.blockTypeSpinner);
        issueTypeSpinner = findViewById(R.id.issueTypeSpinner);
        locationEditText = findViewById(R.id.location);
        descriptionEditText = findViewById(R.id.description);
        submitButton = findViewById(R.id.submitButton);

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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveReport();
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
                        int itemId = item.getItemId();
                        if (itemId == R.id.account) {
                            Toast.makeText(getApplicationContext(), "Account", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), profile.class);
                            startActivity(intent);
                            return true;
                        } else if (itemId == R.id.faulthistory) {
                            Toast.makeText(getApplicationContext(), "Faulty History", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.logfaulty) {
                            Toast.makeText(getApplicationContext(), "Log Faulty", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), report.class);
                            startActivity(intent);
                            return true;
                        } else if (itemId == R.id.pendingfault) {
                            Toast.makeText(getApplicationContext(), "Pending faulty", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), FaultQueue.class);
                            startActivity(intent);
                            return true;
                        } else if (itemId == R.id.settings) {
                            Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_SHORT).show();
                        } else if (itemId == R.id.signout) {
                            Toast.makeText(getApplicationContext(), "You have been Logged out", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), Login.class);
                            startActivity(intent);
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        });
    }

    private void saveReport() {
        String campus = campusSpinner.getSelectedItem().toString();
        String location = locationEditText.getText().toString().trim();
        String block = blockSpinner.getSelectedItem().toString();
        String issueType = issueTypeSpinner.getSelectedItem().toString();
        String description = descriptionEditText.getText().toString().trim();

        if (location.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Save report data to Firestore
        Map<String, Object> reportData = new HashMap<>();
        reportData.put("campus", campus);
        reportData.put("location", location);
        reportData.put("block", block);
        reportData.put("issueType", issueType);
        reportData.put("description", description);

        firestore.collection("reports")
                .add(reportData)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(report.this, "Report submitted successfully", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(report.this, "Failed to submit report", Toast.LENGTH_SHORT).show();
                });
    }
}
