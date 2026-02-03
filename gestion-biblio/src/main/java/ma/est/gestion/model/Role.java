package ma.est.gestion.model;

public class Role {
    private String nomRole; // ex : "ADMIN", "ADHERENT"

    public Role(String nomRole) {
        this.nomRole = nomRole;
    }

    public String getNomRole() { return nomRole; }
    public void setNomRole(String nomRole) { this.nomRole = nomRole; }

    @Override
    public String toString() { return nomRole; }
}
