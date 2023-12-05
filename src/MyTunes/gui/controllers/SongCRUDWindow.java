package MyTunes.gui.controllers;

import MyTunes.be.Song;
import MyTunes.bll.utilities.SongManager;
import MyTunes.gui.models.SongModel;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the SongCRUDWindow controller
 * used for performing song CRUD tasks in the GUI.
 */

public class SongCRUDWindow implements Initializable {

    @FXML private TextField titleTextField;
    @FXML private TextField artistTextField;
    @FXML private TextField fileTextField;
    @FXML private TextField timeTextField;

    @FXML private ComboBox<String> categorySelection;
    @FXML private Button filepathSelection;
    @FXML private Button cancelSongEdit;
    @FXML private Button saveSongEdit;

    private SongModel songModel;
    protected boolean editMode = false;
    String[] genres = {"Country","Dubstep","Electro","Electronic","Hip-Hop","Indie Rock","Jazz","Orchestra/Score","Pop","Rock","Rhythm & Blues","Techno"};
    private ObservableList<String> genre;
    private String saveMode;
    private Song editSong;


    /**
     * Used to set the saveMode from the MyTunesViewController
     * @param saveMode Can be Local or Cloud
     */
    public void setSaveMode(String saveMode) {
        this.saveMode = saveMode;
        if (saveMode.equals("Local")) {
            songModel.getManager().setSongDAO(saveMode);
        } else if (saveMode.equals("Cloud")) {
            songModel.getManager().setSongDAO(saveMode);
        }
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            songModel = new SongModel();
        } catch (Exception e) {
            e.printStackTrace();
        }
        comboBoxGenreSelect();
    }

    public void comboBoxGenreSelect(){
        //Selects a genre to give a song
        genre = FXCollections.observableArrayList();
        genre.addAll(genres);
        categorySelection.setItems(genre);
    }

    /**
     * onAction method for the cancel button in the view.
     */
    public void CancelEditOfSong() {
        Stage window = (Stage) cancelSongEdit.getScene().getWindow();
        window.close();

    }

    /**
     * onAction method for the save button in the view.
     */
    public void saveEditOfSong() {
        if (editMode) {
            songModel.editSong(new Song(editSong.getId(), titleTextField.getText(), fileTextField.getText(), timeTextField.getText(), artistTextField.getText(), categorySelection.getValue(), ""));
        } else {
            if (titleTextField.getText().equals("") || fileTextField.getText().equals("") || songModel.hasFilePath(fileTextField.getText()))
                return;
            songModel.createSong(titleTextField.getText(), fileTextField.getText(), timeTextField.getText(), artistTextField.getText(), categorySelection.getValue(), "");
        }
        Stage window = (Stage) cancelSongEdit.getScene().getWindow();
        window.close();
    }

    /**
     * onAction method for file browse button.
     */
    public void selectTheFilePathOfSong() {
        FileChooser fc = new FileChooser();
        fc.setInitialDirectory(new File("C:\\Users"));
        File selectedFile = fc.showOpenDialog(null);
        if (selectedFile == null) return; //If no file was selected then do nothing.
        Media media = new Media(selectedFile.toURI().toString());
        MediaPlayer player = new MediaPlayer(media);
        String s = "The White Stripes";
        //We listen for changes to the map, since metadata is not immediately available code wise.
        media.getMetadata().addListener((MapChangeListener<String, Object>) ch -> {
            if (ch.wasAdded()) { //To not run the same code multiple times we only run this if the variable is added to the map.
                if ("artist".equals(ch.getKey())) {
                   artistTextField.setText(ch.getValueAdded().toString());
                } else if ("album".equals(ch.getKey())) {
                    //@todo if we want
                } else if ("title".equals(ch.getKey())) {
                    titleTextField.setText(ch.getValueAdded().toString());
                } else if ("genre".equals(ch.getKey())) {
                    //@todo  if we want
                }
            }
        });
        player.setOnReady(() -> timeTextField.setText(String.valueOf(SongManager.secondsToMinutes((int) Math.round(media.getDuration().toSeconds())))));
        fileTextField.setText(selectedFile.getAbsolutePath());
    }


    /**
     * Used to differentiate between when this controller is to be used for editing, or adding a song.
     * If this function isn't called, the controller assumes its to add a song.
     *
     * @param song The song to edit on.
     */
    public void initializeEdit(Song song) {
        editMode = true;
        editSong = song;
        titleTextField.setText(song.getTitle());
        artistTextField.setText(song.getArtist());
        categorySelection.setValue(song.getCategory());
        timeTextField.setText("" + song.getTime());
        timeTextField.setDisable(true);
        fileTextField.setText(song.getFilePath());
        fileTextField.setDisable(true);
        filepathSelection.setDisable(true);
    }


}
