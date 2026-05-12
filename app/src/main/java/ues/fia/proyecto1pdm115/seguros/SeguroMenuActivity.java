package ues.fia.proyecto1pdm115.seguros;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.seguros.*;

public class SeguroMenuActivity extends AppCompatActivity {

    LinearLayout cardVisualizarSeguro, cardCrearSeguro, cardModificarSeguro, cardEliminarSeguro;
    Button btnRegresarSeguros;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seguros);
        Navegador.configurarBarra(this);

        cardVisualizarSeguro = findViewById(R.id.cardVisualizarSeguro);
        cardCrearSeguro = findViewById(R.id.cardCrearSeguro);
        cardModificarSeguro = findViewById(R.id.cardModificarSeguro);
        cardEliminarSeguro = findViewById(R.id.cardEliminarSeguro);
        btnRegresarSeguros = findViewById(R.id.btnRegresarSeguros);

        btnRegresarSeguros.setOnClickListener(v -> {
            finish();
        });

        cardVisualizarSeguro.setOnClickListener(v ->
                startActivity(new Intent(this, VisualizarSegurosActivity.class)));

        cardCrearSeguro.setOnClickListener(v ->
                startActivity(new Intent(this, CrearSeguroActivity.class)));

        cardModificarSeguro.setOnClickListener(v ->
                startActivity(new Intent(this, ModificarSeguroActivity.class)));

        cardEliminarSeguro.setOnClickListener(v ->
                startActivity(new Intent(this, EliminarSeguroActivity.class)));
    }
}
