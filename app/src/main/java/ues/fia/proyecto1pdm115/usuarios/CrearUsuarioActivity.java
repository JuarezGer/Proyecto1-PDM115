package ues.fia.proyecto1pdm115.usuarios;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Usuario;

import ues.fia.proyecto1pdm115.R;

public class CrearUsuarioActivity extends AppCompatActivity {
    EditText edtUsuarioIdCrear,edtNombreUsuarioCrear,edtClaveUsuarioCrear;

    Button btnGuardarUsuario, btnRegresarUsuario;

    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_crear_usuario);

        helper = new controlDBHospitalApp(this);

        edtUsuarioIdCrear = findViewById(R.id.edtUsuarioIdCrear);
        edtNombreUsuarioCrear = findViewById(R.id.edtNombreUsuarioCrear);
        edtClaveUsuarioCrear = findViewById(R.id.edtClaveUsuarioCrear);
        btnGuardarUsuario = findViewById(R.id.btnGuardarUsuario);
        btnRegresarUsuario = findViewById(R.id.btnRegresarUsuario);
        btnGuardarUsuario.setOnClickListener(v -> guardarUsuario());
        btnRegresarUsuario.setOnClickListener(v -> finish());
    }

    private void guardarUsuario(){
        String id = edtUsuarioIdCrear.getText().toString().trim();
        String nombre = edtNombreUsuarioCrear.getText().toString().trim();
        String clave = edtClaveUsuarioCrear.getText().toString().trim();

        if (nombre.isEmpty()) {

            Toast.makeText(
                    this,
                    "Ingrese nombre de usuario",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (id.isEmpty()) {

            Toast.makeText(
                    this,
                    "Ingrese id de usuario",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (clave.isEmpty()) {

            Toast.makeText(
                    this,
                    "Ingrese clave para usuario",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (nombre.length() > 20) {

            Toast.makeText(
                    this,
                    "El nombre de usuario no puede exceder los 20 caracteres",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }
        if (id.length() > 2) {

            Toast.makeText(
                    this,
                    "Máximo 2 caracteres para id EJ: U1",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(id);
        usuario.setNombreUsuario(nombre);
        usuario.setClave(clave);

        helper.abrir();

        String mensaje = helper.insertarUsuario(usuario);
        helper.cerrar();

        Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_LONG
        ).show();

        limpiarCampos();
    }

    private void limpiarCampos(){
        edtUsuarioIdCrear.setText("");
        edtNombreUsuarioCrear.setText("");
        edtClaveUsuarioCrear.setText("");
    }
}