package ues.fia.proyecto1pdm115;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ues.fia.proyecto1pdm115.modelos.Especialidad;
import ues.fia.proyecto1pdm115.modelos.Opcion_crud;
import ues.fia.proyecto1pdm115.modelos.Paciente;
import ues.fia.proyecto1pdm115.modelos.Puede_elegir;
import ues.fia.proyecto1pdm115.modelos.Usuario;
import ues.fia.proyecto1pdm115.modelos.Establecimiento;
import ues.fia.proyecto1pdm115.modelos.Aseguradora;

public class controlDBHospitalApp {

    private static final String BASE_DATOS = "hospital_app.db";
    private static final int VERSION = 4;

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public controlDBHospitalApp(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    // =========================================================
    // LOGIN
    // =========================================================

    public boolean validarLogin(String usuario, String clave) {
        Cursor cursor = null;

        try {
            cursor = db.query(
                    "USUARIO",
                    null,
                    "(ID_USUARIO = ? OR NOMBRE_USUARIO = ?) AND CLAVE = ?",
                    new String[]{usuario, usuario, clave},
                    null,
                    null,
                    null
            );

            return cursor.moveToFirst();

        } catch (Exception e) {
            e.printStackTrace();
            return false;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String obtenerNombreUsuario(String usuario) {
        Cursor cursor = null;

        try {
            cursor = db.query(
                    "USUARIO",
                    new String[]{"NOMBRE_USUARIO"},
                    "ID_USUARIO = ? OR NOMBRE_USUARIO = ?",
                    new String[]{usuario, usuario},
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_USUARIO"));
            }

            return "";

        } catch (Exception e) {
            e.printStackTrace();
            return "";

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }



    public String obtenerIdUsuario(String usuario) {
        Cursor cursor = null;

        try {
            cursor = db.query(
                    "USUARIO",
                    new String[]{"ID_USUARIO"},
                    "ID_USUARIO = ? OR NOMBRE_USUARIO = ?",
                    new String[]{usuario, usuario},
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow("ID_USUARIO"));
            }

            return "";

        } catch (Exception e) {
            e.printStackTrace();
            return "";

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public Set<String> obtenerOpcionesUsuario(String idUsuario) {
        Set<String> opciones = new HashSet<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(
                    "SELECT ID_OPCION FROM PUEDE_ELEGIR WHERE ID_USUARIO = ?",
                    new String[]{idUsuario}
            );

            if (cursor.moveToFirst()) {
                do {
                    opciones.add(cursor.getString(cursor.getColumnIndexOrThrow("ID_OPCION")));
                } while (cursor.moveToNext());
            }

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return opciones;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, BASE_DATOS, null, VERSION);
        }

        @Override
        public void onConfigure(SQLiteDatabase db) {
            super.onConfigure(db);
            db.setForeignKeyConstraintsEnabled(true);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.beginTransaction();

                crearTablas(db);
                crearTriggers(db);
                llenarDatosIniciales(db);

                db.setTransactionSuccessful();

            } catch (SQLException e) {
                e.printStackTrace();

            } finally {
                if (db.inTransaction()) {
                    db.endTransaction();
                }
            }
        }

        private void crearTablas(SQLiteDatabase db) {

            db.execSQL("CREATE TABLE DEPARTAMENTO (" +
                    "COD_DPTO TEXT PRIMARY KEY, " +
                    "NOMBRE_DPTO TEXT NOT NULL" +
                    ");");

            db.execSQL("CREATE TABLE ESPECIALIDAD (" +
                    "ID_ESPECIALIDAD INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NOMBRE_ESPECIALIDAD TEXT NOT NULL" +
                    ");");

            db.execSQL("CREATE TABLE TIPO_EMERGENCIA (" +
                    "COD_EMERGENCIA TEXT PRIMARY KEY, " +
                    "PRIORIDAD TEXT NOT NULL, " +
                    "COSTO_EMERGENCIA REAL NOT NULL" +
                    ");");

            db.execSQL("CREATE TABLE MEDICAMENTO (" +
                    "COD_MEDICAMENTO TEXT PRIMARY KEY, " +
                    "NOMBRE_MEDICAMENTO TEXT NOT NULL, " +
                    "FECHA_VENCIMIENTO TEXT NOT NULL, " +
                    "CANTIDAD_INVENTARIO INTEGER NOT NULL, " +
                    "PRECIO_VENTA REAL NOT NULL, " +
                    "LOTE TEXT NOT NULL" +
                    ");");

            db.execSQL("CREATE TABLE USUARIO (" +
                    "ID_USUARIO TEXT PRIMARY KEY, " +
                    "NOMBRE_USUARIO TEXT NOT NULL, " +
                    "CLAVE TEXT NOT NULL" +
                    ");");

            db.execSQL("CREATE TABLE ASEGURADORA (" +
                    "ID_ASEGURADORA INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NOMBRE_ASEGURADORA TEXT NOT NULL, " +
                    "TELEFONO_ASEGURADORA TEXT NOT NULL" +
                    ");");

            db.execSQL("CREATE TABLE OPCION_CRUD (" +
                    "ID_OPCION TEXT PRIMARY KEY, " +
                    "DESCRIPCION_OPC TEXT NOT NULL, " +
                    "NUM_CRUD INTEGER NOT NULL" +
                    ");");

            db.execSQL("CREATE TABLE MUNICIPIO (" +
                    "COD_MUNICIPIO TEXT PRIMARY KEY, " +
                    "COD_DPTO TEXT NOT NULL, " +
                    "NOMBRE_MUNICIPIO TEXT NOT NULL, " +
                    "FOREIGN KEY (COD_DPTO) REFERENCES DEPARTAMENTO(COD_DPTO)" +
                    ");");

            db.execSQL("CREATE TABLE DISTRITO (" +
                    "COD_DISTRITO TEXT PRIMARY KEY, " +
                    "COD_MUNICIPIO TEXT NOT NULL, " +
                    "NOMBRE_DISTRITO TEXT NOT NULL, " +
                    "FOREIGN KEY (COD_MUNICIPIO) REFERENCES MUNICIPIO(COD_MUNICIPIO)" +
                    ");");

            // Tu script final usa ESTABLECIMIENTO en TRABAJA_CON y VENDEN.
            // Por eso se mantiene esta tabla para que las llaves foráneas funcionen.
            db.execSQL("CREATE TABLE ESTABLECIMIENTO (" +
                    "ID_ESTABLECIMIENTO INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "NOMBRE_ESTABLECIMIENTO TEXT NOT NULL, " +
                    "TELEFONO_ESTABLECIMIENTO TEXT NOT NULL, " +
                    "DIRECCION_ESTABLECIMIENTO TEXT NOT NULL" +
                    ");");

            db.execSQL("CREATE TABLE HOSPITAL (" +
                    "ID_HOSPITAL INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "COD_DISTRITO TEXT NOT NULL, " +
                    "NOMBRE_HOSPITAL TEXT NOT NULL, " +
                    "TELEFONO_HOSPITAL TEXT NOT NULL, " +
                    "FOREIGN KEY (COD_DISTRITO) REFERENCES DISTRITO(COD_DISTRITO)" +
                    ");");

            db.execSQL("CREATE TABLE DOCTOR (" +
                    "DUI_DOCTOR TEXT PRIMARY KEY, " +
                    "ID_HOSPITAL INTEGER NOT NULL, " +
                    "ID_USUARIO TEXT NOT NULL, " +
                    "NOMBRE_DOCTOR TEXT NOT NULL, " +
                    "APELLIDO_DOCTOR TEXT NOT NULL, " +
                    "FOREIGN KEY (ID_HOSPITAL) REFERENCES HOSPITAL(ID_HOSPITAL), " +
                    "FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO(ID_USUARIO)" +
                    ");");

            db.execSQL("CREATE TABLE PACIENTE (" +
                    "DUI_PACIENTE TEXT PRIMARY KEY, " +
                    "ID_POLIZA INTEGER, " +
                    "COD_DISTRITO TEXT NOT NULL, " +
                    "PRIMER_NOMBRE_PACIENTE TEXT NOT NULL, " +
                    "SEGUNDO_NOMBRE_PACIENTE TEXT, " +
                    "PRIMER_APELLIDO_PACIENTE TEXT NOT NULL, " +
                    "SEGUNDO_APELLIDO_PACIENTE TEXT, " +
                    "FECHA_NACIMIENTO_PACIENTE TEXT, " +
                    "GENERO_PACIENTE TEXT NOT NULL, " +
                    "TELEFONO_PACIENTE TEXT NOT NULL, " +
                    "FOREIGN KEY (COD_DISTRITO) REFERENCES DISTRITO(COD_DISTRITO)" +
                    ");");

            db.execSQL("CREATE TABLE SEGURO (" +
                    "ID_POLIZA INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "DUI_PACIENTE TEXT NOT NULL, " +
                    "ID_ASEGURADORA INTEGER NOT NULL, " +
                    "PORCENTAJE_COBERTURA REAL NOT NULL, " +
                    "TIPO_ASEGURADO TEXT NOT NULL, " +
                    "DEDUCTIBLE_MEDICINA REAL NOT NULL, " +
                    "DEDUCTIBLE_OPERACION REAL NOT NULL, " +
                    "FOREIGN KEY (DUI_PACIENTE) REFERENCES PACIENTE(DUI_PACIENTE), " +
                    "FOREIGN KEY (ID_ASEGURADORA) REFERENCES ASEGURADORA(ID_ASEGURADORA)" +
                    ");");

            db.execSQL("CREATE TABLE CONSULTA (" +
                    "ID_CONSULTA INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "DUI_PACIENTE TEXT NOT NULL, " +
                    "DUI_DOCTOR TEXT NOT NULL, " +
                    "ID_HOSPITALIZACION INTEGER, " +
                    "COD_EMERGENCIA TEXT NOT NULL, " +
                    "FECHA_CONSULTA TEXT NOT NULL, " +
                    "DIAGNOSTICO TEXT NOT NULL, " +
                    "CARGO_TOTAL_CONSULTA REAL DEFAULT 0, " +
                    "PAGA_MEDICAMENTO INTEGER NOT NULL, " +
                    "FOREIGN KEY (DUI_PACIENTE) REFERENCES PACIENTE(DUI_PACIENTE), " +
                    "FOREIGN KEY (DUI_DOCTOR) REFERENCES DOCTOR(DUI_DOCTOR), " +
                    "FOREIGN KEY (COD_EMERGENCIA) REFERENCES TIPO_EMERGENCIA(COD_EMERGENCIA)" +
                    ");");

            db.execSQL("CREATE TABLE RECETA (" +
                    "ID_RECETA INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "ID_CONSULTA INTEGER, " +
                    "FECHA_EMISION TEXT NOT NULL, " +
                    "ESTADO_RECETA TEXT NOT NULL, " +
                    "FOREIGN KEY (ID_CONSULTA) REFERENCES CONSULTA(ID_CONSULTA)" +
                    ");");

            db.execSQL("CREATE TABLE DETALLE_RECETA (" +
                    "ID_DETALLE_RECETA INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "ID_RECETA INTEGER NOT NULL, " +
                    "CANTIDAD INTEGER NOT NULL, " +
                    "INSTRUCCIONES TEXT NOT NULL, " +
                    "PRECIO_UNITARIO_HISTORICO REAL, " +
                    "SUB_TOTAL_ITEM REAL, " +
                    "FOREIGN KEY (ID_RECETA) REFERENCES RECETA(ID_RECETA)" +
                    ");");

            db.execSQL("CREATE TABLE PAGO (" +
                    "ID_PAGO INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "ID_CONSULTA INTEGER NOT NULL UNIQUE, " +
                    "TIPO_PAGO TEXT NOT NULL, " +
                    "MONTO_TOTAL REAL NOT NULL, " +
                    "FECHA_PAGO TEXT NOT NULL, " +
                    "FOREIGN KEY (ID_CONSULTA) REFERENCES CONSULTA(ID_CONSULTA)" +
                    ");");

            db.execSQL("CREATE TABLE HOSPITALIZACION (" +
                    "ID_HOSPITALIZACION INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "ID_CONSULTA INTEGER NOT NULL, " +
                    "FECHA_INICIO_HOSP TEXT NOT NULL, " +
                    "FECHA_FIN_HOSP TEXT NOT NULL, " +
                    "MOTIVO_INGRESO TEXT NOT NULL, " +
                    "COSTO_HOSPITALIZACION REAL NOT NULL, " +
                    "FOREIGN KEY (ID_CONSULTA) REFERENCES CONSULTA(ID_CONSULTA)" +
                    ");");

            db.execSQL("CREATE TABLE CUENTA_CON (" +
                    "DUI_DOCTOR TEXT NOT NULL, " +
                    "ID_ESPECIALIDAD INTEGER NOT NULL, " +
                    "PRIMARY KEY (DUI_DOCTOR, ID_ESPECIALIDAD), " +
                    "FOREIGN KEY (DUI_DOCTOR) REFERENCES DOCTOR(DUI_DOCTOR), " +
                    "FOREIGN KEY (ID_ESPECIALIDAD) REFERENCES ESPECIALIDAD(ID_ESPECIALIDAD)" +
                    ");");

            db.execSQL("CREATE TABLE POSEE (" +
                    "ID_HOSPITAL INTEGER NOT NULL, " +
                    "ID_ESPECIALIDAD INTEGER NOT NULL, " +
                    "PRIMARY KEY (ID_HOSPITAL, ID_ESPECIALIDAD), " +
                    "FOREIGN KEY (ID_HOSPITAL) REFERENCES HOSPITAL(ID_HOSPITAL), " +
                    "FOREIGN KEY (ID_ESPECIALIDAD) REFERENCES ESPECIALIDAD(ID_ESPECIALIDAD)" +
                    ");");

            db.execSQL("CREATE TABLE FORMADO_POR (" +
                    "ID_DETALLE_RECETA INTEGER NOT NULL, " +
                    "COD_MEDICAMENTO TEXT NOT NULL, " +
                    "PRIMARY KEY (ID_DETALLE_RECETA, COD_MEDICAMENTO), " +
                    "FOREIGN KEY (ID_DETALLE_RECETA) REFERENCES DETALLE_RECETA(ID_DETALLE_RECETA), " +
                    "FOREIGN KEY (COD_MEDICAMENTO) REFERENCES MEDICAMENTO(COD_MEDICAMENTO)" +
                    ");");

            db.execSQL("CREATE TABLE TRABAJA_CON (" +
                    "ID_ASEGURADORA INTEGER NOT NULL, " +
                    "ID_ESTABLECIMIENTO INTEGER NOT NULL, " +
                    "PRIMARY KEY (ID_ASEGURADORA, ID_ESTABLECIMIENTO), " +
                    "FOREIGN KEY (ID_ASEGURADORA) REFERENCES ASEGURADORA(ID_ASEGURADORA), " +
                    "FOREIGN KEY (ID_ESTABLECIMIENTO) REFERENCES ESTABLECIMIENTO(ID_ESTABLECIMIENTO)" +
                    ");");

            db.execSQL("CREATE TABLE PUEDE_ELEGIR (" +
                    "ID_USUARIO TEXT NOT NULL, " +
                    "ID_OPCION TEXT NOT NULL, " +
                    "PRIMARY KEY (ID_USUARIO, ID_OPCION), " +
                    "FOREIGN KEY (ID_USUARIO) REFERENCES USUARIO(ID_USUARIO), " +
                    "FOREIGN KEY (ID_OPCION) REFERENCES OPCION_CRUD(ID_OPCION)" +
                    ");");

            db.execSQL("CREATE TABLE VENDEN (" +
                    "ID_ESTABLECIMIENTO INTEGER NOT NULL, " +
                    "COD_MEDICAMENTO TEXT NOT NULL, " +
                    "PRIMARY KEY (ID_ESTABLECIMIENTO, COD_MEDICAMENTO), " +
                    "FOREIGN KEY (ID_ESTABLECIMIENTO) REFERENCES ESTABLECIMIENTO(ID_ESTABLECIMIENTO), " +
                    "FOREIGN KEY (COD_MEDICAMENTO) REFERENCES MEDICAMENTO(COD_MEDICAMENTO)" +
                    ");");
        }

        private void crearTriggers(SQLiteDatabase db) {

            db.execSQL("CREATE TRIGGER validar_vencimiento_medicamento " +
                    "BEFORE INSERT ON MEDICAMENTO " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "SELECT CASE " +
                    "WHEN NEW.FECHA_VENCIMIENTO <= DATE('now') " +
                    "THEN RAISE(ABORT, 'Error: No se puede registrar un medicamento que ya esta vencido.') " +
                    "END; " +
                    "END;");

            db.execSQL("CREATE TRIGGER validar_vencimiento_medicamento_actualizado " +
                    "BEFORE UPDATE OF FECHA_VENCIMIENTO ON MEDICAMENTO " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "SELECT CASE " +
                    "WHEN NEW.FECHA_VENCIMIENTO <= DATE('now') " +
                    "THEN RAISE(ABORT, 'Error: No se puede actualizar una fecha de vencimiento pasada.') " +
                    "END; " +
                    "END;");

            db.execSQL("CREATE TRIGGER calcular_subtotal_item " +
                    "AFTER INSERT ON DETALLE_RECETA " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "UPDATE DETALLE_RECETA " +
                    "SET SUB_TOTAL_ITEM = NEW.PRECIO_UNITARIO_HISTORICO * NEW.CANTIDAD " +
                    "WHERE ID_DETALLE_RECETA = NEW.ID_DETALLE_RECETA; " +
                    "END;");

            db.execSQL("CREATE TRIGGER calcular_subtotal_update " +
                    "AFTER UPDATE OF CANTIDAD, PRECIO_UNITARIO_HISTORICO ON DETALLE_RECETA " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "UPDATE DETALLE_RECETA " +
                    "SET SUB_TOTAL_ITEM = NEW.CANTIDAD * NEW.PRECIO_UNITARIO_HISTORICO " +
                    "WHERE ID_DETALLE_RECETA = NEW.ID_DETALLE_RECETA; " +
                    "END;");

            db.execSQL("CREATE TRIGGER asignar_especialidad_general_auto " +
                    "AFTER INSERT ON DOCTOR " +
                    "FOR EACH ROW " +
                    "WHEN NOT EXISTS (SELECT 1 FROM CUENTA_CON WHERE DUI_DOCTOR = NEW.DUI_DOCTOR) " +
                    "BEGIN " +
                    "INSERT INTO CUENTA_CON (DUI_DOCTOR, ID_ESPECIALIDAD) " +
                    "VALUES (NEW.DUI_DOCTOR, 1); " +
                    "END;");

            db.execSQL("CREATE TRIGGER eliminar_especialidades_cascada " +
                    "BEFORE DELETE ON DOCTOR " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "DELETE FROM CUENTA_CON WHERE DUI_DOCTOR = OLD.DUI_DOCTOR; " +
                    "END;");

            db.execSQL("CREATE TRIGGER registrar_hospitalizacion_en_consulta " +
                    "AFTER INSERT ON HOSPITALIZACION " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "UPDATE CONSULTA " +
                    "SET ID_HOSPITALIZACION = NEW.ID_HOSPITALIZACION " +
                    "WHERE ID_CONSULTA = NEW.ID_CONSULTA; " +
                    "END;");

            db.execSQL("CREATE TRIGGER guardar_precio_unitario_historico " +
                    "AFTER INSERT ON FORMADO_POR " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "UPDATE DETALLE_RECETA " +
                    "SET PRECIO_UNITARIO_HISTORICO = ( " +
                    "SELECT PRECIO_VENTA FROM MEDICAMENTO WHERE COD_MEDICAMENTO = NEW.COD_MEDICAMENTO " +
                    ") " +
                    "WHERE ID_DETALLE_RECETA = NEW.ID_DETALLE_RECETA; " +
                    "END;");

            db.execSQL("CREATE TRIGGER retiro_superior_al_stock " +
                    "BEFORE INSERT ON FORMADO_POR " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "SELECT CASE " +
                    "WHEN (SELECT CANTIDAD_INVENTARIO FROM MEDICAMENTO WHERE COD_MEDICAMENTO = NEW.COD_MEDICAMENTO) < " +
                    "(SELECT CANTIDAD FROM DETALLE_RECETA WHERE ID_DETALLE_RECETA = NEW.ID_DETALLE_RECETA) " +
                    "THEN RAISE(ABORT, 'Error: NO HAY SUFICIENTE MEDICAMENTO.') " +
                    "END; " +
                    "END;");

            db.execSQL("CREATE TRIGGER actualizar_inventario_stock " +
                    "AFTER INSERT ON FORMADO_POR " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "UPDATE MEDICAMENTO " +
                    "SET CANTIDAD_INVENTARIO = CANTIDAD_INVENTARIO - ( " +
                    "SELECT CANTIDAD FROM DETALLE_RECETA WHERE ID_DETALLE_RECETA = NEW.ID_DETALLE_RECETA " +
                    ") " +
                    "WHERE COD_MEDICAMENTO = NEW.COD_MEDICAMENTO; " +
                    "END;");

            db.execSQL("CREATE TRIGGER validar_especialidad_hospital_doctor " +
                    "BEFORE UPDATE OF ID_HOSPITAL ON DOCTOR " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "SELECT CASE " +
                    "WHEN EXISTS ( " +
                    "SELECT 1 FROM CUENTA_CON CC " +
                    "WHERE CC.DUI_DOCTOR = OLD.DUI_DOCTOR " +
                    "AND CC.ID_ESPECIALIDAD NOT IN ( " +
                    "SELECT P.ID_ESPECIALIDAD FROM POSEE P WHERE P.ID_HOSPITAL = NEW.ID_HOSPITAL " +
                    ") " +
                    ") " +
                    "THEN RAISE(ABORT, 'Error: El hospital no cuenta con la especialidad del doctor.') " +
                    "END; " +
                    "END;");

            db.execSQL("CREATE TRIGGER calcular_pago_final_con_seguro " +
                    "AFTER INSERT ON PAGO " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "UPDATE PAGO " +
                    "SET MONTO_TOTAL = " +
                    "CASE " +
                    "WHEN EXISTS ( " +
                    "SELECT 1 FROM SEGURO S " +
                    "JOIN CONSULTA C ON S.DUI_PACIENTE = C.DUI_PACIENTE " +
                    "WHERE C.ID_CONSULTA = NEW.ID_CONSULTA " +
                    ") " +
                    "THEN ( " +
                    "SELECT (C.CARGO_TOTAL_CONSULTA * (1 - S.PORCENTAJE_COBERTURA / 100.0)) + " +
                    "S.DEDUCTIBLE_MEDICINA + S.DEDUCTIBLE_OPERACION " +
                    "FROM CONSULTA C " +
                    "JOIN SEGURO S ON C.DUI_PACIENTE = S.DUI_PACIENTE " +
                    "WHERE C.ID_CONSULTA = NEW.ID_CONSULTA " +
                    ") " +
                    "ELSE (SELECT CARGO_TOTAL_CONSULTA FROM CONSULTA WHERE ID_CONSULTA = NEW.ID_CONSULTA) " +
                    "END " +
                    "WHERE ID_PAGO = NEW.ID_PAGO; " +
                    "END;");

            db.execSQL("CREATE TRIGGER actualizar_total_por_hospitalizacion " +
                    "AFTER INSERT ON HOSPITALIZACION " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "UPDATE CONSULTA " +
                    "SET CARGO_TOTAL_CONSULTA = " +
                    "(SELECT COSTO_EMERGENCIA FROM TIPO_EMERGENCIA WHERE COD_EMERGENCIA = CONSULTA.COD_EMERGENCIA) + " +
                    "NEW.COSTO_HOSPITALIZACION + " +
                    "CASE " +
                    "WHEN CONSULTA.PAGA_MEDICAMENTO = 1 THEN " +
                    "COALESCE(( " +
                    "SELECT SUM(DR.SUB_TOTAL_ITEM) " +
                    "FROM RECETA R " +
                    "JOIN DETALLE_RECETA DR ON R.ID_RECETA = DR.ID_RECETA " +
                    "WHERE R.ID_CONSULTA = CONSULTA.ID_CONSULTA " +
                    "), 0) " +
                    "ELSE 0 " +
                    "END " +
                    "WHERE ID_CONSULTA = NEW.ID_CONSULTA; " +
                    "END;");

            db.execSQL("CREATE TRIGGER actualizar_total_por_detalle_receta " +
                    "AFTER INSERT ON DETALLE_RECETA " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "UPDATE CONSULTA " +
                    "SET CARGO_TOTAL_CONSULTA = " +
                    "(SELECT COSTO_EMERGENCIA FROM TIPO_EMERGENCIA WHERE COD_EMERGENCIA = CONSULTA.COD_EMERGENCIA) + " +
                    "COALESCE(( " +
                    "SELECT COSTO_HOSPITALIZACION " +
                    "FROM HOSPITALIZACION " +
                    "WHERE ID_CONSULTA = CONSULTA.ID_CONSULTA " +
                    "), 0) + " +
                    "CASE " +
                    "WHEN CONSULTA.PAGA_MEDICAMENTO = 1 THEN " +
                    "COALESCE(( " +
                    "SELECT SUM(DR.SUB_TOTAL_ITEM) " +
                    "FROM RECETA R " +
                    "JOIN DETALLE_RECETA DR ON R.ID_RECETA = DR.ID_RECETA " +
                    "WHERE R.ID_CONSULTA = CONSULTA.ID_CONSULTA " +
                    "), 0) " +
                    "ELSE 0 " +
                    "END " +
                    "WHERE ID_CONSULTA = ( " +
                    "SELECT ID_CONSULTA FROM RECETA WHERE ID_RECETA = NEW.ID_RECETA " +
                    "); " +
                    "END;");

            db.execSQL("CREATE TRIGGER recalcular_pago_por_cambio_consulta " +
                    "AFTER UPDATE OF CARGO_TOTAL_CONSULTA ON CONSULTA " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "UPDATE PAGO " +
                    "SET MONTO_TOTAL = " +
                    "CASE " +
                    "WHEN EXISTS (SELECT 1 FROM SEGURO S WHERE S.DUI_PACIENTE = NEW.DUI_PACIENTE) " +
                    "THEN ( " +
                    "SELECT (NEW.CARGO_TOTAL_CONSULTA * (1 - S.PORCENTAJE_COBERTURA / 100.0)) + " +
                    "S.DEDUCTIBLE_MEDICINA + S.DEDUCTIBLE_OPERACION " +
                    "FROM SEGURO S " +
                    "WHERE S.DUI_PACIENTE = NEW.DUI_PACIENTE " +
                    ") " +
                    "ELSE NEW.CARGO_TOTAL_CONSULTA " +
                    "END " +
                    "WHERE ID_CONSULTA = NEW.ID_CONSULTA; " +
                    "END;");
        }

        private void llenarDatosIniciales(SQLiteDatabase db) {

            // ============================
            // DEPARTAMENTO
            // Se respetan las llaves originales: SV-AH, SV-CA, etc.
            // Por eso MUNICIPIO referencia esas mismas llaves.
            // ============================
            insertarDepartamento(db, "SV-AH", "Ahuachapan");
            insertarDepartamento(db, "SV-CA", "Cabanas");
            insertarDepartamento(db, "SV-CH", "Chalatenango");
            insertarDepartamento(db, "SV-CU", "Cuscatlan");
            insertarDepartamento(db, "SV-LI", "La Libertad");
            insertarDepartamento(db, "SV-PA", "La Paz");
            insertarDepartamento(db, "SV-MO", "Morazan");
            insertarDepartamento(db, "SV-SA", "Santa Ana");
            insertarDepartamento(db, "SV-SM", "San Miguel");
            insertarDepartamento(db, "SV-SS", "San Salvador");
            insertarDepartamento(db, "SV-SO", "Sonsonate");
            insertarDepartamento(db, "SV-US", "Usulutan");
            insertarDepartamento(db, "SV-SV", "San Vicente");
            insertarDepartamento(db, "SV-UN", "La Union");

            // ============================
            // ESPECIALIDAD
            // ============================
            insertarEspecialidadInicial(db, "Medicina General");
            insertarEspecialidadInicial(db, "Pediatria");
            insertarEspecialidadInicial(db, "Cardiologia");
            insertarEspecialidadInicial(db, "Ginecologia");
            insertarEspecialidadInicial(db, "Neurologia");

            // ============================
            // TIPO_EMERGENCIA
            // ============================
            insertarTipoEmergencia(db, "E01", "Alta", 150.00);
            insertarTipoEmergencia(db, "E02", "Media", 100.00);
            insertarTipoEmergencia(db, "E03", "Baja", 50.00);
            insertarTipoEmergencia(db, "E04", "Critica", 300.00);
            insertarTipoEmergencia(db, "E05", "Urgente", 200.00);

            // ============================
            // MEDICAMENTO
            // ============================
            insertarMedicamento(db, "M001", "Paracetamol 500mg", "2027-06-01", 200, 0.25, "L001");
            insertarMedicamento(db, "M002", "Ibuprofeno 400mg", "2026-12-15", 150, 0.40, "L002");
            insertarMedicamento(db, "M003", "Amoxicilina 500mg", "2027-03-20", 100, 0.60, "L003");
            insertarMedicamento(db, "M004", "Omeprazol 20mg", "2028-01-10", 80, 0.75, "L004");
            insertarMedicamento(db, "M005", "Loratadina 10mg", "2026-11-05", 120, 0.35, "L005");

            // ============================
            // USUARIO
            // ============================
            insertarUsuario(db, "U1", "admin", "admin123");
            insertarUsuario(db, "U2", "facturacion", "factu123");

            // ============================
            // ASEGURADORA
            // ============================
            insertarAseguradoraInicial(db, "Seguros Vida", "22221111");
            insertarAseguradoraInicial(db, "Salud Total", "22222222");
            insertarAseguradoraInicial(db, "Proteccion Medica", "22223333");
            insertarAseguradoraInicial(db, "Cobertura Plus", "22224444");
            insertarAseguradoraInicial(db, "AseguraYa", "22225555");

            // ============================
            // OPCION_CRUD
            // ============================
            insertarOpcionCrud(db, "ADM", "Pantallas de administracion", 1);
            insertarOpcionCrud(db, "FAC", "Gestion de pagos", 2);
            insertarOpcionCrud(db, "PAC", "Atencion medica", 3);

            // ============================
            // PUEDE_ELEGIR
            // ============================
            insertarPuedeElegir(db,"U1","ADM");
            insertarPuedeElegir(db,"U1","FAC");
            insertarPuedeElegir(db,"U1","PAC");
            insertarPuedeElegir(db,"U2","FAC");

            // ============================
            // MUNICIPIO
            // El segundo valor respeta COD_DPTO real de DEPARTAMENTO.
            // ============================
            insertarMunicipio(db, "MUN_AH1", "SV-AH", "Ahuachapan");
            insertarMunicipio(db, "MUN_AH2", "SV-AH", "Apaneca");
            insertarMunicipio(db, "MUN_AH3", "SV-AH", "Atiquizaya");
            insertarMunicipio(db, "MUN_AH4", "SV-AH", "Tacuba");

            insertarMunicipio(db, "MUN_CA1", "SV-CA", "Sensuntepeque");
            insertarMunicipio(db, "MUN_CA2", "SV-CA", "Victoria");
            insertarMunicipio(db, "MUN_CA3", "SV-CA", "Ilobasco");
            insertarMunicipio(db, "MUN_CA4", "SV-CA", "Tejutepeque");

            insertarMunicipio(db, "MUN_CH1", "SV-CH", "La Palma");
            insertarMunicipio(db, "MUN_CH2", "SV-CH", "Chalatenango");
            insertarMunicipio(db, "MUN_CH3", "SV-CH", "Nueva Concepcion");
            insertarMunicipio(db, "MUN_CH4", "SV-CH", "San Ignacio");

            insertarMunicipio(db, "MUN_CU1", "SV-CU", "Cojutepeque");
            insertarMunicipio(db, "MUN_CU2", "SV-CU", "Suchitoto");
            insertarMunicipio(db, "MUN_CU3", "SV-CU", "San Rafael Cedros");
            insertarMunicipio(db, "MUN_CU4", "SV-CU", "San Cristobal");

            insertarMunicipio(db, "MUN_LI1", "SV-LI", "Santa Tecla");
            insertarMunicipio(db, "MUN_LI2", "SV-LI", "Colon");
            insertarMunicipio(db, "MUN_LI3", "SV-LI", "Quezaltepeque");
            insertarMunicipio(db, "MUN_LI4", "SV-LI", "Zaragoza");

            insertarMunicipio(db, "MUN_LP1", "SV-PA", "Zacatecoluca");
            insertarMunicipio(db, "MUN_LP2", "SV-PA", "San Juan Nonualco");
            insertarMunicipio(db, "MUN_LP3", "SV-PA", "San Pedro Masahuat");
            insertarMunicipio(db, "MUN_LP4", "SV-PA", "Olocuilta");

            insertarMunicipio(db, "MUN_MO1", "SV-MO", "San Francisco Gotera");
            insertarMunicipio(db, "MUN_MO2", "SV-MO", "Guatajiagua");
            insertarMunicipio(db, "MUN_MO3", "SV-MO", "Sociedad");
            insertarMunicipio(db, "MUN_MO4", "SV-MO", "Yamabal");

            insertarMunicipio(db, "MUN_SA1", "SV-SA", "Santa Ana");
            insertarMunicipio(db, "MUN_SA2", "SV-SA", "Metapan");
            insertarMunicipio(db, "MUN_SA3", "SV-SA", "Chalchuapa");
            insertarMunicipio(db, "MUN_SA4", "SV-SA", "Coatepeque");

            insertarMunicipio(db, "MUN_SM1", "SV-SM", "San Miguel");
            insertarMunicipio(db, "MUN_SM2", "SV-SM", "Chinameca");
            insertarMunicipio(db, "MUN_SM3", "SV-SM", "Ciudad Barrios");
            insertarMunicipio(db, "MUN_SM4", "SV-SM", "Moncagua");

            insertarMunicipio(db, "MUN_SS1", "SV-SS", "Soyapango");
            insertarMunicipio(db, "MUN_SS2", "SV-SS", "Mejicanos");
            insertarMunicipio(db, "MUN_SS3", "SV-SS", "Ilopango");
            insertarMunicipio(db, "MUN_SS4", "SV-SS", "San Marcos");

            insertarMunicipio(db, "MUN_SO1", "SV-SO", "Sonsonate");
            insertarMunicipio(db, "MUN_SO2", "SV-SO", "Nahuizalco");
            insertarMunicipio(db, "MUN_SO3", "SV-SO", "Izalco");
            insertarMunicipio(db, "MUN_SO4", "SV-SO", "Armenia");

            insertarMunicipio(db, "MUN_US1", "SV-US", "Usulutan");
            insertarMunicipio(db, "MUN_US2", "SV-US", "Jiquilisco");
            insertarMunicipio(db, "MUN_US3", "SV-US", "Santa Maria");
            insertarMunicipio(db, "MUN_US4", "SV-US", "Santiago de Maria");

            insertarMunicipio(db, "MUN_SV1", "SV-SV", "San Vicente");
            insertarMunicipio(db, "MUN_SV2", "SV-SV", "Apastepeque");
            insertarMunicipio(db, "MUN_SV3", "SV-SV", "Tecoluca");
            insertarMunicipio(db, "MUN_SV4", "SV-SV", "Guadalupe");

            insertarMunicipio(db, "MUN_UN1", "SV-UN", "La Union");
            insertarMunicipio(db, "MUN_UN2", "SV-UN", "Conchagua");
            insertarMunicipio(db, "MUN_UN3", "SV-UN", "Santa Rosa de Lima");
            insertarMunicipio(db, "MUN_UN4", "SV-UN", "Yucuaiquin");

            // ============================
            // DISTRITO
            // Se agrega un distrito por municipio para que PACIENTE pueda usar COD_DISTRITO.
            // ============================
            insertarDistrito(db, "DIS_AH1", "MUN_AH1", "Ahuachapan");
            insertarDistrito(db, "DIS_AH2", "MUN_AH2", "Apaneca");
            insertarDistrito(db, "DIS_AH3", "MUN_AH3", "Atiquizaya");
            insertarDistrito(db, "DIS_AH4", "MUN_AH4", "Tacuba");

            insertarDistrito(db, "DIS_CA1", "MUN_CA1", "Sensuntepeque");
            insertarDistrito(db, "DIS_CA2", "MUN_CA2", "Victoria");
            insertarDistrito(db, "DIS_CA3", "MUN_CA3", "Ilobasco");
            insertarDistrito(db, "DIS_CA4", "MUN_CA4", "Tejutepeque");

            insertarDistrito(db, "DIS_CH1", "MUN_CH1", "La Palma");
            insertarDistrito(db, "DIS_CH2", "MUN_CH2", "Chalatenango");
            insertarDistrito(db, "DIS_CH3", "MUN_CH3", "Nueva Concepcion");
            insertarDistrito(db, "DIS_CH4", "MUN_CH4", "San Ignacio");

            insertarDistrito(db, "DIS_CU1", "MUN_CU1", "Cojutepeque");
            insertarDistrito(db, "DIS_CU2", "MUN_CU2", "Suchitoto");
            insertarDistrito(db, "DIS_CU3", "MUN_CU3", "San Rafael Cedros");
            insertarDistrito(db, "DIS_CU4", "MUN_CU4", "San Cristobal");

            insertarDistrito(db, "DIS_LI1", "MUN_LI1", "Santa Tecla");
            insertarDistrito(db, "DIS_LI2", "MUN_LI2", "Colon");
            insertarDistrito(db, "DIS_LI3", "MUN_LI3", "Quezaltepeque");
            insertarDistrito(db, "DIS_LI4", "MUN_LI4", "Zaragoza");

            insertarDistrito(db, "DIS_LP1", "MUN_LP1", "Zacatecoluca");
            insertarDistrito(db, "DIS_LP2", "MUN_LP2", "San Juan Nonualco");
            insertarDistrito(db, "DIS_LP3", "MUN_LP3", "San Pedro Masahuat");
            insertarDistrito(db, "DIS_LP4", "MUN_LP4", "Olocuilta");

            insertarDistrito(db, "DIS_MO1", "MUN_MO1", "San Francisco Gotera");
            insertarDistrito(db, "DIS_MO2", "MUN_MO2", "Guatajiagua");
            insertarDistrito(db, "DIS_MO3", "MUN_MO3", "Sociedad");
            insertarDistrito(db, "DIS_MO4", "MUN_MO4", "Yamabal");

            insertarDistrito(db, "DIS_SA1", "MUN_SA1", "Santa Ana");
            insertarDistrito(db, "DIS_SA2", "MUN_SA2", "Metapan");
            insertarDistrito(db, "DIS_SA3", "MUN_SA3", "Chalchuapa");
            insertarDistrito(db, "DIS_SA4", "MUN_SA4", "Coatepeque");

            insertarDistrito(db, "DIS_SM1", "MUN_SM1", "San Miguel");
            insertarDistrito(db, "DIS_SM2", "MUN_SM2", "Chinameca");
            insertarDistrito(db, "DIS_SM3", "MUN_SM3", "Ciudad Barrios");
            insertarDistrito(db, "DIS_SM4", "MUN_SM4", "Moncagua");

            insertarDistrito(db, "DIS_SS1", "MUN_SS1", "Soyapango");
            insertarDistrito(db, "DIS_SS2", "MUN_SS2", "Mejicanos");
            insertarDistrito(db, "DIS_SS3", "MUN_SS3", "Ilopango");
            insertarDistrito(db, "DIS_SS4", "MUN_SS4", "San Marcos");

            insertarDistrito(db, "DIS_SO1", "MUN_SO1", "Sonsonate");
            insertarDistrito(db, "DIS_SO2", "MUN_SO2", "Nahuizalco");
            insertarDistrito(db, "DIS_SO3", "MUN_SO3", "Izalco");
            insertarDistrito(db, "DIS_SO4", "MUN_SO4", "Armenia");

            insertarDistrito(db, "DIS_US1", "MUN_US1", "Usulutan");
            insertarDistrito(db, "DIS_US2", "MUN_US2", "Jiquilisco");
            insertarDistrito(db, "DIS_US3", "MUN_US3", "Santa Maria");
            insertarDistrito(db, "DIS_US4", "MUN_US4", "Santiago de Maria");

            insertarDistrito(db, "DIS_SV1", "MUN_SV1", "San Vicente");
            insertarDistrito(db, "DIS_SV2", "MUN_SV2", "Apastepeque");
            insertarDistrito(db, "DIS_SV3", "MUN_SV3", "Tecoluca");
            insertarDistrito(db, "DIS_SV4", "MUN_SV4", "Guadalupe");

            insertarDistrito(db, "DIS_UN1", "MUN_UN1", "La Union");
            insertarDistrito(db, "DIS_UN2", "MUN_UN2", "Conchagua");
            insertarDistrito(db, "DIS_UN3", "MUN_UN3", "Santa Rosa de Lima");
            insertarDistrito(db, "DIS_UN4", "MUN_UN4", "Yucuaiquin");
        }

        private void insertarDepartamento(SQLiteDatabase db, String codDpto, String nombreDpto) {
            ContentValues valores = new ContentValues();
            valores.put("COD_DPTO", codDpto);
            valores.put("NOMBRE_DPTO", nombreDpto);
            db.insertWithOnConflict("DEPARTAMENTO", null, valores, SQLiteDatabase.CONFLICT_IGNORE);
        }

        private void insertarEspecialidadInicial(SQLiteDatabase db, String nombreEspecialidad) {
            if (existeValor(db, "ESPECIALIDAD", "NOMBRE_ESPECIALIDAD", nombreEspecialidad)) {
                return;
            }

            ContentValues valores = new ContentValues();
            valores.put("NOMBRE_ESPECIALIDAD", nombreEspecialidad);
            db.insert("ESPECIALIDAD", null, valores);
        }

        private void insertarTipoEmergencia(SQLiteDatabase db, String codEmergencia, String prioridad, double costo) {
            ContentValues valores = new ContentValues();
            valores.put("COD_EMERGENCIA", codEmergencia);
            valores.put("PRIORIDAD", prioridad);
            valores.put("COSTO_EMERGENCIA", costo);
            db.insertWithOnConflict("TIPO_EMERGENCIA", null, valores, SQLiteDatabase.CONFLICT_IGNORE);
        }

        private void insertarMedicamento(SQLiteDatabase db, String codMedicamento, String nombreMedicamento,
                                         String fechaVencimiento, int cantidadInventario,
                                         double precioVenta, String lote) {
            ContentValues valores = new ContentValues();
            valores.put("COD_MEDICAMENTO", codMedicamento);
            valores.put("NOMBRE_MEDICAMENTO", nombreMedicamento);
            valores.put("FECHA_VENCIMIENTO", fechaVencimiento);
            valores.put("CANTIDAD_INVENTARIO", cantidadInventario);
            valores.put("PRECIO_VENTA", precioVenta);
            valores.put("LOTE", lote);
            db.insertWithOnConflict("MEDICAMENTO", null, valores, SQLiteDatabase.CONFLICT_IGNORE);
        }

        private void insertarUsuario(SQLiteDatabase db, String idUsuario, String nombreUsuario, String clave) {
            ContentValues valores = new ContentValues();
            valores.put("ID_USUARIO", idUsuario);
            valores.put("NOMBRE_USUARIO", nombreUsuario);
            valores.put("CLAVE", clave);
            db.insertWithOnConflict("USUARIO", null, valores, SQLiteDatabase.CONFLICT_IGNORE);
        }

        private void insertarAseguradoraInicial(SQLiteDatabase db, String nombreAseguradora, String telefonoAseguradora) {
            if (existeValor(db, "ASEGURADORA", "NOMBRE_ASEGURADORA", nombreAseguradora)) {
                return;
            }

            ContentValues valores = new ContentValues();
            valores.put("NOMBRE_ASEGURADORA", nombreAseguradora);
            valores.put("TELEFONO_ASEGURADORA", telefonoAseguradora);
            db.insert("ASEGURADORA", null, valores);
        }

        private void insertarOpcionCrud(SQLiteDatabase db, String idOpcion, String descripcion, int numCrud) {
            ContentValues valores = new ContentValues();
            valores.put("ID_OPCION", idOpcion);
            valores.put("DESCRIPCION_OPC", descripcion);
            valores.put("NUM_CRUD", numCrud);
            db.insertWithOnConflict("OPCION_CRUD", null, valores, SQLiteDatabase.CONFLICT_IGNORE);
        }

        private void insertarPuedeElegir(SQLiteDatabase db, String idUsuario, String idOpcion){
            ContentValues valores= new ContentValues();
            valores.put("ID_USUARIO",idUsuario);
            valores.put("ID_OPCION",idOpcion);
            db.insertWithOnConflict("PUEDE_ELEGIR",null,valores,SQLiteDatabase.CONFLICT_IGNORE);
        }
        private void insertarMunicipio(SQLiteDatabase db, String codMunicipio, String codDpto, String nombreMunicipio) {
            ContentValues valores = new ContentValues();
            valores.put("COD_MUNICIPIO", codMunicipio);
            valores.put("COD_DPTO", codDpto);
            valores.put("NOMBRE_MUNICIPIO", nombreMunicipio);
            db.insertWithOnConflict("MUNICIPIO", null, valores, SQLiteDatabase.CONFLICT_IGNORE);
        }

        private void insertarDistrito(SQLiteDatabase db, String codDistrito, String codMunicipio, String nombreDistrito) {
            ContentValues valores = new ContentValues();
            valores.put("COD_DISTRITO", codDistrito);
            valores.put("COD_MUNICIPIO", codMunicipio);
            valores.put("NOMBRE_DISTRITO", nombreDistrito);
            db.insertWithOnConflict("DISTRITO", null, valores, SQLiteDatabase.CONFLICT_IGNORE);
        }

        private boolean existeValor(SQLiteDatabase db, String tabla, String columna, String valor) {
            Cursor cursor = null;

            try {
                cursor = db.query(
                        tabla,
                        new String[]{columna},
                        columna + " = ?",
                        new String[]{valor},
                        null,
                        null,
                        null
                );

                return cursor.moveToFirst();

            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TRIGGER IF EXISTS validar_vencimiento_medicamento");
            db.execSQL("DROP TRIGGER IF EXISTS validar_vencimiento_medicamento_actualizado");
            db.execSQL("DROP TRIGGER IF EXISTS calcular_subtotal_item");
            db.execSQL("DROP TRIGGER IF EXISTS calcular_subtotal_update");
            db.execSQL("DROP TRIGGER IF EXISTS asignar_especialidad_general_auto");
            db.execSQL("DROP TRIGGER IF EXISTS eliminar_especialidades_cascada");
            db.execSQL("DROP TRIGGER IF EXISTS registrar_hospitalizacion_en_consulta");
            db.execSQL("DROP TRIGGER IF EXISTS guardar_precio_unitario_historico");
            db.execSQL("DROP TRIGGER IF EXISTS retiro_superior_al_stock");
            db.execSQL("DROP TRIGGER IF EXISTS actualizar_inventario_stock");
            db.execSQL("DROP TRIGGER IF EXISTS validar_especialidad_hospital_doctor");
            db.execSQL("DROP TRIGGER IF EXISTS calcular_pago_final_con_seguro");
            db.execSQL("DROP TRIGGER IF EXISTS actualizar_total_por_hospitalizacion");
            db.execSQL("DROP TRIGGER IF EXISTS actualizar_total_por_detalle_receta");
            db.execSQL("DROP TRIGGER IF EXISTS recalcular_pago_por_cambio_consulta");
            db.execSQL("DROP TRIGGER IF EXISTS calcular_total_consulta_base");

            db.execSQL("DROP TABLE IF EXISTS VENDEN");
            db.execSQL("DROP TABLE IF EXISTS PUEDE_ELEGIR");
            db.execSQL("DROP TABLE IF EXISTS TRABAJA_CON");
            db.execSQL("DROP TABLE IF EXISTS FORMADO_POR");
            db.execSQL("DROP TABLE IF EXISTS POSEE");
            db.execSQL("DROP TABLE IF EXISTS CUENTA_CON");
            db.execSQL("DROP TABLE IF EXISTS HOSPITALIZACION");
            db.execSQL("DROP TABLE IF EXISTS PAGO");
            db.execSQL("DROP TABLE IF EXISTS DETALLE_RECETA");
            db.execSQL("DROP TABLE IF EXISTS RECETA");
            db.execSQL("DROP TABLE IF EXISTS CONSULTA");
            db.execSQL("DROP TABLE IF EXISTS SEGURO");
            db.execSQL("DROP TABLE IF EXISTS PACIENTE");
            db.execSQL("DROP TABLE IF EXISTS DOCTOR");
            db.execSQL("DROP TABLE IF EXISTS HOSPITAL");
            db.execSQL("DROP TABLE IF EXISTS ESTABLECIMIENTO");
            db.execSQL("DROP TABLE IF EXISTS DISTRITO");
            db.execSQL("DROP TABLE IF EXISTS MUNICIPIO");
            db.execSQL("DROP TABLE IF EXISTS OPCION_CRUD");
            db.execSQL("DROP TABLE IF EXISTS ASEGURADORA");
            db.execSQL("DROP TABLE IF EXISTS USUARIO");
            db.execSQL("DROP TABLE IF EXISTS MEDICAMENTO");
            db.execSQL("DROP TABLE IF EXISTS TIPO_EMERGENCIA");
            db.execSQL("DROP TABLE IF EXISTS ESPECIALIDAD");
            db.execSQL("DROP TABLE IF EXISTS DEPARTAMENTO");

            onCreate(db);
        }
    }

    public void abrir() throws SQLException {
        db = DBHelper.getWritableDatabase();
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    public void cerrar() {
        if (DBHelper != null) {
            DBHelper.close();
        }
        db = null;
    }

    public SQLiteDatabase getDb() {
        return db;
    }

    // =========================================================
    // CRUD PACIENTE
    // =========================================================

    public String insertarPaciente(Paciente paciente) {
        try {
            ContentValues valores = new ContentValues();

            valores.put("DUI_PACIENTE", paciente.getDuiPaciente());

            if (paciente.getIdPoliza() != null) {
                valores.put("ID_POLIZA", paciente.getIdPoliza());
            } else {
                valores.putNull("ID_POLIZA");
            }

            valores.put("COD_DISTRITO", paciente.getCodDistrito());
            valores.put("PRIMER_NOMBRE_PACIENTE", paciente.getPrimerNombrePaciente());
            valores.put("SEGUNDO_NOMBRE_PACIENTE", paciente.getSegundoNombrePaciente());
            valores.put("PRIMER_APELLIDO_PACIENTE", paciente.getPrimerApellidoPaciente());
            valores.put("SEGUNDO_APELLIDO_PACIENTE", paciente.getSegundoApellidoPaciente());
            valores.put("FECHA_NACIMIENTO_PACIENTE", paciente.getFechaNacimientoPaciente());
            valores.put("GENERO_PACIENTE", paciente.getGeneroPaciente());
            valores.put("TELEFONO_PACIENTE", paciente.getTelefonoPaciente());

            long resultado = db.insertOrThrow("PACIENTE", null, valores);

            if (resultado == -1) {
                return "Error al insertar paciente.";
            }

            return "Paciente insertado correctamente.";

        } catch (SQLiteConstraintException e) {
            return "Error de integridad: " + e.getMessage();
        } catch (Exception e) {
            return "Error al insertar paciente: " + e.getMessage();
        }
    }

    public Paciente consultarPaciente(String duiPaciente) {
        Cursor cursor = null;

        try {
            cursor = db.query(
                    "PACIENTE",
                    null,
                    "DUI_PACIENTE = ?",
                    new String[]{duiPaciente},
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                return cursorAPaciente(cursor);
            }

            return null;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public ArrayList<Paciente> consultarTodosPacientes() {
        ArrayList<Paciente> listaPacientes = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.query(
                    "PACIENTE",
                    null,
                    null,
                    null,
                    null,
                    null,
                    "PRIMER_APELLIDO_PACIENTE ASC"
            );

            if (cursor.moveToFirst()) {
                do {
                    Paciente paciente = cursorAPaciente(cursor);
                    listaPacientes.add(paciente);
                } while (cursor.moveToNext());
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return listaPacientes;
    }

    public String actualizarPaciente(Paciente paciente) {
        try {
            ContentValues valores = new ContentValues();

            if (paciente.getIdPoliza() != null) {
                valores.put("ID_POLIZA", paciente.getIdPoliza());
            } else {
                valores.putNull("ID_POLIZA");
            }

            valores.put("COD_DISTRITO", paciente.getCodDistrito());
            valores.put("PRIMER_NOMBRE_PACIENTE", paciente.getPrimerNombrePaciente());
            valores.put("SEGUNDO_NOMBRE_PACIENTE", paciente.getSegundoNombrePaciente());
            valores.put("PRIMER_APELLIDO_PACIENTE", paciente.getPrimerApellidoPaciente());
            valores.put("SEGUNDO_APELLIDO_PACIENTE", paciente.getSegundoApellidoPaciente());
            valores.put("FECHA_NACIMIENTO_PACIENTE", paciente.getFechaNacimientoPaciente());
            valores.put("GENERO_PACIENTE", paciente.getGeneroPaciente());
            valores.put("TELEFONO_PACIENTE", paciente.getTelefonoPaciente());

            int filas = db.update(
                    "PACIENTE",
                    valores,
                    "DUI_PACIENTE = ?",
                    new String[]{paciente.getDuiPaciente()}
            );

            if (filas > 0) {
                return "Paciente actualizado correctamente.";
            } else {
                return "No se encontró el paciente.";
            }

        } catch (SQLiteConstraintException e) {
            return "Error de integridad: " + e.getMessage();
        } catch (Exception e) {
            return "Error al actualizar paciente: " + e.getMessage();
        }
    }

    public String eliminarPaciente(String duiPaciente) {
        try {
            int filas = db.delete(
                    "PACIENTE",
                    "DUI_PACIENTE = ?",
                    new String[]{duiPaciente}
            );

            if (filas > 0) {
                return "Paciente eliminado correctamente.";
            } else {
                return "No se encontró el paciente.";
            }

        } catch (SQLiteConstraintException e) {
            return "No se puede eliminar el paciente porque tiene registros relacionados.";
        } catch (Exception e) {
            return "Error al eliminar paciente: " + e.getMessage();
        }
    }

    private Paciente cursorAPaciente(Cursor cursor) {
        Paciente paciente = new Paciente();

        paciente.setDuiPaciente(cursor.getString(cursor.getColumnIndexOrThrow("DUI_PACIENTE")));

        int indexPoliza = cursor.getColumnIndexOrThrow("ID_POLIZA");
        if (!cursor.isNull(indexPoliza)) {
            paciente.setIdPoliza(cursor.getInt(indexPoliza));
        } else {
            paciente.setIdPoliza(null);
        }

        paciente.setCodDistrito(cursor.getString(cursor.getColumnIndexOrThrow("COD_DISTRITO")));
        paciente.setPrimerNombrePaciente(cursor.getString(cursor.getColumnIndexOrThrow("PRIMER_NOMBRE_PACIENTE")));
        paciente.setSegundoNombrePaciente(cursor.getString(cursor.getColumnIndexOrThrow("SEGUNDO_NOMBRE_PACIENTE")));
        paciente.setPrimerApellidoPaciente(cursor.getString(cursor.getColumnIndexOrThrow("PRIMER_APELLIDO_PACIENTE")));
        paciente.setSegundoApellidoPaciente(cursor.getString(cursor.getColumnIndexOrThrow("SEGUNDO_APELLIDO_PACIENTE")));
        paciente.setFechaNacimientoPaciente(cursor.getString(cursor.getColumnIndexOrThrow("FECHA_NACIMIENTO_PACIENTE")));
        paciente.setGeneroPaciente(cursor.getString(cursor.getColumnIndexOrThrow("GENERO_PACIENTE")));
        paciente.setTelefonoPaciente(cursor.getString(cursor.getColumnIndexOrThrow("TELEFONO_PACIENTE")));

        return paciente;
    }
    // =========================================================
    // MÉTODOS PARA PUEDE_ELEGIR (PERMISOS)
    // =========================================================
    public ArrayList<Opcion_crud> consultarPermisos(){
        ArrayList<Opcion_crud> lista = new ArrayList<>();
        Cursor cursor = null;

        try{
            cursor = db.rawQuery("SELECT ID_OPCION,DESCRIPCION_OPC FROM OPCION_CRUD ORDER BY ID_OPCION ASC",
                    null);
            if (cursor.moveToFirst()){
                do {
                    Opcion_crud opcionCrud = new Opcion_crud();

                    opcionCrud.setIdopcion(cursor.getString(cursor.getColumnIndexOrThrow("ID_OPCION")));
                    opcionCrud.setDescripcion(cursor.getString(cursor.getColumnIndexOrThrow("DESCRIPCION_OPC")));

                    lista.add(opcionCrud);
                }while (cursor.moveToNext());
            }
        }finally {
            if (cursor!=null){
                cursor.close();
            }
        }
        return lista;
    }

    public String insertarPermiso(String idUsuario,String idPermiso){
        try{
            ContentValues values = new ContentValues();
            values.put("ID_USUARIO",idUsuario);
            values.put("ID_OPCION",idPermiso);

            long control = db.insert("PUEDE_ELEGIR",null,values);

            if (control==-1){
                return "Error al crear permiso";
            }
            return "Permiso creado exitosamente";
        }catch (Exception e){
            return "Fallo al crear permiso: "+e.getMessage();
        }
    }

    public ArrayList<Puede_elegir> consultarPermisos(String idUsuario){
        ArrayList<Puede_elegir> lista = new ArrayList<>();
        Cursor cursor = null;

        try{
            cursor = db.rawQuery("SELECT * FROM PUEDE_ELEGIR WHERE ID_USUARIO =?",
                    new String[]{String.valueOf(idUsuario)});

            if(cursor.moveToFirst()){
                do {
                    Puede_elegir puedeElegir = new Puede_elegir();
                    puedeElegir.setId_usuario(cursor.getString(cursor.getColumnIndexOrThrow("ID_USUARIO")));
                    puedeElegir.setId_opcion(cursor.getString(cursor.getColumnIndexOrThrow("ID_OPCION")));

                    lista.add(puedeElegir);
                }while(cursor.moveToNext());
            }
        }finally {
            if (cursor!=null){
                cursor.close();
            }
        }
        return lista;
    }

    public String eliminarPermiso(String idUsuario,String idOpcion){
        try{
            int control = db.delete("PUEDE_ELEGIR","ID_USUARIO=? AND ID_OPCION=?",
                    new String[]{idUsuario,idOpcion});
            if (control>0){
                return "Permiso eliminado";
            }else {
                return "Error en eliminacion";
            }
        }catch (Exception e){
            return "No se pudo eliminar el permiso de usuario por: "+e.getMessage();
        }
    }
    // =========================================================
    // MÉTODOS PARA USUARIOS
    // =========================================================
    public String insertarUsuario(Usuario usuario){
        try{
            ContentValues values = new ContentValues();
            values.put("ID_USUARIO",usuario.getIdUsuario());
            values.put("NOMBRE_USUARIO",usuario.getNombreUsuario());
            values.put("CLAVE",usuario.getClave());

            long control = db.insert("USUARIO",null,values);

            if (control==-1){
                return "Error al crear usuario";
            }
            return "Usuario creado exitosamente";
        }catch (Exception e){
            return "Error al crear usuario: "+e.getMessage();
        }
    }

    public ArrayList<Usuario> consultarUsuarios(){
        ArrayList<Usuario> lista = new ArrayList<>();
        Cursor cursor = null;

        try{
            cursor = db.rawQuery(
                    "SELECT * FROM USUARIO ORDER BY ID_USUARIO ASC",
                    null
            );
            if (cursor.moveToFirst()){
                do {
                    Usuario usuario = new Usuario();

                    usuario.setIdUsuario(cursor.getString(cursor.getColumnIndexOrThrow("ID_USUARIO")));
                    usuario.setNombreUsuario(cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_USUARIO")));

                    lista.add(usuario);
                }while (cursor.moveToNext());
            }
        }finally {
            if (cursor!=null){
                cursor.close();
            }
        }
        return lista;
    }

    public Usuario consultarUsuario(String idUsuario){
        Cursor cursor = null;

        try{
            cursor=db.rawQuery(
                    "SELECT * FROM USUARIO WHERE ID_USUARIO = ?",
                    new String[]{String.valueOf(idUsuario)}
            );

            if (cursor.moveToFirst()){
                Usuario usuario =new Usuario();
                usuario.setIdUsuario(cursor.getString(cursor.getColumnIndexOrThrow("ID_USUARIO")));
                usuario.setNombreUsuario(cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_USUARIO")));
                usuario.setClave(cursor.getString(cursor.getColumnIndexOrThrow("CLAVE")));

                return usuario;
            }
            return null;
        }finally {
            {
                if (cursor!=null){
                    cursor.close();
                }
            }
        }
    }

    public String actualizarUsuario(Usuario usuario){
        try{
            ContentValues values = new ContentValues();
            values.put("NOMBRE_USUARIO",usuario.getNombreUsuario());
            values.put("CLAVE",usuario.getClave());
            int control = db.update("USUARIO",values,"ID_USUARIO =?",new String[]{
                    String.valueOf(usuario.getIdUsuario())
            });

            if (control>0){
                return "Usuario actualizado correctamente";
            }else {
                return "Usuario no encontrado";
            }
        }catch (Exception e){
            return "Error al actualizar usuario";
        }
    }

    public String eliminarUsuario(String idUsuario){
        try{
            int control = db.delete("USUARIO","ID_USUARIO=?",
                    new String[]{String.valueOf(idUsuario)});
            if (control>0){
                return "Usuario eliminado";
            }else {
                return "Usuario no encontrado";
            }
        }catch (Exception e){
            return "No se puede eliminar. \nEl usuario cuenta con permisos existentes.";
        }
    }
    // =========================================================
    // MÉTODOS PARA ESPECIALIDAD
    // =========================================================

    public String insertarEspecialidad(Especialidad especialidad) {
        try {
            ContentValues values = new ContentValues();
            values.put("NOMBRE_ESPECIALIDAD", especialidad.getNombreEspecialidad());

            long control = db.insert("ESPECIALIDAD", null, values);

            if (control == -1) {
                return "Error al insertar especialidad";
            }

            return "Especialidad registrada correctamente";

        } catch (Exception e) {
            return "Error al insertar especialidad: " + e.getMessage();
        }
    }

    public ArrayList<Especialidad> consultarEspecialidades() {
        ArrayList<Especialidad> lista = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(
                    "SELECT * FROM ESPECIALIDAD ORDER BY NOMBRE_ESPECIALIDAD ASC",
                    null
            );

            if (cursor.moveToFirst()) {
                do {
                    Especialidad especialidad = new Especialidad();

                    especialidad.setIdEspecialidad(cursor.getInt(cursor.getColumnIndexOrThrow("ID_ESPECIALIDAD")));
                    especialidad.setNombreEspecialidad(cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_ESPECIALIDAD")));

                    lista.add(especialidad);

                } while (cursor.moveToNext());
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return lista;
    }

    public Especialidad consultarEspecialidad(int idEspecialidad) {
        Cursor cursor = null;

        try {
            cursor = db.rawQuery(
                    "SELECT * FROM ESPECIALIDAD WHERE ID_ESPECIALIDAD = ?",
                    new String[]{String.valueOf(idEspecialidad)}
            );

            if (cursor.moveToFirst()) {
                Especialidad especialidad = new Especialidad();

                especialidad.setIdEspecialidad(cursor.getInt(cursor.getColumnIndexOrThrow("ID_ESPECIALIDAD")));
                especialidad.setNombreEspecialidad(cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_ESPECIALIDAD")));

                return especialidad;
            }

            return null;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String eliminarEspecialidad(int idEspecialidad) {
        try {
            int control = db.delete(
                    "ESPECIALIDAD",
                    "ID_ESPECIALIDAD = ?",
                    new String[]{String.valueOf(idEspecialidad)}
            );

            if (control > 0) {
                return "Especialidad eliminada correctamente";
            } else {
                return "Especialidad no encontrada";
            }

        } catch (Exception e) {
            return "No se puede eliminar.\nLa especialidad está asociada a hospitales o doctores.";
        }
    }

    public String actualizarEspecialidad(Especialidad especialidad) {
        try {
            ContentValues values = new ContentValues();
            values.put("NOMBRE_ESPECIALIDAD", especialidad.getNombreEspecialidad());

            int control = db.update(
                    "ESPECIALIDAD",
                    values,
                    "ID_ESPECIALIDAD = ?",
                    new String[]{String.valueOf(especialidad.getIdEspecialidad())}
            );

            if (control > 0) {
                return "Especialidad actualizada correctamente";
            } else {
                return "Especialidad no encontrada";
            }

        } catch (Exception e) {
            return "Error al actualizar especialidad";
        }
    }

    // =========================================================
    // CONSULTAS AUXILIARES PARA SPINNERS / LISTAS
    // =========================================================

    public Cursor consultarDistritosCursor() {
        return db.rawQuery(
                "SELECT COD_DISTRITO, COD_MUNICIPIO, NOMBRE_DISTRITO " +
                        "FROM DISTRITO " +
                        "ORDER BY NOMBRE_DISTRITO ASC",
                null
        );
    }

    public String obtenerNombreDistrito(String codDistrito) {
        Cursor cursor = null;

        try {
            cursor = db.query(
                    "DISTRITO",
                    new String[]{"NOMBRE_DISTRITO"},
                    "COD_DISTRITO = ?",
                    new String[]{codDistrito},
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_DISTRITO"));
            }

            return "";

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public String obtenerCodigoDistritoPorNombre(String nombreDistrito) {
        Cursor cursor = null;

        try {
            cursor = db.query(
                    "DISTRITO",
                    new String[]{"COD_DISTRITO"},
                    "NOMBRE_DISTRITO = ?",
                    new String[]{nombreDistrito},
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow("COD_DISTRITO"));
            }

            return "";

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // =========================================================
// CRUD ASEGURADORA
// =========================================================

    public String insertarAseguradora(Aseguradora aseguradora) {
        try {
            ContentValues valores = new ContentValues();
            valores.put("NOMBRE_ASEGURADORA", aseguradora.getNombreAseguradora());
            valores.put("TELEFONO_ASEGURADORA", aseguradora.getTelefonoAseguradora());

            long resultado = db.insertOrThrow("ASEGURADORA", null, valores);

            if (resultado == -1) {
                return "Error al insertar aseguradora.";
            }

            return "Aseguradora insertada correctamente.";

        } catch (SQLiteConstraintException e) {
            return "Error de integridad: " + e.getMessage();
        } catch (Exception e) {
            return "Error al insertar aseguradora: " + e.getMessage();
        }
    }

    public Aseguradora consultarAseguradora(int idAseguradora) {
        Cursor cursor = null;

        try {
            cursor = db.query(
                    "ASEGURADORA",
                    null,
                    "ID_ASEGURADORA = ?",
                    new String[]{String.valueOf(idAseguradora)},
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                return cursorAAseguradora(cursor);
            }

            return null;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public ArrayList<Aseguradora> consultarTodasAseguradoras() {
        ArrayList<Aseguradora> listaAseguradoras = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.query(
                    "ASEGURADORA",
                    null,
                    null,
                    null,
                    null,
                    null,
                    "NOMBRE_ASEGURADORA ASC"
            );

            if (cursor.moveToFirst()) {
                do {
                    Aseguradora aseguradora = cursorAAseguradora(cursor);
                    listaAseguradoras.add(aseguradora);
                } while (cursor.moveToNext());
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return listaAseguradoras;
    }

    public String actualizarAseguradora(Aseguradora aseguradora) {
        try {
            ContentValues valores = new ContentValues();
            valores.put("NOMBRE_ASEGURADORA", aseguradora.getNombreAseguradora());
            valores.put("TELEFONO_ASEGURADORA", aseguradora.getTelefonoAseguradora());

            int filas = db.update(
                    "ASEGURADORA",
                    valores,
                    "ID_ASEGURADORA = ?",
                    new String[]{String.valueOf(aseguradora.getIdAseguradora())}
            );

            if (filas > 0) {
                return "Aseguradora actualizada correctamente.";
            } else {
                return "No se encontró la aseguradora.";
            }

        } catch (SQLiteConstraintException e) {
            return "Error de integridad: " + e.getMessage();
        } catch (Exception e) {
            return "Error al actualizar aseguradora: " + e.getMessage();
        }
    }

    public String eliminarAseguradora(int idAseguradora) {
        try {
            int filas = db.delete(
                    "ASEGURADORA",
                    "ID_ASEGURADORA = ?",
                    new String[]{String.valueOf(idAseguradora)}
            );

            if (filas > 0) {
                return "Aseguradora eliminada correctamente.";
            } else {
                return "No se encontró la aseguradora.";
            }

        } catch (SQLiteConstraintException e) {
            return "No se puede eliminar la aseguradora porque tiene registros relacionados.";
        } catch (Exception e) {
            return "Error al eliminar aseguradora: " + e.getMessage();
        }
    }

    private Aseguradora cursorAAseguradora(Cursor cursor) {
        Aseguradora aseguradora = new Aseguradora();

        aseguradora.setIdAseguradora(cursor.getInt(cursor.getColumnIndexOrThrow("ID_ASEGURADORA")));
        aseguradora.setNombreAseguradora(cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_ASEGURADORA")));
        aseguradora.setTelefonoAseguradora(cursor.getString(cursor.getColumnIndexOrThrow("TELEFONO_ASEGURADORA")));

        return aseguradora;
    }

    // =========================================================
// CRUD ESTABLECIMIENTO
// =========================================================

    public String insertarEstablecimiento(Establecimiento establecimiento) {
        try {
            ContentValues valores = new ContentValues();
            valores.put("NOMBRE_ESTABLECIMIENTO", establecimiento.getNombreEstablecimiento());
            valores.put("TELEFONO_ESTABLECIMIENTO", establecimiento.getTelefonoEstablecimiento());
            valores.put("DIRECCION_ESTABLECIMIENTO", establecimiento.getDireccionEstablecimiento());

            long resultado = db.insertOrThrow("ESTABLECIMIENTO", null, valores);

            if (resultado == -1) {
                return "Error al insertar establecimiento.";
            }

            return "Establecimiento insertado correctamente.";

        } catch (SQLiteConstraintException e) {
            return "Error de integridad: " + e.getMessage();
        } catch (Exception e) {
            return "Error al insertar establecimiento: " + e.getMessage();
        }
    }

    public Establecimiento consultarEstablecimiento(int idEstablecimiento) {
        Cursor cursor = null;

        try {
            cursor = db.query(
                    "ESTABLECIMIENTO",
                    null,
                    "ID_ESTABLECIMIENTO = ?",
                    new String[]{String.valueOf(idEstablecimiento)},
                    null,
                    null,
                    null
            );

            if (cursor.moveToFirst()) {
                return cursorAEstablecimiento(cursor);
            }

            return null;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public ArrayList<Establecimiento> consultarTodosEstablecimientos() {
        ArrayList<Establecimiento> listaEstablecimientos = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.query(
                    "ESTABLECIMIENTO",
                    null,
                    null,
                    null,
                    null,
                    null,
                    "NOMBRE_ESTABLECIMIENTO ASC"
            );

            if (cursor.moveToFirst()) {
                do {
                    Establecimiento establecimiento = cursorAEstablecimiento(cursor);
                    listaEstablecimientos.add(establecimiento);
                } while (cursor.moveToNext());
            }

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return listaEstablecimientos;
    }

    public String actualizarEstablecimiento(Establecimiento establecimiento) {
        try {
            ContentValues valores = new ContentValues();
            valores.put("NOMBRE_ESTABLECIMIENTO", establecimiento.getNombreEstablecimiento());
            valores.put("TELEFONO_ESTABLECIMIENTO", establecimiento.getTelefonoEstablecimiento());
            valores.put("DIRECCION_ESTABLECIMIENTO", establecimiento.getDireccionEstablecimiento());

            int filas = db.update(
                    "ESTABLECIMIENTO",
                    valores,
                    "ID_ESTABLECIMIENTO = ?",
                    new String[]{String.valueOf(establecimiento.getIdEstablecimiento())}
            );

            if (filas > 0) {
                return "Establecimiento actualizado correctamente.";
            } else {
                return "No se encontró el establecimiento.";
            }

        } catch (SQLiteConstraintException e) {
            return "Error de integridad: " + e.getMessage();
        } catch (Exception e) {
            return "Error al actualizar establecimiento: " + e.getMessage();
        }
    }

    public String eliminarEstablecimiento(int idEstablecimiento) {
        try {
            int filas = db.delete(
                    "ESTABLECIMIENTO",
                    "ID_ESTABLECIMIENTO = ?",
                    new String[]{String.valueOf(idEstablecimiento)}
            );

            if (filas > 0) {
                return "Establecimiento eliminado correctamente.";
            } else {
                return "No se encontró el establecimiento.";
            }

        } catch (SQLiteConstraintException e) {
            return "No se puede eliminar el establecimiento porque tiene registros relacionados.";
        } catch (Exception e) {
            return "Error al eliminar establecimiento: " + e.getMessage();
        }
    }

    private Establecimiento cursorAEstablecimiento(Cursor cursor) {
        Establecimiento establecimiento = new Establecimiento();

        establecimiento.setIdEstablecimiento(cursor.getInt(cursor.getColumnIndexOrThrow("ID_ESTABLECIMIENTO")));
        establecimiento.setNombreEstablecimiento(cursor.getString(cursor.getColumnIndexOrThrow("NOMBRE_ESTABLECIMIENTO")));
        establecimiento.setTelefonoEstablecimiento(cursor.getString(cursor.getColumnIndexOrThrow("TELEFONO_ESTABLECIMIENTO")));
        establecimiento.setDireccionEstablecimiento(cursor.getString(cursor.getColumnIndexOrThrow("DIRECCION_ESTABLECIMIENTO")));

        return establecimiento;
    }


    // =========================================================
    // MÉTODOS GENÉRICOS OPCIONALES
    // =========================================================

    public String insertarRegistro(String tabla, ContentValues valores) {
        try {
            long resultado = db.insertOrThrow(tabla, null, valores);

            if (resultado == -1) {
                return "Error al insertar registro.";
            }

            return "Registro insertado correctamente. ID/Fila: " + resultado;

        } catch (SQLiteConstraintException e) {
            return "Error de integridad: " + e.getMessage();
        } catch (Exception e) {
            return "Error al insertar: " + e.getMessage();
        }
    }

    public Cursor consultarRegistros(String tabla, String[] columnas, String where, String[] whereArgs, String orderBy) {
        return db.query(tabla, columnas, where, whereArgs, null, null, orderBy);
    }

    public String actualizarRegistro(String tabla, ContentValues valores, String where, String[] whereArgs) {
        try {
            int filas = db.update(tabla, valores, where, whereArgs);

            if (filas > 0) {
                return "Registro actualizado correctamente. Filas afectadas: " + filas;
            } else {
                return "No se encontró ningún registro para actualizar.";
            }

        } catch (SQLiteConstraintException e) {
            return "Error de integridad: " + e.getMessage();
        } catch (Exception e) {
            return "Error al actualizar: " + e.getMessage();
        }
    }

    public String eliminarRegistro(String tabla, String where, String[] whereArgs) {
        try {
            int filas = db.delete(tabla, where, whereArgs);

            if (filas > 0) {
                return "Registro eliminado correctamente. Filas afectadas: " + filas;
            } else {
                return "No se encontró ningún registro para eliminar.";
            }

        } catch (SQLiteConstraintException e) {
            return "Error de integridad: " + e.getMessage();
        } catch (Exception e) {
            return "Error al eliminar: " + e.getMessage();
        }
    }

    public boolean existeRegistro(String tabla, String where, String[] whereArgs) {
        Cursor cursor = null;

        try {
            cursor = db.query(
                    tabla,
                    new String[]{"1"},
                    where,
                    whereArgs,
                    null,
                    null,
                    null
            );

            return cursor.moveToFirst();

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    // =========================================================
    // DATOS INICIALES
    // =========================================================

    public String llenarDatosIniciales() {
        try {
            if (db == null || !db.isOpen()) {
                return "La base de datos no está abierta.";
            }

            if (datosInicialesCargados()) {
                return "Los datos iniciales ya estaban cargados.";
            }

            db.beginTransaction();

            DatabaseHelper helper = DBHelper;
            helper.llenarDatosIniciales(db);

            db.setTransactionSuccessful();

            return "Datos iniciales cargados correctamente.";

        } catch (Exception e) {
            return "Error al cargar datos iniciales: " + e.getMessage();

        } finally {
            if (db != null && db.inTransaction()) {
                db.endTransaction();
            }
        }
    }

    private boolean datosInicialesCargados() {
        return contarRegistros("DEPARTAMENTO") > 0 &&
                contarRegistros("MUNICIPIO") > 0 &&
                contarRegistros("DISTRITO") > 0 &&
                contarRegistros("USUARIO") > 0;
    }

    private int contarRegistros(String tabla) {
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("SELECT COUNT(*) FROM " + tabla, null);

            if (cursor.moveToFirst()) {
                return cursor.getInt(0);
            }

            return 0;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
