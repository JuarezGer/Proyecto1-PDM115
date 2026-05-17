package ues.fia.proyecto1pdm115.hospitalizaciones;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;

public class HospitalizacionMenuActivity extends AppCompatActivity {
    LinearLayout cardVisualizarHospitalizacion, cardCrearHospitalizacion;
    Button btnRegresarHospitalizaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitalizaciones);
        Navegador.configurarBarra(this);

        cardVisualizarHospitalizacion = findViewById(R.id.cardVisualizarHospitalizacion);
        cardCrearHospitalizacion = findViewById(R.id.cardCrearHospitalizacion);
        btnRegresarHospitalizaciones = findViewById(R.id.btnRegresarHospitalizaciones);

        btnRegresarHospitalizaciones.setOnClickListener(v -> {
            finish();
        });

        cardVisualizarHospitalizacion.setOnClickListener(v ->
                startActivity(new Intent(this, VisualizarHospitalizacionActivity.class)));

        cardCrearHospitalizacion.setOnClickListener(v ->
                startActivity(new Intent(this, CrearHospitalizacionActivity.class)));
    }
}

