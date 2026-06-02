package ues.fia.proyecto1pdm115.serviciosWeb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import ues.fia.proyecto1pdm115.*;

public class VerPacientesActivityWeb extends AppCompatActivity {

    private TextView txtPacientes;
    private Button btnCargarPacientes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_pacientes_web);

        txtPacientes = findViewById(R.id.txtPacientes);
        btnCargarPacientes = findViewById(R.id.btnCargarPacientes);

        btnCargarPacientes.setOnClickListener(v -> cargarPacientes());
    }

    private void cargarPacientes() {
        String url = ApiConfig.BASE_URL + "listar_pacientes.php";

        ApiClient.get(url, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        JSONArray data = jsonObject.getJSONArray("data");

                        StringBuilder builder = new StringBuilder();

                        for (int i = 0; i < data.length(); i++) {
                            JSONObject paciente = data.getJSONObject(i);

                            builder.append("DUI: ")
                                    .append(paciente.getString("DUI_PACIENTE"))
                                    .append("\n");

                            builder.append("Nombre: ")
                                    .append(paciente.getString("PRIMER_NOMBRE_PACIENTE"))
                                    .append(" ")
                                    .append(paciente.optString("SEGUNDO_NOMBRE_PACIENTE", ""))
                                    .append(" ")
                                    .append(paciente.getString("PRIMER_APELLIDO_PACIENTE"))
                                    .append(" ")
                                    .append(paciente.optString("SEGUNDO_APELLIDO_PACIENTE", ""))
                                    .append("\n");

                            builder.append("Género: ")
                                    .append(paciente.getString("GENERO_PACIENTE"))
                                    .append("\n");

                            builder.append("Teléfono: ")
                                    .append(paciente.getString("TELEFONO_PACIENTE"))
                                    .append("\n");

                            builder.append("----------------------------\n");
                        }

                        txtPacientes.setText(builder.toString());
                    } else {
                        txtPacientes.setText("No se pudieron cargar los pacientes.");
                    }

                } catch (Exception e) {
                    Toast.makeText(
                            VerPacientesActivityWeb.this,
                            "Error al procesar JSON: " + e.getMessage(),
                            Toast.LENGTH_LONG
                    ).show();
                }
            }

            @Override
            public void onError(String error) {
                Toast.makeText(
                        VerPacientesActivityWeb.this,
                        "Error de conexión: " + error,
                        Toast.LENGTH_LONG
                ).show();
            }
        });
    }
}
