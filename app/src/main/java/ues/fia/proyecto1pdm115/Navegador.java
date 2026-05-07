package ues.fia.proyecto1pdm115;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;

public class Navegador {

    public static void configurarBarra(Activity activity) {

        LinearLayout btnInicio = activity.findViewById(R.id.navInicio);
        LinearLayout btnPerfil = activity.findViewById(R.id.navPerfil);
        LinearLayout btnSalir = activity.findViewById(R.id.navSalir);

        if (btnInicio != null) {
            btnInicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(activity instanceof MainActivity)) {
                        Intent intent = new Intent(activity, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        activity.startActivity(intent);
                    }
                }
            });
        }

        if (btnPerfil != null) {
            btnPerfil.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!(activity instanceof PerfilActivity)) {
                        Intent intent = new Intent(activity, PerfilActivity.class);
                        activity.startActivity(intent);
                    }
                }
            });
        }

        if (btnSalir != null) {
            btnSalir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(activity, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activity.startActivity(intent);
                    activity.finish();
                }
            });
        }
    }
}
