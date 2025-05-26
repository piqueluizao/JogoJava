package telas;

import java.awt.*;
import javax.swing.*;
import lixomania.i18n.LocaleManager; // Importar LocaleManager

public class TelaComoJogar extends JPanel {
    public TelaComoJogar(JFrame frame) {
        setBackground(Color.decode("#908AFF"));
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(LocaleManager.getString("telaComoJogar.titulo"), SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        add(titleLabel, BorderLayout.NORTH);

        JTextArea texto = new JTextArea(LocaleManager.getString("telaComoJogar.texto"));
        texto.setEditable(false);
        texto.setLineWrap(true);
        texto.setWrapStyleWord(true);
        texto.setBackground(new Color(0, 0, 0, 0));
        texto.setForeground(Color.WHITE);
        texto.setFont(new Font("Arial", Font.PLAIN, 14));

        JScrollPane scrollPane = new JScrollPane(texto);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        add(scrollPane, BorderLayout.CENTER);

        JButton voltarButton = new JButton(LocaleManager.getString("telaComoJogar.voltar"));
        voltarButton.setOpaque(true);
        voltarButton.setBackground(Color.BLACK);
        voltarButton.setForeground(Color.BLACK);
        voltarButton.setFocusPainted(false);
        voltarButton.addActionListener(e -> {
            frame.setContentPane(new TelaInicial(frame));
            frame.revalidate();
            frame.repaint();
        });
        add(voltarButton, BorderLayout.SOUTH);
    }
}