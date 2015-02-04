import javax.swing.*;
import java.awt.*;

import java.util.List;

/**
 * Created by timur on 03.02.15.
 */
public class MainFrame extends JFrame implements ListCellRenderer {
    public static final int MODE_NONE = 0;
    public static final int MODE_SHAPE = 1;

    public JPanel contentPane;

    private JButton toJson;
    private JPanel levelPanel;
    private JPanel settingsPanel;
    private JTabbedPane tabbedPane1;
    private JLabel coordinatesLabel;
    private JButton uploadCover;
    private JList<List<Long>> shapesList;
    private JCheckBox createShapesModeCheckBox;

    private void createUIComponents() {
        levelPanel = new LevelMap(this);

        settingsPanel = new JPanel();

        toJson = new JButton("Get JSON");

        ListModel shapesListModel = new AbstractListModel<List<Long>>() {
            @Override
            public int getSize() {
                System.out.println(((LevelMap) levelPanel).getShapes().size());
                return ((LevelMap) levelPanel).getShapes().size();
            }

            @Override
            public List<Long> getElementAt(int index) {
                return (List<Long>) ((LevelMap) levelPanel).getShapes().get(index);
            }
        };

        shapesList = new JList<>(shapesListModel);
        shapesList.setModel(shapesListModel);
        shapesList.setCellRenderer(this);
    }

    public void setCoordinates(int x, int y) {
        coordinatesLabel.setText(x + ":" + y);
    }

    public int getMode() {
        if (createShapesModeCheckBox.isSelected()) {
            return MODE_SHAPE;
        }

        return MODE_NONE;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel l = new JLabel();
        l.setText("223344");
        l.setOpaque(true);
        return l;
    }
}
