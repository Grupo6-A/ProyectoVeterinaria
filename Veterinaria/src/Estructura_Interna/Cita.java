package Estructura_Interna;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;

import Clases_Proyecto.Clase_Cita;
import Clases_Proyecto.Clase_Cita.DatosCita;
import Clases_Proyecto.Clase_Mascota.DueñoItem;
import java.sql.*;

public class Cita extends JFrame {

    private JPanel contentPane;
    private JTextField txtObservaciones;
    private JComboBox<String> comboUrgencias;
    private JComboBox<String> comboHora;
    private JComboBox<DueñoItem> comboDueño;
    private JTextField txtFecha;
    private JButton btnGuardar, btnRegresar;

    public Cita() {
        setTitle("Registrar Cita");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 420, 360);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(15, 15, 15, 15));
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(173, 216, 230)); // ← Fondo celeste claro
        setContentPane(contentPane);

        JLabel lblTitulo = new JLabel("CITA");
        lblTitulo.setFont(new Font("Tahoma", Font.BOLD, 18));
        lblTitulo.setBounds(160, 10, 100, 30);
        contentPane.add(lblTitulo);

        JLabel lblDueño = new JLabel("Dueño:");
        lblDueño.setBounds(40, 58, 140, 20);
        contentPane.add(lblDueño);

        comboDueño = new JComboBox<>();
        comboDueño.setBounds(180, 57, 180, 22);
        contentPane.add(comboDueño);

        JLabel lblFecha = new JLabel("Fecha (YYYY-MM-DD):");
        lblFecha.setBounds(40, 89, 140, 20);
        contentPane.add(lblFecha);

        txtFecha = new JTextField(LocalDate.now().toString());
        txtFecha.setBounds(180, 89, 180, 22);
        contentPane.add(txtFecha);

        JLabel lblHora = new JLabel("Hora:");
        lblHora.setBounds(40, 120, 120, 20);
        contentPane.add(lblHora);

        comboHora = new JComboBox<>();
        comboHora.setBounds(180, 121, 180, 22);
        contentPane.add(comboHora);

        JLabel lblObservaciones = new JLabel("Observaciones:");
        lblObservaciones.setBounds(40, 150, 120, 20);
        contentPane.add(lblObservaciones);

        txtObservaciones = new JTextField();
        txtObservaciones.setBounds(180, 150, 180, 20);
        contentPane.add(txtObservaciones);

        JLabel lblUrgencias = new JLabel("Urgencias:");
        lblUrgencias.setBounds(40, 180, 120, 20);
        contentPane.add(lblUrgencias);

        comboUrgencias = new JComboBox<>(new String[]{"Normal", "Media", "Alta"});
        comboUrgencias.setBounds(180, 180, 180, 22);
        contentPane.add(comboUrgencias);

        btnGuardar = new JButton("Guardar Cita");
        btnGuardar.setBounds(120, 230, 150, 25);
        contentPane.add(btnGuardar);

        btnRegresar = new JButton("Regresar");
        btnRegresar.setBounds(130, 266, 130, 25);
        btnRegresar.addActionListener(e -> dispose());
        contentPane.add(btnRegresar);

        cargarDueños();
        cargarHoras();

        txtFecha.addActionListener(e -> cargarHoras());

        btnGuardar.addActionListener(e -> guardarCita());
    }

    private void cargarDueños() {
        try (Connection conn = ConexionBD.ConexionPrincipal.Conectar()) {
            String sql = "SELECT id_Dueño, nombre FROM duenos";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id_Dueño");
                String nombre = rs.getString("nombre");
                comboDueño.addItem(new DueñoItem(id, nombre));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "❌ Error al cargar dueños: " + e.getMessage());
        }
    }

    private void cargarHoras() {
        comboHora.removeAllItems();

        String[] posiblesHoras = {"09:00", "10:00", "11:00", "12:00",
                                  "14:00", "15:00", "16:00", "17:00"};

        String fecha = txtFecha.getText();
        ArrayList<String> ocupadas = Clase_Cita.obtenerHorasOcupadas(fecha);

        for (String hora : posiblesHoras) {
            if (!ocupadas.contains(hora)) {
                comboHora.addItem(hora);
            }
        }
    }

    private void guardarCita() {
        try {
            DueñoItem dueño = (DueñoItem) comboDueño.getSelectedItem();
            String fecha = txtFecha.getText();
            String hora = (String) comboHora.getSelectedItem();
            String urgencia = (String) comboUrgencias.getSelectedItem();
            String observaciones = txtObservaciones.getText();

            DatosCita cita = new DatosCita(dueño.getId(), fecha, hora, urgencia, observaciones);
            Clase_Cita.guardarCita(cita);

            JOptionPane.showMessageDialog(this, " Cita registrada con éxito.");
            dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, " Error al guardar cita: " + e.getMessage());
        }
    }
}

