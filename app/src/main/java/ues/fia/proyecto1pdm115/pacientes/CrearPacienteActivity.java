package ues.fia.proyecto1pdm115.pacientes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Paciente;

public class CrearPacienteActivity extends AppCompatActivity {

    EditText edtDuiCrear, edtCodDistritoCrear, edtPrimerNombreCrear, edtSegundoNombreCrear,
            edtPrimerApellidoCrear, edtSegundoApellidoCrear, edtFechaNacimientoCrear,
            edtGeneroCrear, edtTelefonoCrear;

    Button btnGuardarPaciente, btnRegresarCrear;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_paciente);

        helper = new controlDBHospitalApp(this);

        edtDuiCrear = findViewById(R.id.edtDuiCrear);
        edtCodDistritoCrear = findViewById(R.id.edtCodDistritoCrear);
        edtPrimerNombreCrear = findViewById(R.id.edtPrimerNombreCrear);
        edtSegundoNombreCrear = findViewById(R.id.edtSegundoNombreCrear);
        edtPrimerApellidoCrear = findViewById(R.id.edtPrimerApellidoCrear);
        edtSegundoApellidoCrear = findViewById(R.id.edtSegundoApellidoCrear);
        edtFechaNacimientoCrear = findViewById(R.id.edtFechaNacimientoCrear);
        edtGeneroCrear = findViewById(R.id.edtGeneroCrear);
        edtTelefonoCrear = findViewById(R.id.edtTelefonoCrear);

        btnGuardarPaciente = findViewById(R.id.btnGuardarPaciente);
        btnRegresarCrear = findViewById(R.id.btnRegresarCrear);

        btnGuardarPaciente.setOnClickListener(v -> guardarPaciente());
        btnRegresarCrear.setOnClickListener(v -> finish());
    }

    private void guardarPaciente() {
        if (camposObligatoriosVacios()) {
            Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Paciente paciente = new Paciente();

        paciente.setDuiPaciente(edtDuiCrear.getText().toString().trim());
        paciente.setIdPoliza(null);
        paciente.setCodDistrito(edtCodDistritoCrear.getText().toString().trim());
        paciente.setPrimerNombrePaciente(edtPrimerNombreCrear.getText().toString().trim());
        paciente.setSegundoNombrePaciente(edtSegundoNombreCrear.getText().toString().trim());
        paciente.setPrimerApellidoPaciente(edtPrimerApellidoCrear.getText().toString().trim());
        paciente.setSegundoApellidoPaciente(edtSegundoApellidoCrear.getText().toString().trim());
        paciente.setFechaNacimientoPaciente(edtFechaNacimientoCrear.getText().toString().trim());
        paciente.setGeneroPaciente(edtGeneroCrear.getText().toString().trim());
        paciente.setTelefonoPaciente(edtTelefonoCrear.getText().toString().trim());

        helper.abrir();
        helper.llenarDatosIniciales();
        String mensaje = helper.insertarPaciente(paciente);
        helper.cerrar();

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
        limpiarCampos();
    }

    private boolean camposObligatoriosVacios() {
        return edtDuiCrear.getText().toString().trim().isEmpty() ||
                edtCodDistritoCrear.getText().toString().trim().isEmpty() ||
                edtPrimerNombreCrear.getText().toString().trim().isEmpty() ||
                edtPrimerApellidoCrear.getText().toString().trim().isEmpty() ||
                edtGeneroCrear.getText().toString().trim().isEmpty() ||
                edtTelefonoCrear.getText().toString().trim().isEmpty();
    }

    private void limpiarCampos() {
        edtDuiCrear.setText("");
        edtCodDistritoCrear.setText("SS0101");
        edtPrimerNombreCrear.setText("");
        edtSegundoNombreCrear.setText("");
        edtPrimerApellidoCrear.setText("");
        edtSegundoApellidoCrear.setText("");
        edtFechaNacimientoCrear.setText("");
        edtGeneroCrear.setText("");
        edtTelefonoCrear.setText("");
    }
}