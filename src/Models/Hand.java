package Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * This class is part of the "Scrabble" application.
 *
 * Models.Hand class is used to simulate the Models.Player's hand as it contains an ArrayList containing Tiles. It is also responsible
 * for interacting with this ArrayList by adding and removing Hands respectively. Also performs passive actions on
 * the Models.Hand like shuffling. Models.Hand also keeps track of recently removed and added Tiles
 *
 * @author Mohamed Kaddour
 * @date 2022.10.25
 */
public class Hand implements Serializable {

    public final static int MAX_HAND_SIZE = 7;
    public final static int PARSE_CHAR_AT_ZERO = 0;
    public final static String YELLOW_BOLD_TEXT_COLOR = "\033[1;93m";
    public final static String COLOR_RESET = "\u001B[0m";

    private ArrayList<Tile> hand;
    private ArrayList<Tile> recentlyRemoved;
    private ArrayList<Tile> recentlyAdded;

    /**
     * Public constructor for class Models.Hand. Use to initialize a hand of size MAX_HAND_SIZE (7) and also the
     * recently removed and added ArrayLists*
     * */
    public Hand()
    {
        this.hand = new ArrayList<>(MAX_HAND_SIZE);
        this.recentlyRemoved = new ArrayList<>();
        this.recentlyAdded = new ArrayList<>();
    }

    /**
     * Rearranges elements in hand ArrayList using static Collections.shuffle method.
     * @return void
     * */
    public void shuffleHand()
    {
        Collections.shuffle(this.hand);
    }

    /**
     * Iterates through ArrayList hand and displays the tile value and the letter value of each Models.Tile.
     * @return void
     * */
    public void displayHand()
    {
        for (Tile t : this.hand)
        {
            System.out.print(YELLOW_BOLD_TEXT_COLOR + t + " ");
        }
        System.out.println(COLOR_RESET);
        System.out.println();
    }

    /**
     * Removes a subset of Models.Tile objects from the Models.Hand Arraylist.
     * @param removeTiles ArrayList of Character element to remove the corresponding Tiles
     * @param clear Indicates it's safe to clear recently removed Tiles
     * @return boolean Return true if successfully removed Models.Tile objects, else return false.
     * */
    public boolean removeTiles(ArrayList<Character> removeTiles, boolean clear)
    {
        boolean rc = true;
        int sizeCounter = removeTiles.size() + 1;

        if (clear == true) {
            this.recentlyRemoved.clear();
        }

        ArrayList<Tile> mock = new ArrayList<>();
        mock.addAll(this.hand);

        if ((rc = (!removeTiles.isEmpty())) == false)
        {
            System.out.println("FAILED, PASSED IN ARRAY IS EMPTY");
            rc = false;
        }
        else
        {
            clearLoop:
            for (int i = 0; i < removeTiles.size(); i++) {
                mockLoop:
                for (Tile l : mock) {
                    if (removeTiles.get(i).equals(l.getLetter().charAt(PARSE_CHAR_AT_ZERO)))
                    {
                        this.hand.remove(l);
                        mock.remove(l);
                        sizeCounter--;
                        if (clear == true) {
                            this.recentlyRemoved.add(l);
                        }
                        if (sizeCounter == 0)
                        {
                            break clearLoop;
                        }
                        break mockLoop;
                    }
                }
            }
        }

        return rc;
    }

    /**
     * Returns a set of the recently removed Tiles in order to be added back to the bag if play not valid.
     * @return the set of recently removed Tiles as a Models.Tile ArrayList
     * */
    public ArrayList<Tile> getRecentlyRemoved()
    {
        return this.recentlyRemoved;
    }

    /**
     * Returns a set of the recently added Tiles in order to be removed from hand if play not valid.
     * @return the set of recently removed Tiles as a Models.Tile ArrayList
     * */
    public ArrayList<Tile> getRecentlyAdded()
    {
        return this.recentlyAdded;
    }

    /**
     * Adds a set of Models.Tile objects to the Models.Hand Arraylist.
     * @param addTiles ArrayList of Models.Tile elements to added to hand
     * @param clear Indicates it's safe to clear recently added Tiles
     * */
    public void addTiles(ArrayList<Tile> addTiles, boolean clear)
    {
        if (clear == true) {
            this.recentlyAdded.clear();
            this.recentlyAdded.addAll(addTiles);
        }

        this.hand.addAll(addTiles);
    }

    /**
     * Adds a set of Models.Tile objects to the Models.Hand Arraylist.
     * @return Integer indicating the size of hand ArrayList.
     * */
    public int getHandSize()
    {
        return this.hand.size();
    }

    /**
     * Getter that returns the ArrayList of Models.Tile objects for hand.
     * @return ArrayList<Models.Tile> hand.
     * */
    public ArrayList<Tile> getHand() {
        return this.hand;
    }
}