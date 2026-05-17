package ues.fia.proyecto1pdm115.medicamentos;

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
import ues.fia.proyecto1pdm115.modelos.Medicamento;
public class EliminarMedicamentoActivity extends AppCompatActivity {

    EditText edtCodMedicamentoEliminar;
    Button btnBuscarMedicamentoEliminar, btnEliminarMedicamento, btnRegresarEliminarMedicamento;
    TextView txtMedicamentoEliminar;
    controlDBHospitalApp helper;
    String codMedicamentoEncontrado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_medicamento);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtCodMedicamentoEliminar =
                findViewById(R.id.edtCodMedicamentoEliminar);
        btnBuscarMedicamentoEliminar =
                findViewById(R.id.btnBuscarMedicamentoEliminar);
        btnEliminarMedicamento =
                findViewById(R.id.btnEliminarMedicamento);
        btnRegresarEliminarMedicamento =
                findViewById(R.id.btnRegresarEliminarMedicamento);
        txtMedicamentoEliminar =
                findViewById(R.id.txtMedicamentoEliminar);

        String codMedicamento =
                getIntent().getStringExtra(
                        "COD_MEDICAMENTO"
                );

        if (codMedicamento != null &&
                !codMedicamento.isEmpty()) {

            edtCodMedicamentoEliminar
                    .setText(codMedicamento);

            buscarMedicamento();
        }

        btnBuscarMedicamentoEliminar
                .setOnClickListener(v -> buscarMedicamento());
        btnEliminarMedicamento
                .setOnClickListener(v -> confirmarEliminacion());
        btnRegresarEliminarMedicamento
                .setOnClickListener(v -> finish());
    }

    private void buscarMedicamento() {
        String codigoMedicamento = edtCodMedicamentoEliminar.getText().toString().trim();

        if (codigoMedicamento.isEmpty()) {
            Toast.makeText(this, "Ingrese el código del medicamento", Toast.LENGTH_SHORT).show();
            return;
        }

        helper.abrir();
        Medicamento medicamento = helper.consultarMedicamento(codigoMedicamento);
        helper.cerrar();

        if (medicamento != null) {
            codMedicamentoEncontrado = medicamento.getCodMedicamento();

            String datos = "Código: " + medicamento.getCodMedicamento() + "\n" +
                    "Nombre: " + valor(medicamento.getNombreMedicamento()) + "\n" +
                    "Fecha de vencimiento: " + valor(medicamento.getFechaVencimiento()) + "\n" +
                    "Cantidad inventario: " + valor(medicamento.getCantidadInventario().toString()) + "\n" +
                    "Precio de venta: " + valor(medicamento.getPrecioVenta().toString()) + "\n" +
                    "Lote: " + valor(medicamento.getLote());  //acá me quedé

            txtMedicamentoEliminar.setText(datos);

            Toast.makeText(this, "Medicamento encontrado", Toast.LENGTH_SHORT).show();
        } else {
            codMedicamentoEncontrado = "";
            txtMedicamentoEliminar.setText("Medicamento no encontrado.");
            Toast.makeText(this, "Medicamento no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmarEliminacion() {
        if (codMedicamentoEncontrado.isEmpty()) {
            Toast.makeText(this, "Primero busque un medicamento válido", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Está seguro de eliminar este medicamento?");

        builder.setPositiveButton("Sí, eliminar", (dialog, which) -> {
            eliminarMedicamento();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void eliminarMedicamento() {
        if (codMedicamentoEncontrado.isEmpty()) {
            Toast.makeText(this, "Primero busque un medicamento válido", Toast.LENGTH_SHORT).show();
            return;
        }

        helper.abrir();
        String mensaje = helper.eliminarMedicamento(codMedicamentoEncontrado);
        helper.cerrar();

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

        edtCodMedicamentoEliminar.setText("");
        txtMedicamentoEliminar.setText("Datos del medicamento...");
        codMedicamentoEncontrado = "";
    }

    private String valor(String texto) {
        if (texto == null) {
            return "";
        }
        return texto;
    }
}
