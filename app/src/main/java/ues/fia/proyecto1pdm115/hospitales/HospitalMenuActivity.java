package ues.fia.proyecto1pdm115.hospitales;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;

public class HospitalMenuActivity extends AppCompatActivity {

    LinearLayout cardVisualizarHospital, cardCrearHospital, cardModificarHospital, cardEliminarHospital;
    Button btnRegresarHospital,btnCrearEspeHospital,btnEliminarEspeHospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hospitales);
        Navegador.configurarBarra(this);

        cardVisualizarHospital = findViewById(R.id.cardVisualizarHospital);
        cardCrearHospital = findViewById(R.id.cardCrearHospital);
        cardModificarHospital = findViewById(R.id.cardModificarHospital);
        cardEliminarHospital = findViewById(R.id.cardEliminarHospital);
        btnRegresarHospital = findViewById(R.id.btnRegresarHospital);
        btnCrearEspeHospital=findViewById(R.id.btnCrearEspeHospital);
        btnEliminarEspeHospital=findViewById(R.id.btnEliminarEspeHospital);

        btnEliminarEspeHospital.setOnClickListener(v -> {
            startActivity(new Intent(this,EliminarPoseeActivity.class));
        });
        btnCrearEspeHospital.setOnClickListener(v -> {
            startActivity(new Intent(this, CrearPoseeActivity.class));
        });
        btnRegresarHospital.setOnClickListener(v -> {
            finish();
        });

        cardVisualizarHospital.setOnClickListener(v -> {
             startActivity(new Intent(this, VisualizarHospitalesActivity.class));
        });

        cardCrearHospital.setOnClickListener(v -> {
             startActivity(new Intent(this, CrearHospitalActivity.class));
        });

        cardModificarHospital.setOnClickListener(v -> {
            // startActivity(new Intent(this, ModificarPacienteActivity.class));
        });

        cardEliminarHospital.setOnClickListener(v -> {
            // startActivity(new Intent(this, EliminarPacienteActivity.class));
        });
    }
}
