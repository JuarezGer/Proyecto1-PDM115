package ues.fia.proyecto1pdm115.pacientes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Paciente;

public class EliminarPacienteActivity extends AppCompatActivity {

    EditText edtDuiEliminar;
    Button btnBuscarEliminar, btnEliminarPaciente, btnRegresarEliminar;
    TextView txtPacienteEliminar;

    controlDBHospitalApp helper;

    String duiEncontrado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_paciente);

        helper = new controlDBHospitalApp(this);

        edtDuiEliminar = findViewById(R.id.edtDuiEliminar);
        btnBuscarEliminar = findViewById(R.id.btnBuscarEliminar);
        btnEliminarPaciente = findViewById(R.id.btnEliminarPaciente);
        btnRegresarEliminar = findViewById(R.id.btnRegresarEliminar);
        txtPacienteEliminar = findViewById(R.id.txtPacienteEliminar);

        btnBuscarEliminar.setOnClickListener(v -> buscarPaciente());
        btnEliminarPaciente.setOnClickListener(v -> confirmarEliminacion());
        btnRegresarEliminar.setOnClickListener(v -> finish());
    }

    private void buscarPaciente() {
        String dui = edtDuiEliminar.getText().toString().trim();

        if (dui.isEmpty()) {
            Toast.makeText(this, "Ingrese el DUI del paciente", Toast.LENGTH_SHORT).show();
            return;
        }

        helper.abrir();
        Paciente paciente = helper.consultarPaciente(dui);
        helper.cerrar();

        if (paciente != null) {
            duiEncontrado = paciente.getDuiPaciente();

            String datos = "DUI: " + paciente.getDuiPaciente() + "\n" +
                    "Nombre: " + valor(paciente.getPrimerNombrePaciente()) + " " +
                    valor(paciente.getPrimerApellidoPaciente()) + "\n" +
                    "Teléfono: " + valor(paciente.getTelefonoPaciente()) + "\n" +
                    "Género: " + valor(paciente.getGeneroPaciente());

            txtPacienteEliminar.setText(datos);

            Toast.makeText(this, "Paciente encontrado", Toast.LENGTH_SHORT).show();
        } else {
            duiEncontrado = "";
            txtPacienteEliminar.setText("Paciente no encontrado.");
            Toast.makeText(this, "Paciente no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmarEliminacion() {
        if (duiEncontrado.isEmpty()) {
            Toast.makeText(this, "Primero busque un paciente válido", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Está seguro de eliminar este paciente?");

        builder.setPositiveButton("Sí, eliminar", (dialog, which) -> {
            eliminarPaciente();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void eliminarPaciente() {
        if (duiEncontrado.isEmpty()) {
            Toast.makeText(this, "Primero busque un paciente válido", Toast.LENGTH_SHORT).show();
            return;
        }

        helper.abrir();
        String mensaje = helper.eliminarPaciente(duiEncontrado);
        helper.cerrar();

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

        edtDuiEliminar.setText("");
        txtPacienteEliminar.setText("Datos del paciente...");
        duiEncontrado = "";
    }

    private String valor(String texto) {
        if (texto == null) {
            return "";
        }
        return texto;
    }
}