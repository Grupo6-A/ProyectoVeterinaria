package Menu;

import java.awt.EventQueue;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import Estructura_Interna.Boleta;
import Estructura_Interna.Factura;

import java.sql.*;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

// IMPORTAR tus formularios de historial
import Historial.Historial_Dueños;
import Historial.Historial_Mascota;

public class Menú extends JFrame implements ActionListener {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTable tablaReceta;
    private DefaultTableModel modeloTabla;
    private JTextArea textS;
    private JMenuItem mntmNewMenuItem_1;
    private JMenuItem mntmNewMenuItem;
    private JMenuItem itemBoleta;
    private JMenuItem itemFactura;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Menú frame = new Menú();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Menú() {
        setTitle("Sistema de Veterinaria");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 700, 500);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(173, 216, 230));
        setContentPane(contentPane);

        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        
                JMenu mnCliente = new JMenu("Cliente");
                menuBar.add(mnCliente);
                JMenuItem itemDueno = new JMenuItem("Datos del dueño");
                itemDueno.addActionListener(e -> new Estructura_Interna.Dueño().setVisible(true));
                mnCliente.add(itemDueno);
                JMenuItem itemMascota = new JMenuItem("Datos de la mascota");
                itemMascota.addActionListener(e -> new Estructura_Interna.Mascota().setVisible(true));
                mnCliente.add(itemMascota);
                
                        mntmNewMenuItem = new JMenuItem("Historial_Dueños");
                        mntmNewMenuItem.addActionListener(this);
                        mnCliente.add(mntmNewMenuItem);
                        
                                mntmNewMenuItem_1 = new JMenuItem("Historial_Mascotas");
                                mntmNewMenuItem_1.addActionListener(this);
                                mnCliente.add(mntmNewMenuItem_1);

        JMenu mnVeterinaria = new JMenu("Cita");
        menuBar.add(mnVeterinaria);
        JMenuItem itemCita = new JMenuItem("Realizar Cita");
        itemCita.addActionListener(e -> new Estructura_Interna.Cita().setVisible(true));
        mnVeterinaria.add(itemCita);

        JMenu mnPersonal = new JMenu("Personal");
        menuBar.add(mnPersonal);
        JMenuItem itemVeterinario = new JMenuItem("Veterinario");
        itemVeterinario.addActionListener(e -> new Estructura_Interna.Veterinario().setVisible(true));
        mnPersonal.add(itemVeterinario);

        JMenu mnReceta = new JMenu("Receta");
        menuBar.add(mnReceta);
        JMenuItem itemAgregarReceta = new JMenuItem("Agregar Receta");
        itemAgregarReceta.addActionListener(e -> new Estructura_Interna.Receta().setVisible(true));
        mnReceta.add(itemAgregarReceta);
        JMenuItem itemVenta = new JMenuItem("Venta");
        itemVenta.addActionListener(e -> new Estructura_Interna.Venta().setVisible(true));
        mnReceta.add(itemVenta);

        JMenu mnRecibo = new JMenu("Recibo");
        menuBar.add(mnRecibo);
        {
        	itemBoleta = new JMenuItem("Boleta");
        	itemBoleta.addActionListener(this);
        	mnRecibo.add(itemBoleta);
        }
        {
        	itemFactura = new JMenuItem("Factura");
        	itemFactura.addActionListener(this);
        	mnRecibo.add(itemFactura);
        }

        JLabel lblTitulo = new JLabel("PANEL PRINCIPAL", SwingConstants.CENTER);
        lblTitulo.setFont(new java.awt.Font("Tahoma", java.awt.Font.BOLD, 18));
        lblTitulo.setForeground(new Color(0, 51, 102));
        lblTitulo.setBounds(180, 0, 280, 30);
        contentPane.add(lblTitulo);

        textS = new JTextArea();
        textS.setBorder(BorderFactory.createTitledBorder("Recibo generado"));
        textS.setBackground(Color.WHITE);
        textS.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 13));
        textS.setEditable(false);
        JScrollPane sp = new JScrollPane(textS);
        sp.setBounds(20, 40, 400, 180);
        contentPane.add(sp);

        JButton btnFactura = new JButton("Recibo");
        btnFactura.setBounds(440, 70, 145, 30);
        contentPane.add(btnFactura);

        JButton btnVerReceta = new JButton("Ver Recetas");
        btnVerReceta.setBounds(440, 120, 145, 30);
        contentPane.add(btnVerReceta);

        JButton btnEliminar = new JButton("Cancelar Receta");
        btnEliminar.setBounds(440, 170, 145, 30);
        contentPane.add(btnEliminar);

        String[] columnas = {"Código", "Nombre Veterinario", "Precio", "Fecha"};
        modeloTabla = new DefaultTableModel(columnas, 0);
        tablaReceta = new JTable(modeloTabla);

        JScrollPane scrollPane = new JScrollPane(tablaReceta);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Detalle de Receta"));
        scrollPane.setBounds(20, 230, 640, 200);
        contentPane.add(scrollPane);

        btnFactura.addActionListener(e -> mostrarRecibo());
        btnVerReceta.addActionListener(e -> mostrarReceta());
        btnEliminar.addActionListener(e -> eliminarReceta());
    }

    private void mostrarRecibo() {
        String nombreDueño = JOptionPane.showInputDialog("Ingrese el nombre del dueño:");
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD_Veterinaria", "root", "12345678")) {
            String sqlDue = "SELECT * FROM duenos WHERE nombre = ?";
            PreparedStatement ps1 = conn.prepareStatement(sqlDue);
            ps1.setString(1, nombreDueño);
            ResultSet rs1 = ps1.executeQuery();

            StringBuilder recibo = new StringBuilder();

            if (rs1.next()) {
                int idDueno = rs1.getInt("id_Dueño");
                recibo.append("---- DATOS DEL DUEÑO ----\n")
                      .append("Nombre: ").append(rs1.getString("nombre")).append(" ").append(rs1.getString("apellido")).append("\n")
                      .append("DNI: ").append(rs1.getString("dni")).append("\n")
                      .append("Correo: ").append(rs1.getString("correo")).append("\n")
                      .append("Teléfono: ").append(rs1.getString("telefono")).append("\n\n");

                String sqlCita = "SELECT * FROM citas WHERE id_dueño = ? ORDER BY fecha DESC LIMIT 1";
                PreparedStatement ps2 = conn.prepareStatement(sqlCita);
                ps2.setInt(1, idDueno);
                ResultSet rs2 = ps2.executeQuery();

                if (rs2.next()) {
                    recibo.append("---- DATOS DE LA CITA ----\n")
                          .append("Fecha: ").append(rs2.getString("fecha")).append("\n")
                          .append("Hora: ").append(rs2.getString("hora")).append("\n")
                          .append("Urgencia: ").append(rs2.getString("urgencia")).append("\n")
                          .append("Observaciones: ").append(rs2.getString("observaciones"));
                } else {
                    recibo.append("No hay citas registradas para este dueño.");
                }
            } else {
                recibo.append("Dueño no encontrado");
            }

            textS.setText(recibo.toString());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al obtener datos: " + ex.getMessage());
        }
    }

    private void mostrarReceta() {
        modeloTabla.setRowCount(0); // Limpiar tabla

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD_Veterinaria", "root", "admin")) {
            String sql = """
                SELECT r.codigo_receta, v.nombre, v.apellido, r.medicamentos, r.fecha_hora
                FROM recetas_medicas r
                JOIN veterinarios v ON r.id_veterinario = v.id_veterinario
                WHERE r.estado = 1
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String codigo = rs.getString("codigo_receta");
                String doctor = rs.getString("nombre") + " " + rs.getString("apellido");
                String medicamentos = rs.getString("medicamentos");
                String fecha = rs.getString("fecha_hora");

                double total = 0.0;
                for (String linea : medicamentos.split("\\n")) {
                    linea = linea.replaceFirst("- ", "").trim();
                    String[] partes = linea.split(" - ");
                    if (partes.length == 2) {
                        String med = partes[0].trim();
                        int cant = Integer.parseInt(partes[1].replaceAll("\\D+", ""));
                        total += switch (med) {
                            case "Antibiótico" -> cant * 15.0;
                            case "Analgésico" -> cant * 10.0;
                            case "Antiinflamatorio" -> cant * 12.0;
                            case "Desparasitante" -> cant * 8.0;
                            case "Vitaminas" -> cant * 5.0;
                            default -> 0.0;
                        };
                    }
                }

                modeloTabla.addRow(new Object[]{codigo, doctor, String.format("S/ %.2f", total), fecha});
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al mostrar recetas: " + ex.getMessage());
        }
    }

    private void eliminarReceta() {
        String codigo = JOptionPane.showInputDialog("Ingrese el código de receta a cancelar:");
        if (codigo == null || codigo.trim().isEmpty()) return;

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD_Veterinaria", "root", "admin")) {
            String sql = "UPDATE recetas_medicas SET estado = 0 WHERE codigo_receta = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, codigo);
            int rows = ps.executeUpdate();

            if (rows > 0) {
                JOptionPane.showMessageDialog(this, "Receta cancelada correctamente.");
                for (int i = 0; i < modeloTabla.getRowCount(); i++) {
                    if (modeloTabla.getValueAt(i, 0).equals(codigo)) {
                        modeloTabla.removeRow(i);
                        break;
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "No se encontró una receta activa con ese código.");
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al cancelar receta: " + ex.getMessage());
        }
    }

    // Aquí controlamos los clics de los menús de historial
    public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == itemFactura) {
    		do_itemFactura_actionPerformed(e);
    	}
    	if (e.getSource() == itemBoleta) {
    		do_itemBoleta_actionPerformed(e);
    	}
        if (e.getSource() == mntmNewMenuItem) {
            new Historial_Dueños().setVisible(true);
        } else if (e.getSource() == mntmNewMenuItem_1) {
            new Historial_Mascota().setVisible(true);
        }
    }
	protected void do_itemBoleta_actionPerformed(ActionEvent e) {
        Boleta bl=new Boleta();
		bl.setVisible(true);
	}
	protected void do_itemFactura_actionPerformed(ActionEvent e) {
		Factura ft=new Factura();
		ft.setVisible(true);
	}
}




