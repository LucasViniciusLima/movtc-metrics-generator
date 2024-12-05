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
        this.setSize(500, 300);
        this.setLayout(new FlowLayout());

        this.add(this.generateLabel());

        button = new JButton("Selecionar arquivo");
        button.addActionListener(this);
        button.setSize(100, 50);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);

        this.add(button);
        this.pack();
        this.setVisible(true);
    }

    private JLabel generateLabel() {
        JLabel label = new JLabel();
        label.setText("Selecione o arquivo para gerar as métricas!");
        label.setVerticalTextPosition(SwingConstants.TOP);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setVerticalAlignment(SwingConstants.CENTER);
        return label;
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
