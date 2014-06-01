package jcw.vertx.blackjack.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jcw.vertx.blackjack.data.Card.Suit;
import jcw.vertx.blackjack.data.Card.Value;

/**
 * Implementation of a deck of 52 poker cards
 * 
 * @author James Wang
 * @date May 31, 2014
 */
public class Deck {
    private List<Card> deck;

    public Deck() {
        deck = new ArrayList<Card>(52);

        for (Value value : Value.values()) {
            for (Suit suit : Suit.values()) {
                deck.add(new Card(value, suit));
            }
        }
    }

    public void shuffle() {
        Collections.shuffle(deck);
    }

    public Card draw() {
        if (deck.size() > 0) {
            return deck.remove(0);
        } else {
            // rebuild deck, then remove
            new Deck();

            return null;
        }
    }
}
