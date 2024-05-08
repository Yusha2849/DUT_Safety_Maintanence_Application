package com.example.dutmaintanenceapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.util.ArrayList;
import java.util.List;

public class duties extends AppCompatActivity {


    //I'm assuming  the tech will see the list of issues, issues will be clickable and take the tech to edit duties
    private ImageView mnu;
    private FirebaseFirestore firestore;
    private String currentUserUid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_duties);

        mnu = findViewById(R.id.menu);
        firestore = FirebaseFirestore.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            currentUserUid = currentUser.getUid();
        } else {
            // Handle the case where the user is not logged in
            Toast.makeText(this, "User is not logged in", Toast.LENGTH_SHORT).show();
            finish(); // Finish the activity if the user is not logged in
        }

        fetchAndDisplayReports();

        mnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a PopupMenu
                PopupMenu popupMenu = new PopupMenu(duties.this, mnu);
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

    // Fetch and display reports assigned to the technician
    private void fetchAndDisplayReports() {
        firestore.collection("reports")
                .whereEqualTo("TechnicianID", currentUserUid) // Filter reports by TechnicianId
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> reportList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Retrieve report details
                            String campus = document.getString("campus");
                            String location = document.getString("location");
                            String priority = document.getString("Priority");
                            int upVotes = document.getLong("UpVotes").intValue();

                            // Concatenate report details into a single string
                            String reportDetails = campus + " - " + location + " - " + priority + " - " + upVotes;
                            reportList.add(reportDetails);
                        }

                        // Create an ArrayAdapter to bind the reportList to the ListView
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                                R.layout.list_item_report, R.id.text_report_details, reportList);

                        // Set the adapter for the ListView
                        ListView dutiesListView = findViewById(R.id.dutiesListView);
                        dutiesListView.setAdapter(adapter);

                        // Set item click listener for the ListView
                        dutiesListView.setOnItemClickListener((parent, view, position, id) -> {
                            // Get the selected report details
                            String selectedReportDetails = reportList.get(position);

                            // Launch the edit_duty activity with the selected report details
                            Intent intent = new Intent(duties.this, edit_duty.class);
                            intent.putExtra("selectedReportDetails", selectedReportDetails);
                            startActivity(intent);
                        });

                    } else {
                        // Handle errors
                        Toast.makeText(this, "Failed to fetch reports", Toast.LENGTH_SHORT).show();
                    }
                });
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, tech_home.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}

