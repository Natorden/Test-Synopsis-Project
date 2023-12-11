package MyTunes.bll.utilities;

import MyTunes.be.Song;
import MyTunes.dal.file.SongFileDAO;
import MyTunes.dal.interfaces.ISongDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SongManagerTest {

    private ISongDAO _SongMock;
    private SongManager _SongManagerMock;

    private final List<Song> songs = new ArrayList<>();

    @BeforeEach
    void setUp() {
        _SongMock = mock(SongFileDAO.class);
        _SongManagerMock = new SongManager();


        //songs.add();
    }

    @Test
    void setSongDAO() {
    }

    @Test
    void getAllSongs() {
        when(_SongManagerMock.getAllSongs()).thenReturn(songs);

        //assert needed
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