package ma.est.gestion.model;

public class Livre {
    private int id;
    private String code;
    private String titre;
    private String auteur;
    private int nombreExemplaire;
    private Categorie categorie;

    public Livre() {}

    public Livre(int id, String code, String titre, String auteur, int nombreExemplaire, Categorie categorie) {
        this.id = id;
        this.code = code;
        this.titre = titre;
        this.auteur = auteur;
        this.nombreExemplaire = nombreExemplaire;
        this.categorie = categorie;
    }

    // Getters et setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getTitre() { return titre; }
    public void setTitre(String titre) { this.titre = titre; }

    public String getAuteur() { return auteur; }
    public void setAuteur(String auteur) { this.auteur = auteur; }

    public int getNombreExemplaire() { return nombreExemplaire; }
    public void setNombreExemplaire(int nombreExemplaire) { this.nombreExemplaire = nombreExemplaire; }

    public Categorie getCategorie() { return categorie; }
    public void setCategorie(Categorie categorie) { this.categorie = categorie; }

    @Override
    public String toString() {
        return categorie != null ? categorie.getNom() : "";
    }
}
