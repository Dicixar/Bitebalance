package com.example.teste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton img1, btn2;
    private DBHandler dbHandler;
    private Button btn1;
    private EditText nome, phone, morada;
    private TextView email, nome1;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setupBottomNavigation();

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);
        nome = findViewById(R.id.nome);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        morada = findViewById(R.id.morada);
        btn1 = findViewById(R.id.atualizar);
        btn2 = findViewById(R.id.btn_back);
        nome1 = findViewById(R.id.nome1);
        dbHandler = new DBHandler(this);

        if (!isLoggedIn) {
            // Se não estiver logado, redireciona para o login
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Caso a sessão exista, carrega os dados do utilizador
            String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");

            // Obtém os detalhes do utilizador da base de dados
            DBHandler.User user = dbHandler.getUserDetails(userEmail);

            if (user != null) {
                nome.setText(user.getName());
                email.setText(user.getEmail());
                phone.setText(user.getPhone());
                morada.setText(user.getMorada());
                nome1.setText(user.getName());

            }
        }

        btn1.setOnClickListener(view -> {
            String nome1 = String.valueOf(nome.getText());
            String email1 = String.valueOf(email.getText());
            String morada1 = String.valueOf(morada.getText());
            String phone1 = String.valueOf(phone.getText());

            // Atualiza os dados do utilizador
            dbHandler.editProfile(nome1, email1, morada1, phone1);
            Toast.makeText(ProfileActivity.this, "Atualizado com Sucesso", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();

        });

        img1 = findViewById(R.id.btn_sign_out);
        img1.setOnClickListener(view -> {
            logOut();
        });

        btn2.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
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
