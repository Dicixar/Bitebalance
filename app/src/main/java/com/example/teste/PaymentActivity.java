package com.example.teste;


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    private DBHandler dbHandler;
    private LinearLayout cartLayout;
    private TextView total;
    private ImageView voltar;


    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment); // O ficheiro XML fornecido por ti

        dbHandler = new DBHandler(this);

        // Verificar a sessão do utilizador
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);

        if (!isLoggedIn) {
            // Se não estiver logado, redireciona para o login
            Intent intent = new Intent(PaymentActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Fecha a MainActivity
        } else {
            // Caso a sessão exista, carrega os dados do utilizador
            String userName = sharedPreferences.getString("USER_NAME", "Guest");
            String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");
            displayCartItems(userEmail);
            total = findViewById(R.id.total);
            total.setText("Total: €" + dbHandler.getTotalCart(dbHandler.getUserId(userEmail)));
            DBHandler.User user = dbHandler.getUserDetails(userEmail);
            TextView email = findViewById(R.id.email);
            TextView nome = findViewById(R.id.nome);
            TextView morada = findViewById(R.id.morada);
            if (user != null) {
                nome.setText(user.getName());
                email.setText(user.getEmail());
                morada.setText(user.getMorada());
            }
            Button pay = findViewById(R.id.pay);
            pay.setOnClickListener(view -> {
                // Redirecionar para outra atividade (por exemplo, perfil)
                Intent intent = new Intent(PaymentActivity.this, ProfileActivity.class);
                startActivity(intent);
                finish();
            });


            voltar = findViewById(R.id.voltar);
            voltar.setOnClickListener(view -> {
                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            });

        }
    }
    @SuppressLint("SetTextI18n")
    private void displayCartItems(String userEmail) {
        cartLayout = findViewById(R.id.product_list1);

        List<DBHandler.CartItem> cartItems = dbHandler.getCartItems(dbHandler.getUserId(userEmail));

        // Limpar layout antes de adicionar novos itens
        cartLayout.removeAllViews();

        for (DBHandler.CartItem cartItem : cartItems) {
            // Criar um item de layout para cada item do carrinho
            View itemView = getLayoutInflater().inflate(R.layout.cart_item, cartLayout, false);

            // Preencher com as informações da refeição
            TextView mealName = itemView.findViewById(R.id.nome);
            TextView mealPrice = itemView.findViewById(R.id.preco);

            mealName.setText(cartItem.getMeal().getName());
            mealPrice.setText("€" + cartItem.getTotalPrice());

            // Adicionar o item ao layout
            cartLayout.addView(itemView);
        }
    }
}
