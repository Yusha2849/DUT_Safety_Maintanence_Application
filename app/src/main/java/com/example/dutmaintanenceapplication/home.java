package com.example.dutmaintanenceapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class home extends AppCompatActivity {
    private ImageView mnu;
    CardView faultlog, faulthistory,existingf, justf;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        mnu = findViewById(R.id.menu);
        existingf = findViewById(R.id.existingfault);

        existingf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(home.this,FaultQueue.class);
                startActivity(intent);
                finish();
            }
        });
        mnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a PopupMenu
                PopupMenu popupMenu = new PopupMenu(home.this, mnu);
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
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}