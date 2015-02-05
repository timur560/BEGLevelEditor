import javax.swing.*;
import java.awt.*;

/**
 * Created by timur on 03.02.15.
 */
public class BEGLevelEditor {
    public static void main(String[] args) {
        JFrame frame = new JFrame("MainFrame");
        frame.setContentPane(new MainFrame().contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        // frame.setSize(size.width, size.height - 20);
        frame.setSize(1300, size.height - 20);
        frame.setVisible(true);
    }

}
