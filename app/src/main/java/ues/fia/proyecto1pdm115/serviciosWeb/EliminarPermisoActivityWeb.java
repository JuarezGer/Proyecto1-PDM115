package ues.fia.proyecto1pdm115.serviciosWeb;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ues.fia.proyecto1pdm115.ApiClient;
import ues.fia.proyecto1pdm115.ApiConfig;
import ues.fia.proyecto1pdm115.R;
import ues.fia.proyecto1pdm115.modelos.Opcion_crud;
import ues.fia.proyecto1pdm115.modelos.Usuario;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;

public class EliminarPermisoActivityWeb extends AppCompatActivity {

    Spinner spinnerUsuariosEliminarWeb,
            spinnerPermisosEliminarWeb;

    Button btnEliminarPermisoWeb,
            btnRegresarEliminarPermisoWeb;

    ArrayList<Usuario> listaUsuarios = new ArrayList<>();
    ArrayList<Opcion_crud> listaPermisos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_eliminar_permiso_web);

        spinnerUsuariosEliminarWeb =
                findViewById(R.id.spinnerUsuariosEliminarWeb);

        spinnerPermisosEliminarWeb =
                findViewById(R.id.spinnerPermisosEliminarWeb);

        btnEliminarPermisoWeb =
                findViewById(R.id.btnEliminarPermisoWeb);

        btnRegresarEliminarPermisoWeb =
                findViewById(R.id.btnRegresarEliminarPermisoWeb);

        cargarUsuariosWeb();
        cargarPermisosWeb();

        btnEliminarPermisoWeb.setOnClickListener(
                v -> confirmarEliminacion()
        );

        btnRegresarEliminarPermisoWeb.setOnClickListener(
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
                                                EliminarPermisoActivityWeb.this,
                                                android.R.layout.simple_spinner_item,
                                                listaUsuarios
                                        );

                                adapter.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item
                                );

                                spinnerUsuariosEliminarWeb.setAdapter(adapter);

                            }

                        } catch (Exception e) {

                            Toast.makeText(
                                    EliminarPermisoActivityWeb.this,
                                    "Error al cargar usuarios",
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    }

                    @Override
                    public void onError(String error) {

                        Toast.makeText(
                                EliminarPermisoActivityWeb.this,
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
                                                EliminarPermisoActivityWeb.this,
                                                android.R.layout.simple_spinner_item,
                                                listaPermisos
                                        );

                                adapter.setDropDownViewResource(
                                        android.R.layout.simple_spinner_dropdown_item
                                );

                                spinnerPermisosEliminarWeb.setAdapter(adapter);

                            }

                        } catch (Exception e) {

                            Toast.makeText(
                                    EliminarPermisoActivityWeb.this,
                                    "Error al cargar permisos",
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    }

                    @Override
                    public void onError(String error) {

                        Toast.makeText(
                                EliminarPermisoActivityWeb.this,
                                "Error de red: " + error,
                                Toast.LENGTH_LONG
                        ).show();

                    }
                }
        );
    }

    private void confirmarEliminacion() {

        Usuario usuarioSel =
                (Usuario) spinnerUsuariosEliminarWeb.getSelectedItem();

        Opcion_crud permisoSel =
                (Opcion_crud) spinnerPermisosEliminarWeb.getSelectedItem();

        if (usuarioSel == null ||
                permisoSel == null ||
                usuarioSel.getIdUsuario().equals("-1") ||
                permisoSel.getIdopcion().equals("-1")) {

            Toast.makeText(
                    this,
                    "Seleccione un usuario y un permiso",
                    Toast.LENGTH_SHORT
            ).show();

            return;
        }

        AlertDialog.Builder builder =
                new AlertDialog.Builder(this);

        builder.setTitle("Confirmar eliminación");

        builder.setMessage(
                "¿Desea eliminar este permiso?"
        );

        builder.setPositiveButton(
                "Sí, eliminar",
                (dialog, which) ->
                        eliminarPermisoWeb(
                                usuarioSel.getIdUsuario(),
                                permisoSel.getIdopcion()
                        )
        );

        builder.setNegativeButton(
                "Cancelar",
                (dialog, which) -> dialog.dismiss()
        );

        builder.create().show();
    }

    private void eliminarPermisoWeb(
            String idUsuario,
            String idPermiso) {

        String postData =
                "IDUSUARIO=" + idUsuario
                        + "&IDOPCION=" + idPermiso;

        ApiClient.post(
                ApiConfig.DELETE_PERMISO,
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
                                    EliminarPermisoActivityWeb.this,
                                    mensaje,
                                    Toast.LENGTH_LONG
                            ).show();

                            if (success) {

                                spinnerUsuariosEliminarWeb.setSelection(0);
                                spinnerPermisosEliminarWeb.setSelection(0);

                            }

                        } catch (Exception e) {

                            Toast.makeText(
                                    EliminarPermisoActivityWeb.this,
                                    "Error al procesar respuesta",
                                    Toast.LENGTH_SHORT
                            ).show();

                        }

                    }

                    @Override
                    public void onError(String error) {

                        Toast.makeText(
                                EliminarPermisoActivityWeb.this,
                                "Error de red: " + error,
                                Toast.LENGTH_LONG
                        ).show();

                    }
                }
        );
    }
}