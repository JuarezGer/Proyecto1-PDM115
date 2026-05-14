package ues.fia.proyecto1pdm115.consultas;

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
import ues.fia.proyecto1pdm115.modelos.Consulta;

public class CrearConsultaActivity extends AppCompatActivity {

    EditText edtIdConsulta, edtDuiPacienteCons, edtDuiDoctorCons,
            edtFechaConsulta, edtDiagnostico, edtCargoTotal;

    Spinner spnEmergencia, spnPagaMedicamento;

    Button btnGuardarConsulta, btnRegresarCrear;
    controlDBHospitalApp helper;

    ArrayList<String> codigosEmergencia = new ArrayList<>();
    ArrayList<String> nombresEmergencia = new ArrayList<>();

    private final String COLOR_PLACEHOLDER = "#999999";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_consulta);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtIdConsulta = findViewById(R.id.edtIdConsulta);
        edtDuiPacienteCons = findViewById(R.id.edtDuiPacienteCons);
        edtDuiDoctorCons = findViewById(R.id.edtDuiDoctorCons);
        spnEmergencia = findViewById(R.id.spnEmergencia);
        edtFechaConsulta = findViewById(R.id.edtFechaConsulta);
        edtDiagnostico = findViewById(R.id.edtDiagnostico);
        edtCargoTotal = findViewById(R.id.edtCargoTotal);
        spnPagaMedicamento = findViewById(R.id.spnPagaMedicamento);

        btnGuardarConsulta = findViewById(R.id.btnGuardarConsulta);
        btnRegresarCrear = findViewById(R.id.btnRegresarCrear);

        cargarEmergencias();
        configurarSpinnerMedicamento();

        btnGuardarConsulta.setOnClickListener(v -> guardarConsulta());
        btnRegresarCrear.setOnClickListener(v -> finish());
    }

    private void cargarEmergencias() {
        codigosEmergencia.clear();
        nombresEmergencia.clear();
        codigosEmergencia.add("");
        nombresEmergencia.add("Seleccione tipo de emergencia");

        Cursor cursor = null;
        try {
            helper.abrir();
            cursor = helper.consultarEmergenciasCursor();
            if (cursor.moveToFirst()) {
                do {
                    String codigo = cursor.getString(cursor.getColumnIndexOrThrow("COD_EMERGENCIA"));
                    String prioridad = cursor.getString(cursor.getColumnIndexOrThrow("PRIORIDAD"));
                    codigosEmergencia.add(codigo);
                    nombresEmergencia.add(prioridad + " (" + codigo + ")");
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar emergencias: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) cursor.close();
            helper.cerrar();
        }

        ArrayAdapter<String> adapter = crearAdaptadorPersonalizado(nombresEmergencia);
        spnEmergencia.setAdapter(adapter);
    }

    private void configurarSpinnerMedicamento() {
        ArrayList<String> opciones = new ArrayList<>();
        opciones.add("¿Paga medicamento?");
        opciones.add("Sí");
        opciones.add("No");

        ArrayAdapter<String> adapter = crearAdaptadorPersonalizado(opciones);
        spnPagaMedicamento.setAdapter(adapter);
    }

    private void guardarConsulta() {
        if (camposObligatoriosVacios()) {
            Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Consulta consulta = new Consulta();
        consulta.setIdConsulta(Integer.parseInt(edtIdConsulta.getText().toString()));
        consulta.setDuiPaciente(edtDuiPacienteCons.getText().toString().trim());
        consulta.setDuiDoctor(edtDuiDoctorCons.getText().toString().trim());
        consulta.setCodEmergencia(codigosEmergencia.get(spnEmergencia.getSelectedItemPosition()));
        consulta.setFechaConsulta(edtFechaConsulta.getText().toString().trim());
        consulta.setDiagnostico(edtDiagnostico.getText().toString().trim());


        String cargoStr = edtCargoTotal.getText().toString().trim();
        // Agregamos 'f' al 0.0 y (float) antes de Double.parseDouble
        consulta.setCargoTotalConsulta(cargoStr.isEmpty() ? 0.0f : (float) Double.parseDouble(cargoStr));


        consulta.setPagaMedicamento(spnPagaMedicamento.getSelectedItemPosition() == 1 ? 1 : 0);

        helper.abrir();
        String mensaje = helper.insertarConsulta(consulta); // Debes crear este método en el Helper
        helper.cerrar();

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
        if(!mensaje.contains("Error")) limpiarCampos();
    }

    private boolean camposObligatoriosVacios() {
        return edtIdConsulta.getText().toString().isEmpty() ||
                edtDuiPacienteCons.getText().toString().isEmpty() ||
                edtDuiDoctorCons.getText().toString().isEmpty() ||
                spnEmergencia.getSelectedItemPosition() == 0 ||
                edtDiagnostico.getText().toString().isEmpty() ||
                spnPagaMedicamento.getSelectedItemPosition() == 0;
    }

    private void limpiarCampos() {
        edtIdConsulta.setText("");
        edtDuiPacienteCons.setText("");
        edtDuiDoctorCons.setText("");
        spnEmergencia.setSelection(0);
        edtDiagnostico.setText("");
        edtFechaConsulta.setText("");
        edtCargoTotal.setText("");
        spnPagaMedicamento.setSelection(0);
    }


    private ArrayAdapter<String> crearAdaptadorPersonalizado(ArrayList<String> datos) {
        return new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datos) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                view.setTextColor(position == 0 ? Color.parseColor(COLOR_PLACEHOLDER) :
                        ContextCompat.getColor(getContext(), R.color.text_black));
                return view;
            }
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                view.setTextColor(position == 0 ? Color.parseColor(COLOR_PLACEHOLDER) :
                        ContextCompat.getColor(getContext(), R.color.text_black));
                return view;
            }
        };
    }
}