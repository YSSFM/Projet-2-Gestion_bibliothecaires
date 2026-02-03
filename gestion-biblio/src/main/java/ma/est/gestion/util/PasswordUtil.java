package ma.est.gestion.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

 //Classe utilitaire pour le hachage et la vérification des mots de passe.

public class PasswordUtil {
    
    // Constructeur privé pour empêcher l'instanciation de cette classe utilitaire
    private PasswordUtil() {}

    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            
            // Convertit le tableau de bytes en chaîne hexadécimale
            for (byte b : hash) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            // Cette erreur est critique et indique un problème d'environnement Java
            throw new RuntimeException("Erreur: Algorithme de hachage SHA-256 introuvable.", e);
        }
    }

    public static boolean verifyPassword(String raw, String hashed) {
        // On hache le mot de passe clair et on le compare au hachage stocké
        return hashPassword(raw).equals(hashed);
    }
}