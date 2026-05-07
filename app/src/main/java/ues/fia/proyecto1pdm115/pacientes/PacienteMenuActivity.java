package ues.fia.proyecto1pdm115.pacientes;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;

public class PacienteMenuActivity extends AppCompatActivity {

    LinearLayout cardVisualizarPaciente, cardCrearPaciente, cardModificarPaciente, cardEliminarPaciente;
    Button btnRegresarPacientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pacientes);
        Navegador.configurarBarra(this);

        cardVisualizarPaciente = findViewById(R.id.cardVisualizarPaciente);
        cardCrearPaciente = findViewById(R.id.cardCrearPaciente);
        cardModificarPaciente = findViewById(R.id.cardModificarPaciente);
        cardEliminarPaciente = findViewById(R.id.cardEliminarPaciente);
        btnRegresarPacientes = findViewById(R.id.btnRegresarPacientes);

        btnRegresarPacientes.setOnClickListener(v -> {
            finish();
        });

        cardVisualizarPaciente.setOnClickListener(v ->
                startActivity(new Intent(this, VisualizarPacientesActivity.class)));

        cardCrearPaciente.setOnClickListener(v ->
                startActivity(new Intent(this, CrearPacienteActivity.class)));

        cardModificarPaciente.setOnClickListener(v ->
                startActivity(new Intent(this, ModificarPacienteActivity.class)));

        cardEliminarPaciente.setOnClickListener(v ->
                startActivity(new Intent(this, EliminarPacienteActivity.class)));
    }
}
