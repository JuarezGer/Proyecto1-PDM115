package ues.fia.proyecto1pdm115.establecimientos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Establecimiento;

public class ActualizarEstablecimientoActivity extends AppCompatActivity {

    EditText edtIdEstablecimientoActualizar, edtNombreEstablecimientoActualizar,
            edtTelefonoEstablecimientoActualizar, edtDireccionEstablecimientoActualizar;

    Button btnBuscarEstablecimientoActualizar, btnActualizarEstablecimiento,
            btnRegresarActualizarEstablecimiento;

    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_establecimiento);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtIdEstablecimientoActualizar = findViewById(R.id.edtIdEstablecimientoModificar);
        edtNombreEstablecimientoActualizar = findViewById(R.id.edtNombreEstablecimientoModificar);
        edtTelefonoEstablecimientoActualizar = findViewById(R.id.edtTelefonoEstablecimientoModificar);
        edtDireccionEstablecimientoActualizar = findViewById(R.id.edtDireccionEstablecimientoModificar);

        btnBuscarEstablecimientoActualizar = findViewById(R.id.btnBuscarEstablecimientoModificar);
        btnActualizarEstablecimiento = findViewById(R.id.btnModificarEstablecimiento);
        btnRegresarActualizarEstablecimiento = findViewById(R.id.btnRegresarModificarEstablecimiento);

        btnActualizarEstablecimiento.setEnabled(false);

        btnBuscarEstablecimientoActualizar.setOnClickListener(v -> buscarEstablecimiento());
        btnActualizarEstablecimiento.setOnClickListener(v -> actualizarEstablecimiento());
        btnRegresarActualizarEstablecimiento.setOnClickListener(v -> finish());
    }

    private void buscarEstablecimiento() {
        String idTexto = edtIdEstablecimientoActualizar.getText().toString().trim();

        if (idTexto.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID del establecimiento", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            helper.abrir();
            Establecimiento establecimiento = helper.consultarEstablecimiento(Integer.parseInt(idTexto));

            if (establecimiento == null) {
                limpiarCamposSinId();
                btnActualizarEstablecimiento.setEnabled(false);
                Toast.makeText(this, "Establecimiento no encontrado", Toast.LENGTH_SHORT).show();
                return;
            }

            edtNombreEstablecimientoActualizar.setText(establecimiento.getNombreEstablecimiento());
            edtTelefonoEstablecimientoActualizar.setText(establecimiento.getTelefonoEstablecimiento());
            edtDireccionEstablecimientoActualizar.setText(establecimiento.getDireccionEstablecimiento());
            btnActualizarEstablecimiento.setEnabled(true);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "El ID debe ser numérico", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            helper.cerrar();
        }
    }

    private void actualizarEstablecimiento() {
        if (camposObligatoriosVacios()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Establecimiento establecimiento = new Establecimiento();
            establecimiento.setIdEstablecimiento(Integer.parseInt(edtIdEstablecimientoActualizar.getText().toString().trim()));
            establecimiento.setNombreEstablecimiento(edtNombreEstablecimientoActualizar.getText().toString().trim());
            establecimiento.setTelefonoEstablecimiento(edtTelefonoEstablecimientoActualizar.getText().toString().trim());
            establecimiento.setDireccionEstablecimiento(edtDireccionEstablecimientoActualizar.getText().toString().trim());

            helper.abrir();
            String mensaje = helper.actualizarEstablecimiento(establecimiento);
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

            if (mensaje.toLowerCase().contains("correctamente")) {
                limpiarTodo();
                btnActualizarEstablecimiento.setEnabled(false);
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "El ID debe ser numérico", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            helper.cerrar();
        }
    }

    private boolean camposObligatoriosVacios() {
        return edtIdEstablecimientoActualizar.getText().toString().trim().isEmpty() ||
                edtNombreEstablecimientoActualizar.getText().toString().trim().isEmpty() ||
                edtTelefonoEstablecimientoActualizar.getText().toString().trim().isEmpty() ||
                edtDireccionEstablecimientoActualizar.getText().toString().trim().isEmpty();
    }

    private void limpiarCamposSinId() {
        edtNombreEstablecimientoActualizar.setText("");
        edtTelefonoEstablecimientoActualizar.setText("");
        edtDireccionEstablecimientoActualizar.setText("");
    }

    private void limpiarTodo() {
        edtIdEstablecimientoActualizar.setText("");
        limpiarCamposSinId();
    }
}
