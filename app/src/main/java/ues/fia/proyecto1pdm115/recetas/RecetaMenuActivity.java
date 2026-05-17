package ues.fia.proyecto1pdm115.recetas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;

public class RecetaMenuActivity extends AppCompatActivity{
    LinearLayout cardVisualizarReceta, cardCrearReceta;
    Button btnRegresarRecetas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recetas);
        Navegador.configurarBarra(this);

        cardVisualizarReceta = findViewById(R.id.cardVisualizarReceta);
        cardCrearReceta = findViewById(R.id.cardCrearReceta);
        btnRegresarRecetas = findViewById(R.id.btnRegresarRecetas);

        btnRegresarRecetas.setOnClickListener(v -> {
            finish();
        });

        cardVisualizarReceta.setOnClickListener(v ->
                startActivity(new Intent(this, VisualizarRecetaActivity.class)));

        cardCrearReceta.setOnClickListener(v ->
                startActivity(new Intent(this, CrearRecetaActivity.class)));

    }
}
