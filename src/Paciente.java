public class Paciente extends Usuario implements IPersona {

    public Paciente(String id, String nombre) {
        super(id, nombre);
    }

    @Override
    public String getNombreCompleto() {
        return this.nombre;
    }
}