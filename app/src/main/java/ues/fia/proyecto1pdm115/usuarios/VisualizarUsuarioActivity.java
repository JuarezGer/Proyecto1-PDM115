package ues.fia.proyecto1pdm115.usuarios;

import android.graphics.Typeface;
import android.icu.text.RelativeDateTimeFormatter;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Especialidad;
import ues.fia.proyecto1pdm115.modelos.Usuario;

public class VisualizarUsuarioActivity extends AppCompatActivity {
    TableLayout tableUsuarios;

    Button btnRegresarVisualizarUsuarios;

    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_visualizar_usuario
        );

        tableUsuarios = findViewById(R.id.tableUsuarios);

        btnRegresarVisualizarUsuarios = findViewById(R.id.btnRegresarVisualizarUsuarios);
        helper = new controlDBHospitalApp(this);
        cargarUsuarios();
        btnRegresarVisualizarUsuarios.setOnClickListener(v -> finish());

    }
    private void cargarUsuarios(){
        helper.abrir();
        ArrayList<Usuario> lista = helper.consultarUsuarios();

        helper.cerrar();

        //FILA ENCABEZADO
        TableRow encabezado = new TableRow(this);
        TextView txtIdHeader = new TextView(this);
        txtIdHeader.setText("ID");
        txtIdHeader.setPadding(20,20,20,20);

        txtIdHeader.setTypeface(null,Typeface.BOLD);
        TextView txtNombreHeader = new TextView(this);
        txtNombreHeader.setText("Nombre de usuario");
        txtNombreHeader.setPadding(20,20,20,20);
        txtNombreHeader.setTypeface(null, Typeface.BOLD);

        encabezado.addView(txtIdHeader);
        encabezado.addView(txtNombreHeader);

        tableUsuarios.addView(encabezado);

        //FILAS DE DATOS
        for (Usuario usuario:lista){
            TableRow fila = new TableRow(this);
            TextView txtId = new TextView(this);
            txtId.setText(
                    String.valueOf(
                            usuario.getIdUsuario()
                    )
            );
            txtId.setPadding(20,20,20,20);


            TextView txtNombre = new TextView(this);
            txtNombre.setText(
                    usuario.getNombreUsuario()
            );
            txtNombre.setPadding(20,20,20,20);



            fila.addView(txtId);
            fila.addView(txtNombre);

            tableUsuarios.addView(fila);
        }

    }

}