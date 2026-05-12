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

public class ModificarSeguroActivity extends AppCompatActivity {

    EditText edtIdPolizaActualizar, edtPorcentajeCoberturaActualizar,
            edtTipoAseguradoActualizar, edtDeductibleMedicinaActualizar,
            edtDeductibleOperacionActualizar;

    Spinner spnPacienteSeguroActualizar, spnAseguradoraSeguroActualizar;

    Button btnBuscarSeguroActualizar, btnActualizarSeguro, btnRegresarActualizarSeguro;

    controlDBHospitalApp helper;

    ArrayList<String> duiPacientes = new ArrayList<>();
    ArrayList<String> nombresPacientes = new ArrayList<>();
    ArrayList<Integer> idsAseguradoras = new ArrayList<>();
    ArrayList<String> nombresAseguradoras = new ArrayList<>();

    private final String COLOR_PLACEHOLDER = "#999999";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_seguro);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtIdPolizaActualizar = findViewById(R.id.edtIdPolizaActualizar);
        spnPacienteSeguroActualizar = findViewById(R.id.spnPacienteSeguroActualizar);
        spnAseguradoraSeguroActualizar = findViewById(R.id.spnAseguradoraSeguroActualizar);
        edtPorcentajeCoberturaActualizar = findViewById(R.id.edtPorcentajeCoberturaActualizar);
        edtTipoAseguradoActualizar = findViewById(R.id.edtTipoAseguradoActualizar);
        edtDeductibleMedicinaActualizar = findViewById(R.id.edtDeductibleMedicinaActualizar);
        edtDeductibleOperacionActualizar = findViewById(R.id.edtDeductibleOperacionActualizar);

        btnBuscarSeguroActualizar = findViewById(R.id.btnBuscarSeguroActualizar);
        btnActualizarSeguro = findViewById(R.id.btnActualizarSeguro);
        btnRegresarActualizarSeguro = findViewById(R.id.btnRegresarActualizarSeguro);

        cargarPacientes();
        cargarAseguradoras();

        btnBuscarSeguroActualizar.setOnClickListener(v -> buscarSeguro());
        btnActualizarSeguro.setOnClickListener(v -> actualizarSeguro());
        btnRegresarActualizarSeguro.setOnClickListener(v -> finish());
    }

    private void cargarPacientes() {
        duiPacientes.clear();
        nombresPacientes.clear();

        duiPacientes.add("");
        nombresPacientes.add("Seleccione un paciente");

        Cursor cursor = null;

        try {
            helper.abrir();
            helper.llenarDatosIniciales();
            cursor = helper.consultarPacientesParaSeguroCursor();

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

        spnPacienteSeguroActualizar.setAdapter(crearAdapter(nombresPacientes));
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

        spnAseguradoraSeguroActualizar.setAdapter(crearAdapter(nombresAseguradoras));
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
                    view.setTextColor(ContextCompat.getColor(ModificarSeguroActivity.this, R.color.text_black));
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
                    view.setTextColor(ContextCompat.getColor(ModificarSeguroActivity.this, R.color.text_black));
                }

                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
    }

    private void buscarSeguro() {
        if (edtIdPolizaActualizar.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Ingrese el ID de la póliza", Toast.LENGTH_SHORT).show();
            return;
        }

        int idPoliza;

        try {
            idPoliza = Integer.parseInt(edtIdPolizaActualizar.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID de póliza inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        helper.abrir();
        Seguro seguro = helper.consultarSeguro(idPoliza);
        helper.cerrar();

        if (seguro == null) {
            Toast.makeText(this, "Seguro no encontrado", Toast.LENGTH_SHORT).show();
            limpiarCamposSinId();
            return;
        }

        seleccionarPaciente(seguro.getDuiPaciente());
        seleccionarAseguradora(seguro.getIdAseguradora());
        edtPorcentajeCoberturaActualizar.setText(String.valueOf(seguro.getPorcentajeCobertura()));
        edtTipoAseguradoActualizar.setText(seguro.getTipoAsegurado());
        edtDeductibleMedicinaActualizar.setText(String.valueOf(seguro.getDeductibleMedicina()));
        edtDeductibleOperacionActualizar.setText(String.valueOf(seguro.getDeductibleOperacion()));
    }

    private void actualizarSeguro() {
        if (camposObligatoriosVacios()) {
            Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        int idPoliza;
        double porcentajeCobertura;
        double deductibleMedicina;
        double deductibleOperacion;

        try {
            idPoliza = Integer.parseInt(edtIdPolizaActualizar.getText().toString().trim());
            porcentajeCobertura = Double.parseDouble(edtPorcentajeCoberturaActualizar.getText().toString().trim());
            deductibleMedicina = Double.parseDouble(edtDeductibleMedicinaActualizar.getText().toString().trim());
            deductibleOperacion = Double.parseDouble(edtDeductibleOperacionActualizar.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Ingrese valores numéricos válidos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (porcentajeCobertura < 0 || porcentajeCobertura > 100) {
            Toast.makeText(this, "El porcentaje debe estar entre 0 y 100", Toast.LENGTH_SHORT).show();
            return;
        }

        Seguro seguro = new Seguro();
        seguro.setIdPoliza(idPoliza);
        seguro.setDuiPaciente(obtenerDuiPacienteSeleccionado());
        seguro.setIdAseguradora(obtenerIdAseguradoraSeleccionada());
        seguro.setPorcentajeCobertura(porcentajeCobertura);
        seguro.setTipoAsegurado(edtTipoAseguradoActualizar.getText().toString().trim());
        seguro.setDeductibleMedicina(deductibleMedicina);
        seguro.setDeductibleOperacion(deductibleOperacion);

        helper.abrir();
        String mensaje = helper.actualizarSeguro(seguro);
        helper.cerrar();

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
    }

    private boolean camposObligatoriosVacios() {
        return edtIdPolizaActualizar.getText().toString().trim().isEmpty() ||
                obtenerDuiPacienteSeleccionado().isEmpty() ||
                obtenerIdAseguradoraSeleccionada() == 0 ||
                edtPorcentajeCoberturaActualizar.getText().toString().trim().isEmpty() ||
                edtTipoAseguradoActualizar.getText().toString().trim().isEmpty() ||
                edtDeductibleMedicinaActualizar.getText().toString().trim().isEmpty() ||
                edtDeductibleOperacionActualizar.getText().toString().trim().isEmpty();
    }

    private String obtenerDuiPacienteSeleccionado() {
        int posicion = spnPacienteSeguroActualizar.getSelectedItemPosition();

        if (posicion <= 0 || posicion >= duiPacientes.size()) {
            return "";
        }

        return duiPacientes.get(posicion);
    }

    private int obtenerIdAseguradoraSeleccionada() {
        int posicion = spnAseguradoraSeguroActualizar.getSelectedItemPosition();

        if (posicion <= 0 || posicion >= idsAseguradoras.size()) {
            return 0;
        }

        return idsAseguradoras.get(posicion);
    }

    private void seleccionarPaciente(String duiPaciente) {
        int posicion = duiPacientes.indexOf(duiPaciente);

        if (posicion >= 0) {
            spnPacienteSeguroActualizar.setSelection(posicion);
        }
    }

    private void seleccionarAseguradora(int idAseguradora) {
        int posicion = idsAseguradoras.indexOf(idAseguradora);

        if (posicion >= 0) {
            spnAseguradoraSeguroActualizar.setSelection(posicion);
        }
    }

    private void limpiarCamposSinId() {
        spnPacienteSeguroActualizar.setSelection(0);
        spnAseguradoraSeguroActualizar.setSelection(0);
        edtPorcentajeCoberturaActualizar.setText("");
        edtTipoAseguradoActualizar.setText("");
        edtDeductibleMedicinaActualizar.setText("");
        edtDeductibleOperacionActualizar.setText("");
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}
