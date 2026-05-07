package ues.fia.proyecto1pdm115;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsuarioLogin, edtClaveLogin;
    Button btnIniciarSesion;

    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        helper = new controlDBHospitalApp(this);

        edtUsuarioLogin = findViewById(R.id.edtUsuarioLogin);
        edtClaveLogin = findViewById(R.id.edtClaveLogin);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);

        btnIniciarSesion.setOnClickListener(v -> iniciarSesion());
    }

    private void iniciarSesion() {
        String usuario = edtUsuarioLogin.getText().toString().trim();
        String clave = edtClaveLogin.getText().toString().trim();

        if (usuario.isEmpty() || clave.isEmpty()) {
            Toast.makeText(this, "Ingrese usuario y contraseña", Toast.LENGTH_SHORT).show();
            return;
        }

        helper.abrir();

        boolean loginValido = helper.validarLogin(usuario, clave);
        String nombreUsuario = helper.obtenerNombreUsuario(usuario);

        helper.cerrar();

        if (loginValido) {

            if (nombreUsuario == null || nombreUsuario.isEmpty()) {
                nombreUsuario = usuario;
            }

            SharedPreferences preferences = getSharedPreferences("sesion_usuario", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("idUsuario", usuario);
            editor.putString("nombreUsuario", nombreUsuario);
            editor.putBoolean("sesionActiva", true);

            editor.apply();

            Toast.makeText(this, "Bienvenido " + nombreUsuario, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("idUsuario", usuario);
            intent.putExtra("nombreUsuario", nombreUsuario);
            startActivity(intent);

            finish();

        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
}