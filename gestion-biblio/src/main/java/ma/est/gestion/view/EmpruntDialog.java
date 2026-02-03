package ma.est.gestion.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import ma.est.gestion.model.Livre;

public class EmpruntDialog extends JDialog {
    
    private boolean validated = false;
    private String codeEmprunt;
    private Date dateRetour;
    
    public EmpruntDialog(Frame parent, Livre livre) {
        super(parent, "Valider l'emprunt", true);
        initUI(livre);
    }
    
    private void initUI(Livre livre) {
        setLayout(new BorderLayout());
        setSize(400, 300);
        setLocationRelativeTo(getParent());
        
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Titre
        gbc.gridwidth = 2;
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel titleLabel = new JLabel("Emprunt du livre: " + livre.getTitre());
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(titleLabel, gbc);
        
        // Numéro adhérent
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        mainPanel.add(new JLabel("Numéro adhérent:"), gbc);
        
        JTextField tfNumAdherent = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(tfNumAdherent, gbc);
        
        // Nom adhérent
        gbc.gridx = 0;
        gbc.gridy = 2;
        mainPanel.add(new JLabel("Nom:"), gbc);
        
        JTextField tfNom = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(tfNom, gbc);
        
        // Prénom adhérent
        gbc.gridx = 0;
        gbc.gridy = 3;
        mainPanel.add(new JLabel("Prénom:"), gbc);
        
        JTextField tfPrenom = new JTextField(15);
        gbc.gridx = 1;
        mainPanel.add(tfPrenom, gbc);
        
        // Boutons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        
        JButton btnValider = new JButton("Valider");
        btnValider.addActionListener(e -> {
            if (validerEmprunt(tfNumAdherent.getText(), tfNom.getText(), tfPrenom.getText(), livre)) {
                validated = true;
                dispose();
            }
        });
        
        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.addActionListener(e -> dispose());
        
        buttonPanel.add(btnValider);
        buttonPanel.add(btnAnnuler);
        
        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }
    
    private boolean validerEmprunt(String numAdherentStr, String nom, String prenom, Livre livre) {
        if (numAdherentStr.isEmpty() || nom.isEmpty() || prenom.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Tous les champs sont obligatoires",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
        
        try {
            @SuppressWarnings("unused")
            int numAdherent = Integer.parseInt(numAdherentStr);
            
            // Créer l'emprunt
            Date dateEmprunt = new Date();
            Date dateRetour = new Date(dateEmprunt.getTime() + (14 * 24 * 60 * 60 * 1000L)); // +14 jours
            
            this.dateRetour = dateRetour;
            this.codeEmprunt = "EMP-" + livre.getCode() + "-" + System.currentTimeMillis();
            
            JOptionPane.showMessageDialog(this,
                """
                Emprunt valid\u00e9 !
                Code: """ + codeEmprunt + "\n" +
                "Date retour: " + dateRetour,
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
            
            return true;
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Numéro d'adhérent invalide",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    public boolean isValidated() {
        return validated;
    }
    
    public String getCodeEmprunt() {
        return codeEmprunt;
    }
    
    public Date getDateRetour() {
        return dateRetour;
    }
}