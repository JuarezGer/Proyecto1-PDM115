package ues.fia.proyecto1pdm115.seguros;

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
import ues.fia.proyecto1pdm115.modelos.Seguro;

public class CrearSeguroActivity extends AppCompatActivity {

    Spinner spnPacienteSeguroCrear, spnAseguradoraSeguroCrear;
    EditText edtPorcentajeCoberturaCrear, edtTipoAseguradoCrear,
            edtDeductibleMedicinaCrear, edtDeductibleOperacionCrear;
    Button btnGuardarSeguro, btnRegresarCrearSeguro;

    controlDBHospitalApp helper;

    ArrayList<String> duiPacientes = new ArrayList<>();
    ArrayList<String> nombresPacientes = new ArrayList<>();
    ArrayList<Integer> idsAseguradoras = new ArrayList<>();
    ArrayList<String> nombresAseguradoras = new ArrayList<>();

    private final String COLOR_PLACEHOLDER = "#999999";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_seguro);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        spnPacienteSeguroCrear = findViewById(R.id.spnPacienteSeguroCrear);
        spnAseguradoraSeguroCrear = findViewById(R.id.spnAseguradoraSeguroCrear);
        edtPorcentajeCoberturaCrear = findViewById(R.id.edtPorcentajeCoberturaCrear);
        edtTipoAseguradoCrear = findViewById(R.id.edtTipoAseguradoCrear);
        edtDeductibleMedicinaCrear = findViewById(R.id.edtDeductibleMedicinaCrear);
        edtDeductibleOperacionCrear = findViewById(R.id.edtDeductibleOperacionCrear);

        btnGuardarSeguro = findViewById(R.id.btnGuardarSeguro);
        btnRegresarCrearSeguro = findViewById(R.id.btnRegresarCrearSeguro);

        cargarPacientesSinSeguro();
        cargarAseguradoras();

        btnGuardarSeguro.setOnClickListener(v -> guardarSeguro());
        btnRegresarCrearSeguro.setOnClickListener(v -> finish());
    }

    private void cargarPacientesSinSeguro() {
        duiPacientes.clear();
        nombresPacientes.clear();

        duiPacientes.add("");
        nombresPacientes.add("Seleccione un paciente");

        Cursor cursor = null;

        try {
            helper.abrir();
            helper.llenarDatosIniciales();
            cursor = helper.consultarPacientesSinSeguroCursor();

            if (cursor.moveToFirst()) {
                do {
                    String dui = cursor.getString(cursor.getColumnIndexOrThrow("DUI_PACIENTE"));
                    String nombre = cursor.getString(cursor.getColumnIndexOrThrow("PRIMER_NOMBRE_PACIENTE"));
                    String apellido = cursor.getString(cursor.getColumnIndexOrThrow("PRIMER_APELLIDO_PACIENTE"));

                    duiPacientes.add(dui);
                    nombresPacientes.add(nombre + " " + apellido + " (" + dui + ")");

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar pacientes: " + e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            helper.cerrar();
        }

        spnPacienteSeguroCrear.setAdapter(crearAdapter(nombresPacientes));
    }

    private void cargarAseguradoras() {
        idsAseguradoras.clear();
        nombresAseguradoras.clear();

        idsAseguradoras.add(0);
        nombresAseguradoras.add("Seleccione una aseguradora");

        Cursor cursor = null;

        try {
            helper.abrir();
            helper.llenarDatosIniciales();
            cursor = helper.consultarAseguradorasParaSeguroCursor();

            if (cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("ID_ASEGURADORA"));
                    String nombre = cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_ASEGURADORA"));

                    idsAseguradoras.add(id);
                    nombresAseguradoras.add(nombre + " (ID " + id + ")");

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar aseguradoras: " + e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
            helper.cerrar();
        }

        spnAseguradoraSeguroCrear.setAdapter(crearAdapter(nombresAseguradoras));
    }

    private ArrayAdapter<String> crearAdapter(ArrayList<String> datos) {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                datos
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);
                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                view.setGravity(Gravity.CENTER_VERTICAL);
                view.setSingleLine(false);
                view.setPadding(dpToPx(12), 0, dpToPx(12), 0);

                if (position == 0) {
                    view.setTextColor(Color.parseColor(COLOR_PLACEHOLDER));
                } else {
                    view.setTextColor(ContextCompat.getColor(CrearSeguroActivity.this, R.color.text_black));
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
                    view.setTextColor(ContextCompat.getColor(CrearSeguroActivity.this, R.color.text_black));
                }

                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private String obtenerDuiPacienteSeleccionado() {
        int posicion = spnPacienteSeguroCrear.getSelectedItemPosition();

        if (posicion <= 0 || posicion >= duiPacientes.size()) {
            return "";
        }

        return duiPacientes.get(posicion);
    }

    private int obtenerIdAseguradoraSeleccionada() {
        int posicion = spnAseguradoraSeguroCrear.getSelectedItemPosition();

        if (posicion <= 0 || posicion >= idsAseguradoras.size()) {
            return 0;
        }

        return idsAseguradoras.get(posicion);
    }

    private void guardarSeguro() {
        if (camposObligatoriosVacios()) {
            Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        double porcentajeCobertura;
        double deductibleMedicina;
        double deductibleOperacion;

        try {
            porcentajeCobertura = Double.parseDouble(edtPorcentajeCoberturaCrear.getText().toString().trim());
            deductibleMedicina = Double.parseDouble(edtDeductibleMedicinaCrear.getText().toString().trim());
            deductibleOperacion = Double.parseDouble(edtDeductibleOperacionCrear.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese valores numéricos válidos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (porcentajeCobertura < 0 || porcentajeCobertura > 100) {
            Toast.makeText(this, "El porcentaje debe estar entre 0 y 100", Toast.LENGTH_SHORT).show();
            return;
        }

        Seguro seguro = new Seguro();
        seguro.setDuiPaciente(obtenerDuiPacienteSeleccionado());
        seguro.setIdAseguradora(obtenerIdAseguradoraSeleccionada());
        seguro.setPorcentajeCobertura(porcentajeCobertura);
        seguro.setTipoAsegurado(edtTipoAseguradoCrear.getText().toString().trim());
        seguro.setDeductibleMedicina(deductibleMedicina);
        seguro.setDeductibleOperacion(deductibleOperacion);

        helper.abrir();
        helper.llenarDatosIniciales();
        String mensaje = helper.insertarSeguro(seguro);
        helper.cerrar();

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
        limpiarCampos();
        cargarPacientesSinSeguro();
    }

    private boolean camposObligatoriosVacios() {
        return obtenerDuiPacienteSeleccionado().isEmpty() ||
                obtenerIdAseguradoraSeleccionada() == 0 ||
                edtPorcentajeCoberturaCrear.getText().toString().trim().isEmpty() ||
                edtTipoAseguradoCrear.getText().toString().trim().isEmpty() ||
                edtDeductibleMedicinaCrear.getText().toString().trim().isEmpty() ||
                edtDeductibleOperacionCrear.getText().toString().trim().isEmpty();
    }

    private void limpiarCampos() {
        spnPacienteSeguroCrear.setSelection(0);
        spnAseguradoraSeguroCrear.setSelection(0);
        edtPorcentajeCoberturaCrear.setText("");
        edtTipoAseguradoCrear.setText("");
        edtDeductibleMedicinaCrear.setText("");
        edtDeductibleOperacionCrear.setText("");
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}
