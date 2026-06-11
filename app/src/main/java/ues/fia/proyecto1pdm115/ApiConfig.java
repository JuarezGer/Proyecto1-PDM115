package ues.fia.proyecto1pdm115;

public class ApiConfig {

    // Cambiar IP por su red local, pueden agregar la linea y comentar la otra

    //public static final String BASE_URL = "http://10.32.28.71/hospital_api/";
    //public static final String BASE_URL = "http://192.168.0.8/hospital_api/";
    //public static final String BASE_URL = "http://192.168.1.97/hospital_api/";
    public static final String BASE_URL = "http://192.168.18.12/hospital_api/";

    public static final String REGISTER_USER = BASE_URL + "register_user.php";
    public static final String GET_USER = BASE_URL + "get_user.php";
    public static final String DELETE_USER = BASE_URL + "delete_user.php";

    public static final String GET_USUARIOS = BASE_URL + "get_usuarios.php";
    public static final String GET_OPCIONES = BASE_URL + "get_opciones.php";
    public static final String INSERT_PERMISO = BASE_URL + "insert_permiso.php";
    public static final String DELETE_PERMISO = BASE_URL + "delete_permiso.php";

}
