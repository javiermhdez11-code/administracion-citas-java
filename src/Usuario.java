public abstract class Usuario {
    protected String id;
    protected String nombre;

    public Usuario(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return this.id;
    }

    public String getNombre() {
        return this.nombre;
    }
}