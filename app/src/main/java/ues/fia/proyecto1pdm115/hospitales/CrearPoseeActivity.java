package ues.fia.proyecto1pdm115.hospitales;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import ues.fia.proyecto1pdm115.modelos.Hospital;
import ues.fia.proyecto1pdm115.modelos.Especialidad;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;

import ues.fia.proyecto1pdm115.R;

public class CrearPoseeActivity extends AppCompatActivity {
    Spinner spinnerHospitalesCrear,spinnerEspecialidadCrear;
    Button btnGuardarPoseeCrear,btnRegresarPoseeCrear;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_posee);

        helper=new controlDBHospitalApp(this);
        spinnerHospitalesCrear=findViewById(R.id.spinnerHospitalesCrear);
        spinnerEspecialidadCrear=findViewById(R.id.spinnerEspecialidadCrear);
        btnGuardarPoseeCrear=findViewById(R.id.btnGuardarPoseeCrear);
        btnRegresarPoseeCrear=findViewById(R.id.btnRegresarPoseeCrear);

        cargarDatosSpinners();
        btnRegresarPoseeCrear.setOnClickListener(v -> finish());
        btnGuardarPoseeCrear.setOnClickListener(v -> guardarAsignacion());
    }

    private void cargarDatosSpinners(){
        helper.abrir();
        List<Hospital> hospitales = helper.consultarHospitales();
        List<Especialidad> especialidades = helper.consultarEspecialidades();

        hospitales.add(0, new Hospital(1, "Seleccione un hospital..."));
        especialidades.add(0, new Especialidad(-1, "Seleccione un especialidad..."));

        ArrayAdapter<Hospital> adapterHospi = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,hospitales);
        adapterHospi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHospitalesCrear.setAdapter(adapterHospi);

        ArrayAdapter<Especialidad> adapterEspe = new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,especialidades);
        adapterEspe.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEspecialidadCrear.setAdapter(adapterEspe);
        helper.cerrar();
    }

    private void guardarAsignacion(){
        Hospital hospiSel = (Hospital) spinnerHospitalesCrear.getSelectedItem();
        Especialidad espeSel = (Especialidad) spinnerEspecialidadCrear.getSelectedItem();

        if(hospiSel!=null && espeSel !=null && hospiSel.getIdHospital()!=-1&&espeSel.getIdEspecialidad()!=-1){
            Integer idHospi = hospiSel.getIdHospital();
            Integer idEspe = espeSel.getIdEspecialidad();

            helper.abrir();
            if (helper.verificarAsignacionExistenteHospital(idHospi, idEspe)) {
                Toast.makeText(this, "El hospital ya tiene asignada esa especialidad", Toast.LENGTH_LONG).show();
                helper.cerrar();
                return; // Detenemos la ejecución aquí
            }

            String mensaje = helper.insertarPosee(idHospi,idEspe);
            helper.cerrar();

            Toast.makeText(
                    this,
                    mensaje,
                    Toast.LENGTH_LONG
            ).show();
            spinnerHospitalesCrear.setSelection(0);
            spinnerEspecialidadCrear.setSelection(0);
        }else{
            Toast.makeText(this,"Por favor, seleccione un hospital y una especialidad",Toast.LENGTH_LONG).show();
        }
    }
}