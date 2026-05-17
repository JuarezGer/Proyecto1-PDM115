package ues.fia.proyecto1pdm115.hospitalizaciones;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Hospitalizacion;

public class VisualizarHospitalizacionActivity extends AppCompatActivity{
    TableLayout tablaHospitalizaciones;
    Button btnRegresarVisualizar;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_hospitalizacion);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        tablaHospitalizaciones = findViewById(R.id.tablaHospitalizaciones);

        btnRegresarVisualizar = findViewById(R.id.btnRegresarVisualizar);

        btnRegresarVisualizar.setOnClickListener(v -> finish());

        cargarHospitalizaciones();
    }

    private void cargarHospitalizaciones() {
        tablaHospitalizaciones.removeAllViews();

        helper.abrir();
        ArrayList<Hospitalizacion> lista = helper.consultarTodasHospitalizaciones();
        helper.cerrar();

        if (lista.isEmpty()) {
            TableRow fila = new TableRow(this);

            TextView txtVacio = crearCelda("No hay hospitalizaciones registradas", 315);
            txtVacio.setGravity(Gravity.CENTER);
            txtVacio.setPadding(16, 30, 16, 30);

            fila.addView(txtVacio);
            tablaHospitalizaciones.addView(fila);
            return;
        }

        int numero = 1;

        for (Hospitalizacion hospitalizacion : lista) {
            agregarFilaHospitalizacion(numero, hospitalizacion);
            numero++;
        }
    }

    private void agregarFilaHospitalizacion(int numero, Hospitalizacion hospitalizacion) {
        TableRow fila = new TableRow(this);
        fila.setClickable(true);
        fila.setFocusable(true);
        fila.setPadding(0, 4, 0, 4);

        fila.addView(
                crearCelda(
                        String.valueOf(numero),
                        45
                )
        );

        fila.addView(
                crearCelda(
                        String.valueOf(
                                hospitalizacion
                                        .getIdHospitalizacion()
                        ),
                        60
                )
        );

        fila.addView(
                crearCelda(
                        valor(
                                hospitalizacion
                                        .getMotivoIngreso()
                        ),
                        145
                )
        );

        fila.addView(
                crearCelda(
                        "$" +
                                hospitalizacion
                                        .getCostoHospitalizacion(),
                        80
                )
        );

        fila.setOnClickListener(
                v -> mostrarModalHospitalizacion(
                        hospitalizacion
                )
        );

        tablaHospitalizaciones.addView(fila);
    }

    private TextView crearCelda(String texto, int anchoDp) {
        TextView textView = new TextView(this);

        int anchoPx = convertirDpAPx(anchoDp);

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                anchoPx,
                TableRow.LayoutParams.WRAP_CONTENT
        );

        textView.setLayoutParams(params);

        textView.setText(texto);
        textView.setTextColor(getResources().getColor(R.color.text_black));
        textView.setTextSize(13);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(6, 14, 6, 14);

        return textView;
    }

    private void mostrarModalHospitalizacion(Hospitalizacion hospitalizacion) {

        String datos =
                "ID: " +
                        hospitalizacion
                                .getIdHospitalizacion()
                        + "\n\n" +

                        "ID Consulta: " +
                        hospitalizacion
                                .getIdConsulta()
                        + "\n\n" +

                        "Fecha inicio: " +
                        valor(
                                hospitalizacion
                                        .getFechaInicioHosp()
                        )
                        + "\n\n" +

                        "Fecha fin: " +
                        valor(
                                hospitalizacion
                                        .getFechaFinHosp()
                        )
                        + "\n\n" +

                        "Motivo ingreso: " +
                        valor(
                                hospitalizacion
                                        .getMotivoIngreso()
                        )
                        + "\n\n" +

                        "Costo: $" +
                        hospitalizacion
                                .getCostoHospitalizacion();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Datos de la hospitalización");
        builder.setMessage(datos);

        builder.setPositiveButton(
                "Editar",
                (dialog, which) -> {

                    Intent intent =
                            new Intent(
                                    this,
                                    ActualizarHospitalizacionActivity.class
                            );

                    intent.putExtra(
                            "ID_HOSPITALIZACION",
                            hospitalizacion
                                    .getIdHospitalizacion()
                    );

                    startActivity(intent);
                }
        );

        builder.setNegativeButton(
                "Eliminar",
                (dialog, which) -> {

                    Intent intent =
                            new Intent(
                                    this,
                                    EliminarHospitalizacionActivity.class
                            );

                    intent.putExtra(
                            "ID_HOSPITALIZACION",
                            hospitalizacion
                                    .getIdHospitalizacion()
                    );

                    startActivity(intent);
                }
        );

        builder.setNeutralButton(
                "Cerrar",
                (dialog, which) ->
                        dialog.dismiss()
        );

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String valor(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return "N/A";
        }

        return texto;
    }

    private int convertirDpAPx(int dp) {
        float escala = getResources().getDisplayMetrics().density;
        return (int) (dp * escala + 0.5f);
    }
}
