package MyTunes.be;

import MyTunes.bll.utilities.SongManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;


public class Playlist {

    private int id=-1;
    private final String name;
    private final List<PlaylistRelation> relations = new ArrayList<>();

    public Playlist(int id, String name) {
        this(name);
        this.id = id;
    }
    public Playlist(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId(){ return id; }

    public List<Song> getSongs() {
        List<Song> songsToBeSent=new ArrayList<>();
        relations.sort(Comparator.comparing(PlaylistRelation::getOrderId));
        for(PlaylistRelation relation: relations){
            songsToBeSent.add(relation.getSong());
        }
        return songsToBeSent;
    }

    public List<PlaylistRelation> getRelations() {
        return relations;
    }

    /**
     * Can only be set once. Sets the id for the Song Object.
     *
     * @param wanted_id The id wanted, typically retrieved from the inserted object into the database.
     */
    //Can only be set once.
    public void setIdOnce(int wanted_id){
        if(getId() == -1) {
            return;
        }
        id=wanted_id;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Adds a song to the songs list.
     * @param relation The song to add.
     */
    //@todo possibly rename this method to addRelation instead.?
    public void addSong(PlaylistRelation relation){
        if(relations.contains(relation)) return;
        relations.add(relation);
    }
    /**
     * Removes a song from the list.
     * @param song The song to add.
     */
    public boolean removeSong(Song song){
        for(PlaylistRelation relation:relations){
            if(relation.getSongId()==song.getId()){
                relations.remove(relation);
                return true;
            }
        }
        return false;
    }

    public int getNextOrderId(){
        return this.relations.size()+1;
    }

    public int getRelationsSize(){ return this.relations.size(); }

    public String getTotalSongsTime(){
        int time = 0;
        for(PlaylistRelation relation : this.relations){
            Song song=relation.getSong();
            time += SongManager.minutesStringToSeconds(song.getTime());
        }
        return SongManager.secondsToMinutes(time);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Playlist other = (Playlist) obj;
        return Objects.equals(id, other.id) && Objects.equals(name, other.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
