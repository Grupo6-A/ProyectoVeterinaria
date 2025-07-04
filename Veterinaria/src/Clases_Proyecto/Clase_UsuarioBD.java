package Clases_Proyecto;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import ConexionBD.ConexionPrincipal;

public class Clase_UsuarioBD {

    
    public static boolean validarLogin(String usuario, String clave) {
        try (Connection cnx = ConexionPrincipal.Conectar()) {
            String sql = "SELECT * FROM usuarios WHERE usuario = ? AND clave = ?";
            PreparedStatement ps = cnx.prepareStatement(sql);
            ps.setString(1, usuario);
            ps.setString(2, clave);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("Error al iniciar sesión: " + e);
            return false;
        }
    }

  
}