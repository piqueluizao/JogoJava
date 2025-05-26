package lixomania;

import javax.swing.*;

public class LixoMania {
    private static JFrame mainFrame;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            mainFrame = new JFrame("LixoMania");
            mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            mainFrame.setSize(400, 700);
            mainFrame.setResizable(false);
            mainFrame.setContentPane(new telas.TelaInicial(mainFrame));
            mainFrame.setVisible(true);
        });
    }

    public static void setMainFrame(JFrame frame) {
        mainFrame = frame;
    }

    public static void showTelaInicial() {
        if (mainFrame != null) {
            mainFrame.setContentPane(new telas.TelaInicial(mainFrame));
            mainFrame.revalidate();
            mainFrame.repaint();
        } else {
            System.err.println("Erro: mainFrame não inicializado ao tentar mostrar TelaInicial.");
        }
    }

    public static void showTelaFim(int pontuacaoFinal, int fase, boolean venceu) {
        if (mainFrame != null) {
            mainFrame.setContentPane(new telas.TelaFim(mainFrame, pontuacaoFinal, fase, venceu));
            mainFrame.revalidate();
            mainFrame.repaint();
        }
    }
}