package com.example.dutmaintanenceapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FaultQueue extends AppCompatActivity {
    private ListView faultTableLayout;
    private ImageView mnu;
    private ReportDatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fault_queue);

        faultTableLayout = findViewById(R.id.faultListView);
        mnu = findViewById(R.id.menu);
        databaseHelper = new ReportDatabaseHelper(this);

        mnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a PopupMenu
                PopupMenu popupMenu = new PopupMenu(FaultQueue.this, mnu);
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

        loadFaults();
    }

    private void loadFaults() {
        Cursor cursor = databaseHelper.getAllReports();
        if (cursor.moveToFirst()) {
            do {
                String campus = cursor.getString(cursor.getColumnIndexOrThrow("campus"));
                String location = cursor.getString(cursor.getColumnIndexOrThrow("location"));
                String block = cursor.getString(cursor.getColumnIndexOrThrow("block"));
                String issueType = cursor.getString(cursor.getColumnIndexOrThrow("issue_type"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));

                // Create a new TableRow
                TableRow row = new TableRow(this);

                // Add TextViews for each column in the TableRow
                addTextViewToTableRow(row, campus);
                addTextViewToTableRow(row, location);
                addTextViewToTableRow(row, block);
                addTextViewToTableRow(row, issueType);
                addTextViewToTableRow(row, description);

                // Add the TableRow to the TableLayout
                faultTableLayout.addView(row);
            } while (cursor.moveToNext());
        }
        cursor.close();
    }

    // Method to add a TextView to a TableRow
    private void addTextViewToTableRow(TableRow row, String text) {
        TextView textView = new TextView(this);
        textView.setText(text);
        textView.setPadding(16, 8, 16, 8);
        textView.setGravity(Gravity.CENTER); // Set gravity to center
        row.addView(textView);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}
