package ma.est.gestion.model;

public class Utilisateur {

    private int id;
    private String login;
    private String password;
    private String statut; // "ACTIF" ou "INACTIF"
    private Role role;
    private String numAdherent;

    public Utilisateur(int id, String login, String password, String statut, Role role, String numAdherent) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.statut = statut;
        this.role = role;
        this.numAdherent = numAdherent;
    }

    public Utilisateur() {}

    // Getters & Setters
    public int getId() { return id; }
    public String getLogin() { return login; }
    public String getPassword() { return password; }
    public String getStatut() { return statut; }
    public Role getRole() { return role; }
    public String getNumAdherent() { return numAdherent; }

    public void setId(int id) { this.id = id; }
    public void setLogin(String login) { this.login = login; }
    public void setPassword(String password) { this.password = password; }
    public void setStatut(String statut) { this.statut = statut; }
    public void setRole(Role role) { this.role = role; }
    public void setNumAdherent(String numAdherent) { this.numAdherent = numAdherent; }
}
