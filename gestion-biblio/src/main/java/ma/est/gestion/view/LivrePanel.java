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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.RadialGradientPaint;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableModel;

public class LivrePanel extends JFrame {

    // TABLE
    private JTable table;

    // BOUTONS HEADER
    private JButton btnAjouter;
    private JButton btnSupp;
    private JButton btnGereAdh;
    private JButton btnGereEmp;
    private JButton btnRetour;

    // FORMULAIRE 
    private JPanel formPanel;
    private JTextField tfCode;
    private JTextField tfTitre;
    private JTextField tfAuteur;
    private JTextField tfEx;
    private JComboBox<String> cbCategorie;
    private JButton btnValiderForm;

    // CATEGORIE
    private JButton btnSelectCat;

    public LivrePanel() {
        setTitle("Gestion Bibliothèque - Admin");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Fond principal avec gradient professionnel
        JPanel mainContainer = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                // Gradient de fond sophistiqué
                LinearGradientPaint bgGradient = new LinearGradientPaint(
                    0, 0, 0, getHeight(),
                    new float[]{0.0f, 0.5f, 1.0f},
                    new Color[]{
                        new Color(245, 247, 250),
                        new Color(235, 240, 250),
                        new Color(225, 235, 250)
                    }
                );
                g2d.setPaint(bgGradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                // Effets de lumière subtils
                RadialGradientPaint lightEffect1 = new RadialGradientPaint(
                    new Point2D.Float(getWidth() * 0.2f, getHeight() * 0.3f),
                    getWidth() * 0.4f,
                    new float[]{0.0f, 0.7f, 1.0f},
                    new Color[]{
                        new Color(255, 255, 255, 30),
                        new Color(255, 255, 255, 10),
                        new Color(255, 255, 255, 0)
                    }
                );
                g2d.setPaint(lightEffect1);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                RadialGradientPaint lightEffect2 = new RadialGradientPaint(
                    new Point2D.Float(getWidth() * 0.8f, getHeight() * 0.7f),
                    getWidth() * 0.3f,
                    new float[]{0.0f, 0.8f, 1.0f},
                    new Color[]{
                        new Color(220, 230, 255, 20),
                        new Color(220, 230, 255, 5),
                        new Color(220, 230, 255, 0)
                    }
                );
                g2d.setPaint(lightEffect2);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                g2d.dispose();
            }
        };
        mainContainer.setOpaque(false);
        
