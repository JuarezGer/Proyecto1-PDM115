package ues.fia.proyecto1pdm115.pagos;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Pago;

public class VisualizarPagosActivity extends AppCompatActivity {

    TableLayout tablaPagos;
    Button btnRegresarVisualizarPagos;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_pagos);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        tablaPagos = findViewById(R.id.tablaPagos);
        btnRegresarVisualizarPagos = findViewById(R.id.btnRegresarVisualizarPagos);

        btnRegresarVisualizarPagos.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarPagos();
    }

    private void cargarPagos() {
        tablaPagos.removeAllViews();

        try {
            helper.abrir();
            ArrayList<Pago> lista = helper.consultarTodosPagos();

            if (lista.isEmpty()) {
                agregarFilaVacia();
                return;
            }

            int numero = 1;
            for (Pago pago : lista) {
                agregarFilaPago(numero, pago);
                numero++;
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar pagos: " + e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            helper.cerrar();
        }
    }

    private void agregarFilaPago(int numero, Pago pago) {
        TableRow fila = new TableRow(this);
        fila.setGravity(Gravity.CENTER_VERTICAL);
        fila.setPadding(0, dpToPx(6), 0, dpToPx(6));
        fila.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));

        TextView txtNumero = crearCelda(String.valueOf(numero), 45);
        TextView txtIdPago = crearCelda(String.valueOf(pago.getIdPago()), 65);
        TextView txtIdConsulta = crearCelda(String.valueOf(pago.getIdConsulta()), 80);
        TextView txtTipo = crearCelda(pago.getTipoPago(), 95);
        TextView txtMonto = crearCelda("$" + pago.getMontoTotal(), 85);
        TextView txtFecha = crearCelda(pago.getFechaPago(), 95);

        fila.addView(txtNumero);
        fila.addView(txtIdPago);
        fila.addView(txtIdConsulta);
        fila.addView(txtTipo);
        fila.addView(txtMonto);
        fila.addView(txtFecha);

        fila.setOnClickListener(v -> mostrarDetallePago(pago));

        tablaPagos.addView(fila);
    }

    private TextView crearCelda(String texto, int anchoDp) {
        TextView textView = new TextView(this);
        textView.setLayoutParams(new TableRow.LayoutParams(dpToPx(anchoDp), TableRow.LayoutParams.WRAP_CONTENT));
        textView.setText(texto);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
        textView.setTextColor(ContextCompat.getColor(this, R.color.text_black));
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(dpToPx(4), dpToPx(8), dpToPx(4), dpToPx(8));
        textView.setSingleLine(false);
        return textView;
    }

    private void agregarFilaVacia() {
        TableRow fila = new TableRow(this);
        TextView texto = crearCelda("No hay pagos registrados", 465);
        fila.addView(texto);
        tablaPagos.addView(fila);
    }

    private void mostrarDetallePago(Pago pago) {
        String mensaje = "ID pago: " + pago.getIdPago() + "\n" +
                "ID consulta: " + pago.getIdConsulta() + "\n" +
                "Tipo de pago: " + pago.getTipoPago() + "\n" +
                "Monto total: $" + pago.getMontoTotal() + "\n" +
                "Fecha de pago: " + pago.getFechaPago();

        new AlertDialog.Builder(this)
                .setTitle("Detalle del pago")
                .setMessage(mensaje)
                .setPositiveButton("Cerrar", null)
                .show();
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}
