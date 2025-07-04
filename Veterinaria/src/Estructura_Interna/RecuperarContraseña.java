package Estructura_Interna;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class RecuperarContraseña extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtDni;
	private JPasswordField txtNuevaClave;
	private JButton btnActualizar;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				RecuperarContraseña frame = new RecuperarContraseña();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public RecuperarContraseña() {
		setTitle("Recuperar Contraseña");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 400, 250);
		contentPane = new JPanel();
		contentPane.setBackground(new Color(173, 216, 230));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel("Recuperación de Contraseña");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblTitulo.setBounds(90, 10, 250, 30);
		contentPane.add(lblTitulo);

		JLabel lblDni = new JLabel("Ingrese su DNI:");
		lblDni.setBounds(50, 60, 120, 20);
		contentPane.add(lblDni);

		txtDni = new JTextField();
		txtDni.setBounds(180, 60, 150, 20);
		contentPane.add(txtDni);

		JLabel lblNuevaClave = new JLabel("Nueva Contraseña:");
		lblNuevaClave.setBounds(50, 100, 120, 20);
		contentPane.add(lblNuevaClave);

		txtNuevaClave = new JPasswordField();
		txtNuevaClave.setBounds(180, 100, 150, 20);
		contentPane.add(txtNuevaClave);

		btnActualizar = new JButton("Actualizar Contraseña");
		btnActualizar.setBounds(100, 150, 180, 25);
		contentPane.add(btnActualizar);

		btnActualizar.addActionListener(e -> recuperarContraseña());
	}

	private void recuperarContraseña() {
		String dni = txtDni.getText();
		String nuevaClave = new String(txtNuevaClave.getPassword());

		if (dni.isEmpty() || nuevaClave.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.");
			return;
		}

		try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD_Veterinaria", "root", "12345678")) {

			// Verificar si el DNI existe
			String verificarSql = "SELECT * FROM usuarios WHERE dni = ?";
			PreparedStatement verificarStmt = conn.prepareStatement(verificarSql);
			verificarStmt.setString(1, dni);
			ResultSet rs = verificarStmt.executeQuery();

			if (!rs.next()) {
				JOptionPane.showMessageDialog(this, "DNI no registrado.");
				return;
			}

			// Actualizar contraseña
			String actualizarSql = "UPDATE usuarios SET clave = ? WHERE dni = ?";
			PreparedStatement actualizarStmt = conn.prepareStatement(actualizarSql);
			actualizarStmt.setString(1, nuevaClave);
			actualizarStmt.setString(2, dni);
			actualizarStmt.executeUpdate();

			JOptionPane.showMessageDialog(this, "Contraseña actualizada correctamente.");
			dispose();

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "❌ Error al actualizar: " + ex.getMessage());
		}
	}
}
