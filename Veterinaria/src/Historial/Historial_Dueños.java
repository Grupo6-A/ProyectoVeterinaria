package Historial;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import ConexionBD.ConexionPrincipal;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Historial_Dueños extends JFrame implements ActionListener, MouseListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTable tabla;
	private DefaultTableModel modelo;
	private JTextField txtNombre, txtApellido, txtDni, txtCorreo, txtTelefono;
	private JButton btnModificar, btnEliminar, btnSalir;
	private int idSeleccionado = -1;

	public static void main(String[] args) {
		EventQueue.invokeLater(() -> {
			try {
				Historial_Dueños frame = new Historial_Dueños();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public Historial_Dueños() {
		setTitle("Historial de Dueños");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 750, 500);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		contentPane.setLayout(null);
		contentPane.setBackground(new Color(173, 216, 230));
		setContentPane(contentPane);

		JLabel lblTitulo = new JLabel("HISTORIAL DE DUEÑOS", SwingConstants.CENTER);
		lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
		lblTitulo.setBounds(220, 10, 300, 30);
		lblTitulo.setForeground(new Color(0, 51, 102));
		contentPane.add(lblTitulo);

		tabla = new JTable();
		modelo = new DefaultTableModel(new String[] { "ID", "Nombre", "Apellido", "DNI", "Correo", "Teléfono" }, 0);
		tabla.setModel(modelo);
		tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		tabla.addMouseListener(this);

		JScrollPane scrollPane = new JScrollPane(tabla);
		scrollPane.setBounds(20, 60, 690, 200);
		contentPane.add(scrollPane);

		agregarCampo("Nombre:", 20, 270, txtNombre = new JTextField());
		agregarCampo("Apellido:", 20, 300, txtApellido = new JTextField());
		agregarCampo("DNI:", 300, 270, txtDni = new JTextField());
		agregarCampo("Correo:", 300, 300, txtCorreo = new JTextField());
		agregarCampo("Teléfono:", 20, 330, txtTelefono = new JTextField());

		btnModificar = crearBoton("Modificar", 240, 380);
		btnEliminar = crearBoton("Eliminar", 390, 380);
		btnSalir = crearBoton("Regresar", 580, 420);
		btnSalir.addActionListener(e -> dispose());

		listarDueños();
	}

	private void agregarCampo(String texto, int x, int y, JTextField campo) {
		JLabel lbl = new JLabel(texto);
		lbl.setBounds(x, y, 100, 20);
		contentPane.add(lbl);
		campo.setBounds(x + 100, y, 150, 20);
		contentPane.add(campo);
	}

	private JButton crearBoton(String texto, int x, int y) {
		JButton btn = new CustomButton(texto);
		btn.setBounds(x, y, 140, 35);
		btn.addActionListener(this);
		contentPane.add(btn);
		return btn;
	}

	private void listarDueños() {
		modelo.setRowCount(0);
		try (Connection conn = ConexionPrincipal.Conectar()) {
			String sql = "SELECT * FROM duenos WHERE estado = TRUE";
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				modelo.addRow(new Object[] {
					rs.getInt("id_Dueño"),
					rs.getString("nombre"),
					rs.getString("apellido"),
					rs.getString("dni"),
					rs.getString("correo"),
					rs.getString("telefono")
				});
			}
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "❌ Error al cargar dueños: " + e.getMessage());
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnModificar) {
			if (idSeleccionado != -1) modificarDueño();
			else JOptionPane.showMessageDialog(this, "Selecciona un dueño de la tabla.");
		} else if (e.getSource() == btnEliminar) {
			if (idSeleccionado != -1) eliminarDueño();
			else JOptionPane.showMessageDialog(this, "Selecciona un dueño de la tabla.");
		}
	}

	private void modificarDueño() {
		try (Connection conn = ConexionPrincipal.Conectar()) {
			String sql = "UPDATE duenos SET nombre = ?, apellido = ?, dni = ?, correo = ?, telefono = ? WHERE id_Dueño = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, txtNombre.getText());
			ps.setString(2, txtApellido.getText());
			ps.setString(3, txtDni.getText());
			ps.setString(4, txtCorreo.getText());
			ps.setString(5, txtTelefono.getText());
			ps.setInt(6, idSeleccionado);
			ps.executeUpdate();
			JOptionPane.showMessageDialog(this, "✅ Dueño actualizado correctamente.");
			listarDueños();
			limpiarCampos();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "❌ Error al modificar: " + e.getMessage());
		}
	}

	private void eliminarDueño() {
		try (Connection conn = ConexionPrincipal.Conectar()) {
			String sql = "UPDATE duenos SET estado = FALSE WHERE id_Dueño = ?";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setInt(1, idSeleccionado);
			ps.executeUpdate();
			JOptionPane.showMessageDialog(this, "🗑️ Dueño eliminado lógicamente.");
			listarDueños();
			limpiarCampos();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(this, "❌ Error al eliminar: " + e.getMessage());
		}
	}

	private void limpiarCampos() {
		txtNombre.setText("");
		txtApellido.setText("");
		txtDni.setText("");
		txtCorreo.setText("");
		txtTelefono.setText("");
		idSeleccionado = -1;
		tabla.clearSelection();
	}

	@Override public void mouseClicked(MouseEvent e) {
		int fila = tabla.getSelectedRow();
		if (fila != -1) {
			idSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
			txtNombre.setText(modelo.getValueAt(fila, 1).toString());
			txtApellido.setText(modelo.getValueAt(fila, 2).toString());
			txtDni.setText(modelo.getValueAt(fila, 3).toString());
			txtCorreo.setText(modelo.getValueAt(fila, 4).toString());
			txtTelefono.setText(modelo.getValueAt(fila, 5).toString());
		}
	}
	@Override public void mousePressed(MouseEvent e) {}
	@Override public void mouseReleased(MouseEvent e) {}
	@Override public void mouseEntered(MouseEvent e) {}
	@Override public void mouseExited(MouseEvent e) {}
}

/** ✅ Clase interna para diseño degradado y animación del botón **/
class CustomButton extends JButton {
	private static final long serialVersionUID = 1L;
	private boolean isPressed = false;

	public CustomButton(String text) {
		super(text);
		setFont(new Font("Segoe UI", Font.BOLD, 13));
		setForeground(Color.BLACK); // ✅ Color del texto a negro
		setContentAreaFilled(false);
		setFocusPainted(false);
		setOpaque(false);
		setBorder(BorderFactory.createLineBorder(Color.GRAY));
		setCursor(new Cursor(Cursor.HAND_CURSOR)); // Mano al pasar el mouse

		addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				isPressed = true;
				repaint();
			}
			@Override
			public void mouseReleased(MouseEvent e) {
				isPressed = false;
				repaint();
			}
		});
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g.create();
		int w = getWidth();
		int h = getHeight();

		Color top = isPressed ? new Color(200, 220, 235) : new Color(245, 245, 255);
		Color bottom = isPressed ? new Color(245, 245, 255) : new Color(200, 220, 235);
		GradientPaint gp = new GradientPaint(0, 0, top, 0, h, bottom);
		g2.setPaint(gp);
		g2.fillRoundRect(0, 0, w, h, 10, 10);

		g2.setColor(Color.GRAY);
		g2.drawRoundRect(0, 0, w - 1, h - 1, 10, 10);

		g2.dispose();
		super.paintComponent(g);
	}
}