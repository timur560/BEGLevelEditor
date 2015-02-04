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

    public int mode = MODE_NONE;

    public JPanel contentPane;

    private JButton toJson;
    private LevelMap levelPanel;
    private JPanel settingsPanel;
    private JTabbedPane tabbedPane1;
    private JLabel coordinatesLabel;
    private JButton uploadCover;
    private JCheckBox createShapesModeCheckBox;
    private JScrollPane levelMapScrollArea;
    private JButton deleteShapeButton;

    public JList shapes,
            ladders;
    public DefaultListModel shapesListModel, laddersListModel;
    private JComboBox tilesetCombo;
    private JComboBox effectCombo;
    private JButton resetMode;
    private JButton createShapeButton;
    private JButton createLadderButton;
    private JButton deleteLadderButton;

    public BufferedImage cover, coverFull;

    private void createUIComponents() {
        levelPanel = new LevelMap(this);
        levelMapScrollArea = new JScrollPane(levelPanel);
        settingsPanel = new JPanel();
        toJson = new JButton("Get JSON");

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
                mode = MainFrame.MODE_NONE;
            }
        });

        /**********************************************
         *                  SHAPES
         **********************************************/

        // list
        shapesListModel = new DefaultListModel();
        shapes = new JList(shapesListModel);
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
                mode = MainFrame.MODE_SHAPE;
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
        laddersListModel = new DefaultListModel();
        ladders = new JList(laddersListModel);
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
                mode = MainFrame.MODE_LADDER;
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

    }

    public void setCoordinates(int x, int y) {
        coordinatesLabel.setText(x + ":" + y);
    }

}
