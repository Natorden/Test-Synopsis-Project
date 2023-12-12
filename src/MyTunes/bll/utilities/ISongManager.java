package MyTunes.bll.utilities;

import MyTunes.be.Song;
import MyTunes.dal.interfaces.ISongDAO;

import java.sql.SQLException;
import java.util.List;

public interface ISongManager {
    List<Song> getAllSongs();
    List<Song> filterSong(String search);

    Song createSong(String title, String filePath, String time, String artist, String category, String album);
    void editSong(Song song);
    void removeSong(Song song);

    void setSongDAO(ISongDAO songDAO);
}
