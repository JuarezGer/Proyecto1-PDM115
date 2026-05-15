package ues.fia.proyecto1pdm115.consultas;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;

public class ConsultaMenuActivity extends AppCompatActivity {


    LinearLayout cardVisualizarConsulta, cardCrearConsulta, cardModificarConsulta, cardEliminarConsulta;
    Button btnRegresarConsultas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Asegúrate de crear el layout activity_consultas.xml basado en activity_pacientes.xml
        setContentView(R.layout.activity_consultas);


        Navegador.configurarBarra(this);

        // Inicialización de componentes
        cardVisualizarConsulta = findViewById(R.id.cardVisualizarConsulta);
        cardCrearConsulta = findViewById(R.id.cardCrearConsulta);
        cardModificarConsulta = findViewById(R.id.cardModificarConsulta);
        cardEliminarConsulta = findViewById(R.id.cardEliminarConsulta);
        btnRegresarConsultas = findViewById(R.id.btnRegresarConsultas);


        btnRegresarConsultas.setOnClickListener(v -> {
            finish();
        });


        cardVisualizarConsulta.setOnClickListener(v ->
                startActivity(new Intent(this, VisualizarConsultasActivity.class)));

        cardCrearConsulta.setOnClickListener(v ->
                startActivity(new Intent(this, CrearConsultaActivity.class)));

        cardModificarConsulta.setOnClickListener(v ->
                startActivity(new Intent(this, ModificarConsultaActivity.class)));

        cardEliminarConsulta.setOnClickListener(v ->
                startActivity(new Intent(this, EliminarConsultaActivity.class)));
    }
}