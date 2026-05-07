package ues.fia.proyecto1pdm115.especialidades;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Especialidad;

public class CrearEspecialidadActivity extends AppCompatActivity {

    EditText edtNombreEspecialidadCrear;

    Button btnGuardarEspecialidad, btnRegresarEspecialidad;

    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_especialidad);

        helper = new controlDBHospitalApp(this);

        edtNombreEspecialidadCrear =
                findViewById(R.id.edtNombreEspecialidadCrear);

        btnGuardarEspecialidad =
                findViewById(R.id.btnGuardarEspecialidad);

        btnRegresarEspecialidad =
                findViewById(R.id.btnRegresarEspecialidad);

        btnGuardarEspecialidad.setOnClickListener(
                v -> guardarEspecialidad()
        );

        btnRegresarEspecialidad.setOnClickListener(
                v -> finish()
        );
    }

    private void guardarEspecialidad() {

        String nombre =
                edtNombreEspecialidadCrear
                        .getText()
                        .toString()
                        .trim();

        if (nombre.isEmpty()) {

            Toast.makeText(
                    this,
                    "Ingrese una especialidad",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (nombre.length() > 20) {

            Toast.makeText(
                    this,
                    "Máximo 20 caracteres",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        Especialidad especialidad = new Especialidad();

        especialidad.setNombreEspecialidad(nombre);

        helper.abrir();

        String mensaje =
                helper.insertarEspecialidad(especialidad);

        helper.cerrar();

        Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_LONG
        ).show();

        limpiarCampos();
    }

    private void limpiarCampos() {

        edtNombreEspecialidadCrear.setText("");
    }
}