package ues.fia.proyecto1pdm115.usuarios;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Especialidad;
import ues.fia.proyecto1pdm115.modelos.Usuario;

public class ModificarUsuarioActivity extends AppCompatActivity {
    EditText edtIdUsuarioModificar, edtNombreUsuarioModificar, edtClaveUsuarioModificar;

    Button btnBuscarModificarUsuario, btnActualizarUsuario, btnRegresarModificarUsuario;

    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_usuario);

        helper=new controlDBHospitalApp(this);
        edtIdUsuarioModificar = findViewById(R.id.edtIdUsuarioModificar);
        edtNombreUsuarioModificar = findViewById(R.id.edtNombreUsuarioModificar);
        edtClaveUsuarioModificar = findViewById(R.id.edtClaveUsuarioModificar);

        btnBuscarModificarUsuario = findViewById(R.id.btnBuscarModificarUsuario);
        btnActualizarUsuario=findViewById(R.id.btnActualizarUsuario);
        btnRegresarModificarUsuario=findViewById(R.id.btnRegresarModificarUsuario);

        btnBuscarModificarUsuario.setOnClickListener(v -> buscarUsuario());
        btnActualizarUsuario.setOnClickListener(v -> actualizarUsuario());
        btnRegresarModificarUsuario.setOnClickListener(v -> finish());
    }

    private  void buscarUsuario(){
        String textoid = edtIdUsuarioModificar.getText().toString().trim();

        if (textoid.isEmpty()){
            Toast.makeText(this,"Ingrese el ID",Toast.LENGTH_SHORT).show();
            return;
        }

        String idUsuario = textoid;

        helper.abrir();
        Usuario usuario = helper.consultarUsuario(idUsuario);
        helper.cerrar();

        if (usuario!=null){
            edtNombreUsuarioModificar.setText(valor(usuario.getNombreUsuario()));
            edtClaveUsuarioModificar.setText(valor(usuario.getClave()));

            Toast.makeText(this,"Usuario encontrado",Toast.LENGTH_SHORT).show();
        }else {
            limpiarCamposSinId();

            Toast.makeText(this,"Usuario no encontrado",Toast.LENGTH_SHORT).show();
        }
    }

    private void actualizarUsuario(){
        String textoId = edtIdUsuarioModificar.getText().toString().trim();

        if (textoId.isEmpty()) {

            Toast.makeText(
                    this,
                    "Ingrese el ID",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        String nombreUsuario = edtNombreUsuarioModificar.getText().toString().trim();
        String clave = edtClaveUsuarioModificar.getText().toString().trim();
        if (nombreUsuario.isEmpty()) {

            Toast.makeText(
                    this,
                    "Ingrese el nombre de usuario",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }
        if (clave.isEmpty()) {

            Toast.makeText(
                    this,
                    "Ingrese la clave",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }
        if (nombreUsuario.length() > 20) {

            Toast.makeText(
                    this,
                    "Máximo 20 caracteres para nombre usuario",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }
        if (clave.length() > 8) {

            Toast.makeText(
                    this,
                    "Clave no mayor a 8 caracteres",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        Usuario usuario = new Usuario();
        usuario.setIdUsuario(textoId);
        usuario.setNombreUsuario(nombreUsuario);
        usuario.setClave(clave);

        helper.abrir();
        String mensaje = helper.actualizarUsuario(usuario);
        helper.cerrar();
        Toast.makeText(this,mensaje,Toast.LENGTH_SHORT).show();
        limpiarPosActualizacion();
    }

    private void limpiarCamposSinId(){
        edtNombreUsuarioModificar.setText("");
        edtClaveUsuarioModificar.setText("");
    }

    private void limpiarPosActualizacion(){
        edtNombreUsuarioModificar.setText("");
        edtClaveUsuarioModificar.setText("");
        edtIdUsuarioModificar.setText("");
    }
    private String valor(String texto) {

        if (texto == null) {

            return "";
        }

        return texto;
    }
}