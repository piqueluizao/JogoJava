package telas;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import lixomania.ConfiguracoesJogo;
import lixomania.i18n.LocaleManager;

public class TelaJogo extends JPanel {
    private int pontuacao = 0;
    private int vidas = 3;
    private int tempo;
    private final JLabel pontuacaoLabel;
    private final JLabel tempoLabel;
    private final JPanel vidasPanel;
    private javax.swing.Timer gameTimer;
    private transient java.util.Timer lixoTimer;
    private final JFrame frame;
    private final int fase;
    private final List<Lixeira> lixeiras = new ArrayList<>();
    private final Random random = new Random();

    private final Map<String, List<String>> tiposLixoPorCategoria = new HashMap<>();
    private final List<String> categoriasLixoAtivas = new ArrayList<>();

    private static final int LIXEIRA_WIDTH = 80;
    private static final int LIXEIRA_HEIGHT = 100;
    private static final int LIXO_WIDTH = 70;
    private static final int LIXO_HEIGHT = 70;

    private static final int PONTUACAO_MINIMA_PARA_AVANCAR = 50;

    public TelaJogo(JFrame frame, int fase) {
        System.out.println("==========================================");
        System.out.println("Iniciando TelaJogo - Fase " + fase);
        System.out.println("==========================================");

        this.frame = frame;
        this.fase = fase;

        addAncestorListener(new AncestorListener() {
            @Override
            public void ancestorAdded(AncestorEvent event) {
            }

            @Override
            public void ancestorRemoved(AncestorEvent event) {
                System.out.println("TelaJogo removida da hierarquia. Parando timers...");
                pararTimers();
            }

            @Override
            public void ancestorMoved(AncestorEvent event) {
            }
        });

        configurarFase();
        setBackground(Color.decode("#87CEEB"));
        setLayout(null);

        popularTiposLixo();

        pontuacaoLabel = new JLabel(LocaleManager.getString("telaJogo.pontos") + " " + pontuacao);
        pontuacaoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        pontuacaoLabel.setForeground(Color.BLACK);
        pontuacaoLabel.setBounds(10, 10, 150, 30);
        add(pontuacaoLabel);

        tempoLabel = new JLabel(LocaleManager.getString("telaJogo.tempo") + " " + tempo);
        tempoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        tempoLabel.setForeground(Color.BLACK);
        tempoLabel.setBounds(280, 10, 100, 30);
        add(tempoLabel);

        vidasPanel = new JPanel();
        vidasPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        vidasPanel.setOpaque(false);
        vidasPanel.setBounds(0, 40, frame.getWidth(), 50);
        add(vidasPanel);
        atualizarVidas();

        iniciarJogo();
    }

