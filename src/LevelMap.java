import org.stringtree.json.JSONReader;
import org.stringtree.json.JSONWriter;

import javax.swing.*;
import java.awt.*;
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
    public int width = 40,
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
        g2.setColor(Color.black);
        g2.fillRect(0, 0, width * cellSize, height * cellSize);

        if (frame.cover != null) {
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
                        (point.get(1).intValue()) * cellSize + cellSize / 2 - 2,
                        path.get(j).get(0).intValue() * cellSize + cellSize / 2 - 2,
                        (path.get(j).get(1).intValue()) * cellSize + cellSize / 2 - 2
                );
            }

            g2.fillRect(
                    path.get(0).get(0).intValue() * cellSize + (int) (((List<Double>) e.get("rect")).get(2).floatValue() * cellSize),
                    (path.get(0).get(1).intValue()) * cellSize + (int) (((List<Double>) e.get("rect")).get(3).floatValue() * cellSize),
                    (int) (cellSize * ((List<Double>) e.get("rect")).get(0).floatValue()),
                    (int) (cellSize * ((List<Double>) e.get("rect")).get(1).floatValue())
            );

            if (frame.enemies.getSelectedIndex() == i) {
                g2.setColor(Color.red);
                g2.setStroke(new BasicStroke(4.0f));
                g2.drawRect(
                        path.get(0).get(0).intValue() * cellSize,
                        (path.get(0).get(1).intValue()) * cellSize,
                        cellSize, cellSize
                );
            }

        }
        g2.setStroke(new BasicStroke(1.0f));

        // draw moving platforms
        for (i = 0; i < frame.mp.getModel().getSize(); i++) {
            String el = ((DefaultListModel) frame.mp.getModel()).get(i).toString();
            JSONReader reader = new JSONReader();
            Map<String, Object> mp = (Map<String, Object>) reader.read(el);

            g2.setColor(Color.blue);
            g2.setStroke(new BasicStroke(4.0f));

            int j = 0;
            List<List<Long>> path = (List<List<Long>>)mp.get("path");
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

            g2.setColor(Color.green);
            g2.fillRect(
                    path.get(0).get(0).intValue() * cellSize,
                    (path.get(0).get(1).intValue() - 1) * cellSize + cellSize / 2,
                    cellSize * ((Long) mp.get("width")).intValue(),
                    cellSize / 2
            );

            if (frame.mp.getSelectedIndex() == i) {
                g2.setColor(Color.red);
                g2.setStroke(new BasicStroke(4.0f));
                g2.drawRect(
                        path.get(0).get(0).intValue() * cellSize,
                        (path.get(0).get(1).intValue() - 1) * cellSize,
                        cellSize * ((Long) mp.get("width")).intValue(), cellSize
                );
            }

        }
        g2.setStroke(new BasicStroke(1.0f));

        // draw moving blocks
        for (i = 0; i < frame.movingBlocks.getModel().getSize(); i++) {
            String el = ((DefaultListModel) frame.movingBlocks.getModel()).get(i).toString();
            JSONReader reader = new JSONReader();
            List<Long> s = (List<Long>)reader.read(el);

            g2.setColor(Color.blue);
            g2.fillRect(
                    s.get(0).intValue() * cellSize,
                    (s.get(1).intValue() - 1) * cellSize,
                    cellSize, cellSize
            );

            if (frame.movingBlocks.getSelectedIndex() == i) {
                g2.setColor(Color.red);
                g2.setStroke(new BasicStroke(4.0f));
                g2.drawRect(
                        s.get(0).intValue() * cellSize,
                        (s.get(1).intValue() - 1) * cellSize,
                        cellSize, cellSize
                );
                g2.setStroke(new BasicStroke(1.0f));
            }
        }

        // draw hearts
        for (i = 0; i < frame.hearts.getModel().getSize(); i++) {
            String el = ((DefaultListModel) frame.hearts.getModel()).get(i).toString();
            JSONReader reader = new JSONReader();
            Map<String, Object> s = (Map<String, Object>)reader.read(el);

            g2.setColor(Color.magenta);
            if (((String) s.get("secret")).equals("snowheap")) {
                g2.fillRect(
                        ((List<Long>) s.get("pos")).get(0).intValue() * cellSize,
                        (((List<Long>) s.get("pos")).get(1).intValue()) * cellSize + cellSize / 2,
                        cellSize, cellSize / 2);
            } else {
                g2.fillRect(
                        ((List<Long>) s.get("pos")).get(0).intValue() * cellSize,
                        (((List<Long>) s.get("pos")).get(1).intValue()) * cellSize,
                        cellSize, cellSize);
            }

            if ((boolean) s.get("fake")) {
                g2.setColor(Color.black);
                g2.drawString("F",
                        ((List<Long>)s.get("pos")).get(0).intValue() * cellSize + 2,
                        (((List<Long>)s.get("pos")).get(1).intValue() + 1) * cellSize);
            }

            if (frame.movingBlocks.getSelectedIndex() == i) {
                g2.setColor(Color.red);
                g2.setStroke(new BasicStroke(4.0f));
                g2.drawRect(
                        ((List<Long>)s.get("pos")).get(0).intValue() * cellSize,
                        (((List<Long>)s.get("pos")).get(1).intValue()) * cellSize,
                        cellSize, cellSize
                );
                g2.setStroke(new BasicStroke(1.0f));
            }
        }

        // draw portals
        for (i = 0; i < frame.portals.getModel().getSize(); i++) {
            String el = ((DefaultListModel) frame.portals.getModel()).get(i).toString();
            JSONReader reader = new JSONReader();
            Map<String, Object> s = (Map<String, Object>)reader.read(el);

            g2.setColor(Color.cyan);
            g2.fillRect(
                    ((List<Long>) s.get("pos")).get(0).intValue() * cellSize,
                    (((List<Long>) s.get("pos")).get(1).intValue()) * cellSize,
                    cellSize, cellSize);


            List<Long> wall = (List<Long>) s.get("wall");
            if (wall != null) {
                g2.fillRect(
                        wall.get(0).intValue() * cellSize + cellSize / 4,
                        (wall.get(1).intValue()) * cellSize,
                        cellSize / 2,
                        (wall.get(2).intValue()) * cellSize);

                List<Long> terminal = (List<Long>) s.get("terminal");

                g2.fillRect(
                        terminal.get(0).intValue() * cellSize,
                        (terminal.get(1).intValue()) * cellSize,
                        cellSize,
                        cellSize);

                g2.setColor(Color.black);
                g2.drawString("T" + i,
                        terminal.get(0).intValue() * cellSize + 2,
                        (terminal.get(1).intValue() + 1) * cellSize);

                g2.drawString("P" + i,
                        ((List<Long>) s.get("pos")).get(0).intValue() * cellSize + 2,
                        (((List<Long>) s.get("pos")).get(1).intValue() + 1) * cellSize);
            }

            if (frame.portals.getSelectedIndex() == i) {
                g2.setColor(Color.red);
                g2.setStroke(new BasicStroke(4.0f));
                g2.drawRect(
                        ((List<Long>)s.get("pos")).get(0).intValue() * cellSize,
                        (((List<Long>)s.get("pos")).get(1).intValue()) * cellSize,
                        cellSize, cellSize
                );
                g2.setStroke(new BasicStroke(1.0f));
            }
        }

        // draw grid
        g.setColor(Color.gray);

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
            case MainFrame.MODE_MP:
                g.setColor(Color.green);
                break;
            case MainFrame.MODE_LADDER:
                g.setColor(Color.yellow);
                break;
            case MainFrame.MODE_ENEMY:
                g.setColor(Color.red);
                break;
            case MainFrame.MODE_MB:
                g.setColor(Color.blue);
                break;
            case MainFrame.MODE_HEART:
                g.setColor(Color.magenta);
                break;
            case MainFrame.MODE_PORTAL:
                g.setColor(Color.cyan);
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
                    path.add(Arrays.asList((long) x / cellSize, (long) y.get(i) / cellSize));
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

            case MainFrame.MODE_MP:
                Map<String, Object> mp = new HashMap<>();

                // path
                i = 0;
                for (int x : this.x) {
                    path.add(Arrays.asList((long) x / cellSize, (long) y.get(i) / cellSize + 1));
                    i++;
                }
                mp.put("path", path);

                mp.put("width", Long.valueOf(frame.mpWidth.getText()));
                mp.put("speed", Double.valueOf(frame.mpSpeed.getText()));

                ((DefaultListModel) frame.mp.getModel()).addElement(writer.write(mp));

                break;

            case MainFrame.MODE_MB:
                i = 0;
                for (long xt : x) {
                    ((DefaultListModel) frame.movingBlocks.getModel()).addElement(writer.write(
                            Arrays.asList(xt / cellSize, y.get(i).longValue() / cellSize + 1)
                    ));
                    i++;
                }
                break;

            case MainFrame.MODE_HEART:
                if (x.size() < 1) break;
                Map<String, Object> h = new HashMap<>();
                l.add(x.get(0).longValue() / cellSize);
                l.add(y.get(0).longValue() / cellSize);

                h.put("pos", l);
                h.put("secret", frame.secret.getSelectedItem().toString());
                h.put("fake", frame.fakeCheckBox.isSelected());

                ((DefaultListModel) frame.hearts.getModel()).addElement(writer.write(h));

                break;

            case MainFrame.MODE_PORTAL:
                if (x.size() < 1) break;
                Map<String, Object> p = new HashMap<>();
                l.add(x.get(0).longValue() / cellSize);
                l.add(y.get(0).longValue() / cellSize);

                p.put("pos", l);

                if (x.size() == 4 && y.get(1) < y.get(2) ) {
                    p.put("wall", Arrays.asList(
                            x.get(1) / cellSize,
                            y.get(1) / cellSize,
                            y.get(2) / cellSize - y.get(1) / cellSize + 1
                    ));

                    p.put("terminal", Arrays.asList(
                            x.get(3) / cellSize,
                            y.get(3) / cellSize
                    ));
                }

                p.put("dest", Arrays.asList(
                        Long.valueOf(frame.level.getText()),
                        Long.valueOf(frame.zone.getText()),
                        Long.valueOf(frame.portal.getText())
                ));

                ((DefaultListModel) frame.portals.getModel()).addElement(writer.write(p));

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
