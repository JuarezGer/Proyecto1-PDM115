package ues.fia.proyecto1pdm115.detallesRecetas;

import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.DetalleReceta;

public class ActualizarDetalleRecetaActivity
        extends AppCompatActivity {

    EditText edtIdDetalleModificar,
            edtCantidadModificar,
            edtInstruccionesModificar,
            edtPrecioModificar,
            edtSubtotalModificar;

    Button btnBuscarDetalle,
            btnActualizarDetalle,
            btnRegresarModificar;

    controlDBHospitalApp helper;

    DetalleReceta detalleActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_modificar_detalle_receta
        );

        Navegador.configurarBarra(this);

        helper =
                new controlDBHospitalApp(this);

        edtIdDetalleModificar =
                findViewById(
                        R.id.edtIdDetalleModificar
                );

        edtCantidadModificar =
                findViewById(
                        R.id.edtCantidadModificar
                );

        edtInstruccionesModificar =
                findViewById(
                        R.id.edtInstruccionesModificar
                );

        edtPrecioModificar =
                findViewById(
                        R.id.edtPrecioModificar
                );

        edtSubtotalModificar =
                findViewById(
                        R.id.edtSubtotalModificar
                );

        btnBuscarDetalle =
                findViewById(
                        R.id.btnBuscarDetalle
                );

        btnActualizarDetalle =
                findViewById(
                        R.id.btnActualizarDetalle
                );

        btnRegresarModificar =
                findViewById(
                        R.id.btnRegresarModificar
                );

        btnBuscarDetalle.setOnClickListener(
                v -> buscarDetalle()
        );

        btnActualizarDetalle.setOnClickListener(
                v -> actualizarDetalle()
        );

        btnRegresarModificar.setOnClickListener(
                v -> finish()
        );

        int idDetalle =
                getIntent().getIntExtra(
                        "ID_DETALLE_RECETA",
                        -1
                );

        if (idDetalle != -1) {

            edtIdDetalleModificar.setText(
                    String.valueOf(idDetalle)
            );

            buscarDetalle();
        }
    }

    private void buscarDetalle() {

        String idTexto =
                edtIdDetalleModificar
                        .getText()
                        .toString()
                        .trim();

        if (idTexto.isEmpty()) {

            Toast.makeText(
                    this,
                    "Ingrese el ID",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        helper.abrir();

        detalleActual =
                helper.consultarDetalleReceta(
                        Integer.parseInt(idTexto)
                );

        helper.cerrar();

        if (detalleActual != null) {

            edtCantidadModificar.setText(
                    String.valueOf(
                            detalleActual.getCantidad()
                    )
            );

            edtInstruccionesModificar.setText(
                    detalleActual.getInstrucciones()
            );

            edtPrecioModificar.setText(
                    String.valueOf(
                            detalleActual
                                    .getPrecioUnitarioHistorico()
                    )
            );

            edtSubtotalModificar.setText(
                    String.valueOf(
                            detalleActual
                                    .getSubTotalItem()
                    )
            );

            edtIdDetalleModificar.setEnabled(false);

            Toast.makeText(
                    this,
                    "Detalle encontrado",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            edtIdDetalleModificar.setEnabled(true);

            Toast.makeText(
                    this,
                    "Detalle no encontrado",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void actualizarDetalle() {

        if (detalleActual == null) {

            Toast.makeText(
                    this,
                    "Busque un detalle primero",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        if (
                edtCantidadModificar
                        .getText()
                        .toString()
                        .trim()
                        .isEmpty()

                        || edtInstruccionesModificar
                        .getText()
                        .toString()
                        .trim()
                        .isEmpty()

                        || edtPrecioModificar
                        .getText()
                        .toString()
                        .trim()
                        .isEmpty()
        ) {

            Toast.makeText(
                    this,
                    "Complete todos los campos",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        int cantidad = Integer.parseInt(edtCantidadModificar.getText().toString().trim());
        double precio = Double.parseDouble(edtPrecioModificar.getText().toString().trim());

        if (cantidad <= 0 || precio <= 0) {
            Toast.makeText(this,
                    "Cantidad y precio deben ser mayores a 0",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double subtotal = cantidad * precio;

        detalleActual.setCantidad(cantidad);
        detalleActual.setInstrucciones(edtInstruccionesModificar.getText().toString().trim());
        detalleActual.setPrecioUnitarioHistorico(precio);
        detalleActual.setSubTotalItem(subtotal);

        helper.abrir();

        String mensaje =
                helper.actualizarDetalleReceta(
                        detalleActual
                );

        helper.cerrar();

        Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_LONG
        ).show();
    }
}
