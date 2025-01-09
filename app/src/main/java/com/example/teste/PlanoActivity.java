package com.example.teste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

// Classe PlanoActivity que gere a geração de planos alimentares personalizados
public class PlanoActivity extends AppCompatActivity {

    private EditText inputPeso, inputAltura; // Campos de entrada para peso e altura
    private Spinner spinnerObjetivo; // Spinner para selecionar o objetivo do utilizador
    private Button btnGerarPlano; // Botão para gerar o plano alimentar
    private TextView resultadoPlano; // TextView para exibir o plano alimentar gerado
    private DBHandler dbHandler; // Instância da base de dados
    private ImageView carrinho; // Ícone do carrinho

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plano); // Define o layout da atividade

        // Configura a navegação inferior
        setupBottomNavigation();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_health); // Define o ícone de saúde como selecionado

        // Inicializa os componentes do layout
        inputPeso = findViewById(R.id.inputPeso);
        inputAltura = findViewById(R.id.inputAltura);
        spinnerObjetivo = findViewById(R.id.spinnerObjetivo);
        btnGerarPlano = findViewById(R.id.btnGerarPlano);
        resultadoPlano = findViewById(R.id.resultadoPlano);
        carrinho = findViewById(R.id.carrinho);

        // Configura o clique no ícone do carrinho
        carrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanoActivity.this, EncomendaActivity.class);
                startActivity(intent); // Navega para a atividade de encomendas
            }
        });

        // Inicializa a base de dados
        dbHandler = new DBHandler(this);

        // Obtém o email do utilizador autenticado
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");

        // Obtém o peso, altura e plano alimentar do utilizador da base de dados
        double weight = dbHandler.getBmi(dbHandler.getUserId(userEmail));
        double height = dbHandler.getBmi1(dbHandler.getUserId(userEmail));
        String mealPlan = dbHandler.getMealPlan(dbHandler.getUserId(userEmail));

        // Preenche os campos de entrada com os valores da base de dados
        inputPeso.setText(String.valueOf(weight));
        inputAltura.setText(String.valueOf(height));
        resultadoPlano.setText(mealPlan); // Exibe o plano alimentar existente

        // Define os valores do Spinner (objetivos)
        String[] objetivos = {"Perder Peso", "Ganhar Massa Muscular", "Manter Peso", "Aumentar Energia e Disposição", "Reduzir o Estresse e Ansiedade", "Reduzir o Colesterol"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, objetivos);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerObjetivo.setAdapter(adapter); // Define o adaptador do Spinner

        // Configura o clique no botão de gerar plano
        btnGerarPlano.setOnClickListener(view -> {
            String peso = inputPeso.getText().toString();
            String altura = inputAltura.getText().toString();
            String objetivo = spinnerObjetivo.getSelectedItem().toString();

            // Verifica se os campos de peso e altura estão preenchidos
            if (peso.isEmpty() || altura.isEmpty()) {
                resultadoPlano.setText("Por favor, preencha todos os campos.");
            } else {
                // Gera o plano alimentar usando a API da OpenAI
                OpenAIHelper.gerarPlanoAlimentar(peso, altura, objetivo, new OpenAIHelper.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        // Atualiza a interface com o plano alimentar gerado
                        runOnUiThread(() -> resultadoPlano.setText(response));

                        // Se não houver dados na tabela BMI, adiciona os novos valores
                        if (dbHandler.getBmi(dbHandler.getUserId(userEmail)) == 0.0 && dbHandler.getBmi1(dbHandler.getUserId(userEmail)) == 0.0) {
                            dbHandler.addBmi(dbHandler.getUserId(userEmail), Double.parseDouble(peso), Double.parseDouble(altura));
                        }

                        // Atualiza o plano alimentar na base de dados
                        dbHandler.updateMealPlan(dbHandler.getUserId(userEmail), resultadoPlano.getText().toString());
                    }

                    @Override
                    public void onError(String error) {
                        // Exibe uma mensagem de erro em caso de falha
                        runOnUiThread(() -> resultadoPlano.setText("Erro: " + error));
                    }
                });
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
                navigateToHealth(); // Navega para a atividade de saúde (atual atividade)
                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {
                navigateToProfile(); // Navega para o perfil do utilizador
                return true;
            }
            return false;
        });
    }

    // Método para navegar para a atividade principal
    private void navigateToHome() {
        Intent intent = new Intent(PlanoActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // Fecha a atividade atual
    }

    // Método para navegar para o simulador de IMC
    private void navigateToSimulator() {
        Intent intent = new Intent(PlanoActivity.this, ImcCalculatorActivity.class);
        startActivity(intent);
        finish(); // Fecha a atividade atual
    }

    // Método para navegar para a atividade de saúde (não faz nada, pois já estamos na atividade de saúde)
    private void navigateToHealth() {
    }

    // Método para navegar para o perfil do utilizador
    private void navigateToProfile() {
        Intent intent = new Intent(PlanoActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish(); // Fecha a atividade atual
    }
}