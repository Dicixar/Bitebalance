package com.example.teste;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<FoodItem> foodItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
            String userName = sharedPreferences.getString("USER_NAME", "Guest");
            String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");
            initializeFoodItems();
            setupBottomNavigation();
            setupFoodItems();
        }

    }

    private void initializeFoodItems() {
        foodItems = new ArrayList<>();
        foodItems.add(new FoodItem("Sushi",
                "Boiled sushi, or maki, is sushi where ingredients like fish, vegetables, and rice are wrapped in dried seaweed sheets and then sliced into bite-sized pieces.",
                9.99,
                R.drawable.sushi_image));
        foodItems.add(new FoodItem("Salada com Lombo",
                "Boiled sushi, or maki, is sushi where ingredients like fish, vegetables, and rice are wrapped in dried seaweed sheets and then sliced into bite-sized pieces.",
                8.99,
                R.drawable.salad_image));
        foodItems.add(new FoodItem("Frango com Arroz",
                "Boiled sushi, or maki, is sushi where ingredients like fish, vegetables, and rice are wrapped in dried seaweed sheets and then sliced into bite-sized pieces.",
                9.99,
                R.drawable.chicken_rice_image));
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

    private void setupFoodItems() {
        // Setup food items in the layout
        int[] foodItemIds = {R.id.sushi_item, R.id.salad_item, R.id.chicken_item};

        for (int i = 0; i < foodItemIds.length; i++) {
            View itemView = findViewById(foodItemIds[i]);
            FoodItem item = foodItems.get(i);

            ImageView foodImage = itemView.findViewById(R.id.food_image);
            TextView foodName = itemView.findViewById(R.id.food_name);
            TextView foodDescription = itemView.findViewById(R.id.food_description);
            TextView foodPrice = itemView.findViewById(R.id.food_price);
            Button recommendButton = itemView.findViewById(R.id.btn_recommend);

            foodImage.setImageResource(item.getImageResource());
            foodName.setText(item.getName());
            foodDescription.setText(item.getDescription());
            foodPrice.setText(String.format("$%.2f", item.getPrice()));

            recommendButton.setOnClickListener(v -> {
                // Handle recommend button click
                Intent intent = new Intent(MainActivity.this, EncomendaActivity.class);
                startActivity(intent);
                Toast.makeText(MainActivity.this,
                        "Recommended: " + item.getName(),
                        Toast.LENGTH_SHORT).show();
            });
        }
    }
}