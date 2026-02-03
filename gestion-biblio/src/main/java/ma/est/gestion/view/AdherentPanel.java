package ma.est.gestion.view;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import ma.est.gestion.controller.AdherentController;
import ma.est.gestion.dao.AdherentDao;
import ma.est.gestion.model.Adherent;
import ma.est.gestion.util.DatabaseConnection;

public class AdherentPanel extends JFrame {

    JTextField txtNumAdherent, txtNomAdherent, txtPrenomAdherent, txtEmailAdherent;

    public JButton btnAddAdherent = new JButton("Ajouter");
    public JButton btnUpdateAdherent = new JButton("Modifier");
    public JButton btnDeleteAdherent = new JButton("Supprimer");
    public JButton btnBlockAdherent = new JButton("Bloquer/Débloquer");
    public JButton btnRetour = new JButton("Retour");

    private final DefaultTableModel model;
    private JTable table;

    JPanel panel_general, panelAdherentForm, panelSearch, panelTop;

    private final AdherentDao dao;
    @SuppressWarnings("unused")
    private final AdherentController controller;

    private final Connection connection = DatabaseConnection.getConnection();

    public AdherentPanel() {

        setTitle("Gestion des Adhérents");
        setSize(1450, 750);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Appliquer le style glassmorphisme
        getContentPane().setBackground(new Color(240, 245, 255));

        setLayout(new BorderLayout());

        // En-tête avec effet glass
        JPanel headerPanel = createGlassPanel();
        headerPanel.setLayout(new BorderLayout());
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        
        JLabel header = new JLabel("Gestion des Adhérents", SwingConstants.LEFT);
        header.setFont(new Font("Segoe UI", Font.BOLD, 28));
        header.setForeground(new Color(60, 80, 120));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setOpaque(false);
        
        btnRetour = createGlassButton("Retour", new Color(149, 165, 166));

        btnRetour.addActionListener(e -> {
            java.awt.Window win = javax.swing.SwingUtilities.getWindowAncestor((java.awt.Component)e.getSource());
            if (win != null) win.dispose();
            new MainFrame().setVisible(true);
        });
        
        buttonPanel.add(btnRetour);
        
        headerPanel.add(header, BorderLayout.WEST);
        headerPanel.add(buttonPanel, BorderLayout.EAST);
        add(headerPanel, BorderLayout.NORTH);

        // Champs
        txtNumAdherent = new JTextField();
        txtNumAdherent.setVisible(false);

        txtNomAdherent = createGlassField();
        txtPrenomAdherent = createGlassField();
        txtEmailAdherent = createGlassField();

        // Tableau
        model = new DefaultTableModel(new Object[]{"Numéro", "Nom", "Prénom", "Email", "Statut"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre la table non éditable
            }
        };
        table = new JTable(model);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        table.setRowHeight(26);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(new Color(60, 80, 120));
        table.setBackground(Color.WHITE);
        table.setGridColor(new Color(220, 220, 220));

        table.getTableHeader().setBackground(new Color(70, 130, 180, 180));
        table.getTableHeader().setForeground(Color.WHITE);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(table);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(new LineBorder(new Color(200, 200, 200, 150)));

        // DAO et Controller
        dao = new AdherentDao();
        controller = new AdherentController(dao, this);
        chargerAdherents();

        // FORMULAIRE EN HAUT SUR UNE LIGNE avec effet glass
        panelAdherentForm = createGlassPanel();
        panelAdherentForm.setLayout(new FlowLayout(FlowLayout.LEFT, 20, 10));
        panelAdherentForm.setBorder(new EmptyBorder(10, 20, 10, 20));

        panelAdherentForm.add(createLabel("Nom :"));
        panelAdherentForm.add(txtNomAdherent);
        txtNomAdherent.setPreferredSize(new Dimension(100, 25));

        panelAdherentForm.add(createLabel("Prénom :"));
        panelAdherentForm.add(txtPrenomAdherent);
        txtPrenomAdherent.setPreferredSize(new Dimension(100, 25));

        panelAdherentForm.add(createLabel("Email :"));
        panelAdherentForm.add(txtEmailAdherent);
        txtEmailAdherent.setPreferredSize(new Dimension(250, 25));

        // Boutons sur la même ligne
        styleButton(btnAddAdherent, new Color(36, 104, 99));
        styleButton(btnUpdateAdherent, new Color(100, 95, 15));
        styleButton(btnDeleteAdherent, new Color(231, 76, 60));
        styleButton(btnBlockAdherent, new Color(100, 70, 100));

        panelAdherentForm.add(btnAddAdherent);
        panelAdherentForm.add(btnUpdateAdherent);
        panelAdherentForm.add(btnDeleteAdherent);
        panelAdherentForm.add(btnBlockAdherent);

        // BARRE DE RECHERCHE DÉTACHÉE avec effet glass
        panelSearch = createGlassPanel();
        panelSearch.setLayout(new FlowLayout(FlowLayout.CENTER));
        panelSearch.setBorder(new EmptyBorder(10, 10, 10, 10));

        JTextField txtSearch = createGlassField();
        txtSearch.setPreferredSize(new Dimension(200, 35));

        JLabel searchLabel = new JLabel("🔍 Rechercher : ");
        searchLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        searchLabel.setForeground(new Color(60, 80, 120));

        panelSearch.add(searchLabel);
        panelSearch.add(txtSearch);

        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }

