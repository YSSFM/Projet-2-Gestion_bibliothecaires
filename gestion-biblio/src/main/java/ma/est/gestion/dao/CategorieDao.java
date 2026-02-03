package ma.est.gestion.dao;

import java.util.Arrays;
import java.util.List;
import ma.est.gestion.model.Categorie;

public class CategorieDao {

    public List<Categorie> getAll() {
        return Arrays.asList(
                new Categorie("Informatique"),
                new Categorie("Mathématiques"),
                new Categorie("Réseaux"),
                new Categorie("IA")
        );
    }
}

