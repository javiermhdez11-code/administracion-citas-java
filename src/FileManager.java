import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;


import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class FileManager {
    private static final String DOCTORES_FILE = "db/doctores.csv";
    private static final String PACIENTES_FILE = "db/pacientes.csv";
    private static final String CITAS_FILE = "db/citas.csv";

    // Metodo para guardar los datos
    public void guardarDatos1(List<Doctor> doctores, List<Paciente> pacientes, List<Cita> citas) {
        try (FileWriter writer = new FileWriter(DOCTORES_FILE)) {
            for (Doctor d : doctores) {
                writer.append(d.getId()).append(",").append(d.getNombre()).append(",").append(d.getEspecialidad()).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo de doctores: " + e.getMessage());
        }

        try (FileWriter writer = new FileWriter(PACIENTES_FILE)) {
            for (Paciente p : pacientes) {
                writer.append(p.getId()).append(",").append(p.getNombre()).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo de pacientes: " + e.getMessage());
        }

        try (FileWriter writer = new FileWriter(CITAS_FILE)) {
            for (Cita c : citas) {
                writer.append(c.getId()).append(",").append(c.getIdPaciente()).append(",").append(c.getIdDoctor()).append(",").append(c.getMotivo()).append("\n");
            }
        } catch (IOException e) {
            System.err.println("Error al guardar el archivo de citas: " + e.getMessage());
        }
    }

    // Metodo para cargar los datos desde los archivos CSV
    public void cargarDatos1(List<Doctor> doctores, List<Paciente> pacientes, List<Cita> citas) {
        // Cargar doctores
        try (BufferedReader reader = new BufferedReader(new FileReader(DOCTORES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    doctores.add(new Doctor(data[0], data[1], data[2]));
                }
            }
        } catch (IOException e) {
            System.err.println("No se encontró el archivo de doctores. Se creará uno nuevo.");
        }

        // Cargar pacientes
        try (BufferedReader reader = new BufferedReader(new FileReader(PACIENTES_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 2) {
                    pacientes.add(new Paciente(data[0], data[1]));
                }
            }
        } catch (IOException e) {
            System.err.println("No se encontró el archivo de pacientes. Se creará uno nuevo.");
        }

        // Cargar citas
        try (BufferedReader reader = new BufferedReader(new FileReader(CITAS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) { // Asegura que haya al menos 4 campos
                    citas.add(new Cita(data[0], data[1], data[2], null, null, data[3]));
                }
            }
        } catch (IOException e) {
            System.err.println("No se encontró el archivo de citas. Se creará uno nuevo.");
        }
    }

    // Método para guardar los datos usando CSVWriter
    public boolean guardarDatos(List<Doctor> doctores, List<Paciente> pacientes, List<Cita> citas) {
        try{
            // --- GUARDAR DOCTORES ---
            try (CSVWriter writer = new CSVWriter(new FileWriter(DOCTORES_FILE))) {
                // Escribir el encabezado (opcional, pero buena práctica)
                writer.writeNext(new String[]{"ID", "NOMBRE", "ESPECIALIDAD"});

                for (Doctor d : doctores) {
                    String[] data = {d.getId(), d.getNombre(), d.getEspecialidad()};
                    writer.writeNext(data);
                }
                // Agrega bloques similares para Pacientes y Citas
            } catch (IOException e) {
                System.err.println("Error al guardar el archivo de doctores con OpenCSV: " + e.getMessage());
            }

            //---GUARDAR PACIENTE---
            try(CSVWriter writer = new CSVWriter(new FileWriter(PACIENTES_FILE))){
                //Encabezado del archivo pacientes.csv
                writer.writeNext(new String[]{"ID","NOMBRE"});

                for (Paciente p : pacientes){
                    String[] data = {p.getId(), p.getNombre()};
                    writer.writeNext(data);
                }

            }catch (IOException e){
                System.err.println("Error al guardar el archivo de pacientes con OpenCSV"+e.getMessage());

            }
            //GUARDAR CITAS
            try(CSVWriter writer = new CSVWriter(new FileWriter(CITAS_FILE))){
                writer.writeNext(new String[]{"ID","IDPACIENTE","IDDOCTOR","FECHA","HORA","MOTIVO"});

                for (Cita c : citas){
                    String[] data = {c.getId(), c.getIdPaciente(), c.getIdDoctor(), c.getFecha(), c.getHora(), c.getMotivo()};
                    writer.writeNext(data);
                }

            } catch (Exception e) {
                System.err.println("Error al guardar el archivo de pacientes con OpenSSV"+e.getMessage());
            }
            return true;
        } catch (Exception e) {
            System.err.println("Error al guardar los archivos. Detalles: " + e.getMessage());
            return false;
        }
    }

    // Método para cargar los datos usando CSVReader
    public boolean cargarDatos(List<Doctor> doctores, List<Paciente> pacientes, List<Cita> citas) {
        try{
            // --- CARGAR DOCTORES ---
            try (CSVReader reader = new CSVReader(new FileReader(DOCTORES_FILE))) {
                // Saltar la primera línea si usaste encabezado
                reader.readNext();

                String[] nextRecord;
                while ((nextRecord = reader.readNext()) != null) {
                    // nextRecord[0] = ID, nextRecord[1] = NOMBRE, nextRecord[2] = ESPECIALIDAD
                    if (nextRecord.length >= 3) {
                        doctores.add(new Doctor(nextRecord[0], nextRecord[1], nextRecord[2]));
                    }
                }
                // Agrega bloques similares para Pacientes y Citas
            } catch (IOException e) {
                System.err.println("No se pudo cargar el archivo de doctores. Iniciando con lista vacía.");
            } catch (CsvValidationException e) {
                throw new RuntimeException(e);
            }

            try (CSVReader reader = new CSVReader(new FileReader(PACIENTES_FILE))) {
                // Saltar la primera línea si usaste encabezado
                reader.readNext();

                String[] nextRecord;
                while ((nextRecord = reader.readNext()) != null) {
                    // nextRecord[0] = ID, nextRecord[1] = NOMBRE, nextRecord[2] = ESPECIALIDAD
                    if (nextRecord.length >= 2) {
                        pacientes.add(new Paciente(nextRecord[0], nextRecord[1]));
                    }
                }

            } catch (IOException e) {
                System.err.println("No se pudo cargar el archivo de doctores. Iniciando con lista vacía.");
            } catch (CsvValidationException e) {
                throw new RuntimeException(e);
            }

            try (CSVReader reader = new CSVReader(new FileReader(CITAS_FILE))) {
                // Saltar la primera línea si usaste encabezado
                reader.readNext();

                String[] nextRecord;
                while ((nextRecord = reader.readNext()) != null) {
                    // nextRecord[0] = ID, nextRecord[1] = NOMBRE, nextRecord[2] = ESPECIALIDAD
                    if (nextRecord.length >= 4) {
                        citas.add(new Cita(nextRecord[0], nextRecord[1],  nextRecord[2], nextRecord[3], nextRecord[4], nextRecord[5]));
                    }
                }
                // Agrega bloques similares para Pacientes y Citas
            } catch (IOException e) {
                System.err.println("No se pudo cargar el archivo de doctores. Iniciando con lista vacía.");
            } catch (CsvValidationException e) {
                System.err.println("No se pudo cargar el archivo de citas. Iniciando con lista vacía.");
            }
            return true;
        } catch (Exception e) {
            System.out.println(""+e.getMessage());
            return false;
        }
    }
}