            private void filter() {
                String text = txtSearch.getText().toLowerCase();
                sorter.setRowFilter(text.trim().isEmpty() ? null : RowFilter.regexFilter("(?i)" + text));
            }
        });

        // Panel haut = formulaire + recherche
        panelTop = new JPanel(new BorderLayout());
        panelTop.setOpaque(false);
        panelTop.add(panelAdherentForm, BorderLayout.NORTH);
        panelTop.add(panelSearch, BorderLayout.SOUTH);

        // Panel général
        panel_general = new JPanel(new BorderLayout());
        panel_general.setOpaque(false);
        panel_general.add(panelTop, BorderLayout.NORTH);
        panel_general.add(scroll, BorderLayout.CENTER);

        add(panel_general, BorderLayout.CENTER);
        
        // Ajouter les actions pour les boutons
        btnDeleteAdherent.addActionListener(e -> supprimerAdherent());
        btnBlockAdherent.addActionListener(e -> bloquerAdherent());
        
        // Ajouter la sélection dans la table
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0) {
                    int id = (int) table.getValueAt(row, 0);
                    String nom = table.getValueAt(row, 1).toString();
                    String prenom = table.getValueAt(row, 2).toString();
                    String email = table.getValueAt(row, 3).toString();
                    setForm(id, nom, prenom, email);
                }
            }
        });
    }

    // Méthode pour supprimer un adhérent
    @SuppressWarnings("UseSpecificCatch")
    private void supprimerAdherent() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un adhérent à supprimer",
                "Aucune sélection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = Integer.parseInt(table.getValueAt(selectedRow, 0).toString());
        String nom = table.getValueAt(selectedRow, 1).toString();
        String prenom = table.getValueAt(selectedRow, 2).toString();
        
        int choix = JOptionPane.showConfirmDialog(
            this,
            "Voulez-vous vraiment supprimer l'adhérent " + nom + " " + prenom + " ?\nCette action est irréversible.",
            "Confirmation de suppression",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (choix == JOptionPane.YES_OPTION) {
            try {
                // Supprimer de la base de données
                dao.supprimer(id);
                
                // Supprimer de la table
                model.removeRow(selectedRow);
                
                // Réinitialiser les champs
                txtNumAdherent.setText("");
                txtNomAdherent.setText("");
                txtPrenomAdherent.setText("");
                txtEmailAdherent.setText("");
                
                JOptionPane.showMessageDialog(this,
                    "Adhérent supprimé avec succès",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                    
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this,
                    "Erreur lors de la suppression de l'adhérent: " + e.getMessage(),
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // Méthode pour bloquer un adhérent
    @SuppressWarnings({"CallToPrintStackTrace", "UseSpecificCatch"})
    private void bloquerAdherent() {
        int selectedRow = table.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                "Veuillez sélectionner un adhérent à bloquer",
                "Aucune sélection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String sql = "SELECT statut FROM utilisateur WHERE numAdherent = ?";

        try(PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, Integer.parseInt(table.getValueAt(selectedRow, 0).toString()));
            // Exécuter la requête et obtenir le résultat si nécessaire
            ResultSet rs = ps.executeQuery();

            if(rs.next()) {
                String statut = rs.getString("statut");
                System.out.println("Statut actuel: " + statut);

                if(statut.equals("Inactif")) {
                    System.out.println("L'adhérent est déjà bloqué.");
                    String updateSql = "UPDATE utilisateur SET statut = 'Actif' WHERE numAdherent = ?";
                    
                try(PreparedStatement updatePs = connection.prepareStatement(updateSql)) {
                    updatePs.setInt(1, Integer.parseInt(table.getValueAt(selectedRow, 0).toString()));
                    

                String nom = table.getValueAt(selectedRow, 1).toString();
                String prenom = table.getValueAt(selectedRow, 2).toString();

                String message = "Voulez-vous vraiment débloquer l'adhérent : " + nom + " " + prenom + " ?";
        
                int choix = JOptionPane.showConfirmDialog(
                    this,
                    message,
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
        
                if (choix == JOptionPane.YES_OPTION) {
                        updatePs.executeUpdate();
                        System.out.println("L'adhérent a été débloqué.");
                    try {
                        // Mettre à jour le statut dans la table
                        String stl = "Select statut from utilisateur where numAdherent = ?";
                        PreparedStatement pst = connection.prepareStatement(stl);
                        pst.setInt(1, Integer.parseInt(table.getValueAt(selectedRow, 0).toString()));
                        ResultSet rs2 = pst.executeQuery();
                        if (rs2.next()) {
                            statut = rs2.getString("statut");
                            table.setValueAt(statut, selectedRow, 4);
                        }

                    JOptionPane.showMessageDialog(this,
                        "Statut de l'adhérent mis à jour: " + statut,
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this,
                        "Erreur lors du changement de statut: " + e.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    }
                }
                        
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                String updateSql = "UPDATE utilisateur SET statut = 'Inactif' WHERE numAdherent = ?";
                
                try(PreparedStatement updatePs = connection.prepareStatement(updateSql)) {
                    updatePs.setInt(1, Integer.parseInt(table.getValueAt(selectedRow, 0).toString()));
                    
                String nom = table.getValueAt(selectedRow, 1).toString();
                String prenom = table.getValueAt(selectedRow, 2).toString();

                String message = "Voulez-vous vraiment bloquer l'adhérent : " + nom + " " + prenom + " ?";
        
                int choix = JOptionPane.showConfirmDialog(
                    this,
                    message,
                    "Confirmation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.WARNING_MESSAGE
                );
        
                if (choix == JOptionPane.YES_OPTION) {

                        updatePs.executeUpdate();
                        System.out.println("L'adhérent a été bloqué.");
                        
                    try {
                        // Mettre à jour le statut dans la table
                        String stl = "Select statut from utilisateur where numAdherent = ?";
                        PreparedStatement pst = connection.prepareStatement(stl);
                        pst.setInt(1, Integer.parseInt(table.getValueAt(selectedRow, 0).toString()));
                        ResultSet rs2 = pst.executeQuery();
                        if (rs2.next()) {
                            statut = rs2.getString("statut");
                            table.setValueAt(statut, selectedRow, 4);
                        }

                    JOptionPane.showMessageDialog(this,
                        "Statut de l'adhérent mis à jour: " + statut,
                        "Succès",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(this,
                        "Erreur lors du changement de statut: " + e.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                    }
                }
        
                } catch (Exception e) {
                    e.printStackTrace();
                }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Méthodes utilitaires avec style glassmorphisme
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

    private JTextField createGlassField() {
        JTextField f = new JTextField();
        f.setBackground(new Color(255, 255, 255, 150));
        f.setForeground(new Color(60, 80, 120));
        f.setCaretColor(new Color(60, 80, 120));
        f.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(new Color(200, 200, 220, 200), 1, true),
                new EmptyBorder(6, 10, 6, 10)
        ));
        f.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return f;
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
        
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(6, 18, 6, 18));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private JLabel createLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(60, 80, 120));
        l.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        return l;
    }

    private void styleButton(JButton btn, Color color) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(6, 18, 6, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Effet glass
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) { 
                btn.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 180));
            }
            @Override
            public void mouseExited(MouseEvent e) { 
                btn.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 150));
            }
        });
        
        btn.setBackground(new Color(color.getRed(), color.getGreen(), color.getBlue(), 150));
    }

    // Chargement automatique
    private void chargerAdherents() {
        model.setRowCount(0);
        List<Adherent> adherents = dao.getAll();

        for (Adherent a : adherents) {
            model.addRow(new Object[]{
                    a.getNumAdherent(),
                    a.getNomAdherent(),
                    a.getPrenomAdherent(),
                    a.getEmailAdherent(),
                    "Actif" // Statut par défaut
            });
        }
    }

    public void refreshAdherentTable(List<Adherent> adherents) {
        model.setRowCount(0);
        for (Adherent a : adherents) {
            model.addRow(new Object[]{
                    a.getNumAdherent(),
                    a.getNomAdherent(),
                    a.getPrenomAdherent(),
                    a.getEmailAdherent(),
                    "Actif"
            });
        }
    }

    public void setForm(int id, String nom, String prenom, String email) {
        txtNumAdherent.setText(String.valueOf(id));
        txtNomAdherent.setText(nom);
        txtPrenomAdherent.setText(prenom);
        txtEmailAdherent.setText(email);
    }

    public JButton getBtnAddAdherent() { return btnAddAdherent; }
    public JButton getBtnUpdateAdherent() { return btnUpdateAdherent; }
    public JButton getBtnDeleteAdherent() { return btnDeleteAdherent; }
    public JButton getBtnBlockAdherent() { return btnBlockAdherent; }
    public JButton getBtnRetour() { return btnRetour; }

    public JTable getAdherentTable() { return table; }

    public String getNomAdherent() { return txtNomAdherent.getText(); }
    public String getPrenomAdherent() { return txtPrenomAdherent.getText(); }
    public String getEmailAdherent() { return txtEmailAdherent.getText(); }
}