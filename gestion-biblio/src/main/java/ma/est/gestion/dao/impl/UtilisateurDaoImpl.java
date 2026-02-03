package ma.est.gestion.dao.impl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ma.est.gestion.model.Role;
import ma.est.gestion.model.Utilisateur;
import ma.est.gestion.util.DatabaseConnection;

public class UtilisateurDaoImpl {

    @SuppressWarnings("CallToPrintStackTrace")
    public Utilisateur authentifier(String login, String password) {
        String sql = "SELECT * FROM utilisateur WHERE login=? AND password=? AND statut='ACTIF'";

        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new Utilisateur(
                    rs.getInt("id"),
                    rs.getString("login"),
                    rs.getString("password"),
                    rs.getString("statut"),
                    new Role(rs.getString("role")),
                    rs.getString("numAdherent")
                );
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public String getEmailAdherent(String numAdherent) {
        String sql = "SELECT email FROM adherents WHERE numAdherent=?";
        try (PreparedStatement ps = DatabaseConnection.getConnection().prepareStatement(sql)) {
            ps.setString(1, numAdherent);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
