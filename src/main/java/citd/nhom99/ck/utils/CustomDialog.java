package citd.nhom99.ck.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CustomDialog {
    
    public static void showWarningDialog(Component parent, String message, String title) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), title, true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(parent);
        
        // Main panel with border
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Icon panel
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        
        // Warning icon
        JLabel iconLabel = new JLabel("⚠️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 32));
        iconLabel.setForeground(new Color(255, 193, 7));
        iconPanel.add(iconLabel);
        
        // Message panel
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(Color.WHITE);
        
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(73, 80, 87));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        okButton.setBackground(new Color(255, 193, 7));
        okButton.setForeground(Color.WHITE);
        okButton.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        okButton.setFocusPainted(false);
        okButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        okButton.setPreferredSize(new Dimension(80, 35));
        
        // Add hover effect
        okButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                okButton.setBackground(new Color(230, 162, 0));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                okButton.setBackground(new Color(255, 193, 7));
            }
        });
        
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        buttonPanel.add(okButton);
        
        // Add panels to main panel
        mainPanel.add(iconPanel, BorderLayout.WEST);
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    public static void showInfoDialog(Component parent, String message, String title) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), title, true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setSize(400, 200);
        dialog.setLocationRelativeTo(parent);
        
        // Main panel with border
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Icon panel
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        
        // Info icon
        JLabel iconLabel = new JLabel("ℹ️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 32));
        iconLabel.setForeground(new Color(52, 144, 220));
        iconPanel.add(iconLabel);
        
        // Message panel
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(Color.WHITE);
        
        JLabel messageLabel = new JLabel("<html><div style='text-align: center;'>" + message + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(73, 80, 87));
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton cancelButton = new JButton("Hủy");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setPreferredSize(new Dimension(80, 35));
        
        // Add hover effect
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(90, 98, 104));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(108, 117, 125));
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        buttonPanel.add(cancelButton);
        
        // Add panels to main panel
        mainPanel.add(iconPanel, BorderLayout.WEST);
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
    
    public static boolean showConfirmDialog(Component parent, String message, String title) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), title, true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(false);
        dialog.setSize(450, 300);
        dialog.setLocationRelativeTo(parent);
        
        // Main panel with border
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Icon panel
        JPanel iconPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        iconPanel.setBackground(Color.WHITE);
        iconPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 20));
        
        // Warning icon
        JLabel iconLabel = new JLabel("⚠️");
        iconLabel.setFont(new Font("Segoe UI Emoji", Font.BOLD, 32));
        iconLabel.setForeground(new Color(255, 193, 7));
        iconPanel.add(iconLabel);
        
        // Message panel
        JPanel messagePanel = new JPanel(new BorderLayout());
        messagePanel.setBackground(Color.WHITE);
        
        JLabel messageLabel = new JLabel("<html><div style='text-align: left;'>" + message.replace("\n", "<br>") + "</div></html>");
        messageLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        messageLabel.setForeground(new Color(73, 80, 87));
        messageLabel.setHorizontalAlignment(SwingConstants.LEFT);
        messageLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        messagePanel.add(messageLabel, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setBackground(Color.WHITE);
        
        // Confirm button
        JButton confirmButton = new JButton("Xác nhận");
        confirmButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        confirmButton.setBackground(new Color(220, 53, 69));
        confirmButton.setForeground(Color.WHITE);
        confirmButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        confirmButton.setFocusPainted(false);
        confirmButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        confirmButton.setPreferredSize(new Dimension(100, 35));
        
        // Cancel button
        JButton cancelButton = new JButton("Hủy bỏ");
        cancelButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        cancelButton.setBackground(new Color(108, 117, 125));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        cancelButton.setFocusPainted(false);
        cancelButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        cancelButton.setPreferredSize(new Dimension(100, 35));
        
        // Add hover effects
        confirmButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                confirmButton.setBackground(new Color(200, 35, 51));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                confirmButton.setBackground(new Color(220, 53, 69));
            }
        });
        
        cancelButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(90, 98, 104));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                cancelButton.setBackground(new Color(108, 117, 125));
            }
        });
        
        // Result variable
        final boolean[] result = {false};
        
        confirmButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result[0] = true;
                dialog.dispose();
            }
        });
        
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                result[0] = false;
                dialog.dispose();
            }
        });
        
        buttonPanel.add(confirmButton);
        buttonPanel.add(cancelButton);
        
        // Add panels to main panel
        mainPanel.add(iconPanel, BorderLayout.WEST);
        mainPanel.add(messagePanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
        
        return result[0];
    }
    
    
    public static void showScheduleDialog(Component parent, java.util.List<citd.nhom99.ck.model.Schedule> schedules, String className) {
        JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(parent), "Thời khóa biểu lớp " + className, true);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setResizable(true);
        dialog.setSize(800, 600);
        dialog.setLocationRelativeTo(parent);
        
        // Main panel
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(Color.WHITE);
        
        JLabel titleLabel = new JLabel("📅 Thời khóa biểu lớp " + className);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titleLabel.setForeground(new Color(52, 144, 220));
        headerPanel.add(titleLabel);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Schedule table
        String[] columnNames = {"Thứ", "Tiết 1", "Tiết 2", "Tiết 3", "Tiết 4", "Tiết 5", "Tiết 6", "Tiết 7", "Tiết 8"};
        String[] dayNames = {"Thứ 2", "Thứ 3", "Thứ 4", "Thứ 5", "Thứ 6", "Thứ 7", "Chủ nhật"};
        String[] days = {"MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY", "SATURDAY", "SUNDAY"};
        
        Object[][] data = new Object[7][9];
        
        // Initialize with day names
        for (int i = 0; i < 7; i++) {
            data[i][0] = dayNames[i];
            for (int j = 1; j < 9; j++) {
                data[i][j] = "";
            }
        }
        
        // Fill schedule data
        if (schedules != null) {
            for (citd.nhom99.ck.model.Schedule schedule : schedules) {
                int dayIndex = -1;
                for (int i = 0; i < days.length; i++) {
                    if (days[i].equals(schedule.getDayOfWeek())) {
                        dayIndex = i;
                        break;
                    }
                }
                
                if (dayIndex >= 0 && schedule.getPeriod() >= 1 && schedule.getPeriod() <= 8) {
                    String subjectName = schedule.getSubject() != null ? 
                        schedule.getSubject().getSubjectName() : "Môn " + schedule.getSubjectId();
                    String teacherName = schedule.getTeacher() != null ? 
                        schedule.getTeacher().getUser().getFullName() : "GV " + schedule.getTeacherId();
                    
                    // Convert subject name to Vietnamese
                    String vietnameseSubjectName = SubjectTranslator.convertToVietnamese(subjectName);
                    
                    data[dayIndex][schedule.getPeriod()] = "<html><div style='text-align: center;'>" +
                        "<b>" + vietnameseSubjectName + "</b><br>" +
                        "<small>" + teacherName + "</small></div></html>";
                }
            }
        }
        
        JTable scheduleTable = new JTable(data, columnNames);
        scheduleTable.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        scheduleTable.setRowHeight(60);
        scheduleTable.setGridColor(new Color(200, 200, 200));
        scheduleTable.setShowGrid(true);
        scheduleTable.setEnabled(false); // Read-only
        
        // Style the table
        scheduleTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        scheduleTable.getTableHeader().setBackground(new Color(52, 144, 220));
        scheduleTable.getTableHeader().setForeground(Color.WHITE);
        scheduleTable.getTableHeader().setReorderingAllowed(false);
        
        // Center align all cells
        javax.swing.table.DefaultTableCellRenderer centerRenderer = new javax.swing.table.DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < scheduleTable.getColumnCount(); i++) {
            scheduleTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        // Style the first column (day names)
        javax.swing.table.DefaultTableCellRenderer dayRenderer = new javax.swing.table.DefaultTableCellRenderer();
        dayRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        dayRenderer.setBackground(new Color(240, 248, 255));
        dayRenderer.setFont(new Font("Segoe UI", Font.BOLD, 12));
        scheduleTable.getColumnModel().getColumn(0).setCellRenderer(dayRenderer);
        
        JScrollPane scrollPane = new JScrollPane(scheduleTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        scrollPane.setBackground(Color.WHITE);
        
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        
        JButton closeButton = new JButton("Đóng");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        closeButton.setBackground(new Color(108, 117, 125));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBorder(BorderFactory.createEmptyBorder(8, 24, 8, 24));
        closeButton.setFocusPainted(false);
        closeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        closeButton.setPreferredSize(new Dimension(100, 35));
        
        // Add hover effect
        closeButton.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                closeButton.setBackground(new Color(90, 98, 104));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                closeButton.setBackground(new Color(108, 117, 125));
            }
        });
        
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel);
        dialog.setVisible(true);
    }
}
