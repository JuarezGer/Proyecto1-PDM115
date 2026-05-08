package ues.fia.proyecto1pdm115.aseguradoras;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Aseguradora;

public class ActualizarAseguradoraActivity extends AppCompatActivity {

    EditText edtIdAseguradoraActualizar, edtNombreAseguradoraActualizar,
            edtTelefonoAseguradoraActualizar;

    Button btnBuscarAseguradoraActualizar, btnActualizarAseguradora,
            btnRegresarActualizarAseguradora;

    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_aseguradora);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtIdAseguradoraActualizar = findViewById(R.id.edtIdAseguradoraActualizar);
        edtNombreAseguradoraActualizar = findViewById(R.id.edtNombreAseguradoraActualizar);
        edtTelefonoAseguradoraActualizar = findViewById(R.id.edtTelefonoAseguradoraActualizar);

        btnBuscarAseguradoraActualizar = findViewById(R.id.btnBuscarAseguradoraActualizar);
        btnActualizarAseguradora = findViewById(R.id.btnActualizarAseguradora);
        btnRegresarActualizarAseguradora = findViewById(R.id.btnRegresarActualizarAseguradora);

        btnActualizarAseguradora.setEnabled(false);

        btnBuscarAseguradoraActualizar.setOnClickListener(v -> buscarAseguradora());
        btnActualizarAseguradora.setOnClickListener(v -> actualizarAseguradora());
        btnRegresarActualizarAseguradora.setOnClickListener(v -> finish());
    }

    private void buscarAseguradora() {
        String idTexto = edtIdAseguradoraActualizar.getText().toString().trim();

        if (idTexto.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID de la aseguradora", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            helper.abrir();
            Aseguradora aseguradora = helper.consultarAseguradora(Integer.parseInt(idTexto));

            if (aseguradora == null) {
                limpiarCamposSinId();
                btnActualizarAseguradora.setEnabled(false);
                Toast.makeText(this, "Aseguradora no encontrada", Toast.LENGTH_SHORT).show();
                return;
            }

            edtNombreAseguradoraActualizar.setText(aseguradora.getNombreAseguradora());
            edtTelefonoAseguradoraActualizar.setText(aseguradora.getTelefonoAseguradora());
            btnActualizarAseguradora.setEnabled(true);

        } catch (NumberFormatException e) {
            Toast.makeText(this, "El ID debe ser numérico", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            helper.cerrar();
        }
    }

    private void actualizarAseguradora() {
        if (camposObligatoriosVacios()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Aseguradora aseguradora = new Aseguradora();
            aseguradora.setIdAseguradora(Integer.parseInt(edtIdAseguradoraActualizar.getText().toString().trim()));
            aseguradora.setNombreAseguradora(edtNombreAseguradoraActualizar.getText().toString().trim());
            aseguradora.setTelefonoAseguradora(edtTelefonoAseguradoraActualizar.getText().toString().trim());

            helper.abrir();
            String mensaje = helper.actualizarAseguradora(aseguradora);
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

            if (mensaje.toLowerCase().contains("correctamente")) {
                limpiarTodo();
                btnActualizarAseguradora.setEnabled(false);
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
        return edtIdAseguradoraActualizar.getText().toString().trim().isEmpty() ||
                edtNombreAseguradoraActualizar.getText().toString().trim().isEmpty() ||
                edtTelefonoAseguradoraActualizar.getText().toString().trim().isEmpty();
    }

    private void limpiarCamposSinId() {
        edtNombreAseguradoraActualizar.setText("");
        edtTelefonoAseguradoraActualizar.setText("");
    }

    private void limpiarTodo() {
        edtIdAseguradoraActualizar.setText("");
        limpiarCamposSinId();
    }
}
