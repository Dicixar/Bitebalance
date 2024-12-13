package com.example.teste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<FoodItem> foodItems;
    private ImageView carrinho;
    private DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        carrinho = findViewById(R.id.carrinho);
        carrinho.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EncomendaActivity.class);
            startActivity(intent);
            finish();
        });


        // Verificar a sessão do utilizador
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);

        if (!isLoggedIn) {
            // Se não estiver logado, redireciona para o login
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Fecha a MainActivity
        }
        else{
            // Caso a sessão exista, carrega os dados do utilizador
            String id = sharedPreferences.getString("USER_ID", "no ID");
            String userName = sharedPreferences.getString("USER_NAME", "Guest");
            String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");
            initializeFoodItems();
            setupBottomNavigation();
            setupFoodItems();
        }



    }

    private void initializeFoodItems() {

        foodItems = new ArrayList<>();
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
    }
    private void navigateToSimulator() {
        Intent intent = new Intent(MainActivity.this, ImcCalculatorActivity.class);
        startActivity(intent);
    }
    private void navigateToHealth() {
        // Handle health navigation
    }
    private void navigateToProfile() {
        // Handle profile navigation
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    @SuppressLint("DefaultLocale")
    private void setupFoodItems() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String id = sharedPreferences.getString("USER_ID", "no ID");

        LinearLayout foodcontainer = findViewById(R.id.foodcontainer);
        dbHandler = new DBHandler(this);
        List<DBHandler.Meal> meals = dbHandler.getMeals();
        for (DBHandler.Meal meal : meals) {
            foodItems.add(new FoodItem(meal.getId(), meal.getName(), meal.getDescription(), meal.getPrice(), meal.getImage()));
            View itemView = getLayoutInflater().inflate(R.layout.item_food, foodcontainer, false);
            ImageView foodImage = itemView.findViewById(R.id.food_image);
            TextView foodName = itemView.findViewById(R.id.food_name);
            TextView foodDescription = itemView.findViewById(R.id.food_description);
            TextView foodPrice = itemView.findViewById(R.id.food_price);
            Button recommendButton = itemView.findViewById(R.id.btn_recommend);

            recommendButton.setOnClickListener(v -> {
                dbHandler.addCart(meal, Integer.parseInt(id), meal.getId());
                Toast.makeText(MainActivity.this, "Meal added to cart", Toast.LENGTH_SHORT).show();
                    });
            foodImage.setImageResource(meal.getImage());
            foodName.setText(meal.getName());
            foodDescription.setText(meal.getDescription());
            foodPrice.setText(String.format("€ %.2f", meal.getPrice()));
            foodcontainer.addView(itemView);

        }

    }



}