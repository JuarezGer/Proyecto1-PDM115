package ues.fia.proyecto1pdm115.usuarios;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Usuario;

public class EliminarUsuarioActivity extends AppCompatActivity {

    EditText edtIdUsuarioEliminar;

    Button btnBuscarUsuarioEliminar,
            btnEliminarUsuario,
            btnRegresarEliminarUsuario;

    TextView txtUsuarioEliminar;

    controlDBHospitalApp helper;

    String idEncontrado = "NO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_eliminar_usuario
        );

        helper = new controlDBHospitalApp(this);

        edtIdUsuarioEliminar =
                findViewById(
                        R.id.edtIdUsuarioEliminar
                );

        btnBuscarUsuarioEliminar =
                findViewById(
                        R.id.btnBuscarUsuarioEliminar
                );

        btnEliminarUsuario =
                findViewById(
                        R.id.btnEliminarUsuario
                );

        btnRegresarEliminarUsuario =
                findViewById(
                        R.id.btnRegresarEliminarUsuario
                );

        txtUsuarioEliminar =
                findViewById(
                        R.id.txtUsuarioEliminar
                );

        btnBuscarUsuarioEliminar
                .setOnClickListener(
                        v -> buscarUsuario()
                );

        btnEliminarUsuario
                .setOnClickListener(
                        v -> confirmarEliminacion()
                );

        btnRegresarEliminarUsuario
                .setOnClickListener(
                        v -> finish()
                );
    }

    private void buscarUsuario() {

        String textoId =
                edtIdUsuarioEliminar
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

        String idEspecialidad =
                textoId;

        helper.abrir();

        Usuario usuario =
                helper.consultarUsuario(
                        idEspecialidad
                );

        helper.cerrar();

        if (usuario != null) {

            idEncontrado =
                    usuario.getIdUsuario();

            String datos =
                    "ID: "
                            + usuario.getIdUsuario()
                            + "\n\nNombre: "
                            + usuario.getNombreUsuario();

            txtUsuarioEliminar
                    .setText(datos);

            Toast.makeText(
                    this,
                    "Usuario encontrado",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            idEncontrado = "NO";

            txtUsuarioEliminar.setText(
                    "Usuario no encontrado."
            );

            Toast.makeText(
                    this,
                    "Usuario no encontrado",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void confirmarEliminacion() {

        if (idEncontrado == "NO") {

            Toast.makeText(
                    this,
                    "Primero busque un usuario válido",
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
                "¿Desea eliminar este usuario?"
        );

        builder.setPositiveButton(
                "Sí, eliminar",
                (dialog, which) -> eliminarUsuario()
        );

        builder.setNegativeButton(
                "Cancelar",
                (dialog, which) -> dialog.dismiss()
        );

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void eliminarUsuario() {

        helper.abrir();

        String mensaje =
                helper.eliminarUsuario(
                        idEncontrado
                );

        helper.cerrar();

        Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_LONG
        ).show();

        edtIdUsuarioEliminar.setText("");

        txtUsuarioEliminar.setText(
                "Datos de usuario..."
        );

        idEncontrado = "NO";
    }
}