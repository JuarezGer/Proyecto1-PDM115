package ues.fia.proyecto1pdm115.permisos;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;

public class PermisosMenuActivity extends AppCompatActivity {

    LinearLayout cardVisualizarPermiso, cardCrearPermiso, cardModificarPermiso, cardEliminarPermiso;
    Button btnRegresarPermiso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permisos_menu);
        EdgeToEdge.enable(this);
        Navegador.configurarBarra(this);

        cardVisualizarPermiso = findViewById(R.id.cardVisualizarPermiso);
        cardCrearPermiso = findViewById(R.id.cardCrearPermiso);
        cardModificarPermiso = findViewById(R.id.cardModificarPermiso);
        cardEliminarPermiso = findViewById(R.id.cardEliminarPermiso);
        btnRegresarPermiso = findViewById(R.id.btnRegresarPermiso);

        btnRegresarPermiso.setOnClickListener(v -> {
            finish();
        });

        //cardVisualizarPermiso.setOnClickListener(v -> {startActivity(new Intent(this, VisualizarUsuarioActivity.class));});

        cardCrearPermiso.setOnClickListener(v -> {
            startActivity(new Intent(this, CrearPermisoActivity.class));
        });

        //cardModificarUsuario.setOnClickListener(v -> {
        //    startActivity(new Intent(this, ModificarUsuarioActivity.class));
        //});

        //cardEliminarUsuario.setOnClickListener(v -> {
        //    startActivity(new Intent(this, EliminarUsuarioActivity.class));
        //});
    }
}