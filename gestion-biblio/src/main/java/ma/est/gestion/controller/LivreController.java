package ma.est.gestion.controller;

import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import ma.est.gestion.dao.LivreDao;
import ma.est.gestion.model.Categorie;
import ma.est.gestion.model.Livre;
import ma.est.gestion.view.AdherentPanel;
import ma.est.gestion.view.EmpruntPanel;
import ma.est.gestion.view.LivrePanel;

public class LivreController {

    private LivrePanel view;
    private LivreDao dao;
    private DefaultTableModel model;

    public LivreController(LivrePanel view, LivreDao dao) {
        this.view = view;
        this.dao = dao;

        initTable();
        initActions();
        chargerLivres();
    }

    private void initTable() {
        model = new DefaultTableModel(
                new Object[]{"ID", "Code", "Titre", "Auteur", "Exemplaire", "Catégorie"}, 0
        );
        view.setTableModel(model);
    }

    private void initActions() {

        view.getBtnAjouter().addActionListener(e ->
                view.getFormPanel().setVisible(true)
        );

        view.getBtnValiderForm().addActionListener(e -> ajouterLivre());

        view.getBtnSupp().addActionListener(e -> supprimerSelection());


                // ACTIONS DES BOUTONS DE NAVIGATION

        view.getBtnGereAdh().addActionListener(e -> {
            java.awt.Window win = javax.swing.SwingUtilities.getWindowAncestor((java.awt.Component)e.getSource());
            if (win != null) win.dispose();
            new AdherentPanel().setVisible(true);
        });

        view.getBtnGereEmp().addActionListener(e -> {
            java.awt.Window win = javax.swing.SwingUtilities.getWindowAncestor((java.awt.Component)e.getSource());
            if (win != null) win.dispose();
            new EmpruntPanel().setVisible(true);
        });

        view.getBtnRetour().addActionListener(e -> {
            view.dispose();
            new ma.est.gestion.view.LoginFrame().setVisible(true);
        });

        // Ajouter des catégories par défaut
        view.getCbCategorie().addItem("Informatique");
        view.getCbCategorie().addItem("Mathématiques");
        view.getCbCategorie().addItem("Réseaux");
        view.getCbCategorie().addItem("IA");
        view.getCbCategorie().addItem("Littérature");
        view.getCbCategorie().addItem("Roman");
        
        view.getBtnSelectCat().addActionListener(e -> {
            String newCat = JOptionPane.showInputDialog(view, 
                "Nouvelle catégorie :", 
                "Ajouter une catégorie", 
                JOptionPane.QUESTION_MESSAGE);
            if (newCat != null && !newCat.trim().isEmpty()) {
                view.getCbCategorie().addItem(newCat.trim());
                view.getCbCategorie().setSelectedItem(newCat.trim());
            }
        });
    }

    private void chargerLivres() {
        model.setRowCount(0);
        for (Livre l : dao.getAll()) {
            model.addRow(new Object[]{
                    l.getId(),
                    l.getCode(),
                    l.getTitre(),
                    l.getAuteur(),
                    l.getNombreExemplaire(),
                    l.getCategorie()
            });
        }
    }

    private void ajouterLivre() {
        try {
            // Validation des champs
            if (
                view.getTfCode().getText().trim().isEmpty() ||
                view.getTfTitre().getText().trim().isEmpty() ||
                view.getTfAuteur().getText().trim().isEmpty() ||
                view.getTfEx().getText().trim().isEmpty() ||
                view.getCbCategorie().getSelectedItem() == null) {
                
                JOptionPane.showMessageDialog(view,
                    "Tous les champs sont obligatoires",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            int id = 0; // L'ID sera généré par la base de données
            int exemplaires = Integer.parseInt(view.getTfEx().getText());
            
            if (exemplaires < 0) {
                JOptionPane.showMessageDialog(view,
                    "Le nombre d'exemplaires doit être positif",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            Livre l = new Livre(
                id,
                view.getTfCode().getText(),
                view.getTfTitre().getText(),
                view.getTfAuteur().getText(),
                exemplaires,
                new Categorie(view.getCbCategorie().getSelectedItem().toString())
            );

            dao.addLivre(l);
            JOptionPane.showMessageDialog(view,
                "Livre ajouté avec succès!",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
            
            chargerLivres();
            view.getFormPanel().setVisible(false);
            
            // Réinitialiser les champs
            view.getTfCode().setText("");
            view.getTfTitre().setText("");
            view.getTfAuteur().setText("");
            view.getTfEx().setText("");
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view,
                "L'ID et le nombre d'exemplaires doivent être des nombres",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                "Erreur lors de l'ajout du livre: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerSelection() {
        int row = view.getTable().getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(view,
                "Veuillez sélectionner un livre à supprimer",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(view,
            "Voulez-vous vraiment supprimer ce livre ?",
            "Confirmation",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // Dans une version complète, supprimer de la base de données
            model.removeRow(row);
            JOptionPane.showMessageDialog(view,
                "Livre supprimé avec succès",
                "Succès",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}