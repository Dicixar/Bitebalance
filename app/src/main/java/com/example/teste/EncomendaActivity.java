package com.example.teste;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

public class EncomendaActivity extends AppCompatActivity {

    private MapView mapView;
    private ImageButton img1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encomenda); // O ficheiro XML fornecido por ti


        img1 = findViewById(R.id.voltar);
        img1.setOnClickListener(view -> {
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

}
