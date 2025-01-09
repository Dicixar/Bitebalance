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

// Classe MainActivity que gere a atividade principal da aplicação
public class MainActivity extends AppCompatActivity {

    private List<FoodItem> foodItems; // Lista de itens de comida
    private ImageView carrinho; // Ícone do carrinho
    private EditText searchEditText; // Campo de pesquisa
    private DBHandler dbHandler; // Instância da base de dados
    private LinearLayout foodcontainer; // Layout para exibir os itens de comida

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); // Define o layout da atividade

        // Inicializa o ícone do carrinho e configura o clique
        carrinho = findViewById(R.id.carrinho);
        carrinho.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, EncomendaActivity.class);
            startActivity(intent);
            finish();
        });

        // Verifica se o utilizador está autenticado
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);

        if (!isLoggedIn) {
            // Redireciona para a atividade de login se o utilizador não estiver autenticado
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Obtém o nome e email do utilizador autenticado
            String userName = sharedPreferences.getString("USER_NAME", "Guest");
            String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");

            // Configura a navegação inferior e os itens de comida
            setupBottomNavigation();
            setupFoodItems();
        }

        // Inicializa o campo de pesquisa e o layout de exibição de comida
        searchEditText = findViewById(R.id.search_edit_text);
        foodcontainer = findViewById(R.id.foodcontainer);

        // Configura o TextWatcher para a barra de pesquisa
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                filterFoodItems(charSequence.toString()); // Filtra os itens de comida com base na pesquisa
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
    }

    // Configura a navegação inferior (BottomNavigationView)
    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                navigateToHome(); // Navega para a atividade principal
                return true;
            } else if (item.getItemId() == R.id.navigation_simulator) {
                navigateToSimulator(); // Navega para o simulador de IMC
                return true;
            } else if (item.getItemId() == R.id.navigation_health) {
                navigateToHealth(); // Navega para a atividade de saúde
                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {
                navigateToProfile(); // Navega para o perfil do utilizador
                return true;
            }
            return false;
        });
    }

    // Método para navegar para a atividade principal (não faz nada, pois já estamos na atividade principal)
    private void navigateToHome() {
        // Handle home navigation
    }

    // Método para navegar para o simulador de IMC
    private void navigateToSimulator() {
        Intent intent = new Intent(MainActivity.this, ImcCalculatorActivity.class);
        startActivity(intent);
    }

    // Método para navegar para a atividade de saúde
    private void navigateToHealth() {
        Intent intent = new Intent(MainActivity.this, PlanoActivity.class);
        startActivity(intent);
    }

    // Método para navegar para o perfil do utilizador
    private void navigateToProfile() {
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    // Configura os itens de comida na interface
    @SuppressLint("DefaultLocale")
    private void setupFoodItems() {
        foodItems = new ArrayList<>();

        // Obtém o email do utilizador autenticado
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");

        // Inicializa o layout de exibição de comida e a base de dados
        LinearLayout foodcontainer = findViewById(R.id.foodcontainer);
        dbHandler = new DBHandler(this);

        // Obtém a lista de refeições da base de dados
        List<DBHandler.Meal> meals = dbHandler.getMeals();

        // Adiciona cada refeição à lista de itens de comida e à interface
        for (DBHandler.Meal meal : meals) {
            foodItems.add(new FoodItem(meal.getId(), meal.getName(), meal.getDescription(), meal.getPrice(), meal.getImage(), meal.getCalories(), meal.getCarbohydrates(), meal.getProteins(), meal.getFats()));

            // Infla o layout de um item de comida
            View itemView = getLayoutInflater().inflate(R.layout.item_food, foodcontainer, false);
            ImageView foodImage = itemView.findViewById(R.id.food_image);
            TextView foodName = itemView.findViewById(R.id.food_name);
            TextView foodDescription = itemView.findViewById(R.id.food_description);
            TextView foodPrice = itemView.findViewById(R.id.food_price);
            Button recommendButton = itemView.findViewById(R.id.btn_recommend);

            // Configura o clique no botão de recomendação
            recommendButton.setOnClickListener(v -> {
                dbHandler.addCart(meal, dbHandler.getUserId(userEmail), meal.getId()); // Adiciona a refeição ao carrinho
                Toast.makeText(MainActivity.this, "Adicionado ao carrinho: " + meal.getName(), Toast.LENGTH_SHORT).show(); // Exibe uma mensagem de confirmação
            });

            // Configura o clique no item de comida
            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                intent.putExtra("meal_id", meal.getId() - 1); // Passa o ID da refeição para a atividade de detalhes
                startActivity(intent);
            });

            // Define os valores dos componentes com os detalhes da refeição
            foodImage.setImageResource(meal.getImage());
            foodName.setText(meal.getName());
            foodDescription.setText(meal.getDescription());
            foodPrice.setText(String.format("€ %.2f", meal.getPrice()));

            // Adiciona o item de comida ao layout
            foodcontainer.addView(itemView);
        }
    }

    // Filtra os itens de comida com base na pesquisa
    private void filterFoodItems(String query) {
        foodcontainer.removeAllViews(); // Limpa o layout antes de adicionar os itens filtrados

        // Obtém o email do utilizador autenticado
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");

        // Obtém a lista de refeições da base de dados
        List<DBHandler.Meal> meals = dbHandler.getMeals();

        // Filtra as refeições com base na pesquisa
        for (DBHandler.Meal meal : meals) {
            if (meal.getName().toLowerCase().contains(query.toLowerCase())) {
                // Infla o layout de um item de comida
                View itemView = getLayoutInflater().inflate(R.layout.item_food, foodcontainer, false);
                ImageView foodImage = itemView.findViewById(R.id.food_image);
                TextView foodName = itemView.findViewById(R.id.food_name);
                TextView foodDescription = itemView.findViewById(R.id.food_description);
                TextView foodPrice = itemView.findViewById(R.id.food_price);
                Button recommendButton = itemView.findViewById(R.id.btn_recommend);

                // Configura o clique no botão de recomendação
                recommendButton.setOnClickListener(v -> {
                    dbHandler.addCart(meal, dbHandler.getUserId(userEmail), meal.getId()); // Adiciona a refeição ao carrinho
                    Toast.makeText(MainActivity.this, "Adicionado ao carrinho: " + meal.getName(), Toast.LENGTH_SHORT).show(); // Exibe uma mensagem de confirmação
                });

                // Configura o clique no item de comida
                itemView.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                    intent.putExtra("meal_id", meal.getId() - 1); // Passa o ID da refeição para a atividade de detalhes
                    startActivity(intent);
                });

                // Define os valores dos componentes com os detalhes da refeição
                foodImage.setImageResource(meal.getImage());
                foodName.setText(meal.getName());
                foodDescription.setText(meal.getDescription());
                foodPrice.setText(String.format("€ %.2f", meal.getPrice()));

                // Adiciona o item de comida ao layout
                foodcontainer.addView(itemView);
            }
        }
    }
}