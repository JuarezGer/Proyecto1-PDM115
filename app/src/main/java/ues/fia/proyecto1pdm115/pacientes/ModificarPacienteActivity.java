package ues.fia.proyecto1pdm115.pacientes;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Paciente;

public class ModificarPacienteActivity extends AppCompatActivity {

    EditText edtDuiModificar, edtCodDistritoModificar, edtPrimerNombreModificar,
            edtSegundoNombreModificar, edtPrimerApellidoModificar, edtSegundoApellidoModificar,
            edtFechaNacimientoModificar, edtGeneroModificar, edtTelefonoModificar;

    Button btnBuscarModificar, btnActualizarPaciente, btnRegresarModificar;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_paciente);

        helper = new controlDBHospitalApp(this);

        edtDuiModificar = findViewById(R.id.edtDuiModificar);
        edtCodDistritoModificar = findViewById(R.id.edtCodDistritoModificar);
        edtPrimerNombreModificar = findViewById(R.id.edtPrimerNombreModificar);
        edtSegundoNombreModificar = findViewById(R.id.edtSegundoNombreModificar);
        edtPrimerApellidoModificar = findViewById(R.id.edtPrimerApellidoModificar);
        edtSegundoApellidoModificar = findViewById(R.id.edtSegundoApellidoModificar);
        edtFechaNacimientoModificar = findViewById(R.id.edtFechaNacimientoModificar);
        edtGeneroModificar = findViewById(R.id.edtGeneroModificar);
        edtTelefonoModificar = findViewById(R.id.edtTelefonoModificar);

        btnBuscarModificar = findViewById(R.id.btnBuscarModificar);
        btnActualizarPaciente = findViewById(R.id.btnActualizarPaciente);
        btnRegresarModificar = findViewById(R.id.btnRegresarModificar);

        btnBuscarModificar.setOnClickListener(v -> buscarPaciente());
        btnActualizarPaciente.setOnClickListener(v -> actualizarPaciente());
        btnRegresarModificar.setOnClickListener(v -> finish());
    }

    private void buscarPaciente() {
        String dui = edtDuiModificar.getText().toString().trim();

        if (dui.isEmpty()) {
            Toast.makeText(this, "Ingrese el DUI del paciente", Toast.LENGTH_SHORT).show();
            return;
        }

        helper.abrir();
        Paciente paciente = helper.consultarPaciente(dui);
        helper.cerrar();

        if (paciente != null) {
            edtCodDistritoModificar.setText(valor(paciente.getCodDistrito()));
            edtPrimerNombreModificar.setText(valor(paciente.getPrimerNombrePaciente()));
            edtSegundoNombreModificar.setText(valor(paciente.getSegundoNombrePaciente()));
            edtPrimerApellidoModificar.setText(valor(paciente.getPrimerApellidoPaciente()));
            edtSegundoApellidoModificar.setText(valor(paciente.getSegundoApellidoPaciente()));
            edtFechaNacimientoModificar.setText(valor(paciente.getFechaNacimientoPaciente()));
            edtGeneroModificar.setText(valor(paciente.getGeneroPaciente()));
            edtTelefonoModificar.setText(valor(paciente.getTelefonoPaciente()));

            Toast.makeText(this, "Paciente encontrado", Toast.LENGTH_SHORT).show();
        } else {
            limpiarCamposSinDui();
            Toast.makeText(this, "Paciente no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarPaciente() {
        String dui = edtDuiModificar.getText().toString().trim();

        if (dui.isEmpty()) {
            Toast.makeText(this, "Ingrese el DUI del paciente", Toast.LENGTH_SHORT).show();
            return;
        }

        if (camposObligatoriosVacios()) {
            Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Paciente paciente = new Paciente();

        paciente.setDuiPaciente(dui);
        paciente.setIdPoliza(null);
        paciente.setCodDistrito(edtCodDistritoModificar.getText().toString().trim());
        paciente.setPrimerNombrePaciente(edtPrimerNombreModificar.getText().toString().trim());
        paciente.setSegundoNombrePaciente(edtSegundoNombreModificar.getText().toString().trim());
        paciente.setPrimerApellidoPaciente(edtPrimerApellidoModificar.getText().toString().trim());
        paciente.setSegundoApellidoPaciente(edtSegundoApellidoModificar.getText().toString().trim());
        paciente.setFechaNacimientoPaciente(edtFechaNacimientoModificar.getText().toString().trim());
        paciente.setGeneroPaciente(edtGeneroModificar.getText().toString().trim());
        paciente.setTelefonoPaciente(edtTelefonoModificar.getText().toString().trim());

        helper.abrir();
        helper.llenarDatosIniciales();
        String mensaje = helper.actualizarPaciente(paciente);
        helper.cerrar();

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    private boolean camposObligatoriosVacios() {
        return edtCodDistritoModificar.getText().toString().trim().isEmpty() ||
                edtPrimerNombreModificar.getText().toString().trim().isEmpty() ||
                edtPrimerApellidoModificar.getText().toString().trim().isEmpty() ||
                edtGeneroModificar.getText().toString().trim().isEmpty() ||
                edtTelefonoModificar.getText().toString().trim().isEmpty();
    }

    private void limpiarCamposSinDui() {
        edtCodDistritoModificar.setText("");
        edtPrimerNombreModificar.setText("");
        edtSegundoNombreModificar.setText("");
        edtPrimerApellidoModificar.setText("");
        edtSegundoApellidoModificar.setText("");
        edtFechaNacimientoModificar.setText("");
        edtGeneroModificar.setText("");
        edtTelefonoModificar.setText("");
    }

    private String valor(String texto) {
        if (texto == null) {
            return "";
        }
        return texto;
    }
}