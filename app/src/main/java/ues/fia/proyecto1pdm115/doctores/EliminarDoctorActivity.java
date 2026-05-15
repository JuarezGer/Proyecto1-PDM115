package ues.fia.proyecto1pdm115.doctores;

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

public class EliminarDoctorActivity extends AppCompatActivity {
    String duiEncontrado;
    EditText edtDuiDoctorEliminar;
    TextView txtNombreDoctorEliminar, txtApellidoDoctorEliminar,txtDoctorHospitalEliminar,txtUsuarioDoctorEliminar,txtEspecilidadDoctorEliminar;
    Button btnEliminarHospital,btnRegresarEliminarHospital,btnBuscarDoctorEliminar;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_doctor);
        edtDuiDoctorEliminar=findViewById(R.id.edtDuiDoctorEliminar);
        txtNombreDoctorEliminar=findViewById(R.id.txtNombreDoctorEliminar);
        txtApellidoDoctorEliminar=findViewById(R.id.txtApellidoDoctorEliminar);
        txtDoctorHospitalEliminar=findViewById(R.id.txtDoctorHospitalEliminar);
        txtUsuarioDoctorEliminar=findViewById(R.id.txtUsuarioDoctorEliminar);
        txtEspecilidadDoctorEliminar=findViewById(R.id.txtEspecilidadDoctorEliminar);
        btnEliminarHospital=findViewById(R.id.btnEliminarHospital);
        btnRegresarEliminarHospital=findViewById(R.id.btnRegresarEliminarHospital);
        btnBuscarDoctorEliminar=findViewById(R.id.btnBuscarDoctorEliminar);
        helper=new controlDBHospitalApp(this);
        btnRegresarEliminarHospital.setOnClickListener(v -> finish());
        btnBuscarDoctorEliminar.setOnClickListener(v -> buscarDoctor());
        btnEliminarHospital.setOnClickListener(v -> confirmarEliminacion());
    }

    private void confirmarEliminacion() {

        if (edtDuiDoctorEliminar.getText().toString().isEmpty()){
            Toast.makeText(this,"Primero busque doctor",Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle(
                "Confirmar eliminación"
        );

        builder.setMessage(
                "¿Desea eliminar este doctor?"
        );

        builder.setPositiveButton(
                "Sí, eliminar",
                (dialog, which) -> eliminarDoctor()
        );

        builder.setNegativeButton(
                "Cancelar",
                (dialog, which) -> dialog.dismiss()
        );

        AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void eliminarDoctor() {

        helper.abrir();

        String mensaje =
                helper.eliminarDoctor(
                        duiEncontrado
                );

        helper.cerrar();

        Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_LONG
        ).show();

        limpiar();
    }
    private void buscarDoctor() {

        String textoId = edtDuiDoctorEliminar.getText().toString().trim();

        if (textoId.isEmpty()) {

            Toast.makeText(this, "Ingrese el DUI", Toast.LENGTH_SHORT).show();

            return;
        }


        helper.abrir();
        HashMap<String, String> datos = helper.consultarDoctorDetalle(textoId);
        helper.cerrar();

        if (datos != null) {

            txtNombreDoctorEliminar.setText(valor(datos.get("nombre")));
            txtApellidoDoctorEliminar.setText(datos.get("apellido"));
            txtDoctorHospitalEliminar.setText(datos.get("hospital"));
            txtUsuarioDoctorEliminar.setText(datos.get("usuario"));
            txtEspecilidadDoctorEliminar.setText(datos.get("especialidades"));

            Toast.makeText(this, "Doctor encontrado", Toast.LENGTH_SHORT).show();
            duiEncontrado=datos.get("dui");

        } else {
            Toast.makeText(this, "Doctor no encontrado", Toast.LENGTH_SHORT).show();
        }
    }

    private void limpiar(){
        edtDuiDoctorEliminar.setText("");
        txtNombreDoctorEliminar.setText("");
        txtApellidoDoctorEliminar.setText("");
        txtUsuarioDoctorEliminar.setText("");
        txtEspecilidadDoctorEliminar.setText("");
        txtDoctorHospitalEliminar.setText("");
    }
    private String valor(String texto) {

        if (texto == null) {

            return "";
        }

        return texto;
    }
}