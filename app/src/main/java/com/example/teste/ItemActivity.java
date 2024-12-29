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

public class ItemActivity extends AppCompatActivity {
    private ImageView back,foodImage;
    private TextView nome,desc,preco;
    private Button adicionar;
    private DBHandler dbHandler;

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        back = findViewById(R.id.back);
        foodImage = findViewById(R.id.food_image);
        nome = findViewById(R.id.food_name);
        desc = findViewById(R.id.food_description);
        preco = findViewById(R.id.food_price);
        adicionar = findViewById(R.id.btnOrderNow);
        dbHandler = new DBHandler(this);
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");
        Intent intent = getIntent();
        int mealId = intent.getIntExtra("meal_id", -1);
        intent.removeExtra("meal_id");
        List<DBHandler.Meal> meals = dbHandler.getMeals();
        DBHandler.Meal meal = meals.get(mealId);
        foodImage.setImageResource(meal.getImage());
        nome.setText(meal.getName());
        desc.setText(meal.getDescription());
        preco.setText(String.format("€ %.2f", meal.getPrice()));


        back.setOnClickListener(view -> {
            Intent intent1 = new Intent(ItemActivity.this, MainActivity.class);
            startActivity(intent1);
            finish();
        });
        adicionar.setOnClickListener(view -> {
            dbHandler.addCart(meal, dbHandler.getUserId(userEmail), meal.getId());
            Toast.makeText(ItemActivity.this, "Adicionado ao carrinho", Toast.LENGTH_SHORT).show();

        });



    }
}


