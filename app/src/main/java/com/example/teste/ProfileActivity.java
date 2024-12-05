package com.example.teste;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton img1;
    private EditText nome, phone, morada;
    private TextView email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);

        if (!isLoggedIn) {
            // Se não estiver logado, redireciona para o login
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Fecha a MainActivity
        } else {
            nome = findViewById(R.id.nome);
            email = findViewById(R.id.email);
            phone = findViewById(R.id.phone);
            morada = findViewById(R.id.morada);
            // Caso a sessão exista, carrega os dados do utilizador
            String userName = sharedPreferences.getString("USER_NAME", "Guest");
            String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");
            setupBottomNavigation();
            nome.setText(userName);
            email.setText(userEmail);

        }

        img1 = findViewById(R.id.btn_sign_out);
        img1.setOnClickListener(view -> {
            logOut();
        });


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
    private void logOut() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Limpa os dados da sessão
        editor.clear();
        editor.apply();

        // Redireciona para a página de login
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish(); // Fecha a ProfileActivity
    }
}
