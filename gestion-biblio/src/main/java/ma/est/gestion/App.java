package ma.est.gestion;

import javax.swing.SwingUtilities;

import ma.est.gestion.view.LoginFrame;

public class App {

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            LoginFrame login = new LoginFrame();
            login.setLocationRelativeTo(null);
            login.setVisible(true);
        });
    }
}