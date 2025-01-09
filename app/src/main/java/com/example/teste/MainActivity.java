package com.example.teste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private List<FoodItem> foodItems;
    private ImageView carrinho;
    private EditText searchEditText;
    private DBHandler dbHandler;
    private LinearLayout foodcontainer;
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


        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);

        if (!isLoggedIn) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            String userName = sharedPreferences.getString("USER_NAME", "Guest");
            String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");
            setupBottomNavigation();
            setupFoodItems();
        }

        searchEditText = findViewById(R.id.search_edit_text);
        foodcontainer = findViewById(R.id.foodcontainer);

        // Configura o TextWatcher para a barra de pesquisa
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterFoodItems(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
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
    }
    private void navigateToSimulator() {
        Intent intent = new Intent(MainActivity.this, ImcCalculatorActivity.class);
        startActivity(intent);
    }
    private void navigateToHealth() {
        Intent intent = new Intent(MainActivity.this, PlanoActivity.class);
        startActivity(intent);
    }
    private void navigateToProfile() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    @SuppressLint("DefaultLocale")
    private void setupFoodItems() {
        foodItems = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");

        LinearLayout foodcontainer = findViewById(R.id.foodcontainer);
        dbHandler = new DBHandler(this);
        List<DBHandler.Meal> meals = dbHandler.getMeals();
        for (DBHandler.Meal meal : meals) {
            foodItems.add(new FoodItem(meal.getId(), meal.getName(), meal.getDescription(), meal.getPrice(), meal.getImage(), meal.getCalories(), meal.getCarbohydrates(), meal.getProteins(), meal.getFats()));
            View itemView = getLayoutInflater().inflate(R.layout.item_food, foodcontainer, false);
            ImageView foodImage = itemView.findViewById(R.id.food_image);
            TextView foodName = itemView.findViewById(R.id.food_name);
            TextView foodDescription = itemView.findViewById(R.id.food_description);
            TextView foodPrice = itemView.findViewById(R.id.food_price);
            Button recommendButton = itemView.findViewById(R.id.btn_recommend);


            recommendButton.setOnClickListener(v -> {
                dbHandler.addCart(meal, dbHandler.getUserId(userEmail), meal.getId());
                Toast.makeText(MainActivity.this, "Adicionado ao carrinho: " + meal.getName(), Toast.LENGTH_SHORT).show();
                    });

            itemView.setOnClickListener(v -> {
                        Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                        intent.putExtra("meal_id", meal.getId() - 1);
                        startActivity(intent);
                    });
            foodImage.setImageResource(meal.getImage());
            foodName.setText(meal.getName());
            foodDescription.setText(meal.getDescription());
            foodPrice.setText(String.format("€ %.2f", meal.getPrice()));
            foodcontainer.addView(itemView);

        }

    }

    private void filterFoodItems(String query) {
        foodcontainer.removeAllViews();
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");

        List<DBHandler.Meal> meals = dbHandler.getMeals();

        for (DBHandler.Meal meal : meals) {
            if (meal.getName().toLowerCase().contains(query.toLowerCase())) {
                View itemView = getLayoutInflater().inflate(R.layout.item_food, foodcontainer, false);
                ImageView foodImage = itemView.findViewById(R.id.food_image);
                TextView foodName = itemView.findViewById(R.id.food_name);
                TextView foodDescription = itemView.findViewById(R.id.food_description);
                TextView foodPrice = itemView.findViewById(R.id.food_price);
                Button recommendButton = itemView.findViewById(R.id.btn_recommend);

                recommendButton.setOnClickListener(v -> {
                    dbHandler.addCart(meal, dbHandler.getUserId(userEmail), meal.getId());
                    Toast.makeText(MainActivity.this, "Adicionado ao carrinho: " + meal.getName(), Toast.LENGTH_SHORT).show();
                });

                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                    intent.putExtra("meal_id", meal.getId() - 1);
                    startActivity(intent);
                });

                foodImage.setImageResource(meal.getImage());
                foodName.setText(meal.getName());
                foodDescription.setText(meal.getDescription());
                foodPrice.setText(String.format("€ %.2f", meal.getPrice()));
                foodcontainer.addView(itemView);
            }
        }
    }
}