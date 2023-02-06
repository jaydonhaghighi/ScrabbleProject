package Models;

import Models.Hand;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is part of the "Scrabble" application.
 *
 * Models.InHand class is primarily used to check if the word that the player intends on playing can be played based off
 * of the current status of their hand. This class is used to check this in the main Models.Game class. This class is also
 * used to convert the String to a list of characters.
 *
 * @author Mohamed Kaddour
 * @date 2022.10.25
 */

public class InHand implements Serializable {

    private String wordAttempt;
    private Hand hand;

    /**
     * Models.InHand constructor that takes in a wordAttempt and the current player's hand to initialize the class state
     * @param wordAttempt the String of word to be checked
     * @param hand the player's hand to be looked at
     * */
    public InHand(String wordAttempt, Hand hand)
    {
        this.wordAttempt = wordAttempt;
        this.hand = hand;
    }

    /**
     * Checks if the current word that the player intends on playing is actually in the hand and returns true if this
     * is the case.
     * @return return true if word is in hand
     * */
    public boolean wordInHand()
    {
        ArrayList<Tile> mockHand = (ArrayList<Tile>) (this.hand.getHand().clone());

        System.out.println();
        int initialSize = mockHand.size();

        for (Character c : this.wordAttempt.toCharArray())
        {
            for (Tile t : mockHand)
            {
                if (c.equals(t.getLetter().charAt(Hand.PARSE_CHAR_AT_ZERO)))
                {
                    mockHand.remove(t);
                    break;
                }
            }
        }

        return (mockHand.size() == (initialSize - this.wordAttempt.length()));
    }

    /**
     * Converts the String that is the word attempt of user into an ArrayList of characters
     * @return ArrayList<Character> of character list of the String passed in by the user.
     * */
    public ArrayList<Character> wordToList()
    {
        ArrayList<Character> wordAttemptList = new ArrayList<>();
        for (Character c : this.wordAttempt.toCharArray()) { wordAttemptList.add(c); }

        return wordAttemptList;
    }
}