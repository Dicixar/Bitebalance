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

public class PlanoActivity extends AppCompatActivity {

    private EditText inputPeso, inputAltura;
    private Spinner spinnerObjetivo;
    private Button btnGerarPlano;
    private TextView resultadoPlano;
    private DBHandler dbHandler;
    private ImageView carrinho;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plano);
        setupBottomNavigation();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_health);

        inputPeso = findViewById(R.id.inputPeso);
        inputAltura = findViewById(R.id.inputAltura);
        spinnerObjetivo = findViewById(R.id.spinnerObjetivo);
        btnGerarPlano = findViewById(R.id.btnGerarPlano);
        resultadoPlano = findViewById(R.id.resultadoPlano);
        carrinho = findViewById(R.id.carrinho);
        carrinho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlanoActivity.this, EncomendaActivity.class);
                startActivity(intent);
            }
        });

        dbHandler = new DBHandler(this);
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");

        double weight = dbHandler.getBmi(dbHandler.getUserId(userEmail));
        double height = dbHandler.getBmi1(dbHandler.getUserId(userEmail));
        String mealPlan = dbHandler.getMealPlan(dbHandler.getUserId(userEmail));

        inputPeso.setText(String.valueOf(weight));
        inputAltura.setText(String.valueOf(height));
        resultadoPlano.setText(mealPlan);

        // Define os valores do Spinner
        String[] objetivos = {"Perder Peso", "Ganhar Massa Muscular", "Manter Peso", "Aumentar Energia e Disposição", "Reduzir o Estresse e Ansiedade", "Reduzir o Colesterol",};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, objetivos);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerObjetivo.setAdapter(adapter);

        btnGerarPlano.setOnClickListener(view -> {
            String peso = inputPeso.getText().toString();
            String altura = inputAltura.getText().toString();
            String objetivo = spinnerObjetivo.getSelectedItem().toString();

            if (peso.isEmpty() || altura.isEmpty()) {
                resultadoPlano.setText("Por favor, preencha todos os campos.");
            }
            else
            {
                OpenAIHelper.gerarPlanoAlimentar(peso, altura, objetivo, new OpenAIHelper.Callback() {
                    @Override
                    public void onSuccess(String response) {
                        runOnUiThread(() -> resultadoPlano.setText(response));
                        dbHandler.updateMealPlan(dbHandler.getUserId(userEmail), resultadoPlano.getText().toString());
                    }

                    @Override
                    public void onError(String error) {
                        runOnUiThread(() -> resultadoPlano.setText("Erro: " + error));
                    }
                });
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
        Intent intent = new Intent(PlanoActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    private void navigateToSimulator() {
        Intent intent = new Intent(PlanoActivity.this, ImcCalculatorActivity.class);
        startActivity(intent);
        finish();
    }
    private void navigateToHealth() {
    }
    private void navigateToProfile() {
        Intent intent = new Intent(PlanoActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }



}