package ma.est.gestion.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.RenderingHints;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import ma.est.gestion.controller.EmpruntController;
import ma.est.gestion.model.Emprunt;

public class EmpruntPanel extends JFrame {
    
    private DefaultTableModel tableModelEmp;
    private JTable tableEmp;
    private JButton buttonModifierEmp;
    private JButton buttonResetEmp;
    private JButton buttonSupprimerEmp;
    private JButton buttonRetourEmp;
    private JButton buttonRetardEmp;
    private JLabel label4Emp;

    public EmpruntPanel() {
        super("Gestion des emprunts");
        initUI();
        
        // Initialiser le contrôleur
        @SuppressWarnings("unused")
         EmpruntController controller =
        new EmpruntController(this);
    }

    private void initUI() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        setLayout(new BorderLayout());
        
        JPanel panEmprunt = new JPanel(new BorderLayout());

        // Panel de recherche
        JPanel panel1Emp = new JPanel(new GridBagLayout());
        panel1Emp.setBorder(BorderFactory.createTitledBorder("Recherche d'emprunt"));

        JTextField textFieldEmp = new JTextField(50);
        Dimension tfSize = textFieldEmp.getPreferredSize();
        textFieldEmp.setPreferredSize(new Dimension(tfSize.width, 30));

        buttonResetEmp = createGlassButton("Réinitialiser", new Color(149, 165, 166));

        JPanel group = new JPanel(new FlowLayout(FlowLayout.CENTER, 6, 0));
        group.setOpaque(false);
        group.add(textFieldEmp);
        group.add(buttonResetEmp);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1Emp.add(new JPanel() {{ setOpaque(false); }}, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 1; gbc.weightx = 0; gbc.anchor = GridBagConstraints.CENTER;
        panel1Emp.add(group, gbc);

        gbc = new GridBagConstraints();
        gbc.gridx = 2; gbc.weightx = 1.0; gbc.fill = GridBagConstraints.HORIZONTAL;
        panel1Emp.add(new JPanel() {{ setOpaque(false); }}, gbc);

        // Table des emprunts
        JPanel panel2Emp = new JPanel();
        panel2Emp.setBorder(BorderFactory.createTitledBorder("Liste des Emprunts :"));
        
        tableModelEmp = new DefaultTableModel(
            new Object[]{"Code Emprunt", "Num Adhérent", "Nom Adhérent", "Livre", "Date Emprunt", "Date Retour", "Statut"}, 0
        );
        
        tableEmp = new JTable(tableModelEmp);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModelEmp);
        tableEmp.setRowSorter(sorter);

        tableEmp.setRowHeight(26);
        tableEmp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableEmp.setForeground(new Color(60, 80, 120));
        tableEmp.setBackground(Color.WHITE);
        tableEmp.setGridColor(new Color(220, 220, 220));

        tableEmp.getTableHeader().setBackground(new Color(70, 130, 180, 180));
        tableEmp.getTableHeader().setForeground(Color.WHITE);
        tableEmp.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(tableEmp);
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(new LineBorder(new Color(200, 200, 200, 150)));


