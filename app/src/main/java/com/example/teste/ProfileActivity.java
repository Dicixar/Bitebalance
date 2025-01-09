package com.example.teste;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

// Classe ProfileActivity que gere o perfil do utilizador e as suas encomendas
public class ProfileActivity extends AppCompatActivity {

    private ImageButton img1, btn2; // Botões de imagem (logout e voltar)
    private DBHandler dbHandler; // Instância da base de dados
    private Button btn1; // Botão para atualizar o perfil
    private EditText nome, phone, morada; // Campos de entrada para nome, telefone e morada
    private TextView email, nome1; // TextViews para exibir o email e o nome do utilizador
    private LinearLayout ordersLayout; // Layout para exibir as encomendas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // Define o layout da atividade

        // Configura a navegação inferior
        setupBottomNavigation();
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setSelectedItemId(R.id.navigation_profile); // Define o ícone do perfil como selecionado

        // Obtém as preferências partilhadas para verificar o estado de autenticação do utilizador
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);

        // Inicializa os componentes do layout
        nome = findViewById(R.id.nome);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        morada = findViewById(R.id.morada);
        btn1 = findViewById(R.id.atualizar);
        btn2 = findViewById(R.id.btn_back);
        nome1 = findViewById(R.id.nome1);
        ordersLayout = findViewById(R.id.orders_layout);
        dbHandler = new DBHandler(this);

        // Verifica se o utilizador está autenticado
        if (!isLoggedIn) {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            startActivity(intent); // Redireciona para a atividade de login
            finish(); // Fecha a atividade atual
        } else {
            // Obtém o email do utilizador autenticado
            String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");

            // Obtém os detalhes do utilizador da base de dados
            DBHandler.User user = dbHandler.getUserDetails(userEmail);

            if (user != null) {
                // Preenche os campos com os detalhes do utilizador
                nome.setText(user.getName());
                email.setText(user.getEmail());
                phone.setText(user.getPhone());
                morada.setText(user.getMorada());
                nome1.setText(user.getName());

                // Carrega as encomendas do utilizador
                loadOrders(dbHandler.getUserId(userEmail));
            }
        }

        // Configura o clique no botão de atualizar perfil
        btn1.setOnClickListener(view -> {
            String nome1 = String.valueOf(nome.getText());
            String email1 = String.valueOf(email.getText());
            String morada1 = String.valueOf(morada.getText());
            String phone1 = String.valueOf(phone.getText());

            // Atualiza o perfil na base de dados
            dbHandler.editProfile(nome1, email1, morada1, phone1);
            Toast.makeText(ProfileActivity.this, "Atualizado com Sucesso", Toast.LENGTH_SHORT).show();

            // Recarrega a atividade para refletir as alterações
            Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
            startActivity(intent);
            finish();
        });

        // Configura o clique no botão de logout
        img1 = findViewById(R.id.btn_sign_out);
        img1.setOnClickListener(view -> {
            logOut(); // Efetua o logout
        });

        // Configura o clique no botão de voltar
        btn2.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
            startActivity(intent); // Navega para a atividade principal
            finish(); // Fecha a atividade atual
        });
    }

    // Método para carregar e exibir as encomendas do utilizador
    @SuppressLint("SetTextI18n")
    private void loadOrders(int userId) {
        // Obtém as encomendas do utilizador da base de dados
        List<DBHandler.Order> orders = dbHandler.getOrdersByUserId(userId);

        ordersLayout.removeAllViews(); // Limpa o layout antes de adicionar as encomendas

        // Adiciona cada encomenda ao layout
        for (DBHandler.Order order : orders) {
            View orderView = getLayoutInflater().inflate(R.layout.item_setting_option, ordersLayout, false);

            // Inicializa os componentes do layout da encomenda
            TextView orderIdTextView = orderView.findViewById(R.id.order_id);
            TextView orderDateTextView = orderView.findViewById(R.id.order_date);
            TextView itemNameTextView = orderView.findViewById(R.id.item_name);
            TextView itemQuantityTextView = orderView.findViewById(R.id.item_quantity);
            TextView itemPriceTextView = orderView.findViewById(R.id.item_price);
            Button orderStatusButton = orderView.findViewById(R.id.status);

            // Define os valores dos componentes com os detalhes da encomenda
            orderIdTextView.setText("Encomenda #" + order.getId());
            orderDateTextView.setText("Data: " + order.getDate());
            itemNameTextView.setText("Item: " + order.getMeal().getName());
            itemQuantityTextView.setText("Quantidade: " + order.getQuantity());
            itemPriceTextView.setText("Preço: €" + (order.getMeal().getPrice() * order.getQuantity()));
            orderStatusButton.setText(order.getStatus());

            // Define a cor do botão de status com base no estado da encomenda
            if (order.getStatus().equals("A entregar")) {
                orderStatusButton.setBackgroundColor(ContextCompat.getColor(this, R.color.yellow));
            } else if (order.getStatus().equals("Entregue")) {
                orderStatusButton.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
                LinearLayout buttons = orderView.findViewById(R.id.buttons);
                buttons.setVisibility(View.GONE); // Oculta os botões de ação para encomendas entregues
            } else if (order.getStatus().equals("Cancelado")) {
                orderStatusButton.setBackgroundColor(ContextCompat.getColor(this, R.color.red));
                LinearLayout buttons = orderView.findViewById(R.id.buttons);
                buttons.setVisibility(View.GONE); // Oculta os botões de ação para encomendas canceladas
            }

            // Adiciona a encomenda ao layout
            ordersLayout.addView(orderView);

            // Configura o clique para expandir/recolher os detalhes da encomenda
            LinearLayout headerLayout = orderView.findViewById(R.id.header_layout);
            LinearLayout expandableLayout = orderView.findViewById(R.id.expandable_layout);
            ImageView expandIcon = orderView.findViewById(R.id.expand_icon);

            headerLayout.setOnClickListener(v -> {
                if (expandableLayout.getVisibility() == View.GONE) {
                    expandableLayout.setVisibility(View.VISIBLE); // Expande os detalhes
                    expandIcon.setRotation(90); // Rotaciona o ícone
                } else {
                    expandableLayout.setVisibility(View.GONE); // Recolhe os detalhes
                    expandIcon.setRotation(270); // Rotaciona o ícone
                }
            });

            // Configura o clique no botão "Entregue"
            Button entregueButton = orderView.findViewById(R.id.entregue);
            entregueButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirmação"); // Título do popup
                builder.setMessage("Tem certeza que a encomenda foi entregue?"); // Mensagem do popup

                // Botão de confirmação
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHandler.updateOrderStatus(order.getId(), "Entregue"); // Atualiza o estado da encomenda
                        Toast.makeText(ProfileActivity.this, "Encomenda Entregue", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                        startActivity(intent); // Recarrega a atividade
                        finish();
                    }
                });

                // Botão de cancelamento
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Fecha o popup
                        Toast.makeText(ProfileActivity.this, "Ação cancelada", Toast.LENGTH_SHORT).show();
                    }
                });

                // Mostra o popup
                AlertDialog dialog = builder.create();
                dialog.show();
            });

            // Configura o clique no botão "Cancelar"
            Button cancelarButton = orderView.findViewById(R.id.cancelar);
            cancelarButton.setOnClickListener(v -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirmação"); // Título do popup
                builder.setMessage("Tem a certeza que deseja cancelar a encomenda?"); // Mensagem do popup

                // Botão de confirmação
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHandler.updateOrderStatus(order.getId(), "Cancelado"); // Atualiza o estado da encomenda
                        Toast.makeText(ProfileActivity.this, "Encomenda Cancelada", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                        startActivity(intent); // Recarrega a atividade
                        finish();
                    }
                });

                // Botão de cancelamento
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ProfileActivity.this, "Ação cancelada.", Toast.LENGTH_SHORT).show();
                        dialog.dismiss(); // Fecha o popup
                    }
                });

                // Mostra o popup
                AlertDialog dialog = builder.create();
                dialog.show();
            });
        }
    }

    // Configura a navegação inferior (BottomNavigationView)
    private void setupBottomNavigation() {
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navigation_home) {
                navigateToHome(); // Navega para a atividade principal
                return true;
            } else if (item.getItemId() == R.id.navigation_simulator) {
                navigateToSimulator(); // Navega para o simulador de IMC
                return true;
            } else if (item.getItemId() == R.id.navigation_health) {
                navigateToHealth(); // Navega para a atividade de saúde
                return true;
            } else if (item.getItemId() == R.id.navigation_profile) {
                navigateToProfile(); // Navega para o perfil (atual atividade)
                return true;
            }
            return false;
        });
    }

    // Método para navegar para a atividade principal
    private void navigateToHome() {
        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
        startActivity(intent);
    }

    // Método para navegar para o simulador de IMC
    private void navigateToSimulator() {
        Intent intent = new Intent(ProfileActivity.this, ImcCalculatorActivity.class);
        startActivity(intent);
    }

    // Método para navegar para a atividade de saúde
    private void navigateToHealth() {
        Intent intent = new Intent(ProfileActivity.this, PlanoActivity.class);
        startActivity(intent);
    }

    // Método para navegar para o perfil (não faz nada, pois já estamos na atividade de perfil)
    private void navigateToProfile() {
    }

    // Método para efetuar o logout
    private void logOut() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Limpa os dados da sessão
        editor.clear();
        editor.apply();

        // Redireciona para a página de login
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}