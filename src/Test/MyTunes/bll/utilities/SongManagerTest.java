package MyTunes.bll.utilities;

import MyTunes.be.Playlist;
import MyTunes.be.Song;
import MyTunes.dal.file.SongFileDAO;
import MyTunes.dal.interfaces.ISongDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SongManagerTest {

    private ISongDAO _SongMock;
    private ISongManager _SongManager;

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
        when(_SongMock.deleteSong(allSongs.get(1))).thenReturn(true);
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
    void removeSong_ShouldReturnTrue_WhenRemovingExistingSong() {
        // Arrange
        Song song = allSongs.get(1);
        // Act
        boolean result = _SongManager.removeSong(song);
        // Assert
        assertTrue(result);
    }
    @Test
    void removeSong_ShouldReturnFlase_WhenRemovingNonExistingSong() {
        // Arrange
        Song song = new Song("FakeSong", "C:/music/FakeBand - FakeSong.mp3");
        // Act
        boolean result = _SongManager.removeSong(song);
        // Assert
        assertFalse(result);
    }

    @Test
    void filterSong() {
    }
}