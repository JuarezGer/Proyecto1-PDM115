package ues.fia.proyecto1pdm115.recetas;

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
import ues.fia.proyecto1pdm115.modelos.Receta;

public class EliminarRecetaActivity extends AppCompatActivity{
    EditText edtIdRecetaEliminar;
    Button btnBuscarRecetaEliminar,
            btnEliminarReceta,
            btnRegresarEliminarReceta;
    TextView txtRecetaEliminar;

    controlDBHospitalApp helper;

    int idRecetaEncontrada = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_receta);
        Navegador.configurarBarra(this);

        helper = new controlDBHospitalApp(this);

        edtIdRecetaEliminar =
                findViewById(R.id.edtIdRecetaEliminar);

        btnBuscarRecetaEliminar =
                findViewById(R.id.btnBuscarRecetaEliminar);

        btnEliminarReceta =
                findViewById(R.id.btnEliminarReceta);

        btnRegresarEliminarReceta =
                findViewById(
                        R.id.btnRegresarEliminarReceta
                );

        txtRecetaEliminar =
                findViewById(R.id.txtRecetaEliminar);

        btnBuscarRecetaEliminar
                .setOnClickListener(v -> buscarReceta());

        btnEliminarReceta
                .setOnClickListener(
                        v -> confirmarEliminacion()
                );

        btnRegresarEliminarReceta
                .setOnClickListener(v -> finish());
    }

    private void buscarReceta() {

        String textoId =
                edtIdRecetaEliminar.getText().toString().trim();

        if (textoId.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID de la receta", Toast.LENGTH_SHORT).show();
            return;
        }

        int idReceta = Integer.parseInt(textoId);

        helper.abrir();

        Receta receta = helper.consultarReceta(idReceta);

        helper.cerrar();

        if (receta != null) {
            idRecetaEncontrada =
                    receta.getIdReceta();
            String datos =
                    "ID: " + receta.getIdReceta() + "\n" +
                            "ID Consulta: " + receta.getIdConsulta() + "\n" +
                            "Fecha emisión: " +
                            valor(receta.getFechaEmision()) + "\n" +
                            "Estado: " +
                            valor(receta.getEstadoReceta());

            txtRecetaEliminar.setText(datos);

            Toast.makeText(this, "Receta encontrada", Toast.LENGTH_SHORT).show();

        } else {

            idRecetaEncontrada = -1;

            txtRecetaEliminar.setText("Receta no encontrada.");

            Toast.makeText(this, "Receta no encontrada", Toast.LENGTH_SHORT).show();
        }
    }

    private void confirmarEliminacion() {

        if (idRecetaEncontrada == -1) {
            Toast.makeText(this, "Primero busque una receta válida", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Confirmar eliminación");

        builder.setMessage("¿Está seguro de eliminar esta receta?");

        builder.setPositiveButton("Sí, eliminar",
                (dialog, which) -> {eliminarReceta();});

        builder.setNegativeButton("Cancelar", (dialog, which) -> {
                    dialog.dismiss();});

        AlertDialog dialog = builder.create();dialog.show();
    }

    private void eliminarReceta() {

        if (idRecetaEncontrada == -1) {
            Toast.makeText(this, "Primero busque una receta válida",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        helper.abrir();

        String mensaje = helper.eliminarReceta(idRecetaEncontrada);

        helper.cerrar();

        Toast.makeText(this, mensaje, Toast.LENGTH_LONG).show();

        edtIdRecetaEliminar.setText("");
        txtRecetaEliminar.setText("Datos de la receta...");
        idRecetaEncontrada = -1;
    }

    private String valor(String texto) {
        if (texto == null) {
            return "";
        }
        return texto;
    }
}
