package MyTunes.dal.interfaces;

import MyTunes.be.Playlist;
import MyTunes.be.PlaylistRelation;
import MyTunes.be.Song;

public interface IPlaylistRelationsDAO {

    void getPlaylistSongs(Playlist playlist);
    void addSongToPlaylist(PlaylistRelation playlistRelation);
    void removeSongFromPlaylist(Playlist playlist, Song song);
    void savePlaylistSongs(Playlist playlist);

}
