package ues.fia.proyecto1pdm115.hospitales;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.HashMap;

import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.R;


public class EliminarHospitalActivity extends AppCompatActivity {
    EditText edtIdHospitalEliminar;
    int idHospitalEncontrado;
    TextView txtNombreHospitalEliminar,txtTelefonoHospitalEliminar,txtUbicacionHospitalEliminar,txtEspecialidadesHospitalEliminar;
    Button btnBuscarHospitalEliminar,btnEliminarHospital,btnRegresarEliminarHospital;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_hospital);
        edtIdHospitalEliminar=findViewById(R.id.edtIdHospitalEliminar);
        txtNombreHospitalEliminar=findViewById(R.id.txtNombreHospitalEliminar);
        txtTelefonoHospitalEliminar=findViewById(R.id.txtTelefonoHospitalEliminar);
        txtUbicacionHospitalEliminar=findViewById(R.id.txtUbicacionHospitalEliminar);
        txtEspecialidadesHospitalEliminar=findViewById(R.id.txtEspecialidadesHospitalEliminar);
        btnBuscarHospitalEliminar=findViewById(R.id.btnBuscarHospitalEliminar);
        btnEliminarHospital=findViewById(R.id.btnEliminarHospital);
        btnRegresarEliminarHospital=findViewById(R.id.btnRegresarEliminarHospital);
        helper=new controlDBHospitalApp(this);
        btnRegresarEliminarHospital.setOnClickListener(v -> finish());
        btnBuscarHospitalEliminar.setOnClickListener(v -> buscarHospital());
        btnEliminarHospital.setOnClickListener(v -> confirmarEliminacion());


    }

    private void confirmarEliminacion() {

        if (edtIdHospitalEliminar.getText().toString().isEmpty()){
            Toast.makeText(this,"Primero busque hospital",Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle(
                "Confirmar eliminación"
        );

        builder.setMessage(
                "¿Desea eliminar este hospital?"
        );

        builder.setPositiveButton(
                "Sí, eliminar",
                (dialog, which) -> eliminarHospital()
        );

        builder.setNegativeButton(
                "Cancelar",
                (dialog, which) -> dialog.dismiss()
        );

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void eliminarHospital() {

        helper.abrir();

        String mensaje =
                helper.eliminarHospital(
                        idHospitalEncontrado
                );

        helper.cerrar();

        Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_LONG
        ).show();

        limpiar();
    }

    private void limpiar(){
        edtIdHospitalEliminar.setText("");
        txtNombreHospitalEliminar.setText("");
        txtUbicacionHospitalEliminar.setText("");
        txtTelefonoHospitalEliminar.setText("");
        txtEspecialidadesHospitalEliminar.setText("");
    }
    private void buscarHospital() {

        String textoId = edtIdHospitalEliminar.getText().toString().trim();

        if (textoId.isEmpty()) {

            Toast.makeText(this, "Ingrese el ID", Toast.LENGTH_SHORT).show();

            return;
        }

        int idHospital = Integer.parseInt(textoId);

        helper.abrir();
        HashMap<String, String> datos = helper.consultarHospitalDetalle(idHospital);
        helper.cerrar();

        if (datos != null) {

            txtNombreHospitalEliminar.setText(valor(datos.get("hospital")));
            txtTelefonoHospitalEliminar.setText(datos.get("telefono"));
            txtUbicacionHospitalEliminar.setText(datos.get("ubicacion"));
            txtEspecialidadesHospitalEliminar.setText(datos.get("especialidades"));

            Toast.makeText(this, "Hospital encontrado", Toast.LENGTH_SHORT).show();
            idHospitalEncontrado=Integer.parseInt(textoId);

        } else {
            Toast.makeText(this, "Hospital no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private String valor(String texto) {

        if (texto == null) {

            return "";
        }

        return texto;
    }
}