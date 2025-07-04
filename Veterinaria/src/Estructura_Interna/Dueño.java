package Estructura_Interna;

import java.awt.EventQueue;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.border.*;

import Clases_Proyecto.Clase_Dueño;
import Clases_Proyecto.Clase_Dueño.DatosDueno;

public class Dueño extends JFrame implements ActionListener {

    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JTextField txtNombre, txtApellido, txtDNI, txtCorreo, txtTelefono;
    private JButton btnRegistrar, btnRetroceder;

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                Dueño frame = new Dueño();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public Dueño() {
        setTitle("Datos del Dueño");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setBounds(100, 100, 340, 340);
        contentPane = new JPanel();
        contentPane.setLayout(null);
        contentPane.setBackground(new Color(173, 216, 230)); // Fondo celeste
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JLabel lblTitulo = new JLabel("DATOS DEL DUEÑO");
        lblTitulo.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitulo.setBounds(30, 10, 260, 30);
        contentPane.add(lblTitulo);

        String[] labels = {"Nombre:", "Apellido:", "DNI:", "Correo:", "Teléfono:"};
        JTextField[] fields = new JTextField[5];
        int y = 55;

        for (int i = 0; i < labels.length; i++, y += 33) {
            JLabel lbl = new JLabel(labels[i]);
            lbl.setFont(new Font("Segoe UI", Font.PLAIN, 13));
            lbl.setBounds(24, y, 70, 20);
            contentPane.add(lbl);

            fields[i] = new JTextField();
            fields[i].setFont(new Font("Segoe UI", Font.PLAIN, 12));
            fields[i].setBackground(Color.WHITE);
            fields[i].setBounds(95, y, 200, 25);
            fields[i].setBorder(new LineBorder(Color.GRAY));
            contentPane.add(fields[i]);
        }

        txtNombre = fields[0];
        txtApellido = fields[1];
        txtDNI = fields[2];
        txtCorreo = fields[3];
        txtTelefono = fields[4];

        btnRegistrar = new JButton("Registrar Datos");
        btnRegistrar.setBounds(90, 220, 140, 25);
        btnRegistrar.setFont(new Font("Segoe UI", Font.BOLD, 12));
        contentPane.add(btnRegistrar);

        btnRetroceder = new JButton("Retroceder");
        btnRetroceder.setBounds(105, 255, 110, 25);
        btnRetroceder.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRetroceder.addActionListener(e -> dispose());
        contentPane.add(btnRetroceder);

        btnRegistrar.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String nombre = txtNombre.getText();
        String apellido = txtApellido.getText();
        String dni = txtDNI.getText();
        String correo = txtCorreo.getText();
        String telefono = txtTelefono.getText();

        DatosDueno dueno = new DatosDueno(nombre, apellido, dni, correo, telefono);
        int idDueño = Clase_Dueño.guardarDuenoEnBD(dueno);

        JOptionPane.showMessageDialog(null, "✅ Datos del dueño registrados.");
        dispose();
    }
}

