package view;

import application.ErrorMessages;
import client.Client;
import client.ConnectionException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class SendVerification extends JPanel {

    public SendVerification() {
        Dimension dimension = new Dimension(300, 30);
        JLabel label = new JLabel("We sent a verification link to your email!");
        JButton confirm = new JButton("Confirm");
        JButton sendAgain = new JButton("Send Again");
        label.setPreferredSize(dimension);
        confirm.setPreferredSize(dimension);
        sendAgain.setPreferredSize(dimension);
        this.add(label);
        this.add(confirm);
        this.add(sendAgain);

        confirm.addActionListener(event -> new Thread(() -> {
            try {
                if (Client.getInstance().isEmailVerified())
                    ViewHandler.getInstance().createHomePage();
                else
                    ViewHandler.getInstance().showErrorMessage(ErrorMessages.UNVERIFIED_EMAIL_ERROR);
            } catch (IOException | ConnectionException e) {
                ViewHandler.getInstance().showErrorMessage(ErrorMessages.CONNECTION_ERROR);
            }
        }).start());

        sendAgain.addActionListener(event -> new Thread(() -> {
            try {
                Client.getInstance().sendEmailVerification();
            } catch (IOException | ConnectionException e) {
                ViewHandler.getInstance().showErrorMessage(ErrorMessages.CONNECTION_ERROR);
            }
        }).start());
    }
}
