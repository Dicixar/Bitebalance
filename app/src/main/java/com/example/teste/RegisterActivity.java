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

                if (!validations(nome, email, password, repass)) {
                    return;
                }


                dbHandler.addNewUser(nome, email, password);
                Toast.makeText(RegisterActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                finish();
            }
        });

        signInLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    public boolean validations(String nome, String email, String password, String repass) {
        if (!password.equals(repass)) {
            Toast.makeText(RegisterActivity.this, "As passwords não correspondem", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!email.contains("@")) {
            Toast.makeText(RegisterActivity.this, "O email precisa de conter @", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.isEmpty() || password.isEmpty() || nome.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Preencha todos os Campos", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (password.length() < 8) {
            Toast.makeText(RegisterActivity.this, "Password tem de ter no mínimo 8 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.matches(".*[A-Z].*")) {
            Toast.makeText(RegisterActivity.this, "Password tem de conter pelo menos uma letra maiúscula", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.matches(".*[a-z].*")) {
            Toast.makeText(RegisterActivity.this, "Password tem de conter pelo menos uma letra minúscula", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.matches(".*\\d.*")) {
            Toast.makeText(RegisterActivity.this, "Password tem de conter pelo menos um número", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!password.matches(".*[!@#$%^&*()].*")) {
            Toast.makeText(RegisterActivity.this, "Password tem de conter pelo menos um caractere especial", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.contains(" ")) {
            Toast.makeText(RegisterActivity.this, "Email não pode conter espaços", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.length() > 50) {
            Toast.makeText(RegisterActivity.this, "Email não pode ser maior que 50 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (nome.length() > 50) {
            Toast.makeText(RegisterActivity.this, "Nome não pode ser maior que 50 caracteres", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!checkBox.isChecked()) {
            Toast.makeText(RegisterActivity.this, "Aceite os Termos e Condições", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (dbHandler.isEmailRegistered(email)) {
            Toast.makeText(RegisterActivity.this, "Email já registado", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

}
