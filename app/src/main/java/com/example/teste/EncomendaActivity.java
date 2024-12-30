package com.example.teste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class EncomendaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private ImageButton img1;
    private DBHandler dbHandler;
    private LinearLayout cartLayout;
    private TextView total, carrinho;
    private Button finalizar;
    private Marker deliveryMarker;
    private LatLng selectedLocation; // Armazena a localização selecionada

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encomenda);

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
        } else {
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

        Button limpar = findViewById(R.id.limpar);
        limpar.setOnClickListener(view -> {
            String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");
            dbHandler.clearCart(dbHandler.getUserId(userEmail));
            Intent intent = new Intent(EncomendaActivity.this, EncomendaActivity.class);
            startActivity(intent);
            finish();
        });

        finalizar = findViewById(R.id.btn_finalize_order);
        finalizar.setOnClickListener(view -> {
            String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");
            if (dbHandler.getCartItems(dbHandler.getUserId(userEmail)).isEmpty()) {
                Toast.makeText(EncomendaActivity.this, "O carrinho está vazio", Toast.LENGTH_SHORT).show();
            }
            else {
                if (selectedLocation != null) {
                    // Converte as coordenadas em um endereço
                    getAddressFromLocation(selectedLocation.latitude, selectedLocation.longitude, new GeocodingCallback() {
                        @Override
                        public void onAddressFetched(String address) {
                            if (address != null) {
                                Toast.makeText(EncomendaActivity.this, "Local de entrega: " + address, Toast.LENGTH_LONG).show();

                                // Aqui você pode passar o endereço para a PaymentActivity ou salvá-lo no banco de dados
                                Intent intent = new Intent(EncomendaActivity.this, PaymentActivity.class);
                                intent.putExtra("DELIVERY_ADDRESS", address);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(EncomendaActivity.this, "Não foi possível obter o endereço", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    Toast.makeText(EncomendaActivity.this, "Selecione um local de entrega no mapa", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Inicializa o Google Maps
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @SuppressLint({"SetTextI18n", "WrongViewCast"})
    private void displayCartItems(String userEmail) {
        List<DBHandler.CartItem> cartItems = dbHandler.getCartItems(dbHandler.getUserId(userEmail));

        // Limpa o layout antes de adicionar os novos itens
        cartLayout.removeAllViews();

        for (DBHandler.CartItem cartItem : cartItems) {
            // Criar um item de layout para cada item do carrinho
            View itemView = getLayoutInflater().inflate(R.layout.cart_item, cartLayout, false);

            TextView mealName = itemView.findViewById(R.id.nome);
            TextView mealPrice = itemView.findViewById(R.id.preco);

            mealName.setText(cartItem.getMeal().getName());
            mealPrice.setText("Quantidade: " + cartItem.getQuantity() + "   €" + cartItem.getTotalPrice());

            cartLayout.addView(itemView);
        }
    }

    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Define um ponto inicial no mapa (Exemplo: Lisboa)
        LatLng startPoint = new LatLng(38.736946, -9.142685); // Coordenadas de Lisboa
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 15));

        // Adiciona um marcador no ponto inicial
        deliveryMarker = googleMap.addMarker(new MarkerOptions()
                .position(startPoint)
                .title("Ponto de Entrega")
                .draggable(true)); // Permite mover o marcador

        // Armazena a localização inicial como selecionada
        selectedLocation = startPoint;

        // Listener para obter a nova posição do marcador
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
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
                selectedLocation = marker.getPosition();
                Toast.makeText(EncomendaActivity.this, "Local de entrega selecionado: " + selectedLocation.latitude + ", " + selectedLocation.longitude, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para converter coordenadas em endereço
    private void getAddressFromLocation(double latitude, double longitude, GeocodingCallback callback) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                StringBuilder addressBuilder = new StringBuilder();
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    addressBuilder.append(address.getAddressLine(i)).append(", ");
                }
                // Remove a vírgula extra no final
                String fullAddress = addressBuilder.toString().replaceAll(", $", "");
                callback.onAddressFetched(fullAddress);
            } else {
                callback.onAddressFetched(null);
            }
        } catch (IOException e) {
            e.printStackTrace();
            callback.onAddressFetched(null);
        }
    }

    // Interface para callback do Geocoding
    interface GeocodingCallback {
        void onAddressFetched(String address);
    }


}