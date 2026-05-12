package ues.fia.proyecto1pdm115.hospitales;

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
import ues.fia.proyecto1pdm115.modelos.Especialidad;
import ues.fia.proyecto1pdm115.modelos.Hospital;
import ues.fia.proyecto1pdm115.modelos.Posee;
import ues.fia.proyecto1pdm115.modelos.Puede_elegir;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.modelos.Usuario;

import ues.fia.proyecto1pdm115.R;

public class EliminarPoseeActivity extends AppCompatActivity {
    Spinner spinnerHospitalRelacionEliminar,spinnerEspecialidadRelacionEliminar;
    Button btnBuscarEspecialidadRelacionEliminar,btnRegresarEliminarEspecialidadRelacion,btnEliminarEspecialidadRelacion;
    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_eliminar_posee);
        helper = new controlDBHospitalApp(this);

        spinnerEspecialidadRelacionEliminar=findViewById(R.id.spinnerEspecialidadRelacionEliminar);
        spinnerHospitalRelacionEliminar=findViewById(R.id.spinnerHospitalRelacionEliminar);
        btnBuscarEspecialidadRelacionEliminar=findViewById(R.id.btnBuscarEspecialidadRelacionEliminar);
        btnRegresarEliminarEspecialidadRelacion=findViewById(R.id.btnRegresarEliminarEspecialidadRelacion);
        btnEliminarEspecialidadRelacion=findViewById(R.id.btnEliminarEspecialidadRelacion);
        cargarDatosSpinners();
        btnRegresarEliminarEspecialidadRelacion.setOnClickListener(v -> finish());
        btnBuscarEspecialidadRelacionEliminar.setOnClickListener(v -> buscarPermisos());
        btnEliminarEspecialidadRelacion.setOnClickListener(v -> confirmarEliminacion());

    }

    private void cargarDatosSpinners(){
        helper.abrir();
        List<Hospital> hospitales = helper.consultarHospitales();

        hospitales.add(0, new Hospital(-1, "Seleccione un hospital..."));

        ArrayAdapter<Hospital> adapterHospi = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,hospitales);
        adapterHospi.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerHospitalRelacionEliminar.setAdapter(adapterHospi);
        helper.cerrar();
    }

    private void buscarPermisos() {
        Hospital hospiSel = (Hospital) spinnerHospitalRelacionEliminar.getSelectedItem();

        if (hospiSel != null && hospiSel.getIdHospital() != -1) {
            helper.abrir();

            ArrayList<Especialidad> lista = helper.consultarEspecialidadesPorHospital(hospiSel.getIdHospital());
            helper.cerrar();

            if (lista.isEmpty()) {
                spinnerEspecialidadRelacionEliminar.setAdapter(null);
                Toast.makeText(this, "El hospital no tiene especialidades asignadas", Toast.LENGTH_LONG).show();
                return;
            }

            // Agregamos el placeholder (ID -1)
            lista.add(0, new Especialidad(-1, "Seleccione especialidad a eliminar..."));

            ArrayAdapter<Especialidad> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_item, lista);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            spinnerEspecialidadRelacionEliminar.setAdapter(adapter);
        }
    }

    private void confirmarEliminacion() {
        Hospital hospiSel = (Hospital) spinnerHospitalRelacionEliminar.getSelectedItem();
        // Obtenemos el objeto Especialidad del spinner
        Especialidad espeSel = (Especialidad) spinnerEspecialidadRelacionEliminar.getSelectedItem();

        // Validaciones de seguridad
        if (hospiSel == null || hospiSel.getIdHospital() == -1) {
            Toast.makeText(this, "Seleccione un hospital válido", Toast.LENGTH_SHORT).show();
            return;
        }

        if (espeSel == null || espeSel.getIdEspecialidad() == -1) {
            Toast.makeText(this, "Seleccione una especialidad válida", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setTitle("Confirmar eliminación")
                .setMessage("¿Desea quitar la especialidad '" + espeSel.getNombreEspecialidad() +
                        "' del hospital '" + hospiSel.getNombreHospital() + "'?")
                .setPositiveButton("Sí, eliminar", (dialog, which) -> {
                    eliminarPosee(hospiSel.getIdHospital(), espeSel.getIdEspecialidad());
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void eliminarPosee(Integer idHospi, Integer idEspe) {

        helper.abrir();

        String mensaje =
                helper.eliminarPosee(
                        idHospi,idEspe
                );

        helper.cerrar();

        Toast.makeText(
                this,
                mensaje,
                Toast.LENGTH_LONG
        ).show();

        buscarPermisos();
    }
}