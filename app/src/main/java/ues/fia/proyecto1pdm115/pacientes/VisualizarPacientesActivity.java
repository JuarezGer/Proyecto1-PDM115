package ues.fia.proyecto1pdm115.pacientes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Paciente;

public class VisualizarPacientesActivity extends AppCompatActivity {

    Button btnCargarPacientes, btnRegresarVisualizar;
    TextView txtListaPacientes;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_pacientes);

        helper = new controlDBHospitalApp(this);

        btnCargarPacientes = findViewById(R.id.btnCargarPacientes);
        btnRegresarVisualizar = findViewById(R.id.btnRegresarVisualizar);
        txtListaPacientes = findViewById(R.id.txtListaPacientes);

        btnCargarPacientes.setOnClickListener(v -> cargarPacientes());
        btnRegresarVisualizar.setOnClickListener(v -> finish());

        cargarPacientes();
    }

    private void cargarPacientes() {
        helper.abrir();
        ArrayList<Paciente> lista = helper.consultarTodosPacientes();
        helper.cerrar();

        if (lista.isEmpty()) {
            txtListaPacientes.setText("No hay pacientes registrados.");
            return;
        }

        StringBuilder resultado = new StringBuilder();

        for (Paciente paciente : lista) {
            resultado.append("DUI: ")
                    .append(paciente.getDuiPaciente())
                    .append("\n");

            resultado.append("Nombre: ")
                    .append(valor(paciente.getPrimerNombrePaciente()))
                    .append(" ")
                    .append(valor(paciente.getSegundoNombrePaciente()))
                    .append(" ")
                    .append(valor(paciente.getPrimerApellidoPaciente()))
                    .append(" ")
                    .append(valor(paciente.getSegundoApellidoPaciente()))
                    .append("\n");

            resultado.append("Fecha nacimiento: ")
                    .append(valor(paciente.getFechaNacimientoPaciente()))
                    .append("\n");

            resultado.append("Género: ")
                    .append(valor(paciente.getGeneroPaciente()))
                    .append("\n");

            resultado.append("Teléfono: ")
                    .append(valor(paciente.getTelefonoPaciente()))
                    .append("\n");

            resultado.append("Distrito: ")
                    .append(valor(paciente.getCodDistrito()))
                    .append("\n");

            resultado.append("-----------------------------\n");
        }

        txtListaPacientes.setText(resultado.toString());
    }

    private String valor(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return "";
        }
        return texto;
    }
}