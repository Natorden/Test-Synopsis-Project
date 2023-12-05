package MyTunes.gui.models;

import MyTunes.be.Playlist;
import MyTunes.be.Song;
import MyTunes.bll.utilities.IPlaylistManager;
import MyTunes.bll.utilities.PlaylistManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class PlaylistModel {
    private final IPlaylistManager manager;
    private final ObservableList<Playlist> playlistsSeen=FXCollections.observableArrayList();
    private ObservableList<Song> playlistSongs=FXCollections.observableArrayList();

    public IPlaylistManager getManager() {
        return this.manager;
    }

    public PlaylistModel() {
        manager = new PlaylistManager();
        updatePlaylistsToBeViewed();
    }

    public ObservableList<Playlist> getPlaylistsToBeViewed() {
        return playlistsSeen;
    }

    public void updatePlaylistsToBeViewed() {
        playlistsSeen.setAll(manager.getAllPlaylists()); //@todo NOTE: doesnt take into account search queries.
    }



    public Playlist createPlaylist(String name){
        return manager.createPlaylist(name);
    }
    public void editPlaylist(Playlist playlist){
        manager.editPlaylist(playlist);
    }
    public void removePlaylist(Playlist playlist){
        manager.removePlaylist(playlist);
    }

    public ObservableList<Playlist> getObservablePlaylist(){
        return playlistsSeen;
    }

    public ObservableList<Song> getPlaylistSongs(Playlist playlist){
        List<Song> songs=playlist.getSongs();
        if(songs==null){
            playlistSongs=FXCollections.observableArrayList();
        }else{
            playlistSongs.setAll(songs);
        }
        return playlistSongs;
    }

    public void addSongToPlaylist(Playlist playlist, Song song){
        manager.addSongToPlaylist(playlist,song);
    }
    public void removeSongFromPlaylist(Playlist playlist, Song song){
        manager.removeSongFromPlaylist(playlist,song);
    }
    public void moveSongUpInPlaylist(Playlist playlist, Song song){
        manager.moveSongUpInPlaylist(playlist,song);
    }
    public void moveSongDownInPlaylist(Playlist playlist, Song song){
        manager.moveSongDownInPlaylist(playlist,song);
    }


}
