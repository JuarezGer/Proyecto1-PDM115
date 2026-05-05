package ues.fia.proyecto1pdm115;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import ues.fia.proyecto1pdm115.modelos.Paciente;

public class controlDBHospitalApp {

    private static final String BASE_DATOS = "hospital_app.db";
    private static final int VERSION = 1;

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

    public boolean validarLogin(String idUsuario, String clave) {
        Cursor cursor = null;

        try {
            cursor = db.query(
                    "USUARIO",
                    null,
                    "ID_USUARIO = ? AND CLAVE = ?",
                    new String[]{idUsuario, clave},
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

    public String obtenerNombreUsuario(String idUsuario) {
        Cursor cursor = null;

        try {
            cursor = db.query(
                    "USUARIO",
                    new String[]{"NOMBRE_USUARIO"},
                    "ID_USUARIO = ?",
                    new String[]{idUsuario},
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
                crearTablas(db);
                crearTriggers(db);
            } catch (SQLException e) {
                e.printStackTrace();
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
                    "THEN RAISE(ABORT, 'Error: No se puede registrar un medicamento vencido.') " +
                    "END; " +
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

            db.execSQL("CREATE TRIGGER calcular_total_consulta_base " +
                    "AFTER INSERT ON CONSULTA " +
                    "FOR EACH ROW " +
                    "BEGIN " +
                    "UPDATE CONSULTA " +
                    "SET CARGO_TOTAL_CONSULTA = ( " +
                    "SELECT COSTO_EMERGENCIA " +
                    "FROM TIPO_EMERGENCIA " +
                    "WHERE COD_EMERGENCIA = NEW.COD_EMERGENCIA " +
                    ") " +
                    "WHERE ID_CONSULTA = NEW.ID_CONSULTA; " +
                    "END;");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            db.execSQL("DROP TRIGGER IF EXISTS validar_vencimiento_medicamento");
            db.execSQL("DROP TRIGGER IF EXISTS validar_especialidad_hospital_doctor");
            db.execSQL("DROP TRIGGER IF EXISTS validar_vencimiento_medicamento_actualizado");
            db.execSQL("DROP TRIGGER IF EXISTS calcular_subtotal_item");
            db.execSQL("DROP TRIGGER IF EXISTS calcular_subtotal_update");
            db.execSQL("DROP TRIGGER IF EXISTS asignar_especialidad_general_auto");
            db.execSQL("DROP TRIGGER IF EXISTS eliminar_especialidades_cascada");
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
    }

    public void cerrar() {
        DBHelper.close();
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
    // DATOS INICIALES PARA PODER PROBAR PACIENTES
    // =========================================================

    public String llenarDatosIniciales() {
        try {
            ContentValues valores;

            valores = new ContentValues();
            valores.put("COD_DPTO", "SS");
            valores.put("NOMBRE_DPTO", "San Salvador");
            db.insertWithOnConflict("DEPARTAMENTO", null, valores, SQLiteDatabase.CONFLICT_IGNORE);

            valores = new ContentValues();
            valores.put("COD_MUNICIPIO", "SS01");
            valores.put("COD_DPTO", "SS");
            valores.put("NOMBRE_MUNICIPIO", "San Salvador Centro");
            db.insertWithOnConflict("MUNICIPIO", null, valores, SQLiteDatabase.CONFLICT_IGNORE);

            valores = new ContentValues();
            valores.put("COD_DISTRITO", "SS0101");
            valores.put("COD_MUNICIPIO", "SS01");
            valores.put("NOMBRE_DISTRITO", "San Salvador");
            db.insertWithOnConflict("DISTRITO", null, valores, SQLiteDatabase.CONFLICT_IGNORE);

            valores = new ContentValues();
            valores.put("ID_ESPECIALIDAD", 1);
            valores.put("NOMBRE_ESPECIALIDAD", "General");
            db.insertWithOnConflict("ESPECIALIDAD", null, valores, SQLiteDatabase.CONFLICT_IGNORE);

            valores = new ContentValues();
            valores.put("COD_EMERGENCIA", "EMG01");
            valores.put("PRIORIDAD", "Baja");
            valores.put("COSTO_EMERGENCIA", 25.00);
            db.insertWithOnConflict("TIPO_EMERGENCIA", null, valores, SQLiteDatabase.CONFLICT_IGNORE);

            valores = new ContentValues();
            valores.put("ID_USUARIO", "admin");
            valores.put("NOMBRE_USUARIO", "Administrador");
            valores.put("CLAVE", "1234");
            db.insertWithOnConflict("USUARIO", null, valores, SQLiteDatabase.CONFLICT_IGNORE);

            return "Datos iniciales guardados correctamente.";

        } catch (Exception e) {
            return "Error al llenar datos iniciales: " + e.getMessage();
        }
    }
}