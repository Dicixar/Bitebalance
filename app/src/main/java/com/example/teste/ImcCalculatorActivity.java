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

public class ImcCalculatorActivity extends AppCompatActivity {

    private DBHandler dbHandler;

    @Override
    @SuppressLint({"MissingInflatedId", "LocalSuppress"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc_calculator);
        setupBottomNavigation();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_simulator);
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");


        // Referências aos componentes do layout
        EditText inputHeight = findViewById(R.id.input_height);
        EditText inputWeight = findViewById(R.id.input_weight);
        Button btnCalculate = findViewById(R.id.btn_calculate);
        TextView textResult = findViewById(R.id.text_result);
        ImageView imcBar = findViewById(R.id.imcBar);
        ImageView imcPointer = findViewById(R.id.imcPointer);
        TextView textClassification = findViewById(R.id.text_classification);

        dbHandler = new DBHandler(this);
        double weight1 = dbHandler.getBmi(dbHandler.getUserId(userEmail));
        double height1 = dbHandler.getBmi1(dbHandler.getUserId(userEmail));
        if (weight1 != 0 || height1 != 0) {
            inputHeight.setText(String.valueOf(height1));
            inputWeight.setText(String.valueOf(weight1));
        }

        // Configuração do botão de calcular
        btnCalculate.setOnClickListener(v -> {

            String heightInput = inputHeight.getText().toString();
            String weightInput = inputWeight.getText().toString();


            // Verificar se os campos estão preenchidos corretamente
            if (!heightInput.isEmpty() && !weightInput.isEmpty()) {
                try {
                    double height = Double.parseDouble(heightInput);
                    double weight = Double.parseDouble(weightInput);
                    dbHandler.addBmi(dbHandler.getUserId(userEmail), weight, height);

                    if (height > 0) {
                        // Cálculo do IMC
                        double imc = weight / (height * height);

                        // Exibir o resultado do IMC
                        textResult.setText(String.format("IMC: %.1f", imc));

                        // Obter a largura da barra após ser renderizada
                        imcBar.post(() -> {
                            // Largura total da barra
                            int barWidth = imcBar.getWidth();

                            // Posição inicial do indicador
                            float position = 0;

                            // Determinar a classificação
                            String classification;

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
                            // Atualizar o texto
                            textClassification.setText("Classificação: " + classification);

                            // Posicionar a seta (ajustando para centralizá-la)
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
        Intent intent = new Intent(ImcCalculatorActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void navigateToSimulator() {
        // Handle simulator navigation
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
