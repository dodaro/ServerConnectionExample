package view;

import client.Client;
import client.ConnectionException;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

public class ViewHandler {

    public static ViewHandler instance = new ViewHandler();

    private final JFrame jFrame = new JFrame();

    public static ViewHandler getInstance() {
        return instance;
    }

    public JFrame getMainWindow() {
        return jFrame;
    }

    public void init() {
        jFrame.setTitle("Server connection example");
        createLoginPage();
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    Client.getInstance().close();
                } catch (IOException | ConnectionException ignoredException) {
                }
            }
        });
    }

    public void createLoginPage() {
        jFrame.setContentPane(new LoginPage());
        jFrame.setSize(350,150);
        jFrame.setResizable(false);
    }

    public void createHomePage() {
        jFrame.setContentPane(new HomePage());
        jFrame.setSize(600, 600);
        jFrame.setResizable(true);
    }

    public void createVerificationPage() {
        jFrame.setContentPane(new SendVerification());
        jFrame.setSize(300, 200);
    }

    public void showErrorMessage(String message) {
        JOptionPane.showMessageDialog(getMainWindow(), message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showInfoMessage(String message) {
        JOptionPane.showMessageDialog(getMainWindow(), message, "Info", JOptionPane.INFORMATION_MESSAGE);
    }
}
