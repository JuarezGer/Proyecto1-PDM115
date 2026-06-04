package ues.fia.proyecto1pdm115.serviciosWeb;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.R;

public class ServiciosWebMenuActivity extends AppCompatActivity {

    private Button btnVerPacientes;
    private Button btnVerEstablecimientos;
    private Button btnCrearUsuario;
    private Button btnEliminarUsuario;
    private Button btnCrearPermiso;
    private Button btnEliminarPermiso;
    private Button btnVerHospitalizaciones;
    private Button btnVerMedicamentos;
    private Button btnCrearEstablecimiento;
    private Button btnModificarEstablecimiento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_servicios_web_menu);

        btnVerPacientes = findViewById(R.id.btnVerPacientesWeb);
        btnVerEstablecimientos = findViewById(R.id.btnVerEstablecimientosWeb);
        btnCrearUsuario = findViewById(R.id.btnCrearUsuarioWeb);
        btnEliminarUsuario = findViewById(R.id.btnEliminarUsuarioWeb);
        btnCrearPermiso = findViewById(R.id.btnCrearPermisoWeb);
        btnEliminarPermiso = findViewById(R.id.btnEliminarPermisoWeb);
        btnVerHospitalizaciones = findViewById(R.id.btnVerHospitalizacionesWeb);
        btnVerMedicamentos = findViewById(R.id.btnVerMedicamentosWeb);
        btnModificarEstablecimiento = findViewById(R.id.btnModificarEstablecimientoWeb);

        btnVerPacientes.setOnClickListener(v ->
                startActivity(new Intent(this, VerPacientesActivityWeb.class))
        );

        btnVerEstablecimientos.setOnClickListener(v ->
                startActivity(new Intent(this, VerEstablecimientosActivityWeb.class))
        );

        btnCrearUsuario.setOnClickListener(v ->
                startActivity(new Intent(this, RegistrarUsuarioActivityWeb.class))
        );

        btnEliminarUsuario.setOnClickListener(v ->
                startActivity(new Intent(this, EliminarUsuarioActivityWeb.class))
        );
        /*
        btnCrearPermiso.setOnClickListener(v ->
                startActivity(new Intent(this, CrearPermisoActivityWeb.class))
        );

        btnEliminarPermiso.setOnClickListener(v ->
                startActivity(new Intent(this, EliminarPermisoActivityWeb.class))
        );

        btnVerHospitalizaciones.setOnClickListener(v ->
                startActivity(new Intent(this, VerHospitalizacionesActivityWeb.class))
        );

        btnVerMedicamentos.setOnClickListener(v ->
                startActivity(new Intent(this, VerMedicamentosActivityWeb.class))
        );*/

        btnModificarEstablecimiento.setOnClickListener(v ->
                startActivity(new Intent(this, ModificarEstablecimientoActivityWeb.class))
        );
    }
}