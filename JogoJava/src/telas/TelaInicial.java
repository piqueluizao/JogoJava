package telas;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;
import lixomania.LixoMania;
import lixomania.i18n.LocaleManager;
public class TelaInicial extends JPanel {

    public TelaInicial(JFrame frame) {
        LixoMania.setMainFrame(frame);
        setBackground(Color.decode("#ff3333"));

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();


        JLabel titulo = new JLabel(LocaleManager.getString("telaInicial.titulo"));
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets = new Insets(30, 100, 30, 70);
        add(titulo, gbc);

        JButton startBtn = new JButton(LocaleManager.getString("telaInicial.start"));
        startBtn.setForeground(Color.BLACK);
        startBtn.setBackground(new Color(50, 205, 50));
        startBtn.setOpaque(true);
        startBtn.setFocusPainted(false);
        startBtn.setBorder(createRoundedBorder(20));
        startBtn.addActionListener(e -> {
            frame.setContentPane(new TelaJogo(frame, 1));
            frame.revalidate();
            frame.repaint();
        });
        gbc.gridy = 1;
        add(startBtn, gbc);

        JButton fasesBtn = new JButton(LocaleManager.getString("telaInicial.fases"));
        fasesBtn.setBackground(new Color(178, 34, 34));
        fasesBtn.setForeground(Color.BLACK);
        fasesBtn.setOpaque(true);
        fasesBtn.setFocusPainted(false);
        fasesBtn.setBorder(createRoundedBorder(20));
        fasesBtn.addActionListener(e -> {
            frame.setContentPane(new TelaFases(frame));
            frame.revalidate();
            frame.repaint();
        });

        gbc.gridy = 2;
        add(fasesBtn, gbc);

        JButton comoJogarBtn = new JButton(LocaleManager.getString("telaInicial.comoJogar"));
        comoJogarBtn.setBackground(new Color(25, 25, 112));
        comoJogarBtn.setForeground(Color.BLACK);
        comoJogarBtn.setOpaque(true);
        comoJogarBtn.setFocusPainted(false);
        comoJogarBtn.setBorder(createRoundedBorder(20));
        comoJogarBtn.addActionListener(e -> {
            frame.setContentPane(new TelaComoJogar(frame));
            frame.revalidate();
            frame.repaint();
        });

        gbc.gridy = 3;
        add(comoJogarBtn, gbc);

        JButton configBtn = new JButton(LocaleManager.getString("telaInicial.configuracoes"));
        configBtn.setBackground(new Color(128, 128, 128));
        configBtn.setForeground(Color.BLACK);
        configBtn.setOpaque(true);
        configBtn.setFocusPainted(false);
        configBtn.setBorder(createRoundedBorder(20));
        configBtn.addActionListener(e -> {
            frame.setContentPane(new TelaConfiguracoes(frame));
            frame.revalidate();
            frame.repaint();
        });

        gbc.gridy = 4;
        add(configBtn, gbc);

        JButton exitBtn = new JButton(LocaleManager.getString("telaInicial.sair"));
        exitBtn.setBackground(new Color(75, 0, 130));
        exitBtn.setForeground(Color.BLACK);
        exitBtn.setOpaque(true);
        exitBtn.setFocusPainted(false);
        exitBtn.setBorder(createRoundedBorder(20));
        exitBtn.addActionListener(e -> {
            System.exit(0);
        });

        gbc.gridy = 5;
        add(exitBtn, gbc);
    }

    private Border createRoundedBorder(int radius) {
        return new LineBorder(Color.DARK_GRAY, 2) {
            @Override
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getLineColor());
                g2.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
                g2.dispose();
            }

            @Override
            public Insets getBorderInsets(Component c) {
                return new Insets(radius / 2 + 1, radius + 1, radius / 2 + 1, radius + 1);
            }

            @Override
            public Insets getBorderInsets(Component c, Insets insets) {
                insets.left = insets.top = insets.right = insets.bottom = radius / 2 + 1;
                return insets;
            }
        };
    }
}