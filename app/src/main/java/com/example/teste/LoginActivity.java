package com.example.teste;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

// Classe LoginActivity que gere a autenticação do utilizador
public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmail, etPassword; // Campos de entrada para email e password
    private TextView tvForgotPassword; // Texto para recuperação de password
    private Button btnLogin, btnCreateAccount; // Botões para login e criação de conta
    private DBHandler dbHandler; // Instância da base de dados

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // Define o layout da atividade

        // Inicializa os componentes do layout
        etEmail = findViewById(R.id.et_email_phone);
        etPassword = findViewById(R.id.et_password);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        btnLogin = findViewById(R.id.btn_login);
        btnCreateAccount = findViewById(R.id.btn_create);

        // Inicializa o DBHandler e adiciona o administrador (se necessário)
        dbHandler = new DBHandler(this);
        dbHandler.addAdmin();

        // Configura o clique no botão de login
        btnLogin.setOnClickListener(v -> handleLogin());

        // Configura o clique no botão de criar conta
        btnCreateAccount.setOnClickListener(v -> {
            Intent intent = new Intent(this, RegisterActivity.class);
            startActivity(intent); // Navega para a atividade de registo
        });

        // Configura o clique no texto de recuperação de password
        tvForgotPassword.setOnClickListener(v -> {
            Toast.makeText(this, "Em Breve...", Toast.LENGTH_SHORT).show(); // Exibe uma mensagem temporária
        });
    }

    // Método para tratar o processo de login
    private void handleLogin() {
        String email = etEmail.getText().toString().trim(); // Obtém o email
        String password = etPassword.getText().toString().trim(); // Obtém a password inserida

        // Verifica se os campos estão preenchidos
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Valida as credenciais do utilizador
        if (dbHandler.validateUserLogin(email, password)) {
            Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();

            // Obtém os detalhes do utilizador
            DBHandler.User user = dbHandler.getUserByEmail(email);

            // Guarda os dados do utilizador nas SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("USER_NAME", user.getName()); // Guarda o nome do utilizador
            editor.putString("USER_EMAIL", user.getEmail()); // Guarda o email do utilizador
            editor.putBoolean("IS_LOGGED_IN", true); // Define o estado de autenticação como verdadeiro
            editor.apply(); // Aplica as alterações

            // Navega para a atividade principal (MainActivity)
            Intent intent = new Intent(this, MainActivity.class);
            dbHandler.addMeal(); // Adiciona refeições à base de dados (se necessário)
            startActivity(intent);
            finish(); // Fecha a atividade de login
        } else {
            Toast.makeText(this, "Credenciais inválidas. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }
}