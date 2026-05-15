package ues.fia.proyecto1pdm115.doctores;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

import ues.fia.proyecto1pdm115.controlDBHospitalApp;
import ues.fia.proyecto1pdm115.modelos.Doctor;
import ues.fia.proyecto1pdm115.modelos.Especialidad;
import ues.fia.proyecto1pdm115.R;

public class EliminarCuentaConActivity extends AppCompatActivity {
    Spinner spinnerDoctorRelacionEliminar,spinnerEspecialidadDoctorRelacionEliminar;
    Button btnBuscarEspecialidadDoctorRelacionEliminar,btnRegresarEliminarEspecialidadDoctorRelacion,btnEliminarEspecialidadDoctorRelacion;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eliminar_cuenta_con);

        helper=new controlDBHospitalApp(this);
        spinnerDoctorRelacionEliminar=findViewById(R.id.spinnerDoctorRelacionEliminar);
        spinnerEspecialidadDoctorRelacionEliminar=findViewById(R.id.spinnerEspecialidadDoctorRelacionEliminar);
        btnBuscarEspecialidadDoctorRelacionEliminar=findViewById(R.id.btnBuscarEspecialidadDoctorRelacionEliminar);
        btnRegresarEliminarEspecialidadDoctorRelacion=findViewById(R.id.btnRegresarEliminarEspecialidadDoctorRelacion);
        btnEliminarEspecialidadDoctorRelacion=findViewById(R.id.btnEliminarEspecialidadDoctorRelacion);

        cargarDatosSpinners();
        btnRegresarEliminarEspecialidadDoctorRelacion.setOnClickListener(v -> finish());
        btnBuscarEspecialidadDoctorRelacionEliminar.setOnClickListener(v -> buscarEspecialidades());
        btnEliminarEspecialidadDoctorRelacion.setOnClickListener(v -> confirmarEliminacion());
    }

    private void confirmarEliminacion() {
        Doctor docSel = (Doctor) spinnerDoctorRelacionEliminar.getSelectedItem();
        // Obtenemos el objeto Especialidad del spinner
        Especialidad espeSel = (Especialidad) spinnerEspecialidadDoctorRelacionEliminar.getSelectedItem();

        // Validaciones de seguridad
        if (docSel == null || docSel.getDuiDoctor().equals("1")) {
            Toast.makeText(this, "Seleccione un doctor válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (espeSel == null || espeSel.getIdEspecialidad() == -1) {
            Toast.makeText(this, "Seleccione una especialidad válida", Toast.LENGTH_SHORT).show();
            return;
        }

        /*En el llenado de la base de datos el id de la especialidad general es siempre 1
         y todo doctor debe tener la especialidad general asignada asique se previene su eliminacion */
        if (espeSel.getIdEspecialidad()==1){
            Toast.makeText(this, "NO SE PUEDE ELIMINAR LA ESPECIADLIDAD GENERAL PARA DOCTORES", Toast.LENGTH_LONG).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Desea quitar la especialidad '" + espeSel.getNombreEspecialidad() +
                        "'\n del doctor '" +docSel.getDuiDoctor()+" "+ docSel.getNombreDoctor() + "'?")
                .setPositiveButton("Sí, eliminar", (dialog, which) -> {
                    eliminarCuentaCon(docSel.getDuiDoctor(), espeSel.getIdEspecialidad());
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarCuentaCon(String dui, Integer idEspe) {

        helper.abrir();

        String mensaje =
                helper.eliminarCuentaCon(
                        dui,idEspe
                );

        helper.cerrar();

        Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_LONG
        ).show();

        buscarEspecialidades();
    }

    private void cargarDatosSpinners(){
        helper.abrir();
        List<Doctor> doctores = helper.consultarDoctores();

        doctores.add(0, new Doctor("1", "Seleccione un doctor","..."));

        ArrayAdapter<Doctor> adapterDoc = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,doctores);
        adapterDoc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDoctorRelacionEliminar.setAdapter(adapterDoc);
        helper.cerrar();
    }

    private void buscarEspecialidades() {
        Doctor docSel = (Doctor) spinnerDoctorRelacionEliminar.getSelectedItem();

        if (docSel != null && !docSel.getDuiDoctor().equals("1")) {
            helper.abrir();

            ArrayList<Especialidad> lista = helper.consultarEspecialidadesPorDoctor(docSel.getDuiDoctor());
            helper.cerrar();



            // Agregamos el placeholder (ID -1)
            lista.add(0, new Especialidad(-1, "Seleccione especialidad a eliminar..."));

            ArrayAdapter<Especialidad> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, lista);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerEspecialidadDoctorRelacionEliminar.setAdapter(adapter);
        }
    }
}