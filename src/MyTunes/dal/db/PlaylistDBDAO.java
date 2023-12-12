package MyTunes.dal.db;

import MyTunes.be.Playlist;
import MyTunes.dal.interfaces.IPlaylistDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is used make Playlists sql database CRUD
 * actions.
 */

public class PlaylistDBDAO implements IPlaylistDAO {

    private final PlaylistRelationsDBDAO playlistRelationsDBDAO = new PlaylistRelationsDBDAO();
    private DBDataSourceConnector dataSource;


    @Override
    public List<Playlist> getAllPlaylists() {
        dataSource = new DBDataSourceConnector();
        List<Playlist> allPlaylists = new ArrayList<>();
        try (Connection con = dataSource.getConnection()) {
            String sql = "SELECT * FROM Playlists ORDER BY playlist_id";
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("playlist_id");
                String name = rs.getString("playlist_name");


                Playlist p = new Playlist(id, name);
                allPlaylists.add(p);
                playlistRelationsDBDAO.getPlaylistSongs(p);
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return allPlaylists;
    }


    @Override
    public boolean insertPlaylist(Playlist playlist) {
        dataSource = new DBDataSourceConnector();
        try (Connection con = dataSource.getConnection()) {

            String sql = "INSERT INTO Playlists (playlist_name) VALUES (?)";
            PreparedStatement pstmt = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            pstmt.setString(1, playlist.getName());

            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return true;
    }


    @Override
    public void updatePlaylist(Playlist playlist) {
        dataSource = new DBDataSourceConnector();
        try (Connection con = dataSource.getConnection()) {
            String sql = "UPDATE Playlists SET playlist_name=? WHERE playlist_id=?";

            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setString(1, playlist.getName());
            pstmt.setInt(2, playlist.getId());
            pstmt.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    @Override
    public boolean deletePlaylist(Playlist playlist) {
        dataSource = new DBDataSourceConnector();
        try (Connection con = dataSource.getConnection()) {
            String sql = "DELETE FROM Playlists WHERE playlist_id=?";
            PreparedStatement pstmt = con.prepareStatement(sql);

            pstmt.setInt(1, playlist.getId());
            pstmt.executeUpdate();
            return true;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return false;
        }
    }
}
