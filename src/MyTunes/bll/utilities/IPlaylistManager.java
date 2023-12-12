package MyTunes.bll.utilities;

import MyTunes.be.Playlist;
import MyTunes.be.Song;
import MyTunes.dal.interfaces.IPlaylistDAO;
import MyTunes.dal.interfaces.IPlaylistRelationsDAO;

import java.util.List;

public interface IPlaylistManager {
    List<Playlist> getAllPlaylists();

    Playlist createPlaylist(String name);
    void editPlaylist(Playlist playlist);
    boolean removePlaylist(Playlist playlist);

    void addSongToPlaylist(Playlist playlist, Song song);
    void removeSongFromPlaylist(Playlist playlist, Song song);
    void moveSongUpInPlaylist(Playlist playlist,Song song);
    void moveSongDownInPlaylist(Playlist playlist,Song song);

    void setPlaylistDAO(IPlaylistDAO songDAO, IPlaylistRelationsDAO relationsDAO);
}
