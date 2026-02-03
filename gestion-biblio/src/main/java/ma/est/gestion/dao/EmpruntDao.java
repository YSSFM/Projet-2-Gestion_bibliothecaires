package ma.est.gestion.dao;

import java.sql.Connection;
import java.util.List;

import ma.est.gestion.model.Emprunt;
import ma.est.gestion.util.DatabaseConnection;


public interface EmpruntDao {

    final Connection connection = DatabaseConnection.getConnection();

    void addEmprunt(Emprunt e);

    void modifierEmprunt(Emprunt e);

    List<Emprunt> getAllEmprunts();

    List<Emprunt> getEmpruntsByAdherent(int numAdherent);

    void updateEmprunt(Emprunt e, String newStatut);

    void deleteEmprunt(Emprunt e);

    Emprunt findEmpruntByCode(String codeEmprunt);

    void cloturerEmprunt(Emprunt e);

    int getEmpruntCount();

}