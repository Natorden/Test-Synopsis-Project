package MyTunes.gui.controllers;

import MyTunes.be.Playlist;
import MyTunes.be.Song;
import MyTunes.dal.db.PlaylistDBDAO;
import MyTunes.dal.db.PlaylistRelationsDBDAO;
import MyTunes.dal.file.PlaylistFileDAO;
import MyTunes.dal.file.PlaylistRelationsFileDAO;
import MyTunes.gui.models.PlaylistModel;
import MyTunes.gui.models.SongModel;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The main view controller.
 * Used for controlling the main GUI.
 */
public class MyTunesViewController implements Initializable {

    //Where the playlist TableView fxid are
    @FXML private TableView<Playlist> playlist;
    @FXML private TableColumn<Object, Object> playlistName;
    @FXML private TableColumn<Object, Object> playlistNumberSongs;
    @FXML private TableColumn<Object, Object> playlistTime;
    //Where the song list TableView fxid are
    @FXML private TableView<Song> allSong;
    @FXML private TableColumn<Object, Object> songName;
    @FXML private TableColumn<Object, Object> songArtist;
    @FXML private TableColumn<Object, Object> songGenre;
    @FXML private TableColumn<Object, Object> songTime;
    //Where the songs in a playlist ListView fxid are
    @FXML private ListView<Song> playlistSong;
    //CRUD of playlist
    @FXML private Button playlistEditor;
    @FXML private Button deletePlaylist;
    //adding & removing songs from playlist
    @FXML private Button SendToPlaylist;
    @FXML private Button removePlaylistSong;
    //CRUD for songs
    @FXML private Button songEditor;
    @FXML private Button deleteSong;
    //utilities
    @FXML private TextField commenceSearch;
    @FXML private Label sendTextOnPlayingSong;
    @FXML private Slider volumeSlider;
    @FXML private ProgressBar progressBar;
    @FXML private ComboBox<String> saveMode;
    @FXML private ImageView loopImageSwitch;
    @FXML private ImageView playImageSwitch;
    // Media player to play file using the onPlay() method to start and stop.
    @FXML private Media media;
    @FXML private MediaPlayer mediaPlayer;

    private PlaylistModel playlistModel;
    private SongModel songModel;
    private Song songPlaying;
    private File filePlaying;

    private boolean loopSongs=false;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Create models
        songModel = new SongModel();
        playlistModel = new PlaylistModel();
        saveMode.getSelectionModel().selectFirst();

