package ues.fia.proyecto1pdm115.recetas;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Consulta;
import ues.fia.proyecto1pdm115.modelos.Receta;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.Toast;

public class CrearRecetaActivity extends AppCompatActivity {
    EditText edtFechaEmisionReceCrear,
            edtEstadoReceCrear;
    Spinner spnConsultaCrearReceta;
    Button btnRegresarCrearReceta,
            btnGuardarReceta;
    controlDBHospitalApp helper;

    ArrayList<Integer> idsConsultas = new ArrayList<>();
    ArrayList<String> nombresConsultas = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_receta);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        spnConsultaCrearReceta = findViewById(R.id.spnConsultaCrearReceta);
        edtFechaEmisionReceCrear = findViewById(R.id.edtFechaEmisionReceCrear);
        edtEstadoReceCrear = findViewById(R.id.edtEstadoReceCrear);
        btnGuardarReceta = findViewById(R.id.btnGuardarReceta);
        btnRegresarCrearReceta = findViewById(R.id.btnRegresarCrearReceta);

        btnGuardarReceta.setOnClickListener(v -> guardarReceta());

        btnRegresarCrearReceta.setOnClickListener(v -> finish());

        cargarConsultas();
    }

    private void guardarReceta(){
        if (camposObligatoriosVacios()) {
            Toast.makeText(this,
                    "Complete los campos obligatorios",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        Receta receta = new Receta();
        receta.setFechaEmision(
                edtFechaEmisionReceCrear.getText().toString().trim());

        receta.setEstadoReceta(
                edtEstadoReceCrear.getText().toString().trim());

        receta.setIdConsulta(
                obtenerIdConsultaSeleccionada()
        );
        try {

            helper.abrir();

            String mensaje =
                    helper.insertarReceta(receta);

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
        edtFechaEmisionReceCrear.setText("");
        edtEstadoReceCrear.setText("");
        spnConsultaCrearReceta.setSelection(0);
    }

    private boolean camposObligatoriosVacios(){
        return  edtFechaEmisionReceCrear.getText().toString().trim().isEmpty() ||
                edtEstadoReceCrear.getText().toString().trim().isEmpty() ||
                obtenerIdConsultaSeleccionada() == 0;
    }

    private void cargarConsultas() {

        idsConsultas.clear();
        nombresConsultas.clear();

        idsConsultas.add(0);
        nombresConsultas.add("Seleccione una consulta");

        Cursor cursor = null;

        try {
            helper.abrir();

            cursor = helper.consultarConsultasParaRecetaCursor();

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
                        "No hay consultas registradas",
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

        spnConsultaCrearReceta.setAdapter(adapter);
    }

    private int obtenerIdConsultaSeleccionada(){
        int posicion =
                spnConsultaCrearReceta.getSelectedItemPosition();

        return idsConsultas.get(posicion);
    }
}

