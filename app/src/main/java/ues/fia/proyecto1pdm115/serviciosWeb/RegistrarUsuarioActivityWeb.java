// Autor: gm21007
package ues.fia.proyecto1pdm115.serviciosWeb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import ues.fia.proyecto1pdm115.ApiClient;
import ues.fia.proyecto1pdm115.ApiConfig;
import ues.fia.proyecto1pdm115.R;

public class RegistrarUsuarioActivityWeb extends AppCompatActivity {

    private EditText edtUsuarioIdWeb, edtNombreUsuarioWeb, edtClaveUsuarioWeb;
    private Button btnGuardarUsuarioWeb, btnRegresarUsuarioWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_usuario_web);

        edtUsuarioIdWeb = findViewById(R.id.edtUsuarioIdWeb);
        edtNombreUsuarioWeb = findViewById(R.id.edtNombreUsuarioWeb);
        edtClaveUsuarioWeb = findViewById(R.id.edtClaveUsuarioWeb);
        btnGuardarUsuarioWeb = findViewById(R.id.btnGuardarUsuarioWeb);
        btnRegresarUsuarioWeb = findViewById(R.id.btnRegresarUsuarioWeb);

        btnGuardarUsuarioWeb.setOnClickListener(v -> registrarUsuarioWeb());
        btnRegresarUsuarioWeb.setOnClickListener(v -> finish());
    }

    private void registrarUsuarioWeb() {
        String id = edtUsuarioIdWeb.getText().toString().trim();
        String nombre = edtNombreUsuarioWeb.getText().toString().trim();
        String clave = edtClaveUsuarioWeb.getText().toString().trim();

        if (id.isEmpty()) {
            Toast.makeText(this, "Ingrese id de usuario", Toast.LENGTH_SHORT).show();
            return;
        }
        if (id.length() > 2) {
            Toast.makeText(this, "Máximo 2 caracteres para id EJ: U1", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nombre.isEmpty()) {
            Toast.makeText(this, "Ingrese nombre de usuario", Toast.LENGTH_SHORT).show();
            return;
        }
        if (nombre.length() > 20) {
            Toast.makeText(this, "El nombre de usuario no puede exceder los 20 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }
        if (clave.isEmpty()) {
            Toast.makeText(this, "Ingrese clave para usuario", Toast.LENGTH_SHORT).show();
            return;
        }

        String postData = "IDUSUARIO=" + id + "&NOMUSUARIO=" + nombre + "&CLAVE=" + clave;

        ApiClient.post(ApiConfig.REGISTER_USER, postData, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String mensaje = jsonObject.getString("message");

                    Toast.makeText(RegistrarUsuarioActivityWeb.this, mensaje, Toast.LENGTH_LONG).show();

                    if (success) {
                        limpiarCampos();
                    }
                } catch (Exception e) {
                    Toast.makeText(RegistrarUsuarioActivityWeb.this, "Error al procesar respuesta: " + response, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(RegistrarUsuarioActivityWeb.this, "Error de red: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void limpiarCampos() {
        edtUsuarioIdWeb.setText("");
        edtNombreUsuarioWeb.setText("");
        edtClaveUsuarioWeb.setText("");
    }
}
