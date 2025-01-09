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

// Classe ItemActivity que exibe os detalhes de uma refeição e permite adicioná-la ao carrinho
public class ItemActivity extends AppCompatActivity {
    private ImageView back, foodImage; // Botão de voltar e imagem da refeição
    private TextView nome, desc, preco, title, calories, carbohydrates, proteins, fats; // TextViews para exibir detalhes da refeição
    private Button adicionar; // Botão para adicionar ao carrinho
    private DBHandler dbHandler; // Instância da base de dados

    @SuppressLint({"DefaultLocale", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food); // Define o layout da atividade

        // Muda a cor da StatusBar para verde
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        // Inicializa os componentes do layout
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

        // Inicializa a base de dados
        dbHandler = new DBHandler(this);

        // Obtém as preferências partilhadas para verificar o email do utilizador autenticado
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");

        // Obtém o ID da refeição passado pela atividade anterior
        Intent intent = getIntent();
        int mealId = intent.getIntExtra("meal_id", -1);
        intent.removeExtra("meal_id");

        // Obtém a lista de refeições da base de dados
        List<DBHandler.Meal> meals = dbHandler.getMeals();

        // Verifica se o ID da refeição é válido e obtém a refeição correspondente
        if (mealId >= 0 && mealId < meals.size()) {
            DBHandler.Meal meal = meals.get(mealId);

            // Define os valores dos componentes com os detalhes da refeição
            foodImage.setImageResource(meal.getImage()); // Define a imagem da refeição
            nome.setText(meal.getName()); // Define o nome da refeição
            desc.setText(meal.getDescription()); // Define a descrição da refeição
            preco.setText(String.format("€ %.2f", meal.getPrice())); // Define o preço da refeição
            title.setText(meal.getName()); // Define o título da atividade
            calories.setText(String.format("%d kcal", meal.getCalories())); // Define as calorias
            carbohydrates.setText(String.format("%d g", meal.getCarbohydrates())); // Define os hidratos de carbono
            proteins.setText(String.format("%d g", meal.getProteins())); // Define as proteínas
            fats.setText(String.format("%d g", meal.getFats())); // Define as gorduras

            // Configura o clique no botão de voltar
            back.setOnClickListener(view -> {
                Intent intent1 = new Intent(ItemActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
            });

            // Configura o clique no botão de adicionar ao carrinho
            adicionar.setOnClickListener(view -> {
                dbHandler.addCart(meal, dbHandler.getUserId(userEmail), meal.getId()); // Adiciona a refeição ao carrinho
                Toast.makeText(ItemActivity.this, "Adicionado ao carrinho", Toast.LENGTH_SHORT).show(); // Exibe uma mensagem de confirmação
            });
        } else {
            Toast.makeText(this, "Refeição não encontrada", Toast.LENGTH_SHORT).show();
            finish(); // Fecha a atividade se a refeição não for encontrada
        }
    }
}