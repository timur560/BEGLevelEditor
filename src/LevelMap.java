import org.stringtree.json.JSONReader;
import org.stringtree.json.JSONWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.List;

/**
 * Created by timur on 03.02.15.
 */
public class LevelMap extends JPanel {

    public int cellSize = 30;
    private int width = 40,
            height = 20,
            mouseX, mouseY;
    public List<Integer> x = new ArrayList<>(), y = new ArrayList<>();

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

        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                x.add(e.getX());
                y.add(e.getY());
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
                g2.setStroke(new BasicStroke(4.0f));
                g2.drawRect(
                        s.get(0).intValue() * cellSize,
                        (s.get(1).intValue() - 1) * cellSize,
                        (s.get(2).intValue() - s.get(0).intValue()) * cellSize,
                        (s.get(3).intValue() - s.get(1).intValue() + 1) * cellSize
                );
                g2.setStroke(new BasicStroke(1.0f));
            }
        }

        // draw ladders
        for (i = 0; i < frame.ladders.getModel().getSize(); i++) {
            String el = ((DefaultListModel) frame.ladders.getModel()).get(i).toString();
            JSONReader reader = new JSONReader();
            List<Long> s = (List<Long>)reader.read(el);

            g2.setColor(Color.yellow);
            g2.fillRect(
                    s.get(0).intValue() * cellSize,
                    (s.get(1).intValue()) * cellSize,
                    cellSize,
                    (s.get(2).intValue()) * cellSize
            );

            if (frame.ladders.getSelectedIndex() == i) {
                g2.setColor(Color.red);
                g2.setStroke(new BasicStroke(4.0f));
                g2.drawRect(
                        s.get(0).intValue() * cellSize,
                        (s.get(1).intValue()) * cellSize,
                        cellSize,
                        (s.get(2).intValue()) * cellSize
                );
                g2.setStroke(new BasicStroke(1.0f));
            }
        }

        // draw enemies
        for (i = 0; i < frame.enemies.getModel().getSize(); i++) {
            String el = ((DefaultListModel) frame.enemies.getModel()).get(i).toString();
            JSONReader reader = new JSONReader();
            Map<String, Object> e = (Map<String, Object>) reader.read(el);

            g2.setColor(Color.red);
            g2.setStroke(new BasicStroke(4.0f));

            int j = 0;
            List<List<Long>> path = (List<List<Long>>)e.get("path");
            if (path.size() > 1) for (List<Long> point : path) {
                j++;
                if (j >= path.size()) j = 0;
                g2.drawLine(
                        point.get(0).intValue() * cellSize + cellSize / 2 - 2,
                        (point.get(1).intValue() - 1) * cellSize + cellSize / 2 - 2,
                        path.get(j).get(0).intValue() * cellSize + cellSize / 2 - 2,
                        (path.get(j).get(1).intValue() - 1) * cellSize + cellSize / 2 - 2
                );
            }

            g2.fillRect(
                    path.get(0).get(0).intValue() * cellSize + (int) (((List<Double>) e.get("rect")).get(2).floatValue() * cellSize),
                    (path.get(0).get(1).intValue() - 1) * cellSize + (int) (((List<Double>) e.get("rect")).get(3).floatValue() * cellSize),
                    (int) (cellSize * ((List<Double>) e.get("rect")).get(0).floatValue()),
                    (int) (cellSize * ((List<Double>) e.get("rect")).get(1).floatValue())
            );

            if (frame.enemies.getSelectedIndex() == i) {
                g2.setColor(Color.red);
                g2.setStroke(new BasicStroke(4.0f));
                g2.drawRect(
                        path.get(0).get(0).intValue() * cellSize,
                        (path.get(0).get(1).intValue() - 1) * cellSize,
                        cellSize, cellSize
                );
            }

        }
        g2.setStroke(new BasicStroke(1.0f));


        // draw moving platforms

        // draw moving blocks

        // hearts

        // portals

        // draw grid
        g.setColor(Color.black);

        for (i = 0; i <= width; i++) {
            g.drawLine(i * cellSize, 0, i * cellSize, height * cellSize);
        }

        for (i = 0; i <= height; i++) {
            g.drawLine(0, i * cellSize, width * cellSize, i * cellSize);
        }

        // fill control points
        g.setColor(Color.magenta);
        i = 0;
        for (int x : this.x) {
            g.fillOval(x - x % cellSize + cellSize / 2 - 10, y.get(i) - y.get(i) % cellSize + cellSize / 2 - 10, 20, 20);
            i++;
        }

        // fill cell under mouse in color
        switch (frame.mode) {
            case MainFrame.MODE_SHAPE:
                g.setColor(Color.green);
                break;
            case MainFrame.MODE_LADDER:
                g.setColor(Color.yellow);
                break;
            case MainFrame.MODE_ENEMY:
                g.setColor(Color.red);
                break;
            default:
                g.setColor(Color.gray);
                break;
        }

        // coordinate
        int[] c;
        c = new int[]{mouseX - mouseX % cellSize, mouseY - mouseY % cellSize};
        g.fillRect(c[0], c[1], cellSize, cellSize);
        frame.setCoordinates(c[0] / cellSize, c[1] / cellSize);

    }

    public void completeObject() {
        JSONWriter writer = new JSONWriter();
        List<Long> l = new ArrayList<>();
        List<List<Long>> path = new ArrayList<>();
        int i = 0;
        switch (frame.mode) {
            case MainFrame.MODE_NONE :
                return;

            case MainFrame.MODE_SHAPE:
                if (x.get(0) / cellSize > x.get(1) / cellSize || y.get(0) / cellSize > y.get(1) / cellSize) return;
                l.add((long) x.get(0) / cellSize);
                l.add((long) y.get(0) / cellSize + 1);
                l.add((long) x.get(1) / cellSize + 1);
                l.add((long) y.get(1) / cellSize + 1);
                ((DefaultListModel) frame.shapes.getModel()).addElement(writer.write(l));
                break;

            case MainFrame.MODE_LADDER:
                if (x.get(0) / cellSize > x.get(1) / cellSize || y.get(0) / cellSize > y.get(1) / cellSize) return;
                l.add((long) x.get(0) / cellSize);
                l.add((long) y.get(0) / cellSize);
                l.add((long) y.get(1) / cellSize - y.get(0) / cellSize + 1);
                ((DefaultListModel) frame.ladders.getModel()).addElement(writer.write(l));
                break;

            case MainFrame.MODE_ENEMY:
                Map<String, Object> enemy = new HashMap<>();

                enemy.put("type", frame.enemyType.getSelectedItem().toString());

                // path
                i = 0;
                for (int x : this.x) {
                    path.add(Arrays.asList((long) x / cellSize, (long) y.get(i) / cellSize + 1));
                    i++;
                }
                enemy.put("path", path);

                // rect
                enemy.put("rect", Arrays.asList(
                        Double.valueOf(frame.enemyWidth.getText()),
                        Double.valueOf(frame.enemyHeight.getText()),
                        Double.valueOf(frame.enemyLeft.getText()),
                        Double.valueOf(frame.enemyTop.getText())));

                enemy.put("canDie", Boolean.valueOf(frame.enemyCanDie.isSelected()));
                enemy.put("weapon", Boolean.valueOf(frame.enemyWeapon.isSelected()));

                if (frame.enemyWeapon.isSelected()) {
                    enemy.put("shootDelay", Long.valueOf(frame.enemyShootDelay.getText()));
                }

                if (path.size() <= 1) { // if it is static
                    enemy.put("direction", Long.valueOf(frame.enemyDirection.getSelectedItem().toString()));
                } else { // if moving
                    enemy.put("speed", Double.valueOf(frame.enemySpeed.getText()));
                }

                ((DefaultListModel) frame.enemies.getModel()).addElement(writer.write(enemy));
                break;
        }

        x.clear();
        y.clear();
        repaint();
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
