package ues.fia.proyecto1pdm115.hospitales;

import android.graphics.Color;
import android.os.Bundle;
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

public class VisualizarHospitalesActivity extends AppCompatActivity {
    TableLayout tableHospitales;

    Button btnRegresarVisualizarHospitales;

    controlDBHospitalApp helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_hospitales);

        Navegador.configurarBarra(this);
        tableHospitales = findViewById(R.id.tableHospitales);
        btnRegresarVisualizarHospitales=findViewById(R.id.btnRegresarVisualizarHospitales);
        helper=new controlDBHospitalApp(this);
        cargarHospitales();
        btnRegresarVisualizarHospitales.setOnClickListener(v -> finish());
    }

    private void cargarHospitales() {
        helper.abrir();
        ArrayList<HashMap<String, String>> datos = helper.consultarHospitalesDetalle();
        helper.cerrar();

        tableHospitales.removeAllViews();
        TableRow encabezado = new TableRow(this);
        encabezado.setBackgroundColor(Color.LTGRAY);
        encabezado.addView(crearCelda("Nombre Hospital"));
        encabezado.addView(crearCelda("Telefono Hospital"));
        encabezado.addView(crearCelda("Ubicacion Hospital"));
        encabezado.addView(crearCelda("Especialidades Hospital"));

        tableHospitales.addView(encabezado);

        for (HashMap<String, String> fila : datos) {
            TableRow tr = new TableRow(this);

            // Creamos celdas (TextViews)
            tr.addView(crearCelda(fila.get("hospital")));
            tr.addView(crearCelda(fila.get("telefono")));
            tr.addView(crearCelda(fila.get("ubicacion")));
            tr.addView(crearCelda(fila.get("especialidades")));

            tableHospitales.addView(tr);
        }
    }

    // Método auxiliar para no repetir código de diseño de celdas
    private TextView crearCelda(String texto) {
        TextView tv = new TextView(this);
        tv.setText(texto != null ? texto : "N/A"); // Evita el null
        tv.setPadding(10, 10, 10, 10);
        return tv;
    }
}