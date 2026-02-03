package ma.est.gestion.view;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

import javax.swing.*;
import javax.swing.border.*;

import ma.est.gestion.dao.impl.UtilisateurDaoImpl;
import ma.est.gestion.model.Utilisateur;
import ma.est.gestion.util.DatabaseConnection;

public class LoginFrame extends JFrame {

    private JTextField tfLogin;
    private JPasswordField pfPassword;
    private JTextField tfEmail;
    private JPanel loginPanel;
    private JPanel registerPanel;
    private CardLayout cardLayout;
    private JPanel cardPanel;
    private UtilisateurDaoImpl userDao;
    private JTextField tfRegisterNom;
    private JTextField tfRegisterPrenom;

    public LoginFrame() {

        DatabaseConnection.getConnection();

        userDao = new UtilisateurDaoImpl();

        setTitle("Gestion Bibliothèque - Connexion");
        setSize(850, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Fond global avec dégradé léger pour effet glassmorphisme
        getContentPane().setBackground(new Color(225, 235, 250, 230));

        add(header(), BorderLayout.NORTH);
        add(mainContent(), BorderLayout.CENTER);
    }

    private JPanel header() {
        JPanel h = new JPanel();
        h.setOpaque(false);
        JLabel l = new JLabel("Bienvenue dans la Bibliothèque");
        l.setFont(new Font("Segoe UI", Font.BOLD, 28));
        l.setForeground(new Color(20, 50, 130));
        h.add(l);
        h.setBorder(BorderFactory.createEmptyBorder(25, 0, 15, 0));
        return h;
    }

    private JPanel mainContent() {
        JPanel main = new JPanel(new BorderLayout());
        main.setOpaque(false);
        main.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);
        cardPanel.setOpaque(false);

        loginPanel = createLoginPanel();
        registerPanel = createRegisterPanel();

        cardPanel.add(loginPanel, "LOGIN");
        cardPanel.add(registerPanel, "REGISTER");

        JPanel switchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        switchPanel.setOpaque(false);

        JLabel switchLabel = new JLabel("Pas encore de compte ? ");
        switchLabel.setForeground(new Color(60, 80, 120));
        switchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JButton switchButton = new JButton("Créer un compte");
        switchButton.setBorderPainted(false);
        switchButton.setContentAreaFilled(false);
        switchButton.setForeground(new Color(0, 120, 220));
        switchButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        switchButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        switchButton.addActionListener(e -> cardLayout.show(cardPanel, "REGISTER"));

        switchPanel.add(switchLabel);
        switchPanel.add(switchButton);

        main.add(cardPanel, BorderLayout.CENTER);
        main.add(switchPanel, BorderLayout.SOUTH);

        return main;
    }

