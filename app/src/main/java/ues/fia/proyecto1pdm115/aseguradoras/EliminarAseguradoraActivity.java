package ues.fia.proyecto1pdm115.aseguradoras;

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
import ues.fia.proyecto1pdm115.modelos.Aseguradora;

public class EliminarAseguradoraActivity extends AppCompatActivity {

    EditText edtIdAseguradoraEliminar;
    TextView txtDatosAseguradoraEliminar;
    Button btnBuscarAseguradoraEliminar, btnEliminarAseguradora,
            btnRegresarEliminarAseguradora;

    controlDBHospitalApp helper;
    int idEncontrado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_aseguradora);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtIdAseguradoraEliminar = findViewById(R.id.edtIdAseguradoraEliminar);
        txtDatosAseguradoraEliminar = findViewById(R.id.txtDatosAseguradoraEliminar);
        btnBuscarAseguradoraEliminar = findViewById(R.id.btnBuscarAseguradoraEliminar);
        btnEliminarAseguradora = findViewById(R.id.btnEliminarAseguradora);
        btnRegresarEliminarAseguradora = findViewById(R.id.btnRegresarEliminarAseguradora);

        btnEliminarAseguradora.setEnabled(false);

        btnBuscarAseguradoraEliminar.setOnClickListener(v -> buscarAseguradora());
        btnEliminarAseguradora.setOnClickListener(v -> confirmarEliminacion());
        btnRegresarEliminarAseguradora.setOnClickListener(v -> finish());
    }

    private void buscarAseguradora() {
        String idTexto = edtIdAseguradoraEliminar.getText().toString().trim();

        if (idTexto.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID de la aseguradora", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int id = Integer.parseInt(idTexto);

            helper.abrir();
            Aseguradora aseguradora = helper.consultarAseguradora(id);

            if (aseguradora == null) {
                idEncontrado = -1;
                btnEliminarAseguradora.setEnabled(false);
                txtDatosAseguradoraEliminar.setText("No se encontró la aseguradora.");
                Toast.makeText(this, "Aseguradora no encontrada", Toast.LENGTH_SHORT).show();
                return;
            }

            idEncontrado = aseguradora.getIdAseguradora();
            btnEliminarAseguradora.setEnabled(true);

            String datos = "ID: " + aseguradora.getIdAseguradora() + "\n" +
                    "Nombre: " + aseguradora.getNombreAseguradora() + "\n" +
                    "Teléfono: " + aseguradora.getTelefonoAseguradora();

            txtDatosAseguradoraEliminar.setText(datos);

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
            Toast.makeText(this, "Primero busque una aseguradora", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Desea eliminar esta aseguradora?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarAseguradora())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarAseguradora() {
        try {
            helper.abrir();
            String mensaje = helper.eliminarAseguradora(idEncontrado);
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
        edtIdAseguradoraEliminar.setText("");
        txtDatosAseguradoraEliminar.setText("Busque una aseguradora para mostrar sus datos.");
        btnEliminarAseguradora.setEnabled(false);
        idEncontrado = -1;
    }
}
