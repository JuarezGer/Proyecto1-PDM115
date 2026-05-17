package ues.fia.proyecto1pdm115.detallesRecetas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;

public class DetalleRecetaMenuActivity extends AppCompatActivity{
    LinearLayout cardVisualizarDetalleReceta, cardCrearDetalleReceta;
    Button btnRegresarDetallesRecetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_recetas);
        Navegador.configurarBarra(this);

        cardVisualizarDetalleReceta = findViewById(R.id.cardVisualizarDetalleReceta);
        cardCrearDetalleReceta = findViewById(R.id.cardCrearDetalleReceta);
        btnRegresarDetallesRecetas = findViewById(R.id.btnRegresarDetallesRecetas);

        btnRegresarDetallesRecetas.setOnClickListener(v -> {
            finish();
        });

        cardVisualizarDetalleReceta.setOnClickListener(v ->
                startActivity(new Intent(this, VisualizarDetalleRecetaActivity.class)));

        cardCrearDetalleReceta.setOnClickListener(v ->
                startActivity(new Intent(this, CrearDetalleRecetaActivity.class)));
    }
}
