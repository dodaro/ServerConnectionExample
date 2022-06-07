package controller;

import application.ErrorMessages;
import client.Client;
import client.ConnectionException;
import view.ViewHandler;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public record LoginRegistrationController(JTextField email, JPasswordField password, int type) implements ActionListener {

    public final static int REGISTRATION = 0;
    public final static int LOGIN = 1;
    public final static int RESET = 2;

    public LoginRegistrationController {
        if (type != REGISTRATION && type != LOGIN && type != RESET)
            throw new IllegalArgumentException("Invalid type");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(() -> {
            try {
                switch (this.type) {
                    case REGISTRATION -> {
                        String id = Client.getInstance().register(email.getText(), String.valueOf(password.getPassword()));
                        if (id == null) {
                            ViewHandler.getInstance().showErrorMessage(ErrorMessages.USERNAME_ERROR);
                        } else {
                            if (Client.getInstance().sendEmailVerification())
                                ViewHandler.getInstance().createVerificationPage();
                        }
                    }
                    case LOGIN -> {
                        String id = Client.getInstance().login(email.getText(), String.valueOf(password.getPassword()));
                        if (id != null) {
                            ViewHandler.getInstance().createHomePage();
                        } else {
                            ViewHandler.getInstance().showErrorMessage(ErrorMessages.LOGIN_ERROR);
                        }
                    }
                    case RESET -> {
                        if (Client.getInstance().resetPassword(email.getText()))
                            ViewHandler.getInstance().showInfoMessage("We sent a reset link to your email!");
                        else
                            ViewHandler.getInstance().showErrorMessage(ErrorMessages.EMAIL_ERROR);
                    }
                }
            } catch (IOException | ConnectionException exception) {
                ViewHandler.getInstance().showErrorMessage(ErrorMessages.CONNECTION_ERROR);
            }
        }).start();
    }
}
