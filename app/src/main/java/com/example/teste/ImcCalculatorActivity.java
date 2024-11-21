package com.example.teste;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ImcCalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imc_calculator);
        setupBottomNavigation();

        // Referências aos componentes do layout
        EditText inputHeight = findViewById(R.id.input_height);
        EditText inputWeight = findViewById(R.id.input_weight);
        EditText inputGoal = findViewById(R.id.input_goal);
        Button btnCalculate = findViewById(R.id.btn_calculate);
        TextView textResult = findViewById(R.id.text_result);
        TextView textClassification = findViewById(R.id.text_classification);
        ImageView imcBar = findViewById(R.id.imc_bar);

        // Configuração do botão de calcular
        btnCalculate.setOnClickListener(v -> {
            String heightInput = inputHeight.getText().toString();
            String weightInput = inputWeight.getText().toString();

            // Verificar se os campos estão preenchidos corretamente
            if (!heightInput.isEmpty() && !weightInput.isEmpty()) {
                try {
                    double height = Double.parseDouble(heightInput);
                    double weight = Double.parseDouble(weightInput);

                    if (height > 0) {
                        // Cálculo do IMC
                        double imc = weight / (height * height);

                        // Exibir o resultado do IMC
                        textResult.setText(String.format("IMC: %.1f", imc));

//                        // Determinar a classificação
//                        String classification;
//                        if (imc < 16) {
//                            classification = "Magreza extrema";
//                            imcBar.setImageResource(R.drawable.imc_bar_magreza_extrema);
//                        } else if (imc < 17) {
//                            classification = "Magreza moderada";
//                            imcBar.setImageResource(R.drawable.imc_bar_magreza_moderada);
//                        } else if (imc < 18.5) {
//                            classification = "Abaixo do peso";
//                            imcBar.setImageResource(R.drawable.imc_bar_abaixo_peso);
//                        } else if (imc < 25) {
//                            classification = "Normal";
//                            imcBar.setImageResource(R.drawable.imc_bar_normal);
//                        } else if (imc < 30) {
//                            classification = "Excesso de peso";
//                            imcBar.setImageResource(R.drawable.imc_bar_excesso_peso);
//                        } else if (imc < 35) {
//                            classification = "Obeso classe I";
//                            imcBar.setImageResource(R.drawable.imc_bar_obeso_i);
//                        } else if (imc < 40) {
//                            classification = "Obeso classe II";
//                            imcBar.setImageResource(R.drawable.imc_bar_obeso_ii);
//                        } else {
//                            classification = "Obeso classe III";
//                            imcBar.setImageResource(R.drawable.imc_bar_obeso_iii);
//                        }

//                         Exibir a classificação
//                        textClassification.setText("Classificação: " + classification);
                    }
                    else
                    {
                        Toast.makeText(this, "Altura deve ser maior que zero.", Toast.LENGTH_SHORT).show();
                    }
                }
                    catch (NumberFormatException e) {
                    Toast.makeText(this, "Por favor, insira valores numéricos válidos.", Toast.LENGTH_SHORT).show();
                }
            }
            else {
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
        // Handle home navigation
        Intent intent = new Intent(ImcCalculatorActivity.this, MainActivity.class);
        startActivity(intent);
    }
    private void navigateToSimulator() {
        // Handle simulator navigation
    }
    private void navigateToHealth() {
        // Handle health navigation
    }
    private void navigateToProfile() {
        Intent intent = new Intent(ImcCalculatorActivity.this, ProfileActivity.class);
        startActivity(intent);
    }
}
