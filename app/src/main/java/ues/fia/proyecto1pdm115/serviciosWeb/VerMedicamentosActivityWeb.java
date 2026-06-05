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


public class VerMedicamentosActivityWeb extends AppCompatActivity {

    private TextView txtMedicamentos;
    private Button btnCargarMedicamentos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_medicamentos_web);

        txtMedicamentos = findViewById(R.id.txtMedicamentos);
        btnCargarMedicamentos = findViewById(R.id.btnCargarMedicamentos);

        btnCargarMedicamentos.setOnClickListener(v -> cargarMedicamentos());
    }

    private void cargarMedicamentos() {
        String url = ApiConfig.BASE_URL + "listar_medicamentos.php";
        txtMedicamentos.setText("Cargando medicamentos...");

        ApiClient.get(url, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        StringBuilder builder = new StringBuilder();
                        builder.append("INVENTARIO DE MEDICAMENTOS\n\n");

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject med = data.getJSONObject(i);

                            builder.append("Código: ").append(med.optString("COD_MEDICAMENTO")).append("\n")
                                    .append("Medicamento: ").append(med.optString("NOMBRE_MEDICAMENTO")).append("\n")
                                    .append("Vencimiento: ").append(med.optString("FECHA_VENCIMIENTO")).append("\n")
                                    .append("Stock Disponible: ").append(med.optString("CANTIDAD_INVENTARIO")).append(" unidades\n")
                                    .append("Precio: $").append(med.optString("PRECIO_VENTA")).append("\n")
                                    .append("Lote: ").append(med.optString("LOTE")).append("\n")
                                    .append("----------------------------\n");
                        }
                        txtMedicamentos.setText(builder.toString());
                    } else {
                        txtMedicamentos.setText(jsonObject.optString("message", "No se encontraron medicamentos."));
                    }
                } catch (Exception e) {
                    Toast.makeText(VerMedicamentosActivityWeb.this, "Error al procesar JSON: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    txtMedicamentos.setText("Respuesta recibida:\n\n" + response);
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(VerMedicamentosActivityWeb.this, "Error de conexión: " + error, Toast.LENGTH_LONG).show();
                txtMedicamentos.setText("Error de conexión:\n" + error);
            }
        });
    }
}