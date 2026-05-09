package ues.fia.proyecto1pdm115.especialidades;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Especialidad;

public class ModificarEspecialidadActivity
        extends AppCompatActivity {

    EditText edtIdEspecialidadModificar,
            edtNombreEspecialidadModificar;

    Button btnBuscarModificarEspecialidad,
            btnActualizarEspecialidad,
            btnRegresarModificarEspecialidad;

    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_modificar_especialidad
        );

        helper = new controlDBHospitalApp(this);

        edtIdEspecialidadModificar =
                findViewById(
                        R.id.edtIdEspecialidadModificar
                );

        edtNombreEspecialidadModificar =
                findViewById(
                        R.id.edtNombreEspecialidadModificar
                );

        btnBuscarModificarEspecialidad =
                findViewById(
                        R.id.btnBuscarModificarEspecialidad
                );

        btnActualizarEspecialidad =
                findViewById(
                        R.id.btnActualizarEspecialidad
                );

        btnRegresarModificarEspecialidad =
                findViewById(
                        R.id.btnRegresarModificarEspecialidad
                );

        btnBuscarModificarEspecialidad
                .setOnClickListener(
                        v -> buscarEspecialidad()
                );

        btnActualizarEspecialidad
                .setOnClickListener(
                        v -> actualizarEspecialidad()
                );

        btnRegresarModificarEspecialidad
                .setOnClickListener(
                        v -> finish()
                );
    }

    private void buscarEspecialidad() {

        String textoId =
                edtIdEspecialidadModificar
                        .getText()
                        .toString()
                        .trim();

        if (textoId.isEmpty()) {

            Toast.makeText(
                    this,
                    "Ingrese el ID",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int idEspecialidad =
                Integer.parseInt(textoId);

        helper.abrir();

        Especialidad especialidad =
                helper.consultarEspecialidad(
                        idEspecialidad
                );

        helper.cerrar();

        if (especialidad != null) {

            edtNombreEspecialidadModificar
                    .setText(
                            valor(
                                    especialidad
                                            .getNombreEspecialidad()
                            )
                    );

            Toast.makeText(
                    this,
                    "Especialidad encontrada",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            limpiarCamposSinId();

            Toast.makeText(
                    this,
                    "Especialidad no encontrada",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void actualizarEspecialidad() {

        String textoId =
                edtIdEspecialidadModificar
                        .getText()
                        .toString()
                        .trim();

        if (textoId.isEmpty()) {

            Toast.makeText(
                    this,
                    "Ingrese el ID",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String nombre =
                edtNombreEspecialidadModificar
                        .getText()
                        .toString()
                        .trim();

        if (nombre.isEmpty()) {

            Toast.makeText(
                    this,
                    "Ingrese el nombre",
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

        Especialidad especialidad =
                new Especialidad();

        especialidad.setIdEspecialidad(
                Integer.parseInt(textoId)
        );

        especialidad.setNombreEspecialidad(
                nombre
        );

        helper.abrir();

        String mensaje =
                helper.actualizarEspecialidad(
                        especialidad
                );

        helper.cerrar();

        Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_LONG
        ).show();
        limpiarPosActualizacion();
    }

    private void limpiarCamposSinId() {

        edtNombreEspecialidadModificar
                .setText("");
    }
    private void limpiarPosActualizacion(){
        edtNombreEspecialidadModificar.setText("");
        edtIdEspecialidadModificar.setText("");
    }
    private String valor(String texto) {

        if (texto == null) {

            return "";
        }

        return texto;
    }
}