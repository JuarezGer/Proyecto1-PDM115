package ues.fia.proyecto1pdm115;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilActivity extends AppCompatActivity {

    TextView txtNombrePerfil, txtIdUsuarioPerfil, txtInfoPerfil;
    Button btnCerrarSesionPerfil;
    LinearLayout navInicio, navPerfil, navSalir;

    String idUsuario;
    String nombreUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        Navegador.configurarBarra(this);

        txtNombrePerfil = findViewById(R.id.txtNombrePerfil);
        txtIdUsuarioPerfil = findViewById(R.id.txtIdUsuarioPerfil);
        txtInfoPerfil = findViewById(R.id.txtInfoPerfil);
        btnCerrarSesionPerfil = findViewById(R.id.btnCerrarSesionPerfil);

        navInicio = findViewById(R.id.navInicio);
        navPerfil = findViewById(R.id.navPerfil);
        navSalir = findViewById(R.id.navSalir);

        cargarDatosUsuario();

        btnCerrarSesionPerfil.setOnClickListener(v -> confirmarCerrarSesion());

        navInicio.setOnClickListener(v -> {
            Intent intent = new Intent(PerfilActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });

        navPerfil.setOnClickListener(v -> {
            Toast.makeText(this, "Ya estás en Perfil", Toast.LENGTH_SHORT).show();
        });

        navSalir.setOnClickListener(v -> confirmarCerrarSesion());
    }

    private void cargarDatosUsuario() {
        SharedPreferences preferences = getSharedPreferences("sesion_usuario", MODE_PRIVATE);

        idUsuario = preferences.getString("idUsuario", "");
        nombreUsuario = preferences.getString("nombreUsuario", "");

        if (nombreUsuario.isEmpty()) {
            nombreUsuario = getIntent().getStringExtra("nombreUsuario");
        }

        if (idUsuario.isEmpty()) {
            idUsuario = getIntent().getStringExtra("idUsuario");
        }

        if (nombreUsuario == null || nombreUsuario.isEmpty()) {
            nombreUsuario = "Usuario";
        }

        if (idUsuario == null || idUsuario.isEmpty()) {
            idUsuario = "No disponible";
        }

        txtNombrePerfil.setText(nombreUsuario);
        txtIdUsuarioPerfil.setText("Usuario: " + idUsuario);
        txtInfoPerfil.setText("Sesión activa en Hospital App.");
    }

    private void confirmarCerrarSesion() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cerrar sesión");
        builder.setMessage("¿Está seguro de cerrar sesión?");

        builder.setPositiveButton("Sí, salir", (dialog, which) -> cerrarSesion());

        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void cerrarSesion() {
        SharedPreferences preferences = getSharedPreferences("sesion_usuario", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
