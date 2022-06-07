package view;

import controller.LoginRegistrationController;

import javax.swing.*;
import java.awt.*;

public class LoginPage extends JPanel {

    public LoginPage() {
        Dimension fixedLabelDimension = new Dimension(100, 30);
        Dimension fixedFieldDimension = new Dimension(200, 30);
        JPanel firstRow = new JPanel();
        firstRow.setLayout(new BoxLayout(firstRow, BoxLayout.LINE_AXIS));
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setPreferredSize(fixedLabelDimension);
        firstRow.add(emailLabel);
        JTextField email = new JTextField();
        email.setPreferredSize(fixedFieldDimension);
        firstRow.add(email);
        JPanel secondRow = new JPanel();
        secondRow.setLayout(new BoxLayout(secondRow, BoxLayout.LINE_AXIS));
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setPreferredSize(fixedLabelDimension);
        secondRow.add(passwordLabel);
        JPasswordField password = new JPasswordField();
        password.setPreferredSize(fixedFieldDimension);
        secondRow.add(password);
        JPanel thirdRow = new JPanel();
        thirdRow.setLayout(new BoxLayout(thirdRow, BoxLayout.LINE_AXIS));
        JButton login = new JButton("Login");
        JButton register = new JButton("Register");
        JButton resetPassword = new JButton("Reset password");

        thirdRow.add(login);
        thirdRow.add(register);
        this.add(firstRow);
        this.add(secondRow);
        this.add(thirdRow);
        this.add(resetPassword);
        login.addActionListener(new LoginRegistrationController(email, password, LoginRegistrationController.LOGIN));
        register.addActionListener(new LoginRegistrationController(email, password, LoginRegistrationController.REGISTRATION));
        resetPassword.addActionListener(new LoginRegistrationController(email, null, LoginRegistrationController.RESET));
    }
}
