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

public class ModificarTipoEmergenciaActivity extends AppCompatActivity {

    EditText edtCodEmergenciaModificar, edtPrioridadModificar, edtCostoEmergenciaModificar;
    Button btnBuscarModificar, btnActualizarEmergencia, btnRegresarModificar;
    controlDBHospitalApp helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_modificar_tipo_emergencia);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);


        edtCodEmergenciaModificar = findViewById(R.id.edtCodEmergenciaModificar);
        edtPrioridadModificar = findViewById(R.id.edtPrioridadModificar);
        edtCostoEmergenciaModificar = findViewById(R.id.edtCostoEmergenciaModificar);

        btnBuscarModificar = findViewById(R.id.btnBuscarModificar);
        btnActualizarEmergencia = findViewById(R.id.btnActualizarEmergencia);
        btnRegresarModificar = findViewById(R.id.btnRegresarModificar);


        btnBuscarModificar.setOnClickListener(v -> buscarTipoEmergencia());
        btnActualizarEmergencia.setOnClickListener(v -> actualizarTipoEmergencia());
        btnRegresarModificar.setOnClickListener(v -> finish());
    }

    private void buscarTipoEmergencia() {
        String codigo = edtCodEmergenciaModificar.getText().toString().trim();

        if (codigo.isEmpty()) {
            Toast.makeText(this, "Ingrese el código para buscar", Toast.LENGTH_SHORT).show();
            return;
        }

        helper.abrir();
        Tipo_emergencia tipo = helper.consultarTipoEmergencia(codigo);
        helper.cerrar();

        if (tipo != null) {
            edtPrioridadModificar.setText(tipo.getPrioridad());
            edtCostoEmergenciaModificar.setText(String.valueOf(tipo.getCosto_emergencia()));
            Toast.makeText(this, "Registro encontrado", Toast.LENGTH_SHORT).show();
        } else {
            limpiarCamposSinCodigo();
            Toast.makeText(this, "Tipo de emergencia no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarTipoEmergencia() {
        String codigo = edtCodEmergenciaModificar.getText().toString().trim();
        String prioridad = edtPrioridadModificar.getText().toString().trim();
        String costoStr = edtCostoEmergenciaModificar.getText().toString().trim();

        if (codigo.isEmpty() || prioridad.isEmpty() || costoStr.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            float costo = Float.parseFloat(costoStr);

            Tipo_emergencia tipo = new Tipo_emergencia();
            tipo.setCod_emergencia(codigo);
            tipo.setPrioridad(prioridad);
            tipo.setCosto_emergencia(costo);

            helper.abrir();
            String mensaje = helper.actualizarTipoEmergencia(tipo);
            helper.cerrar();

            Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show();

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Costo inválido", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiarCamposSinCodigo() {
        edtPrioridadModificar.setText("");
        edtCostoEmergenciaModificar.setText("");
    }

}