package ues.fia.proyecto1pdm115;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;

import ues.fia.proyecto1pdm115.pacientes.*;
import ues.fia.proyecto1pdm115.doctores.*;

public class MainActivity extends AppCompatActivity {

    LinearLayout cardPacientes;
    LinearLayout cardDoctores;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
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

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}