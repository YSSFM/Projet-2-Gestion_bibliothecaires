package ma.est.gestion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import ma.est.gestion.model.Categorie;
import ma.est.gestion.model.Livre;
import ma.est.gestion.util.DatabaseConnection;

public class LivreDao {

    private final Connection connection = DatabaseConnection.getConnection();

    @SuppressWarnings("CallToPrintStackTrace")
    public void addLivre(Livre l) {
        String sql = "INSERT INTO livre (code, titre, auteur, nombreExemplaire, categorie) VALUES (?, ?, ?, ?, ?)";


        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setString(1, l.getCode());
            ps.setString(2, l.getTitre());
            ps.setString(3, l.getAuteur());
            ps.setInt(4, l.getNombreExemplaire());
            ps.setString(5, l.getCategorie().toString());

            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public List<Livre> getAll() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livre";

        try (Statement st = connection.createStatement();
            ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                livres.add(new Livre(
                        rs.getInt("id"),
                        rs.getString("code"),
                        rs.getString("titre"),
                        rs.getString("auteur"),
                        rs.getInt("nombreExemplaire"),
                        new Categorie(rs.getString("categorie"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return livres;
    }

    public void deleteLivre(int index) {
        // À améliorer plus tard (delete by id)
    }
}
