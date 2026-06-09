package ues.fia.proyecto1pdm115.serviciosWeb;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.modelos.Opcion_crud;
import ues.fia.proyecto1pdm115.modelos.Usuario;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import ues.fia.proyecto1pdm115.ApiClient;
import ues.fia.proyecto1pdm115.ApiConfig;

public class CrearPermisoActivityWeb extends AppCompatActivity {

    Spinner spinnerUsuariosCrearWeb,
            spinnerPermisosCrearWeb;

    Button btnGuardarPermisoCrearWeb,
            btnRegresarPermisoCrearWeb;

    ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    ArrayList<Opcion_crud> listaPermisos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_permiso_web);

        spinnerUsuariosCrearWeb =
                findViewById(R.id.spinnerUsuariosCrearWeb);

        spinnerPermisosCrearWeb =
                findViewById(R.id.spinnerPermisosCrearWeb);

        btnGuardarPermisoCrearWeb =
                findViewById(R.id.btnGuardarPermisoCrearWeb);

        btnRegresarPermisoCrearWeb =
                findViewById(R.id.btnRegresarPermisoCrearWeb);

        cargarUsuariosWeb();
        cargarPermisosWeb();

        btnGuardarPermisoCrearWeb.setOnClickListener(
                v -> guardarPermisoWeb()
        );

        btnRegresarPermisoCrearWeb.setOnClickListener(
                v -> finish()
        );
    }

    private void cargarUsuariosWeb() {

        ApiClient.post(
                ApiConfig.GET_USUARIOS,
                "",
                new ApiClient.ApiCallback() {

                    @Override
                    public void onSuccess(String response) {

                        try {

                            JSONObject jsonObject =
                                    new JSONObject(response);

                            boolean success =
                                    jsonObject.getBoolean("success");

                            if (success) {

                                JSONArray data =
                                        jsonObject.getJSONArray("data");

                                listaUsuarios.clear();

                                listaUsuarios.add(
                                        new Usuario(
                                                "-1",
                                                "Seleccione un usuario..."
                                        )
                                );

                                for (int i = 0; i < data.length(); i++) {

                                    JSONObject usuario =
                                            data.getJSONObject(i);

                                    listaUsuarios.add(
                                            new Usuario(
                                                    usuario.getString("IDUSUARIO"),
                                                    usuario.getString("NOMUSUARIO")
                                            )
                                    );
                                }

                                ArrayAdapter<Usuario> adapter =
                                        new ArrayAdapter<>(
                                                CrearPermisoActivityWeb.this,
                                                android.R.layout.simple_spinner_item,
                                                listaUsuarios
                                        );

                                adapter.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item
                                );

                                spinnerUsuariosCrearWeb.setAdapter(adapter);

                            }

                        } catch (Exception e) {

                            Toast.makeText(
                                    CrearPermisoActivityWeb.this,
                                    "Error al cargar usuarios",
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    }

                    @Override
                    public void onError(String error) {

                        Toast.makeText(
                                CrearPermisoActivityWeb.this,
                                "Error de red: " + error,
                                Toast.LENGTH_LONG
                        ).show();

                    }
                }
        );
    }

    private void cargarPermisosWeb() {

        ApiClient.post(
                ApiConfig.GET_OPCIONES,
                "",
                new ApiClient.ApiCallback() {

                    @Override
                    public void onSuccess(String response) {

                        try {

                            JSONObject jsonObject =
                                    new JSONObject(response);

                            boolean success =
                                    jsonObject.getBoolean("success");

                            if (success) {

                                JSONArray data =
                                        jsonObject.getJSONArray("data");

                                listaPermisos.clear();

                                listaPermisos.add(
                                        new Opcion_crud(
                                                "-1",
                                                "Seleccione un permiso..."
                                        )
                                );

                                for (int i = 0; i < data.length(); i++) {

                                    JSONObject permiso =
                                            data.getJSONObject(i);

                                    listaPermisos.add(
                                            new Opcion_crud(
                                                    permiso.getString("IDOPCION"),
                                                    permiso.getString("DESCRIPCION")
                                            )
                                    );
                                }

                                ArrayAdapter<Opcion_crud> adapter =
                                        new ArrayAdapter<>(
                                                CrearPermisoActivityWeb.this,
                                                android.R.layout.simple_spinner_item,
                                                listaPermisos
                                        );

                                adapter.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item
                                );

                                spinnerPermisosCrearWeb.setAdapter(adapter);

                            }

                        } catch (Exception e) {

                            Toast.makeText(
                                    CrearPermisoActivityWeb.this,
                                    "Error al cargar permisos",
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    }

                    @Override
                    public void onError(String error) {

                        Toast.makeText(
                                CrearPermisoActivityWeb.this,
                                "Error de red: " + error,
                                Toast.LENGTH_LONG
                        ).show();

                    }
                }
        );
    }

    private void guardarPermisoWeb() {

        Usuario usuarioSel =
                (Usuario) spinnerUsuariosCrearWeb.getSelectedItem();

        Opcion_crud permisoSel =
                (Opcion_crud) spinnerPermisosCrearWeb.getSelectedItem();

        if (usuarioSel == null ||
                permisoSel == null ||
                usuarioSel.getIdUsuario().equals("-1") ||
                permisoSel.getIdopcion().equals("-1")) {

            Toast.makeText(
                    this,
                    "Seleccione un usuario y un permiso",
                    Toast.LENGTH_LONG
            ).show();

            return;
        }

        String postData =
                "IDUSUARIO=" + usuarioSel.getIdUsuario()
                        + "&IDOPCION=" + permisoSel.getIdopcion();

        ApiClient.post(
                ApiConfig.INSERT_PERMISO,
                postData,
                new ApiClient.ApiCallback() {

                    @Override
                    public void onSuccess(String response) {

                        try {

                            JSONObject jsonObject =
                                    new JSONObject(response);

                            boolean success =
                                    jsonObject.getBoolean("success");

                            String mensaje =
                                    jsonObject.getString("message");

                            Toast.makeText(
                                    CrearPermisoActivityWeb.this,
                                    mensaje,
                                    Toast.LENGTH_LONG
                            ).show();

                            if (success) {

                                spinnerUsuariosCrearWeb.setSelection(0);
                                spinnerPermisosCrearWeb.setSelection(0);

                            }

                        } catch (Exception e) {

                            Toast.makeText(
                                    CrearPermisoActivityWeb.this,
                                    "Error al procesar respuesta",
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    }

                    @Override
                    public void onError(String error) {

                        Toast.makeText(
                                CrearPermisoActivityWeb.this,
                                "Error de red: " + error,
                                Toast.LENGTH_LONG
                        ).show();

                    }
                }
        );
    }
}