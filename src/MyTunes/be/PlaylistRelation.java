package MyTunes.be;

import java.util.Objects;

public class PlaylistRelation {
    private final Playlist playlist;
    private final Integer songId;
    private Integer orderId;
    private Song song;

    public PlaylistRelation(Playlist playlist, int songId, int orderId){
        this.playlist=playlist;
        this.songId=songId;
        this.orderId=orderId;
    }

    public PlaylistRelation(Playlist playlist, int songId){
        this.playlist=playlist;
        this.songId=songId;
        this.orderId=this.playlist.getNextOrderId();
    }

    public void setSong(Song song) {
        if(this.song!=null) return; //Only allow to be set once.
        this.song = song;
    }

    public Song getSong() {
        return song;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public Playlist getPlaylist() {
        return playlist;
    }

    public Integer getSongId() {
        return songId;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        PlaylistRelation other = (PlaylistRelation) obj;
        return Objects.equals(playlist, other.playlist) && Objects.equals(songId, other.songId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playlist, songId);
    }
}