    private void configurarFase() {
        int baseLixeiraY = frame.getHeight() - LIXEIRA_HEIGHT - 60 - 20;

        switch (fase) {
            case 1:
                tempo = 60;
                categoriasLixoAtivas.add("papel");
                categoriasLixoAtivas.add("plastico");
                adicionarLixeira("papel", new Point(frame.getWidth() / 4 - LIXEIRA_WIDTH / 2, baseLixeiraY), "azul", "Papel");
                adicionarLixeira("plastico", new Point(3 * frame.getWidth() / 4 - LIXEIRA_WIDTH / 2, baseLixeiraY), "laranja", "Plástico");
                break;
            case 2:
                tempo = 90;
                categoriasLixoAtivas.add("papel");
                categoriasLixoAtivas.add("plastico");
                categoriasLixoAtivas.add("vidro");
                categoriasLixoAtivas.add("metal");
                adicionarLixeira("papel", new Point(frame.getWidth() / 8 - LIXEIRA_WIDTH / 2, baseLixeiraY), "azul", "Papel");
                adicionarLixeira("plastico", new Point(3 * frame.getWidth() / 8 - LIXEIRA_WIDTH / 2, baseLixeiraY), "laranja", "Plástico");
                adicionarLixeira("vidro", new Point(5 * frame.getWidth() / 8 - LIXEIRA_WIDTH / 2, baseLixeiraY), "verde", "Vidro");
                adicionarLixeira("metal", new Point(7 * frame.getWidth() / 8 - LIXEIRA_WIDTH / 2, baseLixeiraY), "amarelo", "Metal");
                break;
            case 3:
                tempo = 120;
                categoriasLixoAtivas.add("papel");
                categoriasLixoAtivas.add("plastico");
                categoriasLixoAtivas.add("vidro");
                categoriasLixoAtivas.add("metal");
                categoriasLixoAtivas.add("organico");
                categoriasLixoAtivas.add("eletronico");
                int linha1Y = baseLixeiraY;
                int linha2Y = baseLixeiraY - LIXEIRA_HEIGHT - 20 - 20;

                adicionarLixeira("papel", new Point(frame.getWidth() / 6 - LIXEIRA_WIDTH / 2, linha1Y), "azul", "Papel");
                adicionarLixeira("plastico", new Point(3 * frame.getWidth() / 6 - LIXEIRA_WIDTH / 2, linha1Y), "laranja", "Plástico");
                adicionarLixeira("vidro", new Point(5 * frame.getWidth() / 6 - LIXEIRA_WIDTH / 2, linha1Y), "verde", "Vidro");

                adicionarLixeira("metal", new Point(frame.getWidth() / 6 - LIXEIRA_WIDTH / 2, linha2Y), "amarelo", "Metal");
                adicionarLixeira("organico", new Point(3 * frame.getWidth() / 6 - LIXEIRA_WIDTH / 2, linha2Y), "cinza", "Orgânico");
                adicionarLixeira("eletronico", new Point(5 * frame.getWidth() / 6 - LIXEIRA_WIDTH / 2, linha2Y), "vermelho", "Eletrônico");
                break;
            default:
                System.err.println("Fase inválida: " + fase);
                lixomania.LixoMania.showTelaInicial();
                return;
        }
        for (Lixeira lixeira : lixeiras) {
            add(lixeira);
        }
    }

    private void adicionarLixeira(String tipo, Point position, String corHex, String nomeExibicao) {
        ImageIcon originalIcon = carregarIcon("assets/lixeiras/" + corHex + "_lixeira.png");
        if (originalIcon != null) {
            Image scaledImage = originalIcon.getImage().getScaledInstance(LIXEIRA_WIDTH, LIXEIRA_HEIGHT, Image.SCALE_SMOOTH);
            ImageIcon scaledIcon = new ImageIcon(scaledImage);
            Lixeira lixeira = new Lixeira(tipo, scaledIcon, nomeExibicao);
            lixeira.setBounds(position.x, position.y, LIXEIRA_WIDTH, LIXEIRA_HEIGHT + 20);
            lixeiras.add(lixeira);
        } else {
            System.err.println("Não foi possível carregar ou redimensionar ícone da lixeira para: " + corHex);
        }
    }

    private void popularTiposLixo() {
        tiposLixoPorCategoria.put("papel", Arrays.asList("jornal.png", "caixa.png", "papel.png", "leite.png"));
        tiposLixoPorCategoria.put("plastico", Arrays.asList("garrafa.png", "sacola.png", "copo.png"));
        tiposLixoPorCategoria.put("vidro", Arrays.asList("jarra.png", "pote.png", "xicara.png"));
        tiposLixoPorCategoria.put("metal", Arrays.asList("lata.png", "cola.png", "tesoura.png", "spray.png"));
        tiposLixoPorCategoria.put("organico", Arrays.asList("banana.png", "ovo.png", "frango.png", "esqueleto.png"));
        tiposLixoPorCategoria.put("eletronico", Arrays.asList("celular.png", "bateria.png", "computador.png", "lampada.png", "chaleira.png"));
    }

