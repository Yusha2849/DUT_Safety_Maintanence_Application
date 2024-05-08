package com.example.dutmaintanenceapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class FaultQueue extends AppCompatActivity {

    private ImageView mnu;

    private static final String TAG = "Fault";
    private TableLayout reportTable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_queue);

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

        //reportTable = findViewById(R.id.reportTable);

        // Initialize Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Retrieve report data
        db.collection("reports")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            renderReport(document);
                        }
                    } else {
                        Log.w(TAG, "Error getting reports.", task.getException());
                    }
                });
    }

    private void renderReport(QueryDocumentSnapshot report) {
        TableRow row = new TableRow(this);
        TableLayout.LayoutParams layoutParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT
        );
        row.setLayoutParams(layoutParams);

        TextView campusTextView = new TextView(this);
        TextView locationTextView = new TextView(this);
        TextView buildingTextView = new TextView(this);
        TextView issueTypeTextView = new TextView(this);
        TextView descTextView = new TextView(this);
        TextView currentVotesTextView = new TextView(this);

        campusTextView.setText(report.getString("campus"));
        locationTextView.setText(report.getString("location"));
        buildingTextView.setText(report.getString("building"));
        issueTypeTextView.setText(report.getString("issueType"));
        descTextView.setText(report.getString("description"));
        currentVotesTextView.setText(String.valueOf(report.getLong("currentVotes")));

        row.addView(campusTextView);
        row.addView(locationTextView);
        row.addView(buildingTextView);
        row.addView(issueTypeTextView);
        row.addView(descTextView);
        row.addView(currentVotesTextView);

        reportTable.addView(row);
    }
}
