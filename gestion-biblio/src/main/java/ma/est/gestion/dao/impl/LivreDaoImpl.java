package ma.est.gestion.dao.impl;

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

public class LivreDaoImpl {

    @SuppressWarnings("CallToPrintStackTrace")
    public List<Livre> getAll() {
        List<Livre> livres = new ArrayList<>();
        String sql = "SELECT * FROM livre";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Livre l = new Livre();
                l.setId(rs.getInt("id"));
                l.setCode(rs.getString("code"));
                l.setTitre(rs.getString("titre"));
                l.setAuteur(rs.getString("auteur"));
                l.setNombreExemplaire(rs.getInt("nombreExemplaire"));
                l.setCategorie(new Categorie(rs.getString("categorie")));
                livres.add(l);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return livres;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public Livre findByCode(String code) {
        String sql = "SELECT * FROM livre WHERE code = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, code);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Livre l = new Livre();
                l.setId(rs.getInt("id"));
                l.setCode(rs.getString("code"));
                l.setTitre(rs.getString("titre"));
                l.setAuteur(rs.getString("auteur"));
                l.setNombreExemplaire(rs.getInt("nombreExemplaire"));
                l.setCategorie(new Categorie(rs.getString("categorie")));
                return l;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public void addLivre(Livre livre) {
        String sql = "INSERT INTO livre (code, titre, auteur, nombreExemplaire, categorie) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, livre.getCode());
            stmt.setString(2, livre.getTitre());
            stmt.setString(3, livre.getAuteur());
            stmt.setInt(4, livre.getNombreExemplaire());
            stmt.setString(5, livre.getCategorie() != null ? livre.getCategorie().getNom() : "");
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
