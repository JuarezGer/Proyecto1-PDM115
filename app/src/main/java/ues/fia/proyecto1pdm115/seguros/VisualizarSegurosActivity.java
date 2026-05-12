package ues.fia.proyecto1pdm115.seguros;

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
import ues.fia.proyecto1pdm115.modelos.Seguro;

public class VisualizarSegurosActivity extends AppCompatActivity {

    TableLayout tablaSeguros;
    Button btnRegresarVisualizarSeguros;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_seguros);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        tablaSeguros = findViewById(R.id.tablaSeguros);
        btnRegresarVisualizarSeguros = findViewById(R.id.btnRegresarVisualizarSeguros);

        btnRegresarVisualizarSeguros.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarSeguros();
    }

    private void cargarSeguros() {
        tablaSeguros.removeAllViews();

        try {
            helper.abrir();
            ArrayList<Seguro> lista = helper.consultarTodosSeguros();

            if (lista.isEmpty()) {
                agregarFilaVacia();
                return;
            }

            int numero = 1;
            for (Seguro seguro : lista) {
                agregarFilaSeguro(numero, seguro);
                numero++;
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar seguros: " + e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            helper.cerrar();
        }
    }

    private void agregarFilaSeguro(int numero, Seguro seguro) {
        TableRow fila = new TableRow(this);
        fila.setGravity(Gravity.CENTER_VERTICAL);
        fila.setPadding(0, dpToPx(6), 0, dpToPx(6));
        fila.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));

        TextView txtNumero = crearCelda(String.valueOf(numero), 45);
        TextView txtIdPoliza = crearCelda(String.valueOf(seguro.getIdPoliza()), 75);
        TextView txtDuiPaciente = crearCelda(seguro.getDuiPaciente(), 115);
        TextView txtAseguradora = crearCelda(String.valueOf(seguro.getIdAseguradora()), 95);
        TextView txtCobertura = crearCelda(seguro.getPorcentajeCobertura() + "%", 90);
        TextView txtTipo = crearCelda(seguro.getTipoAsegurado(), 110);

        fila.addView(txtNumero);
        fila.addView(txtIdPoliza);
        fila.addView(txtDuiPaciente);
        fila.addView(txtAseguradora);
        fila.addView(txtCobertura);
        fila.addView(txtTipo);

        fila.setOnClickListener(v -> mostrarDetalleSeguro(seguro));

        tablaSeguros.addView(fila);
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
        TextView texto = crearCelda("No hay seguros registrados", 530);
        fila.addView(texto);
        tablaSeguros.addView(fila);
    }

    private void mostrarDetalleSeguro(Seguro seguro) {
        String mensaje = "ID póliza: " + seguro.getIdPoliza() + "\n" +
                "DUI paciente: " + seguro.getDuiPaciente() + "\n" +
                "ID aseguradora: " + seguro.getIdAseguradora() + "\n" +
                "Porcentaje cobertura: " + seguro.getPorcentajeCobertura() + "%\n" +
                "Tipo asegurado: " + seguro.getTipoAsegurado() + "\n" +
                "Deductible medicina: $" + seguro.getDeductibleMedicina() + "\n" +
                "Deductible operación: $" + seguro.getDeductibleOperacion();

        new AlertDialog.Builder(this)
                .setTitle("Detalle del seguro")
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
