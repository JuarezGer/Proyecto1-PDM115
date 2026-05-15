package ues.fia.proyecto1pdm115.doctores;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;

public class DoctorMenuActivity extends AppCompatActivity {

    LinearLayout cardVisualizarDoctor, cardCrearDoctor, cardModificarDoctor, cardEliminarDoctor;
    Button btnRegresarDoctores,btnCrearEspeDoc,btnEliminarEspeDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctores);
        Navegador.configurarBarra(this);

        cardVisualizarDoctor = findViewById(R.id.cardVisualizarDoctor);
        cardCrearDoctor = findViewById(R.id.cardCrearDoctor);
        cardModificarDoctor = findViewById(R.id.cardModificarDoctor);
        cardEliminarDoctor = findViewById(R.id.cardEliminarDoctor);
        btnRegresarDoctores = findViewById(R.id.btnRegresarDoctor);
        btnEliminarEspeDoc=findViewById(R.id.btnEliminarEspeDoc);
        btnCrearEspeDoc=findViewById(R.id.btnCrearEspeDoc);

        btnRegresarDoctores.setOnClickListener(v -> {
            finish();
        });

        cardVisualizarDoctor.setOnClickListener(v -> {
             startActivity(new Intent(this, VisualizarDoctorActivity.class));
        });

        cardCrearDoctor.setOnClickListener(v -> {
             startActivity(new Intent(this, CrearDoctorActivity.class));
        });

        cardModificarDoctor.setOnClickListener(v -> {
             startActivity(new Intent(this, ActualizarDoctorActivity.class));
        });

        cardEliminarDoctor.setOnClickListener(v -> {
             startActivity(new Intent(this, EliminarDoctorActivity.class));
        });

        btnCrearEspeDoc.setOnClickListener(v -> {
            startActivity(new Intent(this,CrearCuentaConActivity.class));
        });

        btnEliminarEspeDoc.setOnClickListener(v -> {
            startActivity(new Intent(this, EliminarCuentaConActivity.class));
        });
    }
}
