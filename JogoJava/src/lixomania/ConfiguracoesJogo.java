package lixomania;

public class ConfiguracoesJogo {
    private static boolean somAtivado = true;
    private static boolean musicaAtivada = true;

    public static boolean isSomAtivado() {
        return somAtivado;
    }

    public static void setSomAtivado(boolean somAtivado) {
        ConfiguracoesJogo.somAtivado = somAtivado;
    }

    public static boolean isMusicaAtivada() {
        return musicaAtivada;
    }

    public static void setMusicaAtivada(boolean musicaAtivada) {
        ConfiguracoesJogo.musicaAtivada = musicaAtivada;
    }
}