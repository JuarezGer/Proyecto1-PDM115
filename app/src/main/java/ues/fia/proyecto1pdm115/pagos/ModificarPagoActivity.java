package ues.fia.proyecto1pdm115.pagos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Pago;

public class ModificarPagoActivity extends AppCompatActivity {

    EditText edtIdPagoActualizar, edtIdConsultaActualizar, edtTipoPagoActualizar,
            edtMontoTotalActualizar, edtFechaPagoActualizar;

    Button btnBuscarPagoActualizar, btnActualizarPago, btnRegresarActualizarPago;

    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_pago);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtIdPagoActualizar = findViewById(R.id.edtIdPagoActualizar);
        edtIdConsultaActualizar = findViewById(R.id.edtIdConsultaActualizar);
        edtTipoPagoActualizar = findViewById(R.id.edtTipoPagoActualizar);
        edtMontoTotalActualizar = findViewById(R.id.edtMontoTotalActualizar);
        edtFechaPagoActualizar = findViewById(R.id.edtFechaPagoActualizar);

        btnBuscarPagoActualizar = findViewById(R.id.btnBuscarPagoActualizar);
        btnActualizarPago = findViewById(R.id.btnActualizarPago);
        btnRegresarActualizarPago = findViewById(R.id.btnRegresarActualizarPago);

        btnActualizarPago.setEnabled(false);

        btnBuscarPagoActualizar.setOnClickListener(v -> buscarPago());
        btnActualizarPago.setOnClickListener(v -> actualizarPago());
        btnRegresarActualizarPago.setOnClickListener(v -> finish());
    }

    private void buscarPago() {
        String idTexto = edtIdPagoActualizar.getText().toString().trim();

        if (idTexto.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID del pago", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            helper.abrir();
            Pago pago = helper.consultarPago(Integer.parseInt(idTexto));

            if (pago == null) {
                limpiarCamposSinId();
                btnActualizarPago.setEnabled(false);
                Toast.makeText(this, "Pago no encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            edtIdConsultaActualizar.setText(String.valueOf(pago.getIdConsulta()));
            edtTipoPagoActualizar.setText(pago.getTipoPago());
            edtMontoTotalActualizar.setText(String.valueOf(pago.getMontoTotal()));
            edtFechaPagoActualizar.setText(pago.getFechaPago());
            btnActualizarPago.setEnabled(true);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "El ID debe ser numérico", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            helper.cerrar();
        }
    }

    private void actualizarPago() {
        if (camposObligatoriosVacios()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Pago pago = new Pago();
            pago.setIdPago(Integer.parseInt(edtIdPagoActualizar.getText().toString().trim()));
            pago.setIdConsulta(Integer.parseInt(edtIdConsultaActualizar.getText().toString().trim()));
            pago.setTipoPago(edtTipoPagoActualizar.getText().toString().trim());
            pago.setMontoTotal(Double.parseDouble(edtMontoTotalActualizar.getText().toString().trim()));
            pago.setFechaPago(edtFechaPagoActualizar.getText().toString().trim());

            helper.abrir();
            String mensaje = helper.actualizarPago(pago);
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

            if (mensaje.toLowerCase().contains("correctamente")) {
                limpiarTodo();
                btnActualizarPago.setEnabled(false);
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID y monto deben ser numéricos", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            helper.cerrar();
        }
    }

    private boolean camposObligatoriosVacios() {
        return edtIdPagoActualizar.getText().toString().trim().isEmpty() ||
                edtIdConsultaActualizar.getText().toString().trim().isEmpty() ||
                edtTipoPagoActualizar.getText().toString().trim().isEmpty() ||
                edtMontoTotalActualizar.getText().toString().trim().isEmpty() ||
                edtFechaPagoActualizar.getText().toString().trim().isEmpty();
    }

    private void limpiarCamposSinId() {
        edtIdConsultaActualizar.setText("");
        edtTipoPagoActualizar.setText("");
        edtMontoTotalActualizar.setText("");
        edtFechaPagoActualizar.setText("");
    }

    private void limpiarTodo() {
        edtIdPagoActualizar.setText("");
        limpiarCamposSinId();
    }
}
