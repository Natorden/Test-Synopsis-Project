package MyTunes.bll.utilities;

import MyTunes.be.Song;
import MyTunes.dal.file.SongFileDAO;
import MyTunes.dal.interfaces.ISongDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
        List<Song> allSongs = Arrays.asList();
        when(_SongManagerMock.getAllSongs()).thenReturn(allSongs);

        //assert here (_SongManagerMock.getAllSongs());
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