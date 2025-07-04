package Usuario;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import Clases_Proyecto.Clase_UsuarioBD;
import Menu.Menú;

public class Usuario extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsuario;
	private JTextField txtContraseña;
	private JButton btnIniciarSesion;
	private int intentosFallidos = 0; // Contador de intentos

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Usuario frame = new Usuario();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public Usuario() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 399, 236);
		setTitle("Inicio de Sesión");

		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(173, 216, 230));
		setContentPane(contentPane);

		JLabel lblTitulo = new JLabel("Inicio de Sesión");
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
		lblTitulo.setBounds(120, 10, 160, 30);
		contentPane.add(lblTitulo);

		JLabel lblNombre = new JLabel("Usuario:");
		lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblNombre.setBounds(77, 51, 80, 20);
		contentPane.add(lblNombre);

		txtUsuario = new JTextField();
		txtUsuario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtUsuario.setBounds(167, 51, 120, 20);
		contentPane.add(txtUsuario);

		JLabel lblContrasena = new JLabel("Contraseña:");
		lblContrasena.setFont(new Font("Segoe UI", Font.PLAIN, 13));
		lblContrasena.setBounds(77, 82, 80, 20);
		contentPane.add(lblContrasena);

		txtContraseña = new JTextField();
		txtContraseña.setFont(new Font("Segoe UI", Font.PLAIN, 12));
		txtContraseña.setBounds(167, 82, 120, 20);
		contentPane.add(txtContraseña);

		btnIniciarSesion = new JButton("Iniciar Sesión");
		btnIniciarSesion.setFont(new Font("Segoe UI", Font.BOLD, 12));
		btnIniciarSesion.setBounds(118, 133, 130, 25);
		btnIniciarSesion.addActionListener(this);
		contentPane.add(btnIniciarSesion);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnIniciarSesion) {
			do_btnIniciarSesion_actionPerformed(e);
		}
	}

	protected void do_btnIniciarSesion_actionPerformed(ActionEvent e) {
		String usuario = txtUsuario.getText();
		String contraseña = txtContraseña.getText();

		if (Clase_UsuarioBD.validarLogin(usuario, contraseña)) {
			JOptionPane.showMessageDialog(null, "✅ Inicio de sesión correcto");
			Menú menu = new Menú();
			menu.setVisible(true);
			dispose();
		} else {
			intentosFallidos++;
			JOptionPane.showMessageDialog(null, "❌ Usuario o contraseña incorrectos. Intento " + intentosFallidos + " de 3.");

			if (intentosFallidos >= 3) {
				JOptionPane.showMessageDialog(null, "⚠ Has superado el límite de intentos. Cerrando aplicación.");
				System.exit(0); 
			}
		}
	}
}
