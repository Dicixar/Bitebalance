package com.example.teste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import java.util.List;

public class ItemActivity extends AppCompatActivity {
    private ImageView back,foodImage;
    private TextView nome,desc,preco,title,calories,carbohydrates,proteins,fats;
    private Button adicionar;
    private DBHandler dbHandler;

    @SuppressLint({"DefaultLocale", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        // Muda a cor da StatusBar para verde
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        back = findViewById(R.id.back);
        foodImage = findViewById(R.id.food_image);
        nome = findViewById(R.id.food_name);
        desc = findViewById(R.id.food_description);
        preco = findViewById(R.id.food_price);
        adicionar = findViewById(R.id.btnOrderNow);
        title = findViewById(R.id.food_title);
        calories = findViewById(R.id.calorias);
        carbohydrates = findViewById(R.id.carbo);
        proteins = findViewById(R.id.proteinas);
        fats = findViewById(R.id.gordura);

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
        title.setText(meal.getName());
        calories.setText(String.format("%d kcal", meal.getCalories()));
        carbohydrates.setText(String.format("%d g", meal.getCarbohydrates()));
        proteins.setText(String.format("%d g", meal.getProteins()));
        fats.setText(String.format("%d g", meal.getFats()));


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


