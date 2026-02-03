package ma.est.gestion.model;

public class Adherent {

    private int numAdherent;
    private String nom;
    private String prenom;
    private String email;

    public Adherent() {}

    public Adherent(int numAdherent, String email, String nom, String prenom) {
        this.numAdherent = numAdherent;
        this.email = email;
        this.nom = nom;
        this.prenom = prenom;
    }

    // ================= GETTERS MODERNES =================
    public int getNumAdherent() {
        return numAdherent;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getEmail() {
        return email;
    }

    // ================= SETTERS MODERNES =================
    public void setNumAdherent(int numAdherent) {
        this.numAdherent = numAdherent;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // ================== COMPATIBILITÉ ANCIEN CODE ==================


    public String getNomAdherent() {
        return nom;
    }

    public String getPrenomAdherent() {
        return prenom;
    }

    public void setNomAdherent(String nom) {
        this.nom = nom;
    }

    public void setPrenomAdherent(String prenom) {
        this.prenom = prenom;
    }

    public String getEmailAdherent() {
        return email;
    }

    public void setEmailAdherent(String email) {
        this.email = email;
    }
}
