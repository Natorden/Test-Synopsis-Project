package MyTunes.bll.utilities;

import MyTunes.dal.file.SongFileDAO;
import MyTunes.dal.interfaces.ISongDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class SongManagerTest {

    private ISongDAO _SongMock;
    private SongManager _SongManagerMock;
    @BeforeEach
    void setUp() {
        _SongMock = mock(SongFileDAO.class);
        _SongManagerMock = new SongManager();

    }

    @Test
    void setSongDAO() {
    }

    @Test
    void getAllSongs() {
    }

    @Test
    void createSong() {
    }

    @Test
    void editSong() {
    }

    @Test
    void removeSong() {
    }

    @Test
    void filterSong() {
    }
}