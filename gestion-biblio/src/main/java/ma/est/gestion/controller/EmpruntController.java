package ma.est.gestion.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

import ma.est.gestion.dao.EmpruntDao;
import ma.est.gestion.dao.impl.EmpruntDaoImpl;
import ma.est.gestion.model.Emprunt;
import ma.est.gestion.view.EmpruntPanel;

public class EmpruntController {
    
    private EmpruntDao dao;
    private EmpruntPanel view;

    public EmpruntController(EmpruntPanel view) {
        this.dao = new EmpruntDaoImpl();
        this.view = view;
        
        // Charger les emprunts
        loadEmprunts();
        
        // Initialiser les actions des boutons
        initActions();
    }

    private void loadEmprunts() {
        try {
            var emprunts = dao.getAllEmprunts();
            view.refreshTable(emprunts);
            updateCount();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                "Erreur lors du chargement des emprunts: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    private void initActions() {
        view.getSupprimer().addActionListener(e -> supprimerEmprunt());
        view.getModifier().addActionListener(e -> modifierEmprunt());
        
        // Ajouter un bouton pour marquer comme retourné
        view.getRetourButton().addActionListener(e -> marquerRetourne());
        
        // Ajouter un bouton pour marquer comme en retard
        view.getRetardButton().addActionListener(e -> marquerEnRetard());
    }

    public void supprimerEmprunt() {
        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view, 
                "Veuillez sélectionner un emprunt à supprimer",
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String codeEmprunt = view.getTable().getValueAt(row, 0).toString();
        String adherent = view.getTable().getValueAt(row, 2).toString();
        String livre = view.getTable().getValueAt(row, 3).toString();

        int confirm = JOptionPane.showConfirmDialog(view, 
            "Voulez-vous supprimer cet emprunt ?\n" +
            "Adhérent: " + adherent + "\n" +
            "Livre: " + livre,
            "Confirmation", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Emprunt emprunt = dao.findEmpruntByCode(codeEmprunt);
            if (emprunt != null) {
                dao.deleteEmprunt(emprunt);
                JOptionPane.showMessageDialog(view, 
                    "Emprunt supprimé avec succès!",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
                loadEmprunts();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                "Erreur lors de la suppression: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void modifierEmprunt() {
        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view, 
                "Veuillez sélectionner un emprunt à modifier",
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        if (view.getTable().isEditing()) {
            var editor = view.getTable().getCellEditor();
            if (editor != null) editor.stopCellEditing();
        }

        String codeEmprunt = view.getTable().getValueAt(row, 0).toString();
        String adherent = view.getTable().getValueAt(row, 2).toString();
        String livre = view.getTable().getValueAt(row, 3).toString();

        // Demander la nouvelle date de retour
        String dateRetourStr = (String) JOptionPane.showInputDialog(
            view,
            "Entrez la nouvelle date de retour (format: AAAA-MM-JJ):\n" +
            "Adhérent: " + adherent + "\n" +
            "Livre: " + livre,
            "Modifier la date de retour",
            JOptionPane.QUESTION_MESSAGE,
            null,
            null,
            view.getTable().getValueAt(row, 5)
        );
        
        if (dateRetourStr == null || dateRetourStr.trim().isEmpty()) {
            return;
        }

        Date dateRetour = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dateRetour = sdf.parse(dateRetourStr.trim());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(view, 
                "Format de date invalide. Utilisez AAAA-MM-JJ", 
                "Erreur", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Demander le nouveau statut
        String[] statuts = {"Actif", "Retourné", "En retard"};
        String statutActuel = view.getTable().getValueAt(row, 6).toString();
        String nouveauStatut = (String) JOptionPane.showInputDialog(
            view,
            "Sélectionnez le nouveau statut:",
            "Modifier le statut",
            JOptionPane.QUESTION_MESSAGE,
            null,
            statuts,
            statutActuel
        );
        
        if (nouveauStatut == null) {
            return;
        }

        try {
            Emprunt emprunt = dao.findEmpruntByCode(codeEmprunt);
            if (emprunt != null) {
                emprunt.setDateRetour(dateRetour);
                emprunt.setStatut(nouveauStatut);
                dao.modifierEmprunt(emprunt);
                
                // Mettre à jour l'affichage
                view.getTable().setValueAt(dateRetourStr, row, 5);
                view.getTable().setValueAt(nouveauStatut, row, 6);
                
                JOptionPane.showMessageDialog(view, 
                    "Emprunt modifié avec succès !",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                "Erreur lors de la modification: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void marquerRetourne() {
        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view, 
                "Veuillez sélectionner un emprunt à marquer comme retourné",
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String codeEmprunt = view.getTable().getValueAt(row, 0).toString();
        String adherent = view.getTable().getValueAt(row, 2).toString();
        String livre = view.getTable().getValueAt(row, 3).toString();

        int confirm = JOptionPane.showConfirmDialog(view, 
            "Marquer cet emprunt comme retourné ?\n" +
            "Adhérent: " + adherent + "\n" +
            "Livre: " + livre,
            "Confirmation", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Emprunt emprunt = dao.findEmpruntByCode(codeEmprunt);
            if (emprunt != null) {
                dao.cloturerEmprunt(emprunt);
                
                // Mettre à jour l'affichage
                view.getTable().setValueAt("Retourné", row, 6);
                view.getTable().setValueAt(new Date(), row, 5); // Date actuelle comme date de retour
                
                JOptionPane.showMessageDialog(view, 
                    "Emprunt marqué comme retourné !",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                "Erreur: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    public void marquerEnRetard() {
        int row = view.getTable().getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(view, 
                "Veuillez sélectionner un emprunt à marquer comme en retard",
                "Information", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String codeEmprunt = view.getTable().getValueAt(row, 0).toString();
        String adherent = view.getTable().getValueAt(row, 2).toString();
        String livre = view.getTable().getValueAt(row, 3).toString();

        int confirm = JOptionPane.showConfirmDialog(view, 
            "Marquer cet emprunt comme en retard ?\n" +
            "Adhérent: " + adherent + "\n" +
            "Livre: " + livre,
            "Confirmation", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            Emprunt emprunt = dao.findEmpruntByCode(codeEmprunt);
            if (emprunt != null) {
                emprunt.setStatut("En retard");
                dao.modifierEmprunt(emprunt);
                
                // Mettre à jour l'affichage
                view.getTable().setValueAt("En retard", row, 6);
                
                JOptionPane.showMessageDialog(view, 
                    "Emprunt marqué comme en retard !",
                    "Succès",
                    JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view,
                "Erreur: " + e.getMessage(),
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateCount() {
        try {
            int total = dao.getEmpruntCount();
            view.getLabel4Emp().setText("Total Emprunts: " + total);
        } catch (Exception ex) {
            System.err.println("Erreur lors du calcul du nombre d'emprunts: " + ex.getMessage());
        }
    }
}