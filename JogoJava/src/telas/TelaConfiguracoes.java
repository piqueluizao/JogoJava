package telas;

import java.awt.*;
import java.io.File;
import java.util.Locale;
import javax.swing.*;
import lixomania.ConfiguracoesJogo;
import lixomania.LixoMania;
import lixomania.i18n.LocaleManager; // Importar LixoMania para showTelaInicial

public class TelaConfiguracoes extends JPanel {
    public TelaConfiguracoes(JFrame frame) {
        setBackground(Color.decode("#868383"));
        setLayout(null);

        ImageIcon backgroundIcon = carregarIcon("assets/reciclagem.png");
        JLabel backgroundLabel;

        if (backgroundIcon == null) {
            backgroundLabel = new JLabel();
            backgroundLabel.setBackground(Color.decode("#868383"));
            backgroundLabel.setOpaque(true);
        } else {
            Image scaledImage = backgroundIcon.getImage().getScaledInstance(400, 700, Image.SCALE_SMOOTH);
            backgroundLabel = new JLabel(new ImageIcon(scaledImage));
        }

        backgroundLabel.setBounds(0, 0, 400, 700);
        add(backgroundLabel);

        JLabel somLabel = new JLabel(LocaleManager.getString("telaConfiguracoes.som"));
        somLabel.setForeground(Color.WHITE);
        somLabel.setBounds(100, 100, 100, 30);
        backgroundLabel.add(somLabel);

        JRadioButton somOn = new JRadioButton();
        somOn.setBounds(200, 100, 20, 20);
        somOn.setSelected(ConfiguracoesJogo.isSomAtivado());
        somOn.setOpaque(false);
        somOn.addActionListener(e -> ConfiguracoesJogo.setSomAtivado(somOn.isSelected()));
        backgroundLabel.add(somOn);

        JLabel musicaLabel = new JLabel(LocaleManager.getString("telaConfiguracoes.musica"));
        musicaLabel.setForeground(Color.WHITE);
        musicaLabel.setBounds(100, 150, 100, 30);
        backgroundLabel.add(musicaLabel);

        JRadioButton musicaOn = new JRadioButton();
        musicaOn.setBounds(200, 150, 20, 20);
        musicaOn.setSelected(ConfiguracoesJogo.isMusicaAtivada());
        musicaOn.setOpaque(false);
        musicaOn.addActionListener(e -> ConfiguracoesJogo.setMusicaAtivada(musicaOn.isSelected()));
        backgroundLabel.add(musicaOn);

        JLabel idiomaLabel = new JLabel(LocaleManager.getString("telaConfiguracoes.idioma"));
        idiomaLabel.setForeground(Color.WHITE);
        idiomaLabel.setBounds(100, 200, 200, 30);
        backgroundLabel.add(idiomaLabel);

        String[] idiomas = { "Português", "Inglês" };
        JComboBox<String> idiomaBox = new JComboBox<>(idiomas);
        idiomaBox.setBounds(100, 230, 200, 30);
        // Seleciona o idioma atual no ComboBox
        if (LocaleManager.getCurrentLocale().getLanguage().equals("pt")) {
            idiomaBox.setSelectedItem("Português");
        } else if (LocaleManager.getCurrentLocale().getLanguage().equals("en")) {
            idiomaBox.setSelectedItem("Inglês");
        }
        idiomaBox.addActionListener(e -> {
            String selectedIdioma = (String) idiomaBox.getSelectedItem();
            if ("Português".equals(selectedIdioma)) {
                LocaleManager.setLocale(new Locale("pt", "BR"));
            } else if ("Inglês".equals(selectedIdioma)) {
                LocaleManager.setLocale(new Locale("en", "US"));
            }
            JOptionPane.showMessageDialog(frame,
                                        LocaleManager.getString("telaConfiguracoes.idiomaAlteradoMsg"),
                                        LocaleManager.getString("telaConfiguracoes.idiomaAlteradoTitulo"),
                                        JOptionPane.INFORMATION_MESSAGE);
            // Ao mudar o idioma, podemos voltar para a tela inicial para que ela seja reconstruída
            // LixoMania.showTelaInicial(); // Removido para forçar o restart manual do usuário
        });
        backgroundLabel.add(idiomaBox);

        JButton voltarButton = new JButton(LocaleManager.getString("telaConfiguracoes.voltar"));
        voltarButton.setBounds(150, 600, 100, 40);
        voltarButton.setOpaque(true);
        voltarButton.setBackground(Color.DARK_GRAY);
        voltarButton.setForeground(Color.BLACK);
        voltarButton.setFocusPainted(false);
        voltarButton.addActionListener(e -> {
            LixoMania.showTelaInicial(); // Usar o método estático para voltar ao menu
        });
        backgroundLabel.add(voltarButton);
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