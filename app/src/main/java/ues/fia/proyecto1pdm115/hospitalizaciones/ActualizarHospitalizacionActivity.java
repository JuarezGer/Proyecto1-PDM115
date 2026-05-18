package ues.fia.proyecto1pdm115.hospitalizaciones;

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
import ues.fia.proyecto1pdm115.modelos.Hospitalizacion;
import ues.fia.proyecto1pdm115.modelos.Paciente;

public class ActualizarHospitalizacionActivity extends AppCompatActivity {

    EditText edtIdHospitalizacionModificar,
            edtFechaInicioHospModificar,
            edtFechaFinHospModificar,
            edtMotivoIngresoModificar,
            edtCostoHospitalizacionModificar;

    Spinner spnConsultaModificar;

    Button btnBuscarHospitalizacion,
            btnActualizarHospitalizacion,
            btnRegresarModificar;

    controlDBHospitalApp helper;

    ArrayList<Integer> idsConsultas = new ArrayList<>();

    ArrayList<String> nombresConsultas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_modificar_hospitalizacion
        );

        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtIdHospitalizacionModificar =
                findViewById(
                        R.id.edtIdHospitalizacionModificar
                );

        edtFechaInicioHospModificar =
                findViewById(
                        R.id.edtFechaInicioHospModificar
                );

        edtFechaFinHospModificar =
                findViewById(
                        R.id.edtFechaFinHospModificar
                );

        edtMotivoIngresoModificar =
                findViewById(
                        R.id.edtMotivoIngresoModificar
                );

        edtCostoHospitalizacionModificar =
                findViewById(
                        R.id.edtCostoHospitalizacionModificar
                );

        spnConsultaModificar =
                findViewById(
                        R.id.spnConsultaModificar
                );

        btnBuscarHospitalizacion.setOnClickListener(
                v -> buscarHospitalizacion()
        );

        btnActualizarHospitalizacion.setOnClickListener(
                v -> actualizarHospitalizacion()
        );

        btnRegresarModificar.setOnClickListener(
                v -> finish()
        );

        cargarConsultas(-1);

        int idHospitalizacionIntent =
                getIntent().getIntExtra(
                        "ID_HOSPITALIZACION",
                        -1
                );

        if (idHospitalizacionIntent != -1) {
            edtIdHospitalizacionModificar.setText(
                    String.valueOf(idHospitalizacionIntent)
            );

            buscarHospitalizacion();
        }

    }

    private void cargarConsultas(int idHospitalizacionActual) {

        idsConsultas.clear();
        nombresConsultas.clear();

        idsConsultas.add(0);
        nombresConsultas.add("Seleccione una consulta");

        Cursor cursor = null;

        try {
            helper.abrir();

            cursor = helper.consultarConsultasParaHospitalizacionActualizarCursor(
                    idHospitalizacionActual
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idConsulta = cursor.getInt(
                            cursor.getColumnIndexOrThrow("ID_CONSULTA")
                    );

                    String fecha = cursor.getString(
                            cursor.getColumnIndexOrThrow("FECHA_CONSULTA")
                    );

                    String diagnostico = cursor.getString(
                            cursor.getColumnIndexOrThrow("DIAGNOSTICO")
                    );

                    String paciente = cursor.getString(
                            cursor.getColumnIndexOrThrow("NOMBRE_PACIENTE")
                    );

                    String doctor = cursor.getString(
                            cursor.getColumnIndexOrThrow("NOMBRE_DOCTOR")
                    );

                    idsConsultas.add(idConsulta);

                    nombresConsultas.add(
                            "Consulta #" + idConsulta +
                                    " | " + fecha +
                                    " | Paciente: " + paciente +
                                    " | Doctor: " + doctor +
                                    " | Diagnóstico: " + diagnostico
                    );

                } while (cursor.moveToNext());
            } else {
                Toast.makeText(
                        this,
                        "No hay consultas disponibles",
                        Toast.LENGTH_SHORT
                ).show();
            }

        } catch (Exception e) {
            Toast.makeText(
                    this,
                    "Error al cargar consultas: " + e.getMessage(),
                    Toast.LENGTH_LONG
            ).show();

        } finally {
            if (cursor != null) {
                cursor.close();
            }

            helper.cerrar();
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

        spnConsultaModificar.setAdapter(adapter);
    }

    private void buscarHospitalizacion() {

        String idTexto =
                edtIdHospitalizacionModificar
                        .getText()
                        .toString()
                        .trim();

        if (idTexto.isEmpty()) {

            Toast.makeText(
                    this,
                    "Ingrese el ID",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int idHospitalizacion =
                Integer.parseInt(idTexto);

        helper.abrir();

        Hospitalizacion hospitalizacion =
                helper.consultarHospitalizacion(
                        idHospitalizacion
                );

        helper.cerrar();

        if (hospitalizacion != null) {

            cargarConsultas(idHospitalizacion);

            edtFechaInicioHospModificar.setText(hospitalizacion.getFechaInicioHosp());

            edtFechaFinHospModificar.setText(hospitalizacion.getFechaFinHosp());

            edtMotivoIngresoModificar.setText(hospitalizacion.getMotivoIngreso());

            edtCostoHospitalizacionModificar.setText(String.valueOf(hospitalizacion.getCostoHospitalizacion()));

            int posicionConsulta = 0;

            for (int i = 0;
                 i < idsConsultas.size();
                 i++) {

                if (
                        idsConsultas.get(i)
                                .equals(
                                        hospitalizacion
                                                .getIdConsulta()
                                )
                ) {

                    posicionConsulta = i;
                    break;
                }
            }

            spnConsultaModificar
                    .setSelection(posicionConsulta);

            edtIdHospitalizacionModificar
                    .setEnabled(false);

            Toast.makeText(
                    this,
                    "Hospitalización encontrada",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            edtIdHospitalizacionModificar
                    .setEnabled(true);

            Toast.makeText(
                    this,
                    "No encontrada",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void actualizarHospitalizacion() {

        String idTexto =
                edtIdHospitalizacionModificar
                        .getText()
                        .toString()
                        .trim();

        if (
                idTexto.isEmpty()
                        || edtFechaInicioHospModificar
                        .getText()
                        .toString()
                        .trim()
                        .isEmpty()
                        || edtFechaFinHospModificar
                        .getText()
                        .toString()
                        .trim()
                        .isEmpty()
                        || edtMotivoIngresoModificar
                        .getText()
                        .toString()
                        .trim()
                        .isEmpty()
                        || edtCostoHospitalizacionModificar
                        .getText()
                        .toString()
                        .trim()
                        .isEmpty()
        ) {

            Toast.makeText(
                    this,
                    "Complete todos los campos",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        double costo = Double.parseDouble(edtCostoHospitalizacionModificar.getText().toString().trim());

        if (costo <= 0) {

            Toast.makeText(this,
                    "El costo de hospitalización debe ser mayor a 0",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        Hospitalizacion hospitalizacion =
                new Hospitalizacion();

        hospitalizacion.setIdHospitalizacion(
                Integer.parseInt(idTexto)
        );

        int idConsultaSeleccionada = obtenerIdConsultaSeleccionada();

        if (idConsultaSeleccionada == 0) {
            Toast.makeText(
                    this,
                    "Seleccione una consulta",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        hospitalizacion.setIdConsulta(idConsultaSeleccionada);

        hospitalizacion.setFechaInicioHosp(
                edtFechaInicioHospModificar
                        .getText()
                        .toString()
                        .trim()
        );

        hospitalizacion.setFechaFinHosp(
                edtFechaFinHospModificar
                        .getText()
                        .toString()
                        .trim()
        );

        hospitalizacion.setMotivoIngreso(
                edtMotivoIngresoModificar
                        .getText()
                        .toString()
                        .trim()
        );

        hospitalizacion.setCostoHospitalizacion(
                Double.parseDouble(
                        edtCostoHospitalizacionModificar
                                .getText()
                                .toString()
                                .trim()
                )
        );

        helper.abrir();

        String mensaje =
                helper.actualizarHospitalizacion(
                        hospitalizacion
                );

        helper.cerrar();

        Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_LONG
        ).show();
    }
    private int obtenerIdConsultaSeleccionada() {
        int posicion = spnConsultaModificar.getSelectedItemPosition();

        if (posicion < 0 || posicion >= idsConsultas.size()) {
            return 0;
        }

        return idsConsultas.get(posicion);
    }
}
