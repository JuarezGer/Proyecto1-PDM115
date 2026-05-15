package ues.fia.proyecto1pdm115.tipo_emergencia;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Tipo_emergencia;
import ues.fia.proyecto1pdm115.R;

public class CrearTipoEmergenciaActivity extends AppCompatActivity {

    EditText edtCodEmergenciaCrear, edtPrioridadCrear, edtCostoEmergenciaCrear;
    Button btnGuardarEmergencia, btnRegresarCrear;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_tipo_emergencia);

        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);


        edtCodEmergenciaCrear = findViewById(R.id.edtCodEmergenciaCrear);
        edtPrioridadCrear = findViewById(R.id.edtPrioridadCrear);
        edtCostoEmergenciaCrear = findViewById(R.id.edtCostoEmergenciaCrear);

        btnGuardarEmergencia = findViewById(R.id.btnGuardarEmergencia);
        btnRegresarCrear = findViewById(R.id.btnRegresarCrear);

        btnGuardarEmergencia.setOnClickListener(v -> guardarTipoEmergencia());
        btnRegresarCrear.setOnClickListener(v -> finish());
    }

    private void guardarTipoEmergencia() {

        if (camposVacios()) {
            Toast.makeText(this, "Complete todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Tipo_emergencia tipo = new Tipo_emergencia();

        tipo.setCod_emergencia(edtCodEmergenciaCrear.getText().toString().trim());
        tipo.setPrioridad(edtPrioridadCrear.getText().toString().trim());

        try {
            float costo = Float.parseFloat(edtCostoEmergenciaCrear.getText().toString().trim());
            tipo.setCosto_emergencia(costo);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese un costo válido (ej: 15.50)", Toast.LENGTH_SHORT).show();
            return;
        }


        helper.abrir();
        String mensaje = helper.insertarTipoEmergencia(tipo);
        helper.cerrar();

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

        // Si se insertó correctamente, limpiamos
        if (mensaje.contains("éxito") || mensaje.contains("correctamente")) {
            limpiarCampos();
        }
    }

    private boolean camposVacios() {
        return edtCodEmergenciaCrear.getText().toString().trim().isEmpty() ||
                edtPrioridadCrear.getText().toString().trim().isEmpty() ||
                edtCostoEmergenciaCrear.getText().toString().trim().isEmpty();
    }

    private void limpiarCampos() {
        edtCodEmergenciaCrear.setText("");
        edtPrioridadCrear.setText("");
        edtCostoEmergenciaCrear.setText("");
    }

}