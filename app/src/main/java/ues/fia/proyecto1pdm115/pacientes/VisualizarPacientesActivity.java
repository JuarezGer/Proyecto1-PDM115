package ues.fia.proyecto1pdm115.pacientes;

import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Paciente;

public class VisualizarPacientesActivity extends AppCompatActivity {

    TableLayout tablaPacientes;
    Button btnRegresarVisualizar;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_pacientes);

        helper = new controlDBHospitalApp(this);

        tablaPacientes = findViewById(R.id.tablaPacientes);
        btnRegresarVisualizar = findViewById(R.id.btnRegresarVisualizar);

        btnRegresarVisualizar.setOnClickListener(v -> finish());

        cargarPacientes();
    }

    private void cargarPacientes() {
        tablaPacientes.removeAllViews();

        helper.abrir();
        ArrayList<Paciente> lista = helper.consultarTodosPacientes();
        helper.cerrar();

        if (lista.isEmpty()) {
            TableRow fila = new TableRow(this);

            TextView txtVacio = crearCelda("No hay pacientes registrados", 315);
            txtVacio.setGravity(Gravity.CENTER);
            txtVacio.setPadding(16, 30, 16, 30);

            fila.addView(txtVacio);
            tablaPacientes.addView(fila);
            return;
        }

        int numero = 1;

        for (Paciente paciente : lista) {
            agregarFilaPaciente(numero, paciente);
            numero++;
        }
    }

    private void agregarFilaPaciente(int numero, Paciente paciente) {
        TableRow fila = new TableRow(this);
        fila.setClickable(true);
        fila.setFocusable(true);
        fila.setPadding(0, 4, 0, 4);

        fila.addView(crearCelda(String.valueOf(numero), 45));
        fila.addView(crearCelda(valor(paciente.getDuiPaciente()), 85));
        fila.addView(crearCelda(valor(paciente.getPrimerNombrePaciente()), 95));
        fila.addView(crearCelda(valor(paciente.getTelefonoPaciente()), 90));

        fila.setOnClickListener(v -> mostrarModalPaciente(paciente));

        tablaPacientes.addView(fila);
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

    private void mostrarModalPaciente(Paciente paciente) {
        String datos = "DUI: " + valor(paciente.getDuiPaciente()) + "\n\n" +
                "Código distrito: " + valor(paciente.getCodDistrito()) + "\n\n" +
                "Primer nombre: " + valor(paciente.getPrimerNombrePaciente()) + "\n" +
                "Segundo nombre: " + valor(paciente.getSegundoNombrePaciente()) + "\n\n" +
                "Primer apellido: " + valor(paciente.getPrimerApellidoPaciente()) + "\n" +
                "Segundo apellido: " + valor(paciente.getSegundoApellidoPaciente()) + "\n\n" +
                "Fecha nacimiento: " + valor(paciente.getFechaNacimientoPaciente()) + "\n" +
                "Género: " + valor(paciente.getGeneroPaciente()) + "\n" +
                "Teléfono: " + valor(paciente.getTelefonoPaciente());

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Datos del paciente");
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