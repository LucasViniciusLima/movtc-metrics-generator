package br.com.romanni.metricsgenerator;

import br.com.romanni.metricsgenerator.business.DataProcess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class MyFrame extends JFrame implements ActionListener {

    JButton button;
    DataProcess dataProcess = new DataProcess();

    public MyFrame() {
        this.setTitle("Metrics Generator");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 200);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

        JLabel label = this.generateLabel();
        label.setAlignmentX(Component.CENTER_ALIGNMENT);

        this.add(Box.createRigidArea(new Dimension(0,30)));
        this.add(label);

        generateJButton();

        this.add(Box.createRigidArea(new Dimension(0,20)));
        this.add(button);

        this.setVisible(true);
    }

    private JLabel generateLabel() {
        JLabel label = new JLabel();
        label.setText("Selecione o arquivo para gerar as métricas!");
        label.setFont(new Font("Arial", Font.BOLD, 22));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        return label;
    }

    public void generateJButton() {
        button = new JButton("Selecionar arquivo");
        button.addActionListener(this);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setBackground(new Color(255, 140, 0));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        this.handleButtonAction(e);
    }

    private void handleButtonAction(ActionEvent e) {
        if (e.getSource() != this.button) return;

        try {
            JFileChooser fileChooser = new JFileChooser();

            int response = fileChooser.showOpenDialog(null);
            if (response == JFileChooser.APPROVE_OPTION) {
                var path = fileChooser.getSelectedFile().getAbsolutePath();
                System.out.println(path);
                this.dataProcess.processCSV(path);
            }
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
