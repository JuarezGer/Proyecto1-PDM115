package ues.fia.proyecto1pdm115.medicamentos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
public class MedicamentoMenuActivity extends AppCompatActivity {

    LinearLayout cardVisualizarMedicamento, cardCrearMedicamento;
    Button btnRegresarMedicamentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicamentos);
        Navegador.configurarBarra(this);

        cardVisualizarMedicamento = findViewById(R.id.cardVisualizarMedicamento);
        cardCrearMedicamento = findViewById(R.id.cardCrearMedicamento);
        btnRegresarMedicamentos = findViewById(R.id.btnRegresarMedicamentos);

        btnRegresarMedicamentos.setOnClickListener(v -> {
            finish();
        });

        cardVisualizarMedicamento.setOnClickListener(v ->
                startActivity(new Intent(this, VisualizarMedicamentoActivity.class)));

        cardCrearMedicamento.setOnClickListener(v ->
                startActivity(new Intent(this, CrearMedicamentoActivity.class)));

    }
}
