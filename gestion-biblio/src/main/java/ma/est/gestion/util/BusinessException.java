package ma.est.gestion.util;

//Exception personnalisée utilisée pour signaler les erreurs liées
 //à la logique métier (business rules) de l'application.

public class BusinessException extends RuntimeException {

    // Identifiant de sérialisation pour la compatibilité
    private static final long serialVersionUID = 1L;

  
    public BusinessException(String message) {
        super(message);
    }
    
    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}