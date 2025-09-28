public class AuthService {

    // Credenciales fijas para el administrador
    private static final String USUARIO_ADMIN = "admin";
    private static final String CONTRASENA_ADMIN = "1234";

    /**
     * Verifica si el usuario y la contraseña son correctos.
     * @param usuario El nombre de usuario ingresado.
     * @param contrasena La contraseña ingresada.
     * @return true si las credenciales son válidas, false en caso contrario.
     */
    public boolean autenticar(String usuario, String contrasena) {
        return USUARIO_ADMIN.equals(usuario) && CONTRASENA_ADMIN.equals(contrasena);
    }
}