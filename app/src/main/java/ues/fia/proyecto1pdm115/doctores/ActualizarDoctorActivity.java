package ues.fia.proyecto1pdm115.doctores;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import ues.fia.proyecto1pdm115.modelos.Doctor;
import ues.fia.proyecto1pdm115.modelos.Hospital;
import ues.fia.proyecto1pdm115.modelos.Usuario;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import androidx.appcompat.app.AppCompatActivity;


import java.util.List;

import ues.fia.proyecto1pdm115.R;

public class ActualizarDoctorActivity extends AppCompatActivity {
    EditText edtNombreDoctorActualizar,edtApellidoDoctorActualizar,edtDuiDoctorActualizar;
    Spinner spinnerUsuarioDoctorActualizar, spinnerHospitalDoctorActualizar;
    Button btnRegresarDoctorActualizar,btnGuardarDoctorActualizar,btnBuscarModificarDoctor;
    TextView tvUsuarioActualDoctor;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_doctor);

        edtNombreDoctorActualizar=findViewById(R.id.edtNombreDoctorActualizar);
        edtApellidoDoctorActualizar=findViewById(R.id.edtApellidoDoctorActualizar);
        edtDuiDoctorActualizar=findViewById(R.id.edtDuiDoctorActualizar);
        spinnerUsuarioDoctorActualizar=findViewById(R.id.spinnerUsuarioDoctorActualizar);
        spinnerHospitalDoctorActualizar=findViewById(R.id.spinnerHospitalDoctorActualizar);
        btnRegresarDoctorActualizar=findViewById(R.id.btnRegresarDoctorActualiizar);
        btnGuardarDoctorActualizar=findViewById(R.id.btnGuardarDoctorActualizar);
        btnBuscarModificarDoctor=findViewById(R.id.btnBuscarModificarDoctor);
        tvUsuarioActualDoctor=findViewById(R.id.tvUsuarioActualDoctor);
        helper=new controlDBHospitalApp(this);
        cargarDatosSpinners();
        btnRegresarDoctorActualizar.setOnClickListener(v -> finish());
        btnBuscarModificarDoctor.setOnClickListener(v -> buscarDoctor());
        btnGuardarDoctorActualizar.setOnClickListener(v -> guardarDoctor());

    }

    private void guardarDoctor(){
        Usuario userSel = (Usuario) spinnerUsuarioDoctorActualizar.getSelectedItem();
        Hospital hospiSel =(Hospital) spinnerHospitalDoctorActualizar.getSelectedItem();
        String nombre = edtNombreDoctorActualizar.getText().toString().trim();
        String dui= edtDuiDoctorActualizar.getText().toString().trim();
        String apellido=edtApellidoDoctorActualizar.getText().toString().trim();
        String usuario="";

        if (userSel.getIdUsuario().trim().equals(tvUsuarioActualDoctor.getText().toString().trim())){
            usuario=userSel.getIdUsuario().trim();
        }


        if(userSel!=null&&hospiSel!=null&&!userSel.getIdUsuario().equals("-1")&&hospiSel.getIdHospital()!=-1){
            Doctor doctor=new Doctor();
            Doctor doctorExis;
            doctor.setDuiDoctor(dui);
            doctor.setNombreDoctor(nombre);
            doctor.setApellidoDoctor(apellido);
            doctor.setIdHospital(hospiSel.getIdHospital());
            if (usuario.equals("")){
                helper.abrir();
                doctorExis = helper.consultarDoctorUsuario(userSel.getIdUsuario());
                helper.cerrar();

                if (doctorExis!=null){
                    Toast.makeText(
                            this,
                            "El usuario seleccionado ya esta asociado a un doctor",
                            Toast.LENGTH_LONG
                    ).show();
                    return;
                }
                doctor.setIdUsuario(userSel.getIdUsuario());
            }else {
                doctor.setIdUsuario(usuario);
            }
            helper.abrir();
            String mensaje = helper.actualizarDoctor(doctor);
            helper.cerrar();
            Toast.makeText(
                    this,
                    mensaje,
                    Toast.LENGTH_LONG
            ).show();
            limpiarCampos();
            edtDuiDoctorActualizar.setEnabled(true);

        }

    }
    private void limpiarCampos(){
        edtDuiDoctorActualizar.setText("");
        edtNombreDoctorActualizar.setText("");
        edtApellidoDoctorActualizar.setText("");
        spinnerUsuarioDoctorActualizar.setSelection(0);
        spinnerHospitalDoctorActualizar.setSelection(0);
        tvUsuarioActualDoctor.setText("");
    }
    private void cargarDatosSpinners(){
        helper.abrir();
        List<Usuario> usuarios = helper.consultarUsuarios();
        List<Hospital> hospitales = helper.consultarHospitales();

        usuarios.add(0, new Usuario("-1", "Seleccione usuario..."));
        hospitales.add(0,new Hospital(-1,"Seleccione hospital"));

        ArrayAdapter<Usuario> adapterUser = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,usuarios);
        adapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUsuarioDoctorActualizar.setAdapter(adapterUser);

        ArrayAdapter<Hospital> adapterHospi = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,hospitales);
        adapterHospi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHospitalDoctorActualizar.setAdapter(adapterHospi);

        helper.cerrar();
    }

    private String valor(String texto) {

        if (texto == null) {

            return "";
        }

        return texto;
    }

    private void buscarDoctor() {

        String textoId =
                edtDuiDoctorActualizar
                        .getText()
                        .toString()
                        .trim();

        if (textoId.isEmpty()) {

            Toast.makeText(
                    this,
                    "Ingrese el ID",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        helper.abrir();

        Doctor doctor = helper.consultarDoctor(textoId);

        helper.cerrar();

        if (doctor != null) {

            edtNombreDoctorActualizar.setText(valor(doctor.getNombreDoctor()));
            tvUsuarioActualDoctor.setText(valor(doctor.getIdUsuario()));
            edtApellidoDoctorActualizar.setText(valor(doctor.getApellidoDoctor()));

            Toast.makeText(
                    this,
                    "Doctor encontrado",
                    Toast.LENGTH_SHORT
            ).show();
            edtDuiDoctorActualizar.setEnabled(false);

        } else {

            limpiarCamposSinId();

            Toast.makeText(
                    this,
                    "Hospital no encontrado",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void limpiarCamposSinId() {

        edtNombreDoctorActualizar
                .setText("");
        edtApellidoDoctorActualizar.setText("");
        tvUsuarioActualDoctor.setText("");
    }
}