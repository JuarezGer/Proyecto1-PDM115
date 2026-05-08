package ues.fia.proyecto1pdm115;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.widget.TextView;

import ues.fia.proyecto1pdm115.pacientes.*;
import ues.fia.proyecto1pdm115.doctores.*;
import ues.fia.proyecto1pdm115.especialidades.*;
import ues.fia.proyecto1pdm115.hospitales.*;
import ues.fia.proyecto1pdm115.establecimientos.*;

public class MainActivity extends AppCompatActivity {

    LinearLayout cardPacientes;
    LinearLayout cardDoctores;
    LinearLayout cardEspecialidades;
    LinearLayout cardHospitales;
    LinearLayout cardEstablecimientos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Navegador.configurarBarra(this);

        TextView txtBienvenida = findViewById(R.id.txtBienvenida);
        String nombreUsuario = getIntent().getStringExtra("nombreUsuario");

        if (nombreUsuario != null && !nombreUsuario.isEmpty()) {
            txtBienvenida.setText("¡Bienvenido " + nombreUsuario + "!");
        }
        //Navegación a Pacientes
        cardPacientes = findViewById(R.id.cardPacientes);
        cardPacientes.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, PacienteMenuActivity.class);
            startActivity(intent);
        });
        //Navegación a Doctores
        cardDoctores = findViewById(R.id.cardDoctores);
        cardDoctores.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, DoctorMenuActivity.class);
            startActivity(intent);
        });
        //Navegación a Especialidades
        cardEspecialidades = findViewById(R.id.cardEspecialidades);
        cardEspecialidades.setOnClickListener(v ->{
            Intent intent = new Intent(MainActivity.this, EspecialidadMenuActivity.class);
            startActivity(intent);
        });
        //Navegación a Hospitales
        cardHospitales = findViewById(R.id.cardHospitales);
        cardHospitales.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, HospitalMenuActivity.class);
                    startActivity(intent);
        });
        //Navegación a Establecimientos
        cardEstablecimientos = findViewById(R.id.cardEstablecimientos);
        cardEstablecimientos.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, EstablecimientoMenuActivity.class);
            startActivity(intent);
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}