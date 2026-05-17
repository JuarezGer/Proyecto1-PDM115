package ues.fia.proyecto1pdm115.recetas;

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
import ues.fia.proyecto1pdm115.detallesRecetas.VisualizarDetalleRecetaActivity;
import ues.fia.proyecto1pdm115.modelos.Receta;

public class VisualizarRecetaActivity extends AppCompatActivity {
    TableLayout tablaRecetas;
    Button btnRegresarVisualizar;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_visualizar_recetas
        );

        Navegador.configurarBarra(this);

        helper =
                new controlDBHospitalApp(this);

        tablaRecetas =
                findViewById(
                        R.id.tablaRecetas
                );

        btnRegresarVisualizar =
                findViewById(
                        R.id.btnRegresarVisualizar
                );

        btnRegresarVisualizar
                .setOnClickListener(
                        v -> finish()
                );

        cargarRecetas();
    }

    private void cargarRecetas() {

        tablaRecetas.removeAllViews();

        helper.abrir();

        ArrayList<Receta> lista =
                helper.consultarTodasRecetas();

        helper.cerrar();

        if (lista.isEmpty()) {

            TableRow fila =
                    new TableRow(this);

            TextView txtVacio =
                    crearCelda(
                            "No hay recetas registradas",
                            315
                    );

            txtVacio.setGravity(
                    Gravity.CENTER
            );

            txtVacio.setPadding(
                    16,
                    30,
                    16,
                    30
            );

            fila.addView(txtVacio);

            tablaRecetas.addView(fila);

            return;
        }

        int numero = 1;

        for (Receta receta : lista) {

            agregarFilaReceta(
                    numero,
                    receta
            );

            numero++;
        }
    }

    private void agregarFilaReceta(
            int numero,
            Receta receta
    ) {

        TableRow fila =
                new TableRow(this);

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
                                receta.getIdReceta()
                        ),
                        60
                )
        );

        fila.addView(
                crearCelda(
                        String.valueOf(
                                receta.getIdConsulta()
                        ),
                        90
                )
        );

        fila.addView(
                crearCelda(
                        valor(
                                receta.getFechaEmision()
                        ),
                        115
                )
        );

        fila.setOnClickListener(
                v -> mostrarModalReceta(
                        receta
                )
        );

        tablaRecetas.addView(fila);
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

    private void mostrarModalReceta(
            Receta receta
    ) {

        String datos =
                "ID Receta: "
                        + receta.getIdReceta()
                        + "\n\n" +

                        "ID Consulta: "
                        + receta.getIdConsulta()
                        + "\n\n" +

                        "Fecha emisión: "
                        + valor(
                        receta.getFechaEmision()
                );

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle(
                "Datos de la receta"
        );

        builder.setMessage(datos);

        builder.setPositiveButton(
                "Ver detalles",
                (dialog, which) -> {

                    Intent intent =
                            new Intent(
                                    this,
                                    VisualizarDetalleRecetaActivity.class
                            );

                    intent.putExtra(
                            "ID_RECETA",
                            receta.getIdReceta()
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
                                    EliminarRecetaActivity.class
                            );

                    intent.putExtra(
                            "ID_RECETA",
                            receta.getIdReceta()
                    );

                    startActivity(intent);
                }
        );

        builder.setNeutralButton(
                "Editar",
                (dialog, which) -> {

                    Intent intent =
                            new Intent(
                                    this,
                                    ActualizarRecetaActivity.class
                            );

                    intent.putExtra(
                            "ID_RECETA",
                            receta.getIdReceta()
                    );

                    startActivity(intent);
                }
        );

        AlertDialog dialog =
                builder.create();

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
