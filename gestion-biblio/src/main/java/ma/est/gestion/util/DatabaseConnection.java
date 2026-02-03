package ma.est.gestion.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DatabaseConnection {
	private static DatabaseConnection instance;
    private static final String URL =
            "jdbc:mysql://localhost:3306/gestion_bibliotheque?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

    private static final String USER = "root";
    private static final String PASSWORD = "";

    private static Connection connection;

    private DatabaseConnection() {}

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
            }
            return connection;
        } catch (SQLException e) {
            throw new RuntimeException("Impossible de se connecter à la base", e);
        }
    }

	public static DatabaseConnection getInstance() {
		  if (instance == null) {
	            instance = new DatabaseConnection();
	        }
	        return instance;
	}
}
