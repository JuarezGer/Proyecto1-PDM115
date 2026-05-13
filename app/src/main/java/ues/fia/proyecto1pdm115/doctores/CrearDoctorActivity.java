package ues.fia.proyecto1pdm115.doctores;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import ues.fia.proyecto1pdm115.modelos.Doctor;
import ues.fia.proyecto1pdm115.modelos.Usuario;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import androidx.appcompat.app.AppCompatActivity;


import java.util.List;

import ues.fia.proyecto1pdm115.R;

public class CrearDoctorActivity extends AppCompatActivity {
    EditText edtNombreDoctorCrear,edtApellidoDoctorCrear,edtDuiDoctorCrear;
    Spinner spinnerUsuarioDoctorCrear;
    Button btnRegresarDoctorCrear,btnGuardarDoctorCrear;

    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_doctor);
        edtNombreDoctorCrear=findViewById(R.id.edtNombreDoctorCrear);
        edtApellidoDoctorCrear=findViewById(R.id.edtApellidoDoctorCrear);
        edtDuiDoctorCrear=findViewById(R.id.edtDuiDoctorCrear);
        spinnerUsuarioDoctorCrear=findViewById(R.id.spinnerUsuarioDoctorCrear);
        btnRegresarDoctorCrear=findViewById(R.id.btnRegresarDoctorCrear);
        btnGuardarDoctorCrear=findViewById(R.id.btnGuardarDoctorCrear);

        helper=new controlDBHospitalApp(this);
        cargarDatosSpinners();
        btnRegresarDoctorCrear.setOnClickListener(v -> finish());
        btnGuardarDoctorCrear.setOnClickListener(v -> guardarDoctor());

    }

    private void guardarDoctor(){
        Usuario userSel = (Usuario) spinnerUsuarioDoctorCrear.getSelectedItem();
        String nombre = edtNombreDoctorCrear.getText().toString().trim();
        String apellido = edtApellidoDoctorCrear.getText().toString().trim();
        String dui = edtDuiDoctorCrear.getText().toString().trim();



        if (userSel!=null && !userSel.getIdUsuario().equals("-1")){
            Doctor doctor=new Doctor();
            Doctor doctorExis;
            doctor.setDuiDoctor(dui);
            doctor.setNombreDoctor(nombre);
            doctor.setApellidoDoctor(apellido);
            doctor.setIdUsuario(userSel.getIdUsuario());

            helper.abrir();
            doctorExis=helper.consultarDoctorUsuario(doctor.getIdUsuario());

            if (doctorExis!=null){
                Toast.makeText(
                        this,
                        "El usuario seleccionado ya esta asociado a un doctor",
                        Toast.LENGTH_LONG
                ).show();
                return;
            }
            String mensaje = helper.insertarDoctor(doctor);
            helper.cerrar();
            Toast.makeText(
                    this,
                    mensaje,
                    Toast.LENGTH_LONG
            ).show();
            limpiarCampos();
        }
    }
    private void limpiarCampos(){
        edtDuiDoctorCrear.setText("");
        edtNombreDoctorCrear.setText("");
        edtApellidoDoctorCrear.setText("");
        spinnerUsuarioDoctorCrear.setSelection(0);
    }
    private void cargarDatosSpinners(){
        helper.abrir();
        List<Usuario> usuarios = helper.consultarUsuarios();

        usuarios.add(0, new Usuario("-1", "Seleccione usuario..."));

        ArrayAdapter<Usuario> adapterUser = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,usuarios);
        adapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUsuarioDoctorCrear.setAdapter(adapterUser);

        helper.cerrar();
    }
}