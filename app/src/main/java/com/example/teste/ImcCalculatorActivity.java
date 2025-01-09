package com.example.teste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

// Classe ImcCalculatorActivity que gere a calculadora de IMC (Índice de Massa Corporal)
public class ImcCalculatorActivity extends AppCompatActivity {

    private DBHandler dbHandler; // Instância da base de dados

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc_calculator); // Define o layout da atividade

        // Configura a navegação inferior (BottomNavigationView)
        setupBottomNavigation();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_simulator); // Define o ícone do simulador como selecionado

        // Obtém as preferências partilhadas para verificar o email do utilizador autenticado
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");

        // Referências aos componentes do layout
        EditText inputHeight = findViewById(R.id.input_height); // Campo de entrada para a altura
        EditText inputWeight = findViewById(R.id.input_weight); // Campo de entrada para o peso
        Button btnCalculate = findViewById(R.id.btn_calculate); // Botão para calcular o IMC
        TextView textResult = findViewById(R.id.text_result); // TextView para exibir o resultado do IMC
        ImageView imcBar = findViewById(R.id.imcBar); // Barra visual do IMC
        ImageView imcPointer = findViewById(R.id.imcPointer); // Ponteiro na barra do IMC
        TextView textClassification = findViewById(R.id.text_classification); // TextView para exibir a classificação do IMC
        ImageView carrinho = findViewById(R.id.carrinho); // Ícone do carrinho

        // Configura o clique no ícone do carrinho
        carrinho.setOnClickListener(v -> {
            Intent intent = new Intent(ImcCalculatorActivity.this, EncomendaActivity.class);
            startActivity(intent);
            finish();
        });

        // Inicializa a base de dados
        dbHandler = new DBHandler(this);

        // Obtém o peso e a altura do utilizador da base de dados (se existirem)
        double weight1 = dbHandler.getBmi(dbHandler.getUserId(userEmail));
        double height1 = dbHandler.getBmi1(dbHandler.getUserId(userEmail));
        if (weight1 != 0 || height1 != 0) {
            inputHeight.setText(String.valueOf(height1)); // Preenche o campo da altura
            inputWeight.setText(String.valueOf(weight1)); // Preenche o campo do peso
        }

        // Configuração do botão de calcular
        btnCalculate.setOnClickListener(v -> {
            String heightInput = inputHeight.getText().toString();
            String weightInput = inputWeight.getText().toString();

            // Verifica se os campos estão preenchidos corretamente
            if (!heightInput.isEmpty() && !weightInput.isEmpty()) {
                try {
                    double height = Double.parseDouble(heightInput);
                    double weight = Double.parseDouble(weightInput);

                    // Guarda o peso e a altura na base de dados
                    dbHandler.addBmi(dbHandler.getUserId(userEmail), weight, height);

                    if (height > 0) {
                        // Cálculo do IMC
                        double imc = weight / (height * height);

                        // Exibe o resultado do IMC
                        textResult.setText(String.format("IMC: %.1f", imc));

                        // Obtém a largura da barra após ser renderizada
                        imcBar.post(() -> {
                            int barWidth = imcBar.getWidth(); // Largura total da barra
                            float position = 0; // Posição inicial do indicador
                            String classification; // Classificação do IMC

                            // Determina a classificação e a posição do ponteiro com base no IMC
                            if (imc < 16) {
                                classification = "Magreza extrema";
                                position = barWidth * 0.065f; // 5% da barra
                            } else if (imc < 17) {
                                classification = "Magreza moderada";
                                position = barWidth * 0.185f; // 15% da barra
                            } else if (imc < 18.5) {
                                classification = "Abaixo do peso";
                                position = barWidth * 0.31f; // 25% da barra
                            } else if (imc < 25) {
                                classification = "Normal";
                                position = barWidth * 0.436f; // 50% da barra
                            } else if (imc < 30) {
                                classification = "Excesso de peso";
                                position = barWidth * 0.558f; // 70% da barra
                            } else if (imc < 35) {
                                classification = "Obeso classe I";
                                position = barWidth * 0.685f; // 85% da barra
                            } else if (imc < 40) {
                                classification = "Obeso classe II";
                                position = barWidth * 0.815f; // 95% da barra
                            } else {
                                classification = "Obeso classe III";
                                position = barWidth * 0.94f; // Fim da barra
                            }

                            // Atualiza o texto da classificação
                            textClassification.setText("Classificação: " + classification);

                            // Posiciona o ponteiro na barra (ajustando para centralizá-lo)
                            imcPointer.setTranslationX(position - (imcPointer.getWidth() / 2));
                        });
                    } else {
                        Toast.makeText(this, "Altura deve ser maior que zero.", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Por favor, insira valores numéricos válidos.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
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
                navigateToSimulator(); // Navega para o simulador (atual atividade)
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

    private void navigateToHome() {
        Intent intent = new Intent(ImcCalculatorActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void navigateToSimulator() {
    }

    private void navigateToHealth() {
        Intent intent = new Intent(ImcCalculatorActivity.this, PlanoActivity.class);
        startActivity(intent);
    }

    private void navigateToProfile() {
        Intent intent = new Intent(ImcCalculatorActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}