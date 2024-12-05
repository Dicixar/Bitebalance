package com.example.teste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;

import java.security.cert.CertPathBuilderSpi;

public class RegisterActivity extends AppCompatActivity {

    private CheckBox checkBox;
    private DBHandler dbHandler;
    private TextInputLayout etEmail, etPassword, etNome, etRepass;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Inicializa os campos de entrada e a classe DBHandler
        etEmail = findViewById(R.id.email);
        etNome = findViewById(R.id.nome);
        etPassword = findViewById(R.id.pass);
        etRepass = findViewById(R.id.repass);
        checkBox = findViewById(R.id.checkbox_terms);
        Button btnSignup = findViewById(R.id.btn_signup);
        TextView signInLink = findViewById(R.id.text_signin);

        dbHandler = new DBHandler(this);

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String password = String.valueOf(etPassword.getEditText().getText());
                String nome = String.valueOf(etNome.getEditText().getText());
                String email = String.valueOf(etEmail.getEditText().getText());
                String repass = String.valueOf(etRepass.getEditText().getText());


                if (!password.equals(repass)) {
                    Toast.makeText(RegisterActivity.this, "As passwords não correpondem", Toast.LENGTH_SHORT).show();
                }

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!checkBox.isChecked()) {
                    Toast.makeText(RegisterActivity.this, "Please accept Terms & Privacy", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (dbHandler.isEmailRegistered(email)) {
                    Toast.makeText(RegisterActivity.this, "Email is already registered", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Adiciona o utilizador à base de dados
                dbHandler.addNewUser(nome, email, password);
                Toast.makeText(RegisterActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();

                // Redireciona para a página de login após o registo bem-sucedido
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish(); // Fecha a atividade de registo
            }
        });

        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navega para a atividade de login
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }
}
