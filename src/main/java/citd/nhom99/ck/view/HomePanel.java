package citd.nhom99.ck.view;

import javax.swing.*;

public class HomePanel extends JPanel {
    public HomePanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        JLabel welcomeLabel = new JLabel("Welcome to CK LMS");
        add(welcomeLabel);
    }
}
