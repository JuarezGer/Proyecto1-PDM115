package ues.fia.proyecto1pdm115.doctores;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import ues.fia.proyecto1pdm115.R;

public class DoctorMenuActivity extends AppCompatActivity {

    LinearLayout cardVisualizarDoctor, cardCrearDoctor, cardModificarDoctor, cardEliminarDoctor;
    Button btnRegresarDoctores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctores);

        cardVisualizarDoctor = findViewById(R.id.cardVisualizarDoctor);
        cardCrearDoctor = findViewById(R.id.cardCrearDoctor);
        cardModificarDoctor = findViewById(R.id.cardModificarDoctor);
        cardEliminarDoctor = findViewById(R.id.cardEliminarDoctor);
        btnRegresarDoctores = findViewById(R.id.btnRegresarDoctor);

        btnRegresarDoctores.setOnClickListener(v -> {
            finish();
        });

        cardVisualizarDoctor.setOnClickListener(v -> {
            // startActivity(new Intent(this, VisualizarPacientesActivity.class));
        });

        cardCrearDoctor.setOnClickListener(v -> {
            // startActivity(new Intent(this, CrearPacienteActivity.class));
        });

        cardModificarDoctor.setOnClickListener(v -> {
            // startActivity(new Intent(this, ModificarPacienteActivity.class));
        });

        cardEliminarDoctor.setOnClickListener(v -> {
            // startActivity(new Intent(this, EliminarPacienteActivity.class));
        });
    }
}