        textFieldEmp.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filter(); }

            private void filter() {
                String text = textFieldEmp.getText().trim();
                sorter.setRowFilter(text.isEmpty() ? null : RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
            }
        });

        buttonResetEmp.addActionListener(ev -> {
            textFieldEmp.setText("");
            sorter.setRowFilter(null);
        });

        label4Emp = new JLabel("Total Emprunts: 0");
        panel2Emp.setLayout(new BoxLayout(panel2Emp, BoxLayout.Y_AXIS));
        panel2Emp.add(new JScrollPane(tableEmp));
        panel2Emp.add(label4Emp);

        // Panel d'actions
        JPanel panel3Emp = new JPanel();
        panel3Emp.setBorder(BorderFactory.createTitledBorder("Gestion des Emprunts"));

        panel3Emp.setLayout(new BoxLayout(panel3Emp, BoxLayout.Y_AXIS));

        Dimension panelSizeEmp = new Dimension(300, 300);
        panel3Emp.setPreferredSize(panelSizeEmp);
        panel3Emp.setMinimumSize(panelSizeEmp);
        panel3Emp.setMaximumSize(panelSizeEmp);

        buttonModifierEmp =  createGlassButton("Modifier Emprunt", new Color(46, 204, 113));
        buttonSupprimerEmp = createGlassButton("Supprimer Emprunt", new Color(231, 76, 60));
        buttonRetourEmp = createGlassButton("Marquer Retourné", new Color(52, 152, 219));
        buttonRetardEmp = createGlassButton("Marquer En Retard", new Color(241, 196, 15));

        JLabel labEmp = new JLabel("Statuts disponibles:");
        JLabel modEmp = new JLabel("Actif, Retourné, En retard");


        panel3Emp.add(Box.createVerticalStrut(20));
        panel3Emp.add(buttonModifierEmp);
        panel3Emp.add(Box.createVerticalStrut(10));
        panel3Emp.add(buttonSupprimerEmp);
        panel3Emp.add(Box.createVerticalStrut(10));
        panel3Emp.add(buttonRetourEmp);
        panel3Emp.add(Box.createVerticalStrut(10));
        panel3Emp.add(buttonRetardEmp);
        panel3Emp.add(Box.createVerticalStrut(40));
        panel3Emp.add(labEmp);
        panel3Emp.add(Box.createVerticalStrut(10));
        panel3Emp.add(modEmp);


                
        // Bouton retour vers le menu principal
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton btnRetourMenu = createGlassButton("Retour au menu", new Color(155, 89, 182));


        buttonModifierEmp.setAlignmentX(JButton.CENTER_ALIGNMENT);
        buttonSupprimerEmp.setAlignmentX(JButton.CENTER_ALIGNMENT);
        buttonRetourEmp.setAlignmentX(JButton.CENTER_ALIGNMENT);
        buttonRetardEmp.setAlignmentX(JButton.CENTER_ALIGNMENT);
        labEmp.setAlignmentX(JLabel.CENTER_ALIGNMENT);
        modEmp.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        Dimension buttonSizeEmp = new Dimension(195, 30);
        
        buttonModifierEmp.setPreferredSize(buttonSizeEmp);
        buttonModifierEmp.setMaximumSize(buttonSizeEmp);
        buttonModifierEmp.setMinimumSize(buttonSizeEmp);

        buttonSupprimerEmp.setPreferredSize(buttonSizeEmp);
        buttonSupprimerEmp.setMaximumSize(buttonSizeEmp);
        buttonSupprimerEmp.setMinimumSize(buttonSizeEmp);

        buttonRetourEmp.setPreferredSize(buttonSizeEmp);
        buttonRetourEmp.setMaximumSize(buttonSizeEmp);
        buttonRetourEmp.setMinimumSize(buttonSizeEmp);

        buttonRetardEmp.setPreferredSize(buttonSizeEmp);
        buttonRetardEmp.setMaximumSize(buttonSizeEmp);
        buttonRetardEmp.setMinimumSize(buttonSizeEmp);

        buttonResetEmp.setPreferredSize(buttonSizeEmp);
        buttonResetEmp.setMaximumSize(buttonSizeEmp);
        buttonResetEmp.setMinimumSize(buttonSizeEmp);

        btnRetourMenu.setPreferredSize(buttonSizeEmp);
        btnRetourMenu.setMaximumSize(buttonSizeEmp);
        btnRetourMenu.setMinimumSize(buttonSizeEmp);


        panEmprunt.add(panel1Emp, BorderLayout.NORTH);
        panEmprunt.add(panel2Emp, BorderLayout.CENTER);
        panEmprunt.add(panel3Emp, BorderLayout.EAST);

        add(panEmprunt);

        btnRetourMenu.addActionListener(e -> {
            java.awt.Window win = javax.swing.SwingUtilities.getWindowAncestor((java.awt.Component)e.getSource());
            if (win != null) win.dispose();
            new MainFrame().setVisible(true);
        });

        
        bottomPanel.add(btnRetourMenu);
        add(bottomPanel, BorderLayout.SOUTH);

        setSize(1350, 750);
        setLocationRelativeTo(null);
    }

    public void refreshTable(List<Emprunt> liste) {
        if (tableModelEmp == null) return;
        tableModelEmp.setRowCount(0);
        
        for (Emprunt e : liste) {
            tableModelEmp.addRow(new Object[]{
                e.getCodeEmprunt(),
                e.getNumAdherent(),
                e.getNomAdherent() != null ? e.getNomAdherent() : "Inconnu",
                e.getCodeLivre(),
                e.getDateEmprunt(),
                e.getDateRetour(),
                e.getStatut()
            });
        }
        
        // Mettre à jour le compteur
        updateCount(liste.size());
    }

    public void updateCount(int count) {
        label4Emp.setText("Total Emprunts: " + count);
    }

    public JTable getTable() {
        return tableEmp;
    }

    public JButton getModifier() {
        return buttonModifierEmp;
    }

    public JButton getSupprimer() {
        return buttonSupprimerEmp;
    }

    public JButton getRetourButton() {
        return buttonRetourEmp;
    }

    public JButton getRetardButton() {
        return buttonRetardEmp;
    }

    public JLabel getLabel4Emp() {
        return label4Emp;
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
        
        button.setFont(new java.awt.Font("Segoe UI", java.awt.Font.BOLD, 13));
        button.setForeground(java.awt.Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(javax.swing.BorderFactory.createEmptyBorder(8, 15, 8, 15));
        button.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        return button;
    }
}