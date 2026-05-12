package ues.fia.proyecto1pdm115;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText edtUsuarioLogin, edtClaveLogin;
    Button btnIniciarSesion;
    ImageView imgTogglePassword;

    boolean passwordVisible = false;

    controlDBHospitalApp helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        helper = new controlDBHospitalApp(this);

        edtUsuarioLogin = findViewById(R.id.edtUsuarioLogin);
        edtClaveLogin = findViewById(R.id.edtClaveLogin);
        btnIniciarSesion = findViewById(R.id.btnIniciarSesion);
        imgTogglePassword = findViewById(R.id.imgTogglePassword);

        btnIniciarSesion.setOnClickListener(v -> iniciarSesion());

        imgTogglePassword.setOnClickListener(v -> {
            if (passwordVisible) {
                // Ocultar contraseña
                edtClaveLogin.setInputType(
                        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD
                );

                imgTogglePassword.setImageResource(R.drawable.ic_eye);
                imgTogglePassword.setContentDescription("Mostrar contraseña");
                passwordVisible = false;

            } else {
                // Mostrar contraseña
                edtClaveLogin.setInputType(
                        InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                );

                imgTogglePassword.setImageResource(R.drawable.ic_eye_off);
                imgTogglePassword.setContentDescription("Ocultar contraseña");
                passwordVisible = true;
            }

            edtClaveLogin.setSelection(edtClaveLogin.getText().length());
        });
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
        String idUsuarioReal = helper.obtenerIdUsuario(usuario);

        helper.cerrar();

        if (loginValido) {

            if (nombreUsuario == null || nombreUsuario.isEmpty()) {
                nombreUsuario = usuario;
            }

            if (idUsuarioReal == null || idUsuarioReal.isEmpty()) {
                idUsuarioReal = usuario;
            }

            SharedPreferences preferences = getSharedPreferences("sesion_usuario", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();

            editor.putString("idUsuario", idUsuarioReal);
            editor.putString("nombreUsuario", nombreUsuario);
            editor.putBoolean("sesionActiva", true);

            editor.apply();

            Toast.makeText(this, "Bienvenido " + nombreUsuario, Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            intent.putExtra("idUsuario", idUsuarioReal);
            intent.putExtra("nombreUsuario", nombreUsuario);
            startActivity(intent);

            finish();

        } else {
            Toast.makeText(this, "Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
        }
    }
}