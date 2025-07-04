package Clases_Proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import ConexionBD.ConexionPrincipal;

public class Clase_Cita {

    public static class DatosCita {
        private int idDueño;
        private String fecha;
        private String hora;
        private String urgencia;
        private String observaciones;

        public DatosCita(int idDueño, String fecha, String hora, String urgencia, String observaciones) {
            this.idDueño = idDueño;
            this.fecha = fecha;
            this.hora = hora;
            this.urgencia = urgencia;
            this.observaciones = observaciones;
        }

        public int getIdDueño() { return idDueño; }
        public String getFecha() { return fecha; }
        public String getHora() { return hora; }
        public String getUrgencia() { return urgencia; }
        public String getObservaciones() { return observaciones; }
    }

    public static void guardarCita(DatosCita cita) {
        try (Connection conn = ConexionPrincipal.Conectar()) {
            String sql = "INSERT INTO citas (id_dueño, fecha, hora, urgencia, observaciones) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, cita.getIdDueño());
            ps.setString(2, cita.getFecha());
            ps.setString(3, cita.getHora());
            ps.setString(4, cita.getUrgencia());
            ps.setString(5, cita.getObservaciones());

            ps.executeUpdate();
            System.out.println(" Cita guardada en la base de datos.");
        } catch (Exception e) {
            System.out.println(" Error al guardar cita: " + e.getMessage());
        }
    }

    public static ArrayList<String> obtenerHorasOcupadas(String fecha) {
        ArrayList<String> horas = new ArrayList<>();
        try (Connection conn = ConexionPrincipal.Conectar()) {
            String sql = "SELECT hora FROM citas WHERE fecha = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, fecha);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                horas.add(rs.getString("hora"));
            }
        } catch (Exception e) {
            System.out.println("❌ Error al obtener horas ocupadas: " + e.getMessage());
        }
        return horas;
    }
}
