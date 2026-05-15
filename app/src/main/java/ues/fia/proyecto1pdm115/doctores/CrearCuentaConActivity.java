package ues.fia.proyecto1pdm115.doctores;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ues.fia.proyecto1pdm115.modelos.Doctor;
import ues.fia.proyecto1pdm115.modelos.Especialidad;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;

import ues.fia.proyecto1pdm115.R;

public class CrearCuentaConActivity extends AppCompatActivity {
    controlDBHospitalApp helper;
    Spinner spinnerDoctorCrearEsp,spinnerEspecialidadCrearDoc;
    Button btnRegresarCuentaConCrear,btnGuardarCuentaConCrear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_cuenta_con);

        helper = new controlDBHospitalApp(this);
        spinnerDoctorCrearEsp=findViewById(R.id.spinnerDoctorCrearEsp);
        spinnerEspecialidadCrearDoc=findViewById(R.id.spinnerEspecialidadCrearDoc);
        btnRegresarCuentaConCrear=findViewById(R.id.btnRegresarCuentaConCrear);
        btnGuardarCuentaConCrear=findViewById(R.id.btnGuardarCuentaConCrear);

        cargarDatosSpinners();
        btnRegresarCuentaConCrear.setOnClickListener(v -> finish());
        btnGuardarCuentaConCrear.setOnClickListener(v -> guardarAsignacion());
    }

    private void cargarDatosSpinners(){
        helper.abrir();
        List<Doctor> doctores = helper.consultarDoctores();
        List<Especialidad> especialidades = helper.consultarEspecialidades();

        doctores.add(0, new Doctor("1", "Seleccione un doctor","..."));
        especialidades.add(0, new Especialidad(-1, "Seleccione un especialidad..."));

        ArrayAdapter<Doctor> adapterDoc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,doctores);
        adapterDoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDoctorCrearEsp.setAdapter(adapterDoc);

        ArrayAdapter<Especialidad> adapterEspe = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,especialidades);
        adapterEspe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEspecialidadCrearDoc.setAdapter(adapterEspe);
        helper.cerrar();
    }

    private void guardarAsignacion(){
        Doctor docSel = (Doctor) spinnerDoctorCrearEsp.getSelectedItem();
        Especialidad espeSel = (Especialidad) spinnerEspecialidadCrearDoc.getSelectedItem();

        if(docSel!=null && espeSel !=null && !docSel.getDuiDoctor().equals("1") &&espeSel.getIdEspecialidad()!=-1){
            String dui = docSel.getDuiDoctor();
            Integer idEspe = espeSel.getIdEspecialidad();

            helper.abrir();

            if (helper.verificarAsignacionExistente(dui, idEspe)) {
                Toast.makeText(this, "El doctor ya tiene asignada esa especialidad", Toast.LENGTH_LONG).show();
                helper.cerrar();
                return; // Detenemos la ejecución aquí
            }

            String mensaje = helper.insertarCuentaCon(dui,idEspe);
            helper.cerrar();

            Toast.makeText(
                    this,
                    mensaje,
                    Toast.LENGTH_LONG
            ).show();
            spinnerEspecialidadCrearDoc.setSelection(0);
            spinnerDoctorCrearEsp.setSelection(0);
        }else{
            Toast.makeText(this,"Por favor, seleccione un doctor y una especialidad",Toast.LENGTH_LONG).show();
        }
    }
}