package ues.fia.proyecto1pdm115.pagos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;

public class PagoMenuActivity extends AppCompatActivity {

    LinearLayout cardVisualizarPago, cardCrearPago, cardModificarPago, cardEliminarPago;
    Button btnRegresarPago;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagos);
        Navegador.configurarBarra(this);

        cardVisualizarPago = findViewById(R.id.cardVisualizarPago);
        cardCrearPago = findViewById(R.id.cardCrearPago);
        cardModificarPago = findViewById(R.id.cardModificarPago);
        cardEliminarPago = findViewById(R.id.cardEliminarPago);
        btnRegresarPago = findViewById(R.id.btnRegresarPagos);

        btnRegresarPago.setOnClickListener(v -> {
            finish();
        });

        cardVisualizarPago.setOnClickListener(v ->
                startActivity(new Intent(this, VisualizarPagosActivity.class)));

        cardCrearPago.setOnClickListener(v ->
                startActivity(new Intent(this, CrearPagoActivity.class)));

        cardModificarPago.setOnClickListener(v ->
                startActivity(new Intent(this, ModificarPagoActivity.class)));

        cardEliminarPago.setOnClickListener(v ->
                startActivity(new Intent(this, EliminarPagoActivity.class)));
    }
}
