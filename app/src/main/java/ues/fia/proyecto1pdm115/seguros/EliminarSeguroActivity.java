package ues.fia.proyecto1pdm115.seguros;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Seguro;

public class EliminarSeguroActivity extends AppCompatActivity {

    EditText edtIdPolizaEliminar;
    TextView txtDatosSeguroEliminar;
    Button btnBuscarSeguroEliminar, btnEliminarSeguro, btnRegresarEliminarSeguro;

    controlDBHospitalApp helper;
    int idPolizaEncontrada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_seguro);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtIdPolizaEliminar = findViewById(R.id.edtIdPolizaEliminar);
        txtDatosSeguroEliminar = findViewById(R.id.txtDatosSeguroEliminar);
        btnBuscarSeguroEliminar = findViewById(R.id.btnBuscarSeguroEliminar);
        btnEliminarSeguro = findViewById(R.id.btnEliminarSeguro);
        btnRegresarEliminarSeguro = findViewById(R.id.btnRegresarEliminarSeguro);

        btnBuscarSeguroEliminar.setOnClickListener(v -> buscarSeguro());
        btnEliminarSeguro.setOnClickListener(v -> eliminarSeguro());
        btnRegresarEliminarSeguro.setOnClickListener(v -> finish());
    }

    private void buscarSeguro() {
        if (edtIdPolizaEliminar.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, "Ingrese el ID de la póliza", Toast.LENGTH_SHORT).show();
            return;
        }

        int idPoliza;

        try {
            idPoliza = Integer.parseInt(edtIdPolizaEliminar.getText().toString().trim());
        } catch (NumberFormatException e) {
            Toast.makeText(this, "ID de póliza inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        helper.abrir();
        Seguro seguro = helper.consultarSeguro(idPoliza);
        helper.cerrar();

        if (seguro == null) {
            idPolizaEncontrada = -1;
            txtDatosSeguroEliminar.setText("No se encontró ningún seguro con ese ID.");
            Toast.makeText(this, "Seguro no encontrado", Toast.LENGTH_SHORT).show();
            return;
        }

        idPolizaEncontrada = seguro.getIdPoliza();
        String datos = "ID póliza: " + seguro.getIdPoliza() + "\n" +
                "DUI paciente: " + seguro.getDuiPaciente() + "\n" +
                "ID aseguradora: " + seguro.getIdAseguradora() + "\n" +
                "Cobertura: " + seguro.getPorcentajeCobertura() + "%\n" +
                "Tipo asegurado: " + seguro.getTipoAsegurado() + "\n" +
                "Deductible medicina: $" + seguro.getDeductibleMedicina() + "\n" +
                "Deductible operación: $" + seguro.getDeductibleOperacion();

        txtDatosSeguroEliminar.setText(datos);
    }

    private void eliminarSeguro() {
        if (idPolizaEncontrada == -1) {
            Toast.makeText(this, "Primero busque un seguro válido", Toast.LENGTH_SHORT).show();
            return;
        }

        helper.abrir();
        String mensaje = helper.eliminarSeguro(idPolizaEncontrada);
        helper.cerrar();

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();
        limpiarCampos();
    }

    private void limpiarCampos() {
        edtIdPolizaEliminar.setText("");
        txtDatosSeguroEliminar.setText("Busque una póliza para ver sus datos antes de eliminarla.");
        idPolizaEncontrada = -1;
    }
}
