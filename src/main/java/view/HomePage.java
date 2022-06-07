package view;

import application.ErrorMessages;
import client.Client;
import client.ConnectionException;
import client.util.JSONUtil;
import model.Note;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class HomePage extends JPanel {

    public HomePage() {
        Dimension dimension = new Dimension(100, 30);
        JLabel label = new JLabel("Hi, " + Client.getInstance().getEmail());
        label.setPreferredSize(new Dimension(300, 30));
        JButton logout = new JButton("Logout");
        logout.setPreferredSize(dimension);
        logout.addActionListener(event -> new Thread(() -> {
            try {
                Client.getInstance().logout();
                ViewHandler.getInstance().createLoginPage();
            } catch (IOException | ConnectionException e) {
                ViewHandler.getInstance().showErrorMessage(ErrorMessages.CONNECTION_ERROR);
            }
        }).start());

        JPanel middlePanel = new JPanel();
        JTextField date = new JTextField();
        date.setPreferredSize(dimension);
        JTextField title = new JTextField();
        title.setPreferredSize(dimension);

        DefaultListModel<Note> listModel = new DefaultListModel<>();
        JList<Note> listView = new JList<>(listModel);
        initListView(listModel);

        JButton addNoteButton = new JButton("Add note");
        addNoteButton.addActionListener(event -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.showOpenDialog(ViewHandler.getInstance().getMainWindow());
            File selectedFile = fileChooser.getSelectedFile();
            if(selectedFile != null) {
                Client.getInstance().uploadFile(selectedFile, selectedFile.getName().replaceAll(".*\\.",""),
                        ref -> {
                            Note note = new Note(null, date.getText(), title.getText(), ref.fileId());
                            try {
                                JSONObject obj = JSONUtil.toJSON(note);
                                Client.getInstance().insert("notes", obj,
                                        reference -> {
                                            String elementId = reference.result().getJSONObject("response").getString("element_id");
                                            Note newNote = new Note(elementId, note.date(), note.title(), note.file_id());
                                            listModel.addElement(newNote); },
                                        exc -> {exc.printStackTrace(); ViewHandler.getInstance().showErrorMessage(ErrorMessages.ADD_NOTE_ERROR); });
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        },
                        exception -> ViewHandler.getInstance().showErrorMessage(ErrorMessages.UPLOAD_FILE_ERROR));
            }
        });
        middlePanel.add(date);
        middlePanel.add(title);
        middlePanel.add(addNoteButton);

        JButton downloadSelectedNoteButton = new JButton("Download selected note");
        downloadSelectedNoteButton.addActionListener(event -> {
                    Note selectedNote = listView.getSelectedValue();
                    if(selectedNote != null) {
                        Client.getInstance().retrieveFile(selectedNote.file_id(), reference -> {
                                    JFileChooser fileChooser = new JFileChooser();
                                    fileChooser.showSaveDialog(ViewHandler.getInstance().getMainWindow());
                                    File file = fileChooser.getSelectedFile();
                                    if (file != null) {
                                        try {
                                            reference.saveFile(file.getAbsolutePath());
                                            ViewHandler.getInstance().showInfoMessage("Downloaded file");
                                        } catch (Exception e) {
                                            ViewHandler.getInstance().showErrorMessage(ErrorMessages.SAVE_FILE_ERROR);
                                        }
                                    }
                                },
                                exc -> ViewHandler.getInstance().showErrorMessage(ErrorMessages.DOWNLOAD_FILE_ERROR));
                    }
                }
        );

        JButton deleteSelectedNoteButton = new JButton("Delete selected note");
        deleteSelectedNoteButton.addActionListener(event -> {
            Note selectedNote = listView.getSelectedValue();
            if(selectedNote != null) {
                Client.getInstance().deleteFile(selectedNote.file_id(),
                    reference ->  Client.getInstance().remove("notes", selectedNote.id(),
                        ref -> listModel.remove(listView.getSelectedIndex()),
                        exc -> ViewHandler.getInstance().showErrorMessage(ErrorMessages.DELETE_NOTE_ERROR)),
                    exception -> ViewHandler.getInstance().showErrorMessage(ErrorMessages.DELETE_FILE_ERROR));
            }
        });

        JButton updateSelectedNoteButton = new JButton("Update selected note");
        updateSelectedNoteButton.addActionListener(event -> {
                    Note selectedNote = listView.getSelectedValue();
                    if(selectedNote != null) {
                        try {
                            Note newNote = new Note(selectedNote.id(), date.getText(), title.getText(), selectedNote.file_id());
                            JSONObject obj = JSONUtil.toJSON(newNote);
                            Client.getInstance().update("notes", newNote.id(), obj,
                                    ref -> listModel.set(listView.getSelectedIndex(), newNote),
                                    exc -> ViewHandler.getInstance().showErrorMessage(ErrorMessages.UPDATE_NOTE_ERROR));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
        );

        this.add(label);
        this.add(logout);
        this.add(middlePanel);
        this.add(listView);
        this.add(downloadSelectedNoteButton);
        this.add(deleteSelectedNoteButton);
        this.add(updateSelectedNoteButton);
    }

    private void initListView(DefaultListModel<Note> model) {
        Client.getInstance().get("notes", ref -> {
            JSONObject result = ref.result();
            JSONArray jsonArray = result.getJSONArray("notes");
            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Note n = new Note(obj.getString("element_id"), obj.getString("date"), obj.getString("title"), obj.getString("file_id"));
                model.addElement(n);
            }
        }, exc -> exc.printStackTrace() );
    }
}
