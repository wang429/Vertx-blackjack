package jcw.vertx.blackjack.data;

/**
 * A single typical poker card
 * 
 * @author James Wang
 * @date May 31, 2014
 */
public class Card {
    public enum Value {
        Ace("A"), Two("2"), Three("3"), Four("4"), Five("5"), Six("6"), Seven("7"), Eight("8"), Nine("9"), Ten("10"), Jack(
                "J"), Queen("Q"), King("K");

        public String value;

        Value(String val) {
            value = val;
        }
    }

    public enum Suit {
        Club('C'), Diamond('D'), Heart('H'), Spade('S');

        public char suit;

        Suit(char suit) {
            this.suit = suit;
        }
    }

    Value value;
    Suit suit;

    public Card(Value value, Suit suit) {
        this.value = value;
        this.suit = suit;
    }
}
