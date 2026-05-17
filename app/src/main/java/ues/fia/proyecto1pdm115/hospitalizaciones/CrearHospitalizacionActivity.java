package ues.fia.proyecto1pdm115.hospitalizaciones;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Consulta;
import ues.fia.proyecto1pdm115.modelos.Hospitalizacion;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class CrearHospitalizacionActivity extends AppCompatActivity {

    EditText edtFechaInicioHospCrear,
            edtFechaFinHospCrear,
            edtMotivoIngresoCrear,
            edtCostoHospitalizacionCrear;

    Spinner spnConsultaCrear;

    Button btnRegresarCrearHospitalizacion, btnGuardarHospitalizacion;
    controlDBHospitalApp helper;
    ArrayList<Integer> idsConsultas = new ArrayList<>();
    ArrayList<String> nombresConsultas = new ArrayList<>();
    private final String COLOR_PLACEHOLDER = "#999999";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_hospitalizacion);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        spnConsultaCrear = findViewById(R.id.spnConsultaCrear);
        edtFechaInicioHospCrear = findViewById(R.id.edtFechaInicioHospCrear);
        edtFechaFinHospCrear = findViewById(R.id.edtFechaFinHospCrear);
        edtMotivoIngresoCrear = findViewById(R.id.edtMotivoIngresoCrear);
        edtCostoHospitalizacionCrear = findViewById(R.id.edtCostoHospitalizacionCrear);
        btnGuardarHospitalizacion = findViewById(R.id.btnGuardarHospitalizacion);
        btnRegresarCrearHospitalizacion = findViewById(R.id.btnRegresarCrearHospitalizacion);

        btnGuardarHospitalizacion.setOnClickListener(v -> guardarHospitalizacion());

        btnRegresarCrearHospitalizacion.setOnClickListener(v -> finish());

        cargarConsultas();
    }

    private void guardarHospitalizacion(){
        if (camposObligatoriosVacios()) {
            Toast.makeText(this,
                    "Complete los campos obligatorios",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        double costo = Double.parseDouble(edtCostoHospitalizacionCrear.getText().toString().trim());

        if (costo <= 0) {

            Toast.makeText(this,
                    "El costo de hospitalización debe ser mayor a 0",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        Hospitalizacion hospitalizacion = new Hospitalizacion();
        hospitalizacion.setFechaInicioHosp(
                edtFechaInicioHospCrear.getText().toString().trim());

        hospitalizacion.setFechaFinHosp(
                edtFechaFinHospCrear.getText().toString().trim());

        hospitalizacion.setMotivoIngreso(
                edtMotivoIngresoCrear.getText().toString().trim());

        hospitalizacion.setCostoHospitalizacion(
                Double.parseDouble(
                        edtCostoHospitalizacionCrear.getText().toString().trim()
                ));
        hospitalizacion.setIdConsulta(
                obtenerIdConsultaSeleccionada()
        );
        try {

            helper.abrir();

            String mensaje =
                    helper.insertarHospitalizacion(hospitalizacion);

            Toast.makeText(this,
                    mensaje,
                    Toast.LENGTH_LONG).show();

            if (mensaje.toLowerCase().contains("correctamente")) {
                limpiarCampos();
            }

        } catch (Exception e) {

            Toast.makeText(this,
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();

        } finally {
            helper.cerrar();
        }
    }

    private void limpiarCampos(){
        edtFechaInicioHospCrear.setText("");
        edtFechaFinHospCrear.setText("");
        edtMotivoIngresoCrear.setText("");
        edtCostoHospitalizacionCrear.setText("");
        spnConsultaCrear.setSelection(0);
    }

    private boolean camposObligatoriosVacios(){
        return  edtFechaInicioHospCrear.getText().toString().trim().isEmpty() ||
                edtFechaFinHospCrear.getText().toString().trim().isEmpty() ||
                edtMotivoIngresoCrear.getText().toString().trim().isEmpty() ||
                edtCostoHospitalizacionCrear.getText().toString().trim().isEmpty() ||
                obtenerIdConsultaSeleccionada() == 0;
    }

    private void cargarConsultas() {

        idsConsultas.clear();
        nombresConsultas.clear();

        idsConsultas.add(0);
        nombresConsultas.add("Seleccione una consulta");

        helper.abrir();

        ArrayList<Consulta> lista =
                helper.consultarTodasConsultas();

        helper.cerrar();

        for (Consulta c : lista) {

            idsConsultas.add(
                    c.getIdConsulta()
            );

            nombresConsultas.add(
                    "Consulta #" + c.getIdConsulta()
            );
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        nombresConsultas
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spnConsultaCrear.setAdapter(adapter);
    }

    private int obtenerIdConsultaSeleccionada(){
        int posicion =
                spnConsultaCrear.getSelectedItemPosition();

        return idsConsultas.get(posicion);
    }
}
