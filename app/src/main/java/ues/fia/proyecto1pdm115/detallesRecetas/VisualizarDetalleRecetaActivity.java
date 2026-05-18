package ues.fia.proyecto1pdm115.detallesRecetas;

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

import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.DetalleReceta;

public class VisualizarDetalleRecetaActivity extends AppCompatActivity {

    TableLayout tablaDetallesData;
    Button btnRegresarVisualizar;
    controlDBHospitalApp helper;

    int idReceta;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_detalle_receta);

        helper = new controlDBHospitalApp(this);

        tablaDetallesData = findViewById(R.id.tablaDetallesData);
        btnRegresarVisualizar = findViewById(R.id.btnRegresarVisualizar);

        // Si viene desde una receta específica, se usa ese ID.
        // Si no viene ningún ID, se mostrará todo.
        idReceta = getIntent().getIntExtra("ID_RECETA", 0);

        btnRegresarVisualizar.setOnClickListener(v -> finish());
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarDetalles();
    }

    private void cargarDetalles() {

        tablaDetallesData.removeAllViews();

        helper.abrir();

        ArrayList<DetalleReceta> lista =
                helper.consultarDetallesPorReceta(idReceta);

        helper.cerrar();

        if (lista.isEmpty()) {

            TextView tv = new TextView(this);
            tv.setText("No hay detalles de receta registrados");
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(20, 40, 20, 40);
            tv.setTextSize(15);
            tv.setTextColor(getResources().getColor(R.color.text_black));

            tablaDetallesData.addView(tv);
            return;
        }

        int numero = 1;

        for (DetalleReceta detalle : lista) {
            agregarFilaDetalle(numero, detalle);
            numero++;
        }
    }

    private void agregarFilaDetalle(int numero, DetalleReceta detalle) {

        TableRow fila = new TableRow(this);
        fila.setPadding(0, 6, 0, 6);

        // N°
        fila.addView(crearCelda(String.valueOf(numero), 0.8f));

        // Medicamento
        fila.addView(crearCelda(obtenerMedicamento(detalle.getInstrucciones()), 2.8f));

        // Cantidad
        fila.addView(crearCelda(String.valueOf(detalle.getCantidad()), 1f));

        // Subtotal
        fila.addView(crearCelda("$" + detalle.getSubTotalItem(), 1.4f));

        fila.setOnClickListener(v -> mostrarModalDetalle(detalle));

        tablaDetallesData.addView(fila);
    }

    private TextView crearCelda(String texto, float peso) {

        TextView textView = new TextView(this);

        TableRow.LayoutParams params = new TableRow.LayoutParams(
                0,
                TableRow.LayoutParams.WRAP_CONTENT,
                peso
        );

        textView.setLayoutParams(params);
        textView.setText(valor(texto));
        textView.setTextColor(getResources().getColor(R.color.text_black));
        textView.setTextSize(13);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding(6, 14, 6, 14);

        return textView;
    }

    private void mostrarModalDetalle(DetalleReceta detalle) {

        String datos =
                "ID Detalle: " + detalle.getIdDetalleReceta() + "\n\n" +
                        "Receta: #" + detalle.getIdReceta() + "\n\n" +
                        "Medicamento: " + obtenerMedicamento(detalle.getInstrucciones()) + "\n\n" +
                        "Cantidad: " + detalle.getCantidad() + "\n\n" +
                        "Instrucciones: " + obtenerInstrucciones(detalle.getInstrucciones()) + "\n\n" +
                        "Precio unitario: $" + detalle.getPrecioUnitarioHistorico() + "\n\n" +
                        "Subtotal: $" + detalle.getSubTotalItem();

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("Detalle de receta");
        builder.setMessage(datos);

        builder.setPositiveButton("Editar", (dialog, which) -> {

            Intent intent =
                    new Intent(
                            this,
                            ActualizarDetalleRecetaActivity.class
                    );

            intent.putExtra(
                    "ID_DETALLE_RECETA",
                    detalle.getIdDetalleReceta()
            );

            startActivity(intent);
        });

        builder.setNegativeButton("Eliminar", (dialog, which) -> {

            Intent intent =
                    new Intent(
                            this,
                            EliminarDetalleRecetaActivity.class
                    );

            intent.putExtra(
                    "ID_DETALLE_RECETA",
                    detalle.getIdDetalleReceta()
            );

            startActivity(intent);
        });

        builder.setNeutralButton("Cerrar",
                (dialog, which) -> dialog.dismiss()
        );

        builder.show();
    }

    private String obtenerMedicamento(String texto) {

        if (texto == null || texto.trim().isEmpty()) {
            return "N/A";
        }

        if (texto.contains(" - ")) {
            return texto.split(" - ", 2)[0];
        }

        return texto;
    }

    private String obtenerInstrucciones(String texto) {

        if (texto == null || texto.trim().isEmpty()) {
            return "N/A";
        }

        if (texto.contains(" - ")) {
            return texto.split(" - ", 2)[1];
        }

        return texto;
    }

    private String valor(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return "N/A";
        }

        return texto;
    }
}