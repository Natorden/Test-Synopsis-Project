package MyTunes.bll.utilities;

import MyTunes.be.Playlist;
import MyTunes.be.PlaylistRelation;
import MyTunes.be.Song;
import java.util.Arrays;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

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
            when(_PlaylistMock.deletePlaylist(any(Playlist.class))).thenReturn(true);
            when(_PlaylistMock.updatePlaylist(any(Playlist.class))).thenReturn(true);
        }

        @Test
        void getAllPlaylists() {
            // Arrange - Done in setUp
            // Act
            List<Playlist> result = _PlaylistManager.getAllPlaylists();
            // Assert
            verify(_PlaylistMock).getAllPlaylists();
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
        void editPlaylist_ShouldReturnTrue_whenUpdatingExistingPlaylist() {
            // Arrange
            Playlist playlist = allPlaylists.get(1);
            playlist.addSong(new PlaylistRelation(playlist, 1));
            // Act
            boolean result = _PlaylistManager.editPlaylist(playlist);
            // Assert
            verify(_PlaylistMock).updatePlaylist(playlist);
            assertTrue(result);
        }

        @Test
        void removePlaylist_ShouldReturnTrue_WhenRemovingExistingPlaylist() {
            // Arrange
            Playlist playlist = new Playlist("Playlist 1");
            // Act
            boolean result = _PlaylistManager.removePlaylist(playlist);
            // Assert
            verify(_PlaylistMock).deletePlaylist(playlist);
            assertTrue(result);
        }

        static Stream<Arguments> addSongToPlaylistTestData() {
            Playlist playListWithSongs = new Playlist("Playlist with 2 songs");
            playListWithSongs.addSong(new PlaylistRelation(playListWithSongs, 1));
            playListWithSongs.addSong(new PlaylistRelation(playListWithSongs, 2));

            return Stream.of(
                Arguments.of(playListWithSongs),
                Arguments.of(new Playlist("Empty Playlist")));
        }

        @ParameterizedTest
        @MethodSource("addSongToPlaylistTestData")
        void addSongToPlaylist() {
            // Arrange
            Song songToAdd = new Song("ExampleSong", "C:/music/ExampleBand - ExampleSong.mp3");
            Playlist playlist = new Playlist(1, "Playlist 1");
            // Act
            _PlaylistManager.addSongToPlaylist(playlist, songToAdd);
            Song lastSong = playlist.getSongs().get(playlist.getSongs().size() - 1);
            // Assert
            verify(_RelationMock)
                .addSongToPlaylist(new PlaylistRelation(playlist, songToAdd.getId(),
                    playlist.getNextOrderId()));
            assertEquals(lastSong, songToAdd);
        }

        static Stream<Arguments> removeSongFromPlaylist() {
            return Stream.of(
                Arguments.of(new Playlist(1, "Playlist 1"),
                    Arrays.asList(
                        new Song(1, "Song 1", "C:\\Song1.mp3", "00:00:00", "Artist 1", "Category 1", "Album 1"),
                        new Song(2, "Song 2", "C:\\Song2.mp3", "00:00:00", "Artist 2", "Category 2", "Album 2")),
                    1, 0),
                Arguments.of(new Playlist(2, "Playlist 2"),
                    Arrays.asList(
                        new Song(1, "Song 1", "C:\\Song1.mp3", "00:00:00", "Artist 1", "Category 1", "Album 1"),
                        new Song(2, "Song 2", "C:\\Song2.mp3", "00:00:00", "Artist 2", "Category 2", "Album 2"),
                        new Song(3, "Song 3", "C:\\Song3.mp3", "00:00:00", "Artist 3", "Category 3", "Album 3")),
                    2,
                    2));
        }

        @ParameterizedTest
        @MethodSource("removeSongFromPlaylist")
        void removeSongFromPlaylist_shouldPass_whenSongRemoved(Playlist playlist,
            List<Song> songs, int expected, int index) {
            // Arrange
            for (Song song : songs) {
                playlist.addSong(new PlaylistRelation(playlist, song.getId()));
            }
            // Act
            _PlaylistManager.removeSongFromPlaylist(playlist, songs.get(index));
            // Assert
            verify(_RelationMock).removeSongFromPlaylist(playlist, songs.get(index));
            assertEquals(expected, playlist.getSongs().size());
        }

        @Test
        void removeSongFromPlaylist_shouldPass_whenSongNotInPlaylist() {
            // Arrange
            Playlist playlist = new Playlist(1, "Playlist 1");
            Song song1 = new Song(1, "Song 1", "C:\\Song1.mp3", "00:00:00", "Artist 1", "Category 1", "Album 1");
            Song song2 = new Song(2, "Song 2", "C:\\Song2.mp3", "00:00:00", "Artist 2", "Category 2", "Album 2");
            playlist.addSong(new PlaylistRelation(playlist, song1.getId()));
            // Act
            playlist.removeSong(song2);
            // Assert
            assertEquals(1, playlist.getSongs().size());
        }

        static Stream<Arguments> moveSongDownInPlaylist() {
            return Stream.of(
                Arguments.of(
                    new Playlist(1, "Playlist 1"),
                    Arrays.asList(
                        new Song(1, "Song 1", "C:\\Song1.mp3", "00:00:00", "Artist 1", "Category 1", "Album 1"),
                        new Song(2, "Song 2", "C:\\Song2.mp3", "00:00:00", "Artist 2", "Category 2", "Album 2")),
                    2,
                    1,
                    0),
                Arguments.of(
        new Playlist(2, "Playlist 2"),
                    Arrays.asList(
                        new Song(1, "Song 1", "C:\\Song1.mp3", "00:00:00", "Artist 1", "Category 1", "Album 1"),
                        new Song(2, "Song 2", "C:\\Song2.mp3", "00:00:00", "Artist 2", "Category 2", "Album 2"),
                        new Song(3, "Song 3", "C:\\Song3.mp3", "00:00:00", "Artist 3", "Category 3", "Album 3")),
                    3,
                    2,
                    1));
        }

        @ParameterizedTest
        @MethodSource("moveSongDownInPlaylist")
        void moveSongUpInPlaylist(Playlist playlist, List<Song> songs, int expected, int index, int index2) {
            // Arrange
            for (Song song : songs) {
                PlaylistRelation relation = new PlaylistRelation(playlist, song.getId());
                relation.setSong(song);
                playlist.addSong(relation);
                _PlaylistManager.addSongToPlaylist(playlist, song);
            }
            // Act
            _PlaylistManager.moveSongUpInPlaylist(playlist, songs.get(index));
            // Assert
            assertEquals(expected, playlist.getRelations().get(index2).getOrderId());
        }

        static Stream<Arguments> moveSongUpInPlaylist() {
            return Stream.of(
                    Arguments.of(new Playlist(1, "Playlist 1"),
                    Arrays.asList(
                        new Song(1, "Song 1", "C:\\Song1.mp3", "00:00:00", "Artist 1", "Category 1", "Album 1"),
                        new Song(2, "Song 2", "C:\\Song2.mp3", "00:00:00", "Artist 2", "Category 2", "Album 2")),
                        2, 0, 0),
                    Arguments.of(new Playlist(2, "Playlist 2"),
                        Arrays.asList(
                        new Song(1, "Song 1", "C:\\Song1.mp3", "00:00:00", "Artist 1", "Category 1", "Album 1"),
                        new Song(2, "Song 2", "C:\\Song2.mp3", "00:00:00", "Artist 2", "Category 2", "Album 2"),
                        new Song(3, "Song 3", "C:\\Song3.mp3", "00:00:00", "Artist 3", "Category 3", "Album 3")),
                        2, 1, 2));
        }

        @ParameterizedTest
        @MethodSource("moveSongUpInPlaylist")
        void moveSongDownInPlaylist(Playlist playlist, List<Song> songs, int expected, int index, int index2) {
            // Arrange
            for (Song song : songs) {
                PlaylistRelation relation = new PlaylistRelation(playlist, song.getId());
                relation.setSong(song);
                playlist.addSong(relation);
                _PlaylistManager.addSongToPlaylist(playlist, song);
            }
            // Act
            _PlaylistManager.moveSongDownInPlaylist(playlist, songs.get(index));
            // Assert
            assertEquals(expected, playlist.getRelations().get(index2).getOrderId());
        }
}