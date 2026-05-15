package ues.fia.proyecto1pdm115.tipo_emergencia;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Tipo_emergencia;
import ues.fia.proyecto1pdm115.R;

public class VisualizarTipoEmergenciaActivity extends AppCompatActivity {

    TableLayout tablaEmergencias;
    Button btnRegresarVisualizar;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_visualizar_tipo_emergencia);

        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        tablaEmergencias = findViewById(R.id.tablaEmergencias);
        btnRegresarVisualizar = findViewById(R.id.btnRegresarVisualizar);

        btnRegresarVisualizar.setOnClickListener(v -> finish());

        cargarTiposEmergencia();
    }

    private void cargarTiposEmergencia() {
        tablaEmergencias.removeAllViews();

        helper.abrir();

        ArrayList<Tipo_emergencia> lista = helper.consultarTodosTiposEmergencia();
        helper.cerrar();

        if (lista.isEmpty()) {
            TableRow fila = new TableRow(this);
            TextView txtVacio = crearCelda("No hay registros", 315);
            txtVacio.setGravity(Gravity.CENTER);
            txtVacio.setPadding(16, 30, 16, 30);
            fila.addView(txtVacio);
            tablaEmergencias.addView(fila);
            return;
        }

        int numero = 1;
        for (Tipo_emergencia tipo : lista) {
            agregarFilaEmergencia(numero, tipo);
            numero++;
        }
    }

    private void agregarFilaEmergencia(int numero, Tipo_emergencia tipo) {
        TableRow fila = new TableRow(this);
        fila.setClickable(true);
        fila.setFocusable(true);
        fila.setPadding(0, 4, 0, 4);


        fila.addView(crearCelda(String.valueOf(numero), 45));
        fila.addView(crearCelda(valor(tipo.getCod_emergencia()), 85));
        fila.addView(crearCelda(valor(tipo.getPrioridad()), 95));
        fila.addView(crearCelda("$" + String.format("%.2f", tipo.getCosto_emergencia()), 90));

        fila.setOnClickListener(v -> mostrarModalEmergencia(tipo));

        tablaEmergencias.addView(fila);
    }

    private TextView crearCelda(String texto, int anchoDp) {
        TextView textView = new TextView(this);
        int anchoPx = convertirDpAPx(anchoDp);
        TableRow.LayoutParams params = new TableRow.LayoutParams(anchoPx, TableRow.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        textView.setText(texto);
        textView.setTextColor(getResources().getColor(R.color.text_black));
        textView.setTextSize(13);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(6, 14, 6, 14);
        return textView;
    }

    private void mostrarModalEmergencia(Tipo_emergencia tipo) {
        String datos = "Código: " + valor(tipo.getCod_emergencia()) + "\n\n" +
                "Prioridad: " + valor(tipo.getPrioridad()) + "\n\n" +
                "Costo: $" + String.format("%.2f", tipo.getCosto_emergencia());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Detalles de Emergencia");
        builder.setMessage(datos);
        builder.setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private String valor(String texto) {
        return (texto == null || texto.trim().isEmpty()) ? "N/A" : texto;
    }

    private int convertirDpAPx(int dp) {
        float escala = getResources().getDisplayMetrics().density;
        return (int) (dp * escala + 0.5f);
    }

}