package ues.fia.proyecto1pdm115.consultas;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Consulta;

public class EliminarConsultaActivity extends AppCompatActivity {

    EditText edtIdConsultaEliminar;
    Button btnBuscarEliminar, btnEliminarConsulta, btnRegresarEliminar;
    TextView txtConsultaEliminar;

    controlDBHospitalApp helper;

    int idEncontrado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_consulta);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        // Enlace de vistas
        edtIdConsultaEliminar = findViewById(R.id.edtIdConsultaEliminar);
        btnBuscarEliminar = findViewById(R.id.btnBuscarEliminar);
        btnEliminarConsulta = findViewById(R.id.btnEliminarConsulta);
        btnRegresarEliminar = findViewById(R.id.btnRegresarEliminar);
        txtConsultaEliminar = findViewById(R.id.txtConsultaEliminar);

        // Eventos
        btnBuscarEliminar.setOnClickListener(v -> buscarConsulta());
        btnEliminarConsulta.setOnClickListener(v -> confirmarEliminacion());
        btnRegresarEliminar.setOnClickListener(v -> finish());
    }

    private void buscarConsulta() {
        String idStr = edtIdConsultaEliminar.getText().toString().trim();

        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID de la consulta", Toast.LENGTH_SHORT).show();
            return;
        }

        helper.abrir();
        Consulta consulta = helper.consultarConsulta(Integer.parseInt(idStr));
        helper.cerrar();

        if (consulta != null) {
            idEncontrado = consulta.getIdConsulta();

            String datos = "ID Consulta: " + consulta.getIdConsulta() + "\n" +
                    "Paciente (DUI): " + consulta.getDuiPaciente() + "\n" +
                    "Doctor (DUI): " + consulta.getDuiDoctor() + "\n" +
                    "Fecha: " + consulta.getFechaConsulta() + "\n" +
                    "Diagnóstico: " + valor(consulta.getDiagnostico()) + "\n" +
                    "Total: $" + consulta.getCargoTotalConsulta();

            txtConsultaEliminar.setText(datos);
            Toast.makeText(this, "Consulta encontrada", Toast.LENGTH_SHORT).show();
        } else {
            idEncontrado = -1;
            txtConsultaEliminar.setText("Consulta no encontrada.");
            Toast.makeText(this, "Consulta no encontrada", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmarEliminacion() {
        if (idEncontrado == -1) {
            Toast.makeText(this, "Primero busque una consulta válida", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Está seguro de eliminar esta consulta? Esta acción no se puede deshacer.");

        builder.setPositiveButton("Sí, eliminar", (dialog, which) -> {
            eliminarConsulta();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void eliminarConsulta() {
        if (idEncontrado == -1) {
            return;
        }

        helper.abrir();
        String mensaje = helper.eliminarConsulta(idEncontrado);
        helper.cerrar();

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

        // Limpiar pantalla tras éxito o error
        edtIdConsultaEliminar.setText("");
        txtConsultaEliminar.setText("Datos de la consulta...");
        idEncontrado = -1;
    }

    private String valor(String texto) {
        if (texto == null || texto.isEmpty()) {
            return "N/A";
        }
        return texto;
    }
}