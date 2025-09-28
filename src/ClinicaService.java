import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClinicaService {

    private List<Doctor> doctores;
    private List<Paciente> pacientes;
    private List<Cita> citas;
    private FileManager fileManager  = new FileManager(); // Referencia a FileManager


    //Contadores para IDs secuenciales
    private  int doctorIdCounter = 0;
    private  int pacienteIdCounter = 0;
    private  int citaIdCounter = 0;


    public void actualizarContadores() {
        // Patrón para extraer números de los IDs (ej. DOCTS15 -> 15)
        Pattern pattern = Pattern.compile("\\d+");
        Matcher matcher;
        int maxId;

        // --- Doctores ---
        maxId = 0;
        for (Doctor d : doctores) {
            matcher = pattern.matcher(d.getId());
            if (matcher.find()) {
                try {
                    int idNum = Integer.parseInt(matcher.group());
                    if (idNum > maxId) {
                        maxId = idNum;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Advertencia: Formato de ID de doctor no válido: " + d.getId());
                }
            }
        }
        this.doctorIdCounter = maxId;

        // --- Pacientes ---
        maxId = 0;
        for (Paciente p : pacientes) {
            matcher = pattern.matcher(p.getId());
            if (matcher.find()) {
                try {
                    int idNum = Integer.parseInt(matcher.group());
                    if (idNum > maxId) {
                        maxId = idNum;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Advertencia: Formato de ID de paciente no válido: " + p.getId());
                }
            }
        }
        this.pacienteIdCounter = maxId;

        // --- Citas ---
        maxId = 0;
        for (Cita c : citas) {
            matcher = pattern.matcher(c.getId());
            if (matcher.find()) {
                try {
                    int idNum = Integer.parseInt(matcher.group());
                    if (idNum > maxId) {
                        maxId = idNum;
                    }
                } catch (NumberFormatException e) {
                    System.err.println("Advertencia: Formato de ID de cita no válido: " + c.getId());
                }
            }
        }
        this.citaIdCounter = maxId;

        System.out.println("✅ Contadores de IDs actualizados. Siguiente Doctor: " + (this.doctorIdCounter + 1) +
                ", Siguiente Paciente: " + (this.pacienteIdCounter + 1) +
                ", Siguiente Cita: " + (this.citaIdCounter + 1));
    }

    public ClinicaService() {
        this.doctores = new ArrayList<>();
        this.pacientes = new ArrayList<>();
        this.citas = new ArrayList<>();
    }

    // Método para registrar un doctor
    public void registrarDoctor(String nombre, String especialidad) {
        //Validacion de datos
        if (nombre == null || especialidad == null){
            System.err.println("Error: El nombre y la especialidad son campos obligatorios");
            return;
        }
        //Generar ID, incrementar contador y crear objeto
        this.doctorIdCounter++;
        //String id = UUID.randomUUID().toString();
        String id = "DOC-" + this.doctorIdCounter;

        Doctor nuevoDoctor = new Doctor(id, nombre, especialidad);
        doctores.add(nuevoDoctor);
        System.out.println("Doctor registrado con éxito. ID: " + id);

        // ¡Guardar los datos inmediatamente!
        fileManager.guardarDatos(doctores, pacientes, citas);
    }

    // Método para registrar un paciente
    public void registrarPaciente(String nombre) {
        //Validacion de datos
        if (nombre == null){
            System.err.println("Error: El nombre es un campo obligatorio");
            return;
        }

        //Generar ID, incrementar  crear el objeto
        this.pacienteIdCounter++;
        String id = "PAC-" + pacienteIdCounter;

        //String id = UUID.randomUUID().toString();
        Paciente nuevoPaciente = new Paciente(id, nombre);
        pacientes.add(nuevoPaciente);
        System.out.println("Paciente registrado con éxito. ID: " + id);

        // ¡Guardar los datos inmediatamente!
        fileManager.guardarDatos(doctores, pacientes, citas);

    }

    // Método para crear una cita
    public void crearCita(String idPaciente, String idDoctor, String motivo) {
        //Validacion de datos
        if (idPaciente == null || idPaciente == null || motivo == null){
            System.err.println("Error: El nombre y la especialidad son campos obligatorios");
            return;
        }
        // Validación básica de IDs (esta lógica se mejorará más adelante)
        else if (buscarDoctorPorId(idDoctor) == null || buscarPacientePorId(idPaciente) == null) {
            System.out.println("Error: ID de doctor o paciente no encontrado.");
            return;
        }

        //Generar ID, incrementar contador y crear el objeto
        this.citaIdCounter++;
        String idCita = "CITA-" + citaIdCounter;

        // 1. Obtener la Fecha y Hora actuales usando java.time
        LocalDate fechaActual = LocalDate.now();
        LocalTime horaActual = LocalTime.now();

        // 2. Definir el formato deseado (ej: dd/MM/yyyy y HH:mm:ss)
        DateTimeFormatter fechaFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter horaFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        String fecha = fechaActual.format(fechaFormatter);
        String hora = horaActual.format(horaFormatter);

        //String idCita = UUID.randomUUID().toString();
        Cita nuevaCita = new Cita(idCita, idPaciente, idDoctor, fecha, hora, motivo);
        citas.add(nuevaCita);
        System.out.println("Cita creada con éxito. ID: " + idCita);
        System.out.println("Datos de la cita: ");

        // ¡Guardar los datos inmediatamente!
        fileManager.guardarDatos(doctores, pacientes, citas);
    }

    // Método para mostrar todos los doctores
    public void mostrarDoctores() {
        if (doctores.isEmpty()) {
            System.out.println("No hay doctores registrados.");
            return;
        }
        System.out.println("\n--- Lista de Doctores ---");
        for (Doctor doctor : doctores) {
            System.out.println("ID: " + doctor.getId() + ", Nombre: " + doctor.getNombreCompleto() + ", Especialidad: " + doctor.getEspecialidad());
        }
    }

    // Método para mostrar todos los pacientes
    public void mostrarPacientes() {
        if (pacientes.isEmpty()) {
            System.out.println("No hay pacientes registrados.");
            return;
        }
        System.out.println("\n--- Lista de Pacientes ---");
        for (Paciente paciente : pacientes) {
            System.out.println("ID: " + paciente.getId() + ", Nombre: " + paciente.getNombre());
        }
    }

    // Método para mostrar todas las citas
    public void mostrarCitas() {
        if (citas.isEmpty()) {
            System.out.println("No hay citas registradas.");
            return;
        }
        System.out.println("\n--- Lista de Citas ---");
        for (Cita cita : citas) {
            System.out.println("ID: " + cita.getId() + ", ID Paciente: " + cita.getIdPaciente() + ", ID Doctor: " + cita.getIdDoctor() + ", Fecha cita: " + cita.getFecha() + ", Hora cita: " + cita.getHora() + ", Motivo: " + cita.getMotivo());
        }
    }

    // Métodos auxiliares para la búsqueda (necesarios para crear citas)
    private Doctor buscarDoctorPorId(String id) {
        for (Doctor doctor : doctores) {
            if (doctor.getId().equals(id)) {
                return doctor;
            }
        }
        return null;
    }

    private Paciente buscarPacientePorId(String id) {
        for (Paciente paciente : pacientes) {
            if (paciente.getId().equals(id)) {
                return paciente;
            }
        }
        return null;
    }

    // Método para buscar una persona por su nombre
    public void buscarPersonaPorNombre(String nombreCompleto) {
        boolean encontrado = false;

        // Buscar en la lista de doctores
        for (Doctor doctor : doctores) {
            if (doctor.getNombreCompleto().equalsIgnoreCase(nombreCompleto)) {
                System.out.println("✅ Doctor encontrado:");
                System.out.println("ID: " + doctor.getId());
                System.out.println("Nombre: " + doctor.getNombreCompleto());
                System.out.println("Especialidad: " + doctor.getEspecialidad());
                encontrado = true;
                break;
            }
        }

        // Si no se encontró un doctor, buscar en la lista de pacientes
        if (!encontrado) {
            for (Paciente paciente : pacientes) {
                if (paciente.getNombreCompleto().equalsIgnoreCase(nombreCompleto)) {
                    System.out.println("✅ Paciente encontrado:");
                    System.out.println("ID: " + paciente.getId());
                    System.out.println("Nombre: " + paciente.getNombreCompleto());
                    encontrado = true;
                    break;
                }
            }
        }

        if (!encontrado) {
            System.out.println("No se encontró ninguna persona con ese nombre.");
        }
    }

    public List<Doctor> getDoctores() { return doctores; }
    public List<Paciente> getPacientes() { return pacientes; }
    public List<Cita> getCitas() { return citas; }

}