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

public class CrearAseguradoraActivity extends AppCompatActivity {

    EditText edtNombreAseguradoraCrear, edtTelefonoAseguradoraCrear;

    Button btnGuardarAseguradora, btnRegresarCrearAseguradora;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_aseguradora);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtNombreAseguradoraCrear = findViewById(R.id.edtNombreAseguradoraCrear);
        edtTelefonoAseguradoraCrear = findViewById(R.id.edtTelefonoAseguradoraCrear);

        btnGuardarAseguradora = findViewById(R.id.btnGuardarAseguradora);
        btnRegresarCrearAseguradora = findViewById(R.id.btnRegresarCrearAseguradora);

        btnGuardarAseguradora.setOnClickListener(v -> guardarAseguradora());
        btnRegresarCrearAseguradora.setOnClickListener(v -> finish());
    }

    private void guardarAseguradora() {
        if (camposObligatoriosVacios()) {
            Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Aseguradora aseguradora = new Aseguradora();
        aseguradora.setNombreAseguradora(edtNombreAseguradoraCrear.getText().toString().trim());
        aseguradora.setTelefonoAseguradora(edtTelefonoAseguradoraCrear.getText().toString().trim());

        try {
            helper.abrir();
            String mensaje = helper.insertarAseguradora(aseguradora);
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
        return edtNombreAseguradoraCrear.getText().toString().trim().isEmpty() ||
                edtTelefonoAseguradoraCrear.getText().toString().trim().isEmpty();
    }

    private void limpiarCampos() {
        edtNombreAseguradoraCrear.setText("");
        edtTelefonoAseguradoraCrear.setText("");
    }
}
