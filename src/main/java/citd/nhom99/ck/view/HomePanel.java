package citd.nhom99.ck.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import citd.nhom99.ck.config.DBConfig;
import citd.nhom99.ck.model.User;
import citd.nhom99.ck.model.constant.Role;

public class HomePanel extends JPanel {
    private User currentUser;
    private java.util.function.Consumer<String> onNavigateToPanel;
    
    public HomePanel() {
        this(null, null);
    }
    
    public HomePanel(User user, java.util.function.Consumer<String> onNavigateToPanel) {
        this.currentUser = user;
        this.onNavigateToPanel = onNavigateToPanel;
        
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
        JLabel welcomeLabel = new JLabel("Chào mừng đến với Hệ thống quản lý lớp học");
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
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        
        // Welcome message based on user role
        JPanel welcomePanel = createWelcomePanel();
        panel.add(welcomePanel, BorderLayout.NORTH);
        
        // Quick access buttons for admin
        if (currentUser != null && currentUser.getRole() == Role.ADMIN) {
            // Create main content area with charts and quick access
            JPanel mainContentPanel = new JPanel(new BorderLayout());
            mainContentPanel.setBackground(new Color(248, 249, 250));
            
            // Add charts panel
            JPanel chartsPanel = createChartsPanel();
            mainContentPanel.add(chartsPanel, BorderLayout.CENTER);
            
            // Add smaller quick access buttons at the bottom
            JPanel quickAccessPanel = createCompactQuickAccessPanel();
            mainContentPanel.add(quickAccessPanel, BorderLayout.SOUTH);
            
            panel.add(mainContentPanel, BorderLayout.CENTER);
        } else {
            // Default feature cards for other roles
            JPanel featurePanel = new JPanel(new GridLayout(2, 2, 20, 20));
            featurePanel.setBackground(new Color(248, 249, 250));
            
            featurePanel.add(createFeatureCard("Quản lý Học sinh", 
                "Xem thông tin, điểm số và thời khóa biểu của học sinh", 
                new Color(52, 144, 220)));
                
            featurePanel.add(createFeatureCard("Quản lý Giáo viên", 
                "Nhập điểm, xem lịch dạy và quản lý lớp học", 
                new Color(40, 167, 69)));
                
            featurePanel.add(createFeatureCard("Báo cáo & Thống kê", 
                "Theo dõi tiến độ học tập và kết quả học tập", 
                new Color(255, 193, 7)));
                
            featurePanel.add(createFeatureCard("Quản lý Hệ thống", 
                "Cấu hình lớp học, môn học và phân quyền người dùng", 
                new Color(220, 53, 69)));
            
            panel.add(featurePanel, BorderLayout.CENTER);
        }
        
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
    
    private JPanel createWelcomePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        String welcomeText = "Chào mừng bạn đến với hệ thống!";
        if (currentUser != null) {
            welcomeText = "Xin chào, " + currentUser.getFullName() + "!";
        }
        
        JLabel welcomeLabel = new JLabel(welcomeText);
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
        welcomeLabel.setForeground(new Color(52, 58, 64));
        welcomeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        panel.add(welcomeLabel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createChartsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        // Top row - Overview cards
        JPanel overviewPanel = createOverviewPanel();
        panel.add(overviewPanel, BorderLayout.NORTH);
        
        // Bottom row - Charts
        JPanel chartsRow = new JPanel(new GridLayout(1, 2, 20, 20));
        chartsRow.setBackground(new Color(248, 249, 250));
        chartsRow.setBorder(BorderFactory.createEmptyBorder(20, 0, 0, 0));
        
        chartsRow.add(createChartPanel("Thống kê Học sinh", new Color(52, 144, 220)));
        chartsRow.add(createChartPanel("Thống kê Giáo viên", new Color(40, 167, 69)));
        
        panel.add(chartsRow, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel createOverviewPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 15));
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
        
        // Get system statistics
        Map<String, Integer> systemStats = getSystemStatistics();
        
        // Create overview cards
        panel.add(createOverviewCard("Tổng Học sinh", systemStats.get("totalStudents"), "👥", new Color(52, 144, 220)));
        panel.add(createOverviewCard("Tổng Giáo viên", systemStats.get("totalTeachers"), "👨‍🏫", new Color(40, 167, 69)));
        panel.add(createOverviewCard("Tổng Lớp học", systemStats.get("totalClassrooms"), "🏫", new Color(255, 193, 7)));
        panel.add(createOverviewCard("Lớp có GVCN", systemStats.get("classroomsWithTeachers"), "✅", new Color(108, 117, 125)));
        
        return panel;
    }
    
