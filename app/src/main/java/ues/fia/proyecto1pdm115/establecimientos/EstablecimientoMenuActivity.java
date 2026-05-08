package ues.fia.proyecto1pdm115.establecimientos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.pacientes.VisualizarPacientesActivity;

public class EstablecimientoMenuActivity extends AppCompatActivity {

    LinearLayout cardVisualizarEstablecimiento, cardCrearEstablecimiento, cardModificarEstablecimiento, cardEliminarEstablecimiento;
    Button btnRegresarEstablecimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_establecimientos);

        cardVisualizarEstablecimiento = findViewById(R.id.cardVisualizarEstablecimiento);
        cardCrearEstablecimiento = findViewById(R.id.cardCrearEstablecimiento);
        cardModificarEstablecimiento = findViewById(R.id.cardModificarEstablecimiento);
        cardEliminarEstablecimiento = findViewById(R.id.cardEliminarEstablecimiento);
        btnRegresarEstablecimiento = findViewById(R.id.btnRegresarEstablecimientos);

        btnRegresarEstablecimiento.setOnClickListener(v -> {
            finish();
        });

        cardVisualizarEstablecimiento.setOnClickListener(v -> {
            startActivity(new Intent(this, VisualizarEstablecimientosActivity.class));
        });

        cardCrearEstablecimiento.setOnClickListener(v -> {
            startActivity(new Intent(this, CrearEstablecimientoActivity.class));
        });

        cardModificarEstablecimiento.setOnClickListener(v -> {
            startActivity(new Intent(this, ActualizarEstablecimientoActivity.class));
        });

        cardEliminarEstablecimiento.setOnClickListener(v -> {
            startActivity(new Intent(this, EliminarEstablecimientoActivity.class));
        });
    }
}
