package Clases_Proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import ConexionBD.ConexionPrincipal;

public class Clase_Mascota {

    public static DatosMascota mascotaActual = null;

    // Clase interna para encapsular los datos de una mascota
    public static class DatosMascota {
        private String nombre;
        private String raza;
        private String sexo;
        private String edad;
        private int idDueno;

        public DatosMascota(String nombre, String raza, String sexo, String edad, int idDueno) {
            this.nombre = nombre;
            this.raza = raza;
            this.sexo = sexo;
            this.edad = edad;
            this.idDueno = idDueno;
        }

        public String getNombre() { return nombre; }
        public String getRaza() { return raza; }
        public String getSexo() { return sexo; }
        public String getEdad() { return edad; }
        public int getIdDueno() { return idDueno; }

        @Override
        public String toString() {
            return "Nombre: " + nombre + "\nRaza: " + raza + "\nSexo: " + sexo + "\nEdad: " + edad;
        }
    }

    // Clase para representar dueños en el combo
    public static class DueñoItem {
        private int id;
        private String nombre;

        public DueñoItem(int id, String nombre) {
            this.id = id;
            this.nombre = nombre;
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return nombre;
        }
    }

    // Clase para representar veterinarios en el combo
    public static class VeterinarioItem {
        private int id;
        private String descripcion;

        public VeterinarioItem(int id, String nombre, String apellido, String especialidad) {
            this.id = id;
            this.descripcion = "Dr. " + nombre + " " + apellido + " (" + especialidad + ")";
        }

        public int getId() {
            return id;
        }

        @Override
        public String toString() {
            return descripcion;
        }
    }

    // Método actualizado: guarda la mascota con el nombre del veterinario directamente
    public static void guardarMascotaEnBD(DatosMascota mascota, String nombreVeterinario) {
        try (Connection conn = ConexionPrincipal.Conectar()) {
            String sql = "INSERT INTO mascotas (nombre, raza, sexo, edad, id_dueño, veterinario) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, mascota.getNombre());
            ps.setString(2, mascota.getRaza());
            ps.setString(3, mascota.getSexo());
            ps.setString(4, mascota.getEdad());
            ps.setInt(5, mascota.getIdDueno());
            ps.setString(6, nombreVeterinario);  // Aquí se guarda directamente el nombre del veterinario

            ps.executeUpdate();
            System.out.println("✅ Mascota registrada con veterinario.");
        } catch (Exception e) {
            System.out.println("❌ Error al guardar mascota: " + e.getMessage());
        }
    }
}
