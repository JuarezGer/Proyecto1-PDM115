package ues.fia.proyecto1pdm115.permisos;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ues.fia.proyecto1pdm115.modelos.Opcion_crud;
import ues.fia.proyecto1pdm115.modelos.Usuario;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;

import ues.fia.proyecto1pdm115.R;

public class CrearPermisoActivity extends AppCompatActivity {

    Spinner spinnerUsuarios,spinnerPermisos;
    Button btnGuardarPermisoCrear,btnRegresarPermisoCrear;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_permiso);
        helper = new controlDBHospitalApp(this);

        spinnerUsuarios = findViewById(R.id.spinnerUsuariosCrear);
        spinnerPermisos = findViewById(R.id.spinnerPermisosCrear);
        btnGuardarPermisoCrear = findViewById(R.id.btnGuardarPermisoCrear);
        btnRegresarPermisoCrear=findViewById(R.id.btnRegresarPermisoCrear);

        cargarDatosSpinners();
        btnGuardarPermisoCrear.setOnClickListener(v -> guardarAsignacion());
        btnRegresarPermisoCrear.setOnClickListener(v -> finish());

    }

    private void cargarDatosSpinners(){
        helper.abrir();
        List<Usuario> usuarios = helper.consultarUsuarios();
        List<Opcion_crud> permisos = helper.consultarPermisos();

        usuarios.add(0, new Usuario("-1", "Seleccione un usuario..."));
        permisos.add(0, new Opcion_crud("-1", "Seleccione un permiso..."));

        ArrayAdapter<Usuario> adapterUser = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,usuarios);
        adapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUsuarios.setAdapter(adapterUser);

        ArrayAdapter<Opcion_crud> adapterPerm = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,permisos);
        adapterPerm.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPermisos.setAdapter(adapterPerm);
        helper.cerrar();
    }

    private void guardarAsignacion(){
        Usuario usuarioSel = (Usuario) spinnerUsuarios.getSelectedItem();
        Opcion_crud permisoSel = (Opcion_crud) spinnerPermisos.getSelectedItem();

        if(usuarioSel!=null && permisoSel !=null){
            String idUsuario = usuarioSel.getIdUsuario();
            String idPermiso = permisoSel.getIdopcion();

            helper.abrir();

            String mensaje = helper.insertarPermiso(idUsuario,idPermiso);
            helper.cerrar();

            Toast.makeText(
                    this,
                    mensaje,
                    Toast.LENGTH_LONG
            ).show();
        }
    }
}