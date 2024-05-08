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

public class history extends AppCompatActivity {

    private ImageView mnu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_history);

        mnu = findViewById(R.id.menu);

        mnu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PopupMenu popupMenu = new PopupMenu(history.this, mnu);
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

    }


    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, home.class);
        startActivity(intent);
        finish();
        super.onBackPressed();
    }
}