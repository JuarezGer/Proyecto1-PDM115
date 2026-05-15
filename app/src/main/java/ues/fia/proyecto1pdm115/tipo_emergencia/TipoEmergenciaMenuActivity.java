package ues.fia.proyecto1pdm115.tipo_emergencia;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.widget.Button;
import android.widget.LinearLayout;
import ues.fia.proyecto1pdm115.Navegador;

import ues.fia.proyecto1pdm115.R;


public class TipoEmergenciaMenuActivity extends AppCompatActivity {

    LinearLayout cardVisualizarEmergencia, cardCrearEmergencia, cardModificarEmergencia, cardEliminarEmergencia;
    Button btnRegresarEmergencias;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tipo_emergencia);

        Navegador.configurarBarra(this);

        cardVisualizarEmergencia = findViewById(R.id.cardVisualizarEmergencia);
        cardCrearEmergencia = findViewById(R.id.cardCrearEmergencia);
        cardModificarEmergencia = findViewById(R.id.cardModificarEmergencia);
        cardEliminarEmergencia = findViewById(R.id.cardEliminarEmergencia);
        btnRegresarEmergencias = findViewById(R.id.btnRegresarEmergencias);


        btnRegresarEmergencias.setOnClickListener(v -> {
            finish();
        });


        cardVisualizarEmergencia.setOnClickListener(v ->
                startActivity(new Intent(this, VisualizarTipoEmergenciaActivity.class)));

        cardCrearEmergencia.setOnClickListener(v ->
                startActivity(new Intent(this, CrearTipoEmergenciaActivity.class)));

        cardModificarEmergencia.setOnClickListener(v ->
                startActivity(new Intent(this, ModificarTipoEmergenciaActivity.class)));

        cardEliminarEmergencia.setOnClickListener(v ->
                startActivity(new Intent(this, EliminarTipoEmergenciaActivity.class)));
    }

}