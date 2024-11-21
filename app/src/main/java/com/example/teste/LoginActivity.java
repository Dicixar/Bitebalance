//package com.example.teste;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.google.android.material.textfield.TextInputEditText;
//
//public class LoginActivity extends AppCompatActivity {
//
//    private TextInputEditText etEmailPhone, etPassword;
//    private TextView tvForgotPassword;
//    private Button btnLogin, btnCreateAccount;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//
//        etEmailPhone = findViewById(R.id.et_email_phone);
//        etPassword = findViewById(R.id.et_password);
//        tvForgotPassword = findViewById(R.id.tv_forgot_password);
//        btnLogin = findViewById(R.id.btn_login);
//        btnCreateAccount = findViewById(R.id.btn_create);
//
//        btnLogin.setOnClickListener(v -> handleLogin());
//        btnCreateAccount.setOnClickListener(v -> handleCreateAccount());
//        tvForgotPassword.setOnClickListener(v -> handleForgotPassword());
//    }
//
//    private void handleLogin() {
//        String emailPhone = etEmailPhone.getText().toString();
//        String password = etPassword.getText().toString();
//
//        // Implement login logic here
//        // Call your authentication service to validate the user
//    }
//
//    private void handleCreateAccount() {
//        // Navigate to the sign-up screen
//        Intent intent = new Intent(this, SignUpActivity.class);
//        startActivity(intent);
//    }
//
//    private void handleForgotPassword() {
//        // Navigate to the forgot password screen
//        Intent intent = new Intent(this, ForgotPasswordActivity.class);
//        startActivity(intent);
//    }
//}