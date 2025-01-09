package com.example.teste;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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

// Classe EncomendaActivity que gere a interface de encomendas
public class EncomendaActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap; // Objeto para interagir com o Google Maps
    private ImageButton img1; // Botão para voltar à atividade principal
    private DBHandler dbHandler; // Instância da base de dados
    private LinearLayout cartLayout; // Layout para exibir os itens do carrinho
    private TextView total, carrinho; // TextViews para exibir o total e o título do carrinho
    private Button finalizar; // Botão para finalizar a encomenda
    private Marker deliveryMarker; // Marcador no mapa para o local de entrega
    private LatLng selectedLocation; // Armazena a localização selecionada no mapa

    @SuppressLint({"MissingInflatedId", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encomenda);

        // Muda a cor da StatusBar para verde
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.green));

        // Inicializa a base de dados
        dbHandler = new DBHandler(this);
        cartLayout = findViewById(R.id.product_list);

        // Verifica se o utilizador está autenticado
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("IS_LOGGED_IN", false);

        if (!isLoggedIn) {
            // Redireciona para a atividade de login se o utilizador não estiver autenticado
            Intent intent = new Intent(EncomendaActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Obtém o nome e email do utilizador autenticado
            String userName = sharedPreferences.getString("USER_NAME", "Guest");
            String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");
            displayCartItems(userEmail); // Exibe os itens do carrinho
            total = findViewById(R.id.total);
            total.setText("Total: €" + dbHandler.getTotalCart(dbHandler.getUserId(userEmail))); // Exibe o total do carrinho
        }

        // Configura o botão de voltar
        img1 = findViewById(R.id.voltar);
        img1.setOnClickListener(view -> {
            Intent intent = new Intent(EncomendaActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        // Configura o botão para limpar o carrinho
        Button limpar = findViewById(R.id.limpar);
        limpar.setOnClickListener(view -> {
            String userEmail = sharedPreferences.getString("USER_EMAIL", "No email");
            dbHandler.clearCart(dbHandler.getUserId(userEmail)); // Limpa o carrinho
            Intent intent = new Intent(EncomendaActivity.this, EncomendaActivity.class);
            startActivity(intent);
            finish();
        });

        // Configura o botão para finalizar a encomenda
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

                                // Redireciona para a atividade de pagamento com o endereço de entrega
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

    // Método para exibir os itens do carrinho
    @SuppressLint({"SetTextI18n", "WrongViewCast"})
    private void displayCartItems(String userEmail) {
        List<DBHandler.CartItem> cartItems = dbHandler.getCartItems(dbHandler.getUserId(userEmail));

        cartLayout.removeAllViews(); // Limpa o layout antes de adicionar novos itens

        for (DBHandler.CartItem cartItem : cartItems) {
            View itemView = getLayoutInflater().inflate(R.layout.cart_item, cartLayout, false);

            TextView mealName = itemView.findViewById(R.id.nome);
            TextView mealPrice = itemView.findViewById(R.id.preco);

            mealName.setText(cartItem.getMeal().getName());
            mealPrice.setText("Quantidade: " + cartItem.getQuantity() + "   €" + cartItem.getTotalPrice());

            cartLayout.addView(itemView); // Adiciona o item ao layout
        }
    }

    // Método chamado quando o Google Maps está pronto para ser usado
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        LatLng startPoint = new LatLng(38.736946, -9.142685);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startPoint, 15)); // Define a localização inicial do mapa

        // Adiciona um marcador no mapa para o local de entrega
        deliveryMarker = googleMap.addMarker(new MarkerOptions()
                .position(startPoint)
                .title("Ponto de Entrega")
                .draggable(true)); // Permite mover o marcador

        // Armazena a localização inicial como selecionada
        selectedLocation = startPoint;

        // Configura o listener para quando o marcador é arrastado
        googleMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                selectedLocation = marker.getPosition(); // Atualiza a localização selecionada
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