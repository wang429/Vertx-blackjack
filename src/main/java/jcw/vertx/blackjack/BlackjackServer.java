package jcw.vertx.blackjack;

import java.io.IOException;
import java.util.Set;

import jcw.vertx.blackjack.data.Deck;
import jcw.vertx.blackjack.data.Hand;
import jcw.vertx.blackjack.data.Players;

import org.vertx.java.core.Handler;
import org.vertx.java.core.buffer.Buffer;
import org.vertx.java.core.http.HttpServerRequest;
import org.vertx.java.core.http.ServerWebSocket;
import org.vertx.java.core.logging.Logger;
import org.vertx.java.platform.Verticle;

/**
 * Verticle for a blackjack game played from the browser. Sign in from
 * http://localhost:8080/signin Server code
 * 
 * TODO implement some way of detecting when players leave the game
 * 
 * @author James Wang
 * @date May 31, 2014
 */
public class BlackjackServer extends Verticle {
    Logger logger;

    private Deck deck;
    private Players players;
    private Hand dealerHand;

    public void start() {
        logger = container.logger();
        players = new Players();

        vertx.createHttpServer().websocketHandler(new Handler<ServerWebSocket>() {
            public void handle(final ServerWebSocket ws) {
                if (ws.path().equals("/blackjack")) {
                    // handle input from the game page
                    ws.dataHandler(new Handler<Buffer>() {
                        public void handle(Buffer data) {
                            String input = data.toString();

                            if (input.startsWith("hit")) { // if hit
                                String playerName = input.replaceFirst("hit", "");
                                players.dealCard(playerName, deck.draw());

                                // if player bust, end round
                                if (players.getPlayer(playerName).isBust()) {
                                    endGame(playerName);
                                }
                            } else if (input.startsWith("stay")) { // if stay
                                String playerName = input.replaceFirst("stay", "");
                                endGame(playerName);
                            }

                            // just forces the page to refresh
                            ws.writeTextFrame("filler");
                        }

                        // ends the game
                        // TODO wait for all players to finish round, then end
                        private void endGame(String playerName) {
                            boolean playerWin = determineIfPlayerWon(playerName);
                            String str = "Dealer had: " + dealerHand.toString() + "\nYou had: "
                                    + players.getPlayer(playerName).toString();
                            if (playerWin) {
                                ws.writeTextFrame("You Win!\n" + str);
                            } else {
                                ws.writeTextFrame("You Lose!\n" + str);
                            }

                            startNewRound();
                        }
                    });
                } else if (ws.path().equals("/signin")) {
                    ws.dataHandler(new Handler<Buffer>() {
                        // when a player signs in from /signin
                        public void handle(Buffer data) {

                            if (players.isEmpty()) {
                                startNewRound();
                            }
                            String player = data.toString();
                            players.addPlayer(player);
                            // do this twice because hands in blackjack start
                            // with 2 cards
                            dealCards(player);
                            dealCards(player);
                            ws.writeTextFrame(player);
                        }
                    });
                } else {
                    ws.reject();
                }
            }
        }).requestHandler(new Handler<HttpServerRequest>() {
            public void handle(HttpServerRequest req) {
                logger.error(req.path());
                if (req.path().startsWith("/signin")) {
                    req.response().sendFile("login.html");
                } else if (req.path().startsWith("/blackjack")) {
                    try {
                        String name = req.path().replaceFirst("/blackjack/", "");
                        String dealerHandStr = dealerHand.toString();
                        // TODO: This can be cleaner...
                        // for example: js for better interactivity and make a
                        // real time web app
                        req.response().sendFile(
                                GenerateHTML.generateGameHTMLFile("blackjack.html",
                                        "XX" + dealerHandStr.substring(dealerHandStr.indexOf(' ')),
                                        players.getPlayer(name).toString(), name));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).listen(8080);
    }

    /**
     * Start a new round of blackjack
     */
    private void startNewRound() {
        deck = new Deck();
        deck.shuffle();

        dealerHand = new Hand();
        // do this twice because hands in blackjack start with 2 cards
        dealerHand.add(deck.draw());
        dealerHand.add(deck.draw());

        players.newRound();
        Set<String> playerNames = players.getPlayerNames();
        for (String playerName : playerNames) {
            // do this twice because hands in blackjack start with 2 cards
            dealCards(playerName);
            dealCards(playerName);
        }
    }

    /**
     * determine if the player won the round
     * 
     * @return true if player wins, false otherwise
     */
    private boolean determineIfPlayerWon(String playerName) {
        boolean playerWin;
        if (players.getPlayer(playerName).isBust()) {
            // dealer wins
            playerWin = false;
        } else {
            while (dealerHand.smallerValue() < 17) {
                dealerHand.add(deck.draw());
            }
            if (dealerHand.isBust()) {
                // player wins
                playerWin = true;
            } else {
                if (players.getPlayer(playerName).value() > dealerHand.value()) {
                    // player wins
                    playerWin = true;
                } else {
                    // dealer wins
                    playerWin = false;
                }
            }
        }

        return playerWin;
    }

    /**
     * Deal a card to param playerName
     * 
     * @param playerName
     *            playerName to deal to
     */
    private void dealCards(String playerName) {
        players.dealCard(playerName, deck.draw());
    }
}
