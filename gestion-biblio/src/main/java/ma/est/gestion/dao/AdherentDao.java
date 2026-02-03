package ma.est.gestion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import ma.est.gestion.dao.impl.AdherentDaoImpl;
import ma.est.gestion.model.Adherent;
import ma.est.gestion.util.DatabaseConnection;

public class AdherentDao implements AdherentDaoImpl {

    private final Connection connection = DatabaseConnection.getConnection();

    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public void ajouter(Adherent adherent) {
        // Code pour ajouter un adhérent
        String sql = "INSERT INTO adherents (nom, prenom, email) VALUES (?, ?, ?)";
        // Exécution de la requête avec les paramètres de l'objet adherent

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, adherent.getNomAdherent());
            stmt.setString(2, adherent.getPrenomAdherent());
            stmt.setString(3, adherent.getEmailAdherent());
            stmt.executeUpdate();

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public Adherent getByEmail(String email) {
    Adherent adherent = null;
    String sql = "SELECT * FROM adherents WHERE email = ?";

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            adherent = new Adherent(
                rs.getInt("id"),
                rs.getString("email"),
                rs.getString("nom"),
                rs.getString("prenom")
            );
        }
    } catch (Exception e) {
        e.printStackTrace();
    }

    return adherent;
}

    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public void modifier(Adherent adherent) {
        // Code pour modifier un adhérent
        String sql = "UPDATE adherents SET nom = ?, prenom = ?, email = ? WHERE numAdherent = ?";
        // Exécution de la requête avec les paramètres de l'objet adherent
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, adherent.getNomAdherent());
            stmt.setString(2, adherent.getPrenomAdherent());
            stmt.setString(3, adherent.getEmailAdherent());
            stmt.setInt(4, adherent.getNumAdherent());
            stmt.executeUpdate();

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public void supprimer(int numAdherent) {
        // Code pour supprimer un adhérent
        String sql = "DELETE FROM adherents WHERE numAdherent = ?";
        // Exécution de la requête avec le numéro d'adhérent
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, numAdherent);
            stmt.executeUpdate();

            stmt.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public Adherent trouverParId(int numAdherent) {
        // Code pour trouver un adhérent par son numéro
        String sql = "SELECT * FROM adherents WHERE numAdherent = ?";
        Adherent adherent = null;
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setInt(1, numAdherent);
            var rs = stmt.executeQuery();
            if (rs.next()) {
                adherent = new Adherent(
                    rs.getInt("numAdherent"),
                    rs.getString("email"),
                    rs.getString("nom"),
                    rs.getString("prenom")
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adherent;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public List<Adherent> getAll() {
        // Code pour lister tous les adhérents
        String sql = "SELECT * FROM adherents";
        List<Adherent> adherents = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            var rs = stmt.executeQuery();
            while (rs.next()) {
                Adherent adherent = new Adherent(
                    rs.getInt("numAdherent"),
                    rs.getString("email"),
                    rs.getString("nom"),
                    rs.getString("prenom")
                );
                adherents.add(adherent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return adherents;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public List<Adherent> AfficherTousLesAdherents() {
       List<Adherent> adherents = new ArrayList<>();
         String sql = "SELECT * FROM adherents";

         try (PreparedStatement stmt = connection.prepareStatement(sql)){
             var rs = stmt.executeQuery();
             while (rs.next()) {
                Adherent adherent = new Adherent();
                
                adherent.setNumAdherent(rs.getInt("numAdherent"));
                adherent.setNomAdherent(rs.getString("nom"));
                adherent.setPrenomAdherent(rs.getString("prenom"));
                adherent.setEmailAdherent(rs.getString("email"));
                adherents.add(adherent);
             }

            } catch (Exception e) {
             e.printStackTrace();
             }
         return adherents;
    }
}