    private JPanel createLoginPanel() {
        JPanel panel = createGlassPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Connexion");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(20, 50, 130));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(title, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        panel.add(createLabel("Login :"), gbc);

        tfLogin = createGlassTextField();
        gbc.gridx = 1;
        panel.add(tfLogin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(createLabel("Mot de passe :"), gbc);

        pfPassword = new JPasswordField();
        stylizeField(pfPassword);
        gbc.gridx = 1;
        panel.add(pfPassword, gbc);

        // Bouton pour afficher/cacher le mot de passe
        JButton showPwdBtn = new JButton("👁");
        showPwdBtn.setPreferredSize(new Dimension(45, 36));
        showPwdBtn.setBorder(BorderFactory.createEmptyBorder());
        showPwdBtn.setContentAreaFilled(false);
        showPwdBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        showPwdBtn.addActionListener(e -> {
            if (pfPassword.getEchoChar() != '\u0000') {
                pfPassword.setEchoChar((char) 0);
            } else {
                pfPassword.setEchoChar('•');
            }
        });
        JPanel pwdPanel = new JPanel(new BorderLayout());
        pwdPanel.setOpaque(false);
        pwdPanel.add(pfPassword, BorderLayout.CENTER);
        pwdPanel.add(showPwdBtn, BorderLayout.EAST);
        gbc.gridx = 1;
        panel.add(pwdPanel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(createLabel("Email :"), gbc);

        tfEmail = createGlassTextField();
        gbc.gridx = 1;
        panel.add(tfEmail, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton btnLogin = createGlassButton("Se connecter", new Color(46, 204, 113));
        btnLogin.addActionListener(e -> loginUser());

        JButton btnAdmin = createGlassButton("Admin", new Color(52, 152, 219));
        btnAdmin.addActionListener(e -> loginAdmin());

        JButton btnBack = createGlassButton("Réinitialiser", new Color(149, 165, 166));
        btnBack.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });

        buttonPanel.add(btnLogin);
        buttonPanel.add(btnAdmin);
        buttonPanel.add(btnBack);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private JPanel createRegisterPanel() {
        JPanel panel = createGlassPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Créer un compte");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(20, 50, 130));
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(title, gbc);

        JTextField tfRegisterLogin = createGlassTextField();
        JPasswordField pfRegisterPassword = new JPasswordField();
        JPasswordField pfConfirmPassword = new JPasswordField();
        JTextField tfRegisterEmail = createGlassTextField();
        tfRegisterNom = createGlassTextField();
        tfRegisterPrenom = createGlassTextField();

        stylizeField(pfRegisterPassword);
        stylizeField(pfConfirmPassword);

        // Login
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(createLabel("Login :"), gbc);

        gbc.gridx = 1;
        panel.add(tfRegisterLogin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(createLabel("Nom :"), gbc);

        gbc.gridx = 1;
        panel.add(tfRegisterNom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(createLabel("Prénom :"), gbc);

        gbc.gridx = 1;
        panel.add(tfRegisterPrenom, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        panel.add(createLabel("Mot de passe :"), gbc);

        gbc.gridx = 1;
        panel.add(pfRegisterPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        panel.add(createLabel("Confirmer :"), gbc);

        gbc.gridx = 1;
        panel.add(pfConfirmPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        panel.add(createLabel("Email :"), gbc);

        gbc.gridx = 1;
        panel.add(tfRegisterEmail, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);

        JButton btnRegister = createGlassButton("S'inscrire", new Color(155, 89, 182));
        btnRegister.addActionListener(e -> registerUser(
                tfRegisterLogin.getText(),
                tfRegisterNom.getText(),
                tfRegisterPrenom.getText(),
                new String(pfRegisterPassword.getPassword()),
                new String(pfConfirmPassword.getPassword()),
                tfRegisterEmail.getText()
        ));

        JButton btnBackToLogin = createGlassButton("Retour", new Color(149, 165, 166));
        btnBackToLogin.addActionListener(e -> cardLayout.show(cardPanel, "LOGIN"));

        buttonPanel.add(btnRegister);
        buttonPanel.add(btnBackToLogin);

        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 7;
        panel.add(buttonPanel, gbc);

        return panel;
    }

    private void loginUser() {
        String login = tfLogin.getText();
        String password = new String(pfPassword.getPassword());

        if (login.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez remplir tous les champs",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Utilisateur u = userDao.authentifier(login, password);

        if (u != null) {
            String email = userDao.getEmailAdherent(u.getNumAdherent());
            dispose();
            new UtilisateurPanel(u.getLogin(), email, Integer.parseInt(u.getNumAdherent())).setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this,
                    "Échec de la connexion. Vérifiez vos identifiants.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loginAdmin() {
        String login = tfLogin.getText();
        String pwd = new String(pfPassword.getPassword());

        Utilisateur u = userDao.authentifier(login, pwd);

        if ("admin".equals(login) && "admin".equals(pwd) ||
                (u != null && "ADMIN".equals(u.getRole().getNomRole()))) {

            dispose();
            new MainFrame().setVisible(true);

        } else {
            JOptionPane.showMessageDialog(this,
                    "Accès refusé. Vérifiez vos identifiants admin.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registerUser(String login, String nom, String prenom, String password, String confirmPassword, String email) {
        try {
            if (login.isEmpty() || nom.isEmpty() || prenom.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || email.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Tous les champs sont obligatoires",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this,
                        "Les mots de passe ne correspondent pas",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (!email.contains("@")) {
                JOptionPane.showMessageDialog(this,
                        "Email invalide",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String sqlAdherent = "INSERT INTO adherents (nom, prenom, email) VALUES (?, ?, ?)";
            String sqlUser = "INSERT INTO utilisateur (login, password, statut, role, numAdherent) VALUES (?, ?, ?, ?, ?)";

            Connection con = DatabaseConnection.getConnection();

            try {
                con.setAutoCommit(false);

                PreparedStatement psAdh = con.prepareStatement(
                        sqlAdherent,
                        PreparedStatement.RETURN_GENERATED_KEYS
                );

                psAdh.setString(1, nom);
                psAdh.setString(2, prenom);
                psAdh.setString(3, email);
                psAdh.executeUpdate();

                ResultSet rs = psAdh.getGeneratedKeys();
                if (!rs.next()) {
                    throw new SQLException("Impossible de récupérer l'id adherent");
                }

                int numAdherent = rs.getInt(1);

                PreparedStatement psUser = con.prepareStatement(sqlUser);
                psUser.setString(1, login);
                psUser.setString(2, password);
                psUser.setString(3, "ACTIF");
                psUser.setString(4, "USER");
                psUser.setInt(5, numAdherent);
                psUser.executeUpdate();

                con.commit();

            } catch (Exception e) {
                try {
                    con.rollback();
                } catch (SQLException ex) {}
                JOptionPane.showMessageDialog(this,
                        "Erreur lors de l'inscription : " + e.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                    "Erreur inattendue : " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        JOptionPane.showMessageDialog(this,
                "Compte créé avec succès!\nVous pouvez maintenant vous connecter.",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);

        cardLayout.show(cardPanel, "LOGIN");
        tfLogin.setText(login);
        tfEmail.setText(email);
        pfPassword.setText("");
    }

    // PANEL GLASSMORPHISME
    private JPanel createGlassPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int arc = 25;
                Color bg = new Color(255, 255, 255, 180);
                Color blueOverlay = new Color(100, 150, 255, 50);

                g2d.setColor(bg);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

                g2d.setColor(blueOverlay);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), arc, arc);

                g2d.setColor(new Color(255, 255, 255, 200));
                g2d.setStroke(new BasicStroke(2f));
                g2d.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, arc, arc);

                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        return panel;
    }

    private JTextField createGlassTextField() {
        JTextField field = new JTextField(20);
        stylizeField(field);
        return field;
    }

    private void stylizeField(JComponent field) {
        field.setPreferredSize(new Dimension(220, 36));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(180, 180, 220, 200), 1),
                BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        field.setOpaque(false);
        field.setBackground(new Color(255, 255, 255, 160));
        field.setForeground(new Color(30, 30, 60));
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        label.setForeground(new Color(20, 50, 130));
        return label;
    }

    private JButton createGlassButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color c = new Color(color.getRed(), color.getGreen(), color.getBlue(), 180);
                if (getModel().isPressed()) {
                    c = new Color(color.getRed(), color.getGreen(), color.getBlue(), 220);
                } else if (getModel().isRollover()) {
                    c = new Color(color.getRed(), color.getGreen(), color.getBlue(), 200);
                }

                g2d.setColor(c);
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2d.dispose();

                super.paintComponent(g);
            }
        };

        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 25, 10, 25));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        return button;
    }
}
