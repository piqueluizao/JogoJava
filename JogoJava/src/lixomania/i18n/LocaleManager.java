package lixomania.i18n;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences; // Importar Preferences

public class LocaleManager {
    private static Locale currentLocale; // Não inicializar aqui, vamos carregar ou definir
    private static ResourceBundle messages;
    private static final Preferences prefs = Preferences.userNodeForPackage(LocaleManager.class);
    private static final String LOCALE_KEY = "user_locale";

    // Bloco estático para carregar o locale preferido na inicialização
    static {
        String savedLocaleTag = prefs.get(LOCALE_KEY, "pt-BR"); // Padrão para pt-BR
        currentLocale = Locale.forLanguageTag(savedLocaleTag);
        loadBundle();
    }

    private static void loadBundle() {
        try {
            messages = ResourceBundle.getBundle("messages", currentLocale);
        } catch (MissingResourceException e) {
            System.err.println("WARN: Bundle de recursos não encontrado para o locale " + currentLocale + ". Tentando pt_BR.");
            // Fallback para pt_BR se o bundle específico não for encontrado
            currentLocale = new Locale("pt", "BR");
            messages = ResourceBundle.getBundle("messages", currentLocale);
        }
    }

    public static void setLocale(Locale newLocale) {
        currentLocale = newLocale;
        prefs.put(LOCALE_KEY, newLocale.toLanguageTag()); // Salva a preferência
        loadBundle(); // Recarrega o bundle para a nova localidade
    }

    public static Locale getCurrentLocale() {
        return currentLocale;
    }

    public static String getString(String key) {
        try {
            return messages.getString(key);
        } catch (MissingResourceException e) {
            System.err.println("Chave de tradução não encontrada: " + key + " para o locale: " + currentLocale);
            return "MISSING_KEY_" + key;
        }
    }
}