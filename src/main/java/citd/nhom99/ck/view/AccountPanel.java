package citd.nhom99.ck.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import citd.nhom99.ck.controller.AppController;
import citd.nhom99.ck.model.User;
import citd.nhom99.ck.model.constant.Gender;

public class AccountPanel extends JPanel {
    private JTextField usernameField;
    private JTextField fullNameField;
    private JTextField emailField;
    private JTextField phoneField;
    private JComboBox<Gender> genderComboBox;
    private JTextField roleField;
    private JButton editButton;
    private JButton saveButton;
    private JButton cancelButton;
    private final User user;
    private final AppController controller;

    public AccountPanel(User user, AppController controller) {
        this.user = user;
        this.controller = controller;
        
        // Set up main panel
        setLayout(new BorderLayout());
        setBackground(new Color(248, 249, 250));
        setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        // Create header panel
        JPanel headerPanel = createHeaderPanel();
        add(headerPanel, BorderLayout.NORTH);

        // Create form panel
        JPanel formPanel = createFormPanel();
        add(formPanel, BorderLayout.CENTER);

        // Create button panel
        JPanel buttonPanel = createButtonPanel();
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        headerPanel.setBackground(new Color(248, 249, 250));
        
        JLabel titleLabel = new JLabel("Thông tin tài khoản");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(new Color(52, 58, 64));
        headerPanel.add(titleLabel);

        return headerPanel;
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(new Color(248, 249, 250));
        formPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(30, 30, 30, 30)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        formPanel.add(createStyledLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1;
        usernameField = createStyledTextField(30);
        usernameField.setText(user.getUsername());
        usernameField.setEditable(false);
        usernameField.setBackground(new Color(240, 240, 240));
        formPanel.add(usernameField, gbc);

        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        formPanel.add(createStyledLabel("Họ và tên:"), gbc);
        gbc.gridx = 1;
        fullNameField = createStyledTextField(30);
        fullNameField.setText(user.getFullName());
        fullNameField.setEditable(false);
        formPanel.add(fullNameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        formPanel.add(createStyledLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = createStyledTextField(30);
        emailField.setText(user.getEmail());
        emailField.setEditable(false);
        formPanel.add(emailField, gbc);

        // Phone Number
        gbc.gridx = 0;
        gbc.gridy = 3;
        formPanel.add(createStyledLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1;
        phoneField = createStyledTextField(30);
        phoneField.setText(user.getPhoneNumber());
        phoneField.setEditable(false);
        formPanel.add(phoneField, gbc);

        // Gender
        gbc.gridx = 0;
        gbc.gridy = 4;
        formPanel.add(createStyledLabel("Giới tính:"), gbc);
        gbc.gridx = 1;
        genderComboBox = createStyledComboBox(Gender.values());
        genderComboBox.setSelectedItem(user.getGender());
        genderComboBox.setEnabled(false);
        formPanel.add(genderComboBox, gbc);

        // Role
        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(createStyledLabel("Vai trò:"), gbc);
        gbc.gridx = 1;
        roleField = createStyledTextField(30);
        roleField.setText(user.getRole().toString());
        roleField.setEditable(false);
        roleField.setBackground(new Color(240, 240, 240));
        formPanel.add(roleField, gbc);

        return formPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 20));
        buttonPanel.setBackground(new Color(248, 249, 250));

        editButton = createStyledButton("Chỉnh sửa", new Color(52, 152, 219));
        saveButton = createStyledButton("Lưu", new Color(46, 204, 113));
        cancelButton = createStyledButton("Hủy", new Color(149, 165, 166));

        saveButton.setVisible(false);
        cancelButton.setVisible(false);

        buttonPanel.add(editButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        editButton.addActionListener(e -> setFieldsEditable(true));
        cancelButton.addActionListener(e -> {
            setFieldsEditable(false);
            resetFields();
        });
        saveButton.addActionListener(e -> saveChanges());

        return buttonPanel;
    }

    // Helper methods for styled components
    private JLabel createStyledLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(new Color(60, 60, 60));
        label.setPreferredSize(new Dimension(150, 25));
        return label;
    }

    private JTextField createStyledTextField(int columns) {
        JTextField textField = new JTextField(columns);
        textField.setFont(new Font("Arial", Font.PLAIN, 14));
        textField.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return textField;
    }

    private <T> JComboBox<T> createStyledComboBox(T[] items) {
        JComboBox<T> comboBox = new JComboBox<>(items);
        comboBox.setFont(new Font("Arial", Font.PLAIN, 14));
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(10, 15, 10, 15)
        ));
        return comboBox;
    }

    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setFont(new Font("Arial", Font.BOLD, 13));
        button.setBackground(backgroundColor);
        button.setForeground(Color.WHITE);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Add hover effect
        Color hoverColor = new Color(
            Math.max(0, backgroundColor.getRed() - 20),
            Math.max(0, backgroundColor.getGreen() - 20),
            Math.max(0, backgroundColor.getBlue() - 20)
        );
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(backgroundColor);
            }
        });
        
        return button;
    }

    private void setFieldsEditable(boolean editable) {
        fullNameField.setEditable(editable);
        emailField.setEditable(editable);
        phoneField.setEditable(editable);
        genderComboBox.setEnabled(editable);
        
        // Visual feedback for editable state
        if (editable) {
            fullNameField.setBackground(Color.WHITE);
            emailField.setBackground(Color.WHITE);
            phoneField.setBackground(Color.WHITE);
            genderComboBox.setBackground(Color.WHITE);
        } else {
            fullNameField.setBackground(new Color(240, 240, 240));
            emailField.setBackground(new Color(240, 240, 240));
            phoneField.setBackground(new Color(240, 240, 240));
            genderComboBox.setBackground(new Color(240, 240, 240));
        }
        
        // Role and username are not editable
        editButton.setVisible(!editable);
        saveButton.setVisible(editable);
        cancelButton.setVisible(editable);
    }

    private void resetFields() {
        fullNameField.setText(user.getFullName());
        emailField.setText(user.getEmail());
        phoneField.setText(user.getPhoneNumber());
        genderComboBox.setSelectedItem(user.getGender());
    }

    private void saveChanges() {
        try {
            // Validation
            String fullName = fullNameField.getText().trim();
            String email = emailField.getText().trim();
            String phoneNumber = phoneField.getText().trim();

            if (fullName.isEmpty() || email.isEmpty() || phoneNumber.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra email format
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(this, "Email không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Kiểm tra phone number format
            if (!phoneNumber.matches("^[0-9]{10,11}$")) {
                JOptionPane.showMessageDialog(this, "Số điện thoại phải có 10-11 chữ số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Update the user object from the fields
            user.setFullName(fullName);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            user.setGender((Gender) genderComboBox.getSelectedItem());

            // Call the controller to save the changes
            controller.updateUser(user);

            setFieldsEditable(false);
            JOptionPane.showMessageDialog(this, "Thông tin đã được cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thông tin: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}
