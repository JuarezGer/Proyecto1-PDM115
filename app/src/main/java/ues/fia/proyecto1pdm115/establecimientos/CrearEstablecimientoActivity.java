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

public class CrearEstablecimientoActivity extends AppCompatActivity {

    EditText edtNombreEstablecimientoCrear, edtTelefonoEstablecimientoCrear,
            edtDireccionEstablecimientoCrear;

    Button btnGuardarEstablecimiento, btnRegresarCrearEstablecimiento;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_establecimiento);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtNombreEstablecimientoCrear = findViewById(R.id.edtNombreEstablecimientoCrear);
        edtTelefonoEstablecimientoCrear = findViewById(R.id.edtTelefonoEstablecimientoCrear);
        edtDireccionEstablecimientoCrear = findViewById(R.id.edtDireccionEstablecimientoCrear);

        btnGuardarEstablecimiento = findViewById(R.id.btnGuardarEstablecimiento);
        btnRegresarCrearEstablecimiento = findViewById(R.id.btnRegresarCrearEstablecimiento);

        btnGuardarEstablecimiento.setOnClickListener(v -> guardarEstablecimiento());
        btnRegresarCrearEstablecimiento.setOnClickListener(v -> finish());
    }

    private void guardarEstablecimiento() {
        if (camposObligatoriosVacios()) {
            Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Establecimiento establecimiento = new Establecimiento();
        establecimiento.setNombreEstablecimiento(edtNombreEstablecimientoCrear.getText().toString().trim());
        establecimiento.setTelefonoEstablecimiento(edtTelefonoEstablecimientoCrear.getText().toString().trim());
        establecimiento.setDireccionEstablecimiento(edtDireccionEstablecimientoCrear.getText().toString().trim());

        try {
            helper.abrir();
            String mensaje = helper.insertarEstablecimiento(establecimiento);
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

    private boolean camposObligatoriosVacios() {
        return edtNombreEstablecimientoCrear.getText().toString().trim().isEmpty() ||
                edtTelefonoEstablecimientoCrear.getText().toString().trim().isEmpty() ||
                edtDireccionEstablecimientoCrear.getText().toString().trim().isEmpty();
    }

    private void limpiarCampos() {
        edtNombreEstablecimientoCrear.setText("");
        edtTelefonoEstablecimientoCrear.setText("");
        edtDireccionEstablecimientoCrear.setText("");
    }
}
