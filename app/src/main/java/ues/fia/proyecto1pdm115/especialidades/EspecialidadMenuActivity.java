package ues.fia.proyecto1pdm115.especialidades;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;

public class EspecialidadMenuActivity extends AppCompatActivity {

    LinearLayout cardVisualizarEspecialidad, cardCrearEspecialidad, cardModificarEspecialidad, cardEliminarEspecialidad;
    Button btnRegresarEspecialidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_especialidades);
        EdgeToEdge.enable(this);
        Navegador.configurarBarra(this);

        cardVisualizarEspecialidad = findViewById(R.id.cardVisualizarEspecialidad);
        cardCrearEspecialidad = findViewById(R.id.cardCrearEspecialidad);
        cardModificarEspecialidad = findViewById(R.id.cardModificarEspecialidad);
        cardEliminarEspecialidad = findViewById(R.id.cardEliminarEspecialidad);
        btnRegresarEspecialidad = findViewById(R.id.btnRegresarEspecialidad);

        btnRegresarEspecialidad.setOnClickListener(v -> {
            finish();
        });

        cardVisualizarEspecialidad.setOnClickListener(v -> {
            startActivity(new Intent(this, VisualizarEspecialidadesActivity.class));
        });

        cardCrearEspecialidad.setOnClickListener(v -> {
            startActivity(new Intent(this, CrearEspecialidadActivity.class));
        });

        cardModificarEspecialidad.setOnClickListener(v -> {
             startActivity(new Intent(this, ModificarEspecialidadActivity.class));
        });

        cardEliminarEspecialidad.setOnClickListener(v -> {
            startActivity(new Intent(this, EliminarEspecialidadActivity.class));
        });
    }
}
