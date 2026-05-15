package ues.fia.proyecto1pdm115.tipo_emergencia;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Tipo_emergencia;
import ues.fia.proyecto1pdm115.R;

public class EliminarTipoEmergenciaActivity extends AppCompatActivity {

    EditText edtCodEmergenciaEliminar;
    Button btnBuscarEliminar, btnEliminarEmergencia, btnRegresarEliminar;
    TextView txtEmergenciaEliminar;

    controlDBHospitalApp helper;
    String codigoEncontrado = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eliminar_tipo_emergencia);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtCodEmergenciaEliminar = findViewById(R.id.edtCodEmergenciaEliminar);
        btnBuscarEliminar = findViewById(R.id.btnBuscarEliminar);
        btnEliminarEmergencia = findViewById(R.id.btnEliminarEmergencia);
        btnRegresarEliminar = findViewById(R.id.btnRegresarEliminar);
        txtEmergenciaEliminar = findViewById(R.id.txtEmergenciaEliminar);

        btnBuscarEliminar.setOnClickListener(v -> buscarTipoEmergencia());
        btnEliminarEmergencia.setOnClickListener(v -> confirmarEliminacion());
        btnRegresarEliminar.setOnClickListener(v -> finish());
    }

    private void buscarTipoEmergencia() {
        String codigo = edtCodEmergenciaEliminar.getText().toString().trim();

        if (codigo.isEmpty()) {
            Toast.makeText(this, "Ingrese el código de la emergencia", Toast.LENGTH_SHORT).show();
            return;
        }

        helper.abrir();
        Tipo_emergencia tipo = helper.consultarTipoEmergencia(codigo);
        helper.cerrar();

        if (tipo != null) {
            codigoEncontrado = tipo.getCod_emergencia();

            String datos = "Código: " + tipo.getCod_emergencia() + "\n" +
                    "Prioridad: " + tipo.getPrioridad() + "\n" +
                    "Costo: $" + String.format("%.2f", tipo.getCosto_emergencia());

            txtEmergenciaEliminar.setText(datos);
            Toast.makeText(this, "Registro encontrado", Toast.LENGTH_SHORT).show();
        } else {
            codigoEncontrado = "";
            txtEmergenciaEliminar.setText("Registro no encontrado.");
            Toast.makeText(this, "No se encontró el tipo de emergencia", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmarEliminacion() {
        if (codigoEncontrado.isEmpty()) {
            Toast.makeText(this, "Primero busque un código válido", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Está seguro de eliminar este tipo de emergencia?");

        builder.setPositiveButton("Sí, eliminar", (dialog, which) -> {
            eliminarTipoEmergencia();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.dismiss();
        });

        builder.create().show();
    }

    private void eliminarTipoEmergencia() {
        helper.abrir();
        String mensaje = helper.eliminarTipoEmergencia(codigoEncontrado);
        helper.cerrar();

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

        // Limpiar después de eliminar
        edtCodEmergenciaEliminar.setText("");
        txtEmergenciaEliminar.setText("Datos de la emergencia...");
        codigoEncontrado = "";
    }

}