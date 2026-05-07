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

public class CrearPacienteActivity extends AppCompatActivity {

    EditText edtDuiCrear, edtPrimerNombreCrear, edtSegundoNombreCrear,
            edtPrimerApellidoCrear, edtSegundoApellidoCrear, edtFechaNacimientoCrear,
            edtGeneroCrear, edtTelefonoCrear;

    Spinner spnDistritoCrear;

    Button btnGuardarPaciente, btnRegresarCrear;
    controlDBHospitalApp helper;

    ArrayList<String> codigosDistritos = new ArrayList<>();
    ArrayList<String> nombresDistritos = new ArrayList<>();

    private final String COLOR_PLACEHOLDER = "#999999";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_paciente);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtDuiCrear = findViewById(R.id.edtDuiCrear);
        spnDistritoCrear = findViewById(R.id.edtCodDistritoCrear);
        edtPrimerNombreCrear = findViewById(R.id.edtPrimerNombreCrear);
        edtSegundoNombreCrear = findViewById(R.id.edtSegundoNombreCrear);
        edtPrimerApellidoCrear = findViewById(R.id.edtPrimerApellidoCrear);
        edtSegundoApellidoCrear = findViewById(R.id.edtSegundoApellidoCrear);
        edtFechaNacimientoCrear = findViewById(R.id.edtFechaNacimientoCrear);
        edtGeneroCrear = findViewById(R.id.edtGeneroCrear);
        edtTelefonoCrear = findViewById(R.id.edtTelefonoCrear);

        btnGuardarPaciente = findViewById(R.id.btnGuardarPaciente);
        btnRegresarCrear = findViewById(R.id.btnRegresarCrear);

        cargarDistritos();

        btnGuardarPaciente.setOnClickListener(v -> guardarPaciente());
        btnRegresarCrear.setOnClickListener(v -> finish());
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
                    view.setTextColor(ContextCompat.getColor(CrearPacienteActivity.this, R.color.text_black));
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
                    view.setTextColor(ContextCompat.getColor(CrearPacienteActivity.this, R.color.text_black));
                }

                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDistritoCrear.setAdapter(adapter);
    }

    private String obtenerCodigoDistritoSeleccionado() {
        int posicion = spnDistritoCrear.getSelectedItemPosition();

        if (posicion <= 0 || posicion >= codigosDistritos.size()) {
            return "";
        }

        return codigosDistritos.get(posicion);
    }

    private void guardarPaciente() {
        if (camposObligatoriosVacios()) {
            Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Paciente paciente = new Paciente();

        paciente.setDuiPaciente(edtDuiCrear.getText().toString().trim());
        paciente.setIdPoliza(null);
        paciente.setCodDistrito(obtenerCodigoDistritoSeleccionado());
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
                obtenerCodigoDistritoSeleccionado().isEmpty() ||
                edtPrimerNombreCrear.getText().toString().trim().isEmpty() ||
                edtPrimerApellidoCrear.getText().toString().trim().isEmpty() ||
                edtGeneroCrear.getText().toString().trim().isEmpty() ||
                edtTelefonoCrear.getText().toString().trim().isEmpty();
    }

    private void limpiarCampos() {
        edtDuiCrear.setText("");
        spnDistritoCrear.setSelection(0);
        edtPrimerNombreCrear.setText("");
        edtSegundoNombreCrear.setText("");
        edtPrimerApellidoCrear.setText("");
        edtSegundoApellidoCrear.setText("");
        edtFechaNacimientoCrear.setText("");
        edtGeneroCrear.setText("");
        edtTelefonoCrear.setText("");
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}