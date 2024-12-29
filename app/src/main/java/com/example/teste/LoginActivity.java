package com.example.teste;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etEmailPhone, etPassword;
    private TextView tvForgotPassword;
    private Button btnLogin, btnCreateAccount;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmailPhone = findViewById(R.id.et_email_phone);
        etPassword = findViewById(R.id.et_password);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        btnLogin = findViewById(R.id.btn_login);
        btnCreateAccount = findViewById(R.id.btn_create);

        // Inicializa o DBHandler
        dbHandler = new DBHandler(this);
        dbHandler.addAdmin();

        btnLogin.setOnClickListener(v -> handleLogin());
        btnCreateAccount.setOnClickListener(v -> handleCreateAccount());
        tvForgotPassword.setOnClickListener(v -> handleForgotPassword());
    }

    private void handleLogin() {
        String email = etEmailPhone.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verifica se as credenciais são válidas
        if (dbHandler.validateUserLogin(email, password)) {
            Toast.makeText(this, "Login bem-sucedido!", Toast.LENGTH_SHORT).show();
            DBHandler.User user = dbHandler.getUserByEmail(email); // Obter os dados do utilizador

            // Guardar os dados no SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("USER_NAME", user.getName());
            editor.putString("USER_EMAIL", user.getEmail());
            editor.putBoolean("IS_LOGGED_IN", true); // Marcar como sessão ativa
            editor.apply();

            Intent intent = new Intent(this, MainActivity.class);
            dbHandler.addMeal();
            startActivity(intent);
            finish(); // Fecha a tela de login
        } else {
            Toast.makeText(this, "Credenciais inválidas. Tente novamente.", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCreateAccount() {
        // Navegar para a tela de registro
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

    private void handleForgotPassword() {
        // Implementar navegação para a tela de recuperação de senha
    }
}
