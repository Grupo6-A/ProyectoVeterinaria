package Historial;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import ConexionBD.ConexionPrincipal;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Historial_Mascota extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tabla;
    private DefaultTableModel modelo;
    private JTextField txtNombre, txtRaza, txtSexo, txtEdad, txtDueño, txtVeterinario;
    private JButton btnModificar, btnEliminar, btnSalir;
    private int idSeleccionado = -1;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                Historial_Mascota frame = new Historial_Mascota();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Historial_Mascota() {
        setTitle("Historial de Mascotas");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 800, 530);
        contentPane = new JPanel();
        contentPane.setBackground(new Color(173, 216, 230));
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setLayout(null);
        setContentPane(contentPane);

        JLabel lblTitulo = new JLabel("HISTORIAL DE MASCOTAS", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitulo.setForeground(new Color(0, 51, 102));
        lblTitulo.setBounds(200, 10, 400, 30);
        contentPane.add(lblTitulo);

        tabla = new JTable();
        modelo = new DefaultTableModel(new String[]{
            "ID", "Nombre", "Raza", "Sexo", "Edad", "Dueño", "Veterinario"
        }, 0);
        tabla.setModel(modelo);
        tabla.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int fila = tabla.getSelectedRow();
                if (fila != -1) {
                    idSeleccionado = Integer.parseInt(modelo.getValueAt(fila, 0).toString());
                    txtNombre.setText(modelo.getValueAt(fila, 1).toString());
                    txtRaza.setText(modelo.getValueAt(fila, 2).toString());
                    txtSexo.setText(modelo.getValueAt(fila, 3).toString());
                    txtEdad.setText(modelo.getValueAt(fila, 4).toString());
                    txtDueño.setText(modelo.getValueAt(fila, 5).toString());
                    txtVeterinario.setText(modelo.getValueAt(fila, 6).toString());
                }
            }
        });

        JScrollPane scrollPane = new JScrollPane(tabla);
        scrollPane.setBounds(20, 60, 740, 200);
        contentPane.add(scrollPane);

        agregarCampo("Nombre:", 20, 270, txtNombre = new JTextField());
        agregarCampo("Raza:", 20, 300, txtRaza = new JTextField());
        agregarCampo("Sexo:", 20, 330, txtSexo = new JTextField());
        agregarCampo("Edad:", 400, 270, txtEdad = new JTextField());
        agregarCampo("Dueño:", 400, 300, txtDueño = new JTextField());
        agregarCampo("Veterinario:", 400, 330, txtVeterinario = new JTextField());

        btnModificar = crearBoton("Modificar", 230, 390);
        btnEliminar = crearBoton("Eliminar", 380, 390);
        btnSalir = crearBoton("Salir", 580, 430);
        btnSalir.addActionListener(e -> dispose());

        listarMascotas();
    }

    private void agregarCampo(String texto, int x, int y, JTextField campo) {
        JLabel lbl = new JLabel(texto);
        lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lbl.setBounds(x, y, 100, 20);
        contentPane.add(lbl);
        campo.setBounds(x + 100, y, 150, 20);
        contentPane.add(campo);
    }

    private JButton crearBoton(String texto, int x, int y) {
        JButton btn = new CustomButton(texto); // ✅ Solo usamos el CustomButton ya existente
        btn.setBounds(x, y, 120, 35);
        btn.addActionListener(this);
        contentPane.add(btn);
        return btn;
    }

    private void listarMascotas() {
        modelo.setRowCount(0);
        try (Connection conn = ConexionPrincipal.Conectar()) {
            String sql = "SELECT m.id, m.nombre, m.raza, m.sexo, m.edad, d.nombre AS nombre_dueño, m.veterinario " +
                         "FROM mascotas m LEFT JOIN duenos d ON m.id_dueño = d.id_Dueño";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                modelo.addRow(new Object[]{
                    rs.getInt("id"),
                    rs.getString("nombre"),
                    rs.getString("raza"),
                    rs.getString("sexo"),
                    rs.getString("edad"),
                    rs.getString("nombre_dueño"),
                    rs.getString("veterinario") != null ? rs.getString("veterinario") : "Sin veterinario"
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al listar mascotas: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Object src = e.getSource();
        if (src == btnModificar) {
            if (idSeleccionado != -1) modificarMascota();
            else JOptionPane.showMessageDialog(this, "Selecciona una mascota para modificar.");
        } else if (src == btnEliminar) {
            if (idSeleccionado != -1) eliminarMascota();
            else JOptionPane.showMessageDialog(this, "Selecciona una mascota para eliminar.");
        }
    }

    private void modificarMascota() {
        try (Connection conn = ConexionPrincipal.Conectar()) {
            String sql = "UPDATE mascotas SET nombre = ?, raza = ?, sexo = ?, edad = ?, veterinario = ? WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, txtNombre.getText());
            ps.setString(2, txtRaza.getText());
            ps.setString(3, txtSexo.getText());
            ps.setString(4, txtEdad.getText());
            ps.setString(5, txtVeterinario.getText());
            ps.setInt(6, idSeleccionado);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "✅ Mascota modificada correctamente.");
            listarMascotas();
            limpiarCampos();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al modificar: " + e.getMessage());
        }
    }

    private void eliminarMascota() {
        try (Connection conn = ConexionPrincipal.Conectar()) {
            String sql = "DELETE FROM mascotas WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idSeleccionado);
            int confirmar = JOptionPane.showConfirmDialog(this, "¿Eliminar esta mascota?", "Confirmar", JOptionPane.YES_NO_OPTION);
            if (confirmar == JOptionPane.YES_OPTION) {
                ps.executeUpdate();
                JOptionPane.showMessageDialog(this, "🗑️ Mascota eliminada.");
                listarMascotas();
                limpiarCampos();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al eliminar: " + e.getMessage());
        }
    }

    private void limpiarCampos() {
        txtNombre.setText("");
        txtRaza.setText("");
        txtSexo.setText("");
        txtEdad.setText("");
        txtDueño.setText("");
        txtVeterinario.setText("");
        idSeleccionado = -1;
        tabla.clearSelection();
    }
}

