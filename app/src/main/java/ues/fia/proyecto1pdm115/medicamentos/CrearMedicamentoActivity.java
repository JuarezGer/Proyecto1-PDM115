package ues.fia.proyecto1pdm115.medicamentos;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Medicamento;
public class CrearMedicamentoActivity extends AppCompatActivity {

    EditText edtCodMedicamentoCrear, edtNombreMedicamentoCrear, edtFechaVencimientoCrear,
            edtCantidadInventarioCrear, edtLoteCrear,  edtPrecioVentaCrear;

    Button btnRegresarCrearMedicamento, btnGuardarMedicamento;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_medicamento);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtCodMedicamentoCrear = findViewById(R.id.edtCodMedicamentoCrear);
        edtNombreMedicamentoCrear = findViewById(R.id.edtNombreMedicamentoCrear);
        edtFechaVencimientoCrear = findViewById(R.id.edtFechaVencimientoCrear);
        edtCantidadInventarioCrear = findViewById(R.id.edtCantidadInventarioCrear);
        edtLoteCrear = findViewById(R.id.edtLoteCrear);
        edtPrecioVentaCrear = findViewById(R.id.edtPrecioVentaCrear);

        btnGuardarMedicamento = findViewById(R.id.btnGuardarMedicamento);
        btnRegresarCrearMedicamento = findViewById(R.id.btnRegresarCrearMedicamento);

        btnGuardarMedicamento.setOnClickListener(v -> guardarMedicamento());
        btnRegresarCrearMedicamento.setOnClickListener(v -> finish());
    }

    private void guardarMedicamento(){
        if (camposObligatoriosVacios()) {
            Toast.makeText(this,
                    "Complete los campos obligatorios",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        int cantidad = Integer.parseInt(edtCantidadInventarioCrear.getText().toString().trim());
        double precio = Double.parseDouble(edtPrecioVentaCrear.getText().toString().trim());

        if (cantidad <= 0 || precio <= 0) {

            Toast.makeText(this,
                    "Cantidad y precio deben ser mayores a 0",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        Medicamento medicamento = new Medicamento();

        medicamento.setCodMedicamento(edtCodMedicamentoCrear.getText().toString().trim());
        medicamento.setNombreMedicamento(edtNombreMedicamentoCrear.getText().toString().trim());
        medicamento.setFechaVencimiento(edtFechaVencimientoCrear.getText().toString().trim());
        medicamento.setCantidadInventario(Integer.parseInt(edtCantidadInventarioCrear.getText().toString().trim()));
        medicamento.setPrecioVenta(Double.parseDouble(edtPrecioVentaCrear.getText().toString().trim()));
        medicamento.setLote(edtLoteCrear.getText().toString().trim());

        try {
            helper.abrir();
            String mensaje = helper.insertarMedicamento(medicamento);
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

            if (mensaje.toLowerCase().contains("correctamente")) {
                limpiarCampos();
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            helper.cerrar();
        }
    }

    private boolean camposObligatoriosVacios() {
        return edtCodMedicamentoCrear.getText().toString().trim().isEmpty() ||
                edtNombreMedicamentoCrear.getText().toString().trim().isEmpty() ||
                edtFechaVencimientoCrear.getText().toString().trim().isEmpty() ||
                edtCantidadInventarioCrear.getText().toString().trim().isEmpty() ||
                edtPrecioVentaCrear.getText().toString().trim().isEmpty() ||
                edtLoteCrear.getText().toString().trim().isEmpty();
    }

    private void limpiarCampos() {
        edtCodMedicamentoCrear.setText("");
        edtNombreMedicamentoCrear.setText("");
        edtFechaVencimientoCrear.setText("");
        edtCantidadInventarioCrear.setText("");
        edtPrecioVentaCrear.setText("");
        edtLoteCrear.setText("");
    }
}
