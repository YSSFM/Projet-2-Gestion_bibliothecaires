package ma.est.gestion.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import ma.est.gestion.controller.LivreController;
import ma.est.gestion.dao.LivreDao;

public class MainFrame extends JFrame {

    private JPanel contentPanel;

    public MainFrame() {

        setTitle("Gestion Bibliothèque - Admin");
        setSize(1350, 750);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Appliquer le style glassmorphisme
        getContentPane().setBackground(new Color(240, 245, 255));

        // MENU BAR 
        JMenuBar menuBar = new JMenuBar();

        JMenu menuGestion = new JMenu("Gestion");
        JMenuItem itemLivres = new JMenuItem("Livres");
        JMenuItem itemAdherents = new JMenuItem("Adhérents");
        JMenuItem itemEmprunts = new JMenuItem("Emprunts");
        JMenuItem itemQuitter = new JMenuItem("Quitter");

        menuGestion.add(itemLivres);
        menuGestion.add(itemAdherents);
        menuGestion.add(itemEmprunts);
        menuGestion.addSeparator();
        menuGestion.add(itemQuitter);

        menuBar.add(menuGestion);
        setJMenuBar(menuBar);

        //PANEL CENTRAL
        contentPanel = new JPanel(new BorderLayout());
        contentPanel.setOpaque(false);
        add(contentPanel, BorderLayout.CENTER);

        //ACTIONS MENU
        itemLivres.addActionListener(e -> afficherLivres());
        itemAdherents.addActionListener(e -> {
            dispose();
            new AdherentPanel().setVisible(true);
        });
        
        itemEmprunts.addActionListener(e -> {
            dispose();
            new EmpruntPanel().setVisible(true);
        });

        itemQuitter.addActionListener(e -> System.exit(0));

        //AFFICHAGE PAR DÉFAUT
        afficherLivres();
    }

    private void afficherLivres() {
        contentPanel.removeAll();

        LivrePanel livrePanel = new LivrePanel();
        LivreDao dao = new LivreDao();
        new LivreController(livrePanel, dao);

        contentPanel.add(livrePanel.getContentPane(), BorderLayout.CENTER);

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}