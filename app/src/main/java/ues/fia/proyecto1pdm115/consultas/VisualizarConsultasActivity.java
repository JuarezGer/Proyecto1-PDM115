package ues.fia.proyecto1pdm115.consultas;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Consulta;

public class VisualizarConsultasActivity extends AppCompatActivity {

    TableLayout tablaConsultas;
    Button btnRegresarVisualizar;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Asegúrate de tener el layout activity_visualizar_consultas.xml
        setContentView(R.layout.activity_visualizar_consultas);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        tablaConsultas = findViewById(R.id.tablaConsultas);
        btnRegresarVisualizar = findViewById(R.id.btnRegresarVisualizar);

        btnRegresarVisualizar.setOnClickListener(v -> finish());

        cargarConsultas();
    }

    private void cargarConsultas() {
        tablaConsultas.removeAllViews();

        helper.abrir();
        // Debes implementar este método en tu helper
        ArrayList<Consulta> lista = helper.consultarTodasConsultas();
        helper.cerrar();

        if (lista.isEmpty()) {
            TableRow fila = new TableRow(this);
            TextView txtVacio = crearCelda("No hay consultas registradas", 315);
            txtVacio.setGravity(Gravity.CENTER);
            txtVacio.setPadding(16, 30, 16, 30);
            fila.addView(txtVacio);
            tablaConsultas.addView(fila);
            return;
        }

        int numero = 1;
        for (Consulta consulta : lista) {
            agregarFilaConsulta(numero, consulta);
            numero++;
        }
    }

    private void agregarFilaConsulta(int numero, Consulta consulta) {
        TableRow fila = new TableRow(this);
        fila.setClickable(true);
        fila.setFocusable(true);
        fila.setPadding(0, 4, 0, 4);

        // Columnas: #, ID, Paciente (DUI), Fecha
        fila.addView(crearCelda(String.valueOf(numero), 40));
        fila.addView(crearCelda(String.valueOf(consulta.getIdConsulta()), 50));
        fila.addView(crearCelda(valor(consulta.getDuiPaciente()), 110));
        fila.addView(crearCelda(valor(consulta.getFechaConsulta()), 115));

        fila.setOnClickListener(v -> mostrarModalConsulta(consulta));

        tablaConsultas.addView(fila);
    }

    private TextView crearCelda(String texto, int anchoDp) {
        TextView textView = new TextView(this);
        int anchoPx = convertirDpAPx(anchoDp);
        TableRow.LayoutParams params = new TableRow.LayoutParams(anchoPx, TableRow.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(params);
        textView.setText(texto);
        textView.setTextColor(getResources().getColor(R.color.text_black));
        textView.setTextSize(12); // Un poco más pequeña para que quepa la información
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(4, 14, 4, 14);
        return textView;
    }

    private void mostrarModalConsulta(Consulta consulta) {
        String pagaMed = (consulta.getPagaMedicamento() == 1) ? "Sí" : "No";

        String datos = "ID Consulta: " + consulta.getIdConsulta() + "\n\n" +
                "Paciente (DUI): " + valor(consulta.getDuiPaciente()) + "\n" +
                "Doctor (DUI): " + valor(consulta.getDuiDoctor()) + "\n\n" +
                "Fecha: " + valor(consulta.getFechaConsulta()) + "\n" +
                "Emergencia: " + valor(consulta.getCodEmergencia()) + "\n\n" +
                "DIAGNÓSTICO:\n" + valor(consulta.getDiagnostico()) + "\n\n" +
                "Cargo Total: $" + consulta.getCargoTotalConsulta() + "\n" +
                "Paga Medicamento: " + pagaMed;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Detalle de Consulta Médica");
        builder.setMessage(datos);
        builder.setPositiveButton("Cerrar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String valor(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return "N/A";
        }
        return texto;
    }

    private int convertirDpAPx(int dp) {
        float escala = getResources().getDisplayMetrics().density;
        return (int) (dp * escala + 0.5f);
    }
}