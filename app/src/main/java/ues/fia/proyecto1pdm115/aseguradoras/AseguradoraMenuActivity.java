package ues.fia.proyecto1pdm115.aseguradoras;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;

public class AseguradoraMenuActivity extends AppCompatActivity {

    LinearLayout cardVisualizarAseguradora, cardCrearAseguradora, cardModificarAseguradora, cardEliminarAseguradora;
    Button btnRegresarAseguradoras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aseguradoras);
        Navegador.configurarBarra(this);

        cardVisualizarAseguradora = findViewById(R.id.cardVisualizarAseguradora);
        cardCrearAseguradora = findViewById(R.id.cardCrearAseguradora);
        cardModificarAseguradora = findViewById(R.id.cardModificarAseguradora);
        cardEliminarAseguradora = findViewById(R.id.cardEliminarAseguradora);
        btnRegresarAseguradoras = findViewById(R.id.btnRegresarAseguradoras);

        btnRegresarAseguradoras.setOnClickListener(v -> {
            finish();
        });

        cardVisualizarAseguradora.setOnClickListener(v ->
                startActivity(new Intent(this, VisualizarAseguradorasActivity.class)));

        cardCrearAseguradora.setOnClickListener(v ->
                startActivity(new Intent(this, CrearAseguradoraActivity.class)));

        cardModificarAseguradora.setOnClickListener(v ->
                startActivity(new Intent(this, ActualizarAseguradoraActivity.class)));

        cardEliminarAseguradora.setOnClickListener(v ->
                startActivity(new Intent(this, EliminarAseguradoraActivity.class)));
    }
}