        // HEADER avec effet glass professionnel
        JPanel header = createGlassPanel();
        header.setLayout(new BorderLayout(20, 0));
        header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(15, 25, 15, 25),
            BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(255, 255, 255, 120))
        ));

        JLabel title = new JLabel("📚 Gestion des Livres");
        title.setForeground(new Color(45, 65, 105, 240));
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));

        JPanel headerBtns = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        headerBtns.setOpaque(false);

        btnAjouter = createGlassButton("＋ Ajouter", new Color(39, 174, 96));
        btnSupp = createGlassButton("🗑 Supprimer", new Color(192, 57, 43));
        btnGereAdh = createGlassButton("👥 Adhérents", new Color(41, 128, 185));
        btnGereEmp = createGlassButton("📋 Emprunts", new Color(243, 156, 18));
        btnRetour = createGlassButton("← Accueil", new Color(127, 140, 141));

        btnRetour.addActionListener(e -> {
            java.awt.Window win = javax.swing.SwingUtilities.getWindowAncestor((java.awt.Component)e.getSource());
            if (win != null) win.dispose();
            new LoginFrame().setVisible(true);
        });

        headerBtns.add(btnAjouter);
        headerBtns.add(btnSupp);
        headerBtns.add(btnGereAdh);
        headerBtns.add(btnGereEmp);
        headerBtns.add(btnRetour);

        header.add(title, BorderLayout.WEST);
        header.add(headerBtns, BorderLayout.EAST);

        mainContainer.add(header, BorderLayout.NORTH);

        // CONTENU PRINCIPAL
        JPanel contentPanel = new JPanel(new BorderLayout(20, 20));
        contentPanel.setOpaque(false);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        // TABLE avec effet glass amélioré
        table = new JTable() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Rendre la table non éditable
            }
        };
        
        // Personnalisation de la table
        table.setRowHeight(40);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(52, 152, 219, 80));
        table.setSelectionForeground(new Color(45, 65, 105));
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setForeground(new Color(60, 70, 90));
        
        JScrollPane scroll = new JScrollPane(table) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Effet glass pour le scroll pane
                Color scrollBg = new Color(255, 255, 255, 40);
                g2d.setColor(scrollBg);
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                g2d.dispose();
            }
        };
        
        scroll.setOpaque(false);
        scroll.getViewport().setOpaque(false);
        scroll.setBorder(BorderFactory.createEmptyBorder());
        scroll.getViewport().setBackground(new Color(255, 255, 255, 0));
        
        JPanel tablePanel = createGlassPanel();
        tablePanel.setLayout(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createEmptyBorder(),
            BorderFactory.createLineBorder(new Color(255, 255, 255, 100), 1)
        ));
        tablePanel.add(scroll, BorderLayout.CENTER);
        
        // FORMULAIRE avec effet glass professionnel
        formPanel = createGlassPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(
                BorderFactory.createEmptyBorder(),
                "📝 Informations du livre",
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION,
                javax.swing.border.TitledBorder.DEFAULT_POSITION,
                new Font("Segoe UI", Font.BOLD, 16),
                new Color(45, 65, 105, 220)
            ),
            BorderFactory.createEmptyBorder(25, 25, 25, 25)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        tfCode = createGlassTextField();
        tfTitre = createGlassTextField();
        tfAuteur = createGlassTextField();
        tfEx = createGlassTextField();

        cbCategorie = new JComboBox<>();
        cbCategorie.setEditable(true);
        stylizeComboBox(cbCategorie);

        // Configuration des champs du formulaire
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createFormLabel("Code :"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfCode, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(createFormLabel("Titre :"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfTitre, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(createFormLabel("Auteur :"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfAuteur, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(createFormLabel("Exemplaires :"), gbc);
        gbc.gridx = 1;
        formPanel.add(tfEx, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(createFormLabel("Catégorie :"), gbc);
        gbc.gridx = 1;
        formPanel.add(cbCategorie, gbc);

        btnValiderForm = createGlassButton("✓ Valider", new Color(142, 68, 173));

        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setOpaque(false);
        formContainer.add(formPanel, BorderLayout.CENTER);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        buttonPanel.add(btnValiderForm);
        
        formContainer.add(buttonPanel, BorderLayout.SOUTH);
        formPanel.setVisible(false);

        // Panel de droite pour le formulaire
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setOpaque(false);
        rightPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
        rightPanel.add(formContainer, BorderLayout.CENTER);

        contentPanel.add(tablePanel, BorderLayout.CENTER);
        contentPanel.add(rightPanel, BorderLayout.EAST);
        mainContainer.add(contentPanel, BorderLayout.CENTER);

        // FOOTER avec effet glass
        btnSelectCat = createGlassButton("＋ Nouvelle Catégorie", new Color(44, 62, 80));

        JPanel bottom = createGlassPanel();
        bottom.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
        bottom.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(2, 0, 0, 0, new Color(255, 255, 255, 120)),
            BorderFactory.createEmptyBorder(15, 25, 15, 25)
        ));

        JLabel catLabel = createFormLabel("📂 Catégories disponibles :");
        catLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        
        cbCategorie.setPreferredSize(new Dimension(200, 40));
        
        bottom.add(catLabel);
        bottom.add(cbCategorie);
        bottom.add(btnSelectCat);

        mainContainer.add(bottom, BorderLayout.SOUTH);
        add(mainContainer);
    }

    // Méthodes pour le style glassmorphisme professionnel

    private JPanel createGlassPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                int width = getWidth();
                int height = getHeight();
                int arc = 20;

                // Fond glassmorphisme avec blur effect
                LinearGradientPaint glassGradient = new LinearGradientPaint(
                    0, 0, 0, height,
                    new float[]{0.0f, 1.0f},
                    new Color[]{
                        new Color(255, 255, 255, 220),
                        new Color(255, 255, 255, 180)
                    }
                );
                g2d.setPaint(glassGradient);
                g2d.fillRoundRect(0, 0, width, height, arc, arc);

                // Bordure lumineuse
                g2d.setColor(new Color(255, 255, 255, 240));
                g2d.setStroke(new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawRoundRect(1, 1, width - 3, height - 3, arc, arc);

                // Ombre intérieure subtile
                g2d.setColor(new Color(0, 0, 0, 10));
                g2d.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);

                // Effet de réflexion en haut
                LinearGradientPaint highlight = new LinearGradientPaint(
                    0, 0, 0, height / 3,
                    new float[]{0.0f, 1.0f},
                    new Color[]{
                        new Color(255, 255, 255, 100),
                        new Color(255, 255, 255, 0)
                    }
                );
                g2d.setPaint(highlight);
                g2d.fillRoundRect(2, 2, width - 4, height / 3, arc - 2, arc - 2);

                g2d.dispose();
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    private JTextField createGlassTextField() {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                if (!isFocusOwner()) {
                    // Effet glass pour le champ non focus
                    g2d.setColor(new Color(255, 255, 255, 120));
                    g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                }

                g2d.dispose();
            }
        };
        
        field.setPreferredSize(new Dimension(200, 45));
        field.setBackground(new Color(255, 255, 255, 80));
        field.setForeground(new Color(45, 65, 105));
        field.setCaretColor(new Color(52, 152, 219));
        field.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 230, 150), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setSelectionColor(new Color(52, 152, 219, 100));
        return field;
    }

    private void stylizeComboBox(JComboBox<String> comboBox) {
        comboBox.setPreferredSize(new Dimension(200, 45));
        comboBox.setBackground(new Color(255, 255, 255, 120));
        comboBox.setForeground(new Color(45, 65, 105));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 210, 230, 150), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        
        // Style de la flèche du combo box
        comboBox.setUI(new javax.swing.plaf.basic.BasicComboBoxUI() {
            @Override
            protected javax.swing.JButton createArrowButton() {
                javax.swing.JButton button = new javax.swing.JButton() {
                    @Override
                    protected void paintComponent(Graphics g) {
                        super.paintComponent(g);
                        Graphics2D g2d = (Graphics2D) g.create();
                        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        
                        // Dessiner une flèche stylisée
                        g2d.setColor(new Color(45, 65, 105, 180));
                        int[] xPoints = {getWidth()/2 - 5, getWidth()/2 + 5, getWidth()/2};
                        int[] yPoints = {getHeight()/2 - 3, getHeight()/2 - 3, getHeight()/2 + 3};
                        g2d.fillPolygon(xPoints, yPoints, 3);
                        
                        g2d.dispose();
                    }
                };
                button.setContentAreaFilled(false);
                button.setBorder(BorderFactory.createEmptyBorder());
                return button;
            }
        });
    }

    private JLabel createFormLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(new Color(45, 65, 105, 220));
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        return label;
    }

    private JButton createGlassButton(String text, Color color) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

                int width = getWidth();
                int height = getHeight();
                int arc = 12;

                // Effet glass pour les boutons
                if (getModel().isPressed()) {
                    LinearGradientPaint pressedGradient = new LinearGradientPaint(
                        0, 0, 0, height,
                        new float[]{0.0f, 1.0f},
                        new Color[]{
                            new Color(color.getRed(), color.getGreen(), color.getBlue(), 220),
                            new Color(color.getRed(), color.getGreen(), color.getBlue(), 180)
                        }
                    );
                    g2d.setPaint(pressedGradient);
                } else if (getModel().isRollover()) {
                    LinearGradientPaint hoverGradient = new LinearGradientPaint(
                        0, 0, 0, height,
                        new float[]{0.0f, 1.0f},
                        new Color[]{
                            new Color(color.getRed(), color.getGreen(), color.getBlue(), 180),
                            new Color(color.getRed(), color.getGreen(), color.getBlue(), 140)
                        }
                    );
                    g2d.setPaint(hoverGradient);
                } else {
                    LinearGradientPaint normalGradient = new LinearGradientPaint(
                        0, 0, 0, height,
                        new float[]{0.0f, 1.0f},
                        new Color[]{
                            new Color(color.getRed(), color.getGreen(), color.getBlue(), 160),
                            new Color(color.getRed(), color.getGreen(), color.getBlue(), 120)
                        }
                    );
                    g2d.setPaint(normalGradient);
                }
                
                g2d.fillRoundRect(0, 0, width, height, arc, arc);

                // Bordure lumineuse
                g2d.setColor(new Color(255, 255, 255, 180));
                g2d.setStroke(new BasicStroke(1.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
                g2d.drawRoundRect(0, 0, width - 1, height - 1, arc, arc);

                // Effet de surbrillance
                if (!getModel().isPressed()) {
                    LinearGradientPaint highlight = new LinearGradientPaint(
                        0, 0, 0, height / 3,
                        new float[]{0.0f, 1.0f},
                        new Color[]{
                            new Color(255, 255, 255, 80),
                            new Color(255, 255, 255, 0)
                        }
                    );
                    g2d.setPaint(highlight);
                    g2d.fillRoundRect(1, 1, width - 2, height / 3, arc - 1, arc - 1);
                }

                g2d.dispose();
                super.paintComponent(g);
            }
        };
        
        button.setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(12, 20, 12, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Effet d'ombre pour le texte
        button.setUI(new javax.swing.plaf.basic.BasicButtonUI() {
            @Override
            public void paint(Graphics g, javax.swing.JComponent c) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
                
                super.paint(g, c);
                g2d.dispose();
            }
        });
        
        return button;
    }

    // GETTERS 
    public JTable getTable() { return table; }

    public JButton getBtnAjouter() { return btnAjouter; }
    public JButton getBtnSupp() { return btnSupp; }
    public JButton getBtnGereAdh() { return btnGereAdh; }
    public JButton getBtnGereEmp() { return btnGereEmp; }
    public JButton getBtnRetour() { return btnRetour; }

    public JButton getBtnValiderForm() { return btnValiderForm; }
    public JPanel getFormPanel() { return formPanel; }

    public JTextField getTfCode() { return tfCode; }
    public JTextField getTfTitre() { return tfTitre; }
    public JTextField getTfAuteur() { return tfAuteur; }
    public JTextField getTfEx() { return tfEx; }
    public JComboBox<String> getCbCategorie() { return cbCategorie; }

    public JButton getBtnSelectCat() { return btnSelectCat; }

    public void setTableModel(TableModel model) {
        table.setModel(model);
        
        // Personnalisation de l'en-tête de la table
        table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Style glass pour l'en-tête
                setBackground(new Color(70, 130, 180, 200));
                setForeground(Color.WHITE);
                setFont(new Font("Segoe UI Semibold", Font.PLAIN, 14));
                setHorizontalAlignment(SwingConstants.CENTER);
                setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createMatteBorder(0, 0, 1, 1, new Color(255, 255, 255, 100)),
                    BorderFactory.createEmptyBorder(10, 5, 10, 5)
                ));
                return this;
            }
        });
        
        // Ajuster la largeur des colonnes
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        table.getTableHeader().setReorderingAllowed(false);
        
        // Style des cellules
        DefaultTableCellRenderer cellRenderer = new DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Alternance de couleurs pour les lignes
                if (row % 2 == 0) {
                    setBackground(new Color(255, 255, 255, 60));
                } else {
                    setBackground(new Color(255, 255, 255, 40));
                }
                
                setForeground(new Color(45, 65, 105));
                setFont(new Font("Segoe UI", Font.PLAIN, 13));
                setBorder(BorderFactory.createEmptyBorder(8, 12, 8, 12));
                setHorizontalAlignment(SwingConstants.LEFT);
                
                return this;
            }
        };
        
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setCellRenderer(cellRenderer);
        }
    }
}