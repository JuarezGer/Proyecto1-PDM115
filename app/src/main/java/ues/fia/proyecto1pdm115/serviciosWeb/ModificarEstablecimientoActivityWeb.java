package ues.fia.proyecto1pdm115.serviciosWeb;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

import ues.fia.proyecto1pdm115.ApiClient;
import ues.fia.proyecto1pdm115.ApiConfig;
import ues.fia.proyecto1pdm115.R;

public class ModificarEstablecimientoActivityWeb extends AppCompatActivity {

    private Spinner spinnerEstablecimientos;
    private EditText edtId, edtNombre, edtTelefono, edtDireccion;
    private Button btnModificar, btnRecargar;
    private TextView txtResultado;

    private final ArrayList<EstablecimientoItem> listaEstablecimientos = new ArrayList<>();
    private EstablecimientoItem establecimientoSeleccionado = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar_establecimiento_web);

        spinnerEstablecimientos = findViewById(R.id.spinnerEstablecimientos);
        edtId = findViewById(R.id.edtIdEstablecimiento);
        edtNombre = findViewById(R.id.edtNombreEstablecimiento);
        edtTelefono = findViewById(R.id.edtTelefonoEstablecimiento);
        edtDireccion = findViewById(R.id.edtDireccionEstablecimiento);
        btnModificar = findViewById(R.id.btnModificarEstablecimiento);
        btnRecargar = findViewById(R.id.btnRecargarEstablecimientos);
        txtResultado = findViewById(R.id.txtResultadoModificar);

        edtId.setEnabled(false);

        btnModificar.setOnClickListener(v -> modificarEstablecimiento());
        btnRecargar.setOnClickListener(v -> cargarEstablecimientos());

        cargarEstablecimientos();
    }

    private void cargarEstablecimientos() {
        String url = ApiConfig.BASE_URL + "listar_establecimientos.php";

        txtResultado.setText("Cargando establecimientos...");
        listaEstablecimientos.clear();

        ApiClient.get(url, new ApiClient.ApiCallback() {
            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject json = new JSONObject(response);
                    boolean success = json.getBoolean("success");

                    if (!success) {
                        txtResultado.setText(json.optString("message", "No se pudieron cargar los establecimientos."));
                        return;
                    }

                    JSONArray data = json.getJSONArray("data");

                    for (int i = 0; i < data.length(); i++) {
                        JSONObject item = data.getJSONObject(i);

                        EstablecimientoItem establecimiento = new EstablecimientoItem(
                                item.optString("ID_ESTABLECIMIENTO"),
                                item.optString("NOMBRE_ESTABLECIMIENTO"),
                                item.optString("TELEFONO_ESTABLECIMIENTO"),
                                item.optString("DIRECCION_ESTABLECIMIENTO")
                        );

                        listaEstablecimientos.add(establecimiento);
                    }

                    ArrayAdapter<EstablecimientoItem> adapter = new ArrayAdapter<>(
                            ModificarEstablecimientoActivityWeb.this,
                            android.R.layout.simple_spinner_item,
                            listaEstablecimientos
                    );

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerEstablecimientos.setAdapter(adapter);

                    spinnerEstablecimientos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            establecimientoSeleccionado = listaEstablecimientos.get(position);
                            llenarCampos(establecimientoSeleccionado);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            establecimientoSeleccionado = null;
                        }
                    });

                    if (listaEstablecimientos.isEmpty()) {
                        txtResultado.setText("No hay establecimientos registrados.");
                    } else {
                        txtResultado.setText("Seleccione un establecimiento para modificar.");
                    }

                } catch (Exception e) {
                    txtResultado.setText("Error al procesar JSON:\n" + e.getMessage() + "\n\nRespuesta:\n" + response);
                }
            }

            @Override
            public void onError(String error) {
                txtResultado.setText("Error de conexión:\n" + error);
            }
        });
    }

    private void llenarCampos(EstablecimientoItem establecimiento) {
        edtId.setText(establecimiento.id);
        edtNombre.setText(establecimiento.nombre);
        edtTelefono.setText(establecimiento.telefono);
        edtDireccion.setText(establecimiento.direccion);
    }

    private void modificarEstablecimiento() {
        if (establecimientoSeleccionado == null) {
            Toast.makeText(this, "Seleccione un establecimiento.", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = edtId.getText().toString().trim();
        String nombre = edtNombre.getText().toString().trim();
        String telefono = edtTelefono.getText().toString().trim();
        String direccion = edtDireccion.getText().toString().trim();

        if (nombre.isEmpty() || telefono.isEmpty() || direccion.isEmpty()) {
            Toast.makeText(this, "Complete todos los campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            String url = ApiConfig.BASE_URL + "modificar_establecimiento.php";

            String body =
                    "id_establecimiento=" + URLEncoder.encode(id, "UTF-8") +
                            "&nombre_establecimiento=" + URLEncoder.encode(nombre, "UTF-8") +
                            "&telefono_establecimiento=" + URLEncoder.encode(telefono, "UTF-8") +
                            "&direccion_establecimiento=" + URLEncoder.encode(direccion, "UTF-8");

            txtResultado.setText("Modificando establecimiento...");

            ApiClient.post(url, body, new ApiClient.ApiCallback() {
                @Override
                public void onSuccess(String response) {
                    try {
                        JSONObject json = new JSONObject(response);

                        boolean success = json.optBoolean("success", false);
                        String message = json.optString("message", "Sin mensaje del servidor.");

                        txtResultado.setText(message);

                        Toast.makeText(
                                ModificarEstablecimientoActivityWeb.this,
                                message,
                                Toast.LENGTH_LONG
                        ).show();

                        if (success) {
                            cargarEstablecimientos();
                        }

                    } catch (Exception e) {
                        txtResultado.setText("Respuesta recibida:\n" + response);
                    }
                }

                @Override
                public void onError(String error) {
                    txtResultado.setText("Error de conexión:\n" + error);
                }
            });

        } catch (Exception e) {
            txtResultado.setText("Error:\n" + e.getMessage());
        }
    }

    private static class EstablecimientoItem {
        String id;
        String nombre;
        String telefono;
        String direccion;

        EstablecimientoItem(String id, String nombre, String telefono, String direccion) {
            this.id = id;
            this.nombre = nombre;
            this.telefono = telefono;
            this.direccion = direccion;
        }

        @Override
        public String toString() {
            return id + " - " + nombre;
        }
    }
}