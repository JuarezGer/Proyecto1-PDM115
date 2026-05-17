package ues.fia.proyecto1pdm115.detallesRecetas;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.DetalleReceta;

public class EliminarDetalleRecetaActivity
        extends AppCompatActivity {

    EditText edtIdDetalleEliminar;

    TextView txtIdRecetaDetalle,
            txtCantidadDetalle,
            txtInstruccionesDetalle,
            txtPrecioDetalle,
            txtSubtotalDetalle;

    Button btnBuscarDetalleEliminar,
            btnEliminarDetalle,
            btnRegresarEliminar;

    controlDBHospitalApp helper;

    DetalleReceta detalleActual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_eliminar_detalle_receta
        );

        Navegador.configurarBarra(this);

        helper =
                new controlDBHospitalApp(this);

        edtIdDetalleEliminar =
                findViewById(
                        R.id.edtIdDetalleEliminar
                );

        txtIdRecetaDetalle =
                findViewById(
                        R.id.txtIdRecetaDetalle
                );

        txtCantidadDetalle =
                findViewById(
                        R.id.txtCantidadDetalle
                );

        txtInstruccionesDetalle =
                findViewById(
                        R.id.txtInstruccionesDetalle
                );

        txtPrecioDetalle =
                findViewById(
                        R.id.txtPrecioDetalle
                );

        txtSubtotalDetalle =
                findViewById(
                        R.id.txtSubtotalDetalle
                );

        btnBuscarDetalleEliminar =
                findViewById(
                        R.id.btnBuscarDetalleEliminar
                );

        btnEliminarDetalle =
                findViewById(
                        R.id.btnEliminarDetalle
                );

        btnRegresarEliminar =
                findViewById(
                        R.id.btnRegresarEliminar
                );

        btnBuscarDetalleEliminar.setOnClickListener(
                v -> buscarDetalle()
        );

        btnEliminarDetalle.setOnClickListener(
                v -> eliminarDetalle()
        );

        btnRegresarEliminar.setOnClickListener(
                v -> finish()
        );

        int idDetalle =
                getIntent().getIntExtra(
                        "ID_DETALLE_RECETA",
                        -1
                );

        if (idDetalle != -1) {

            edtIdDetalleEliminar.setText(
                    String.valueOf(idDetalle)
            );

            buscarDetalle();
        }
    }

    private void buscarDetalle() {

        String idTexto =
                edtIdDetalleEliminar
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

            txtIdRecetaDetalle.setText(
                    "ID Receta: "
                            + detalleActual.getIdReceta()
            );

            txtCantidadDetalle.setText(
                    "Cantidad: "
                            + detalleActual.getCantidad()
            );

            txtInstruccionesDetalle.setText(
                    "Instrucciones: "
                            + detalleActual.getInstrucciones()
            );

            txtPrecioDetalle.setText(
                    "Precio: "
                            + detalleActual
                            .getPrecioUnitarioHistorico()
            );

            txtSubtotalDetalle.setText(
                    "Subtotal: "
                            + detalleActual
                            .getSubTotalItem()
            );

            Toast.makeText(
                    this,
                    "Detalle encontrado",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            Toast.makeText(
                    this,
                    "Detalle no encontrado",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void eliminarDetalle() {

        if (detalleActual == null) {

            Toast.makeText(
                    this,
                    "Busque un detalle primero",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        helper.abrir();

        String mensaje =
                helper.eliminarDetalleReceta(
                        detalleActual
                                .getIdDetalleReceta()
                );

        helper.cerrar();

        Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_LONG
        ).show();

        if (
                mensaje.contains(
                        "correctamente"
                )
        ) {

            finish();
        }
    }
}
