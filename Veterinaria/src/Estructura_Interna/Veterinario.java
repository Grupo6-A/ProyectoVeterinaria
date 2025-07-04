package Estructura_Interna;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Veterinario extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField textFieldNombre, textFieldApellido, textFieldEdad;
	private JComboBox<String> comboBoxEspecializacion;
	private JButton btnGuardar, btnRegresar;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Veterinario frame = new Veterinario();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public Veterinario() {
		setTitle("Registro de Veterinario");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 320, 310);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		contentPane.setBackground(new Color(173, 216, 230)); // Celeste claro
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblTitulo = new JLabel("REGISTRO VETERINARIO");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitulo.setBounds(30, 10, 250, 30);
		contentPane.add(lblTitulo);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblNombre.setBounds(30, 50, 100, 20);
		contentPane.add(lblNombre);

		textFieldNombre = new JTextField();
		textFieldNombre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textFieldNombre.setBounds(130, 50, 140, 22);
		contentPane.add(textFieldNombre);

		JLabel lblApellido = new JLabel("Apellido:");
		lblApellido.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblApellido.setBounds(30, 80, 100, 20);
		contentPane.add(lblApellido);

		textFieldApellido = new JTextField();
		textFieldApellido.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textFieldApellido.setBounds(130, 80, 140, 22);
		contentPane.add(textFieldApellido);

		JLabel lblEdad = new JLabel("Edad:");
		lblEdad.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblEdad.setBounds(30, 110, 100, 20);
		contentPane.add(lblEdad);

		textFieldEdad = new JTextField();
		textFieldEdad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		textFieldEdad.setBounds(130, 110, 140, 22);
		contentPane.add(textFieldEdad);

		JLabel lblEspecializacion = new JLabel("Especialización:");
		lblEspecializacion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblEspecializacion.setBounds(30, 140, 100, 20);
		contentPane.add(lblEspecializacion);

		comboBoxEspecializacion = new JComboBox<>();
		comboBoxEspecializacion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		comboBoxEspecializacion.setBounds(130, 140, 140, 22);
		contentPane.add(comboBoxEspecializacion);

		// Agregar especialidades
		comboBoxEspecializacion.addItem("General");
		comboBoxEspecializacion.addItem("Cirugía");
		comboBoxEspecializacion.addItem("Dermatología");
		comboBoxEspecializacion.addItem("Cardiología");
		comboBoxEspecializacion.addItem("Odontología");

		// Botón "Añadir" con estilo visual Swing
		btnGuardar = new JButton("Añadir");
		btnGuardar.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnGuardar.setBounds(80, 180, 140, 25);
		btnGuardar.addActionListener(this);
		contentPane.add(btnGuardar);

		// Botón "Regresar" con mismo estilo
		btnRegresar = new JButton("Regresar");
		btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnRegresar.setBounds(80, 215, 140, 25);
		btnRegresar.addActionListener(e -> dispose());
		contentPane.add(btnRegresar);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnGuardar) {
			guardarVeterinario();
		}
	}

	private void guardarVeterinario() {
		try {
			String nombre = textFieldNombre.getText().trim();
			String apellido = textFieldApellido.getText().trim();
			int edad = Integer.parseInt(textFieldEdad.getText().trim());
			String especializacion = comboBoxEspecializacion.getSelectedItem().toString();

			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD_Veterinaria", "root", "12345678");
			String sql = "INSERT INTO veterinarios (nombre, apellido, edad, especializacion) VALUES (?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, nombre);
			ps.setString(2, apellido);
			ps.setInt(3, edad);
			ps.setString(4, especializacion);

			int filas = ps.executeUpdate();
			if (filas > 0) {
				JOptionPane.showMessageDialog(this, " Veterinario guardado correctamente.");
				textFieldNombre.setText("");
				textFieldApellido.setText("");
				textFieldEdad.setText("");
				comboBoxEspecializacion.setSelectedIndex(0);
			}

			ps.close();
			conn.close();
		} catch (NumberFormatException ex) {
			JOptionPane.showMessageDialog(this, "⚠️ La edad debe ser un número entero.");
		} catch (SQLException ex) {
			JOptionPane.showMessageDialog(this, "❌ Error en base de datos: " + ex.getMessage());
		}
	}
}

