package MyTunes.bll.utilities;

import MyTunes.be.Playlist;
import MyTunes.be.PlaylistRelation;
import MyTunes.be.Song;
import MyTunes.dal.file.PlaylistFileDAO;
import MyTunes.dal.file.PlaylistRelationsFileDAO;
import MyTunes.dal.interfaces.IPlaylistDAO;
import MyTunes.dal.interfaces.IPlaylistRelationsDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.any;

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
        _PlaylistManager.setPlaylistDAO(_PlaylistMock, _RelationMock);

        allPlaylists = new ArrayList<>();
        allPlaylists.add(new Playlist(0, "Playlist 1"));
        allPlaylists.add(new Playlist(1, "Playlist 2"));

        when(_PlaylistMock.getAllPlaylists()).thenReturn(allPlaylists);
        when(_PlaylistMock.insertPlaylist(any(Playlist.class))).thenReturn(true);
    }

    @Test
    void getAllPlaylists() {
        // Arrange - Done in setUp
        // Act
        List<Playlist> result = _PlaylistManager.getAllPlaylists();
        // Assert
        assertEquals(allPlaylists, result);
    }

    @Test
    void createPlaylist_shouldPass_whenPlaylistCreated() {
        // Arrange
        Playlist expectedPlaylist = new Playlist("Playlist 3");

        // Act
        Playlist actualPlaylist = _PlaylistManager.createPlaylist("Playlist 3");

        // Assert
        verify(_PlaylistMock).insertPlaylist(expectedPlaylist);
        assertEquals(expectedPlaylist, actualPlaylist);
    }

    @Test
    void editPlaylist() {
        Playlist playlist = allPlaylists.get(1);

    }

    @Test
    void removePlaylist() {
    }

    @Test
    void addSongToPlaylist() {
    }

    static Stream<Arguments> songProvider() {
        return Stream.of(
                Arguments.of(new Playlist(1, "Playlist 1"),
                        new Song(1, "Song 1", "C:\\Song1.mp3", "00:00:00", "Artist 1", "Category 1", "Album 1"),
                        new Song(2, "Song 2", "C:\\Song2.mp3", "00:00:00", "Artist 2", "Category 2", "Album 2")));
    }

    @ParameterizedTest
    @MethodSource("songProvider")
    void removeSongFromPlaylist(Playlist playlist, Song song1, Song song2) {
        // Arrange
        playlist.addSong(new PlaylistRelation(playlist, song1.getId()));
        playlist.addSong(new PlaylistRelation(playlist, song2.getId()));
        // Act
        playlist.removeSong(song1);
        // Assert
        assertEquals(1, playlist.getSongs().size());
    }

    @ParameterizedTest
    @MethodSource("songProvider")
    void moveSongUpInPlaylist(Playlist playlist, Song song1, Song song2) {
        // Arrange
        PlaylistRelation relation1 = new PlaylistRelation(playlist, song1.getId(), 1);
        PlaylistRelation relation2 = new PlaylistRelation(playlist, song2.getId(), 2);
        relation1.setSong(song1);
        relation2.setSong(song2);
        playlist.addSong(relation1);
        playlist.addSong(relation2);
        // Act
        _PlaylistManager.moveSongUpInPlaylist(playlist, song2);
        // Assert
        assertEquals(2, playlist.getRelations().getFirst().getOrderId());
    }

    @ParameterizedTest
    @MethodSource("songProvider")
    void moveSongDownInPlaylist(Playlist playlist, Song song1, Song song2) {
        // Arrange
        PlaylistRelation relation1 = new PlaylistRelation(playlist, song1.getId(), 1);
        PlaylistRelation relation2 = new PlaylistRelation(playlist, song2.getId(), 2);
        relation1.setSong(song1);
        relation2.setSong(song2);
        playlist.addSong(relation1);
        playlist.addSong(relation2);
        // Act
        _PlaylistManager.moveSongDownInPlaylist(playlist, song1);
        // Assert
        assertEquals(2, playlist.getRelations().getFirst().getOrderId());
    }
}