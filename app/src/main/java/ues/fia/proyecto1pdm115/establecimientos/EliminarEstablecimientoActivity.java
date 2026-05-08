package ues.fia.proyecto1pdm115.establecimientos;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Establecimiento;

public class EliminarEstablecimientoActivity extends AppCompatActivity {

    EditText edtIdEstablecimientoEliminar;
    TextView txtDatosEstablecimientoEliminar;
    Button btnBuscarEstablecimientoEliminar, btnEliminarEstablecimiento,
            btnRegresarEliminarEstablecimiento;

    controlDBHospitalApp helper;
    int idEncontrado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_establecimiento);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtIdEstablecimientoEliminar = findViewById(R.id.edtIdEstablecimientoEliminar);
        txtDatosEstablecimientoEliminar = findViewById(R.id.txtDatosEstablecimientoEliminar);
        btnBuscarEstablecimientoEliminar = findViewById(R.id.btnBuscarEstablecimientoEliminar);
        btnEliminarEstablecimiento = findViewById(R.id.btnEliminarEstablecimiento);
        btnRegresarEliminarEstablecimiento = findViewById(R.id.btnRegresarEliminarEstablecimiento);

        btnEliminarEstablecimiento.setEnabled(false);

        btnBuscarEstablecimientoEliminar.setOnClickListener(v -> buscarEstablecimiento());
        btnEliminarEstablecimiento.setOnClickListener(v -> confirmarEliminacion());
        btnRegresarEliminarEstablecimiento.setOnClickListener(v -> finish());
    }

    private void buscarEstablecimiento() {
        String idTexto = edtIdEstablecimientoEliminar.getText().toString().trim();

        if (idTexto.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID del establecimiento", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int id = Integer.parseInt(idTexto);

            helper.abrir();
            Establecimiento establecimiento = helper.consultarEstablecimiento(id);

            if (establecimiento == null) {
                idEncontrado = -1;
                btnEliminarEstablecimiento.setEnabled(false);
                txtDatosEstablecimientoEliminar.setText("No se encontró el establecimiento.");
                Toast.makeText(this, "Establecimiento no encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            idEncontrado = establecimiento.getIdEstablecimiento();
            btnEliminarEstablecimiento.setEnabled(true);

            String datos = "ID: " + establecimiento.getIdEstablecimiento() + "\n" +
                    "Nombre: " + establecimiento.getNombreEstablecimiento() + "\n" +
                    "Teléfono: " + establecimiento.getTelefonoEstablecimiento() + "\n" +
                    "Dirección: " + establecimiento.getDireccionEstablecimiento();

            txtDatosEstablecimientoEliminar.setText(datos);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "El ID debe ser numérico", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            helper.cerrar();
        }
    }

    private void confirmarEliminacion() {
        if (idEncontrado == -1) {
            Toast.makeText(this, "Primero busque un establecimiento", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Desea eliminar este establecimiento?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarEstablecimiento())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarEstablecimiento() {
        try {
            helper.abrir();
            String mensaje = helper.eliminarEstablecimiento(idEncontrado);
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

            if (mensaje.toLowerCase().contains("correctamente")) {
                limpiarCampos();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            helper.cerrar();
        }
    }

    private void limpiarCampos() {
        edtIdEstablecimientoEliminar.setText("");
        txtDatosEstablecimientoEliminar.setText("Busque un establecimiento para mostrar sus datos.");
        btnEliminarEstablecimiento.setEnabled(false);
        idEncontrado = -1;
    }
}
