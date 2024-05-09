package com.example.dutmaintanenceapplication;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import com.example.dutmaintanenceapplication.MyCustomAdapter;

public class FaultQueue extends AppCompatActivity {

    private ImageView mnu;
    private static final String TAG = "Fault";
    private ListView faultListView;
    private ArrayList<HashMap<String, Object>> reportDataList; // Stores report data

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_queue);

        reportDataList = new ArrayList<>(); // Initialize report data list
        faultListView = findViewById(R.id.faultListView);
        mnu = findViewById(R.id.menu);

        mnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(FaultQueue.this, mnu);
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



        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve report data
        // Retrieve report data with status "Pending"
        db.collection("reports")
                .whereEqualTo("IssueStatus", "Pending")
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
                Intent intent = new Intent(FaultQueue.this, view_issue.class);
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

    // Create a custom adapter class to format list items (implementation details omitted)



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}