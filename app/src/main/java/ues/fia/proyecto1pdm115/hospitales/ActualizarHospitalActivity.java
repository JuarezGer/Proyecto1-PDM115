package ues.fia.proyecto1pdm115.hospitales;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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

public class ActualizarHospitalActivity extends AppCompatActivity {
    Spinner spinnerHospitalDepartamentoActualizar,spinnerHospitalMunicipioActualizar,spinnerHospitalDistritoActualizar;
    EditText edtNombreHospitalActualizar,edtTelefonoHospitalActualizar,edtIdHospitalModificar;
    Button btnRegresarHospitalActualizar,btnGuardarHospitalActualizar,btnBuscarModificarHospital;
    TextView tvMostrarDistritoActual;
    int idHospitalEncontrado = -1;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_hospital);

        spinnerHospitalDepartamentoActualizar=findViewById(R.id.spinnerHospitalDepartamentoActualizar);
        spinnerHospitalDistritoActualizar=findViewById(R.id.spinnerHospitalDistritoActualizar);
        spinnerHospitalMunicipioActualizar=findViewById(R.id.spinnerHospitalMunicipioActualizar);
        edtNombreHospitalActualizar=findViewById(R.id.edtNombreHospitalActualizar);
        edtTelefonoHospitalActualizar=findViewById(R.id.edtTelefonoHospitalActualizar);
        edtIdHospitalModificar=findViewById(R.id.edtIdHospitalModificar);
        btnRegresarHospitalActualizar=findViewById(R.id.btnRegresarHospitalActualizar);
        btnGuardarHospitalActualizar=findViewById(R.id.btnGuardarHospitalActualizar);
        btnBuscarModificarHospital=findViewById(R.id.btnBuscarModificarHospital);
        tvMostrarDistritoActual=findViewById(R.id.tvMostrarDistritoActual);
        helper = new controlDBHospitalApp(this);
        cargarDatosSpinners();
        configurarEventosSpinners();
        btnRegresarHospitalActualizar.setOnClickListener(v -> finish());
        btnBuscarModificarHospital.setOnClickListener(v -> buscarHospital());
        btnGuardarHospitalActualizar.setOnClickListener(v -> guardarHospital());

    }

    private void cargarDatosSpinners(){
        helper.abrir();
        List<Departamento> departamentos = helper.consultarDepartamentos();

        departamentos.add(0, new Departamento("-1", "Seleccione un departamento..."));

        ArrayAdapter<Departamento> dptoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,departamentos);
        dptoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHospitalDepartamentoActualizar.setAdapter(dptoAdapter);


        helper.cerrar();
    }

    private void guardarHospital(){
        Distrito distriSel = (Distrito) spinnerHospitalDistritoActualizar.getSelectedItem();
        String nombre = edtNombreHospitalActualizar.getText().toString().trim();
        String telefono = edtTelefonoHospitalActualizar.getText().toString().trim();

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
        if(distriSel!=null){
            Hospital hospital=new Hospital();
            hospital.setIdHospital(idHospitalEncontrado);
            hospital.setNombreHospital(nombre);
            hospital.setCodDistrito(distriSel.getCodDistrito());
            hospital.setTelefonoHospital(telefono);

            helper.abrir();
            String mensaje = helper.actualizarHospital(hospital);
            helper.cerrar();
            Toast.makeText(
                    this,
                    mensaje,
                    Toast.LENGTH_LONG
            ).show();
            limpiarCampos();
            edtIdHospitalModificar.setEnabled(true);
        }

    }

    private void buscarHospital() {

        String textoId =
                edtIdHospitalModificar
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

        int idHospital =
                Integer.parseInt(textoId);

        helper.abrir();

        Hospital hospital =
                helper.consultarHospital(
                        idHospital
                );

        helper.cerrar();

        if (hospital != null) {

            edtNombreHospitalActualizar.setText(valor(hospital.getNombreHospital()));
            tvMostrarDistritoActual.setText("El hospital se encuentra actualmente en: "+valor(hospital.getCodDistrito()));
            edtTelefonoHospitalActualizar.setText(valor(hospital.getTelefonoHospital()));

            Toast.makeText(
                    this,
                    "Hospital encontrado",
                    Toast.LENGTH_SHORT
            ).show();
            edtIdHospitalModificar.setEnabled(false);
            idHospitalEncontrado=hospital.getIdHospital();

        } else {

            limpiarCamposSinId();

            Toast.makeText(
                    this,
                    "Hospital no encontrado",
                    Toast.LENGTH_SHORT
            ).show();
        }
    }

    private void limpiarCampos(){
        edtNombreHospitalActualizar.setText("");
        edtTelefonoHospitalActualizar.setText("");
        edtIdHospitalModificar.setText("");
        tvMostrarDistritoActual.setText("");

        spinnerHospitalDepartamentoActualizar.setSelection(0);
        spinnerHospitalMunicipioActualizar.setAdapter(null);
        spinnerHospitalDistritoActualizar.setAdapter(null);
    }

    private void configurarEventosSpinners(){
        spinnerHospitalDepartamentoActualizar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Departamento dep = (Departamento) parent.getSelectedItem();
                if(dep!=null && !dep.getCodDepartamento().equals("-1")){
                    spinnerHospitalMunicipioActualizar.setAdapter(null);
                    spinnerHospitalDistritoActualizar.setAdapter(null);
                    cargarMunicipios(dep.getCodDepartamento());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerHospitalMunicipioActualizar.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Municipio muni = (Municipio) parent.getSelectedItem();
                if(muni!=null){
                    spinnerHospitalDistritoActualizar.setAdapter(null);
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
        spinnerHospitalMunicipioActualizar.setAdapter(muniAdapter);
        helper.cerrar();
    }

    private void cargarDistritos(String idDistrito){
        helper.abrir();
        List<Distrito> distritos = helper.consultarDistritos(idDistrito);
        ArrayAdapter<Distrito> disAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,distritos);
        disAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHospitalDistritoActualizar.setAdapter(disAdapter);
        helper.cerrar();
    }

    private void limpiarCamposSinId() {

        edtTelefonoHospitalActualizar
                .setText("");
        edtNombreHospitalActualizar.setText("");
    }
    private String valor(String texto) {

        if (texto == null) {

            return "";
        }

        return texto;
    }
}