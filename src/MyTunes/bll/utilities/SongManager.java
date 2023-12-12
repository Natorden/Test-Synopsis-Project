package MyTunes.bll.utilities;

import MyTunes.be.Song;
import MyTunes.dal.db.SongDBDAO;
import MyTunes.dal.file.SongFileDAO;
import MyTunes.dal.interfaces.ISongDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SongManager implements ISongManager {


    private ISongDAO songDAO;
    private final List<Song> songs = new ArrayList<>();

    public SongManager() {
        songDAO = new SongFileDAO();
    }

    public void setSongDAO(ISongDAO songDAO) {
        this.songDAO = songDAO;
    }

    @Override
    public List<Song> getAllSongs(){
        return songDAO.getAllSongs();
    }

    @Override
    public Song createSong(String title, String filePath, String time, String artist, String category, String album) {
        Song song = new Song(title, filePath, time, artist, category, album);
        songDAO.insertSong(song);
        return song;
    }

    @Override
    public void editSong(Song song) {
        songDAO.updateSong(song);
    }

    @Override
    public boolean removeSong(Song song) {
        return songDAO.deleteSong(song);
    }

    @Override
    public List<Song> filterSong(String search) {
        return getAllSongs().stream().filter((song) -> {
            String query = search.toLowerCase().trim().replaceAll("\\s+", ""); //Filter title "name".
            if (song.getTitle().toLowerCase().trim().replaceAll("\\s+", "").contains(query)) return true;
            else if (song.getArtist() != null && !song.getArtist().equals("")) { //Filter artists.
                return song.getArtist().toLowerCase().trim().contains(query);
            }
            return false;
        }).collect(Collectors.toList());
    }


    public static int minutesStringToSeconds(String time){
        if(!time.contains(":")) return Integer.parseInt(time);
        String[] separatedLine = time.split(":");
        int minutes=Integer.parseInt(separatedLine[0]);
        int seconds=Integer.parseInt(separatedLine[1]);
        return minutes*60+seconds;
    }

    /**
     * Used to convert seconds to be shown as minutes and seconds.
     * @param time Time as an int.
     * @return A double that illustrates the time.
     */
    public static String secondsToMinutes(int time) {
        int minutes = time / 60;
        int seconds = time % 60;
        String minutesSeconds = minutes + ":" + seconds;
        if(seconds == 0){
            minutesSeconds += 0;
        }
        else if(seconds < 10){
            minutesSeconds = minutes + ":" + "0" + seconds;
        }
        return minutesSeconds;
    }
}
