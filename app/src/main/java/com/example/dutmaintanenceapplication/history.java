package com.example.dutmaintanenceapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

public class history extends AppCompatActivity {

    private ListView faultListView;
    private static final String TAG = "FaultQueue";
    private ArrayList<HashMap<String, Object>> reportDataList; // Stores report data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_queue);

        reportDataList = new ArrayList<>(); // Initialize report data list
        faultListView = findViewById(R.id.faultListView);

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve report data
        // Retrieve report data with status "Pending"
        db.collection("reports")
                .whereEqualTo("IssueStatus", "Complete")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            renderReport(document);
                        }
                        updateListView(); // Update ListView after data retrieval
                    } else {
                        Log.w(TAG, "Error getting reports.", task.getException());
                    }
                });


        // Set click listener for ListView items
        faultListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Get the clicked report data
                HashMap<String, Object> clickedReport = reportDataList.get(position);

                // Start a new activity to show full details
                Intent intent = new Intent(history.this, view_issue.class);
                intent.putExtra("reportData", clickedReport); // Pass report data to new activity
                startActivity(intent);
            }
        });
    }

    private void renderReport(QueryDocumentSnapshot report) {
        // Extract report data (consider including all relevant fields)
        String reportSummary = "Campus: " + report.getString("campus") + "\n" +
                "Location: " + report.getString("location") + "\n" +
                "Block: " + report.getString("block") + "\n" +
                "Issue Type: " + report.getString("issueType");

        HashMap<String, Object> reportData = new HashMap<>();
        reportData.put("campus", report.getString("campus"));
        reportData.put("location", report.getString("location"));
        reportData.put("block", report.getString("block"));
        reportData.put("issueType", report.getString("issueType"));
        reportData.put("description", report.getString("description")); // Add description
        reportData.put("UpVotes", String.valueOf(report.getLong("UpVotes")));
        reportData.put("imageUrl", report.getString("imageUrl"));
        reportData.put("safetyTips", report.getString("safetyTips"));

        reportDataList.add(reportData); // Add report data to the list
    }

    private void updateListView() {
        // Create or update the ListView adapter
        ArrayAdapter<HashMap<String, Object>> adapter = new MyCustomAdapter(this, reportDataList);
        faultListView.setAdapter(adapter);
    }

    // Adjust onBackPressed to go back to home
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
