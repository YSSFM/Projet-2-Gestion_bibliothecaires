package ma.est.gestion.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.List;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.LineBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import ma.est.gestion.dao.impl.EmpruntDaoImpl;
import ma.est.gestion.model.Emprunt;

public class EmpruntActivePanel extends JFrame {

    private JTable tableEmp;
    private DefaultTableModel tableModelEmp;
    private JTextField textFieldSearch;
    private JLabel labelCount;

    private JButton btnRetour;
    private JButton btnReset;
    private JButton btnRetournerLivre;

    private final EmpruntDaoImpl empruntDao;

    private final String login;
    private final String email;
    private final int numAdherent;

    public EmpruntActivePanel(String login, String email, int numAdherent) {
        this.login = login;
        this.email = email;
        this.numAdherent = numAdherent;

        empruntDao = new EmpruntDaoImpl();

        setTitle("Mes Emprunts - " + login);
        setSize(1200, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        initUI();
        loadMyEmprunts();
    }

    private void initUI() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        header.setBackground(new Color(240, 245, 255));

        JLabel title = new JLabel("📚 Mes Emprunts");
        title.setFont(new Font("Segoe UI", Font.BOLD, 18));
        title.setForeground(new Color(60, 80, 120));

        btnRetour = createGlassButton("Acceuil", new Color(149, 165, 166));
        btnRetournerLivre = createGlassButton("Retourner le Livre", new Color(52, 152, 219));
        btnRetournerLivre.setPreferredSize(new Dimension(160, 35));

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(new Color(240, 245, 255)); 
        centerPanel.add(btnRetournerLivre); 


        header.add(title, BorderLayout.WEST);
        header.add(centerPanel, BorderLayout.CENTER);
        header.add(btnRetour, BorderLayout.EAST);


        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Rechercher un emprunt"));

        textFieldSearch = new JTextField(40);
        textFieldSearch.setPreferredSize(new Dimension(400, 30));

        btnReset = createGlassButton("Réinitialiser", new Color(189, 195, 199));

        searchPanel.add(textFieldSearch);
        searchPanel.add(btnReset);

        JPanel topContainer = new JPanel();
        topContainer.setLayout(new BorderLayout());

        topContainer.add(header, BorderLayout.NORTH);
        topContainer.add(searchPanel, BorderLayout.SOUTH);

        add(topContainer, BorderLayout.NORTH);


        tableModelEmp = new DefaultTableModel(
                new Object[]{"Code Emprunt", "Num Adhérent", "Nom Adhérent",
                        "Livre", "Date Emprunt", "Date Retour", "Statut"}, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        tableEmp = new JTable(tableModelEmp);
        tableEmp.setRowHeight(26);
        tableEmp.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tableEmp.setGridColor(new Color(220, 220, 220));

        tableEmp.getTableHeader().setBackground(new Color(70, 130, 180, 180));
        tableEmp.getTableHeader().setForeground(Color.WHITE);
        tableEmp.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));

        JScrollPane scroll = new JScrollPane(tableEmp);
        scroll.setBorder(new LineBorder(new Color(200, 200, 200)));

        add(scroll, BorderLayout.CENTER);

        labelCount = new JLabel("Total Emprunts : 0");
        labelCount.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));

        add(labelCount, BorderLayout.SOUTH);

        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModelEmp);
        tableEmp.setRowSorter(sorter);

        textFieldSearch.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filter(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filter(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filter(); }

            private void filter() {
                String text = textFieldSearch.getText().trim();
                sorter.setRowFilter(text.isEmpty() ? null :
                        RowFilter.regexFilter("(?i)" + Pattern.quote(text)));
            }
        });

        btnReset.addActionListener(e -> {
            textFieldSearch.setText("");
            sorter.setRowFilter(null);
        });

        btnRetournerLivre.addActionListener(e -> retournerLivre());

        btnRetour.addActionListener(e -> {
            dispose();
            new UtilisateurPanel(this.login, this.email, this.numAdherent).setVisible(true);
        });
    }

    private void loadMyEmprunts() {
        tableModelEmp.setRowCount(0);

        List<Emprunt> emprunts = empruntDao.getEmpruntsByAdherent(numAdherent);

        for (Emprunt e : emprunts) {
            tableModelEmp.addRow(new Object[]{
                    e.getCodeEmprunt(),
                    e.getNumAdherent(),
                    e.getNomAdherent(),
                    e.getCodeLivre(),
                    e.getDateEmprunt(),
                    e.getDateRetour(),
                    e.getStatut()
            });
        }

        labelCount.setText("Total Emprunts : " + emprunts.size());
    }

    private JButton createGlassButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                Color c = new Color(color.getRed(), color.getGreen(), color.getBlue(), 170);
                g2d.setColor(c);
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
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));

        return button;
    }
    private void retournerLivre() {

        int selectedRow = tableEmp.getSelectedRow();

        if (selectedRow == -1) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Veuillez sélectionner un emprunt à retourner",
                    "Aucune sélection",
                    javax.swing.JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Convertir l’index de la vue vers le modèle (important avec le tri)
        int modelRow = tableEmp.convertRowIndexToModel(selectedRow);

        Emprunt emprunt = new Emprunt();
        emprunt.setCodeEmprunt((String) tableModelEmp.getValueAt(modelRow, 0));
        emprunt.setCodeLivre((String) tableModelEmp.getValueAt(modelRow, 3));
        emprunt.setStatut((String) tableModelEmp.getValueAt(modelRow, 6));

        // Vérifier si déjà retourné
        if ("Retourne".equalsIgnoreCase(emprunt.getStatut())) {
            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Ce livre est déjà retourné",
                    "Action impossible",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        int confirm = javax.swing.JOptionPane.showConfirmDialog(
                this,
                "Confirmer le retour du livre ?",
                "Confirmation",
                javax.swing.JOptionPane.YES_NO_OPTION
        );

        if (confirm == javax.swing.JOptionPane.YES_OPTION) {

            empruntDao.cloturerEmprunt(emprunt);

            javax.swing.JOptionPane.showMessageDialog(
                    this,
                    "Livre retourné avec succès",
                    "Succès",
                    javax.swing.JOptionPane.INFORMATION_MESSAGE
            );

            loadMyEmprunts(); // Rafraîchir le tableau
        }
    }
}
