package ues.fia.proyecto1pdm115.medicamentos;

import androidx.appcompat.app.AppCompatActivity;
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

import androidx.core.content.ContextCompat;

import java.util.ArrayList;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Medicamento;

public class ActualizarMedicamentoActivity extends AppCompatActivity {
    EditText edtCod, edtNombre, edtFecha, edtCantidad, edtPrecio, edtLote;
    Button btnBuscar, btnActualizar;

    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_medicamento);

        helper = new controlDBHospitalApp(this);

        edtCod = findViewById(R.id.edtCodMedicamentoModificar);
        edtNombre = findViewById(R.id.edtNombreMedicamento);
        edtFecha = findViewById(R.id.edtFechaVencimiento);
        edtCantidad = findViewById(R.id.edtCantidadInventario);
        edtPrecio = findViewById(R.id.edtPrecioVenta);
        edtLote = findViewById(R.id.edtLote);

        btnBuscar = findViewById(R.id.btnBuscarMedicamento);
        btnActualizar = findViewById(R.id.btnActualizarMedicamento);

        String codigo =
                getIntent().getStringExtra(
                        "COD_MEDICAMENTO"
                );

        if (codigo != null) {

            edtCod.setText(codigo);

            buscarMedicamento();
        }

        btnBuscar.setOnClickListener(v -> buscarMedicamento());
        btnActualizar.setOnClickListener(v -> actualizarMedicamento());
    }

    private void buscarMedicamento() {

        String codigo =
                edtCod.getText().toString().trim();

        if (codigo.isEmpty()) {

            Toast.makeText(
                    this,
                    "Ingrese el código del medicamento",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        helper.abrir();

        Medicamento medicamento =
                helper.consultarMedicamento(codigo);

        helper.cerrar();

        if (medicamento != null) {

            edtNombre.setText(
                    medicamento.getNombreMedicamento()
            );

            edtFecha.setText(
                    medicamento.getFechaVencimiento()
            );

            edtCantidad.setText(
                    String.valueOf(
                            medicamento.getCantidadInventario()
                    )
            );

            edtPrecio.setText(
                    String.valueOf(
                            medicamento.getPrecioVenta()
                    )
            );

            edtLote.setText(
                    medicamento.getLote()
            );

            Toast.makeText(
                    this,
                    "Medicamento encontrado",
                    Toast.LENGTH_SHORT
            ).show();
            edtCod.setEnabled(false);
        }
        else {

            Toast.makeText(
                    this,
                    "Medicamento no encontrado",
                    Toast.LENGTH_SHORT
            ).show();
            edtCod.setEnabled(true);
        }
    }

    private void actualizarMedicamento() {

        if (
                edtNombre.getText().toString().trim().isEmpty()
                        || edtFecha.getText().toString().trim().isEmpty()
                        || edtCantidad.getText().toString().trim().isEmpty()
                        || edtPrecio.getText().toString().trim().isEmpty()
                        || edtLote.getText().toString().trim().isEmpty()
        ) {

            Toast.makeText(
                    this,
                    "Complete todos los campos",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }
        int cantidad = Integer.parseInt(edtCantidad.getText().toString().trim());
        double precio = Double.parseDouble(edtPrecio.getText().toString().trim());

        if (cantidad <= 0 || precio <= 0) {

            Toast.makeText(this,
                    "Cantidad y precio deben ser mayores a 0",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        Medicamento m = new Medicamento();

        m.setCodMedicamento(edtCod.getText().toString().trim());
        m.setNombreMedicamento(edtNombre.getText().toString().trim());
        m.setFechaVencimiento(edtFecha.getText().toString().trim());
        m.setCantidadInventario(Integer.parseInt(edtCantidad.getText().toString().trim()));
        m.setPrecioVenta(Double.parseDouble(edtPrecio.getText().toString().trim()));
        m.setLote(edtLote.getText().toString().trim());

        helper.abrir();
        String msg = helper.actualizarMedicamento(m);
        helper.cerrar();

        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }
}

