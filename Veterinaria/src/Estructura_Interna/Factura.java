package Estructura_Interna;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Factura extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JLabel lblTitulo;
    private JButton btnImprimir, btnRegresar;
    private JScrollPane scrollPane;
    private TextArea txtS;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Factura frame = new Factura();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Factura() {
        setTitle("Factura");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 510, 390);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPane.setBackground(new Color(173, 216, 230));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        lblTitulo = new JLabel("Generación de Factura");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBounds(140, 10, 250, 20);
        contentPane.add(lblTitulo);

        btnImprimir = new JButton("IMPRIMIR");
        btnImprimir.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnImprimir.setBounds(50, 45, 150, 25);
        btnImprimir.addActionListener(this);
        contentPane.add(btnImprimir);

        btnRegresar = new JButton("REGRESAR");
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegresar.setBounds(280, 45, 150, 25);
        btnRegresar.addActionListener(e -> dispose());
        contentPane.add(btnRegresar);

        scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 85, 460, 250);
        contentPane.add(scrollPane);

        txtS = new TextArea();
        txtS.setFont(new Font("Monospaced", Font.PLAIN, 12));
        scrollPane.setViewportView(txtS);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String nombreDueño = JOptionPane.showInputDialog("Ingrese el nombre del dueño:");
        if (nombreDueño == null || nombreDueño.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nombre inválido.");
            return;
        }

        String codReceta = JOptionPane.showInputDialog("Ingrese el código de la receta:");
        if (codReceta == null || codReceta.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Código de receta inválido.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD_Veterinaria", "root", "12345678")) {

            String sql = """
                SELECT v.detalle, v.total, v.fecha,
                       d.apellido, d.dni, d.correo, d.telefono,
                       v.codigo_receta, vet.especializacion
                FROM ventas v
                JOIN duenos d ON v.id_dueño = d.id_Dueño
                JOIN veterinarios vet ON v.id_veterinario = vet.id_veterinario
                WHERE d.nombre = ? AND v.codigo_receta = ?
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nombreDueño);
            ps.setString(2, codReceta);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                JOptionPane.showMessageDialog(this, "No se encontró una venta registrada con ese nombre y receta.");
                return;
            }

            // Obtener datos
            String detalle = rs.getString("detalle");
            double totalBase = rs.getDouble("total");
            String fecha = rs.getString("fecha");
            String apellido = rs.getString("apellido");
            String dni = rs.getString("dni");
            String correo = rs.getString("correo");
            String telefono = rs.getString("telefono");
            String especializacion = rs.getString("especializacion");

            // Evaluar si se cobra la consulta general
            double precioConsultaGeneral = especializacion.equalsIgnoreCase("general") ? 0.0 : 20.0;
            double totalFinal = totalBase + precioConsultaGeneral;

            // Generar factura
            StringBuilder factura = new StringBuilder();
            factura.append("====================================\n");
            factura.append("      VETERINARIA YOGUES PETS       \n");
            factura.append("        RUC: 12345678901            \n");
            factura.append("     Av. Fel. Arancibia 581-Rímac   \n");
            factura.append("         Tel: 987654321             \n");
            factura.append("====================================\n");
            factura.append("           FACTURA DE VENTA         \n");
            factura.append("====================================\n");

            factura.append("Cliente: ").append(nombreDueño).append(" ").append(apellido).append("\n");
            factura.append("DNI: ").append(dni).append("\n");
            factura.append("Correo: ").append(correo).append("\n");
            factura.append("Teléfono: ").append(telefono).append("\n");
            factura.append("Fecha: ").append(fecha).append("\n");
            factura.append("Código Receta: ").append(codReceta).append("\n");
            factura.append("Especialización: ").append(especializacion).append("\n");
            factura.append("------------------------------------\n");

            if (detalle == null || detalle.trim().isEmpty()) {
                factura.append("-> Sin medicamentos registrados\n");
            } else {
                factura.append("Medicamentos:\n").append(detalle).append("\n");
            }

            factura.append(String.format("Subtotal medicamentos + especialización: S/ %.2f\n", totalBase));
            if (precioConsultaGeneral > 0) {
                factura.append(String.format("Consulta general: S/ %.2f\n", precioConsultaGeneral));
            }

            factura.append("------------------------------------\n");
            factura.append(String.format("TOTAL PAGADO: S/ %.2f\n", totalFinal));
            factura.append("====================================\n");
            factura.append("Gracias por su preferencia ❤️\n");

            txtS.setText(factura.toString());

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "❌ Error al generar factura: " + ex.getMessage());
        }
    }
}

