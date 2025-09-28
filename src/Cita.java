public class Cita {
    private String id;
    private String idPaciente;
    private String idDoctor;
    private String fecha;
    private String hora;
    private String motivo;

    // Constructor
    public Cita(String id, String idPaciente, String idDoctor, String fecha, String hora, String motivo) {
        this.id = id;
        this.idPaciente = idPaciente;
        this.idDoctor = idDoctor;
        this.fecha = fecha; // Ahora asigna el String formateado
        this.hora = hora;   // Ahora asigna el String formateado
        this.motivo = motivo;
    }

    public String getId() { return this.id; }
    public String getIdPaciente() { return this.idPaciente; }
    public String getIdDoctor() { return this.idDoctor; }
    public String getFecha() { return this.fecha; }
    public String getHora() { return this.hora; }
    public String getMotivo() { return this.motivo; }
}