package telas;

import java.awt.*;
import javax.swing.*;
import lixomania.i18n.LocaleManager; // Importar LocaleManager

public class TelaFases extends JPanel {
    public TelaFases(JFrame frame) {
        setBackground(Color.decode("#F0FF6B"));
        setLayout(null);

        JLabel titulo = new JLabel(LocaleManager.getString("telaFases.titulo"));
        titulo.setFont(new Font("Arial", Font.BOLD, 20));
        titulo.setForeground(Color.BLACK);
        titulo.setBounds(110, 40, 200, 30);
        add(titulo);

        JButton fase1 = new JButton(LocaleManager.getString("telaFases.fase1"));
        fase1.setBounds(120, 100, 160, 40);
        fase1.setBackground(Color.GREEN);
        fase1.setForeground(Color.GREEN); // Mantenha a cor do texto
        fase1.setOpaque(true);
        fase1.setFocusPainted(false);
        fase1.addActionListener(e -> {
            frame.setContentPane(new TelaJogo(frame, 1));
            frame.revalidate();
            frame.repaint();
        });
        add(fase1);

        JButton fase2 = new JButton(LocaleManager.getString("telaFases.fase2"));
        fase2.setBounds(120, 160, 160, 40);
        fase2.setBackground(Color.ORANGE);
        fase2.setForeground(Color.ORANGE); // Mantenha a cor do texto
        fase2.setOpaque(true);
        fase2.setFocusPainted(false);
        fase2.addActionListener(e -> {
            frame.setContentPane(new TelaJogo(frame, 2));
            frame.revalidate();
            frame.repaint();
        });
        add(fase2);

        JButton fase3 = new JButton(LocaleManager.getString("telaFases.fase3"));
        fase3.setBounds(120, 220, 160, 40);
        fase3.setBackground(Color.RED);
        fase3.setForeground(Color.RED); // Mantenha a cor do texto
        fase3.setOpaque(true);
        fase3.setFocusPainted(false);
        fase3.addActionListener(e -> {
            frame.setContentPane(new TelaJogo(frame, 3));
            frame.revalidate();
            frame.repaint();
        });
        add(fase3);

        JButton voltarButton = new JButton(LocaleManager.getString("telaFases.voltar"));
        voltarButton.setBounds(120, 300, 160, 40);
        voltarButton.setBackground(Color.BLACK);
        voltarButton.setForeground(Color.BLACK);
        voltarButton.setFocusPainted(false);
        voltarButton.addActionListener(e -> {
            frame.setContentPane(new TelaInicial(frame));
            frame.revalidate();
            frame.repaint();
        });
        add(voltarButton);
    }
}