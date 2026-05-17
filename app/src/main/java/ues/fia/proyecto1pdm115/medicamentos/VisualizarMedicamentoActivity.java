package ues.fia.proyecto1pdm115.medicamentos;

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
import ues.fia.proyecto1pdm115.modelos.Medicamento;

public class VisualizarMedicamentoActivity extends AppCompatActivity{
    TableLayout tablaMedicamentos;
    Button btnRegresarVisualizar;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_medicamentos);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        tablaMedicamentos = findViewById(R.id.tablaMedicamentos);

        btnRegresarVisualizar = findViewById(R.id.btnRegresarVisualizar);

        btnRegresarVisualizar.setOnClickListener(v -> finish());

        cargarMedicamentos();
    }

    private void cargarMedicamentos() {
        tablaMedicamentos.removeAllViews();

        helper.abrir();
        ArrayList<Medicamento> lista = helper.consultarTodosMedicamentos();
        helper.cerrar();

        if (lista.isEmpty()) {
            TableRow fila = new TableRow(this);

            TextView txtVacio = crearCelda("No hay medicamentos registrados", 315);
            txtVacio.setGravity(Gravity.CENTER);
            txtVacio.setPadding(16, 30, 16, 30);

            fila.addView(txtVacio);
            tablaMedicamentos.addView(fila);
            return;
        }

        int numero = 1;

        for (Medicamento medicamento : lista) {
            agregarFilaMedicamento(numero, medicamento);
            numero++;
        }
    }

    private void agregarFilaMedicamento(int numero, Medicamento medicamento) {
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
                        valor(
                                medicamento.getCodMedicamento()
                        ),
                        90
                )
        );

        fila.addView(
                crearCelda(
                        valor(
                                medicamento.getNombreMedicamento()
                        ),
                        120
                )
        );

        fila.addView(
                crearCelda(
                        "$" +
                                medicamento.getPrecioVenta(),
                        75
                )
        );

        fila.setOnClickListener(
                v -> mostrarModalMedicamento(
                        medicamento
                )
        );

        tablaMedicamentos.addView(fila);
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

    private void mostrarModalMedicamento(Medicamento medicamento) {

        String datos =
                "Código: " +
                        valor(
                                medicamento.getCodMedicamento()
                        ) + "\n\n" +

                        "Nombre: " +
                        valor(
                                medicamento.getNombreMedicamento()
                        ) + "\n\n" +

                        "Fecha vencimiento: " +
                        valor(
                                medicamento.getFechaVencimiento()
                        ) + "\n\n" +

                        "Inventario: " +
                        medicamento.getCantidadInventario() + "\n\n" +

                        "Precio: $" +
                        medicamento.getPrecioVenta() + "\n\n" +

                        "Lote: " +
                        valor(
                                medicamento.getLote()
                        );

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Datos del medicamento");
        builder.setMessage(datos);

        builder.setPositiveButton(
                "Editar",
                (dialog, which) -> {
                    Intent intent =
                            new Intent(
                                    this,
                                    ActualizarMedicamentoActivity.class
                            );

                    intent.putExtra(
                            "COD_MEDICAMENTO",
                            medicamento.getCodMedicamento()
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
                                    EliminarMedicamentoActivity.class
                            );

                    intent.putExtra(
                            "COD_MEDICAMENTO",
                            medicamento.getCodMedicamento()
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
