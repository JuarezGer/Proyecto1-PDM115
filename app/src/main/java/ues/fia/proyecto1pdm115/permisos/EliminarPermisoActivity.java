package ues.fia.proyecto1pdm115.permisos;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Puede_elegir;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.modelos.Usuario;

import ues.fia.proyecto1pdm115.R;

public class EliminarPermisoActivity extends AppCompatActivity {

    Spinner spinnerUsuariosEliminar, spinnerPermisosEliminar;
    Button btnRegresarEliminarPermisos,btnBuscarPermisosEliminar,btnEliminarPermiso;
    controlDBHospitalApp helper;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_permiso);
        helper = new controlDBHospitalApp(this);

        spinnerUsuariosEliminar = findViewById(R.id.spinnerUsuariosEliminar);
        spinnerPermisosEliminar = findViewById(R.id.spinnerPermisosEliminar);
        btnRegresarEliminarPermisos = findViewById(R.id.btnRegresarEliminarPermisos);
        btnBuscarPermisosEliminar=findViewById(R.id.btnBuscarPermisosEliminar);
        btnEliminarPermiso=findViewById(R.id.btnEliminarPermiso);

        cargarDatosSpinners();
        btnRegresarEliminarPermisos.setOnClickListener(v -> finish());
        btnBuscarPermisosEliminar.setOnClickListener(v -> buscarPermisos());
        btnEliminarPermiso.setOnClickListener(v -> confirmarEliminacion());
    }
    private void cargarDatosSpinners(){
        helper.abrir();
        List<Usuario> usuarios = helper.consultarUsuarios();

        usuarios.add(0, new Usuario("-1", "Seleccione un usuario..."));

        ArrayAdapter<Usuario> adapterUser = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,usuarios);
        adapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUsuariosEliminar.setAdapter(adapterUser);
        helper.cerrar();
    }

    private void confirmarEliminacion() {
        Usuario usuarioSel = (Usuario) spinnerUsuariosEliminar.getSelectedItem();
        Object item = spinnerPermisosEliminar.getSelectedItem();

        if (item==null){
            Toast.makeText(this,"Primero busque permisos",Toast.LENGTH_SHORT).show();
            return;
        }

        String permiso = item.toString();
        if (usuarioSel==null || usuarioSel.getIdUsuario().equals("-1")|| permiso.equals("-1")) {

            Toast.makeText(
                    this,
                    "Primero busque un usuario válido y su permiso",
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
                "¿Desea eliminar este permiso?"
        );

        builder.setPositiveButton(
                "Sí, eliminar",
                (dialog, which) -> eliminarPermiso(usuarioSel.getIdUsuario(),permiso)
        );

        builder.setNegativeButton(
                "Cancelar",
                (dialog, which) -> dialog.dismiss()
        );

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void eliminarPermiso(String idUsuario, String idOpc) {

        helper.abrir();

        String mensaje =
                helper.eliminarPermiso(
                        idUsuario,idOpc
                );

        helper.cerrar();

        Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_LONG
        ).show();

        buscarPermisos();
    }
    private void buscarPermisos(){
        Usuario usuarioSel = (Usuario) spinnerUsuariosEliminar.getSelectedItem();

        if (usuarioSel!=null && !usuarioSel.getIdUsuario().equals("-1")){
            String idUsuario = usuarioSel.getIdUsuario();

            helper.abrir();
            ArrayList<Puede_elegir> lista = helper.consultarPermisos(idUsuario);
            helper.cerrar();
            if(lista.isEmpty()){
                spinnerPermisosEliminar.setAdapter(null);
                Toast.makeText(this,"El usuario aun no cuenta con permisos asignados",Toast.LENGTH_LONG).show();
                return;
            }
            //Obtener solo permisos
            ArrayList<String> permisoSpinner = new ArrayList<>();
            for(Puede_elegir permiso:lista){
                permisoSpinner.add(permiso.getId_opcion());
            }

            permisoSpinner.add(0,new String("-1"));
            ArrayAdapter<String> permisosAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,permisoSpinner);
            permisosAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerPermisosEliminar.setAdapter(permisosAdapter);
        }
    }
}