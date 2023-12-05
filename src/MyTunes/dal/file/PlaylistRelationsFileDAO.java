package MyTunes.dal.file;

import MyTunes.be.Playlist;
import MyTunes.be.PlaylistRelation;
import MyTunes.be.Song;
import MyTunes.dal.interfaces.IPlaylistRelationsDAO;

import java.util.ArrayList;
import java.util.List;

public class PlaylistRelationsFileDAO implements IPlaylistRelationsDAO {

    private static final String PLAYLIST_FILE = "data/playlist_songs.txt";

    public void getPlaylistSongs(Playlist playlist){
        List<PlaylistRelation> playlistRelations=new ArrayList<>();
        List<String> listOfResults = FileDAO.readFileToList(PLAYLIST_FILE);
        if(listOfResults==null) return;
        for (String line : listOfResults) {
            if (line == null) continue;
            String[] separatedLine = line.split(",");
            if(separatedLine.length<2) continue;
            if(Integer.parseInt(separatedLine[0])!=playlist.getId()) continue;
            if(separatedLine.length==3) {
                playlistRelations.add(new PlaylistRelation(playlist, Integer.parseInt(separatedLine[1]), Integer.parseInt(separatedLine[2])));
            }else{
                playlistRelations.add(new PlaylistRelation(playlist, Integer.parseInt(separatedLine[1])));
            }
        }
        SongFileDAO songDAO=new SongFileDAO();
        List<Song> allSongs=songDAO.getAllSongs();
        for(PlaylistRelation playlistRelation:playlistRelations){
            for(Song song:allSongs){
                if(playlistRelation.getSongId()==song.getId()){
                    playlistRelation.setSong(song);
                    playlist.addSong(playlistRelation);
                }
            }
        }
    }

    public void addSongToPlaylist(PlaylistRelation relation){
        FileDAO.appendLineToFile(PLAYLIST_FILE,relation.getPlaylist().getId()+","+relation.getSongId()+","+relation.getOrderId());
    }

    public void removeSongFromPlaylist(Playlist playlist, Song song){
        List<String> newList=new ArrayList<>();
        List<String> listOfResults = FileDAO.readFileToList(PLAYLIST_FILE);
        if(listOfResults==null) return;
        for (String line : listOfResults) {
            if (line == null) continue;
            String[] separatedLine = line.split(",");
            if(separatedLine.length<2) continue;
            if(Integer.parseInt(separatedLine[0])==playlist.getId()&&Integer.parseInt(separatedLine[1])==song.getId()) continue;
            newList.add(line);
        }
        FileDAO.saveListToFile(PLAYLIST_FILE, newList);
    }

    @Override
    public void savePlaylistSongs(Playlist playlist) {
        List<String> newList=new ArrayList<>();
        List<String> listOfResults = FileDAO.readFileToList(PLAYLIST_FILE);
        if(listOfResults==null) return;
        for (String line : listOfResults) {
            if (line == null) continue;
            String[] separatedLine = line.split(",");
            if(separatedLine.length<2) continue;
            for(PlaylistRelation relation:playlist.getRelations()){
                if(relation.getSongId()==Integer.parseInt(separatedLine[1])){
                    line=relation.getPlaylist().getId()+","+relation.getSongId()+","+relation.getOrderId();
                }
            }
            newList.add(line);
        }
        FileDAO.saveListToFile(PLAYLIST_FILE, newList);
    }
}
