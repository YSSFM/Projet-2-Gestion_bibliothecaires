package ma.est.gestion.dao.impl;
import java.util.List;

import ma.est.gestion.model.Adherent;

public interface AdherentDaoImpl {

    public void ajouter(Adherent adherent);
    public void modifier(Adherent adherent);
    public void supprimer(int numAdherent); 
    public Adherent trouverParId(int numAdherent);
    public List<Adherent> getAll();
}

