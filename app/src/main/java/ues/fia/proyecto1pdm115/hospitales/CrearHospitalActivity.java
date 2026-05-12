package ues.fia.proyecto1pdm115.hospitales;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import ues.fia.proyecto1pdm115.modelos.Departamento;
import ues.fia.proyecto1pdm115.modelos.Especialidad;
import ues.fia.proyecto1pdm115.modelos.Distrito;
import ues.fia.proyecto1pdm115.modelos.Hospital;
import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import androidx.appcompat.app.AppCompatActivity;


import java.util.List;

import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.modelos.Municipio;
import ues.fia.proyecto1pdm115.modelos.Opcion_crud;
import ues.fia.proyecto1pdm115.modelos.Usuario;

public class CrearHospitalActivity extends AppCompatActivity {
    Spinner spinnerHospitalDistritoCrear,spinnerHospitalEspecialidadCrear, spinnerHospitalDepartamentoCrear, spinnerHospitalMunicipioCrear;
    EditText edtNombreHospitalCrear, edtTelefonoHospitalCrear;
    Button btnRegresarHospitalCrear, btnGuardarHospitalCrear;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_hospital);
        helper = new controlDBHospitalApp(this);

        spinnerHospitalDistritoCrear=findViewById(R.id.spinnerHospitalDistritoCrear);
        spinnerHospitalDepartamentoCrear=findViewById(R.id.spinnerHospitalDepartamentoCrear);
        spinnerHospitalMunicipioCrear=findViewById(R.id.spinnerHospitalMunicipioCrear);
        spinnerHospitalEspecialidadCrear=findViewById(R.id.spinnerHospitalEspecialidadCrear);
        edtNombreHospitalCrear=findViewById(R.id.edtNombreHospitalCrear);
        edtTelefonoHospitalCrear=findViewById(R.id.edtTelefonoHospitalCrear);
        btnRegresarHospitalCrear=findViewById(R.id.btnRegresarHospitalCrear);
        btnGuardarHospitalCrear=findViewById(R.id.btnGuardarHospitalCrear);

        cargarDatosSpinners();
        configurarEventosSpinners();
        btnRegresarHospitalCrear.setOnClickListener(v -> finish());
        btnGuardarHospitalCrear.setOnClickListener(v -> guardarHospital());

    }

    private void guardarHospital(){
        Distrito distriSel = (Distrito) spinnerHospitalDistritoCrear.getSelectedItem();
        Especialidad espeSel = (Especialidad) spinnerHospitalEspecialidadCrear.getSelectedItem();
        String nombre = edtNombreHospitalCrear.getText().toString().trim();
        String telefono = edtTelefonoHospitalCrear.getText().toString().trim();

        if (nombre.length() >50){
            Toast.makeText(this,"El nombre del hospital no puede exceder los 50 caracteres",Toast.LENGTH_SHORT).show();
            return;
        }
        if(telefono.length()>8){
            Toast.makeText(
                    this,
                    "Telefono no puede exceder los 8 caracteres",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }
        if(distriSel!=null&&espeSel!=null&& espeSel.getIdEspecialidad()!=-1){
            Hospital hospital=new Hospital();
            hospital.setNombreHospital(nombre);
            hospital.setCodDistrito(distriSel.getCodDistrito());
            hospital.setTelefonoHospital(telefono);

            helper.abrir();
            String mensaje = helper.insertarHospital(hospital,espeSel);
            helper.cerrar();
            Toast.makeText(
                    this,
                    mensaje,
                    Toast.LENGTH_LONG
            ).show();
            limpiarCampos();
        }

    }
    private void cargarDatosSpinners(){
        helper.abrir();
        List<Especialidad> especialidades = helper.consultarEspecialidades();
        List<Departamento> departamentos = helper.consultarDepartamentos();

        especialidades.add(0, new Especialidad(-1, "Seleccione especialidad..."));
        departamentos.add(0, new Departamento("-1", "Seleccione un departamento..."));

        ArrayAdapter<Especialidad> adapterUser = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,especialidades);
        adapterUser.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHospitalEspecialidadCrear.setAdapter(adapterUser);

        ArrayAdapter<Departamento> dptoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,departamentos);
        dptoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHospitalDepartamentoCrear.setAdapter(dptoAdapter);


        helper.cerrar();
    }

    private void limpiarCampos(){
        edtNombreHospitalCrear.setText("");
        edtTelefonoHospitalCrear.setText("");

        spinnerHospitalDepartamentoCrear.setSelection(0);
        spinnerHospitalMunicipioCrear.setAdapter(null);
        spinnerHospitalDistritoCrear.setAdapter(null);
        spinnerHospitalEspecialidadCrear.setSelection(0);
    }
    private void configurarEventosSpinners(){
        spinnerHospitalDepartamentoCrear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Departamento dep = (Departamento) parent.getSelectedItem();
                if(dep!=null){
                    spinnerHospitalMunicipioCrear.setAdapter(null);
                    spinnerHospitalDistritoCrear.setAdapter(null);
                    cargarMunicipios(dep.getCodDepartamento());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerHospitalMunicipioCrear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Municipio muni = (Municipio) parent.getSelectedItem();
                if(muni!=null){
                    spinnerHospitalDistritoCrear.setAdapter(null);
                    cargarDistritos(muni.getCodMunicipio());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void cargarMunicipios(String idDpto){
        helper.abrir();
        List<Municipio> municipios = helper.consultarMunicipios(idDpto);
        ArrayAdapter<Municipio> muniAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,municipios);
        muniAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHospitalMunicipioCrear.setAdapter(muniAdapter);
        helper.cerrar();
    }

    private void cargarDistritos(String idDistrito){
        helper.abrir();
        List<Distrito> distritos = helper.consultarDistritos(idDistrito);
        ArrayAdapter<Distrito> disAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,distritos);
        disAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHospitalDistritoCrear.setAdapter(disAdapter);
        helper.cerrar();
    }
}