package MyTunes.gui.controllers;

import MyTunes.be.Playlist;
import MyTunes.gui.models.PlaylistModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This is the PlaylistCRUDWindow controller
 * used for performing playlist CRUD tasks in the GUI.
 */

public class PlaylistCRUDWindow implements Initializable {
    @FXML private TextField editPlaylistField;
    @FXML private Button cancelPlaylistEdit;
    @FXML private Button savePlaylistEdit;

    private PlaylistModel playlistModel;
    private Playlist selectedPlaylist;
    protected boolean editMode=false;

    private String saveMode;

    public void setSaveMode(String saveMode) {
        this.saveMode = saveMode;
        if(saveMode.equals("Local")) {

            playlistModel.getManager().setPlaylistDAO(saveMode);

            System.out.println("PlaylistFileDAO chosen");
        }
        else if(saveMode.equals("Cloud")){
            playlistModel.getManager().setPlaylistDAO(saveMode);
            System.out.println("PlaylistDBDAO chosen");
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        playlistModel = new PlaylistModel();
    }

    /**
     * onAction method for the cancel button in the view.
     */
    public void cancelEditOnPlaylist() {
        Stage window = (Stage) cancelPlaylistEdit.getScene().getWindow();
        window.close();
    }

    /**
     * onAction method for the save button in the view.
     */
    public void saveEditOnPlaylist(){
        if(editPlaylistField==null||editPlaylistField.getText()==null||editPlaylistField.getText().equals("")||editPlaylistField.getText().length()>32) return;
        if(editMode){
            playlistModel.editPlaylist(new Playlist(selectedPlaylist.getId(),editPlaylistField.getText()));
            selectedPlaylist=null;
        }else{
            playlistModel.createPlaylist(editPlaylistField.getText());
        }
        Stage window = (Stage) cancelPlaylistEdit.getScene().getWindow();
        window.close();
    }

    /**
     * Used to differentiate between when this controller is to be used for editing, or adding a playlist.
     * If this function isn't called, the controller assumes its to add a playlist.
     * @param playlist The playlist to edit on.
     */
    public void initializeEdit(Playlist playlist) {
        editMode=true;
        selectedPlaylist=playlist;
        editPlaylistField.setText(playlist.getName());
    }

}
