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

public class Venta extends JFrame {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtCodReceta;
    private JTextArea areaVenta;
    private JButton btnImprimir, btnRegresar;
    private JComboBox<String> cbVeterinario, cbDueño;

    private Map<String, Double> precios = new HashMap<>();
    private Map<String, Integer> dueñosMap = new HashMap<>();
    private Map<String, Integer> veterinariosMap = new HashMap<>();

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Venta frame = new Venta();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Venta() {
        setTitle("Venta");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 530, 470);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.setBackground(new Color(173, 216, 230));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("VENTA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(190, 10, 120, 30);
        contentPane.add(lblTitulo);

        JLabel lblVeterinario = new JLabel("Veterinario:");
        lblVeterinario.setBounds(30, 60, 80, 20);
        contentPane.add(lblVeterinario);

        cbVeterinario = new JComboBox<>();
        cbVeterinario.setBounds(120, 60, 150, 22);
        cbVeterinario.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contentPane.add(cbVeterinario);

        JLabel lblDueño = new JLabel("Dueño:");
        lblDueño.setBounds(30, 90, 80, 20);
        contentPane.add(lblDueño);

        cbDueño = new JComboBox<>();
        cbDueño.setBounds(120, 90, 150, 22);
        cbDueño.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        contentPane.add(cbDueño);

        JLabel lblCodReceta = new JLabel("Cod. Receta:");
        lblCodReceta.setBounds(290, 60, 80, 20);
        contentPane.add(lblCodReceta);

        txtCodReceta = new JTextField();
        txtCodReceta.setBounds(380, 60, 100, 22);
        contentPane.add(txtCodReceta);

        btnImprimir = new JButton("Imprimir");
        btnImprimir.setBounds(120, 125, 100, 25);
        btnImprimir.addActionListener(e -> generarVenta());
        contentPane.add(btnImprimir);

        btnRegresar = new JButton("Regresar");
        btnRegresar.setBounds(230, 125, 100, 25);
        btnRegresar.addActionListener(e -> dispose());
        contentPane.add(btnRegresar);

        areaVenta = new JTextArea();
        areaVenta.setEditable(false);
        areaVenta.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(areaVenta);
        scrollPane.setBounds(30, 170, 460, 240);
        contentPane.add(scrollPane);

        // Precios de medicamentos
        precios.put("Antibiótico", 15.0);
        precios.put("Analgésico", 10.0);
        precios.put("Antiinflamatorio", 12.0);
        precios.put("Desparasitante", 8.0);
        precios.put("Vitaminas", 5.0);

        cargarVeterinarios();
        cargarDueños();
    }

    private void cargarVeterinarios() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD_Veterinaria", "root", "12345678")) {
            String sql = "SELECT id_veterinario, nombre, apellido FROM veterinarios";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            cbVeterinario.removeAllItems();
            veterinariosMap.clear();
            while (rs.next()) {
                String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido");
                cbVeterinario.addItem(nombreCompleto);
                veterinariosMap.put(nombreCompleto, rs.getInt("id_veterinario"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al cargar veterinarios: " + e.getMessage());
        }
    }

    private void cargarDueños() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD_Veterinaria", "root", "12345678")) {
            String sql = "SELECT id_Dueño, nombre, apellido FROM duenos";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            cbDueño.removeAllItems();
            dueñosMap.clear();
            while (rs.next()) {
                String nombreCompleto = rs.getString("nombre") + " " + rs.getString("apellido");
                cbDueño.addItem(nombreCompleto);
                dueñosMap.put(nombreCompleto, rs.getInt("id_Dueño"));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al cargar dueños: " + e.getMessage());
        }
    }

    private void generarVenta() {
        String codReceta = txtCodReceta.getText().trim();
        String nombreVeterinario = (String) cbVeterinario.getSelectedItem();
        String nombreDueño = (String) cbDueño.getSelectedItem();

        if (codReceta.isEmpty() || nombreVeterinario == null || nombreDueño == null) {
            JOptionPane.showMessageDialog(this, "Complete todos los campos.");
            return;
        }

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/BD_Veterinaria", "root", "12345678")) {
            String sql = """
                SELECT r.codigo_receta, r.fecha_hora, r.medicamentos,
                       v.nombre, v.apellido, v.id_veterinario, v.especializacion
                FROM recetas_medicas r
                JOIN veterinarios v ON r.id_veterinario = v.id_veterinario
                WHERE r.codigo_receta = ?
            """;

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, codReceta);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                String vetNombre = rs.getString("nombre") + " " + rs.getString("apellido");
                String especializacion = rs.getString("especializacion");
                String medicamentos = rs.getString("medicamentos");
                int idVeterinario = rs.getInt("id_veterinario");
                int idDueño = dueñosMap.get(nombreDueño);

                double total = 0.0;
                StringBuilder detalle = new StringBuilder();
                detalle.append("Veterinario: ").append(vetNombre).append("\n");
                detalle.append("Especialización: ").append(especializacion).append("\n");
                detalle.append("Dueño: ").append(nombreDueño).append("\n");
                detalle.append("Código receta: ").append(codReceta).append("\n");
                detalle.append("Medicamentos:\n");

                double precioConsulta = switch (especializacion.toLowerCase()) {
                    case "cirugía" -> 1000.0;
                    case "dermatología" -> 80.0;
                    case "cardiología" -> 120.0;
                    case "odontología" -> 100.0;
                    default -> 40.0;
                };

                String[] lineas = medicamentos.split("\\n");
                for (String linea : lineas) {
                    linea = linea.replaceFirst("- ", "").trim();
                    String[] partes = linea.split(" - ");
                    if (partes.length == 2) {
                        String nombreMed = partes[0].trim();
                        int cantidad = Integer.parseInt(partes[1].replaceAll("\\D+", ""));
                        double precioUnitario = precios.getOrDefault(nombreMed, 0.0);
                        double subtotal = cantidad * precioUnitario;
                        total += subtotal;
                        detalle.append("- ").append(nombreMed).append(": ")
                               .append(cantidad).append(" x S/")
                               .append(precioUnitario).append(" = S/")
                               .append(String.format("%.2f", subtotal)).append("\n");
                    }
                }

                detalle.append("\nSubtotal medicamentos: S/").append(String.format("%.2f", total)).append("\n");
                detalle.append("Consulta por especialización: S/").append(String.format("%.2f", precioConsulta)).append("\n");
                total += precioConsulta;
                detalle.append("\nTOTAL: S/").append(String.format("%.2f", total));

                areaVenta.setText(detalle.toString());

                String insert = "INSERT INTO ventas (codigo_receta, id_veterinario, id_dueño, detalle, total, fecha) VALUES (?, ?, ?, ?, ?, ?)";
                PreparedStatement psInsert = conn.prepareStatement(insert);
                psInsert.setString(1, codReceta);
                psInsert.setInt(2, idVeterinario);
                psInsert.setInt(3, idDueño);
                psInsert.setString(4, detalle.toString());
                psInsert.setDouble(5, total);
                psInsert.setString(6, obtenerFechaHoraActual());

                psInsert.executeUpdate();
                psInsert.close();

                JOptionPane.showMessageDialog(this, "Venta registrada exitosamente.");
            } else {
                JOptionPane.showMessageDialog(this, "Código de receta no encontrado.");
            }

            rs.close();
            ps.close();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }

    private String obtenerFechaHoraActual() {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formato);
    }
}
