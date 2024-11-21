//package com.example.teste;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.TextView;
//import android.widget.Toast;
//import androidx.appcompat.app.AppCompatActivity;
//
//public class RegisterActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//        Button btnSignup = findViewById(R.id.btn_signup);
//        CheckBox checkBox = findViewById(R.id.checkbox_terms);
//        TextView signInLink = findViewById(R.id.text_signin);
//
//        btnSignup.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (checkBox.isChecked()) {
//                    // Lógica para registar o utilizador
//                    Toast.makeText(RegisterActivity.this, "Account Created!", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(RegisterActivity.this, "Please accept Terms & Privacy", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//
//        signInLink.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Navega para a atividade de login
//                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
//            }
//        });
//    }
//}
