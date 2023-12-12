package MyTunes.dal.file;

import MyTunes.be.Playlist;
import MyTunes.dal.interfaces.IPlaylistDAO;

import java.util.ArrayList;
import java.util.List;

public class PlaylistFileDAO implements IPlaylistDAO {

    private static final String PLAYLIST_FILE = "data/playlists.txt";
    private final PlaylistRelationsFileDAO playlistRelationsFileDAO = new PlaylistRelationsFileDAO();

    public int getHighestFilePlaylistID(){
        int highestID = 0; //Retrieve a id before inserting.
        List<Playlist> playlists = getAllPlaylists();
        for (Playlist playlist : playlists) {
            if (playlist.getId() >= highestID) {
                highestID = playlist.getId() + 1;
            }
        }
        return highestID;
    }

    @Override
    public List<Playlist> getAllPlaylists() {
        List<Playlist> allPlaylists = new ArrayList<>();
        List<String> listOfResults= FileDAO.readFileToList(PLAYLIST_FILE);
        for (String line : listOfResults) {
            if (line == null) continue;
            String[] separatedLine = line.split(",");

            if(separatedLine.length==1) {
                String name = separatedLine[0];
                Playlist playlist = new Playlist(name);
                allPlaylists.add(playlist);
            }else{
                int id=Integer.parseInt(separatedLine[0]);
                String name = separatedLine[1];
                Playlist playlist = new Playlist(id,name);
                allPlaylists.add(playlist);
                playlistRelationsFileDAO.getPlaylistSongs(playlist);
            }
        }
        return allPlaylists;
    }

    @Override
    public boolean insertPlaylist(Playlist playlist) {
        int highestID = 0; //Retrieve a id before inserting.
        if (playlist.getId() == -1) { highestID=getHighestFilePlaylistID(); }
        FileDAO.appendLineToFile(PLAYLIST_FILE,highestID+","+playlist.getName());
        playlist.setIdOnce(highestID);
        return true;
    }

    @Override
    public void updatePlaylist(Playlist playlist) {
        List<Playlist> playlists=getAllPlaylists();
        List<String> newPlaylistList=new ArrayList<>();
        for(Playlist playlistloop:playlists){
            if(playlistloop.getId()==playlist.getId()){
                playlistloop=playlist;
            }
            newPlaylistList.add(playlistloop.getId()+","+playlistloop.getName());
        }
        FileDAO.saveListToFile(PLAYLIST_FILE,newPlaylistList);
    }

    @Override
    public void deletePlaylist(Playlist playlist) {
        List<Playlist> playlists = getAllPlaylists();
        List<String> newPlaylistsList = new ArrayList<>();
        for (Playlist playlistLoop: playlists) {
            if (playlistLoop.getId()!=playlist.getId()) {
                newPlaylistsList.add(playlistLoop.getId() + "," + playlistLoop.getName());
            }
        }
        FileDAO.saveListToFile(PLAYLIST_FILE, newPlaylistsList);
    }
}
