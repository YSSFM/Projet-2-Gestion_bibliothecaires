package ma.est.gestion_biblio;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import java.sql.Connection;
import java.sql.SQLException;
import org.junit.jupiter.api.Test;
import ma.est.gestion.util.DatabaseConnection;

public class AppTest {

    @Test
    public void testConnexionBase() {
        try {
            DatabaseConnection db = DatabaseConnection.getInstance();
            Connection con = db.getConnection();

            assertNotNull(con, "La connexion ne doit pas être nulle");

            if (con != null && !con.isClosed()) {
                System.out.println("✔ Connexion réussie à la base de données !");
            } else {
                System.out.println("✘ La connexion a échoué !");
            }

        } catch (SQLException e) {
            System.out.println("✘ Erreur lors de la connexion à la base : " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.out.println("✘ Erreur inattendue : " + e.getMessage());
            e.printStackTrace();
        }
    }
}

