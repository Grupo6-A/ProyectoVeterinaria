package ConexionBD;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConexionPrincipal {
	public static Connection Conectar() {
		Connection cnx= null;
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			System.out.print("Driver correcto");
			cnx= DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/BD_Veterinaria", "root", "12345678"
			);
			System.out.println("\nconexión establecida");
		} catch (Exception e) {
			System.out.println("Error: "+e);
		}
		return cnx;
	}

	public static void main(String[] args) {
		Conectar();
	}
}

