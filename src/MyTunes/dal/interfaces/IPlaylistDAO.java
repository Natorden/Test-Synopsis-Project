package MyTunes.dal.interfaces;

import MyTunes.be.Playlist;

import java.util.List;

public interface IPlaylistDAO {
    List<Playlist> getAllPlaylists();

    void insertPlaylist(Playlist playlist);
    boolean updatePlaylist(Playlist playlist);
    boolean deletePlaylist(Playlist playlist);
}
