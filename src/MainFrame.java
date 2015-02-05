import org.stringtree.json.JSONReader;
import org.stringtree.json.JSONWriter;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;

import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

/**
 * Created by timur on 03.02.15.
 */
public class MainFrame extends JFrame {
    public static final int MODE_NONE = 0;
    public static final int MODE_SHAPE = 1;
    public static final int MODE_LADDER = 2;
    public static final int MODE_ENEMY = 3;
    public static final int MODE_MP = 4;
    public static final int MODE_MB = 5;
    public static final int MODE_HEART = 6;
    public static final int MODE_PORTAL = 7;

    public int mode = MODE_NONE;

    public JPanel contentPane;

    private JButton saveAsButton;
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
    private JButton createMovingPlatformButton;
    private JButton deleteMovingPlatformButton;
    public JList mp;

    // moving blocks tab
    public JList movingBlocks;
    private JButton createMovableBlockButton;
    private JButton deleteMovableBlockButton;

    // hearts tab
    public JList hearts;
    private JButton startCreatingHeartButton;
    private JButton deleteHeartButton;
    public JComboBox secret;
    public JCheckBox fakeCheckBox;

    // portal tab
    public JList portals;
    public JTextField level;
    public JTextField zone;
    public JTextField portal;
    private JButton startCreatingPortalButton;
    private JButton deletePortalButton;
    private JTextField fileTextField;
    private JButton saveButton;

    public BufferedImage cover, coverFull;

    private void createUIComponents() {
        levelPanel = new LevelMap(this);
        levelMapScrollArea = new JScrollPane(levelPanel);
        settingsPanel = new JPanel();
        saveAsButton = new JButton("Get JSON");

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

        saveAsButton = new JButton();
        saveAsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                if (fc.showSaveDialog(MainFrame.this) == JFileChooser.APPROVE_OPTION) {
                    fileTextField.setText(fc.getSelectedFile().getPath());
                    File f = fc.getSelectedFile();
                    try {
                        PrintWriter writer = new PrintWriter(f, "UTF-8");
                        writer.print(getJson());
                        writer.close();
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        });

        saveButton = new JButton();

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

        // list
        movingBlocks = new JList(new DefaultListModel());
        movingBlocks.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                levelPanel.repaint();
            }
        });

        // create mode
        createMovableBlockButton = new JButton();
        createMovableBlockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeMode(MainFrame.MODE_MB);
            }
        });

        // delete
        deleteMovableBlockButton = new JButton();
        deleteMovableBlockButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((DefaultListModel) movingBlocks.getModel()).remove(movingBlocks.getSelectedIndex());
                levelPanel.repaint();
            }
        });


        /**********************************************
         *                 HEARTS
         **********************************************/

        // list
        hearts = new JList(new DefaultListModel());
        hearts.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                levelPanel.repaint();
            }
        });

        // create mode
        startCreatingHeartButton = new JButton();
        startCreatingHeartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeMode(MainFrame.MODE_HEART);
            }
        });

        // delete
        deleteHeartButton = new JButton();
        deleteHeartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((DefaultListModel) hearts.getModel()).remove(hearts.getSelectedIndex());
                levelPanel.repaint();
            }
        });

        /**********************************************
         *                 PORTALS
         **********************************************/

        // list
        portals = new JList(new DefaultListModel());
        portals.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                levelPanel.repaint();
            }
        });

        // create mode
        startCreatingPortalButton = new JButton();
        startCreatingPortalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                changeMode(MainFrame.MODE_PORTAL);
            }
        });

        // delete
        deletePortalButton = new JButton();
        deletePortalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ((DefaultListModel) portals.getModel()).remove(portals.getSelectedIndex());
                levelPanel.repaint();
            }
        });
    }

    private String getJson() {
        Map<String, Object> result = new HashMap<>();

        result.put("width", levelPanel.width);
        result.put("height", levelPanel.height);
        result.put("effect", effectCombo.getSelectedItem().toString());
        result.put("tileset", Long.valueOf(tilesetCombo.getSelectedItem().toString()));

        int i;
        List s = new ArrayList();

        // shapes
        for (i = 0; i < shapes.getModel().getSize(); i++) {
            String el = ((DefaultListModel) shapes.getModel()).get(i).toString();
            JSONReader reader = new JSONReader();
            s.add((List<Long>) reader.read(el));
        }
        result.put("shapes", ((ArrayList) s).clone());

        // ladders
        s.clear();
        for (i = 0; i < ladders.getModel().getSize(); i++) {
            String el = ((DefaultListModel) ladders.getModel()).get(i).toString();
            JSONReader reader = new JSONReader();
            s.add((List<Long>) reader.read(el));
        }
        result.put("ladders", ((ArrayList) s).clone());

        // enemies
        s.clear();
        for (i = 0; i < enemies.getModel().getSize(); i++) {
            String el = ((DefaultListModel) enemies.getModel()).get(i).toString();
            JSONReader reader = new JSONReader();
            s.add((Map) reader.read(el));
        }
        result.put("enemies", ((ArrayList) s).clone());

        // moving platforms
        s.clear();
        for (i = 0; i < mp.getModel().getSize(); i++) {
            String el = ((DefaultListModel) mp.getModel()).get(i).toString();
            JSONReader reader = new JSONReader();
            s.add((Map) reader.read(el));
        }
        result.put("movingPlatforms", ((ArrayList) s).clone());

        // moving blocks
        s.clear();
        for (i = 0; i < movingBlocks.getModel().getSize(); i++) {
            String el = ((DefaultListModel) movingBlocks.getModel()).get(i).toString();
            JSONReader reader = new JSONReader();
            s.add((List<Long>) reader.read(el));
        }
        result.put("movingBlocks", ((ArrayList) s).clone());

        // hearts
        s.clear();
        for (i = 0; i < hearts.getModel().getSize(); i++) {
            String el = ((DefaultListModel) hearts.getModel()).get(i).toString();
            JSONReader reader = new JSONReader();
            s.add((Map) reader.read(el));
        }
        result.put("hearts", ((ArrayList) s).clone());

        // portals
        s.clear();
        for (i = 0; i < portals.getModel().getSize(); i++) {
            String el = ((DefaultListModel) portals.getModel()).get(i).toString();
            JSONReader reader = new JSONReader();
            s.add((Map) reader.read(el));
        }
        result.put("portals", ((ArrayList) s).clone());

        JSONWriter writer = new JSONWriter();
        return writer.write(result);
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
