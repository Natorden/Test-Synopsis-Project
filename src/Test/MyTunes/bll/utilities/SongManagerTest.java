package MyTunes.bll.utilities;

import MyTunes.be.Playlist;
import MyTunes.be.Song;
import MyTunes.dal.file.SongFileDAO;
import MyTunes.dal.interfaces.ISongDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
        when(_SongMock.updateSong(any(Song.class))).thenReturn(true);
    }

    @Test
    void getAllSongs() {
        // Arrange - Done in setUp
        // Act
        List<Song> result = _SongManager.getAllSongs();
        // Assert
        verify(_SongMock).getAllSongs();
        assertEquals(allSongs, result);
    }

    @Test
    void createSong() {
        // Arrange
        Song expectedSong = new Song("Ultimate2", "C:/music/Denzel Curry - Ultimate.mp3", "", "", "", "");

        // Act
        Song actualSong = _SongManager.createSong("Ultimate2", "C:/music/Denzel Curry - Ultimate.mp3", "", "", "", "");

        // Assert
        verify(_SongMock).insertSong(expectedSong);
        assertEquals(expectedSong, actualSong);
    }

    @Test
    void editSong() {
        // Arrange
        Song song = allSongs.get(0);
        Song newSong = new Song(song.getId(), "New Title", song.getFilePath());
        // Act
        boolean result = _SongManager.editSong(newSong);
        // Assert
        verify(_SongMock).updateSong(newSong);
        assertTrue(result);
    }

    @Test
    void removeSong_ShouldReturnTrue_WhenRemovingExistingSong() {
        // Arrange
        Song song = allSongs.get(1);
        // Act
        boolean result = _SongManager.removeSong(song);
        // Assert
        verify(_SongMock).deleteSong(song);
        assertTrue(result);
    }
    @Test
    void removeSong_ShouldReturnFalse_WhenRemovingNonExistingSong() {
        // Arrange
        Song song = new Song("FakeSong", "C:/music/FakeBand - FakeSong.mp3");
        // Act
        boolean result = _SongManager.removeSong(song);
        // Assert
        verify(_SongMock).deleteSong(song);
        assertFalse(result);
    }

    static Stream<Arguments> filterSongTestData() {
        List<Song> songs = new ArrayList<>();
        songs.add(new Song("Ultimate", "C:/music/Denzel Curry - Ultimate.mp3"));
        songs.add(new Song("Gold", "C:/music/Imagine Dragons - Gold.mp3"));
        List<Song> expectedSongsForUlti = new ArrayList<>();
        expectedSongsForUlti.add(songs.get(0));
        return Stream.of(
                Arguments.of("Ulti", expectedSongsForUlti),
                Arguments.of("l", songs),
                Arguments.of("z", new ArrayList<Song>())
        );
    }
    @ParameterizedTest
    @MethodSource("filterSongTestData")
    void filterSong_ShouldReturnMatchingSongs(String filter, ArrayList<Song> expectedSongs) {
        // Arrange - Done in MethodSource
        // Act
        List<Song> returnedSongs = _SongManager.filterSong(filter);
        // Assert
        assertEquals(expectedSongs, returnedSongs);
    }
}