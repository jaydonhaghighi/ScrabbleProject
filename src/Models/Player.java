package Models;

import Models.Hand;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is part of the "Scrabble" application.
 *
 * Models.Player class manages the Models.Player as well as allows a way for the Models.Game to interact with the hand. Contains storage
 * for the player's number, status and current points. Allows for the ability to control the Models.Hand through an exchange
 * method.
 *
 * @author Mohamed Kaddour
 * @date 2022.10.25
 */

public class Player implements Serializable {

    public static final int MIN_POINTS = 0;

    private Hand hand;
    private int points;
    private int playerNumber;
    private boolean active;
    private boolean isAI;

    /**
     * Constructor to initialize player points to 0 when player is created. Constructor to initialize
     * hand.
     * @param playerNumber the number assigned to the player
     * */
    public Player (int playerNumber)
    {
        this.points = MIN_POINTS;
        hand = new Hand();
        this.playerNumber = playerNumber;
        this.active = true;
        this.isAI = false;
    }

    /**
     * getter method for whether the player is an AI or not
     * @return boolean
     * */
    public boolean isAI() {
        return isAI;
    }

    /**
     * Setter method for whether the player is an AI or not
     * */
    public void setAI(boolean AI) {
        isAI = AI;
    }

    /**
     * Emulates the exchanging of Tiles by removing the Models.Tile objects from hand and replacing with new TIle objects
     * passed into the method.
     * @param removeTiles The subset of Models.Tile objects to be removed from Models.Hand (based off the characters)
     * @param addTiles The set of Models.Tile objects to be added to the Models.Hand
     * @return boolean Return true is successfully exchanged Tiles, else return false.
     * */
    public ArrayList<Tile> exchange(ArrayList<Tile> addTiles, ArrayList<Character> removeTiles)
    {
        if ((this.hand.removeTiles(removeTiles, true)) == false)
        {
            System.out.println("Couldn't remove tiles from hand");
        }
        else
        {
            this.hand.addTiles(addTiles, true);

            if ((this.hand.getHandSize() == Hand.MAX_HAND_SIZE) == false)
            {
                System.out.print("Unexpected Models.Hand size: " + this.hand.getHandSize());
            }
        }

        return this.hand.getRecentlyRemoved();
    }

    /**
     * If the play is deemed illegal, roll back by adding the recently removed Tiles to the hand and
     * removing recently added.
     * */
    public void rollBack()
    {
        ArrayList<Character> charArray = new ArrayList<>();

        this.hand.addTiles(this.hand.getRecentlyRemoved(), false);

        for (Tile t : this.hand.getRecentlyAdded())
        {
            charArray.add(t.getLetter().charAt(Hand.PARSE_CHAR_AT_ZERO));
        }

        this.hand.removeTiles(charArray, false);
    }

    /**
     * Initializes the Models.Player's hand with 7 tiles
     * @param t ArrayList of Tiles to initialize Models.Player's hand with.
     * @return boolean returns true if the size of the ArrayList is 7
     * */
    public boolean initializePlayerHand(ArrayList<Tile> t)
    {
        boolean rc = true;

        if ((rc = (t.size() == Hand.MAX_HAND_SIZE)) == false) {
            System.out.println("Need to Initialize with 7 tiles");
        }
        else
        {
            this.hand.addTiles(t, true);
        }

        return rc;
    }

    /**
     * Displays the Models.Player's hand
     * */
    public void displayHand()
    {
        this.hand.displayHand();
    }

    /**
     * Rearranges object tiles in Models.Hand.
     * @return void
     * */
    public void shuffle()
    {
        this.hand.shuffleHand();
    }

    /**
     * Getter to get current state of the points of the user.
     * @return int current points of Models.Player
     * */
    public int getPoints()
    {
        return this.points;
    }

    /**
     * Added select amount of points to users total point pool.
     * @param addedPoints the amount of points to add to total Models.Player points
     * */
    public void addPoints(int addedPoints)
    {
        this.points += addedPoints;
    }

    /**
     * Getter to get current state of the Models.Player's hand
     * @return the current Models.Hand of player
     * */
    public Hand getHand ()
    {
        return this.hand;
    }

    /**
     * Getter to get the number of the player
     * @return the number of the player
     * */
    public int getPlayerNumber() {
        return playerNumber;
    }

    /**
     * Getter for the current player's active state
     * @return boolean returns true if active.
     * */
    public boolean isActive() {
        return active;
    }

    /**
     * Setter for the current player's active state
     * @return set the state of the player to active or inactive.
     * */
    public void setActive(boolean active) {
        this.active = active;
    }
}
