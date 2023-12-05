package MyTunes.dal.db;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JDBCDAO {
    protected static boolean useSQL=false;
    private static String SQL_HOST="10.176.111.31";
    private static String SQL_USERNAME="CSe20B_24";
    private static String SQL_PASSWORD="hunden123";
    private static String SQL_PORT="1433";
    private static String SQL_DB="MyTunes2020";

    /**
     * Used to check if necessary parameters for establishing a database connection is set.
     * @return true if configuration seems okay. False if not.
     */
    private static boolean checkConfiguration() {
        boolean state=true;
        if (SQL_HOST == null) {
            System.out.println("SQL_HOST not set.");
            state=false;
        }
        if (SQL_PASSWORD == null) {
            System.out.println("SQL_PASSWORD not set.");
            state=false;
        }
        if (SQL_USERNAME == null) {
            System.out.println("SQL_USERNAME not set.");
            state=false;
        }
        if (SQL_DB == null) {
            System.out.println("SQL_DB not set.");
            state=false;
        }
        return state;
    }

    /**
     * Used to retrieve a list of strings, each string corresponding to a row.
     * @param source Table to select from.
     * @param columnSelects Rows to select.
     * @return List<String> of rows found returned.
     */
    protected static List<String> selectSongToStringList(String source, String columnSelects) {
        if(!checkConfiguration()) return null;
        List<String> returnList = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlserver://"+SQL_HOST+":"+SQL_PORT+";database="+SQL_DB+";user="+SQL_USERNAME+";password="+SQL_PASSWORD+";");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + columnSelects + " FROM " + source);
            while (rs.next()) {
                returnList.add(rs.getInt("song_id") + "," + rs.getString("song_title") + "," + rs.getString("song_filepath") + "," + rs.getString("song_time") + "," + rs.getString("song_artist") + "," + rs.getString("song_category") + "," + rs.getString("song_album"));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }

    /**
     * Used to retrieve a list of strings, each string corresponding to a row.
     * @param source Table to select from.
     * @param columnSelects Rows to select.
     * @return List<String> of rows found returned.
     */
    protected static List<String> selectPlaylistToStringList(String source, String columnSelects) {
        if(!checkConfiguration()) return null;
        List<String> returnList = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlserver://"+SQL_HOST+":"+SQL_PORT+";database="+SQL_DB+";user="+SQL_USERNAME+";password="+SQL_PASSWORD+";");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT " + columnSelects + " FROM " + source);
            while (rs.next()) {
                returnList.add(rs.getInt("playlist_id") + "," + rs.getString("playlist_name"));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }

    /**
     * Used to retrieve a list of strings, each string corresponding to a row.
     * @param source Table to select from.
     * @param columnSelects Rows to select.
     * @param additional Additional SQL to append after SELECT FROM, example could be: "WHERE id=`someID`".
     * @return List<String> of rows found returned.
     */
    protected static List<String> selectPlaylistSongToStringList(String source, String columnSelects, String additional) {
        if(!checkConfiguration()) return null;
        List<String> returnList = new ArrayList<>();
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlserver://"+SQL_HOST+":"+SQL_PORT+";database="+SQL_DB+";user="+SQL_USERNAME+";password="+SQL_PASSWORD+";");
            Statement stmt = con.createStatement();
            System.out.println("SELECT " + columnSelects + "FROM " + source + " " + additional);
            ResultSet rs = stmt.executeQuery("SELECT " + columnSelects + " FROM " + source + " " + additional);
            while (rs.next()) {
                returnList.add(rs.getInt("playlist_id")+","+rs.getInt("song_id")+","+rs.getInt("playlist_song_order"));
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;
    }

    /**
     * Inserts to table the data.
     * @param source Table to query.
     * @param insertColumns the columns to insert into formatted as such: "column1,column2,column3"
     * @param insertData The data to insert into the columns formatted as such: "insertDataColumn1,insertDataColumn2,insertDataColumn3"
     * @param getInsertID boolean, true to get id.
     * @return Returns integer with respective id value for the insert, if there is one. If not it just returns -1.
     */
    protected static int insertToTable(String source, String insertColumns, String insertData,boolean getInsertID) {
        String insertFormatted="INSERT INTO "+source+" ("+insertColumns+") VALUES ("+insertData+")";
        if(!checkConfiguration()) return -1;
        int insertId = -1;
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlserver://"+SQL_HOST+":"+SQL_PORT+";database="+SQL_DB+";user="+SQL_USERNAME+";password="+SQL_PASSWORD+";");
            Statement stmt = con.createStatement();
            int affectedRows=stmt.executeUpdate(insertFormatted, Statement.RETURN_GENERATED_KEYS);

            if (affectedRows == 0) {
                throw new SQLException("insertToTable failed, no rows affected.");
            }

            if(getInsertID){
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    insertId=rs.getInt("GENERATED_KEYS");
                }
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return insertId;
    }

    /**
     * Inserts to table the data.
     * @param source Table to query.
     * @param insertColumns @todo
     * @param insertData @todo
     */
    protected static void insertToTable(String source, String insertColumns, String insertData) {
        executeUpdate("INSERT INTO "+source+" ("+insertColumns+") VALUES ("+insertData+")");
    }

    /**
     * Delete row from table.
     * @param source Table to query.
     * @param whereCondition Which row to insert to, use where condition for example formatted: "id=0" or: "id=0 AND name='Hello World'"
     * @return Returns integer with respective id value for the insert, if there is one. If not it just returns -1.
     */
    protected static void deleteFromTable(String source, String whereCondition){
        executeUpdate("DELETE FROM "+source+" WHERE "+whereCondition);
    }

    /**
     * Update to table the data.
     * @param source Table to query.
     * @param setQuery the update query formatted as such: "column1=value1,column2=value2,column3=value3".
     * @param whereCondition Which row to insert to, use where condition for example formatted: "id=0" or: "id=0 AND name='Hello World'"
     */
    protected static void updateToTable(String source,String setQuery, String whereCondition){
        executeUpdate("UPDATE "+source+" SET "+setQuery+" WHERE "+whereCondition);
    }

    /**
     * Private method used instead of using duplicate code, basically for any queries where returns isn't necessary.
     * @param query Query.
     */
    private static void executeUpdate(String query){
        if(!checkConfiguration()) return;
        try {
            Connection con = DriverManager.getConnection("jdbc:sqlserver://"+SQL_HOST+":"+SQL_PORT+";database="+SQL_DB+";user="+SQL_USERNAME+";password="+SQL_PASSWORD+";");
            Statement stmt = con.createStatement();
            stmt.executeUpdate(query);
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
