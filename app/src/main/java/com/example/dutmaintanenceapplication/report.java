package com.example.dutmaintanenceapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class report extends AppCompatActivity {
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
