package ues.fia.proyecto1pdm115.doctores;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;

public class VisualizarDoctorActivity extends AppCompatActivity {
    TableLayout tableDoctores;

    Button btnRegresarVisualizarDoctor;

    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_visualizar_doctor);

        Navegador.configurarBarra(this);
        tableDoctores=findViewById(R.id.tableDoctores);
        btnRegresarVisualizarDoctor=findViewById(R.id.btnRegresarVisualizarDoctor);
        helper=new controlDBHospitalApp(this);
        cargarDoctores();
        btnRegresarVisualizarDoctor.setOnClickListener(v -> finish());

    }

    private void cargarDoctores() {
        helper.abrir();
        ArrayList<HashMap<String, String>> datos = helper.consultarDoctoresDetalle();
        helper.cerrar();

        tableDoctores.removeAllViews();
        TableRow encabezado = new TableRow(this);
        encabezado.setBackgroundColor(Color.LTGRAY);
        encabezado.addView(crearCelda("DUI Doctor"));
        encabezado.addView(crearCelda("Nombre Doctor"));
        encabezado.addView(crearCelda("Apellido Doctor"));
        encabezado.addView(crearCelda("Datos Hospital Asignado"));
        encabezado.addView(crearCelda("Datos Usuario Asignado"));
        encabezado.addView(crearCelda("Especialidades De Doctor"));

        tableDoctores.addView(encabezado);

        for (HashMap<String, String> fila : datos) {
            TableRow tr = new TableRow(this);

            // Creamos celdas (TextViews)
            tr.addView(crearCelda(fila.get("dui")));
            tr.addView(crearCelda(fila.get("nombre")));
            tr.addView(crearCelda(fila.get("apellido")));
            tr.addView(crearCelda(fila.get("hospital")));
            tr.addView(crearCelda(fila.get("usuario")));
            tr.addView(crearCelda(fila.get("especialidades")));


            tableDoctores.addView(tr);
        }
    }
    private TextView crearCelda(String texto) {
        TextView tv = new TextView(this);
        tv.setText(texto != null ? texto : "N/A"); // Evita el null
        tv.setPadding(30, 40, 30, 40);
        tv.setGravity(Gravity.CENTER_VERTICAL);

        // 3. Altura mínima (Opcional, asegura que todas midan al menos X píxeles)
        tv.setMinHeight(120);
        tv.setBackgroundResource(android.R.drawable.edit_text);
        tv.setEnabled(false);
        return tv;
    }
}