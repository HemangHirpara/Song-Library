//Author: Hemang Hirpara
//Author: Poojan Patel
package hhh23_pdp83;

import java.io.File;  // Import the File class
import java.io.FileWriter;
import java.io.IOException;  // Import the IOException class to handle errors

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.*;

public class Controller implements Initializable {

    @FXML private ComboBox<Song> songComboBox;
    @FXML private Button add_btn;
    @FXML private Button edit_btn;
    @FXML private Button del_btn;
    @FXML private Button cancel_btn;
    @FXML private TextField name_tf;
    @FXML private TextField artist_tf;
    @FXML private TextField album_tf;
    @FXML private TextField year_tf;
    @FXML private TextField title;
    @FXML private TextField artist;
    @FXML private TextField album;
    @FXML private TextField year;
    @FXML private TextField message_tf;

    ObservableList<Song> data;
    FileWriter myWriter;

    private String tName;
    private String tArtist;
    private String tAlbum;
    private String tYear;

    @Override
    /*
    read the file and populate the list
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
        songComboBox.setVisibleRowCount(7);
        year.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,4}")) {
                year.setText(oldValue);
            }
        });
        year_tf.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d{0,4}")) {
                year_tf.setText(oldValue);
            }
        });
    }

    private void loadData() {
        data = FXCollections.observableArrayList();
        try
        {

            File songs = new File("songs.txt");
            // if file already exists, read it in and populate the list
            if(!songs.createNewFile())
            {
                //read file line by line
                Scanner sc = new Scanner(songs);
                while (sc.hasNextLine()){
                    //System.out.println(sc.nextLine());
                    String[] tokens = sc.nextLine().split("%");
                    Song temp = new Song(tokens[0], tokens[1], tokens[2], tokens[3]);
                    data.add(temp);
                }
                /*
                print song list to stdout
                for(Song s: data)
                {
                    System.out.println(s.toString());
                }
                 */
                sc.close();
                Collections.sort(data);
                songComboBox.setItems(data);
                songComboBox.getSelectionModel().selectFirst();
                setSongDisplay();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void onAddClicked() {
        if(add_btn.getText().equals("Add Song"))
        {
            name_tf.setVisible(true);
            artist_tf.setVisible(true);
            album_tf.setVisible(true);
            year_tf.setVisible(true);
            del_btn.setVisible(false);
            edit_btn.setVisible(false);
            cancel_btn.setVisible(true);
            add_btn.setText("Confirm");
            message_tf.setText("Adding a new song");
        }
        else
        {
            if (name_tf.getText() == null || name_tf.getText().trim().isEmpty()
                    || artist_tf.getText() == null || artist_tf.getText().trim().isEmpty()) {
                message_tf.setText("Cannot add song without Title and Artist");
                return;
            }

            if ((year_tf.getText() == null || year_tf.getText().trim().isEmpty()))
                year_tf.setText(" ");
            if ((album_tf.getText() == null || album_tf.getText().trim().isEmpty()))
                album_tf.setText(" ");

            Song toAdd = new Song(name_tf.getText(), artist_tf.getText(), album_tf.getText(), year_tf.getText());
            // checking for duplicate

            if(isDuplicate(toAdd)){
                message_tf.setText("Cannot Add Duplicate");
                return;
            }
            // add it to the data list
            data.add(toAdd);
            songComboBox.setItems(null);
            Collections.sort(data);
            songComboBox.setItems(data);
            songComboBox.getSelectionModel().select(toAdd);
            resetFields();
            rewriteFile();
            message_tf.setText("Song added successfully");

        }
    }

    public void onEditClicked() {
        message_tf.setText("Editing Selected Song");
        if(edit_btn.getText().equals("Edit Song"))
        {
            if(songComboBox.getSelectionModel().getSelectedItem() == null)
            {
                message_tf.setText("No Songs in the list to edit");
                resetEditFields();
                return;
            }
            edit_btn.setText("Confirm");
            songComboBox.setDisable(true);
            cancel_btn.setVisible(true);
            add_btn.setVisible(false);
            del_btn.setVisible(false);
            // make red
            title.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            title.setEditable(true);
            artist.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            artist.setEditable(true);
            year.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            year.setEditable(true);
            album.setStyle("-fx-border-color: red ; -fx-border-width: 2px ;");
            album.setEditable(true);
            // save old ones
            tName = title.getText();
            tArtist = artist.getText();
            tAlbum = album.getText();
            tYear = year.getText();
        }
        else
        {
            // to prevent changing song selected
            Song s = songComboBox.getSelectionModel().getSelectedItem();
            songComboBox.setDisable(false);
            // if name and aritsts are changed are not the blank or empty -> update them
            if (title.getText() == null || title.getText().trim().isEmpty()
                    || artist.getText() == null || artist.getText().trim().isEmpty()) {
                message_tf.setText("Cannot edit song without Title/Artist");
                return;
            }
            // set album or year
            s.setSongTitle(title.getText());
            s.setSongArtist(artist.getText());
            s.setSongAlbum(album.getText());
            s.setSongYear(year.getText());

            //check if duplicate
            if(isDuplicate(s))
            {
                s.setSongTitle(tName);
                s.setSongArtist(tArtist);
                s.setSongYear(tYear);
                s.setSongAlbum(tAlbum);
                title.setText(tName);
                artist.setText(tArtist);
                album.setText(tAlbum);
                year.setText(tYear);
                message_tf.setText("Cannot edit song; duplicate found");
                return;
            }

            setSongDisplay();
            //sort again
            songComboBox.setItems(null);
            Collections.sort(data);
            songComboBox.setItems(data);
            songComboBox.getSelectionModel().select(s);
            rewriteFile();
            message_tf.setText("Edit Successful");
            resetEditFields();
        }
    }

