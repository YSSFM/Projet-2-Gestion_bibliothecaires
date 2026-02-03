package ma.est.gestion.controller;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JOptionPane;

import ma.est.gestion.dao.AdherentDao;
import ma.est.gestion.model.Adherent;
import ma.est.gestion.view.AdherentPanel;

public class AdherentController {
    private final AdherentDao dao;
    private final AdherentPanel panel;

    public AdherentController(AdherentDao dao, AdherentPanel panel) {
        this.dao = dao;
        this.panel = panel;

        initController();
    }

    private void initController() {
        panel.getBtnAddAdherent().addActionListener(e -> ajouter());
        panel.getBtnUpdateAdherent().addActionListener(e -> modifier());
        panel.getBtnDeleteAdherent().addActionListener(e -> supprimer());

        panel.getAdherentTable().addMouseListener(new MouseAdapter() {
    	    @Override
    	    public void mouseClicked(MouseEvent e) {
    	          int row = panel.getAdherentTable().getSelectedRow();
    	        if (row >= 0) {
    	            int id = (int) panel.getAdherentTable().getValueAt(row, 0);
    	            String nom = panel.getAdherentTable().getValueAt(row, 1).toString();
                    String prenom = panel.getAdherentTable().getValueAt(row, 2).toString();
                    String email = panel.getAdherentTable().getValueAt(row, 3).toString();
    	            panel.setForm(id, nom, prenom, email);
    	        }
    	    }
    	});

    
    }

private void ajouter() {
    try {
        String nom = panel.getNomAdherent();
        String prenom = panel.getPrenomAdherent();
        String email = panel.getEmailAdherent();

        // 1. Vérification des champs vides
        if (nom == null || nom.isEmpty() ||
            prenom == null || prenom.isEmpty() ||
            email == null || email.isEmpty()) {

            JOptionPane.showMessageDialog(null,
                "Tous les champs doivent être remplis.",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 2. Vérification du format email
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(null,
                "L'adresse email n'est pas valide.\nVeuillez entrer une adresse email correcte.\nExemple: utilisateur@domaine.com",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 3. Vérification de l'existence préalable (email unique)
        Adherent existant = dao.getByEmail(email);
        if (existant != null) {
            JOptionPane.showMessageDialog(null,
                "Un adhérent avec cet email existe déjà.",
                "Erreur", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // 4. Ajout si tout est OK
        Adherent adherent = new Adherent(0, email, nom, prenom);
        dao.ajouter(adherent);

        // 5. Rafraîchissement de la table
        List<Adherent> adherents = dao.getAll();
        panel.refreshAdherentTable(adherents);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null,
            "Erreur lors de l'ajout de l'adhérent.",
            "Erreur", JOptionPane.ERROR_MESSAGE);
    }
}

private void modifier() {
    try {
        // ✅ 1. Vérifier qu'une ligne est sélectionnée
        int selectedRow = panel.getAdherentTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(null,
                "Veuillez sélectionner un adhérent à modifier.",
                "Aucune sélection",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // ✅ 2. Récupération de l'ID depuis la table
        int id = Integer.parseInt(
            panel.getAdherentTable().getValueAt(selectedRow, 0).toString()
        );

        // ✅ 3. Récupération des champs
        String nom = panel.getNomAdherent();
        String prenom = panel.getPrenomAdherent();
        String email = panel.getEmailAdherent();

        // ✅ 4. Vérification des champs vides
        if (nom.isEmpty() || prenom.isEmpty() || email.isEmpty()) {
            JOptionPane.showMessageDialog(null,
                "Tous les champs doivent être remplis.",
                "Champs manquants",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ 5. Vérification du format email
        if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            JOptionPane.showMessageDialog(null,
                "L'adresse email n'est pas valide.",
                "Email incorrect",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // ✅ 6. Création de l'objet et modification
        Adherent adherent = new Adherent(id, email, nom, prenom);
        dao.modifier(adherent);

        // ✅ 7. Rafraîchir la table
        List<Adherent> adherents = dao.getAll();
        panel.refreshAdherentTable(adherents);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null,
            "Erreur lors de la modification.",
            "Erreur",
            JOptionPane.ERROR_MESSAGE);
    }
}

private void supprimer() {

    int selectedRow = panel.getAdherentTable().getSelectedRow();

    // ✅ Vérifier qu'une ligne est sélectionnée
    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(null,
            "Veuillez sélectionner un adhérent à supprimer.",
            "Aucune sélection",
            JOptionPane.WARNING_MESSAGE);
        return;
    }

    // ✅ Récupérer l'ID depuis la table
    int id = Integer.parseInt(
        panel.getAdherentTable().getValueAt(selectedRow, 0).toString()
    );

    // ✅ Message de confirmation
    int choix = JOptionPane.showConfirmDialog(
        null,
        "Voulez-vous vraiment supprimer cet adhérent ?",
        "Confirmation",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.WARNING_MESSAGE
    );

    if (choix != JOptionPane.YES_OPTION) {
        return; // Annulé
    }

    // ✅ Suppression dans la base
    dao.supprimer(id);

    // ✅ Rafraîchir la table
    panel.refreshAdherentTable(dao.getAll());
}
}
