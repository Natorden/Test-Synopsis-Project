package MyTunes.dal.interfaces;

import MyTunes.be.Playlist;

import java.util.List;

public interface IPlaylistDAO {
    List<Playlist> getAllPlaylists();

    boolean insertPlaylist(Playlist playlist);
    void updatePlaylist(Playlist playlist);
    void deletePlaylist(Playlist playlist);
}
