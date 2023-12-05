package MyTunes.dal.interfaces;

import MyTunes.be.Song;

import java.sql.SQLException;
import java.util.List;

public interface ISongDAO {
    List<Song> getAllSongs();

    void insertSong(Song song);
    void updateSong(Song song);
    void deleteSong(Song song);
}
