package ues.fia.proyecto1pdm115.hospitalizaciones;

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
import ues.fia.proyecto1pdm115.modelos.Hospitalizacion;

public class EliminarHospitalizacionActivity extends AppCompatActivity {
    EditText edtIdHospitalizacionEliminar;
    Button btnBuscarHospitalizacionEliminar,
            btnEliminarHospitalizacion,
            btnRegresarEliminarHospitalizacion;
    TextView txtHospitalizacionEliminar;

    controlDBHospitalApp helper;

    int idHospitalizacionEncontrada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_hospitalizacion);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtIdHospitalizacionEliminar = findViewById(R.id.edtIdHospitalizacionEliminar);
        btnBuscarHospitalizacionEliminar = findViewById(R.id.btnBuscarHospitalizacionEliminar);
        btnEliminarHospitalizacion = findViewById(R.id.btnEliminarHospitalizacion);
        btnRegresarEliminarHospitalizacion = findViewById(R.id.btnRegresarEliminarHospitalizacion);
        txtHospitalizacionEliminar = findViewById(R.id.txtHospitalizacionEliminar);

        Integer idHospitalizacion =
                getIntent().getIntExtra(
                        "ID_HOSPITALIZACION",
                        -1
                );

        if (idHospitalizacion != -1) {

            edtIdHospitalizacionEliminar
                    .setText(
                            String.valueOf(
                                    idHospitalizacion
                            )
                    );

            buscarHospitalizacion();
        }

        btnBuscarHospitalizacionEliminar
                .setOnClickListener(v -> buscarHospitalizacion());

        btnEliminarHospitalizacion
                .setOnClickListener(v -> confirmarEliminacion());

        btnRegresarEliminarHospitalizacion
                .setOnClickListener(v -> finish());
    }

    private void buscarHospitalizacion() {
        String textoId = edtIdHospitalizacionEliminar.getText().toString().trim();

        if (textoId.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID de la hospitalización", Toast.LENGTH_SHORT).show();
            return;
        }

        int idHospitalizacion = Integer.parseInt(textoId);

        helper.abrir();
        Hospitalizacion hospitalizacion = helper.consultarHospitalizacion(idHospitalizacion);
        helper.cerrar();

        if (hospitalizacion != null) {
            idHospitalizacionEncontrada = hospitalizacion.getIdHospitalizacion();

            String datos =
                    "ID: " + hospitalizacion.getIdHospitalizacion() + "\n" +
                            "ID Consulta: " + hospitalizacion.getIdConsulta() + "\n" +
                            "Fecha inicio: " + valor(hospitalizacion.getFechaInicioHosp()) + "\n" +
                            "Fecha fin: " + valor(hospitalizacion.getFechaFinHosp()) + "\n" +
                            "Motivo: " + valor(hospitalizacion.getMotivoIngreso()) + "\n" +
                            "Costo: $" + hospitalizacion.getCostoHospitalizacion();

            txtHospitalizacionEliminar.setText(datos);

            Toast.makeText(this, "Hospitalización encontrada", Toast.LENGTH_SHORT).show();
        } else {
            idHospitalizacionEncontrada = -1;
            txtHospitalizacionEliminar.setText("Hospitalización no encontrada.");
            Toast.makeText(this, "Paciente no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmarEliminacion() {
        if (idHospitalizacionEncontrada == -1) {
            Toast.makeText(this, "Primero busque una hospitalización válida",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Está seguro de eliminar esta hospitalización?");

        builder.setPositiveButton("Sí, eliminar", (dialog, which) -> {
            eliminarHospitalizacion();
        });

        builder.setNegativeButton("Cancelar", (dialog, which) -> {
            dialog.dismiss();
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void eliminarHospitalizacion() {
        if (idHospitalizacionEncontrada == -1) {
            Toast.makeText(this, "Primero busque una hospitalización válida",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        helper.abrir();
        String mensaje =
                helper.eliminarHospitalizacion(
                        idHospitalizacionEncontrada
                );
        helper.cerrar();

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

        edtIdHospitalizacionEliminar.setText("");
        txtHospitalizacionEliminar.setText("Datos de la hospitalización...");
        idHospitalizacionEncontrada = -1;
    }

    private String valor(String texto) {
        if (texto == null) {
            return "";
        }
        return texto;
    }
}
