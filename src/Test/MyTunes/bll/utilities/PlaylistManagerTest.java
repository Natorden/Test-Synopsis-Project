package MyTunes.bll.utilities;

import MyTunes.dal.db.PlaylistDBDAO;
import MyTunes.dal.db.PlaylistRelationsDBDAO;
import MyTunes.dal.file.PlaylistFileDAO;
import MyTunes.dal.file.PlaylistRelationsFileDAO;
import MyTunes.dal.interfaces.IPlaylistDAO;
import MyTunes.dal.interfaces.IPlaylistRelationsDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class PlaylistManagerTest {

    private IPlaylistDAO _PlaylistMock;
    private IPlaylistRelationsDAO _RelationMock;
    private IPlaylistManager _PlaylistManager;

    @BeforeEach
    void setUp() {

        _PlaylistMock = mock(PlaylistFileDAO.class);
        _RelationMock = mock(PlaylistRelationsFileDAO.class);
        _PlaylistManager = new PlaylistManager();
    }

    @Test
    void setPlaylistDAO() {
    }

    @Test
    void getAllPlaylists() {
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