    private JPanel createOverviewCard(String title, int value, String icon, Color color) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Icon and title panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        JLabel iconLabel = new JLabel(icon);
        iconLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        iconLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(new Color(100, 100, 100));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        headerPanel.add(iconLabel, BorderLayout.NORTH);
        headerPanel.add(titleLabel, BorderLayout.SOUTH);
        
        // Value panel
        JPanel valuePanel = new JPanel(new BorderLayout());
        valuePanel.setBackground(Color.WHITE);
        
        JLabel valueLabel = new JLabel(String.valueOf(value));
        valueLabel.setFont(new Font("Arial", Font.BOLD, 32));
        valueLabel.setForeground(color);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        valuePanel.add(valueLabel, BorderLayout.CENTER);
        
        card.add(headerPanel, BorderLayout.NORTH);
        card.add(valuePanel, BorderLayout.CENTER);
        
        return card;
    }
    
    private JPanel createChartPanel(String title, Color color) {
        JPanel chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);
        chartPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Title
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(color);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        chartPanel.add(titleLabel, BorderLayout.NORTH);
        
        // Get real data based on chart type
        String[] labels;
        int[] values;
        
        if (title.contains("Học sinh")) {
            Map<String, Integer> chartData = getStudentStatistics();
            labels = new String[]{"Lớp 10", "Lớp 11", "Lớp 12", "Tổng HS"};
            values = new int[]{
                chartData.getOrDefault("Lớp 10", 0),
                chartData.getOrDefault("Lớp 11", 0), 
                chartData.getOrDefault("Lớp 12", 0),
                chartData.getOrDefault("Tổng", 0)
            };
        } else {
            Map<String, Integer> chartData = getTeacherStatistics();
            labels = new String[]{"Lớp 10", "Lớp 11", "Lớp 12", "Tổng GV"};
            values = new int[]{
                chartData.getOrDefault("Lớp 10", 0),
                chartData.getOrDefault("Lớp 11", 0),
                chartData.getOrDefault("Lớp 12", 0), 
                chartData.getOrDefault("Tổng", 0)
            };
        }
        
        // Chart area with real data
        JPanel chartArea = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                
                // Draw background gradient
                java.awt.GradientPaint gradient = new java.awt.GradientPaint(
                    0, 0, new Color(248, 249, 250),
                    0, height, new Color(240, 242, 245)
                );
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, width, height);
                
                // Draw bar chart
                int barWidth = 40;
                int spacing = 20;
                int startX = 30;
                int baseY = height - 50;
                
                // Find max value for scaling
                int maxValue = 0;
                for (int value : values) {
                    if (value > maxValue) maxValue = value;
                }
                if (maxValue == 0) maxValue = 1; // Avoid division by zero
                
                for (int i = 0; i < values.length; i++) {
                    int barHeight = (int) ((double) values[i] / maxValue * (height - 120));
                    int x = startX + i * (barWidth + spacing);
                    int y = baseY - barHeight;
                    
                    // Create gradient for bars
                    Color lightColor = new Color(
                        Math.min(255, color.getRed() + 30),
                        Math.min(255, color.getGreen() + 30),
                        Math.min(255, color.getBlue() + 30)
                    );
                    java.awt.GradientPaint barGradient = new java.awt.GradientPaint(
                        x, y, lightColor,
                        x, y + barHeight, color
                    );
                    g2d.setPaint(barGradient);
                    
                    // Draw bar with rounded corners
                    g2d.fillRoundRect(x, y, barWidth, barHeight, 8, 8);
                    
                    // Draw bar border
                    g2d.setColor(new Color(color.getRed() - 20, color.getGreen() - 20, color.getBlue() - 20));
                    g2d.setStroke(new java.awt.BasicStroke(1));
                    g2d.drawRoundRect(x, y, barWidth, barHeight, 8, 8);
                    
                    // Draw value with better styling
                    g2d.setColor(new Color(60, 60, 60));
                    g2d.setFont(new Font("Arial", Font.BOLD, 12));
                    String valueText = String.valueOf(values[i]);
                    int textWidth = g2d.getFontMetrics().stringWidth(valueText);
                    g2d.drawString(valueText, x + (barWidth - textWidth) / 2, y - 8);
                    
                    // Draw label with better styling
                    g2d.setColor(new Color(100, 100, 100));
                    g2d.setFont(new Font("Arial", Font.PLAIN, 10));
                    int labelWidth = g2d.getFontMetrics().stringWidth(labels[i]);
                    g2d.drawString(labels[i], x + (barWidth - labelWidth) / 2, baseY + 18);
                }
                
                g2d.dispose();
            }
        };
        chartArea.setPreferredSize(new Dimension(300, 200));
        chartArea.setBackground(Color.WHITE);
        chartPanel.add(chartArea, BorderLayout.CENTER);
        
        return chartPanel;
    }
    
    private JPanel createCompactQuickAccessPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 4, 15, 15));
        panel.setBackground(new Color(248, 249, 250));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        
        // Compact quick access buttons for admin
        panel.add(createCompactQuickAccessButton("Quản lý Học sinh", 
            new Color(52, 144, 220), "Quản lý Học sinh"));
            
        panel.add(createCompactQuickAccessButton("Quản lý Giáo viên", 
            new Color(40, 167, 69), "Quản lý Giáo viên"));
            
        panel.add(createCompactQuickAccessButton("Quản lý Lớp học", 
            new Color(255, 193, 7), "Quản lý Lớp học"));
            
        panel.add(createCompactQuickAccessButton("Tài khoản", 
            new Color(220, 53, 69), "Tài khoản"));
        
        return panel;
    }
    
    private JPanel createCompactQuickAccessButton(String title, Color color, String panelName) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        
        // Create smaller icon panel
        JPanel iconPanel = createCompactIconPanel(color);
        
        // Title with icon
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(Color.WHITE);
        titlePanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 8, 0));
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 14));
        titleLabel.setForeground(color);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        
        titlePanel.add(iconPanel, BorderLayout.NORTH);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        
        // Action button
        JButton actionButton = new JButton("Truy cập");
        actionButton.setFont(new Font("Arial", Font.BOLD, 11));
        actionButton.setBackground(color);
        actionButton.setForeground(Color.WHITE);
        actionButton.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15));
        actionButton.setFocusPainted(false);
        actionButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        actionButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                actionButton.setBackground(new Color(
                    Math.max(0, color.getRed() - 20),
                    Math.max(0, color.getGreen() - 20),
                    Math.max(0, color.getBlue() - 20)
                ));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                actionButton.setBackground(color);
            }
        });
        
        // Add click action
        actionButton.addActionListener(e -> {
            if (onNavigateToPanel != null) {
                onNavigateToPanel.accept(panelName);
            }
        });
        
        card.add(titlePanel, BorderLayout.CENTER);
        card.add(actionButton, BorderLayout.SOUTH);
        
        return card;
    }
    
    private JPanel createCompactIconPanel(Color color) {
        JPanel iconPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Draw a smaller circle icon
                g2d.setColor(color);
                g2d.fillOval(8, 8, 16, 16);
                
                // Draw a smaller inner circle
                g2d.setColor(Color.WHITE);
                g2d.fillOval(12, 12, 8, 8);
                
                g2d.dispose();
            }
        };
        iconPanel.setPreferredSize(new Dimension(32, 32));
        iconPanel.setBackground(Color.WHITE);
        return iconPanel;
    }
    
    private Map<String, Integer> getStudentStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT " +
                    "CASE " +
                    "  WHEN c.class_name LIKE '10%' THEN 'Lớp 10' " +
                    "  WHEN c.class_name LIKE '11%' THEN 'Lớp 11' " +
                    "  WHEN c.class_name LIKE '12%' THEN 'Lớp 12' " +
                    "  ELSE 'Khác' " +
                    "END as grade_level, " +
                    "COUNT(*) as student_count " +
                    "FROM students s " +
                    "LEFT JOIN classrooms c ON s.class_id = c.class_id " +
                    "GROUP BY grade_level";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            int totalStudents = 0;
            while (rs.next()) {
                String gradeLevel = rs.getString("grade_level");
                int count = rs.getInt("student_count");
                stats.put(gradeLevel, count);
                totalStudents += count;
            }
            stats.put("Tổng", totalStudents);
            
        } catch (SQLException e) {
            System.out.println("Error getting student statistics: " + e.getMessage());
            // Return default values if error
            stats.put("Lớp 10", 0);
            stats.put("Lớp 11", 0);
            stats.put("Lớp 12", 0);
            stats.put("Tổng", 0);
        }
        
        return stats;
    }
    
    private Map<String, Integer> getTeacherStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        String sql = "SELECT " +
                    "CASE " +
                    "  WHEN c.class_name LIKE '10%' THEN 'Lớp 10' " +
                    "  WHEN c.class_name LIKE '11%' THEN 'Lớp 11' " +
                    "  WHEN c.class_name LIKE '12%' THEN 'Lớp 12' " +
                    "  ELSE 'Khác' " +
                    "END as grade_level, " +
                    "COUNT(DISTINCT t.user_id) as teacher_count " +
                    "FROM teachers t " +
                    "LEFT JOIN classrooms c ON t.classroom_id = c.class_id " +
                    "WHERE t.classroom_id > 0 " +
                    "GROUP BY grade_level";
        
        try (Connection conn = DBConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            
            int totalTeachers = 0;
            while (rs.next()) {
                String gradeLevel = rs.getString("grade_level");
                int count = rs.getInt("teacher_count");
                stats.put(gradeLevel, count);
                totalTeachers += count;
            }
            stats.put("Tổng", totalTeachers);
            
        } catch (SQLException e) {
            System.out.println("Error getting teacher statistics: " + e.getMessage());
            // Return default values if error
            stats.put("Lớp 10", 0);
            stats.put("Lớp 11", 0);
            stats.put("Lớp 12", 0);
            stats.put("Tổng", 0);
        }
        
        return stats;
    }
    
    private Map<String, Integer> getSystemStatistics() {
        Map<String, Integer> stats = new HashMap<>();
        
        try (Connection conn = DBConfig.getConnection()) {
            // Total students
            String studentSql = "SELECT COUNT(*) as total FROM students";
            try (PreparedStatement pstmt = conn.prepareStatement(studentSql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalStudents", rs.getInt("total"));
                }
            }
            
            // Total teachers
            String teacherSql = "SELECT COUNT(*) as total FROM teachers";
            try (PreparedStatement pstmt = conn.prepareStatement(teacherSql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalTeachers", rs.getInt("total"));
                }
            }
            
            // Total classrooms
            String classroomSql = "SELECT COUNT(*) as total FROM classrooms";
            try (PreparedStatement pstmt = conn.prepareStatement(classroomSql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("totalClassrooms", rs.getInt("total"));
                }
            }
            
            // Classrooms with homeroom teachers
            String classroomWithTeacherSql = "SELECT COUNT(*) as total FROM classrooms WHERE teacher_id > 0";
            try (PreparedStatement pstmt = conn.prepareStatement(classroomWithTeacherSql);
                 ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    stats.put("classroomsWithTeachers", rs.getInt("total"));
                }
            }
            
        } catch (SQLException e) {
            System.out.println("Error getting system statistics: " + e.getMessage());
            // Return default values if error
            stats.put("totalStudents", 0);
            stats.put("totalTeachers", 0);
            stats.put("totalClassrooms", 0);
            stats.put("classroomsWithTeachers", 0);
        }
        
        return stats;
    }
    
}
