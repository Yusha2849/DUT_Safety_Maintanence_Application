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

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class report extends AppCompatActivity {
    private ImageView mnu;
    private Spinner campusSpinner, blockSpinner, issueTypeSpinner;
    private ArrayAdapter<CharSequence> campusAdapter, blockAdapter, issueTypeAdapter;
    private ReportDatabaseHelper databaseHelper;
    private TextInputEditText locationEditText, descriptionEditText;
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        EdgeToEdge.enable(this);

        // Initialize database helper
        databaseHelper = new ReportDatabaseHelper(this);
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
                // Create a PopupMenu
                PopupMenu popupMenu = new PopupMenu(report.this, mnu);
                popupMenu.getMenuInflater().inflate(R.menu.menu_main, popupMenu.getMenu());

                // Set item click listener for the menu items
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        // Handle menu item clicks
                        int itemId = item.getItemId();
                        if (itemId == R.id.account) {
                            // Handle menu item 1 click
                            Toast.makeText(getApplicationContext(), "Account", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), profile.class);
                            startActivity(intent);
                            return true;
                        } else if (itemId == R.id.faulthistory) {
                            // Handle menu item 2 click
                            Toast.makeText(getApplicationContext(), "Faulty History", Toast.LENGTH_SHORT).show();
                            /*Intent intent = new Intent(getApplicationContext(), ClinicBooking.class);
                            startActivity(intent);
                            return true;*/
                        }
                        else if (itemId == R.id.logfaulty) {
                            Toast.makeText(getApplicationContext(), "Log Faulty", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), report.class);
                            startActivity(intent);
                            return true;
                        } else if (itemId == R.id.pendingfault) {
                            // Handle menu item 3 click
                            Toast.makeText(getApplicationContext(), "My Profile", Toast.LENGTH_SHORT).show();
                            /*Intent intent = new Intent(getApplicationContext(), MyProfile.class);
                            startActivity(intent);
                            return true;*/
                        }else if (itemId == R.id.settings) {
                            // Handle menu item 3 click
                            Toast.makeText(getApplicationContext(), "Setting", Toast.LENGTH_SHORT).show();
                            /*Intent intent = new Intent(getApplicationContext(), MyProfile.class);
                            startActivity(intent);
                            return true;*/
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

    // Method to save report when submit button is clicked
    private void saveReport() {
        String campus = campusSpinner.getSelectedItem().toString();
        String location = locationEditText.getText().toString();
        String block = blockSpinner.getSelectedItem().toString();
        String issueType = issueTypeSpinner.getSelectedItem().toString();
        String description = descriptionEditText.getText().toString();

        long newRowId = databaseHelper.addReport(campus, location, block, issueType, description);
        if (newRowId != -1) {
            Toast.makeText(this, "Report submitted successfully", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to submit report", Toast.LENGTH_SHORT).show();
        }
    }
}
