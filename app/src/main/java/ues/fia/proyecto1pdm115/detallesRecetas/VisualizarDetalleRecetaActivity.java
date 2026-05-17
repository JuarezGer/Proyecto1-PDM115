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

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.medicamentos.ActualizarMedicamentoActivity;
import ues.fia.proyecto1pdm115.medicamentos.EliminarMedicamentoActivity;
import ues.fia.proyecto1pdm115.modelos.DetalleReceta;
import ues.fia.proyecto1pdm115.modelos.Medicamento;

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

        idReceta = getIntent().getIntExtra("ID_RECETA", -1);

        btnRegresarVisualizar.setOnClickListener(v -> finish());

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
            tv.setText("No hay detalles en esta receta");
            tv.setGravity(Gravity.CENTER);
            tv.setPadding(20, 40, 20, 40);

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
        fila.addView(crearCelda(String.valueOf(numero), 45));

        // ID detalle
        fila.addView(crearCelda(
                String.valueOf(detalle.getIdDetalleReceta()),
                70
        ));

        // Medicamento (viene en instrucciones modificado)
        fila.addView(crearCelda(
                valor(detalle.getInstrucciones()),
                180
        ));

        // Cantidad
        fila.addView(crearCelda(
                String.valueOf(detalle.getCantidad()),
                70
        ));

        // Subtotal
        fila.addView(crearCelda(
                "$" + detalle.getSubTotalItem(),
                90
        ));

        // Click → modal
        fila.setOnClickListener(v ->
                mostrarModalDetalle(detalle)
        );

        tablaDetallesData.addView(fila);
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

    private void mostrarModalDetalle(DetalleReceta detalle) {

        String datos =
                "ID Detalle: " + detalle.getIdDetalleReceta() + "\n\n" +
                        "Cantidad: " + detalle.getCantidad() + "\n\n" +
                        "Instrucciones: " + valor(detalle.getInstrucciones()) + "\n\n" +
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
