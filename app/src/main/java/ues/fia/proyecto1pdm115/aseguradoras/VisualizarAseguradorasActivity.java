package ues.fia.proyecto1pdm115.aseguradoras;

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
import ues.fia.proyecto1pdm115.modelos.Aseguradora;

public class VisualizarAseguradorasActivity extends AppCompatActivity {

    TableLayout tablaAseguradoras;
    Button btnRegresarVisualizarAseguradoras;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_aseguradoras);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        tablaAseguradoras = findViewById(R.id.tablaAseguradoras);
        btnRegresarVisualizarAseguradoras = findViewById(R.id.btnRegresarVisualizarAseguradoras);

        btnRegresarVisualizarAseguradoras.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarAseguradoras();
    }

    private void cargarAseguradoras() {
        tablaAseguradoras.removeAllViews();

        try {
            helper.abrir();
            ArrayList<Aseguradora> lista = helper.consultarTodasAseguradoras();

            if (lista.isEmpty()) {
                agregarFilaVacia();
                return;
            }

            int numero = 1;
            for (Aseguradora aseguradora : lista) {
                agregarFilaAseguradora(numero, aseguradora);
                numero++;
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar aseguradoras: " + e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            helper.cerrar();
        }
    }

    private void agregarFilaAseguradora(int numero, Aseguradora aseguradora) {
        TableRow fila = new TableRow(this);
        fila.setGravity(Gravity.CENTER_VERTICAL);
        fila.setPadding(0, dpToPx(6), 0, dpToPx(6));
        fila.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));

        TextView txtNumero = crearCelda(String.valueOf(numero), 45);
        TextView txtId = crearCelda(String.valueOf(aseguradora.getIdAseguradora()), 55);
        TextView txtNombre = crearCelda(aseguradora.getNombreAseguradora(), 120);
        TextView txtTelefono = crearCelda(aseguradora.getTelefonoAseguradora(), 90);

        fila.addView(txtNumero);
        fila.addView(txtId);
        fila.addView(txtNombre);
        fila.addView(txtTelefono);

        fila.setOnClickListener(v -> mostrarDetalleAseguradora(aseguradora));

        tablaAseguradoras.addView(fila);
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
        TextView texto = crearCelda("No hay aseguradoras registradas", 310);
        fila.addView(texto);
        tablaAseguradoras.addView(fila);
    }

    private void mostrarDetalleAseguradora(Aseguradora aseguradora) {
        String mensaje = "ID: " + aseguradora.getIdAseguradora() + "\n" +
                "Nombre: " + aseguradora.getNombreAseguradora() + "\n" +
                "Teléfono: " + aseguradora.getTelefonoAseguradora();

        new AlertDialog.Builder(this)
                .setTitle("Detalle de la aseguradora")
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
