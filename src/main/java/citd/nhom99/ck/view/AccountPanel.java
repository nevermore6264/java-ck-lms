package citd.nhom99.ck.view;

import citd.nhom99.ck.controller.AppController;
import citd.nhom99.ck.model.constant.Gender;
import citd.nhom99.ck.model.User;

import javax.swing.*;
import java.awt.*;

public class AccountPanel extends JPanel {
    private final JTextField usernameField;
    private final JTextField fullNameField;
    private final JTextField emailField;
    private final JTextField phoneField;
    private final JComboBox<Gender> genderComboBox;
    private final JTextField roleField;
    private final JButton editButton;
    private final JButton saveButton;
    private final JButton cancelButton;
    private final User user;
    private final AppController controller;

    public AccountPanel(User user, AppController controller) {
        this.user = user;
        this.controller = controller;
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Username
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Tên đăng nhập:"), gbc);
        gbc.gridx = 1;
        usernameField = new JTextField(user.getUsername(), 20);
        usernameField.setEditable(false);
        add(usernameField, gbc);

        // Full Name
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Họ và tên:"), gbc);
        gbc.gridx = 1;
        fullNameField = new JTextField(user.getFullName(), 20);
        fullNameField.setEditable(false);
        add(fullNameField, gbc);

        // Email
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Email:"), gbc);
        gbc.gridx = 1;
        emailField = new JTextField(user.getEmail(), 20);
        emailField.setEditable(false);
        add(emailField, gbc);

        // Phone Number
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Số điện thoại:"), gbc);
        gbc.gridx = 1;
        phoneField = new JTextField(user.getPhoneNumber(), 20);
        phoneField.setEditable(false);
        add(phoneField, gbc);

        // Gender
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Giới tính:"), gbc);
        gbc.gridx = 1;
        genderComboBox = new JComboBox<>(Gender.values());
        genderComboBox.setSelectedItem(user.getGender());
        genderComboBox.setEnabled(false);
        add(genderComboBox, gbc);

        // Role
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Vai trò:"), gbc);
        gbc.gridx = 1;
        roleField = new JTextField(user.getRole().toString(), 20);
        roleField.setEditable(false);
        add(roleField, gbc);

        // Buttons
        JPanel buttonPanel = new JPanel();
        editButton = new JButton("Chỉnh sửa");
        saveButton = new JButton("Lưu");
        cancelButton = new JButton("Hủy");

        saveButton.setVisible(false);
        cancelButton.setVisible(false);

        buttonPanel.add(editButton);
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);

        editButton.addActionListener(e -> setFieldsEditable(true));
        cancelButton.addActionListener(e -> {
            setFieldsEditable(false);
            resetFields();
        });
        saveButton.addActionListener(e -> saveChanges());
    }

    private void setFieldsEditable(boolean editable) {
        fullNameField.setEditable(editable);
        emailField.setEditable(editable);
        phoneField.setEditable(editable);
        genderComboBox.setEnabled(editable);
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
        // Update the user object from the fields
        user.setFullName(fullNameField.getText());
        user.setEmail(emailField.getText());
        user.setPhoneNumber(phoneField.getText());
        user.setGender((Gender) genderComboBox.getSelectedItem());

        // Call the controller to save the changes
//        controller.updateUser(user);

        setFieldsEditable(false);
        JOptionPane.showMessageDialog(this, "Thông tin đã được cập nhật.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
    }
}
