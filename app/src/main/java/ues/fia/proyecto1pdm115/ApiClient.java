package ues.fia.proyecto1pdm115;

import android.os.Handler;
import android.os.Looper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ApiClient {

    public interface ApiCallback {
        void onSuccess(String response);
        void onError(String error);
    }

    private static final ExecutorService executor = Executors.newSingleThreadExecutor();
    private static final Handler mainHandler = new Handler(Looper.getMainLooper());

    public static void get(String urlString, ApiCallback callback) {
        executor.execute(() -> {
            HttpURLConnection connection = null;

            try {
                URL url = new URL(urlString);
                connection = (HttpURLConnection) url.openConnection();

                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);

                int responseCode = connection.getResponseCode();

                BufferedReader reader;

                if (responseCode >= 200 && responseCode < 300) {
                    reader = new BufferedReader(
                            new InputStreamReader(connection.getInputStream())
                    );
                } else {
                    reader = new BufferedReader(
                            new InputStreamReader(connection.getErrorStream())
                    );
                }

                StringBuilder response = new StringBuilder();
                String line;

                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }

                reader.close();

                if (responseCode >= 200 && responseCode < 300) {
                    mainHandler.post(() -> callback.onSuccess(response.toString()));
                } else {
                    mainHandler.post(() -> callback.onError("Error HTTP: " + responseCode));
                }

            } catch (Exception e) {
                mainHandler.post(() -> callback.onError(e.getMessage()));
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        });
    }
}
