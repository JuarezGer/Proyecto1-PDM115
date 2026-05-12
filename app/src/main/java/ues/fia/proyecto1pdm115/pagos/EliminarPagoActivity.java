package ues.fia.proyecto1pdm115.pagos;

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
import ues.fia.proyecto1pdm115.modelos.Pago;

public class EliminarPagoActivity extends AppCompatActivity {

    EditText edtIdPagoEliminar;
    TextView txtDatosPagoEliminar;
    Button btnBuscarPagoEliminar, btnEliminarPago, btnRegresarEliminarPago;

    controlDBHospitalApp helper;
    int idEncontrado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_pago);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtIdPagoEliminar = findViewById(R.id.edtIdPagoEliminar);
        txtDatosPagoEliminar = findViewById(R.id.txtDatosPagoEliminar);
        btnBuscarPagoEliminar = findViewById(R.id.btnBuscarPagoEliminar);
        btnEliminarPago = findViewById(R.id.btnEliminarPago);
        btnRegresarEliminarPago = findViewById(R.id.btnRegresarEliminarPago);

        btnEliminarPago.setEnabled(false);

        btnBuscarPagoEliminar.setOnClickListener(v -> buscarPago());
        btnEliminarPago.setOnClickListener(v -> confirmarEliminacion());
        btnRegresarEliminarPago.setOnClickListener(v -> finish());
    }

    private void buscarPago() {
        String idTexto = edtIdPagoEliminar.getText().toString().trim();

        if (idTexto.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID del pago", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            int id = Integer.parseInt(idTexto);

            helper.abrir();
            Pago pago = helper.consultarPago(id);

            if (pago == null) {
                idEncontrado = -1;
                btnEliminarPago.setEnabled(false);
                txtDatosPagoEliminar.setText("No se encontró el pago.");
                Toast.makeText(this, "Pago no encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            idEncontrado = pago.getIdPago();
            btnEliminarPago.setEnabled(true);

            String datos = "ID pago: " + pago.getIdPago() + "\n" +
                    "ID consulta: " + pago.getIdConsulta() + "\n" +
                    "Tipo de pago: " + pago.getTipoPago() + "\n" +
                    "Monto total: $" + pago.getMontoTotal() + "\n" +
                    "Fecha de pago: " + pago.getFechaPago();

            txtDatosPagoEliminar.setText(datos);

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
            Toast.makeText(this, "Primero busque un pago", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Desea eliminar este pago?")
                .setPositiveButton("Eliminar", (dialog, which) -> eliminarPago())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarPago() {
        try {
            helper.abrir();
            String mensaje = helper.eliminarPago(idEncontrado);
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
        edtIdPagoEliminar.setText("");
        txtDatosPagoEliminar.setText("Busque un pago para mostrar sus datos.");
        btnEliminarPago.setEnabled(false);
        idEncontrado = -1;
    }
}
