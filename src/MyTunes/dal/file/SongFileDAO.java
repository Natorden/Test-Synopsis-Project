package MyTunes.dal.file;

import MyTunes.be.Song;
import MyTunes.be.SongOption;
import MyTunes.dal.interfaces.ISongDAO;

import java.util.ArrayList;
import java.util.List;

public class SongFileDAO implements ISongDAO {

    private static final String ADDED_SONGS = "data/added_songs.txt";

    public String formatSongToFileString(Song song){
        return song.getTitle() + "," + song.getFilePath()+","+song.getTime()+","+song.getArtist()+","+song.getCategory()+","+song.getAlbum();
    }

    public int getHighestFileSongID(){
        int highestID = 0; //Retrieve a id before inserting.
        List<Song> songs = getAllSongs();
        for (Song songLoop : songs) {
            if (songLoop.getId() >= highestID) {
                highestID = songLoop.getId() + 1;
            }
        }
        return highestID;
    }

    @Override
    public List<Song> getAllSongs() {
        List<Song> allSongs = new ArrayList<>();
        List<String> results = FileDAO.readFileToList(ADDED_SONGS);
        for (String line : results) {
            if (line == null || line.isBlank()) continue;
            String[] separatedLine = line.split(",");

            Song song;
            if (separatedLine.length < 3) {
                String name = separatedLine[0];
                String fileUri = separatedLine[1];
                song = new Song(name, fileUri);
            } else if(separatedLine.length<4){
                int id = Integer.parseInt(separatedLine[0]);
                String name = separatedLine[1];
                String fileUri = separatedLine[2];
                song = new Song(id, name, fileUri);
            }else{
                int id = Integer.parseInt(separatedLine[0]);
                String name = separatedLine[1];
                String fileUri = separatedLine[2];
                List<SongOption> songOptions=new ArrayList<>();
                if(separatedLine.length>3) songOptions.add(new SongOption("time",separatedLine[3]));
                if(separatedLine.length>4) songOptions.add(new SongOption("artist",separatedLine[4]));
                if(separatedLine.length>5) songOptions.add(new SongOption("category",separatedLine[5]));
                if(separatedLine.length>6) songOptions.add(new SongOption("album",separatedLine[6]));
                song = new Song(id, name, fileUri,songOptions);
            }
            allSongs.add(song);
        }
        return allSongs;
    }

    @Override
    public void insertSong(Song song) {
        int highestID = 0; //Retrieve a id before inserting.
        if (song.getId() == -1) {
            highestID=getHighestFileSongID();
        }
        FileDAO.appendLineToFile(ADDED_SONGS, highestID + "," + formatSongToFileString(song));
        song.setIdOnce(highestID);
    }

    @Override
    public boolean updateSong(Song song) {
        try {
            List<Song> songs = getAllSongs();
            List<String> newSongsList = new ArrayList<>();
            for (Song songloop : songs) {
                if (songloop.getFilePath().equals(song.getFilePath()) || songloop.getTitle().equals(song.getTitle())) {
                    songloop = song;
                }
                int highestID = 0; //Retrieve a id before inserting.
                if (song.getId() == -1) {
                    highestID = getHighestFileSongID();
                } else {
                    highestID = song.getId();
                }
                newSongsList.add(highestID + "," + formatSongToFileString(songloop));
            }
            FileDAO.saveListToFile(ADDED_SONGS, newSongsList);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    @Override
    public boolean deleteSong(Song song) {
        try {
            List<Song> songs = getAllSongs();
            List<String> newSongsList = new ArrayList<>();
            for (Song songloop : songs) {
                if (!songloop.getFilePath().equals(song.getFilePath())) {
                    newSongsList.add(songloop.getMetaDate());
                }
            }
            FileDAO.saveListToFile(ADDED_SONGS, newSongsList);
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }


    }
}