    public void onDelClicked() {
        message_tf.setText("Deleting Selected Song");
        if(del_btn.getText().equals("Delete Song"))
        {
            if(songComboBox.getSelectionModel().getSelectedItem() == null)
            {
                message_tf.setText("There are no songs in the list");
                resetFields();
                return;
            }
            songComboBox.setDisable(true);
            add_btn.setVisible(false);
            edit_btn.setVisible(false);
            cancel_btn.setVisible(true);
            del_btn.setText("Confirm");
        }
        else
        {
            data.remove(songComboBox.getSelectionModel().getSelectedItem());
            if(data.size() > 0) {
                songComboBox.getSelectionModel().selectNext();
                setSongDisplay();
            }
            message_tf.setText("Delete Successful");
            songComboBox.setDisable(false);
            rewriteFile();
            resetFields();
        }
    }

    public void onCancelClicked() {
        if(edit_btn.getText().equals("Confirm"))
        {
            title.setText(tName);
            artist.setText(tArtist);
            album.setText(tAlbum);
            year.setText(tYear);
            resetEditFields();
        }
        else
            resetFields();
        message_tf.setText("Canceled Last Action");

    }

    private boolean isDuplicate(Song s) {
        int i = 0;
        while(i < data.size())
        {
            Song temp = data.get(i);
            if (temp.equals(s) && temp != s) {
                //messageField.setText("Song already exists");
                return true;
            }
            i++;
        }
        return false;
    }

    private void rewriteFile() {
        String song_to_add = "";
        try {
            File fileObj = new File("songs.txt");
            FileWriter myWriter = new FileWriter("songs.txt", false);
            for(Song s : data){
                if(s.getSongYear().isEmpty())
                    s.setSongYear(" ");
                song_to_add += s.getSongTitle() + "%" + s.getSongArtist() + "%" + s.getSongAlbum() + "%" + s.getSongYear() + "\n";
                myWriter.write(song_to_add);
                song_to_add = "";
            }

            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private void resetEditFields() {
        title.setStyle("-fx-border-color: none; -fx-border-width: 0px ;");
        artist.setStyle("-fx-border-color: none; -fx-border-width: 0px ;");
        year.setStyle("-fx-border-color: none; -fx-border-width: 0px ;");
        album.setStyle("-fx-border-color: none; -fx-border-width: 0px ;");
        title.setEditable(false);
        artist.setEditable(false);
        year.setEditable(false);
        album.setEditable(false);
        edit_btn.setText("Edit Song");
        add_btn.setVisible(true);
        del_btn.setVisible(true);
        cancel_btn.setVisible(false);
        songComboBox.setDisable(false);
    }

    private void resetFields() {
        name_tf.setVisible(false);
        name_tf.clear();
        artist_tf.setVisible(false);
        artist_tf.clear();
        album_tf.setVisible(false);
        album_tf.clear();
        year_tf.setVisible(false);
        year_tf.clear();
        del_btn.setVisible(true);
        edit_btn.setVisible(true);
        add_btn.setVisible(true);
        cancel_btn.setVisible(false);
        add_btn.setText("Add Song");
        del_btn.setText("Delete Song");
        edit_btn.setText("Edit Song");
        songComboBox.setDisable(false);

    }

    @FXML
    private void setSongDisplay() {
        title.setText("");
        artist.setText("");
        album.setText("");
        year.setText("");
        if(songComboBox.getSelectionModel().getSelectedItem() != null)
        {
            //System.out.println("displaying song");
            title.setText(songComboBox.getSelectionModel().getSelectedItem().getSongTitle());
            artist.setText(songComboBox.getSelectionModel().getSelectedItem().getSongArtist());
            //since year and album are optional, if they are blank, set value to empty string
            if(songComboBox.getSelectionModel().getSelectedItem().getSongYear() == null)
                year.setText("");
            else
                year.setText(songComboBox.getSelectionModel().getSelectedItem().getSongYear());
            if(songComboBox.getSelectionModel().getSelectedItem().getSongAlbum() == null)
                album.setText("");
            else
                album.setText(songComboBox.getSelectionModel().getSelectedItem().getSongAlbum());
            //year.setText(songComboBox.getSelectionModel().getSelectedItem().getSongYear());
            //album.setText(songComboBox.getSelectionModel().getSelectedItem().getSongAlbum());
        }
        else
        {
            title.setText("");
            artist.setText("");
            album.setText("");
            year.setText("");
        }
    }
}
