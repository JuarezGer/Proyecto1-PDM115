package ues.fia.proyecto1pdm115.especialidades;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Especialidad;

public class VisualizarEspecialidadesActivity extends AppCompatActivity {

    TableLayout tableEspecialidades;

    Button btnRegresarVisualizarEspecialidad;

    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(
                R.layout.activity_visualizar_especialidades
        );

        tableEspecialidades =
                findViewById(R.id.tableEspecialidades);

        btnRegresarVisualizarEspecialidad =
                findViewById(
                        R.id.btnRegresarVisualizarEspecialidad
                );

        helper = new controlDBHospitalApp(this);

        cargarEspecialidades();

        btnRegresarVisualizarEspecialidad
                .setOnClickListener(v -> finish());
    }

    private void cargarEspecialidades() {

        helper.abrir();

        ArrayList<Especialidad> lista =
                helper.consultarEspecialidades();

        helper.cerrar();

        // FILA ENCABEZADO
        TableRow encabezado = new TableRow(this);

        TextView txtIdHeader = new TextView(this);
        txtIdHeader.setText("ID");
        txtIdHeader.setPadding(20,20,20,20);

        txtIdHeader.setTypeface(null, Typeface.BOLD);

        TextView txtNombreHeader = new TextView(this);
        txtNombreHeader.setText("ESPECIALIDAD");
        txtNombreHeader.setPadding(20,20,20,20);

        txtNombreHeader.setTypeface(null, Typeface.BOLD);

        encabezado.addView(txtIdHeader);
        encabezado.addView(txtNombreHeader);

        tableEspecialidades.addView(encabezado);

        // FILAS DATOS
        for (Especialidad especialidad : lista) {

            TableRow fila = new TableRow(this);

            TextView txtId = new TextView(this);
            txtId.setText(
                    String.valueOf(
                            especialidad.getIdEspecialidad()
                    )
            );
            txtId.setPadding(20,20,20,20);


            TextView txtNombre = new TextView(this);
            txtNombre.setText(
                    especialidad.getNombreEspecialidad()
            );
            txtNombre.setPadding(20,20,20,20);



            fila.addView(txtId);
            fila.addView(txtNombre);

            tableEspecialidades.addView(fila);
        }
    }
}