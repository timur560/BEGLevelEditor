import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Created by timur on 03.02.15.
 */
public class LevelMap extends JPanel {

    private List<List<Long>> shapes = new ArrayList<>();
    private int cellSize = 30,
            width = 20,
            height = 20,
            mouseX, mouseY,
            startX, startY,
            endX, endY;

    private MainFrame frame;

    public LevelMap(MainFrame f) {
        super();

        frame = f;

        addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                super.mouseMoved(e);
                mouseX = e.getX();
                mouseY = e.getY();
                repaint();
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                super.mouseMoved(e);
                mouseX = e.getX();
                mouseY = e.getY();
                repaint();
            }

        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                startX = e.getX();
                startY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                super.mouseReleased(e);
                endX = e.getX();
                endY = e.getY();
                completeObject();
            }
        });

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // fill background
        g.setColor(Color.lightGray);
        g.fillRect(0, 0, width * cellSize, height * cellSize);

        // draw shapes, enemies, etc.
        g.setColor(Color.green);
        for (List<Long> s : shapes) {
            g.fillRect(
                    s.get(0).intValue() * cellSize,
                    (s.get(1).intValue() - 1) * cellSize,
                    (s.get(2).intValue() - s.get(0).intValue()) * cellSize,
                    (s.get(3).intValue() - s.get(1).intValue() + 1) * cellSize
            );
        }

        // draw grid
        g.setColor(Color.black);
        int i;
        for (i = 0; i <= width; i++) {
            g.drawLine(0, i * cellSize, width * cellSize, i * cellSize);
        }

        for (i = 0; i <= height; i++) {
            g.drawLine(i * cellSize, 0, i * cellSize, height * cellSize);
        }

        // fill cell under mouse in color
        switch (frame.getMode()) {
            case MainFrame.MODE_SHAPE:
                g.setColor(Color.green);
                break;
            default:
                g.setColor(Color.gray);
                break;
        }

        // coordinate
        int[] c = new int[]{mouseX - mouseX % cellSize, mouseY - mouseY % cellSize};
        g.fillRect(c[0], c[1], cellSize, cellSize);
        frame.setCoordinates(c[0] / cellSize, c[1] / cellSize);
    }

    public void completeObject() {
        switch (frame.getMode()) {
            case MainFrame.MODE_NONE :
                return;
            case MainFrame.MODE_SHAPE:
                List<Long> l = new ArrayList<Long>();
                l.add((long) startX / cellSize);
                l.add((long) startY / cellSize + 1);
                l.add((long) endX / cellSize + 1);
                l.add((long) endY / cellSize + 1);
                shapes.add(l);
//                System.out.println(shapes);
                break;
        }
    }

    public List getShapes() {
        return shapes;
    }
}
