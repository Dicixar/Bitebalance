package com.example.teste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

import java.util.List;

public class EncomendaActivity extends AppCompatActivity {

    private MapView mapView;
    private ImageButton img1;
    private DBHandler dbHandler;
    private LinearLayout cartLayout;
    private TextView total;


    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encomenda); // O ficheiro XML fornecido por ti

        dbHandler = new DBHandler(this);
        cartLayout = findViewById(R.id.product_list);


        // Verificar a sessão do utilizador
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);

        if (!isLoggedIn) {
            // Se não estiver logado, redireciona para o login
            Intent intent = new Intent(EncomendaActivity.this, LoginActivity.class);
            startActivity(intent);
            finish(); // Fecha a MainActivity
        }
        else{
            // Caso a sessão exista, carrega os dados do utilizador
            String userName = sharedPreferences.getString("USER_NAME", "Guest");
            String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");
            displayCartItems(userEmail);
            total = findViewById(R.id.total);
            total.setText("Total: €" + dbHandler.getTotalCart(dbHandler.getUserId(userEmail)));


        }


        img1 = findViewById(R.id.voltar);
        img1.setOnClickListener(view -> {
            Intent intent = new Intent(EncomendaActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Configuração inicial do OSMDroid
        Configuration.getInstance().load(getApplicationContext(),
                getSharedPreferences("osm_prefs", MODE_PRIVATE));

        // Inicializa o mapa no map_container
        mapView = new MapView(this);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(15.0);

        // Adiciona o MapView dinamicamente ao FrameLayout
        FrameLayout mapContainer = findViewById(R.id.map_container);
        mapContainer.addView(mapView);

        // Define um ponto inicial no mapa (Exemplo: Lisboa)
        GeoPoint startPoint = new GeoPoint(38.736946, -9.142685); // Coordenadas de Lisboa
        mapView.getController().setCenter(startPoint);

        // Adiciona um marcador no ponto inicial
        Marker marker = new Marker(mapView);
        marker.setPosition(startPoint);
        marker.setTitle("Ponto de Entrega");
        marker.setDraggable(true); // Permite mover o marcador
        mapView.getOverlays().add(marker);

        // Listener para obter a nova posição do marcador
        marker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
                // Pode exibir alguma notificação, se necessário
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                // Atualiza dinamicamente enquanto o marcador é arrastado
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // Posição final do marcador
                GeoPoint newPosition = marker.getPosition();
                double latitude = newPosition.getLatitude();
                double longitude = newPosition.getLongitude();
                // Aqui podes atualizar a posição selecionada para envio
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume(); // Necessário para OSMDroid
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause(); // Necessário para OSMDroid
    }

    @SuppressLint("SetTextI18n")
    private void displayCartItems(String userEmail) {

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
            mealPrice.setText("Quantidade: " + cartItem.getQuantity() + "   €" + cartItem.getTotalPrice());

            // Adicionar o item ao layout
            cartLayout.addView(itemView);
        }
    }

}
