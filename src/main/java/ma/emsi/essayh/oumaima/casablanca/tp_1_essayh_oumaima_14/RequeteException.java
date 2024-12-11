package ma.emsi.essayh.oumaima.casablanca.tp_1_essayh_oumaima_14;


/**
 * Exception personnalisée pour gérer les erreurs liées aux requêtes LLM.
 */
public class RequeteException extends Exception {

    // Constructeur avec un message d'erreur
    public RequeteException(String message, String s) {
        super(message);
    }

    // Constructeur avec un message d'erreur et la cause
    public RequeteException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructeur avec la cause uniquement
    public RequeteException(Throwable cause) {
        super(cause);
    }
}

