package citd.nhom99.ck.view;

import citd.nhom99.ck.controller.AppController;
import citd.nhom99.ck.model.User;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {
    private final SidebarPanel sidebar;
    private final MainContentPanel mainContent;

    public DashboardFrame(User user, AppController controller) {
        setTitle("Trường THPT N99 - LMS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1200, 800);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        sidebar = new SidebarPanel(this, user, controller);
        mainContent = new MainContentPanel(user, controller);

        add(sidebar, BorderLayout.WEST);
        add(mainContent, BorderLayout.CENTER);
    }

    public void updateMainContent(String text) {
        this.mainContent.updateMainContent(text);
    }
}
