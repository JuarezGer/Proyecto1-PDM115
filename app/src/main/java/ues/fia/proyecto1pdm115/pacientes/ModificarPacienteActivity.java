package ues.fia.proyecto1pdm115.pacientes;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Paciente;

public class ModificarPacienteActivity extends AppCompatActivity {

    EditText edtDuiModificar, edtPrimerNombreModificar,
            edtSegundoNombreModificar, edtPrimerApellidoModificar, edtSegundoApellidoModificar,
            edtFechaNacimientoModificar, edtGeneroModificar, edtTelefonoModificar;

    Spinner spnDistritoModificar;

    Button btnBuscarModificar, btnActualizarPaciente, btnRegresarModificar;
    controlDBHospitalApp helper;

    ArrayList<String> codigosDistritos = new ArrayList<>();
    ArrayList<String> nombresDistritos = new ArrayList<>();

    private final String COLOR_PLACEHOLDER = "#999999";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_paciente);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtDuiModificar = findViewById(R.id.edtDuiModificar);
        spnDistritoModificar = findViewById(R.id.edtCodDistritoModificar);
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

        cargarDistritos();

        btnBuscarModificar.setOnClickListener(v -> buscarPaciente());
        btnActualizarPaciente.setOnClickListener(v -> actualizarPaciente());
        btnRegresarModificar.setOnClickListener(v -> finish());
    }

    private void cargarDistritos() {
        codigosDistritos.clear();
        nombresDistritos.clear();

        codigosDistritos.add("");
        nombresDistritos.add("Seleccione un distrito");

        Cursor cursor = null;

        try {
            helper.abrir();
            helper.llenarDatosIniciales();

            cursor = helper.consultarDistritosCursor();

            if (cursor.moveToFirst()) {
                do {
                    String codigo = cursor.getString(cursor.getColumnIndexOrThrow("COD_DISTRITO"));
                    String nombre = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_DISTRITO"));

                    codigosDistritos.add(codigo);
                    nombresDistritos.add(nombre + " (" + codigo + ")");

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar distritos: " + e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            if (cursor != null) {
                cursor.close();
            }

            helper.cerrar();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                nombresDistritos
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);

                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                view.setGravity(Gravity.CENTER_VERTICAL);
                view.setSingleLine(true);
                view.setPadding(dpToPx(12), 0, dpToPx(12), 0);

                if (position == 0) {
                    view.setTextColor(Color.parseColor(COLOR_PLACEHOLDER));
                } else {
                    view.setTextColor(ContextCompat.getColor(ModificarPacienteActivity.this, R.color.text_black));
                }

                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);

                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                view.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));

                if (position == 0) {
                    view.setTextColor(Color.parseColor(COLOR_PLACEHOLDER));
                } else {
                    view.setTextColor(ContextCompat.getColor(ModificarPacienteActivity.this, R.color.text_black));
                }

                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDistritoModificar.setAdapter(adapter);
    }

    private String obtenerCodigoDistritoSeleccionado() {
        int posicion = spnDistritoModificar.getSelectedItemPosition();

        if (posicion <= 0 || posicion >= codigosDistritos.size()) {
            return "";
        }

        return codigosDistritos.get(posicion);
    }

    private void seleccionarDistritoPorCodigo(String codigoDistrito) {
        if (codigoDistrito == null) {
            spnDistritoModificar.setSelection(0);
            return;
        }

        for (int i = 0; i < codigosDistritos.size(); i++) {
            if (codigosDistritos.get(i).equals(codigoDistrito)) {
                spnDistritoModificar.setSelection(i);
                return;
            }
        }

        spnDistritoModificar.setSelection(0);
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
            seleccionarDistritoPorCodigo(paciente.getCodDistrito());

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
        paciente.setCodDistrito(obtenerCodigoDistritoSeleccionado());
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
        return obtenerCodigoDistritoSeleccionado().isEmpty() ||
                edtPrimerNombreModificar.getText().toString().trim().isEmpty() ||
                edtPrimerApellidoModificar.getText().toString().trim().isEmpty() ||
                edtGeneroModificar.getText().toString().trim().isEmpty() ||
                edtTelefonoModificar.getText().toString().trim().isEmpty();
    }

    private void limpiarCamposSinDui() {
        spnDistritoModificar.setSelection(0);
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

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}