    private void iniciarJogo() {
        gameTimer = new javax.swing.Timer(1000, e -> {
            tempo--;
            tempoLabel.setText(LocaleManager.getString("telaJogo.tempo") + " " + tempo);
            if (tempo <= 0) {
                gameTimer.stop();
                fimDeJogo();
            }
        });
        gameTimer.start();

        lixoTimer = new java.util.Timer();
        lixoTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> gerarLixo());
            }
        }, 0, 2000);
    }

    private void gerarLixo() {
        if (categoriasLixoAtivas.isEmpty()) {
            System.out.println("Nenhuma categoria de lixo ativa para gerar lixo.");
            return;
        }

        String categoriaSelecionada = categoriasLixoAtivas.get(random.nextInt(categoriasLixoAtivas.size()));
        List<String> imagensDaCategoria = tiposLixoPorCategoria.get(categoriaSelecionada);

        if (imagensDaCategoria == null || imagensDaCategoria.isEmpty()) {
            System.err.println("Nenhuma imagem definida para a categoria: " + categoriaSelecionada);
            return;
        }

        String imagemLixo = imagensDaCategoria.get(random.nextInt(imagensDaCategoria.size()));

        ImageIcon originalIcon = carregarIcon("assets/lixo/" + categoriaSelecionada + "/" + imagemLixo);
        if (originalIcon == null) {
            System.err.println("Ícone de lixo não carregado: " + "assets/lixo/" + categoriaSelecionada + "/" + imagemLixo);
            return;
        }
        Image scaledLixoImage = originalIcon.getImage().getScaledInstance(LIXO_WIDTH, LIXO_HEIGHT, Image.SCALE_SMOOTH);
        ImageIcon scaledLixoIcon = new ImageIcon(scaledLixoImage);

        Lixo lixo = new Lixo(categoriaSelecionada, scaledLixoIcon);
        int x = random.nextInt(frame.getWidth() - LIXO_WIDTH);
        lixo.setBounds(x, -LIXO_HEIGHT, LIXO_WIDTH, LIXO_HEIGHT);
        add(lixo);
        revalidate();
        repaint();

        new Thread(() -> {
            lixo.cair(() -> {
                if (!lixo.foiReciclado()) {
                    System.out.println("Lixo '" + lixo.getTipo() + "' caiu no chão.");
                    perderVida();
                    tocarSom("assets/audio/erro.wav");
                    SwingUtilities.invokeLater(() -> {
                        remove(lixo);
                        repaint();
                    });
                }
            });
        }).start();
    }

    private void perderVida() {
        vidas--;
        atualizarVidas();
        if (vidas <= 0) {
            fimDeJogo();
        }
    }

    private void atualizarVidas() {
        vidasPanel.removeAll();
        ImageIcon coracaoIcon = carregarIcon("assets/coracao.png");
        if (coracaoIcon != null) {
            Image scaledCoracao = coracaoIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
            ImageIcon scaledCoracaoIcon = new ImageIcon(scaledCoracao);
            for (int i = 0; i < vidas; i++) {
                JLabel coracaoLabel = new JLabel(scaledCoracaoIcon);
                vidasPanel.add(coracaoLabel);
            }
        } else {
            System.err.println("Ícone de coração não carregado.");
        }
        vidasPanel.revalidate();
        vidasPanel.repaint();
    }

    private void aumentarPontuacao(int pontos) {
        pontuacao += pontos;
        pontuacaoLabel.setText(LocaleManager.getString("telaJogo.pontos") + " " + pontuacao);
        tocarSom("assets/audio/acerto.wav");
    }

    private void verificarDescarte(Lixo lixo) {
        List<Lixeira> lixeirasOrdenadas = new ArrayList<>(lixeiras);
        lixeirasOrdenadas.sort(Comparator.comparingInt(Component::getY));

        for (Lixeira lixeira : lixeirasOrdenadas) {
            if (lixo.getBounds().intersects(lixeira.getBounds())) {
                if (lixeira.getTipoLixo().equals(lixo.getTipo())) {
                    lixo.setReciclado(true);
                    aumentarPontuacao(10);
                    SwingUtilities.invokeLater(() -> {
                        remove(lixo);
                        repaint();
                    });
                    return;
                }
            }
        }

        System.out.println("Item descartado incorretamente: " + lixo.getTipo());
        perderVida();
        tocarSom("assets/audio/erro.wav");
    }

    private void tocarSom(String filePath) {
        if (!ConfiguracoesJogo.isSomAtivado()) {
            return;
        }
        try {
            InputStream audioStream = getClass().getClassLoader().getResourceAsStream(filePath);
            if (audioStream == null) {
                File audioFile = new File(System.getProperty("user.dir") + File.separator + "src" + File.separator + filePath);
                if (audioFile.exists() && audioFile.isFile() && audioFile.canRead()) {
                    audioStream = new FileInputStream(audioFile);
                } else {
                    System.err.println("Arquivo de áudio não encontrado: " + filePath);
                    return;
                }
            }

            AudioInputStream audioIn = AudioSystem.getAudioInputStream(audioStream);
            Clip clip = AudioSystem.getClip();
            clip.open(audioIn);
            clip.start();
            clip.addLineListener(event -> {
                if (event.getType() == LineEvent.Type.STOP) {
                    clip.close();
                }
            });
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            System.err.println("Erro ao tocar som " + filePath + ": " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void fimDeJogo() {
        pararTimers();
        boolean venceu = false;

        if (tempo <= 0 && pontuacao >= PONTUACAO_MINIMA_PARA_AVANCAR) {
            venceu = true;
        } else if (vidas <= 0) {
            venceu = false;
        } else if (tempo <= 0 && pontuacao < PONTUACAO_MINIMA_PARA_AVANCAR) {
            venceu = false;
        }

        lixomania.LixoMania.showTelaFim(pontuacao, fase, venceu);
    }

    private void pararTimers() {
        if (gameTimer != null && gameTimer.isRunning()) {
            gameTimer.stop();
            System.out.println("gameTimer parado.");
        }
        if (lixoTimer != null) {
            lixoTimer.cancel();
            lixoTimer.purge();
            System.out.println("lixoTimer cancelado e purgado.");
        }
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

    private class Lixeira extends JPanel {
        private final String tipoLixo;
        private final JLabel imagemLabel;
        private final JLabel nomeLabel;

        public Lixeira(String tipoLixo, ImageIcon icon, String nomeExibicao) {
            this.tipoLixo = tipoLixo;
            setLayout(new BorderLayout());
            setOpaque(false);

            imagemLabel = new JLabel(icon);
            imagemLabel.setHorizontalAlignment(SwingConstants.CENTER);
            imagemLabel.setVerticalAlignment(SwingConstants.BOTTOM);

            nomeLabel = new JLabel(nomeExibicao, SwingConstants.CENTER);
            nomeLabel.setFont(new Font("Arial", Font.BOLD, 12));
            nomeLabel.setForeground(Color.BLACK);
            nomeLabel.setVerticalAlignment(SwingConstants.TOP);

            add(imagemLabel, BorderLayout.CENTER);
            add(nomeLabel, BorderLayout.NORTH);
        }

        public String getTipoLixo() {
            return tipoLixo;
        }
    }

    private class Lixo extends JLabel {
        private final String tipo;
        private boolean reciclado = false;
        private int xOffset;
        private int yOffset;

        public Lixo(String tipo, ImageIcon icon) {
            super(icon);
            this.tipo = tipo;
            setOpaque(false);
            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    xOffset = e.getX();
                    yOffset = e.getY();
                    setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                    getParent().setComponentZOrder(Lixo.this, 0);
                    getParent().repaint();
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    setCursor(Cursor.getDefaultCursor());
                    verificarDescarte(Lixo.this);
                }
            });

            addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    int newX = getX() + e.getX() - xOffset;
                    int newY = getY() + e.getY() - yOffset;

                    newX = Math.max(0, Math.min(newX, getParent().getWidth() - getWidth()));
                    newY = Math.max(0, Math.min(newY, getParent().getHeight() - getHeight()));

                    setLocation(newX, newY);
                }
            });
        }

        public String getTipo() {
            return tipo;
        }

        public boolean foiReciclado() {
            return reciclado;
        }

        public void setReciclado(boolean reciclado) {
            this.reciclado = reciclado;
        }

        public void cair(Runnable aoCairNoChao) {
            try {
                int chaoY = getParent().getHeight() - 40;
                while (getY() < chaoY && !reciclado) {
                    Thread.sleep(30);
                    SwingUtilities.invokeAndWait(() -> setLocation(getX(), getY() + 5));
                }
                if (!reciclado) {
                    SwingUtilities.invokeLater(aoCairNoChao);
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (java.lang.reflect.InvocationTargetException e) {
                System.err.println("Erro ao mover lixo na EDT: " + e.getMessage());
            }
        }
    }
}