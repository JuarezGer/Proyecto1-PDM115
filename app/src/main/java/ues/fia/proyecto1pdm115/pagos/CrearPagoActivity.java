package ues.fia.proyecto1pdm115.pagos;

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
import ues.fia.proyecto1pdm115.modelos.Pago;

public class CrearPagoActivity extends AppCompatActivity {

    Spinner spnConsultaPagoCrear;
    EditText edtTipoPagoCrear, edtMontoTotalCrear, edtFechaPagoCrear;
    Button btnGuardarPago, btnRegresarCrearPago;

    controlDBHospitalApp helper;

    ArrayList<Integer> idsConsultas = new ArrayList<>();
    ArrayList<String> nombresConsultas = new ArrayList<>();

    private final String COLOR_PLACEHOLDER = "#999999";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_pago);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        spnConsultaPagoCrear = findViewById(R.id.spnConsultaPagoCrear);
        edtTipoPagoCrear = findViewById(R.id.edtTipoPagoCrear);
        edtMontoTotalCrear = findViewById(R.id.edtMontoTotalCrear);
        edtFechaPagoCrear = findViewById(R.id.edtFechaPagoCrear);

        btnGuardarPago = findViewById(R.id.btnGuardarPago);
        btnRegresarCrearPago = findViewById(R.id.btnRegresarCrearPago);

        cargarConsultasSinPago();

        btnGuardarPago.setOnClickListener(v -> guardarPago());
        btnRegresarCrearPago.setOnClickListener(v -> finish());
    }

    private void cargarConsultasSinPago() {
        idsConsultas.clear();
        nombresConsultas.clear();

        idsConsultas.add(0);
        nombresConsultas.add("Seleccione una consulta");

        Cursor cursor = null;

        try {
            helper.abrir();
            cursor = helper.consultarConsultasSinPagoCursor();

            if (cursor.moveToFirst()) {
                do {
                    int idConsulta = cursor.getInt(cursor.getColumnIndexOrThrow("ID_CONSULTA"));
                    String duiPaciente = cursor.getString(cursor.getColumnIndexOrThrow("DUI_PACIENTE"));
                    String fechaConsulta = cursor.getString(cursor.getColumnIndexOrThrow("FECHA_CONSULTA"));
                    double cargoTotal = cursor.getDouble(cursor.getColumnIndexOrThrow("CARGO_TOTAL_CONSULTA"));

                    idsConsultas.add(idConsulta);
                    nombresConsultas.add("Consulta #" + idConsulta + " | Paciente: " + duiPaciente + " | $" + cargoTotal + " | " + fechaConsulta);

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            Toast.makeText(this, "Error al cargar consultas: " + e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            if (cursor != null) {
                cursor.close();
            }

            helper.cerrar();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_item,
                nombresConsultas
        ) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView, parent);

                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                view.setGravity(Gravity.CENTER_VERTICAL);
                view.setSingleLine(true);
                view.setPadding(dpToPx(12), 0, dpToPx(12), 0);

                if (position == 0) {
                    view.setTextColor(Color.parseColor(COLOR_PLACEHOLDER));
                } else {
                    view.setTextColor(ContextCompat.getColor(CrearPagoActivity.this, R.color.text_black));
                }

                return view;
            }

            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getDropDownView(position, convertView, parent);

                view.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
                view.setPadding(dpToPx(12), dpToPx(12), dpToPx(12), dpToPx(12));

                if (position == 0) {
                    view.setTextColor(Color.parseColor(COLOR_PLACEHOLDER));
                } else {
                    view.setTextColor(ContextCompat.getColor(CrearPagoActivity.this, R.color.text_black));
                }

                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnConsultaPagoCrear.setAdapter(adapter);
    }

    private int obtenerIdConsultaSeleccionada() {
        int posicion = spnConsultaPagoCrear.getSelectedItemPosition();

        if (posicion <= 0 || posicion >= idsConsultas.size()) {
            return 0;
        }

        return idsConsultas.get(posicion);
    }

    private void guardarPago() {
        if (camposObligatoriosVacios()) {
            Toast.makeText(this, "Complete los campos obligatorios", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Pago pago = new Pago();
            pago.setIdConsulta(obtenerIdConsultaSeleccionada());
            pago.setTipoPago(edtTipoPagoCrear.getText().toString().trim());
            pago.setMontoTotal(Double.parseDouble(edtMontoTotalCrear.getText().toString().trim()));
            pago.setFechaPago(edtFechaPagoCrear.getText().toString().trim());

            helper.abrir();
            String mensaje = helper.insertarPago(pago);
            Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

            if (mensaje.toLowerCase().contains("correctamente")) {
                limpiarCampos();
                cargarConsultasSinPago();
            }

        } catch (NumberFormatException e) {
            Toast.makeText(this, "El monto debe ser numérico", Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();

        } finally {
            helper.cerrar();
        }
    }

    private boolean camposObligatoriosVacios() {
        return obtenerIdConsultaSeleccionada() == 0 ||
                edtTipoPagoCrear.getText().toString().trim().isEmpty() ||
                edtMontoTotalCrear.getText().toString().trim().isEmpty() ||
                edtFechaPagoCrear.getText().toString().trim().isEmpty();
    }

    private void limpiarCampos() {
        spnConsultaPagoCrear.setSelection(0);
        edtTipoPagoCrear.setText("");
        edtMontoTotalCrear.setText("");
        edtFechaPagoCrear.setText("");
    }

    private int dpToPx(int dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp,
                getResources().getDisplayMetrics()
        );
    }
}
