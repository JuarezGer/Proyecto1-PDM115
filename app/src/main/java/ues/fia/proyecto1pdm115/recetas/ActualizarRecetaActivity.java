package ues.fia.proyecto1pdm115.recetas;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Receta;
public class ActualizarRecetaActivity
        extends AppCompatActivity {

    EditText edtIdRecetaModificar,
            edtFechaEmisionModificar,
            edtEstadoRecetaModificar;

    Spinner spnConsultaModificar;

    Button btnBuscarReceta,
            btnActualizarReceta,
            btnRegresarModificar;

    controlDBHospitalApp helper;

    ArrayList<Integer> idsConsultas =
            new ArrayList<>();

    ArrayList<String> nombresConsultas =
            new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(
                R.layout.activity_modificar_receta
        );

        Navegador.configurarBarra(this);

        helper =
                new controlDBHospitalApp(this);

        edtIdRecetaModificar =
                findViewById(
                        R.id.edtIdRecetaModificar
                );

        edtFechaEmisionModificar =
                findViewById(
                        R.id.edtFechaEmisionModificar
                );

        edtEstadoRecetaModificar =
                findViewById(
                        R.id.edtEstadoRecetaModificar
                );

        spnConsultaModificar =
                findViewById(
                        R.id.spnConsultaModificar
                );

        btnBuscarReceta =
                findViewById(
                        R.id.btnBuscarReceta
                );

        btnActualizarReceta =
                findViewById(
                        R.id.btnActualizarReceta
                );

        btnRegresarModificar =
                findViewById(
                        R.id.btnRegresarModificar
                );

        String idTexto =
                String.valueOf(
                        getIntent().getIntExtra(
                                "ID_RECETA",
                                -1
                        )
                );

        if (!idTexto.equals("-1")) {

            edtIdRecetaModificar
                    .setText(idTexto);

            buscarReceta();
        }

        cargarConsultas();

        btnBuscarReceta.setOnClickListener(
                v -> buscarReceta()
        );

        btnActualizarReceta.setOnClickListener(
                v -> actualizarReceta()
        );

        btnRegresarModificar.setOnClickListener(
                v -> finish()
        );
    }

    private void cargarConsultas() {

        idsConsultas.clear();
        nombresConsultas.clear();

        idsConsultas.add(0);

        nombresConsultas.add(
                "Seleccione una consulta"
        );

        idsConsultas.add(1);

        nombresConsultas.add(
                "Consulta #1"
        );

        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(
                        this,
                        android.R.layout.simple_spinner_item,
                        nombresConsultas
                );

        adapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spnConsultaModificar.setAdapter(adapter);
    }

    private void buscarReceta() {

        String idTexto =
                edtIdRecetaModificar
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

        int idReceta =
                Integer.parseInt(idTexto);

        helper.abrir();

        Receta receta =
                helper.consultarReceta(
                        idReceta
                );

        helper.cerrar();

        if (receta != null) {

            edtFechaEmisionModificar.setText(
                    receta.getFechaEmision()
            );

            edtEstadoRecetaModificar.setText(
                    receta.getEstadoReceta()
            );

            int posicionConsulta = 0;

            for (int i = 0;
                 i < idsConsultas.size();
                 i++) {

                if (
                        idsConsultas.get(i)
                                .equals(
                                        receta.getIdConsulta()
                                )
                ) {

                    posicionConsulta = i;
                    break;
                }
            }

            spnConsultaModificar
                    .setSelection(posicionConsulta);

            edtIdRecetaModificar
                    .setEnabled(false);

            Toast.makeText(
                    this,
                    "Receta encontrada",
                    Toast.LENGTH_SHORT
            ).show();

        } else {

            edtIdRecetaModificar
                    .setEnabled(true);

            Toast.makeText(
                    this,
                    "Receta no encontrada",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void actualizarReceta() {

        String idTexto =
                edtIdRecetaModificar
                        .getText()
                        .toString()
                        .trim();

        if (
                idTexto.isEmpty()
                        || edtFechaEmisionModificar
                        .getText()
                        .toString()
                        .trim()
                        .isEmpty()
                        || edtEstadoRecetaModificar
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

        Receta receta =
                new Receta();

        receta.setIdReceta(
                Integer.parseInt(idTexto)
        );

        receta.setIdConsulta(
                idsConsultas.get(
                        spnConsultaModificar
                                .getSelectedItemPosition()
                )
        );

        receta.setFechaEmision(
                edtFechaEmisionModificar
                        .getText()
                        .toString()
                        .trim()
        );

        receta.setEstadoReceta(
                edtEstadoRecetaModificar
                        .getText()
                        .toString()
                        .trim()
        );

        helper.abrir();

        String mensaje =
                helper.actualizarReceta(
                        receta
                );

        helper.cerrar();

        Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_LONG
        ).show();
    }
}