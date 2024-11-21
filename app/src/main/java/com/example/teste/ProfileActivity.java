package com.example.teste;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupBottomNavigation();
    }
    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                navigateToHome();
                return true;
            } else if (item.getItemId() == R.id.navigation_simulator) {
                navigateToSimulator();
                return true;
            } else if (item.getItemId() == R.id.navigation_health) {
                navigateToHealth();
                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {
                navigateToProfile();
                return true;
            }
            return false;
        });
    }

    private void navigateToHome() {
        // Handle home navigation
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }
    private void navigateToSimulator() {
        Intent intent = new Intent(ProfileActivity.this, ImcCalculatorActivity.class);
        startActivity(intent);
    }
    private void navigateToHealth() {
        // Handle health navigation
    }
    private void navigateToProfile() {
        // Handle profile navigation
    }
}
