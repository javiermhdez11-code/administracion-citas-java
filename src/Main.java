import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        AuthService authService = new AuthService();
        ClinicaService clinicaService = new ClinicaService();
        FileManager fileManager = new FileManager();

        //1. Cargar los datos al iniciar el programa
        fileManager.cargarDatos(clinicaService.getDoctores(), clinicaService.getPacientes(), clinicaService.getCitas());

        //2. Actualizar los contadores con el ID mas alto encontrado
        clinicaService.actualizarContadores();

        boolean autenticado = false;

        System.out.println("--- Sistema de Administración de Citas ---");

        while (!autenticado) {
            System.out.print("Usuario: ");
            String usuario = scanner.nextLine();
            System.out.print("Contraseña: ");
            String contrasena = scanner.nextLine();

            if (authService.autenticar(usuario, contrasena)) {
                System.out.println("\n¡Inicio de sesión exitoso! Bienvenido.");
                autenticado = true;
                ejecutarMenuPrincipal(scanner, clinicaService, fileManager);
                break;
            } else {
                System.out.println("Credenciales incorrectas. Por favor, intente de nuevo.");
            }
        }
        scanner.close();

    }

    private static void ejecutarMenuPrincipal(Scanner scanner, ClinicaService clinicaService, FileManager fileManager) {
        boolean salir = false;
        while (!salir) {
            System.out.println("\n--- Menú Principal ---");
            System.out.println("1. Registrar nuevo Doctor");
            System.out.println("2. Registrar nuevo Paciente");
            System.out.println("3. Crear nueva Cita");
            System.out.println("4. Mostrar todos los Doctores");
            System.out.println("5. Mostrar todos los Pacientes");
            System.out.println("6. Mostrar todas las Citas");
            System.out.println("7. Buscar persona por nombre");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "1":
                    System.out.print("Nombre del doctor: ");
                    String nombreDoctor = scanner.nextLine();
                    System.out.print("Especialidad: ");
                    String especialidad = scanner.nextLine();
                    clinicaService.registrarDoctor(nombreDoctor, especialidad);
                    //clinicaService.mostrarDoctores();
                    break;
                case "2":
                    System.out.print("Nombre del paciente: ");
                    String nombrePaciente = scanner.nextLine();
                    clinicaService.registrarPaciente(nombrePaciente);
                    break;
                case "3":
                    System.out.print("ID del paciente: ");
                    String idPaciente = scanner.nextLine();
                    System.out.print("ID del doctor: ");
                    String idDoctor = scanner.nextLine();
                    System.out.print("Motivo de la cita: ");
                    String motivo = scanner.nextLine();
                    clinicaService.crearCita(idPaciente, idDoctor, motivo);

                    break;
                case "4":
                    clinicaService.mostrarDoctores();
                    break;
                case "5":
                    clinicaService.mostrarPacientes();
                    break;
                case "6":
                    clinicaService.mostrarCitas();
                    break;
                case "7":
                    System.out.print("Ingrese el nombre completo de la persona a buscar: ");
                    String nombreABuscar = scanner.nextLine();
                    clinicaService.buscarPersonaPorNombre(nombreABuscar);
                    break;
                case "8":
                    salir = true;
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }
        }
    }
}