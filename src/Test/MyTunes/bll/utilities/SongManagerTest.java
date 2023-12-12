package MyTunes.bll.utilities;

import MyTunes.be.Playlist;
import MyTunes.be.Song;
import MyTunes.dal.file.SongFileDAO;
import MyTunes.dal.interfaces.ISongDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SongManagerTest {

    private ISongDAO _SongMock;
    private SongManager _SongManager;

    private List<Song> allSongs;

    @BeforeEach
    void setUp() {
        _SongMock = mock(SongFileDAO.class);
        _SongManager = new SongManager();
        _SongManager.setSongDAO(_SongMock);

        allSongs = new ArrayList<>();
        allSongs.add(new Song("Ultimate", "C:/music/Denzel Curry - Ultimate.mp3"));
        allSongs.add(new Song("Gold", "C:/music/Imagine Dragons - Gold.mp3"));

        when(_SongMock.getAllSongs()).thenReturn(allSongs);
    }

    @Test
    void getAllSongs() {
        // Arrange - Done in setUp
        // Act
        List<Song> result = _SongManager.getAllSongs();
        // Assert
        assertEquals(allSongs, result);
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