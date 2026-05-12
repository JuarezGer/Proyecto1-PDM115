package ues.fia.proyecto1pdm115.usuarios;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import ues.fia.proyecto1pdm115.Navegador;
import ues.fia.proyecto1pdm115.R;

public class UsuariosMenuActivity extends AppCompatActivity {

    LinearLayout cardVisualizarUsuario, cardCrearUsuario, cardModificarUsuario, cardEliminarUsuario;
    Button btnRegresarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuarios_menu);
        EdgeToEdge.enable(this);
        Navegador.configurarBarra(this);

        cardVisualizarUsuario = findViewById(R.id.cardVisualizarUsuario);
        cardCrearUsuario = findViewById(R.id.cardCrearUsuario);
        cardModificarUsuario = findViewById(R.id.cardModificarUsuario);
        cardEliminarUsuario = findViewById(R.id.cardEliminarUsuario);
        btnRegresarUsuario = findViewById(R.id.btnRegresarUsuario);

        btnRegresarUsuario.setOnClickListener(v -> {
            finish();
        });

        cardVisualizarUsuario.setOnClickListener(v -> {startActivity(new Intent(this, VisualizarUsuarioActivity.class));});

        cardCrearUsuario.setOnClickListener(v -> {
            startActivity(new Intent(this, CrearUsuarioActivity.class));
        });

        cardModificarUsuario.setOnClickListener(v -> {
            startActivity(new Intent(this, ModificarUsuarioActivity.class));
        });

        cardEliminarUsuario.setOnClickListener(v -> {
            startActivity(new Intent(this, EliminarUsuarioActivity.class));
        });
    }
}