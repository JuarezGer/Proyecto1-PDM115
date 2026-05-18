package ues.fia.proyecto1pdm115.detallesRecetas;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.ArrayList;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.DetalleReceta;
import ues.fia.proyecto1pdm115.modelos.Medicamento;
import ues.fia.proyecto1pdm115.modelos.Receta;

import android.database.Cursor;
import android.widget.ArrayAdapter;
import android.widget.Toast;
public class CrearDetalleRecetaActivity extends AppCompatActivity{
    EditText edtCantidadDetalleCrear, edtInstruccionesDetalleCrear,
            edtPrecioHistoricoDetalleCrear, edtSubtotalDetalleCrear;

    Spinner spnRecetaCrearDetalle, spnMedicamentoCrearDetalle;

    Button btnRegresarCrearDetalleReceta, btnGuardarDetalleReceta;

    controlDBHospitalApp helper;

    ArrayList<Integer> idsRecetas = new ArrayList<>();
    ArrayList<String> nombresRecetas = new ArrayList<>();

    ArrayList<String> codigosMedicamentos = new ArrayList<>();
    ArrayList<String> nombresMedicamentos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_detalle_receta);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        spnRecetaCrearDetalle = findViewById(R.id.spnRecetaCrearDetalle);
        spnMedicamentoCrearDetalle = findViewById(R.id.spnMedicamentoCrearDetalle);
        edtCantidadDetalleCrear = findViewById(R.id.edtCantidadDetalleCrear);
        edtInstruccionesDetalleCrear = findViewById(R.id.edtInstruccionesDetalleCrear);
        edtPrecioHistoricoDetalleCrear = findViewById(R.id.edtPrecioHistoricoDetalleCrear);
        edtSubtotalDetalleCrear = findViewById(R.id.edtSubtotalDetalleCrear);
        btnGuardarDetalleReceta = findViewById(R.id.btnGuardarDetalleReceta);
        btnRegresarCrearDetalleReceta = findViewById(R.id.btnRegresarCrearDetalleReceta);

        edtSubtotalDetalleCrear.setEnabled(false);

        edtCantidadDetalleCrear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calcularSubtotal();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        edtPrecioHistoricoDetalleCrear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                calcularSubtotal();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        btnGuardarDetalleReceta.setOnClickListener(
                v -> guardarDetalleReceta()
        );

        btnRegresarCrearDetalleReceta
                .setOnClickListener(v -> finish());

        cargarRecetas();
        cargarMedicamentos();
    }
    @Override
    protected void onResume() {
        super.onResume();
        cargarRecetas();
        cargarMedicamentos();
    }

    private void guardarDetalleReceta(){
        if (camposObligatoriosVacios()) {
            Toast.makeText(this,
                    "Complete los campos obligatorios",
                    Toast.LENGTH_SHORT).show();
            return;

        }
        int cantidad = Integer.parseInt(
                edtCantidadDetalleCrear.getText().toString().trim()
        );

        double precio = Double.parseDouble(
                edtPrecioHistoricoDetalleCrear.getText().toString().trim()
        );

        if (cantidad <= 0 || precio <= 0) {

            Toast.makeText(this,
                    "Cantidad y precio deben ser mayores a 0",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        DetalleReceta detalleReceta = new DetalleReceta();
        detalleReceta.setCantidad(Integer.parseInt(
                edtCantidadDetalleCrear.getText().toString().trim()));

        detalleReceta.setInstrucciones(
                edtInstruccionesDetalleCrear.getText().toString().trim());

        detalleReceta.setPrecioUnitarioHistorico(Double.parseDouble(
                        edtPrecioHistoricoDetalleCrear.getText().toString().trim()));

        double subtotal =
                Integer.parseInt(edtCantidadDetalleCrear.getText().toString().trim())
                        * Double.parseDouble(edtPrecioHistoricoDetalleCrear.getText().toString().trim());

        detalleReceta.setSubTotalItem(subtotal);

        detalleReceta.setIdReceta(obtenerIdRecetaSeleccionada());

        try {

            helper.abrir();

            String mensaje =
                    helper.insertarDetalleReceta(
                            detalleReceta,
                            obtenerCodMedicamentoSeleccionado()
                    );

            Toast.makeText(this,
                    mensaje,
                    Toast.LENGTH_LONG).show();

            if (mensaje.toLowerCase()
                    .contains("correctamente")) {

                limpiarCampos();
            }

        } catch (Exception e) {

            Toast.makeText(this,
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();

        } finally {

            helper.cerrar();
        }
    }

    private void limpiarCampos(){
        edtCantidadDetalleCrear.setText("");
        edtInstruccionesDetalleCrear.setText("");
        edtPrecioHistoricoDetalleCrear.setText("");
        edtSubtotalDetalleCrear.setText("");

        spnRecetaCrearDetalle.setSelection(0);
        spnMedicamentoCrearDetalle.setSelection(0);
    }

    private boolean camposObligatoriosVacios(){
        return  edtCantidadDetalleCrear.getText().toString().trim().isEmpty() ||
                edtInstruccionesDetalleCrear.getText().toString().trim().isEmpty() ||
                edtPrecioHistoricoDetalleCrear.getText().toString().trim().isEmpty() ||
                obtenerIdRecetaSeleccionada() == 0 ||
                obtenerCodMedicamentoSeleccionado().isEmpty();
    }


    private void cargarRecetas() {

        idsRecetas.clear();
        nombresRecetas.clear();

        idsRecetas.add(0);
        nombresRecetas.add("Seleccione una receta");

        helper.abrir();

        ArrayList<Receta> lista =
                helper.consultarTodasRecetas();

        helper.cerrar();

        for (Receta r : lista) {

            idsRecetas.add(
                    r.getIdReceta()
            );

            nombresRecetas.add(
                    "Receta #" + r.getIdReceta()
                            + " | Consulta #" + r.getIdConsulta()
                            + " | " + r.getFechaEmision()
            );
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        nombresRecetas
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spnRecetaCrearDetalle.setAdapter(adapter);
    }

    private void cargarMedicamentos() {

        codigosMedicamentos.clear();
        nombresMedicamentos.clear();

        codigosMedicamentos.add("");
        nombresMedicamentos.add("Seleccione un medicamento");

        helper.abrir();
        ArrayList<Medicamento> lista = helper.consultarTodosMedicamentos();
        helper.cerrar();

        for (Medicamento m : lista) {
            codigosMedicamentos.add(m.getCodMedicamento());
            nombresMedicamentos.add(m.getNombreMedicamento());
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        nombresMedicamentos
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spnMedicamentoCrearDetalle.setAdapter(adapter);
    }

    private int obtenerIdRecetaSeleccionada() {
        int posicion =
                spnRecetaCrearDetalle
                        .getSelectedItemPosition();

        return idsRecetas.get(posicion);
    }

    private String obtenerCodMedicamentoSeleccionado() {
        int posicion =
                spnMedicamentoCrearDetalle
                        .getSelectedItemPosition();

        return codigosMedicamentos.get(posicion);
    }

    private void calcularSubtotal() {

        String cantidadStr = edtCantidadDetalleCrear.getText().toString().trim();
        String precioStr = edtPrecioHistoricoDetalleCrear.getText().toString().trim();

        if (cantidadStr.isEmpty() || precioStr.isEmpty()) {
            edtSubtotalDetalleCrear.setText("");
            return;
        }

        try {

            int cantidad = Integer.parseInt(cantidadStr);
            double precio = Double.parseDouble(precioStr);

            double subtotal = cantidad * precio;

            edtSubtotalDetalleCrear.setText(String.valueOf(subtotal));

        } catch (Exception e) {
            edtSubtotalDetalleCrear.setText("");
        }
    }
}
