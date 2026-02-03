package ma.est.gestion.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import ma.est.gestion.dao.EmpruntDao;
import ma.est.gestion.model.Emprunt;

public class EmpruntDaoImpl implements EmpruntDao {
    
    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public void addEmprunt(Emprunt e) {
        if (e == null) throw new IllegalArgumentException("Emprunt null");

        String checkEx = "SELECT nombreExemplaire FROM livre WHERE code=?";
        String countActive = " SELECT COUNT(*) FROM emprunt WHERE numAdherent = ? AND statut = 'Actif'";
        String insert = "INSERT INTO emprunt (codeEmprunt, numAdherent, dateEmprunt, dateRetour, statut, codeLivre) VALUES (?, ?, ?, ?, ?, ?)";
        String updateLivre = "UPDATE livre SET nombreExemplaire = nombreExemplaire - 1 WHERE code=?";

        try (
            PreparedStatement psCount = connection.prepareStatement(countActive);
            PreparedStatement psCheck = connection.prepareStatement(checkEx);
            PreparedStatement psInsert = connection.prepareStatement(insert);
            PreparedStatement psUpdate = connection.prepareStatement(updateLivre)
        ) {

            psCount.setInt(1, e.getNumAdherent());
            ResultSet rsCount = psCount.executeQuery();

            if (rsCount.next() && rsCount.getInt(1) >= 3) {
                throw new IllegalStateException(
                    "Vous avez déjà 3 emprunts actifs. Veuillez retourner un livre."
                );
            }

            psCheck.setString(1, e.getCodeLivre());
            ResultSet rs = psCheck.executeQuery();

            if (!rs.next() || rs.getInt("nombreExemplaire") <= 0)
                throw new IllegalStateException("Aucun exemplaire disponible");

            psInsert.setString(1, e.getCodeEmprunt());
            psInsert.setInt(2, e.getNumAdherent());
            psInsert.setDate(3, new java.sql.Date(e.getDateEmprunt().getTime()));
            psInsert.setDate(4, new java.sql.Date(e.getDateRetour().getTime()));
            psInsert.setString(5, e.getStatut());
            psInsert.setString(6, e.getCodeLivre());
            psInsert.executeUpdate();

            psUpdate.setString(1, e.getCodeLivre());
            psUpdate.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Erreur lors de l'ajout de l'emprunt: " + ex.getMessage());
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public List<Emprunt> getAllEmprunts() {
        List<Emprunt> list = new ArrayList<>();

        String sql = "SELECT e.*, a.nom, a.prenom, a.email, l.titre " +
                    "FROM emprunt e " +
                    "LEFT JOIN adherents a ON e.numAdherent = a.numAdherent " +
                    "LEFT JOIN livre l ON e.codeLivre = l.code " +
                    "ORDER BY e.dateEmprunt DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Emprunt e = new Emprunt();
                e.setCodeEmprunt(rs.getString("codeEmprunt"));
                e.setNumAdherent(rs.getInt("numAdherent"));
                e.setNomAdherent(rs.getString("nom") + " " + rs.getString("prenom"));
                e.setEmailAdherent(rs.getString("email"));
                e.setDateEmprunt(rs.getDate("dateEmprunt"));
                e.setDateRetour(rs.getDate("dateRetour"));
                e.setStatut(rs.getString("statut"));
                e.setCodeLivre(rs.getString("codeLivre"));
                
                list.add(e);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException("Erreur lors de la récupération des emprunts: " + ex.getMessage());
        }
        return list;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public void updateEmprunt(Emprunt e, String statut) {
        String sql = "UPDATE emprunt SET statut=? WHERE codeEmprunt=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, statut);
            ps.setString(2, e.getCodeEmprunt());

            if (ps.executeUpdate() == 0)
                throw new NoSuchElementException("Emprunt introuvable");

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public void deleteEmprunt(Emprunt e) {
        String sql = "DELETE FROM emprunt WHERE codeEmprunt=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, e.getCodeEmprunt());
            
            // Récupérer le livre avant suppression pour mettre à jour le stock
            String getLivreSql = "SELECT codeLivre FROM emprunt WHERE codeEmprunt=?";
            try (PreparedStatement psLivre = connection.prepareStatement(getLivreSql)) {
                psLivre.setString(1, e.getCodeEmprunt());
                ResultSet rs = psLivre.executeQuery();
                if (rs.next()) {
                    String codeLivre = rs.getString("codeLivre");
                    // Incrémenter le stock
                    String updateStock = "UPDATE livre SET nombreExemplaire = nombreExemplaire + 1 WHERE code=?";
                    try (PreparedStatement psStock = connection.prepareStatement(updateStock)) {
                        psStock.setString(1, codeLivre);
                        psStock.executeUpdate();
                    }
                }
            }
            
            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")    
    @Override
    public Emprunt findEmpruntByCode(String code) {
        String sql = "SELECT e.*, a.nom, a.prenom, a.email, l.titre " +
                    "FROM emprunt e " +
                    "LEFT JOIN adherents a ON e.numAdherent = a.numAdherent " +
                    "LEFT JOIN livre l ON e.codeLivre = l.code " +
                    "WHERE e.codeEmprunt=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, code);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Emprunt e = new Emprunt();
                e.setCodeEmprunt(code);
                e.setNumAdherent(rs.getInt("numAdherent"));
                e.setNomAdherent(rs.getString("nom") + " " + rs.getString("prenom"));
                e.setEmailAdherent(rs.getString("email"));
                e.setDateEmprunt(rs.getDate("dateEmprunt"));
                e.setDateRetour(rs.getDate("dateRetour"));
                e.setStatut(rs.getString("statut"));
                e.setCodeLivre(rs.getString("codeLivre"));

                return e;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        throw new NoSuchElementException("Emprunt introuvable");
    }

    @SuppressWarnings("CallToPrintStackTrace")    
    @Override
    public List<Emprunt> getEmpruntsByAdherent(int numAdherent) {

        List<Emprunt> list = new ArrayList<>();

        String sql = " SELECT e.*, a.nom, a.prenom, a.email, l.titre " +
                " FROM emprunt e " +
                " LEFT JOIN adherents a ON e.numAdherent = a.numAdherent " +
                " LEFT JOIN livre l ON e.codeLivre = l.code " +
                " WHERE e.numAdherent = ? " +
                " ORDER BY e.dateEmprunt DESC ";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {

            ps.setInt(1, numAdherent);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Emprunt e = new Emprunt();

                e.setCodeEmprunt(rs.getString("codeEmprunt"));
                e.setNumAdherent(rs.getInt("numAdherent"));
                e.setNomAdherent(
                    rs.getString("nom") + " " + rs.getString("prenom")
                );
                e.setEmailAdherent(rs.getString("email"));
                e.setCodeLivre(rs.getString("codeLivre"));
                e.setDateEmprunt(rs.getDate("dateEmprunt"));
                e.setDateRetour(rs.getDate("dateRetour"));
                e.setStatut(rs.getString("statut"));

                list.add(e);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new RuntimeException(
                "Erreur lors de la récupération des emprunts de l'adhérent: " + ex.getMessage()
            );
        }

        return list;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public void modifierEmprunt(Emprunt e) {
        String sql = "UPDATE emprunt SET dateRetour=?, statut=? WHERE codeEmprunt=?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, new java.sql.Date(e.getDateRetour().getTime()));
            ps.setString(2, e.getStatut());
            ps.setString(3, e.getCodeEmprunt());

            ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public List<Emprunt> findEmpruntsByAdherent(int numAdherent) {
        List<Emprunt> list = new ArrayList<>();
        String sql = "SELECT * FROM emprunt WHERE numAdherent=? ORDER BY dateEmprunt DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, numAdherent);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Emprunt e = new Emprunt();
                e.setCodeEmprunt(rs.getString("codeEmprunt"));
                e.setNumAdherent(numAdherent);
                e.setCodeLivre(rs.getString("codeLivre"));
                e.setDateEmprunt(rs.getDate("dateEmprunt"));
                e.setDateRetour(rs.getDate("dateRetour"));
                e.setStatut(rs.getString("statut"));
                list.add(e);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    public List<Emprunt> findEmpruntsByStatut(String statut) {
        List<Emprunt> list = new ArrayList<>();
        String sql = "SELECT * FROM emprunt WHERE statut=? ORDER BY dateEmprunt DESC";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, statut);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Emprunt e = new Emprunt();
                e.setCodeEmprunt(rs.getString("codeEmprunt"));
                e.setNumAdherent(rs.getInt("numAdherent"));
                e.setCodeLivre(rs.getString("codeLivre"));
                e.setDateEmprunt(rs.getDate("dateEmprunt"));
                e.setDateRetour(rs.getDate("dateRetour"));
                e.setStatut(statut);
                list.add(e);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return list;
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public void cloturerEmprunt(Emprunt e) {
        String sql = "UPDATE emprunt SET statut='Retourne' WHERE codeEmprunt=?";
        
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setString(1, e.getCodeEmprunt());
            ps.executeUpdate();
            
            // Incrémenter le stock du livre
            String updateStock = "UPDATE livre SET nombreExemplaire = nombreExemplaire + 1 WHERE code=?";
            try (PreparedStatement psStock = connection.prepareStatement(updateStock)) {
                psStock.setString(1, e.getCodeLivre());
                psStock.executeUpdate();
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("CallToPrintStackTrace")
    @Override
    public int getEmpruntCount() {
        String sql = "SELECT COUNT(*) FROM emprunt";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next())
                return rs.getInt(1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public Connection getConnection() {
        return connection;
    }
}