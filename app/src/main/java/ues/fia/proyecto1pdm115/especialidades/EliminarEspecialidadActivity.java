package ues.fia.proyecto1pdm115.especialidades;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Especialidad;

public class EliminarEspecialidadActivity
        extends AppCompatActivity {

    EditText edtIdEspecialidadEliminar;

    Button btnBuscarEspecialidadEliminar,
            btnEliminarEspecialidad,
            btnRegresarEliminarEspecialidad;

    TextView txtEspecialidadEliminar;

    controlDBHospitalApp helper;

    int idEncontrado = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_eliminar_especialidad
        );

        helper = new controlDBHospitalApp(this);

        edtIdEspecialidadEliminar =
                findViewById(
                        R.id.edtIdEspecialidadEliminar
                );

        btnBuscarEspecialidadEliminar =
                findViewById(
                        R.id.btnBuscarEspecialidadEliminar
                );

        btnEliminarEspecialidad =
                findViewById(
                        R.id.btnEliminarEspecialidad
                );

        btnRegresarEliminarEspecialidad =
                findViewById(
                        R.id.btnRegresarEliminarEspecialidad
                );

        txtEspecialidadEliminar =
                findViewById(
                        R.id.txtEspecialidadEliminar
                );

        btnBuscarEspecialidadEliminar
                .setOnClickListener(
                        v -> buscarEspecialidad()
                );

        btnEliminarEspecialidad
                .setOnClickListener(
                        v -> confirmarEliminacion()
                );

        btnRegresarEliminarEspecialidad
                .setOnClickListener(
                        v -> finish()
                );
    }

    private void buscarEspecialidad() {

        String textoId =
                edtIdEspecialidadEliminar
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

            idEncontrado =
                    especialidad.getIdEspecialidad();

            String datos =
                    "ID: "
                            + especialidad.getIdEspecialidad()
                            + "\n\nNombre: "
                            + especialidad.getNombreEspecialidad();

            txtEspecialidadEliminar
                    .setText(datos);

            Toast.makeText(
                    this,
                    "Especialidad encontrada",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            idEncontrado = -1;

            txtEspecialidadEliminar.setText(
                    "Especialidad no encontrada."
            );

            Toast.makeText(
                    this,
                    "Especialidad no encontrada",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void confirmarEliminacion() {

        if (idEncontrado == -1) {

            Toast.makeText(
                    this,
                    "Primero busque una especialidad válida",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle(
                "Confirmar eliminación"
        );

        builder.setMessage(
                "¿Desea eliminar esta especialidad?"
        );

        builder.setPositiveButton(
                "Sí, eliminar",
                (dialog, which) -> eliminarEspecialidad()
        );

        builder.setNegativeButton(
                "Cancelar",
                (dialog, which) -> dialog.dismiss()
        );

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void eliminarEspecialidad() {

        helper.abrir();

        String mensaje =
                helper.eliminarEspecialidad(
                        idEncontrado
                );

        helper.cerrar();

        Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_LONG
        ).show();

        edtIdEspecialidadEliminar.setText("");

        txtEspecialidadEliminar.setText(
                "Datos de especialidad..."
        );

        idEncontrado = -1;
    }
}