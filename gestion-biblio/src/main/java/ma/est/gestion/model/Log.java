package ma.est.gestion.model;

import java.time.LocalDateTime;

public class Log {
    private LocalDateTime dateAction;
    private String action;
    private Utilisateur utilisateur;

    public Log(LocalDateTime dateAction, String action, Utilisateur utilisateur) {
        this.dateAction = dateAction;
        this.action = action;
        this.utilisateur = utilisateur;
    }

    // Getters / Setters
    public LocalDateTime getDateAction() { return dateAction; }
    public String getAction() { return action; }
    public Utilisateur getUtilisateur() { return utilisateur; }

    public void setDateAction(LocalDateTime dateAction) { this.dateAction = dateAction; }
    public void setAction(String action) { this.action = action; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
}
