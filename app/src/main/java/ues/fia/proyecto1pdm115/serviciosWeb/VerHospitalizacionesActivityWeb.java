package ues.fia.proyecto1pdm115.serviciosWeb;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ues.fia.proyecto1pdm115.R;

import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONObject;
import ues.fia.proyecto1pdm115.ApiClient;
import ues.fia.proyecto1pdm115.ApiConfig;


public class VerHospitalizacionesActivityWeb extends AppCompatActivity {

    private TextView txtHospitalizaciones;
    private Button btnCargarHospitalizaciones;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_hospitalizaciones_web);

        txtHospitalizaciones = findViewById(R.id.txtHospitalizaciones);
        btnCargarHospitalizaciones = findViewById(R.id.btnCargarHospitalizaciones);

        btnCargarHospitalizaciones.setOnClickListener(v -> cargarHospitalizaciones());
    }

    private void cargarHospitalizaciones() {
        String url = ApiConfig.BASE_URL + "listar_hospitalizaciones.php";
        txtHospitalizaciones.setText("Cargando hospitalizaciones...");

        ApiClient.get(url, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        StringBuilder builder = new StringBuilder();
                        builder.append("LISTADO DE HOSPITALIZACIONES\n\n");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject hosp = data.getJSONObject(i);

                            builder.append("ID Hospitalización: ").append(hosp.optString("ID_HOSPITALIZACION")).append("\n")
                                    .append("ID Consulta: ").append(hosp.optString("ID_CONSULTA")).append("\n")
                                    .append("Fecha Inicio: ").append(hosp.optString("FECHA_INICIO_HOSP")).append("\n")
                                    .append("Fecha Fin: ").append(hosp.optString("FECHA_FIN_HOSP")).append("\n")
                                    .append("Motivo: ").append(hosp.optString("MOTIVO_INGRESO")).append("\n")
                                    .append("Costo: $").append(hosp.optString("COSTO_HOSPITALIZACION")).append("\n")
                                    .append("----------------------------\n");
                        }
                        txtHospitalizaciones.setText(builder.toString());
                    } else {
                        txtHospitalizaciones.setText(jsonObject.optString("message", "No se encontraron registros."));
                    }
                } catch (Exception e) {
                    Toast.makeText(VerHospitalizacionesActivityWeb.this, "Error al procesar JSON: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    txtHospitalizaciones.setText("Respuesta recibida:\n\n" + response);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(VerHospitalizacionesActivityWeb.this, "Error de conexión: " + error, Toast.LENGTH_LONG).show();
                txtHospitalizaciones.setText("Error de conexión:\n" + error);
            }
        });
    }
}