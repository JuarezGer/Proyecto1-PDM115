package ues.fia.proyecto1pdm115.serviciosWeb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import ues.fia.proyecto1pdm115.ApiClient;
import ues.fia.proyecto1pdm115.ApiConfig;
import ues.fia.proyecto1pdm115.R;

public class VerEstablecimientosActivityWeb extends AppCompatActivity {

    private TextView txtEstablecimientos;
    private Button btnCargarEstablecimientos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_establecimientos_web);

        txtEstablecimientos = findViewById(R.id.txtEstablecimientos);
        btnCargarEstablecimientos = findViewById(R.id.btnCargarEstablecimientos);

        btnCargarEstablecimientos.setOnClickListener(v -> cargarEstablecimientos());
    }

    private void cargarEstablecimientos() {
        String url = ApiConfig.BASE_URL + "listar_establecimientos.php";

        txtEstablecimientos.setText("Cargando establecimientos...");

        ApiClient.get(url, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        JSONArray data = jsonObject.getJSONArray("data");

                        StringBuilder builder = new StringBuilder();

                        builder.append("LISTADO DE ESTABLECIMIENTOS\n\n");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject establecimiento = data.getJSONObject(i);

                            builder.append("ID: ")
                                    .append(establecimiento.optString("ID_ESTABLECIMIENTO"))
                                    .append("\n");

                            builder.append("Nombre: ")
                                    .append(establecimiento.optString("NOMBRE_ESTABLECIMIENTO"))
                                    .append("\n");

                            builder.append("Teléfono: ")
                                    .append(establecimiento.optString("TELEFONO_ESTABLECIMIENTO"))
                                    .append("\n");

                            builder.append("Dirección: ")
                                    .append(establecimiento.optString("DIRECCION_ESTABLECIMIENTO"))
                                    .append("\n");

                            builder.append("----------------------------\n");
                        }

                        txtEstablecimientos.setText(builder.toString());

                    } else {
                        String mensaje = jsonObject.optString(
                                "message",
                                "No se pudieron cargar los establecimientos."
                        );

                        txtEstablecimientos.setText(mensaje);
                    }

                } catch (Exception e) {
                    Toast.makeText(
                            VerEstablecimientosActivityWeb.this,
                            "Error al procesar JSON: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();

                    txtEstablecimientos.setText("Respuesta recibida:\n\n" + response);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(
                        VerEstablecimientosActivityWeb.this,
                        "Error de conexión: " + error,
                        Toast.LENGTH_LONG
                ).show();

                txtEstablecimientos.setText("Error de conexión:\n" + error);
            }
        });
    }
}
