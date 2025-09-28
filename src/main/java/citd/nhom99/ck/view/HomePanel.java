package citd.nhom99.ck.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class HomePanel extends JPanel {
    
    public HomePanel() {
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        initializeComponents();
    }
    
    private void initializeComponents() {
        // Header section
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);
        
        // Main content section
        JPanel contentPanel = createContentPanel();
        add(contentPanel, BorderLayout.CENTER);
        
        // Footer section
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }
    
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(52, 58, 64));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));
        
        // Welcome text
        JLabel welcomeLabel = new JLabel("Chào mừng đến với Hệ thống Quản lý Học tập");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 28));
        welcomeLabel.setForeground(Color.WHITE);
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Subtitle
        JLabel subtitleLabel = new JLabel("Learning Management System - Trường THPT N99");
        subtitleLabel.setFont(new Font("Arial", Font.ITALIC, 16));
        subtitleLabel.setForeground(new Color(200, 200, 200));
        subtitleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(welcomeLabel, BorderLayout.CENTER);
        panel.add(subtitleLabel, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createContentPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 20, 20));
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        
        // Feature cards
        panel.add(createFeatureCard("Quản lý Học sinh", 
            "Xem thông tin, điểm số và thời khóa biểu của học sinh", 
            new Color(52, 144, 220)));
            
        panel.add(createFeatureCard("Quản lý Giáo viên", 
            "Nhập điểm, xem lịch dạy và quản lý lớp học", 
            new Color(40, 167, 69)));
            
        panel.add(createFeatureCard("Báo cáo & Thống kê", 
            "Theo dõi tiến độ học tập và kết quả học tập", 
            new Color(255, 193, 7)));
            
        panel.add(createFeatureCard("Quản lý Hệ thống", 
            "Cấu hình lớp học, môn học và phân quyền người dùng", 
            new Color(220, 53, 69)));
        
        return panel;
    }
    
    private JPanel createFeatureCard(String title, String description, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Create icon panel
        JPanel iconPanel = createIconPanel(color);
        
        // Title with icon
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
        titleLabel.setForeground(color);
        
        titlePanel.add(iconPanel, BorderLayout.WEST);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        // Description
        JLabel descLabel = new JLabel("<html><div style='width: 200px; text-align: justify;'>" + description + "</div></html>");
        descLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        descLabel.setForeground(new Color(100, 100, 100));
        
        card.add(titlePanel, BorderLayout.NORTH);
        card.add(descLabel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createIconPanel(Color color) {
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw a simple circle icon
                g2d.setColor(color);
                g2d.fillOval(5, 5, 20, 20);
                
                // Draw a smaller inner circle
                g2d.setColor(Color.WHITE);
                g2d.fillOval(10, 10, 10, 10);
                
                g2d.dispose();
            }
        };
        iconPanel.setPreferredSize(new Dimension(30, 30));
        iconPanel.setBackground(Color.WHITE);
        return iconPanel;
    }
    
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(52, 58, 64));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        
        // Current date
        String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        JLabel dateLabel = new JLabel("Hôm nay: " + currentDate);
        dateLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        dateLabel.setForeground(Color.WHITE);
        
        // System info
        JLabel systemLabel = new JLabel("Hệ thống LMS v1.0 - © 2025 Trường THPT N99");
        systemLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        systemLabel.setForeground(new Color(200, 200, 200));
        systemLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        
        panel.add(dateLabel, BorderLayout.WEST);
        panel.add(systemLabel, BorderLayout.EAST);
        
        return panel;
    }
}