        songListDisplay();
        playlistDisplay();
        search();
        volume();
        clickHandling();

    }


    public void songListDisplay(){
        //Set Song TableView columns
        songName.setCellValueFactory(new PropertyValueFactory<>("title")); //New
        songArtist.setCellValueFactory(new PropertyValueFactory<>("artist")); //New
        songGenre.setCellValueFactory(new PropertyValueFactory<>("category")); //New
        songTime.setCellValueFactory(new PropertyValueFactory<>("time")); //New
        allSong.setItems(songModel.getSongsToBeViewed());
    }

    public void playlistDisplay(){
        //Set Playlist TableView columns
        playlistName.setCellValueFactory(new PropertyValueFactory<>("name"));
        playlistNumberSongs.setCellValueFactory(new PropertyValueFactory<>("relationsSize"));
        playlistTime.setCellValueFactory(new PropertyValueFactory<>("totalSongsTime"));
        playlist.setItems(playlistModel.getObservablePlaylist());
    }

    public void search(){
        //Listens for search TextField input.
        commenceSearch.textProperty().addListener((obs, oldText, newText) -> songModel.filterSongs(newText));
    }

    public void volume(){
        //Adjusts the volume
        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> { //Volume handling
            if (mediaPlayer==null) return;
            mediaPlayer.setVolume((double)newVal/100);
        });
    }

    public void clickHandling(){
        final Image[] image = new Image[1];
        //Listen for clicks on the playlist listview (left ListView)
            playlist.setOnMouseClicked(click -> playlistSong.setItems(playlistModel.getPlaylistSongs(playlist.getSelectionModel().getSelectedItem())));

        //Listen for clicks on the playlistSongs listview (middle ListView)
        playlistSong.setOnMouseClicked(click ->{
            allSong.getSelectionModel().clearSelection();
            if (click.getClickCount()!=2||playlistSong.getSelectionModel().getSelectedItem()==songPlaying) return;
            playSongFromListIndex(playlistSong.getSelectionModel().getSelectedIndex());
            image[0] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../views/images/pause.png")));
            playImageSwitch.setImage(image[0]);
            
        });

        //Listen for clicks on the allSong listview (right ListView)
        allSong.setOnMouseClicked(click ->{
            playlistSong.getSelectionModel().clearSelection();
            if (click.getClickCount()!=2||allSong.getSelectionModel().getSelectedItem()==songPlaying) return;
            playSongFromListIndex(allSong.getSelectionModel().getSelectedIndex());
            image[0] = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../views/images/pause.png")));
            playImageSwitch.setImage(image[0]);
        });
    }

    public void createPlaylist() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/PlaylistCRUDWindow.fxml"));
        Parent root = loader.load();
        PlaylistCRUDWindow controller = loader.getController();
        controller.setSaveMode(saveMode.getValue());
        handleStageGeneral(root,"New/Edit Our Tunes Playlists");

        playlist.setItems(playlistModel.getPlaylistsToBeViewed());
        playlistModel.updatePlaylistsToBeViewed();
    }

    public void editPlaylist() throws IOException {
        Playlist selectedPlaylist = playlist.getSelectionModel().getSelectedItem();
        if (selectedPlaylist == null) return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/PlaylistCRUDWindow.fxml"));
        Parent root = loader.load();
        PlaylistCRUDWindow controller = loader.getController();
        controller.initializeEdit(selectedPlaylist);
        controller.setSaveMode(saveMode.getValue());
        handleStageGeneral(root,"New/Edit Our Tunes Playlists");
        playlist.setItems(playlistModel.getPlaylistsToBeViewed());
        playlistModel.updatePlaylistsToBeViewed();
    }

    public void removePlaylist() {
        Playlist selectedPlaylist = playlist.getSelectionModel().getSelectedItem();
        if (selectedPlaylist == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + selectedPlaylist + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            playlistModel.removePlaylist(selectedPlaylist);
            playlist.setItems(playlistModel.getPlaylistsToBeViewed());
            playlistModel.updatePlaylistsToBeViewed();
        }
    }

    public void addSongToPlaylist() {
        Song selectedSong = allSong.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = playlist.getSelectionModel().getSelectedItem();
        if (selectedSong == null||selectedPlaylist==null) return;
        playlistModel.addSongToPlaylist(selectedPlaylist,selectedSong);
        playlistSong.setItems(playlistModel.getPlaylistSongs(selectedPlaylist));
        playlistModel.updatePlaylistsToBeViewed();
    }

    public void deletePlaylistSong() {
        Song selectedSong = playlistSong.getSelectionModel().getSelectedItem();
        Playlist selectedPlaylist = playlist.getSelectionModel().getSelectedItem();
        if (selectedSong == null||selectedPlaylist==null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remove " + selectedSong + " from " + selectedPlaylist + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            playlistModel.removeSongFromPlaylist(selectedPlaylist,selectedSong);
            playlistSong.setItems(playlistModel.getPlaylistSongs(selectedPlaylist));
            playlistModel.updatePlaylistsToBeViewed();
        }
    }

    public void createSong() throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/SongCRUDWindow.fxml"));
        Parent root = loader.load();
        SongCRUDWindow songCRUDWindow = loader.getController();
        songCRUDWindow.setSaveMode(saveMode.getValue());
        handleStageGeneral(root,"New/Edit Our Tunes Songs");

        songModel.updateSongsToBeViewed();
    }

    public void editSong() throws IOException {
        Song selectedSong = allSong.getSelectionModel().getSelectedItem();
        if (selectedSong == null) return;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../views/SongCRUDWindow.fxml"));
        Parent root = loader.load();
        SongCRUDWindow controller = loader.getController();
        controller.initializeEdit(selectedSong);
        controller.setSaveMode(saveMode.getValue());
        handleStageGeneral(root,"New/Edit Our Tunes Songs");
        songModel.updateSongsToBeViewed();
    }

    public void removeSong(){
        Song selectedSong = allSong.getSelectionModel().getSelectedItem();
        if (selectedSong == null) return;
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Delete " + selectedSong + " ?", ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            songModel.removeSong(selectedSong);
        }

    }

    public void playPreviousSong(){
        Image image;
        int index;
        if(allSong.getSelectionModel().getSelectedItem()!=null) {
            index = allSong.getSelectionModel().getSelectedIndex();
            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../views/images/pause.png")));
            playImageSwitch.setImage(image);
        }else{
            index = playlistSong.getSelectionModel().getSelectedIndex();
        }
        if (index == 0) return; //Currently playing song at top of the list, nothing else to play previously so we return...
        playSongFromListIndex(index-1);

    }

    public void playNextSong() {
        Image image;
        int index;
        if(allSong.getSelectionModel().getSelectedItem()!=null) {
            index = allSong.getSelectionModel().getSelectedIndex()+1;
            if (allSong.getItems().size() <= index ){ index=0; } //Currently playing song at the end of the list, nothing else to play next so we return...
            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../views/images/pause.png")));
            playImageSwitch.setImage(image);
        }else{
            index = playlistSong.getSelectionModel().getSelectedIndex()+1;
            if (playlistSong.getItems().size() <= index + 1){ index=0; } //Currently playing song at the end of the list, nothing else to play next so we return...
        }
        playSongFromListIndex(index);

    }

    public void onPlay() {
        Image image;
        if(getSelectedSong()==null) return; //If we cant find a selected song at all then we cant play anything.
        if (mediaPlayer == null) {
            // If theres no media player
            setupSong(getSelectedSong());
        }
        if (mediaPlayer.getStatus().equals(MediaPlayer.Status.PLAYING)) {
            mediaPlayer.pause();
            sendTextOnPlayingSong.setText("Paused...");
            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../views/images/play.png")));
        }else{
            //If the mediaPlayer is paused and you press the play button this code runs.
            if(getSelectedSong()!=songPlaying){ //If the selectedSong list isn't the same as the currently playing song then create new media:
                setupSong(getSelectedSong());
            }
            sendTextOnPlayingSong.setText(songPlaying.getTitle());
            mediaPlayer.play();
            image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../views/images/pause.png")));
        }
        playImageSwitch.setImage(image);
    }

    /**
     * Plays the song with the given index within the listview that's currently selected, if possible.
     * @param index The index to play.
     */
    public void playSongFromListIndex(int index) {
        if(getSelectedSong()==null) return; //If we cant find a selected song at all then we cant play anything.
        if (mediaPlayer != null){ mediaPlayer.stop(); }
        if(allSong.getSelectionModel().getSelectedItem()!=null){
            setupSong(allSong.getItems().get(index));
            allSong.getSelectionModel().select(index);
        }else{
            setupSong(playlistSong.getItems().get(index));
            playlistSong.getSelectionModel().select(index);
        }
        sendTextOnPlayingSong.setText(songPlaying.getTitle());
        mediaPlayer.play();
    }

    /**
     * Used to avoid duplicate code, sets the song up, setting variables: songPlaying, filePlaying, media, mediaPlayer.
     * @param song Song to get ready.
     */
    private void setupSong(Song song) {
        songPlaying=song;
        filePlaying = new File(songPlaying.getFilePath());
        try {
            media = new Media(filePlaying.toURI().toURL().toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        mediaPlayer = new MediaPlayer(media);//Listens for search TextField input.
        mediaPlayer.setOnReady(() -> {
            mediaPlayer.currentTimeProperty().addListener((observable, oldValue, newValue) -> { //Progress bar handling
                progressBar.setProgress(newValue.toSeconds()/mediaPlayer.getTotalDuration().toSeconds());
            });
        });

        mediaPlayer.setOnEndOfMedia(() -> { // Logic for autoloop
            if(!loopSongs) return;
            playNextSong();
        });
    }

    /**
     * Gets selected song from either playlistSongs listview or allSong listview depending on which one is selected.
     * @return Returns the selected song, returns null if none found.
     */
    private Song getSelectedSong(){
        Song selectedSong = allSong.getSelectionModel().getSelectedItem();
        if(selectedSong==null){ //If no item selected in primary song listview.
            selectedSong=playlistSong.getSelectionModel().getSelectedItem();
            if(selectedSong==null&&allSong.getItems().size()!=0){   //If no item selected in the playlist songs listview & the primary song listview isn't empty...
                                                                    //... we select the top song on the primary list and return it.
                allSong.getSelectionModel().select(0);
                selectedSong=allSong.getItems().get(0);
            }
        }
        return selectedSong;
    }

    /**
     * Used to avoid duplicate code multiple places.
     */
    private void handleStageGeneral(Parent root, String title) {
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.setResizable(false);
        stage.setTitle(title);
        stage.showAndWait();
    }

    /**
     * Switch between saving locally or into db in Main Window.
     */
    public void onSelectMode() {
        songModel.getManager().setSongDAO(saveMode.getValue());
        if (saveMode.getValue().equals("Local")) {
            playlistModel.getManager().setPlaylistDAO(new PlaylistFileDAO(), new PlaylistRelationsFileDAO());
        }
        else if (saveMode.getValue().equals("Cloud")) {
            playlistModel.getManager().setPlaylistDAO(new PlaylistDBDAO(), new PlaylistRelationsDBDAO());
        }

        songModel.updateSongsToBeViewed();
        playlistModel.updatePlaylistsToBeViewed();

    }

    /**
     * Used to Loop a playlist or the song list so it wont end
     */
    public void loopSong() {
        Image image;
        loopSongs=!loopSongs;
        if(loopSongs) image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../views/images/loopOn.png")));
        else image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../views/images/loopOff.png")));
        loopImageSwitch.setImage(image);
    }

    /**
     * Moves a playlist song up
     */
    public void onMoveSongUpInPlaylist(){
        Song song=getSelectedSong();
        Playlist playlist_=playlist.getSelectionModel().getSelectedItem();
        playlistModel.getManager().moveSongUpInPlaylist(playlist_,song);
        playlistSong.setItems(playlistModel.getPlaylistSongs(playlist.getSelectionModel().getSelectedItem()));
        playlistSong.getSelectionModel().select(song);
    }

    /**
     * Moves a playlist song down
     */
    public void onMoveSongDownInPlaylist(){
        Song song=getSelectedSong();
        Playlist playlist_=playlist.getSelectionModel().getSelectedItem();
        playlistModel.getManager().moveSongDownInPlaylist(playlist_,song);
        playlistSong.setItems(playlistModel.getPlaylistSongs(playlist.getSelectionModel().getSelectedItem()));
        playlistSong.getSelectionModel().select(song);
    }
}
