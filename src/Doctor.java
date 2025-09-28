public class Doctor extends Usuario implements IPersona {
    private String especialidad;

    public Doctor(String id, String nombre, String especialidad) {
        super(id, nombre);
        this.especialidad = especialidad;
    }

    public String getEspecialidad() {
        return this.especialidad;
    }

    @Override
    public String getNombreCompleto() {
        return this.nombre;
    }
}