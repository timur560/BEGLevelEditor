import javax.swing.*;

/**
 * Created by timur on 03.02.15.
 */
public class BEGLevelEditor {
    public static void main(String[] args) {
        JFrame frame = new JFrame("MainFrame");
        frame.setContentPane(new MainFrame().contentPane);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setVisible(true);
    }

}
