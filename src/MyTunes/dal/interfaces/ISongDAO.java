package MyTunes.dal.interfaces;

import MyTunes.be.Song;

import java.util.List;

public interface ISongDAO {
    List<Song> getAllSongs();

    void insertSong(Song song);
    boolean updateSong(Song song);
    boolean deleteSong(Song song);
}
