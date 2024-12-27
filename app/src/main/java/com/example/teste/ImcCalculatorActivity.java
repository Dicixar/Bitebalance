package com.example.teste_bb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Referências aos componentes do layout
        EditText inputHeight = findViewById(R.id.altura);
        EditText inputWeight = findViewById(R.id.peso);
        Button btnCalculate = findViewById(R.id.calcularButton);
        TextView textResult = findViewById(R.id.resultado);
        ImageView imcBar = findViewById(R.id.imcBar);
        ImageView imcPointer = findViewById(R.id.imcPointer);


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
                                position = barWidth * -0.40f; // 5% da barra
                            } else if (imc < 17) {
                                classification = "Magreza moderada";
                                position = barWidth * -0.28f; // 15% da barra
                            } else if (imc < 18.5) {
                                classification = "Abaixo do peso";
                                position = barWidth * -0.15f; // 25% da barra
                            } else if (imc < 25) {
                                classification = "Normal";
                                position = barWidth * 0.0f; // 50% da barra
                            } else if (imc < 30) {
                                classification = "Excesso de peso";
                                position = barWidth * 0.07f; // 70% da barra
                            } else if (imc < 35) {
                                classification = "Obeso classe I";
                                position = barWidth * 0.24f; // 85% da barra
                            } else if (imc < 40) {
                                classification = "Obeso classe II";
                                position = barWidth * 0.34f; // 95% da barra
                            } else {
                                classification = "Obeso classe III";
                                position = barWidth * 0.50f; // Fim da barra
                            }

                            // Atualizar o texto
                            textResult.setText("Classificação: " + classification);

                            // Posicionar a seta (ajustando para centralizá-la)
                            imcPointer.setTranslationX(position - (imcPointer.getWidth() / 2));
                        });
                    }else
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
    };
    }
