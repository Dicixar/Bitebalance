package com.example.teste;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

// Classe PaymentActivity que gere o processo de pagamento e confirmação de encomendas
public class PaymentActivity extends AppCompatActivity {
    private DBHandler dbHandler; // Instância da base de dados
    private LinearLayout cartLayout; // Layout para exibir os itens do carrinho
    private TextView total; // TextView para exibir o total da encomenda
    private ImageView voltar; // Botão para voltar à atividade anterior
    private RadioGroup radioGroup; // Grupo de botões de rádio para selecionar o método de pagamento

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment); // Define o layout da atividade

        dbHandler = new DBHandler(this); // Inicializa a base de dados

        // Obtém as preferências partilhadas para verificar o estado de autenticação do utilizador
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);

        // Obtém o endereço de entrega passado pela atividade anterior
        String deliveryAddress = getIntent().getStringExtra("DELIVERY_ADDRESS");

        // Verifica se o utilizador está autenticado
        if (!isLoggedIn) {
            Intent intent = new Intent(PaymentActivity.this, LoginActivity.class);
            startActivity(intent); // Redireciona para a atividade de login
            finish(); // Fecha a atividade atual
        } else {
            // Obtém o nome e email do utilizador autenticado
            String userName = sharedPreferences.getString("USER_NAME", "Guest");
            String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");

            // Exibe os itens do carrinho e o total da encomenda
            displayCartItems(userEmail);
            total = findViewById(R.id.total);
            total.setText("Total: €" + dbHandler.getTotalCart(dbHandler.getUserId(userEmail)));

            // Obtém os detalhes do utilizador
            DBHandler.User user = dbHandler.getUserDetails(userEmail);
            TextView email = findViewById(R.id.email);
            TextView nome = findViewById(R.id.nome);
            TextView morada = findViewById(R.id.morada);

            // Define os valores dos TextViews com os detalhes do utilizador
            if (user != null) {
                nome.setText(user.getName());
                email.setText(user.getEmail());
                morada.setText(deliveryAddress); // Define o endereço de entrega
            }

            // Configura o botão de pagamento
            Button pay = findViewById(R.id.pay);
            pay.setOnClickListener(view -> {
                // Cria um popup de confirmação de pagamento
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirmação"); // Título do popup
                builder.setMessage("Efetuar pagamento?"); // Mensagem do popup

                // Configura o botão "Sim" do popup
                builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        radioGroup = findViewById(R.id.radioGroup);
                        String paymentMethod = "";

                        // Verifica qual método de pagamento foi selecionado
                        if (radioGroup.getCheckedRadioButtonId() == R.id.radioButton1) {
                            paymentMethod = "Multibanco";
                        } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButton2) {
                            paymentMethod = "Cartão de Crédito";
                        } else if (radioGroup.getCheckedRadioButtonId() == R.id.radioButton3) {
                            paymentMethod = "Giftcard";
                        } else {
                            Toast.makeText(PaymentActivity.this, "Selecione um método de pagamento", Toast.LENGTH_SHORT).show();
                            return; // Retorna se nenhum método de pagamento for selecionado
                        }

                        // Cria a encomenda e exibe uma mensagem de sucesso
                        createOrder(userEmail, paymentMethod);
                        Toast.makeText(PaymentActivity.this, "Pagamento realizado com sucesso", Toast.LENGTH_SHORT).show();
                    }
                });

                // Configura o botão "Não" do popup
                builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Fecha o popup
                        Toast.makeText(PaymentActivity.this, "Ação cancelada", Toast.LENGTH_SHORT).show();
                    }
                });

                // Mostra o popup
                AlertDialog dialog = builder.create();
                dialog.show();
            });

            // Configura o botão de voltar
            voltar = findViewById(R.id.voltar);
            voltar.setOnClickListener(view -> {
                Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
                startActivity(intent); // Navega para a atividade principal
                finish(); // Fecha a atividade atual
            });
        }
    }

    // Método para exibir os itens do carrinho
    @SuppressLint("SetTextI18n")
    private void displayCartItems(String userEmail) {
        cartLayout = findViewById(R.id.product_list1);

        // Obtém os itens do carrinho do utilizador
        List<DBHandler.CartItem> cartItems = dbHandler.getCartItems(dbHandler.getUserId(userEmail));

        cartLayout.removeAllViews(); // Limpa o layout antes de adicionar os itens

        // Adiciona cada item do carrinho ao layout
        for (DBHandler.CartItem cartItem : cartItems) {
            View itemView = getLayoutInflater().inflate(R.layout.cart_item, cartLayout, false);

            TextView mealName = itemView.findViewById(R.id.nome);
            TextView mealPrice = itemView.findViewById(R.id.preco);

            mealName.setText(cartItem.getMeal().getName()); // Define o nome da refeição
            mealPrice.setText("€" + cartItem.getTotalPrice()); // Define o preço total do item

            cartLayout.addView(itemView); // Adiciona o item ao layout
        }
    }

    // Método para criar a encomenda e finalizar o pagamento
    public void createOrder(String userEmail, String paymentMethod) {
        dbHandler.finishOrder(dbHandler.getUserId(userEmail), paymentMethod); // Finaliza a encomenda na base de dados
        Intent intent = new Intent(PaymentActivity.this, ProfileActivity.class);
        startActivity(intent); // Navega para o perfil do utilizador
        finish(); // Fecha a atividade atual
    }
}