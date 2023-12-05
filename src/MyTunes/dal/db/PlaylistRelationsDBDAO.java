package MyTunes.dal.db;

import MyTunes.be.Playlist;
import MyTunes.be.PlaylistRelation;
import MyTunes.be.Song;
import MyTunes.dal.file.FileDAO;
import MyTunes.dal.file.SongFileDAO;
import MyTunes.dal.interfaces.IPlaylistRelationsDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used make PlaylistRelations sql database CRUD
 * actions. PlaylistRelations contains reference information
 * of which playlist and song that belongs to a specific playlist.
 */

public class PlaylistRelationsDBDAO implements IPlaylistRelationsDAO {

    private DBDataSourceConnector dataSource;

    @Override
    public void getPlaylistSongs(Playlist playlist) {
        dataSource = new DBDataSourceConnector();
        int playlistIds = playlist.getId();
        List<PlaylistRelation> playlistRelations = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT * FROM Playlist_songs WHERE playlist_id =(" + playlistIds + ") ";
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int songId = rs.getInt("song_id");
                int playlistId = rs.getInt("playlist_id");
                int playlistSongOrder = rs.getInt("playlist_song_order");

                if (playlistId != playlist.getId()) continue;
                playlistRelations.add(new PlaylistRelation(playlist, songId, playlistSongOrder));
            }
            SongDBDAO songDAO = new SongDBDAO();
            List<Song> allSongs = songDAO.getAllSongs();
            for (PlaylistRelation playlistRelation : playlistRelations) {
                for (Song song : allSongs) {
                    if (playlistRelation.getSongId() == song.getId()) {
                        playlistRelation.setSong(song);
                        playlist.addSong(playlistRelation);
                    }
                }
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Override
    public void addSongToPlaylist(PlaylistRelation relation) {
        dataSource = new DBDataSourceConnector();
        try (Connection con = dataSource.getConnection()) {

            String sql = "INSERT INTO Playlist_songs (playlist_id, song_id, playlist_song_order) VALUES (?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setInt(1, relation.getPlaylist().getId());
            pstmt.setInt(2, relation.getSong().getId());
            pstmt.setInt(3, relation.getOrderId());

            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Override
    public void removeSongFromPlaylist(Playlist playlist, Song song) {
        dataSource = new DBDataSourceConnector();
        try (Connection con = dataSource.getConnection()) {
            String sql = "DELETE FROM Playlist_songs WHERE playlist_id=? AND song_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, playlist.getId());
            pstmt.setInt(2, song.getId());
            pstmt.executeUpdate();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Override
    public void savePlaylistSongs(Playlist playlist) {
        dataSource = new DBDataSourceConnector();
        try (Connection con = dataSource.getConnection()) {
            List<PlaylistRelation> relations = playlist.getRelations();
            for (PlaylistRelation relation : relations) {
                String sql = "UPDATE Playlist_songs SET playlist_song_order=? WHERE playlist_id=? AND song_id=?";
                PreparedStatement pstmt = con.prepareStatement(sql);

                pstmt.setInt(1, relation.getOrderId());
                pstmt.setInt(2, playlist.getId());
                pstmt.setInt(3, relation.getSong().getId());
                pstmt.executeUpdate();
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
