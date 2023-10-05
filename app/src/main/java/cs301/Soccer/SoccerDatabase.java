package cs301.Soccer;

import android.util.Log;
import cs301.Soccer.soccerPlayer.SoccerPlayer;
import java.io.File;
import java.lang.reflect.Array;
import java.util.*;
import java.io.PrintWriter;
import java.io.IOException;


/**
 * Soccer player database -- presently, all dummied up
 *
 * @author *** put your name here ***
 * @version *** put date of completion here ***
 *
 */
public class SoccerDatabase implements SoccerDB {

    // dummied up variable; you will need to change this
    private Hashtable<String,SoccerPlayer> database = new Hashtable<>();
    private String makeKey(String fName, String lName){
       return fName + " ## " + lName;
    }

    /**
     * add a player
     *
     * @see SoccerDB#addPlayer(String, String, int, String)
     */
    @Override
    public boolean addPlayer(String firstName, String lastName,
                             int uniformNumber, String teamName) {
        String key = makeKey(firstName, lastName);
        if(database.containsKey(key)){ return false; }

        SoccerPlayer player = new SoccerPlayer(firstName, lastName, uniformNumber, teamName);
        database.put(key, player);
        Log.d("addPlayer", database.get(key).toString());
        return true;
    }

    /**
     * remove a player
     *
     * @see SoccerDB#removePlayer(String, String)
     */
    @Override
    public boolean removePlayer(String firstName, String lastName) {
        // Create a hashtable key using the createKey method
        String key = makeKey(firstName, lastName);

        // Check if the player exists in the database
        if(database.containsKey(key)) {
            // If the player exists, remove them and return true
            database.remove(key);
            return true;
        }

        // If the player doesn't exist, return false
        return false;
    }


    /**
     * look up a player
     *
     * @see SoccerDB#getPlayer(String, String)
     */
    @Override
    public SoccerPlayer getPlayer(String firstName, String lastName) {
        // Create a hashtable key using the createKey method
        String key = makeKey(firstName, lastName);

        // Look up the SoccerPlayer object using the key
        SoccerPlayer player = database.get(key);

        // If the player object is not null, it means the player exists in the database
        // so we return the player object
        // If the player object is null, it means the player doesn't exist, so we return null
        return player;
    }


    /**
     * increment a player's goals
     *
     * @see SoccerDB#bumpGoals(String, String)
     */
    @Override
    public boolean bumpGoals(String firstName, String lastName) {
        String key = makeKey(firstName, lastName);
        if(!database.containsKey(key)) { return false; }
        database.get(key).bumpGoals();
        return true;
    }

    /**
     * increment a player's yellow cards
     *
     * @see SoccerDB#bumpYellowCards(String, String)
     */
    @Override
    public boolean bumpYellowCards(String firstName, String lastName) {
        String key = makeKey(firstName, lastName);
        if(!database.containsKey(key)) { return false; }
        database.get(key).bumpYellowCards();
        return true;
    }

    /**
     * increment a player's red cards
     *
     * @see SoccerDB#bumpRedCards(String, String)
     */
    @Override
    public boolean bumpRedCards(String firstName, String lastName) {
        String key = makeKey(firstName, lastName);
        if(!database.containsKey(key)) { return false; }
        database.get(key).bumpRedCards();
        return true;
    }

    /**
     * tells the number of players on a given team
     *
     * @see SoccerDB#numPlayers(String)
     */
    @Override
    public int numPlayers(String teamName) {
        if(teamName == null) {
            return database.size();
        }

        int count = 0;
        for(SoccerPlayer player : database.values()) {
            if(player.getTeamName().equals(teamName)) {
                count++;
            }
        }
        return count;
    }


    /**
     * gives the nth player on a the given team
     *
     * @see SoccerDB#playerIndex(int, String)
     */
    // get the nTH player
    @Override
    public SoccerPlayer playerIndex(int idx, String teamName) {
        // Check if index is out of bounds
        if(idx >= numPlayers(teamName)) {
            return null;
        }

        int count = 0;
        for(SoccerPlayer player : database.values()) {
            // If a team name is specified, only consider players from that team
            if(teamName != null && !player.getTeamName().equals(teamName)) {
                continue;
            }

            if(count == idx) {
                return player;
            }
            count++;
        }

        return null;
    }


    /**
     * reads database data from a file
     *
     * @see SoccerDB#readData(File)
     */
    // read data from file
    @Override
    public boolean readData(File file) {
        Scanner scanner = null;

        try {
            scanner = new Scanner(file);

            // Clear the current database to ensure old data doesn't interfere
            database.clear();

            while (scanner.hasNext()) {
                String firstName = scanner.nextLine();
                String lastName = scanner.nextLine();
                int uniformNumber = Integer.parseInt(scanner.nextLine());
                String teamName = scanner.nextLine();
                int goals = Integer.parseInt(scanner.nextLine());
                int yellowCards = Integer.parseInt(scanner.nextLine());
                int redCards = Integer.parseInt(scanner.nextLine());

                SoccerPlayer player = new SoccerPlayer(firstName, lastName, uniformNumber, teamName);
                database.put(makeKey(firstName, lastName), player);

                // Bump goals, yellow cards, and red cards
                for (int i = 0; i < goals; i++) {
                    player.bumpGoals();
                }
                for (int i = 0; i < yellowCards; i++) {
                    player.bumpYellowCards();
                }
                for (int i = 0; i < redCards; i++) {
                    player.bumpRedCards();
                }
            }
            return true;
        } catch (IOException e) {
            Log.e("readData", "Error reading data from file", e);
            return false;
        } finally {
            if (scanner != null) {
                scanner.close();
            }
        }
    }

    @Override
    public HashSet<String> getTeams() {
        HashSet<String> teams = new HashSet<>();
        for (SoccerPlayer player : database.values()) {
            teams.add(player.getTeamName());
        }
        return teams;
    }

    // write data to file
    @Override
    public boolean writeData(File file) {
        PrintWriter pw = null;
        Log.i("written",file.getAbsolutePath());
        try {
            pw = new PrintWriter(file);
            for (SoccerPlayer player : database.values()) {
                pw.println(logString(player.getFirstName()));
                pw.println(logString(player.getLastName()));
                pw.println(logString(String.valueOf(player.getUniform())));
                pw.println(logString(player.getTeamName()));
                pw.println(logString(String.valueOf(player.getGoals())));
                pw.println(logString(String.valueOf(player.getYellowCards())));
                pw.println(logString(String.valueOf(player.getRedCards())));
            }
            return true;
        } catch (IOException e) {
            Log.e("writeData", "Error writing data to file", e);
            return false;
        } finally {
            if (pw != null) {
                pw.close();
            }
        }
    }


    /**
     * helper method that logcat-logs a string, and then returns the string.
     * @param s the string to log
     * @return the string s, unchanged
     */
    private String logString(String s) {
        Log.i("write string", s);
        return s;
    }

    /**
     * returns the list of team names in the database
     *
     * @see SoccerDB#getTeams()
     */
    // return list of teams

    /**
     * Helper method to empty the database and the list of teams in the spinner;
     * this is faster than restarting the app
     */
    public boolean clear() {
        if(database != null) {
            database.clear();
            return true;
        }
        return false;
    }
}
