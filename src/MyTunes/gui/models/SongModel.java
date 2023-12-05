package MyTunes.gui.models;

import MyTunes.be.Song;
import MyTunes.bll.utilities.ISongManager;
import MyTunes.bll.utilities.SongManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

public class SongModel {

    private ISongManager manager;
    private ObservableList<Song> songsToBeViewed = FXCollections.observableArrayList();


    public ISongManager getManager() {
        return manager;
    }


    public SongModel() {
        manager = new SongManager();
        updateSongsToBeViewed();
    }


    public List<Song> getAllSongs() {
        return manager.getAllSongs();
    }


    public ObservableList<Song> getSongsToBeViewed() {
        return songsToBeViewed;
    }

    public void updateSongsToBeViewed() {
        songsToBeViewed.setAll(manager.getAllSongs()); //@todo NOTE: doesnt take into account search queries.
    }


    public void createSong(String title, String filePath, String time, String artist, String category, String album) {
        manager.createSong(title, filePath, time, artist, category, album);
    }


    public void editSong(Song song) {
        manager.editSong(song);
    }

    public void removeSong(Song song) {
        manager.removeSong(song);
        updateSongsToBeViewed();
    }


    /**
     * Method to see if file already exists within the library, to avoid adding duplicates.
     *
     * @param path The path to the file.
     * @return True if it exists, if not it returns false.
     */
    public boolean hasFilePath(String path) {
        List<Song> songs = getAllSongs();
        for (Song song : songs) {
            if (song.getFilePath().equals(path)) return true;
        }
        return false;
    }


    /**
     * Search method, it calls the manager in BLL layer and gets the filtered songs relative to the search query.
     * Then it sets them to be in the observableList.
     *
     * @param search Search query.
     */
    public void filterSongs(String search) {
        songsToBeViewed.setAll(manager.filterSong(search));
    }
}

