package ues.fia.proyecto1pdm115;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.HashSet;
import java.util.Set;

import ues.fia.proyecto1pdm115.pacientes.*;
import ues.fia.proyecto1pdm115.doctores.*;
import ues.fia.proyecto1pdm115.especialidades.*;
import ues.fia.proyecto1pdm115.hospitales.*;
import ues.fia.proyecto1pdm115.usuarios.*;
import ues.fia.proyecto1pdm115.establecimientos.*;
import ues.fia.proyecto1pdm115.aseguradoras.*;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private controlDBHospitalApp helper;
    private final Set<View> cardsEncontradas = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            EdgeToEdge.enable(this);
        } catch (Exception e) {
            Log.e(TAG, "No se pudo activar EdgeToEdge", e);
        }

        setContentView(R.layout.activity_main);

        try {
            Navegador.configurarBarra(this);
        } catch (Exception e) {
            Log.e(TAG, "Error al configurar la barra inferior", e);
        }

        helper = new controlDBHospitalApp(this);

        SharedPreferences preferences = getSharedPreferences("sesion_usuario", MODE_PRIVATE);

        String nombreUsuario = getIntent().getStringExtra("nombreUsuario");
        if (nombreUsuario == null || nombreUsuario.isEmpty()) {
            nombreUsuario = preferences.getString("nombreUsuario", "");
        }

        String idUsuario = getIntent().getStringExtra("idUsuario");
        if (idUsuario == null || idUsuario.isEmpty()) {
            idUsuario = preferences.getString("idUsuario", "");
        }

        TextView txtBienvenida = buscarVista("txtBienvenida");
        if (txtBienvenida != null && nombreUsuario != null && !nombreUsuario.isEmpty()) {
            txtBienvenida.setText("¡Bienvenido " + nombreUsuario + "!");
        }

        registrarCards();
        configurarNavegacionCards();
        aplicarPermisosPorUsuario(idUsuario);
        configurarInsets();
    }

    @SuppressWarnings("unchecked")
    private <T extends View> T buscarVista(String nombreId) {
        int id = getResources().getIdentifier(nombreId, "id", getPackageName());
        if (id == 0) {
            return null;
        }
        return (T) findViewById(id);
    }

    private void registrarCards() {
        agregarCard("cardPacientes");
        agregarCard("cardDoctores");
        agregarCard("cardEspecialidades");
        agregarCard("cardHospitales");
        agregarCard("cardUsuarios");
        agregarCard("cardUsuariosAdmin");
        agregarCard("cardEstablecimientos");
        agregarCard("cardAseguradoras");
        agregarCard("cardConsultas");
        agregarCard("cardTipoEmergencia");
        agregarCard("cardRecetas");
        agregarCard("cardHospitalizacion");
        agregarCard("cardMedicamentos");
        agregarCard("cardDetalleReceta");
        agregarCard("cardPagos");
        agregarCard("cardSeguros");
        agregarCard("cardPermisos");
    }

    private void agregarCard(String nombreId) {
        View card = buscarVista(nombreId);
        if (card != null) {
            cardsEncontradas.add(card);
        } else {
            Log.w(TAG, "No existe en activity_main.xml el ID: " + nombreId);
        }
    }

    private void configurarNavegacionCards() {
        configurarClick("cardPacientes", PacienteMenuActivity.class);
        configurarClick("cardDoctores", DoctorMenuActivity.class);
        configurarClick("cardEspecialidades", EspecialidadMenuActivity.class);
        configurarClick("cardHospitales", HospitalMenuActivity.class);
        configurarClick("cardAseguradoras", AseguradoraMenuActivity.class);
        configurarClick("cardEstablecimientos", EstablecimientoMenuActivity.class);
        configurarClick("cardUsuarios", UsuariosMenuActivity.class);
        configurarClick("cardUsuariosAdmin", UsuariosMenuActivity.class);

        View cardPermisos = buscarVista("cardPermisos");
        if (cardPermisos != null) {
            cardPermisos.setOnClickListener(v ->
                    Toast.makeText(this, "Pantalla de permisos pendiente", Toast.LENGTH_SHORT).show());
        }
    }

    private void configurarClick(String nombreId, Class<?> activityDestino) {
        View card = buscarVista(nombreId);
        if (card != null) {
            card.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, activityDestino)));
        }
    }

    private void aplicarPermisosPorUsuario(String idUsuario) {
        ocultarTodasLasCards();

        if (idUsuario == null || idUsuario.isEmpty()) {
            Toast.makeText(this, "No se encontró el usuario de la sesión", Toast.LENGTH_SHORT).show();
            mostrarTodasLasCards();
            return;
        }

        Set<String> opciones = new HashSet<>();

        try {
            helper.abrir();
            opciones = helper.obtenerOpcionesUsuario(idUsuario);
        } catch (Exception e) {
            Log.e(TAG, "Error al cargar permisos", e);
            Toast.makeText(this, "Error al cargar permisos. Se mostrará el menú completo.", Toast.LENGTH_LONG).show();
            mostrarTodasLasCards();
            return;
        } finally {
            try {
                helper.cerrar();
            } catch (Exception e) {
                Log.e(TAG, "Error al cerrar la base", e);
            }
        }

        if (opciones.isEmpty()) {
            Toast.makeText(this, "El usuario no tiene permisos registrados. Se mostrará el menú completo.", Toast.LENGTH_LONG).show();
            mostrarTodasLasCards();
            return;
        }

        if (opciones.contains("ADM")) {
            mostrarCardsAdministracion();
        }

        if (opciones.contains("FAC")) {
            mostrarCardsFacturacion();
        }

        if (opciones.contains("PAC")) {
            mostrarCardsAtencionMedica();
        }
    }

    private void ocultarTodasLasCards() {
        for (View card : cardsEncontradas) {
            card.setVisibility(View.GONE);
        }
    }

    private void mostrarTodasLasCards() {
        for (View card : cardsEncontradas) {
            card.setVisibility(View.VISIBLE);
        }
    }

    private void mostrarCardsAdministracion() {
        mostrar("cardDoctores", "cardEspecialidades", "cardHospitales", "cardUsuarios",
                "cardUsuariosAdmin", "cardEstablecimientos", "cardAseguradoras",
                "cardTipoEmergencia", "cardMedicamentos", "cardPermisos");
    }

    private void mostrarCardsFacturacion() {
        mostrar("cardPagos", "cardSeguros", "cardAseguradoras");
    }

    private void mostrarCardsAtencionMedica() {
        mostrar("cardPacientes", "cardDoctores", "cardHospitales", "cardConsultas",
                "cardRecetas", "cardHospitalizacion", "cardMedicamentos", "cardDetalleReceta");
    }

    private void mostrar(String... ids) {
        for (String nombreId : ids) {
            View card = buscarVista(nombreId);
            if (card != null) {
                card.setVisibility(View.VISIBLE);
            }
        }
    }

    private void configurarInsets() {
        View mainLayout = buscarVista("mainLayout");
        if (mainLayout != null) {
            ViewCompat.setOnApplyWindowInsetsListener(mainLayout, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }
}
