package MyTunes.bll.utilities;

import MyTunes.be.Playlist;
import MyTunes.be.PlaylistRelation;
import MyTunes.be.Song;
import MyTunes.dal.db.PlaylistDBDAO;
import MyTunes.dal.db.PlaylistRelationsDBDAO;
import MyTunes.dal.file.PlaylistFileDAO;
import MyTunes.dal.file.PlaylistRelationsFileDAO;
import MyTunes.dal.interfaces.IPlaylistDAO;
import MyTunes.dal.interfaces.IPlaylistRelationsDAO;

import java.util.List;

public class PlaylistManager implements IPlaylistManager {
    private IPlaylistDAO playlistDAO;
    private IPlaylistRelationsDAO playlistRelationsDAO;
    private List<Playlist> allPlaylists;

    public PlaylistManager(){
        playlistDAO = new PlaylistFileDAO();
        playlistRelationsDAO=new PlaylistRelationsFileDAO();
    }


    @Override
    public void setPlaylistDAO(IPlaylistDAO songDAOm, IPlaylistRelationsDAO relationsDAO) {
        this.playlistDAO = songDAOm;
        this.playlistRelationsDAO = relationsDAO;
    }

    @Override
    public List<Playlist> getAllPlaylists()  {
        return playlistDAO.getAllPlaylists();
    }

    @Override
    public Playlist createPlaylist(String name) {
        Playlist playlist=new Playlist(name);
        playlistDAO.insertPlaylist(playlist);
        return playlist;
    }

    @Override
    public void editPlaylist(Playlist playlist) {
        playlistDAO.updatePlaylist(playlist);
    }

    @Override
    public void removePlaylist(Playlist playlist) {
        playlistDAO.deletePlaylist(playlist);
    }

    @Override
    public void addSongToPlaylist(Playlist playlist, Song song) {
        PlaylistRelation playlistRelation =new PlaylistRelation(playlist,song.getId(),playlist.getNextOrderId());
        playlistRelation.setSong(song);
        playlistRelationsDAO.addSongToPlaylist(playlistRelation);
        playlist.addSong(playlistRelation);
    }

    @Override
    public void removeSongFromPlaylist(Playlist playlist, Song song) {
        playlistRelationsDAO.removeSongFromPlaylist(playlist,song);
        playlist.removeSong(song);
    }

    @Override
    public void moveSongUpInPlaylist(Playlist playlist, Song song) {
        List<PlaylistRelation> relations=playlist.getRelations();
        for(int i=0;i<relations.size();i++){
            PlaylistRelation relation=relations.get(i);
            if(relation.getSong()==song){
                if(relation.getOrderId()==1) return; //Its already at top.
                relation.setOrderId(relation.getOrderId()-1);
                relations.get(i-1).setOrderId(relation.getOrderId()+1);
            }
        }
        playlistRelationsDAO.savePlaylistSongs(playlist);
    }

    @Override
    public void moveSongDownInPlaylist(Playlist playlist, Song song) {
        List<PlaylistRelation> relations=playlist.getRelations();
        for(int i=0;i<relations.size();i++){
            PlaylistRelation relation=relations.get(i);
            if(relation.getSong()==song){
                if(relation.getOrderId()+1==playlist.getNextOrderId()) return; //Its already at the bottom.
                relation.setOrderId(relation.getOrderId()+1);
                relations.get(i+1).setOrderId(relation.getOrderId()-1);
            }
        }
        playlistRelationsDAO.savePlaylistSongs(playlist);
    }


}

