package Clases_Proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import ConexionBD.ConexionPrincipal;

public class Clase_Dueño {

    public static DatosDueno dueñoActual = null;

    public static class DatosDueno {
        private String nombre;
        private String apellido;
        private String dni;
        private String correo;
        private String telefono;

        public DatosDueno(String nombre, String apellido, String dni, String correo, String telefono) {
            this.nombre = nombre;
            this.apellido = apellido;
            this.dni = dni;
            this.correo = correo;
            this.telefono = telefono;
        }

        public String toString() {
            return "Nombre: " + nombre + " " + apellido + "\nDNI: " + dni +
                   "\nCorreo: " + correo + "\nTeléfono: " + telefono;
        }

        public String getNombre() { return nombre; }
        public String getApellido() { return apellido; }
        public String getDni() { return dni; }
        public String getCorreo() { return correo; }
        public String getTelefono() { return telefono; }
    }
    
    public static int guardarDuenoEnBD(DatosDueno dueno) {
        int idGenerado = -1;
        try (Connection conn = ConexionPrincipal.Conectar()) {
            String sql = "INSERT INTO duenos (nombre, apellido, dni, correo, telefono) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setString(1, dueno.getNombre());
            ps.setString(2, dueno.getApellido());
            ps.setString(3, dueno.getDni());
            ps.setString(4, dueno.getCorreo());
            ps.setString(5, dueno.getTelefono());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                idGenerado = rs.getInt(1);
            }

            System.out.println("Dueño registrado en BD con ID: " + idGenerado);
        } catch (Exception e) {
            System.out.println("Error al guardar dueño: " + e.getMessage());
        }
        return idGenerado;
    }
    
}

