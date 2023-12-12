package MyTunes.dal.db;

import MyTunes.be.Song;
import MyTunes.dal.interfaces.ISongDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used make Song sql database CRUD
 * actions.
 */

public class SongDBDAO implements ISongDAO {

    private DBDataSourceConnector dataSource;


    @Override
    public List<Song> getAllSongs() {
        dataSource = new DBDataSourceConnector();
        List<Song> allSongs = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT * FROM Songs ORDER BY song_id";
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("song_id");
                String title = rs.getString("song_title");
                String filepath = rs.getString("song_filepath");
                String time = rs.getString("song_time");
                String artist = rs.getString("song_artist");
                String category = rs.getString("song_category");
                String album = rs.getString("song_album");

                Song s = new Song(id, title, filepath, time, artist, category, album);
                allSongs.add(s);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allSongs;
    }


    @Override
    public void insertSong(Song song) {
        dataSource = new DBDataSourceConnector();
        try (Connection con = dataSource.getConnection()) {

            String sql = "INSERT INTO Songs (song_title,song_filePath,song_time,song_artist,song_category,song_album) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            setPreparedSongValues(song, pstmt);
            pstmt.executeUpdate();
    } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setPreparedSongValues(Song song, PreparedStatement pstmt) throws SQLException {
        pstmt.setString(1, song.getTitle());
        pstmt.setString(2, song.getFilePath());
        pstmt.setString(3, song.getTime());
        pstmt.setString(4, song.getArtist());
        pstmt.setString(5, song.getCategory());
        pstmt.setString(6, song.getAlbum());
    }


    @Override
    public boolean updateSong(Song song) {
        dataSource = new DBDataSourceConnector();
        try (Connection con = dataSource.getConnection()) {

            String sql = "UPDATE Songs SET song_title=?, song_filepath=?, song_time=?, song_artist=?, song_category=?, song_album=? WHERE song_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);

            setPreparedSongValues(song, pstmt);
            pstmt.setInt(7, song.getId());
            pstmt.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }


    @Override
    public boolean deleteSong(Song song) {
        dataSource = new DBDataSourceConnector();
        try (Connection con = dataSource.getConnection()) {
            String sql = "DELETE FROM Songs WHERE song_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, song.getId());
            pstmt.executeUpdate();
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }


}
