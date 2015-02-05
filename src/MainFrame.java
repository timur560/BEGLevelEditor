import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by timur on 03.02.15.
 */
public class MainFrame extends JFrame {
    public static final int MODE_NONE = 0;
    public static final int MODE_SHAPE = 1;
    public static final int MODE_LADDER = 2;
    public static final int MODE_ENEMY = 3;
    public static final int MODE_MP = 4;

    public int mode = MODE_NONE;

    public JPanel contentPane;

    private JButton saveButton;
    private LevelMap levelPanel;
    private JPanel settingsPanel;
    private JTabbedPane tabbedPane1;
    private JLabel coordinatesLabel;
    private JButton uploadCover;
    private JCheckBox createShapesModeCheckBox;
    private JScrollPane levelMapScrollArea;
    private JButton deleteShapeButton;

    public JList shapes;
    public JList ladders;
    private JComboBox tilesetCombo;
    private JComboBox effectCombo;
    private JButton resetMode;
    private JButton createShapeButton;
    private JButton createLadderButton;
    private JButton deleteLadderButton;
    private JButton completeObjectButton;

    // enemy tab
    public JList enemies;
    private JButton createEnemyButton;
    private JButton deleteEnemyButton;
    public JCheckBox enemyCanDie;
    public JCheckBox enemyWeapon;
    public JTextField enemyWidth;
    public JTextField enemyHeight;
    public JTextField enemyLeft;
    public JTextField enemyTop;
    public JTextField enemySpeed;
    public JTextField enemyShootDelay;
    public JComboBox enemyType;
    public JComboBox enemyDirection;

    // moving platforms tab
    public JTextField mpWidth;
    public JTextField mpSpeed;
    public JButton createMovingPlatformButton;
    public JButton deleteMovingPlatformButton;
    public JList mp;
    private JList movingBlocks;

    public BufferedImage cover, coverFull;

    private void createUIComponents() {
        levelPanel = new LevelMap(this);
        levelMapScrollArea = new JScrollPane(levelPanel);
        settingsPanel = new JPanel();
        saveButton = new JButton("Get JSON");

        // upload cover image
        uploadCover = new JButton("Upload Cover");
        uploadCover.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();

                int returnVal = fc.showOpenDialog(MainFrame.this);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    try {
                        File file = fc.getSelectedFile();
                        coverFull = ImageIO.read(file);

                        cover = new BufferedImage(
                                coverFull.getWidth() / 50 * levelPanel.cellSize,
                                coverFull.getHeight() / 50 * levelPanel.cellSize,
                                BufferedImage.TYPE_INT_RGB
                        );
                        Graphics g = cover.createGraphics();
                        g.drawImage(coverFull, 0, 0,
                                coverFull.getWidth() / 50 * levelPanel.cellSize,
                                coverFull.getHeight() / 50 * levelPanel.cellSize,
                                null);
                        g.dispose();

                        levelPanel.setWidth(cover.getWidth());
                        levelPanel.setHeight(cover.getHeight());
                        levelPanel.repaint();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } else {
                    ///
                }
            }

        });

        // reset mode
        resetMode = new JButton();
        resetMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeMode(MainFrame.MODE_NONE);
            }
        });

        completeObjectButton = new JButton();
        completeObjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                levelPanel.completeObject();
            }
        });


        /**********************************************
         *                  SHAPES
         **********************************************/

        // list
        shapes = new JList(new DefaultListModel());
        shapes.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                levelPanel.repaint();
            }
        });

        // create mode
        createShapeButton = new JButton();
        createShapeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeMode(MainFrame.MODE_SHAPE);
            }
        });

        // delete
        deleteShapeButton = new JButton();
        deleteShapeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((DefaultListModel) shapes.getModel()).remove(shapes.getSelectedIndex());
                levelPanel.repaint();
            }
        });

        /**********************************************
         *                 LADDERS
         **********************************************/

        // list
        ladders = new JList(new DefaultListModel());
        ladders.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                levelPanel.repaint();
            }
        });

        // create mode
        createLadderButton = new JButton();
        createLadderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeMode(MainFrame.MODE_LADDER);
            }
        });

        // delete
        deleteLadderButton = new JButton();
        deleteLadderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((DefaultListModel) ladders.getModel()).remove(ladders.getSelectedIndex());
                levelPanel.repaint();
            }
        });

        /**********************************************
         *                 ENEMIES
         **********************************************/

        // list
        enemies = new JList(new DefaultListModel());
        enemies.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                levelPanel.repaint();
            }
        });

        // create mode
        createEnemyButton = new JButton();
        createEnemyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeMode(MainFrame.MODE_ENEMY);
            }
        });

        // delete
        deleteEnemyButton = new JButton();
        deleteEnemyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((DefaultListModel) enemies.getModel()).remove(enemies.getSelectedIndex());
                levelPanel.repaint();
            }
        });

        // change type
        enemyType = new JComboBox();
        enemyType.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (enemyType.getModel().getSelectedItem().toString().equals("snowman")) {
                    enemyWidth.setText("0.6");
                    enemyHeight.setText("1.0");
                    enemyLeft.setText("0.0");
                    enemyTop.setText("0.0");
                } else if (enemyType.getModel().getSelectedItem().toString().equals("snowflake")) {
                    enemyWidth.setText("1.0");
                    enemyHeight.setText("1.0");
                    enemyLeft.setText("0.0");
                    enemyTop.setText("0.0");
                }
            }
        });

        /**********************************************
         *                 MOVING PLATFORMS
         **********************************************/

        // list
        mp = new JList(new DefaultListModel());
        mp.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                levelPanel.repaint();
            }
        });

        // create mode
        createMovingPlatformButton = new JButton();
        createMovingPlatformButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeMode(MainFrame.MODE_MP);
            }
        });

        // delete
        deleteMovingPlatformButton = new JButton();
        deleteMovingPlatformButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((DefaultListModel) mp.getModel()).remove(mp.getSelectedIndex());
                levelPanel.repaint();
            }
        });

        /**********************************************
         *                 MOVING BLOCKS
         **********************************************/

        movingBlocks = new JList(new DefaultListModel());
    }

    public void setCoordinates(int x, int y) {
        coordinatesLabel.setText(x + ":" + y);
    }

    public void changeMode(int m) {
        levelPanel.x.clear();
        levelPanel.y.clear();
        mode = m;
        levelPanel.repaint();
    }
}
