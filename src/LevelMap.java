import org.stringtree.json.JSONReader;
import org.stringtree.json.JSONWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

/**
 * Created by timur on 03.02.15.
 */
public class LevelMap extends JPanel {

    public int cellSize = 30;
    private int width = 40,
            height = 20,
            mouseX, mouseY,
            startX, startY,
            endX, endY;
    private boolean mousePressed = false;

    private MainFrame frame;

    public LevelMap(MainFrame f) {
        super();

        frame = f;

        setPreferredSize(new Dimension(width * cellSize, height * cellSize));

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
                mousePressed = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                mousePressed = false;
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

        Graphics2D g2 = (Graphics2D) g;

        // fill background
        if (frame.cover == null) {
            g2.setColor(Color.lightGray);
            g2.fillRect(0, 0, width * cellSize, height * cellSize);
        } else {
            g2.drawImage(frame.cover, 0, 0, null);
        }

        int i;

        // draw shapes
        for (i = 0; i < frame.shapes.getModel().getSize(); i++) {
            String el = ((DefaultListModel) frame.shapes.getModel()).get(i).toString();
            JSONReader reader = new JSONReader();
            List<Long> s = (List<Long>)reader.read(el);

            g2.setColor(Color.green);
            g2.fillRect(
                    s.get(0).intValue() * cellSize,
                    (s.get(1).intValue() - 1) * cellSize,
                    (s.get(2).intValue() - s.get(0).intValue()) * cellSize,
                    (s.get(3).intValue() - s.get(1).intValue() + 1) * cellSize
            );

            if (frame.shapes.getSelectedIndex() == i) {
                g2.setColor(Color.red);
                g2.setStroke(new BasicStroke(5.0f));
                g2.drawRect(
                        s.get(0).intValue() * cellSize,
                        (s.get(1).intValue() - 1) * cellSize,
                        (s.get(2).intValue() - s.get(0).intValue()) * cellSize,
                        (s.get(3).intValue() - s.get(1).intValue() + 1) * cellSize
                );
                g2.setStroke(new BasicStroke(1.0f));
            }
        }

        // draw grid
        g.setColor(Color.black);

        for (i = 0; i <= width; i++) {
            g.drawLine(i * cellSize, 0, i * cellSize, height * cellSize);
        }

        for (i = 0; i <= height; i++) {
            g.drawLine(0, i * cellSize, width * cellSize, i * cellSize);
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
        int[] c;
        if (mousePressed) {
            c = new int[]{startX - startX % cellSize, startY - startY % cellSize};
            g.fillRect(c[0], c[1], cellSize * (1 + (mouseX - startX) / cellSize), cellSize * (1 + (mouseY - startY) / cellSize));
            frame.setCoordinates(1 + (mouseX - startX) / cellSize, 1 + (mouseY - startY) / cellSize);
        } else {
            c = new int[]{mouseX - mouseX % cellSize, mouseY - mouseY % cellSize};
            g.fillRect(c[0], c[1], cellSize, cellSize);
            frame.setCoordinates(c[0] / cellSize, c[1] / cellSize);
        }

    }

    public void completeObject() {
        switch (frame.getMode()) {
            case MainFrame.MODE_NONE :
                return;
            case MainFrame.MODE_SHAPE:
                if (startX / cellSize > endX / cellSize || startY / cellSize > endY / cellSize) return;
                List<Long> l = new ArrayList<Long>();
                l.add((long) startX / cellSize);
                l.add((long) startY / cellSize + 1);
                l.add((long) endX / cellSize + 1);
                l.add((long) endY / cellSize + 1);
                JSONWriter writer = new JSONWriter();
                ((DefaultListModel) frame.shapes.getModel()).addElement(writer.write(l));
//                System.out.println(shapes);
                break;
        }
    }

    public void setWidth(int widthPx) {
        width = widthPx / cellSize;
        setPreferredSize(new Dimension(width * cellSize, height * cellSize));
    }

    public void setHeight(int heightPx) {
        height = heightPx / cellSize;
        setPreferredSize(new Dimension(width * cellSize, height * cellSize));
    }
}
