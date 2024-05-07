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
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class edit_duty extends AppCompatActivity {

    //I'm assuming all the fields will be filled in based on the issue selected, then the tech can change what they see and update
    private ImageView mnu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_duty);

        mnu = findViewById(R.id.menu);

        mnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a PopupMenu
                PopupMenu popupMenu = new PopupMenu(edit_duty.this, mnu);
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
}