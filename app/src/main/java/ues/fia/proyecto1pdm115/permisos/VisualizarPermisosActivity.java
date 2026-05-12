package ues.fia.proyecto1pdm115.permisos;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Puede_elegir;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.modelos.Usuario;

public class VisualizarPermisosActivity extends AppCompatActivity {
    Spinner spinnerUsuarios;
    Button btnRegresarVisualizarPermisos,btnBuscarPermisosVisualizar;
    TableLayout tablePermisos;
    controlDBHospitalApp helper;

    @Override
    public void onCreate(Bundle saveInstanceState){
        super.onCreate(saveInstanceState);
        setContentView(R.layout.activity_visualizar_permisos);
        tablePermisos=findViewById(R.id.tablePermisos);
        helper=new controlDBHospitalApp(this);

        spinnerUsuarios=findViewById(R.id.spinnerUsuariosVisualizar);
        btnBuscarPermisosVisualizar=findViewById(R.id.btnBuscarPermisosVisualizar);
        btnRegresarVisualizarPermisos=findViewById(R.id.btnRegresarVisualizarPermisos);
        cargarDatosSpinners();
        btnBuscarPermisosVisualizar.setOnClickListener(v -> buscarPermisos());
        btnRegresarVisualizarPermisos.setOnClickListener(v -> finish());
    }
    private void cargarDatosSpinners(){
        helper.abrir();
        List<Usuario> usuarios = helper.consultarUsuarios();

        usuarios.add(0, new Usuario("-1", "Seleccione un usuario..."));

        ArrayAdapter<Usuario> adapterUser = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,usuarios);
        adapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUsuarios.setAdapter(adapterUser);
        helper.cerrar();
    }

    private void buscarPermisos(){
        tablePermisos.removeAllViews();
        Usuario usuarioSel = (Usuario) spinnerUsuarios.getSelectedItem();

        if (usuarioSel!=null && !usuarioSel.getIdUsuario().equals("-1")){
            String idUsuario = usuarioSel.getIdUsuario();

            helper.abrir();
            ArrayList<Puede_elegir> lista = helper.consultarPermisos(idUsuario);
            helper.cerrar();
            if(lista.isEmpty()){
                Toast.makeText(this,"El usuario aun no cuenta con permisos asignados",Toast.LENGTH_LONG).show();
                return;
            }
            //FILA ENCABEZADO
            TableRow encabezado = new TableRow(this);
            TextView txtIdHeader = new TextView(this);
            txtIdHeader.setText("ID_USUARIO");
            txtIdHeader.setPadding(20,20,20,20);

            txtIdHeader.setTypeface(null, Typeface.BOLD);
            TextView txtOpcHeader = new TextView(this);
            txtOpcHeader.setText("Permiso asignado");
            txtOpcHeader.setPadding(20,20,20,20);
            txtOpcHeader.setTypeface(null, Typeface.BOLD);

            encabezado.addView(txtIdHeader);
            encabezado.addView(txtOpcHeader);

            tablePermisos.addView(encabezado);

            //FILAS DE DATOS
            for (Puede_elegir puedeElegir:lista){
                TableRow fila = new TableRow(this);
                TextView txtId = new TextView(this);
                txtId.setText(
                        String.valueOf(
                                puedeElegir.getId_usuario()
                        )
                );
                txtId.setPadding(20,20,20,20);


                TextView txtOpc = new TextView(this);
                txtOpc.setText(
                        puedeElegir.getId_opcion()
                );
                txtOpc.setPadding(20,20,20,20);



                fila.addView(txtId);
                fila.addView(txtOpc);

                tablePermisos.addView(fila);
            }
        }else {
            Toast.makeText(this,"Por favor, seleccione un usuario",Toast.LENGTH_LONG).show();
        }
    }

}