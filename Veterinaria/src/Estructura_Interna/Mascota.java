package Estructura_Interna;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import Clases_Proyecto.Clase_Mascota;

public class Mascota extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtNombre, txtRaza, txtEdad;
    private JButton btnRegistrar, btnRegresar;
    private JComboBox<String> comboSexo;
    private JComboBox<Clase_Mascota.DueñoItem> comboDueños;
    private JComboBox<Clase_Mascota.VeterinarioItem> comboVeterinarios;

    public Mascota() {
        setTitle("Datos de la Mascota");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 370, 420);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.setBackground(new Color(173, 216, 230));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblTitulo = new JLabel("DATOS DE LA MASCOTA");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setBounds(50, 10, 250, 30);
        contentPane.add(lblTitulo);

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblNombre.setBounds(40, 52, 100, 20);
        contentPane.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtNombre.setBounds(150, 52, 140, 20);
        contentPane.add(txtNombre);

        JLabel lblRaza = new JLabel("Raza:");
        lblRaza.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblRaza.setBounds(40, 82, 100, 20);
        contentPane.add(lblRaza);

        txtRaza = new JTextField();
        txtRaza.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtRaza.setBounds(150, 82, 140, 20);
        contentPane.add(txtRaza);

        JLabel lblSexo = new JLabel("Sexo:");
        lblSexo.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblSexo.setBounds(40, 112, 100, 20);
        contentPane.add(lblSexo);

        comboSexo = new JComboBox<>(new String[] {"Macho", "Hembra"});
        comboSexo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboSexo.setBounds(150, 113, 140, 22);
        contentPane.add(comboSexo);

        JLabel lblEdad = new JLabel("Edad:");
        lblEdad.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblEdad.setBounds(40, 142, 100, 20);
        contentPane.add(lblEdad);

        txtEdad = new JTextField();
        txtEdad.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtEdad.setBounds(150, 142, 140, 20);
        contentPane.add(txtEdad);

        JLabel lblDueno = new JLabel("Dueño:");
        lblDueno.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDueno.setBounds(40, 172, 100, 20);
        contentPane.add(lblDueno);

        comboDueños = new JComboBox<>();
        comboDueños.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboDueños.setBounds(150, 172, 140, 22);
        contentPane.add(comboDueños);
        cargarDueños();

        JLabel lblVeterinario = new JLabel("Veterinario:");
        lblVeterinario.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblVeterinario.setBounds(40, 202, 100, 20);
        contentPane.add(lblVeterinario);

        comboVeterinarios = new JComboBox<>();
        comboVeterinarios.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboVeterinarios.setBounds(150, 202, 140, 22);
        contentPane.add(comboVeterinarios);
        cargarVeterinarios();

        btnRegistrar = new JButton("Registrar Datos");
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegistrar.setBounds(91, 260, 150, 25);
        btnRegistrar.addActionListener(this);
        contentPane.add(btnRegistrar);

        btnRegresar = new JButton("Regresar");
        btnRegresar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRegresar.setBounds(101, 295, 120, 25);
        btnRegresar.addActionListener(e -> dispose());
        contentPane.add(btnRegresar);
    }

    private void cargarDueños() {
        try (Connection conn = ConexionBD.ConexionPrincipal.Conectar()) {
            String sql = "SELECT id_Dueño, nombre FROM duenos WHERE estado = TRUE";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_Dueño");
                String nombre = rs.getString("nombre");
                comboDueños.addItem(new Clase_Mascota.DueñoItem(id, nombre));
            }
        } catch (Exception e) {
            System.out.println("❌ Error al cargar dueños: " + e.getMessage());
        }
    }

    private void cargarVeterinarios() {
        try (Connection conn = ConexionBD.ConexionPrincipal.Conectar()) {
            String sql = "SELECT id_veterinario, nombre, apellido, especializacion FROM veterinarios";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_veterinario");
                String nombre = rs.getString("nombre");
                String apellido = rs.getString("apellido");
                String especialidad = rs.getString("especializacion");
                comboVeterinarios.addItem(new Clase_Mascota.VeterinarioItem(id, nombre, apellido, especialidad));
            }
        } catch (Exception e) {
            System.out.println("❌ Error al cargar veterinarios: " + e.getMessage());
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnRegistrar) {
            String nombre = txtNombre.getText();
            String raza = txtRaza.getText();
            String sexo = comboSexo.getSelectedItem().toString();
            String edad = txtEdad.getText();

            Clase_Mascota.DueñoItem dueno = (Clase_Mascota.DueñoItem) comboDueños.getSelectedItem();
            Clase_Mascota.VeterinarioItem vet = (Clase_Mascota.VeterinarioItem) comboVeterinarios.getSelectedItem();

            Clase_Mascota.DatosMascota mascota = new Clase_Mascota.DatosMascota(nombre, raza, sexo, edad, dueno.getId());
            Clase_Mascota.mascotaActual = mascota;

            // 🔄 Guardar pasando el nombre del veterinario
            Clase_Mascota.guardarMascotaEnBD(mascota, vet.toString());

            JOptionPane.showMessageDialog(this, "✅ Mascota registrada con veterinario.");
            dispose();
        }
    }
}
