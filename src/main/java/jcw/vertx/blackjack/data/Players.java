package jcw.vertx.blackjack.data;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * All players in the game of Blackjack
 * 
 * @author James Wang
 * @date May 31, 2014
 */
public class Players {
    private Map<String, Hand> players;

    public Players() {
        players = new HashMap<String, Hand>();
    }

    public boolean addPlayer(String name) {
        if (players.containsKey(players)) {
            return false;
        }
        players.put(name, new Hand());
        return true;
    }

    /**
     * Clears all players' hands for a new round
     */
    public void newRound() {
        Set<String> playerNames = getPlayerNames();
        for (String playerName : playerNames) {
            players.put(playerName, new Hand());
        }
    }

    public void removePlayer(String name) {
        // TODO have the Verticle call this when a player leaves
        players.remove(name);
    }

    public Hand getPlayer(String name) {
        return players.get(name);
    }

    public void dealCard(String name, Card card) {
        players.get(name).add(card);
    }

    public Set<String> getPlayerNames() {
        return players.keySet();
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }
}
