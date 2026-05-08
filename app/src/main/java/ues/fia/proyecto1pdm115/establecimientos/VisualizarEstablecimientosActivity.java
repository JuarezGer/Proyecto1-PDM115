package ues.fia.proyecto1pdm115.establecimientos;

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
import ues.fia.proyecto1pdm115.modelos.Establecimiento;

public class VisualizarEstablecimientosActivity extends AppCompatActivity {

    TableLayout tablaEstablecimientos;
    Button btnRegresarVisualizarEstablecimientos;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_establecimientos);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        tablaEstablecimientos = findViewById(R.id.tablaEstablecimientos);
        btnRegresarVisualizarEstablecimientos = findViewById(R.id.btnRegresarVisualizarEstablecimientos);

        btnRegresarVisualizarEstablecimientos.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarEstablecimientos();
    }

    private void cargarEstablecimientos() {
        tablaEstablecimientos.removeAllViews();

        try {
            helper.abrir();
            ArrayList<Establecimiento> lista = helper.consultarTodosEstablecimientos();

            if (lista.isEmpty()) {
                agregarFilaVacia();
                return;
            }

            int numero = 1;
            for (Establecimiento establecimiento : lista) {
                agregarFilaEstablecimiento(numero, establecimiento);
                numero++;
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar establecimientos: " + e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            helper.cerrar();
        }
    }

    private void agregarFilaEstablecimiento(int numero, Establecimiento establecimiento) {
        TableRow fila = new TableRow(this);
        fila.setGravity(Gravity.CENTER_VERTICAL);
        fila.setPadding(0, dpToPx(6), 0, dpToPx(6));
        fila.setBackgroundColor(ContextCompat.getColor(this, android.R.color.white));

        TextView txtNumero = crearCelda(String.valueOf(numero), 45);
        TextView txtId = crearCelda(String.valueOf(establecimiento.getIdEstablecimiento()), 55);
        TextView txtNombre = crearCelda(establecimiento.getNombreEstablecimiento(), 120);
        TextView txtTelefono = crearCelda(establecimiento.getTelefonoEstablecimiento(), 90);

        fila.addView(txtNumero);
        fila.addView(txtId);
        fila.addView(txtNombre);
        fila.addView(txtTelefono);

        fila.setOnClickListener(v -> mostrarDetalleEstablecimiento(establecimiento));

        tablaEstablecimientos.addView(fila);
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
        TextView texto = crearCelda("No hay establecimientos registrados", 310);
        fila.addView(texto);
        tablaEstablecimientos.addView(fila);
    }

    private void mostrarDetalleEstablecimiento(Establecimiento establecimiento) {
        String mensaje = "ID: " + establecimiento.getIdEstablecimiento() + "\n" +
                "Nombre: " + establecimiento.getNombreEstablecimiento() + "\n" +
                "Teléfono: " + establecimiento.getTelefonoEstablecimiento() + "\n" +
                "Dirección: " + establecimiento.getDireccionEstablecimiento();

        new AlertDialog.Builder(this)
                .setTitle("Detalle del establecimiento")
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
