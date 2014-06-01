package jcw.vertx.blackjack.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the cards currently in a player's hand in Blackjack
 * 
 * @author James Wang
 * @date May 31, 2014
 */
public class Hand {
    private List<Card> hand;
    private boolean hasFirstAce;
    private int[] values;

    public Hand() {
        hand = new ArrayList<Card>();
        hasFirstAce = false;
        values = new int[2];
    }

    public boolean add(Card c) {
        addValue(c);
        return hand.add(c);
    }

    public boolean isBust() {
        return smallerValue() > 21;
    }

    public int value() {
        return values[1] <= 21 ? values[1] : values[0];
    }

    public int smallerValue() {
        return values[0];
    }

    public int largerValue() {
        return values[1];
    }

    private void addValue(int val) {
        values[0] += val;
        values[1] += val;
    }

    private void addValue(Card c) {
        switch (c.value) {
        case Ace:
            values[0] += 1;
            if (hasFirstAce) {
                values[1] += 11;
                hasFirstAce = false;
            } else {
                values[1] += 1;
            }
        case Two:
            addValue(2);
            break;
        case Three:
            addValue(3);
            break;
        case Four:
            addValue(4);
            break;
        case Five:
            addValue(5);
            break;
        case Six:
            addValue(6);
            break;
        case Seven:
            addValue(7);
            break;
        case Eight:
            addValue(8);
            break;
        case Nine:
            addValue(9);
            break;
        case Ten:
        case Jack:
        case Queen:
        case King:
            addValue(10);
            break;
        default:
            // Shouldn't ever reach here..
            break;

        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Card c : hand) {
            sb.append(c.suit.suit);
            sb.append(c.value.value);
            sb.append(' ');
        }

        return sb.toString();
    }
}
