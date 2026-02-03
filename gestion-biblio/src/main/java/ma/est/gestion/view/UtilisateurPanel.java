package ma.est.gestion.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import ma.est.gestion.dao.LivreDao;
import ma.est.gestion.dao.impl.EmpruntDaoImpl;
import ma.est.gestion.model.Emprunt;
import ma.est.gestion.util.DatabaseConnection;

public class UtilisateurPanel extends JFrame {

    private final Connection connection = DatabaseConnection.getConnection();

    private final JTable tableLivres;
    private final EmpruntDaoImpl empruntDao;
    private int exemplairesSelectionnes;
    private String codeLivreSelectionne;
    private String titreLivreSelectionne;
    
    // Variables pour stocker les informations de l'utilisateur connecté
    private String loginUtilisateur;
    private String emailUtilisateur;
    private int numAdherentUtilisateur;
    
    private final JLabel lblTitre = new JLabel("Espace Adhérent - Livres disponibles");
    private JButton btnEmprunter = new JButton("Emprunter");
    private JButton btnRetour = new JButton("Retour");
    private JButton btnMyEmprunts = new JButton("Mes Emprunts");

    // Constructeur avec informations utilisateur
    public UtilisateurPanel(String login, String email, int numAdherent) {
        this.loginUtilisateur = login;
        this.emailUtilisateur = email;
        this.numAdherentUtilisateur = numAdherent;
        
        empruntDao = new EmpruntDaoImpl();
        
        setTitle("Espace Adhérent - " + login);
        setSize(1000, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Appliquer le style glassmorphisme
        getContentPane().setBackground(new Color(240, 245, 255));

        //HEADER
        JPanel header = createGlassPanel();
        header.setLayout(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        lblTitre.setText("Espace Adhérent - " + login + " - Livres disponibles");
        lblTitre.setForeground(new Color(60, 80, 120));
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actions.setOpaque(false);

        btnMyEmprunts = createGlassButton("Mes Emprunts", new Color(52, 152, 219));
        btnEmprunter = createGlassButton("Emprunter", new Color(46, 204, 113));
        btnRetour = createGlassButton("Retour", new Color(149, 165, 166));

        actions.add(btnMyEmprunts);
        actions.add(btnEmprunter);
        actions.add(btnRetour);

        header.add(lblTitre, BorderLayout.WEST);
        header.add(actions, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);

        // PANEL PRINCIPAL POUR LES LIVRES
        JPanel mainPanel = createGlassPanel();
        mainPanel.setLayout(new BorderLayout());
        
        // Créer la table pour afficher les livres
        tableLivres = createBooksTable();
        
        // Charger les livres depuis la base
        loadBooks();
        
        // Ajouter la table au panneau principal
        JScrollPane scrollPane = new JScrollPane(tableLivres);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);

        // ACTIONS
        btnMyEmprunts.addActionListener(e -> {
            dispose();
            new EmpruntActivePanel(this.loginUtilisateur, this.emailUtilisateur, this.numAdherentUtilisateur).setVisible(true);
        });

        btnEmprunter.addActionListener(e -> emprunterLivre());

        btnRetour.addActionListener(e -> {
            dispose();
            new LoginFrame().setVisible(true);
        });
    }

    // Créer la table pour afficher les livres
    private JTable createBooksTable() {
        String[] columnNames = {"ID", "Code", "Titre", "Auteur", "Exemplaires", "Catégorie"};
        Object[][] data = {}; // Données vides initialement
        
        JTable table = new JTable(data, columnNames);
        table.setRowHeight(25);
        table.getTableHeader().setBackground(new Color(70, 130, 180, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setGridColor(new Color(220, 220, 220));
        
        return table;
    }

    // Charger les livres depuis la base de données
    private void loadBooks() {
        try {
            LivreDao livreDao = new LivreDao();
            var livres = livreDao.getAll();
            
            // Créer un modèle de table
            String[] columnNames = {"ID", "Code", "Titre", "Auteur", "Exemplaires", "Catégorie"};
            javax.swing.table.DefaultTableModel model = new javax.swing.table.DefaultTableModel(columnNames, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false; // Rendre la table non éditable
                }
            };
            
            for (var livre : livres) {
                model.addRow(new Object[]{
                    livre.getId(),
                    livre.getCode(),
                    livre.getTitre(),
                    livre.getAuteur(),
                    livre.getNombreExemplaire(),
                    livre.getCategorie() != null ? livre.getCategorie().toString() : ""
                });
            }
            
            tableLivres.setModel(model);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors du chargement des livres: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // STYLE glassmorphisme
    private JPanel createGlassPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                Color background = new Color(255, 255, 255, 180);
                Color border = new Color(255, 255, 255, 200);
                
                g2d.setColor(background);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.setColor(border);
                g2d.setStroke(new BasicStroke(1.5f));
                g2d.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                
                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    private JButton createGlassButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isPressed()) {
                    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 200));
                } else if (getModel().isRollover()) {
                    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 180));
                } else {
                    g2d.setColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 150));
                }
                
                g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2d.dispose();
                
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 13));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    // MÉTHODE EMPRUNTER fonctionnelle
    private void emprunterLivre() {
        int selectedRow = tableLivres.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Veuillez sélectionner un livre à emprunter",
                    "Aucun livre sélectionné",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Récupérer les informations du livre sélectionné
        codeLivreSelectionne = tableLivres.getValueAt(selectedRow, 1).toString();
        titreLivreSelectionne = tableLivres.getValueAt(selectedRow, 2).toString();
        
        try {
            Object exemplairesObj = tableLivres.getValueAt(selectedRow, 4);
            if (exemplairesObj != null) {
                exemplairesSelectionnes = Integer.parseInt(exemplairesObj.toString());
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Nombre d'exemplaires invalide",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Vérifier la disponibilité
        if (exemplairesSelectionnes <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Ce livre n'est plus disponible",
                    "Indisponible",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Demander confirmation avec les informations automatiques
        int confirmation = JOptionPane.showConfirmDialog(
            this,
            """
            Confirmer l'emprunt du livre :
            \u2022 Titre : """ + titreLivreSelectionne + "\n" +
            "• Code : " + codeLivreSelectionne + "\n" +
            "• Votre numéro adhérent : " + numAdherentUtilisateur + "\n" +
            "• Votre login : " + loginUtilisateur + "\n" +
            "• Votre email : " + emailUtilisateur + "\n\n" +
            "L'emprunt sera enregistré avec vos informations.",
            "Confirmation d'emprunt",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );

        if (confirmation == JOptionPane.YES_OPTION) {
            validerEmprunt(selectedRow);
        }
    }

    @SuppressWarnings("UseSpecificCatch")
    private void validerEmprunt(int selectedRow) {
        try {
            // Dates
            Date dateEmprunt = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateEmprunt);
            cal.add(Calendar.DAY_OF_MONTH, 14);
            Date dateRetour = cal.getTime();
            
            // Format de date pour l'affichage
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            // Générer code emprunt
            String codeEmprunt = "EMP-" + codeLivreSelectionne + "-" + System.currentTimeMillis();
            
            // Récupérer le nom et prénom depuis la base (ou utiliser le login par défaut)
            String nomAdherent = "Utilisateur"; // Par défaut
            @SuppressWarnings("unused")
            String prenomAdherent = loginUtilisateur; // Utiliser le login comme prénom
            
            // Si vous avez une table adhérents avec les informations, vous pourriez les récupérer ici
            
            // Créer l'objet Emprunt
            Emprunt emprunt = new Emprunt();
            emprunt.setCodeEmprunt(codeEmprunt);
            emprunt.setNumAdherent(numAdherentUtilisateur);
            emprunt.setNomAdherent(nomAdherent);
            emprunt.setEmailAdherent(emailUtilisateur);
            emprunt.setDateEmprunt(dateEmprunt);
            emprunt.setDateRetour(dateRetour);
            emprunt.setStatut("Actif");
            emprunt.setCodeLivre(codeLivreSelectionne);
            
            try {
                // Enregistrer l'emprunt dans la base de données
                empruntDao.addEmprunt(emprunt);
                
                // Mettre à jour le nombre d'exemplaires dans la base de données
                updateBookStock(codeLivreSelectionne, exemplairesSelectionnes - 1);
                
                JOptionPane.showMessageDialog(this,
                    "✅ Emprunt enregistré dans la base de données !",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de l'enregistrement: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // Afficher confirmation
            JOptionPane.showMessageDialog(
                    this,
                    """
                    \u2705 Emprunt valid\u00e9 avec succ\u00e8s !
                    
                    \ud83d\udccb D\u00e9tails de l'emprunt :
                    \u2022 Livre : """ + titreLivreSelectionne + "\n" +
                    "• Code emprunt : " + codeEmprunt + "\n" +
                    "• Votre numéro adhérent : " + numAdherentUtilisateur + "\n" +
                    "• Votre login : " + loginUtilisateur + "\n" +
                    "• Votre email : " + emailUtilisateur + "\n" +
                    "• Date emprunt : " + sdf.format(dateEmprunt) + "\n" +
                    "• Date retour : " + sdf.format(dateRetour) + "\n" +
                    "• Statut : Actif\n\n" +
                    "📚 Pensez à rapporter le livre avant la date de retour !",
                    "Emprunt validé",
                    JOptionPane.INFORMATION_MESSAGE
            );
            
            // Mettre à jour le nombre d'exemplaires dans la table
            int newExemplaires = exemplairesSelectionnes - 1;
            tableLivres.setValueAt(newExemplaires, selectedRow, 4);
            
            // Recharger les livres pour refléter les changements
            loadBooks();
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de la validation de l'emprunt: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Méthode pour mettre à jour le stock des livres dans la base de données
    @SuppressWarnings({"CallToPrintStackTrace", "UseSpecificCatch"})
    private void updateBookStock(String codeLivre, int nouveauStock) {

        PreparedStatement statement = null;
        
        try {
            
            // Requête SQL pour mettre à jour le stock
            String sql = "UPDATE livre SET nombreExemplaire = ? WHERE code = ?";
            statement = connection.prepareStatement(sql);
            statement.setInt(1, nouveauStock);
            statement.setString(2, codeLivre);
            
            int rowsUpdated = statement.executeUpdate();
            
            if (rowsUpdated > 0) {
                System.out.println("Stock du livre " + codeLivre + " mis à jour à " + nouveauStock);
            } else {
                System.out.println("Aucun livre trouvé avec le code: " + codeLivre);
            }
            
        } catch (Exception e) {
            System.err.println("Erreur lors de la mise à jour du stock: " + e.getMessage());
            e.printStackTrace();
        }
    }
}