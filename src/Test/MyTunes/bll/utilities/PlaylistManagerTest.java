package MyTunes.bll.utilities;

import MyTunes.be.Playlist;

import MyTunes.dal.file.PlaylistFileDAO;
import MyTunes.dal.file.PlaylistRelationsFileDAO;
import MyTunes.dal.interfaces.IPlaylistDAO;
import MyTunes.dal.interfaces.IPlaylistRelationsDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlaylistManagerTest {

    private IPlaylistDAO _PlaylistMock;
    private IPlaylistRelationsDAO _RelationMock;
    private IPlaylistManager _PlaylistManager;

    private List<Playlist> allPlaylists;

    @BeforeEach
    void setUp() {

        _PlaylistMock = mock(PlaylistFileDAO.class);
        _RelationMock = mock(PlaylistRelationsFileDAO.class);
        _PlaylistManager = new PlaylistManager();

        //allPlaylists.add();
    }

    @Test
    void setPlaylistDAO() {
    }

    @Test
    void getAllPlaylists() {
        when(_PlaylistManager.getAllPlaylists()).thenReturn(allPlaylists);

    }

    @Test
    void createPlaylist() {
    }

    @Test
    void editPlaylist() {
    }

    @Test
    void removePlaylist() {
    }

    @Test
    void addSongToPlaylist() {
    }

    @Test
    void removeSongFromPlaylist() {
    }

    @Test
    void moveSongUpInPlaylist() {
    }

    @Test
    void moveSongDownInPlaylist() {
    }
}