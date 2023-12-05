package MyTunes.be;

import java.util.List;

public class Song {

    private String title;
    private String filePath;
    private int id=-1;

    private String artist="";
    private String category="";
    private String album="";
    private String song="";
    private String time="";

    //Many ways of creating a song, lol.
    public Song(int id,String title, String filePath, String time, String artist, String category, String album){
        this.id=id;
        this.title=title;
        this.filePath=filePath;
        this.time=time;
        this.artist=artist;
        this.category=category;
        this.album=album;
    }
    public Song(String title, String filePath, String time, String artist, String category, String album){
        this.title=title;
        this.filePath=filePath;
        this.time=time;
        this.artist=artist;
        this.category=category;
        this.album=album;
    }
    public Song(int id, String title, String filePath, List<SongOption> songOptions) {
        this.title = title;
        this.filePath = filePath;
        this.id=id;

        for(SongOption option:songOptions){
            switch(option.getOption().toLowerCase()){
                case "artist":
                    this.artist=option.getValueString();
                    break;
                case "category":
                    this.category=option.getValueString();
                    break;
                case "album":
                    this.album=option.getValueString();
                    break;
                case "time":
                    this.time=option.getValueString();
                    break;
            }
        }
    }
    public Song(int id, String title, String filePath) {
        this.title = title;
        this.filePath = filePath;
        this.id=id;
    }
    public Song(String title, String filePath) {
        this.title = title;
        this.filePath = filePath;
    }


    //End of ways to create songs...


    public int getId() { return id; }

    /**
     * Can only be set once. Sets the id for the Song Object.
     * @param wanted_id The id wanted, typically retrieved from the inserted object into the database.
     * @return returns true if set, false if already set.
     */
    public boolean setIdOnce(int wanted_id){
        if(getId()!=-1) return false;
        id=wanted_id;
        return true;
    }

    public String getTitle() {
        return title;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getArtist(){
        return artist;
    }

    public String getCategory(){
        return category;
    }

    public String getAlbum(){
        return album;
    }

    public String getTime(){
        return time;
    }

    public String getSong() { return song; }

    @Override
    public String toString() {
        return this.title;
    }

    public String getMetaDate(){
        String metaData =  this.id + "," + this.title + "," + this.filePath + "," + this.time + "," + this.artist + "," + this.category  + "," + this.album;
        return  metaData;
    }
}

