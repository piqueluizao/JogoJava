package telas;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.*;
import lixomania.LixoMania;
import lixomania.i18n.LocaleManager;

public class TelaFim extends JPanel {

    public TelaFim(JFrame frame, int pontuacaoFinal, int fase, boolean venceu) {
        setBackground(Color.decode("#ff8533"));
        setLayout(null);

        ImageIcon backgroundIcon = carregarIcon("assets/reciclagem.jpg");
        if (backgroundIcon == null) {
            JPanel solidBackground = new JPanel();
            solidBackground.setBackground(Color.decode("#ff8533"));
            solidBackground.setBounds(0, 0, 400, 700);
            add(solidBackground);
        } else {
            Image scaledImage = backgroundIcon.getImage().getScaledInstance(400, 700, Image.SCALE_SMOOTH);
            JLabel background = new JLabel(new ImageIcon(scaledImage));
            background.setBounds(0, 0, 400, 700);
            add(background);
        }

        // Usar LocaleManager para o título
        JLabel titulo = new JLabel(venceu ? LocaleManager.getString("telaFim.vitoria") : LocaleManager.getString("telaFim.fimDeJogo"));
        titulo.setFont(new Font("Arial", Font.BOLD, 36));
        titulo.setForeground(Color.BLACK);
        titulo.setHorizontalAlignment(SwingConstants.CENTER);
        titulo.setBounds(0, 100, 400, 50);
        add(titulo);

        JLabel pontuacaoLabel = new JLabel(LocaleManager.getString("telaFim.pontuacaoFinal") + " " + pontuacaoFinal);
        pontuacaoLabel.setFont(new Font("Arial", Font.BOLD, 24));
        pontuacaoLabel.setForeground(Color.BLACK);
        pontuacaoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pontuacaoLabel.setBounds(0, 180, 400, 40);
        add(pontuacaoLabel);

        JLabel mensagemOutcome = new JLabel();
        mensagemOutcome.setFont(new Font("Arial", Font.PLAIN, 16));
        mensagemOutcome.setForeground(Color.BLACK);
        mensagemOutcome.setHorizontalAlignment(SwingConstants.CENTER);
        mensagemOutcome.setBounds(0, 230, 400, 30);
        add(mensagemOutcome);

        if (venceu) {
            if (fase < 3) {
                // Usar String.format para substituir %d com o número da fase
                mensagemOutcome.setText(String.format(LocaleManager.getString("telaFim.mensagemFaseCompleta"), fase));
            } else {
                mensagemOutcome.setText(LocaleManager.getString("telaFim.mensagemJogoCompleto"));
            }
        } else {
            mensagemOutcome.setText(LocaleManager.getString("telaFim.mensagemDerrota"));
        }

        JButton reiniciarButton = new JButton(LocaleManager.getString("telaFim.reiniciarFase"));
        reiniciarButton.setBounds(100, 300, 200, 40);
        reiniciarButton.setBackground(Color.GREEN.darker());
        reiniciarButton.setForeground(Color.BLACK);
        reiniciarButton.setFocusPainted(false);
        reiniciarButton.addActionListener(e -> {
            frame.setContentPane(new TelaJogo(frame, fase));
            frame.revalidate();
            frame.repaint();
        });
        add(reiniciarButton);

        JButton proximaFaseButton = new JButton(LocaleManager.getString("telaFim.proximaFase"));
        if (venceu && fase < 3) {
            proximaFaseButton.setBounds(100, 350, 200, 40);
            proximaFaseButton.setBackground(Color.BLUE.darker());
            proximaFaseButton.setForeground(Color.BLACK);
            proximaFaseButton.setFocusPainted(false);
            proximaFaseButton.addActionListener(e -> {
                frame.setContentPane(new TelaJogo(frame, fase + 1));
                frame.revalidate();
                frame.repaint();
            });
            add(proximaFaseButton);
        }

        JButton menuButton = new JButton(LocaleManager.getString("telaFim.menuPrincipal"));
        menuButton.setBounds(100, (venceu && fase < 3) ? 400 : 350, 200, 40);
        menuButton.setBackground(Color.GRAY.darker());
        menuButton.setForeground(Color.BLACK);
        menuButton.setFocusPainted(false);
        menuButton.addActionListener(new MeuListener(frame));
        add(menuButton);
    }

    private ImageIcon carregarIcon(String path) {
        try {
            java.net.URL imgURL = getClass().getClassLoader().getResource(path);
            if (imgURL != null) {
                return new ImageIcon(imgURL);
            } else {
                File file = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + path);
                if (file.exists() && file.isFile() && file.canRead()) {
                    return new ImageIcon(file.getAbsolutePath());
                }
                System.err.println("Recurso não encontrado: " + path);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar imagem: " + path + " - " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

class MeuListener implements ActionListener {
    private final JFrame frame;

    public MeuListener(JFrame frame) {
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        LixoMania.showTelaInicial();
    }
}