package Estructura_Interna;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Receta extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtCodigoReceta, txtcantidad, txthorayfecha;
	private JTextArea txtS;
	private JComboBox<String> cbmedicamentos, cbVeterinario;
	private JButton btn_Agregar, btnRecetar, btnRegresar;
	private Map<String, Integer> veterinariosMap = new HashMap<>();
	private JRadioButton rbHabilitarMedicamentos;
	private JButton btnEliminarMedicamento;
	private DefaultListModel<String> modeloMedicamentos = new DefaultListModel<>();

	public static void main(String[] args) {
	    EventQueue.invokeLater(() -> {
	        try {
	            Receta frame = new Receta();
	            frame.setVisible(true);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    });
	}

	public Receta() {
	    setTitle("Receta");
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    setBounds(100, 100, 550, 360);
	    contentPane = new JPanel();
	    contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
	    contentPane.setBackground(new Color(173, 216, 230)); // Celeste
	    setContentPane(contentPane);
	    contentPane.setLayout(null);

	    JLabel lblTitulo = new JLabel("RECETA");
	    lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
	    lblTitulo.setBounds(20, 0, 120, 30);
	    contentPane.add(lblTitulo);

	    JLabel lblCodigo = new JLabel("Código Receta:");
	    lblCodigo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
	    lblCodigo.setBounds(40, 41, 100, 20);
	    contentPane.add(lblCodigo);

	    txtCodigoReceta = new JTextField();
	    txtCodigoReceta.setFont(new Font("Segoe UI", Font.PLAIN, 12));
	    txtCodigoReceta.setBounds(140, 41, 100, 20);
	    contentPane.add(txtCodigoReceta);

	    JLabel lblDescripcion = new JLabel("Medicamento:");
	    lblDescripcion.setFont(new Font("Segoe UI", Font.PLAIN, 13));
	    lblDescripcion.setBounds(40, 72, 100, 20);
	    contentPane.add(lblDescripcion);

	    cbmedicamentos = new JComboBox<>(new String[] {
	        "Antibiótico", "Analgésico", "Antiinflamatorio", "Desparasitante", "Vitaminas"
	    });
	    cbmedicamentos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
	    cbmedicamentos.setBounds(140, 72, 100, 22);
	    contentPane.add(cbmedicamentos);

	    JLabel lblCantidad = new JLabel("Cant:");
	    lblCantidad.setFont(new Font("Segoe UI", Font.PLAIN, 13));
	    lblCantidad.setBounds(260, 75, 40, 14);
	    contentPane.add(lblCantidad);

	    txtcantidad = new JTextField();
	    txtcantidad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
	    txtcantidad.setBounds(300, 72, 30, 20);
	    contentPane.add(txtcantidad);

	    btn_Agregar = new JButton("Agregar");
	    btn_Agregar.setFont(new Font("Segoe UI", Font.BOLD, 12));
	    btn_Agregar.setBounds(350, 71, 90, 23);
	    btn_Agregar.addActionListener(this);
	    contentPane.add(btn_Agregar);

	    JScrollPane scrollPane = new JScrollPane();
	    scrollPane.setBounds(40, 103, 460, 140);
	    contentPane.add(scrollPane);

	    txtS = new JTextArea();
	    txtS.setFont(new Font("Segoe UI", Font.PLAIN, 13));
	    txtS.setBackground(Color.WHITE);
	    scrollPane.setViewportView(txtS);

	    JLabel lblVeterinario = new JLabel("Veterinario:");
	    lblVeterinario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
	    lblVeterinario.setBounds(180, 11, 80, 14);
	    contentPane.add(lblVeterinario);

	    cbVeterinario = new JComboBox<>();
	    cbVeterinario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
	    cbVeterinario.setBounds(260, 7, 180, 22);
	    contentPane.add(cbVeterinario);

	    JLabel lblHora = new JLabel("Hora y fecha:");
	    lblHora.setFont(new Font("Segoe UI", Font.PLAIN, 13));
	    lblHora.setBounds(260, 44, 80, 14);
	    contentPane.add(lblHora);

	    txthorayfecha = new JTextField();
	    txthorayfecha.setFont(new Font("Segoe UI", Font.PLAIN, 12));
	    txthorayfecha.setBounds(350, 41, 126, 20);
	    contentPane.add(txthorayfecha);

	    btnRecetar = new JButton("Recetar");
	    btnRecetar.setFont(new Font("Segoe UI", Font.BOLD, 12));
	    btnRecetar.setBounds(230, 254, 100, 25);
	    btnRecetar.addActionListener(this);
	    contentPane.add(btnRecetar);

	    btnRegresar = new JButton("Regresar");
	    btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 12));
	    btnRegresar.setBounds(363, 254, 100, 25);
	    btnRegresar.addActionListener(this);
	    contentPane.add(btnRegresar);

	    rbHabilitarMedicamentos = new JRadioButton("Habilitar medicamentos");
	    rbHabilitarMedicamentos.setFont(new Font("Segoe UI", Font.PLAIN, 12));
	    rbHabilitarMedicamentos.setBounds(40, 260, 160, 20);
	    rbHabilitarMedicamentos.setBackground(new Color(173, 216, 230));
	    rbHabilitarMedicamentos.addActionListener(this);
	    contentPane.add(rbHabilitarMedicamentos);

	    btnEliminarMedicamento = new JButton("Eliminar último");
	    btnEliminarMedicamento.setFont(new Font("Segoe UI", Font.BOLD, 12));
	    btnEliminarMedicamento.setBounds(404, 285, 120, 25);
	    btnEliminarMedicamento.addActionListener(this);
	    contentPane.add(btnEliminarMedicamento);

	    cbmedicamentos.setEnabled(false);
	    txtcantidad.setEnabled(false);
	    btn_Agregar.setEnabled(false);
	    btnEliminarMedicamento.setEnabled(false);

	    cargarVeterinarios();
	    txtCodigoReceta.setText(generarCodigoReceta());
	    txthorayfecha.setText(obtenerFechaHoraActual());
	}

	private void cargarVeterinarios() {
	    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD_Veterinaria", "root", "12345678")) {
	        String sql = "SELECT id_veterinario, nombre, apellido FROM veterinarios";
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ResultSet rs = ps.executeQuery();

	        cbVeterinario.removeAllItems();
	        veterinariosMap.clear();

	        while (rs.next()) {
	            int id = rs.getInt("id_veterinario");
	            String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido");
	            cbVeterinario.addItem(nombreCompleto);
	            veterinariosMap.put(nombreCompleto, id);
	        }
	    } catch (Exception e) {
	        JOptionPane.showMessageDialog(this, "Error al cargar veterinarios: " + e.getMessage());
	    }
	}

	private void guardarReceta() {
	    try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD_Veterinaria", "root", "12345678")) {

	        String sql = "INSERT INTO recetas_medicas (codigo_receta, fecha_hora, medicamentos, id_veterinario) VALUES (?, ?, ?, ?)";
	        PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, txtCodigoReceta.getText());
	        ps.setString(2, txthorayfecha.getText());
	        ps.setString(3, txtS.getText().isEmpty() ? "Sin medicamentos" : txtS.getText());
	        ps.setInt(4, veterinariosMap.get(cbVeterinario.getSelectedItem().toString()));

	        int filas = ps.executeUpdate();
	        if (filas > 0) {
	            limpiarCampos();
	            JOptionPane.showMessageDialog(this, "Receta guardada correctamente.");
	        }
	    } catch (Exception ex) {
	        JOptionPane.showMessageDialog(this, "Error al guardar receta: " + ex.getMessage());
	    }
	}

	private void limpiarCampos() {
	    txtCodigoReceta.setText(generarCodigoReceta());
	    txthorayfecha.setText(obtenerFechaHoraActual());
	    txtcantidad.setText("");
	    txtS.setText("");
	    modeloMedicamentos.clear();
	}

	private void agregarMedicamento() {
	    String med = cbmedicamentos.getSelectedItem().toString();
	    String cant = txtcantidad.getText().trim();
	    if (!cant.matches("\\d+")) {
	        JOptionPane.showMessageDialog(this, "Cantidad inválida");
	        return;
	    }
	    txtS.append("- " + med + " - " + cant + " unidades\n");
	    txtcantidad.setText("");
	}

	private String generarCodigoReceta() {
	    String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	    StringBuilder codigo = new StringBuilder("RX-");
	    for (int i = 0; i < 5; i++) {
	        int indice = (int) (Math.random() * caracteres.length());
	        codigo.append(caracteres.charAt(indice));
	    }
	    return codigo.toString();
	}

	private String obtenerFechaHoraActual() {
	    DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	    return LocalDateTime.now().format(formato);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
	    if (e.getSource() == btnRecetar) {
	        guardarReceta();
	    } else if (e.getSource() == btn_Agregar) {
	        agregarMedicamento();
	    } else if (e.getSource() == btnEliminarMedicamento) {
	        String texto = txtS.getText();
	        int ultimoSalto = texto.lastIndexOf("\n");
	        if (ultimoSalto > 0) {
	            txtS.setText(texto.substring(0, ultimoSalto));
	        }
	    } else if (e.getSource() == rbHabilitarMedicamentos) {
	        boolean habilitado = rbHabilitarMedicamentos.isSelected();
	        cbmedicamentos.setEnabled(habilitado);
	        txtcantidad.setEnabled(habilitado);
	        btn_Agregar.setEnabled(habilitado);
	        btnEliminarMedicamento.setEnabled(habilitado);
	        txtS.setText("");
	    } else if (e.getSource() == btnRegresar) {
	        dispose();
	    }
	}
}