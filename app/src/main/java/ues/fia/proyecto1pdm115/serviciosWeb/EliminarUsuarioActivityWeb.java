package ues.fia.proyecto1pdm115.serviciosWeb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONObject;

import ues.fia.proyecto1pdm115.ApiClient;
import ues.fia.proyecto1pdm115.ApiConfig;
import ues.fia.proyecto1pdm115.R;

public class EliminarUsuarioActivityWeb extends AppCompatActivity {

    private EditText edtIdUsuarioEliminarWeb;
    private Button btnBuscarUsuarioEliminarWeb, btnEliminarUsuarioWeb, btnRegresarEliminarUsuarioWeb;
    private TextView txtUsuarioEliminarWeb;
    private String idEncontrado = "NO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_usuario_web);

        edtIdUsuarioEliminarWeb = findViewById(R.id.edtIdUsuarioEliminarWeb);
        btnBuscarUsuarioEliminarWeb = findViewById(R.id.btnBuscarUsuarioEliminarWeb);
        btnEliminarUsuarioWeb = findViewById(R.id.btnEliminarUsuarioWeb);
        btnRegresarEliminarUsuarioWeb = findViewById(R.id.btnRegresarEliminarUsuarioWeb);
        txtUsuarioEliminarWeb = findViewById(R.id.txtUsuarioEliminarWeb);

        btnBuscarUsuarioEliminarWeb.setOnClickListener(v -> buscarUsuarioWeb());
        btnEliminarUsuarioWeb.setOnClickListener(v -> confirmarEliminacion());
        btnRegresarEliminarUsuarioWeb.setOnClickListener(v -> finish());
    }

    private void buscarUsuarioWeb() {
        String id = edtIdUsuarioEliminarWeb.getText().toString().trim();

        if (id.isEmpty()) {
            Toast.makeText(this, "Ingrese el ID", Toast.LENGTH_SHORT).show();
            return;
        }

        String postData = "IDUSUARIO=" + id;

        ApiClient.post(ApiConfig.GET_USER, postData, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        JSONObject data = jsonObject.getJSONObject("data");
                        idEncontrado = data.getString("IDUSUARIO");
                        String nombre = data.getString("NOMUSUARIO");

                        String info = "ID: " + idEncontrado + "\n\nNombre: " + nombre;
                        txtUsuarioEliminarWeb.setText(info);
                        Toast.makeText(EliminarUsuarioActivityWeb.this, "Usuario encontrado", Toast.LENGTH_SHORT).show();
                    } else {
                        idEncontrado = "NO";
                        txtUsuarioEliminarWeb.setText("Usuario no encontrado.");
                        Toast.makeText(EliminarUsuarioActivityWeb.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(EliminarUsuarioActivityWeb.this, "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(EliminarUsuarioActivityWeb.this, "Error de red: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void confirmarEliminacion() {
        if (idEncontrado.equals("NO")) {
            Toast.makeText(this, "Primero busque un usuario válido", Toast.LENGTH_SHORT).show();
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirmar eliminación");
        builder.setMessage("¿Desea eliminar este usuario del servidor?");
        builder.setPositiveButton("Sí, eliminar", (dialog, which) -> eliminarUsuarioWeb());
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }

    private void eliminarUsuarioWeb() {
        String postData = "IDUSUARIO=" + idEncontrado;

        ApiClient.post(ApiConfig.DELETE_USER, postData, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    String mensaje = jsonObject.getString("message");

                    Toast.makeText(EliminarUsuarioActivityWeb.this, mensaje, Toast.LENGTH_LONG).show();

                    if (success) {
                        limpiarCampos();
                    }
                } catch (Exception e) {
                    Toast.makeText(EliminarUsuarioActivityWeb.this, "Error al procesar respuesta", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(EliminarUsuarioActivityWeb.this, "Error de red: " + error, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void limpiarCampos() {
        edtIdUsuarioEliminarWeb.setText("");
        txtUsuarioEliminarWeb.setText("Datos de usuario...");
        idEncontrado = "NO";
    }
}
