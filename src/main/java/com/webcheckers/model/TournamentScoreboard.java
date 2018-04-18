package com.webcheckers.model;

import java.util.LinkedList;
import java.util.logging.Logger;

public class TournamentScoreboard {

    private static final Logger LOG = Logger.getLogger(TournamentScoreboard.class.getName());


    LinkedList<Player> players;

    public TournamentScoreboard(){
        players = new LinkedList<>();
        LOG.info("Tournament Scoreboard has been created.");
    }


    public void sortPlayers(){
        LOG.info("Sorting...");
        int size = players.size();
        LinkedList<Player> newList = new LinkedList<>();

        // Iterates through newList
        for(int newbies = 0; newbies < size; newbies++){
            int biggest = -1;   // highest number of wins
            int index = 0;      // index of the player with the most wins

            // Iterates through players
            for(int playas = 0; playas < players.size(); playas++){

                if(players.get(playas).getWins() > biggest){
                    biggest = players.get(playas).getWins();
                    index = playas;
                }
            }

            newList.add(players.get(index));
            players.remove(index);
        }

        players = newList;
        LOG.info("Sorted.");
    }

    public void newPlayer(Player player){
        LOG.info(String.format("Adding %s to the tournament scoreboard", player.getName()));
        players.add(player);
        sortPlayers();
    }

    public void removePlayer(Player player){
        players.remove(player);
    }

}
