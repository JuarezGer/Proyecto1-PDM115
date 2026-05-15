package ues.fia.proyecto1pdm115.consultas;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
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

public class ModificarConsultaActivity extends AppCompatActivity {

    EditText edtIdConsultaMod, edtDuiPacienteMod,
            edtFechaConsultaMod, edtDiagnosticoMod, edtCargoTotalMod;

    Spinner spnDoctorMod, spnEmergenciaMod, spnPagaMedMod;

    Button btnBuscarModificar, btnActualizarConsulta, btnRegresarModificar;
    controlDBHospitalApp helper;

    ArrayList<String> duisDoctor = new ArrayList<>();
    ArrayList<String> nombresDoctor = new ArrayList<>();

    ArrayList<String> codigosEmergencia = new ArrayList<>();
    ArrayList<String> nombresEmergencia = new ArrayList<>();
    String[] opcionesPago = {"¿Paga Medicamento?", "No", "Sí"};

    private final String COLOR_PLACEHOLDER = "#999999";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_consulta);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtIdConsultaMod = findViewById(R.id.edtIdConsultaMod);
        edtDuiPacienteMod = findViewById(R.id.edtDuiPacienteMod);
        spnDoctorMod = findViewById(R.id.spnDoctorMod);
        edtFechaConsultaMod = findViewById(R.id.edtFechaConsultaMod);
        edtDiagnosticoMod = findViewById(R.id.edtDiagnosticoMod);
        edtCargoTotalMod = findViewById(R.id.edtCargoTotalMod);

        spnEmergenciaMod = findViewById(R.id.spnEmergenciaMod);
        spnPagaMedMod = findViewById(R.id.spnPagaMedMod);

        btnBuscarModificar = findViewById(R.id.btnBuscarModificar);
        btnActualizarConsulta = findViewById(R.id.btnActualizarConsulta);
        btnRegresarModificar = findViewById(R.id.btnRegresarModificar);

        cargarDoctores();
        cargarEmergencias();
        configurarEstiloSpinner(spnPagaMedMod, opcionesPago);

        btnBuscarModificar.setOnClickListener(v -> buscarConsulta());
        btnActualizarConsulta.setOnClickListener(v -> actualizarConsulta());
        btnRegresarModificar.setOnClickListener(v -> finish());
    }

    private void cargarDoctores() {
        duisDoctor.clear();
        nombresDoctor.clear();
        duisDoctor.add("");
        nombresDoctor.add("Seleccione doctor");

        Cursor cursor = null;
        try {
            helper.abrir();
            cursor = helper.consultarDoctoresCursor();
            if (cursor.moveToFirst()) {
                do {
                    String dui = cursor.getString(cursor.getColumnIndexOrThrow("DUI_DOCTOR"));
                    String nombre = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_DOCTOR"));

                    duisDoctor.add(dui);
                    nombresDoctor.add(nombre + " - " + dui);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar doctores: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) cursor.close();
            helper.cerrar();
        }

        configurarEstiloSpinner(spnDoctorMod, nombresDoctor.toArray(new String[0]));
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
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        } finally {
            if (cursor != null) cursor.close();
            helper.cerrar();
        }

        configurarEstiloSpinner(spnEmergenciaMod, nombresEmergencia.toArray(new String[0]));
    }

    private void buscarConsulta() {
        String idStr = edtIdConsultaMod.getText().toString().trim();
        if (idStr.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID de la consulta", Toast.LENGTH_SHORT).show();
            return;
        }

        helper.abrir();
        Consulta consulta = helper.consultarConsulta(Integer.parseInt(idStr));
        helper.cerrar();

        if (consulta != null) {
            edtDuiPacienteMod.setText(consulta.getDuiPaciente());
            seleccionarDoctorEnSpinner(consulta.getDuiDoctor());
            edtFechaConsultaMod.setText(consulta.getFechaConsulta());
            edtDiagnosticoMod.setText(consulta.getDiagnostico());
            edtCargoTotalMod.setText(String.valueOf(consulta.getCargoTotalConsulta()));

            seleccionarEmergenciaEnSpinner(consulta.getCodEmergencia());
            spnPagaMedMod.setSelection(consulta.getPagaMedicamento() == 1 ? 2 : 1);

            Toast.makeText(this, "Consulta encontrada", Toast.LENGTH_SHORT).show();
        } else {
            limpiarCamposSinId();
            Toast.makeText(this, "Consulta no encontrada", Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarEstiloSpinner(Spinner spinner, String[] datos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, datos) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                view.setPadding(dpToPx(12), 0, 0, 0);
                if (position == 0) {
                    view.setTextColor(Color.parseColor(COLOR_PLACEHOLDER));
                } else {
                    view.setTextColor(ContextCompat.getColor(getContext(), R.color.text_black));
                }
                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);
                view.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));
                if (position == 0) {
                    view.setTextColor(Color.parseColor(COLOR_PLACEHOLDER));
                } else {
                    view.setTextColor(ContextCompat.getColor(getContext(), R.color.text_black));
                }
                return view;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void actualizarConsulta() {
        String idStr = edtIdConsultaMod.getText().toString().trim();
        if (idStr.isEmpty() || camposObligatoriosVacios()) {
            Toast.makeText(this, "Complete todos los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        Consulta consulta = new Consulta();
        consulta.setIdConsulta(Integer.parseInt(idStr));
        consulta.setDuiPaciente(edtDuiPacienteMod.getText().toString().trim());
        consulta.setDuiDoctor(duisDoctor.get(spnDoctorMod.getSelectedItemPosition()));
        consulta.setCodEmergencia(codigosEmergencia.get(spnEmergenciaMod.getSelectedItemPosition()));
        consulta.setFechaConsulta(edtFechaConsultaMod.getText().toString().trim());
        consulta.setDiagnostico(edtDiagnosticoMod.getText().toString().trim());

        String cargoStr = edtCargoTotalMod.getText().toString().trim();
        consulta.setCargoTotalConsulta(cargoStr.isEmpty() ? 0.0f : Float.parseFloat(cargoStr));

        consulta.setPagaMedicamento(spnPagaMedMod.getSelectedItemPosition() == 2 ? 1 : 0);

        helper.abrir();
        String mensaje = helper.actualizarConsulta(consulta);
        helper.cerrar();

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    private boolean camposObligatoriosVacios() {
        return edtDuiPacienteMod.getText().toString().isEmpty() ||
                spnDoctorMod.getSelectedItemPosition() == 0 ||
                spnEmergenciaMod.getSelectedItemPosition() == 0 ||
                spnPagaMedMod.getSelectedItemPosition() == 0;
    }

    private void seleccionarDoctorEnSpinner(String duiDoctor) {
        int posicion = duisDoctor.indexOf(duiDoctor);
        spnDoctorMod.setSelection(posicion != -1 ? posicion : 0);
    }

    private void seleccionarEmergenciaEnSpinner(String valorCodigo) {
        int posicion = codigosEmergencia.indexOf(valorCodigo);
        spnEmergenciaMod.setSelection(posicion != -1 ? posicion : 0);
    }

    private void limpiarCamposSinId() {
        edtDuiPacienteMod.setText("");
        spnDoctorMod.setSelection(0);
        edtFechaConsultaMod.setText("");
        edtDiagnosticoMod.setText("");
        edtCargoTotalMod.setText("");
        spnEmergenciaMod.setSelection(0);
        spnPagaMedMod.setSelection(0);
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
