package com.example.teste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

// Classe RegisterActivity que gere o registo de novos utilizadores
public class RegisterActivity extends AppCompatActivity {

    private CheckBox checkBox; // Checkbox para aceitar os termos e condições
    private DBHandler dbHandler; // Instância da base de dados
    private TextInputLayout etEmail, etPassword, etNome, etRepass; // Campos de entrada para email, password, nome e repetir password

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Define o layout da atividade

        // Inicializa os componentes do layout
        etEmail = findViewById(R.id.email);
        etNome = findViewById(R.id.nome);
        etPassword = findViewById(R.id.pass);
        etRepass = findViewById(R.id.repass);
        checkBox = findViewById(R.id.checkbox_terms);
        Button btnSignup = findViewById(R.id.btn_signup); // Botão de registo
        TextView signInLink = findViewById(R.id.text_signin); // Link para a atividade de login

        dbHandler = new DBHandler(this); // Inicializa a base de dados

        // Configura o clique no botão de registo
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtém os valores dos campos de entrada
                String password = String.valueOf(etPassword.getEditText().getText());
                String nome = String.valueOf(etNome.getEditText().getText());
                String email = String.valueOf(etEmail.getEditText().getText());
                String repass = String.valueOf(etRepass.getEditText().getText());

                // Valida os dados inseridos
                if (!validations(nome, email, password, repass)) {
                    return; // Interrompe o processo se a validação falhar
                }

                // Adiciona o novo utilizador à base de dados
                dbHandler.addNewUser(nome, email, password);
                Toast.makeText(RegisterActivity.this, "Conta criada com sucesso!", Toast.LENGTH_SHORT).show();

                // Navega para a atividade de login
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish(); // Fecha a atividade de registo
            }
        });

        // Configura o clique no link para a atividade de login
        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class)); // Navega para a atividade de login
            }
        });
    }

    // Método para validar os dados inseridos pelo utilizador
    public boolean validations(String nome, String email, String password, String repass) {
        // Verifica se as passwords coincidem
        if (!password.equals(repass)) {
            Toast.makeText(RegisterActivity.this, "As passwords não correspondem", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verifica se o email contém o caractere '@'
        if (!email.contains("@")) {
            Toast.makeText(RegisterActivity.this, "O email precisa de conter @", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verifica se todos os campos estão preenchidos
        if (email.isEmpty() || password.isEmpty() || nome.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Preencha todos os Campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verifica se a password tem pelo menos 8 caracteres
        if (password.length() < 8) {
            Toast.makeText(RegisterActivity.this, "Password tem de ter no mínimo 8 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verifica se a password contém pelo menos uma letra maiúscula
        if (!password.matches(".*[A-Z].*")) {
            Toast.makeText(RegisterActivity.this, "Password tem de conter pelo menos uma letra maiúscula", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verifica se a password contém pelo menos uma letra minúscula
        if (!password.matches(".*[a-z].*")) {
            Toast.makeText(RegisterActivity.this, "Password tem de conter pelo menos uma letra minúscula", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verifica se a password contém pelo menos um número
        if (!password.matches(".*\\d.*")) {
            Toast.makeText(RegisterActivity.this, "Password tem de conter pelo menos um número", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verifica se a password contém pelo menos um caractere especial
        if (!password.matches(".*[!@#$%^&*()].*")) {
            Toast.makeText(RegisterActivity.this, "Password tem de conter pelo menos um caractere especial", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verifica se o email contém espaços
        if (email.contains(" ")) {
            Toast.makeText(RegisterActivity.this, "Email não pode conter espaços", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verifica se o email não excede 50 caracteres
        if (email.length() > 50) {
            Toast.makeText(RegisterActivity.this, "Email não pode ser maior que 50 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verifica se o nome não excede 50 caracteres
        if (nome.length() > 50) {
            Toast.makeText(RegisterActivity.this, "Nome não pode ser maior que 50 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verifica se o utilizador aceitou os termos e condições
        if (!checkBox.isChecked()) {
            Toast.makeText(RegisterActivity.this, "Aceite os Termos e Condições", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Verifica se o email já está registado
        if (dbHandler.isEmailRegistered(email)) {
            Toast.makeText(RegisterActivity.this, "Email já registado", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true; // Retorna verdadeiro se todas as validações forem bem-sucedidas
    }
}