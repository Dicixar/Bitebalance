package com.example.teste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private ImageButton img1, btn2;
    private DBHandler dbHandler;
    private Button btn1;
    private EditText nome, phone, morada;
    private TextView email, nome1;
    private LinearLayout ordersLayout;

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
        ordersLayout = findViewById(R.id.orders_layout); // Adicione este ID no layout
        dbHandler = new DBHandler(this);

        if (!isLoggedIn) {
            // Se não estiver logado, redireciona para o login
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");
            DBHandler.User user = dbHandler.getUserDetails(userEmail);

            if (user != null) {
                nome.setText(user.getName());
                email.setText(user.getEmail());
                phone.setText(user.getPhone());
                morada.setText(user.getMorada());
                nome1.setText(user.getName());

                // Carrega as encomendas do usuário
                loadOrders(dbHandler.getUserId(userEmail));
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


    @SuppressLint("SetTextI18n")
    private void loadOrders(int userId) {
        List<DBHandler.Order> orders = dbHandler.getOrdersByUserId(userId);

        // Limpa o layout antes de adicionar novas encomendas
        ordersLayout.removeAllViews();

        for (DBHandler.Order order : orders) {
            // Infla o layout de cada encomenda
            View orderView = getLayoutInflater().inflate(R.layout.item_setting_option, ordersLayout, false);

            // Preenche os dados da encomenda
            TextView orderIdTextView = orderView.findViewById(R.id.order_id);
            TextView orderDateTextView = orderView.findViewById(R.id.order_date);
            TextView itemNameTextView = orderView.findViewById(R.id.item_name);
            TextView itemQuantityTextView = orderView.findViewById(R.id.item_quantity);
            TextView itemPriceTextView = orderView.findViewById(R.id.item_price);

            orderIdTextView.setText("Order #" + order.getId());
            orderDateTextView.setText("Date: " + order.getDate());
            itemNameTextView.setText("Item: " + order.getMeal().getName());
            itemQuantityTextView.setText("Quantity: " + order.getQuantity());
            itemPriceTextView.setText("Price: €" + (order.getMeal().getPrice() * order.getQuantity()));

            // Adiciona a encomenda ao layout
            ordersLayout.addView(orderView);

            // Configura o clique para expandir/recolher os detalhes da encomenda
            LinearLayout headerLayout = orderView.findViewById(R.id.header_layout);
            LinearLayout expandableLayout = orderView.findViewById(R.id.expandable_layout);
            ImageView expandIcon = orderView.findViewById(R.id.expand_icon);

            headerLayout.setOnClickListener(v -> {
                if (expandableLayout.getVisibility() == View.GONE) {
                    expandableLayout.setVisibility(View.VISIBLE);
                    expandIcon.setRotation(90); // Gira o ícone para baixo
                } else {
                    expandableLayout.setVisibility(View.GONE);
                    expandIcon.setRotation(270); // Gira o ícone para cima
                }
            });
        }
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
