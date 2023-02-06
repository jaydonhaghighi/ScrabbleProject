package Models;

import java.io.Serializable;
import java.util.*;

/**
 * This class creates a bag of 100 tiles it has letters ranging from A to Z each letter
 * has different number of occurrences and values.
 *
 * @author Jaydon Haghighi
 * @version 2022.10.25
 */
public class Bag implements Serializable {

    final private LinkedList<Tile> tiles = new LinkedList<>();
    private List<String> numOfLetters = Arrays.asList("A-9", "B-2", "C-2", "D-4", "E-12", "F-2", "G-3", "H-2", "I-9", "J-1",
            "K-1", "L-4", "M-2", "N-6", "O-8", "P-2", "Q-1", "R-6", "S-4", "T-6", "U-4", "V-2", "W-2", "X-1", "Y-2", "Z-1");
    private Random random = new Random();


    /**
     * Constructs Models.Bag containing 100 tiles.
     */
    public Bag() {
        initialize(); //initializes Models.Bag
    }


    /**
     * Initializes all letters with corresponding values and places it into tiles.
     */
    private void initialize() {
        final Map<Integer, List<String>> pointLetterMap = new HashMap<>(); //Map of points for each letter
        pointLetterMap.put(1, Arrays.asList("A", "E", "I", "O", "U", "L", "N", "S", "T", "R"));
        pointLetterMap.put(2, Arrays.asList("D", "G"));
        pointLetterMap.put(3, Arrays.asList("B", "C", "M", "P"));
        pointLetterMap.put(4, Arrays.asList("F", "H", "V", "W", "Y"));
        pointLetterMap.put(5, Arrays.asList("K"));
        pointLetterMap.put(8, Arrays.asList("J", "X"));
        pointLetterMap.put(10, Arrays.asList("Q", "Z"));

        final Map<String, Integer> eachLetterPointMap = new HashMap<>(); //Map of letters with points associated to it

        for (Map.Entry<Integer, List<String>> entry : pointLetterMap.entrySet()) { //loops through letter and associates it to a value
            for (String letter : entry.getValue()) {
                eachLetterPointMap.put(letter, entry.getKey());
            }
        }

        for (String numOfLetter : numOfLetters) { //loops through each letter and duplicates the letter occordingly to scrabble rules
            String[] token = numOfLetter.split("-");
            for (int i = 0; i < Integer.parseInt(token[1]); i++) {
                tiles.add(new Tile(token[0], eachLetterPointMap.get(token[0])));
            }
        }
        for (int i = 0; i < 2; i++) {
            tiles.add(new Tile("_", 0));
        }
    }

    /**
     * Gets bag size.
     * @return amount of tiles within bag
     */
    public int getBagSize() {
        return tiles.size();
    }

    /**
     * Removes tiles from bag.
     * @param amount amount of tiles to remove
     * @return Returns the removed tiles from bag
     */
    public List<Tile> removeTiles(int amount){
        List<Tile> removedTiles = new ArrayList<>();
        if (amount > getBagSize()) {
            //throw new Models.NotEnoughTiles("Not Enough Tiles, Only "+getBagSize()+" Tiles Left."); //Implemented in later milestones
        }
        for (int i = 0; i < amount; i++) {
            removedTiles.add(tiles.remove(random.nextInt(getBagSize())));
        }

        return removedTiles;
    }

    /**
     * Adds tiles given back into bag.
     * @param tiles The subset of Models.Tile objects to be placed into bag.
     */
    public void placeTiles(List<Tile> tiles) {
        for (Tile tile : tiles) {
            this.tiles.add(tile);
        }
    }

    /**
     * Returns Letters value
     * @param letter Letter between A-Z
     * @return returns value of letter given
     */
    public static Integer getLetterValue(String letter) {
        if (letter.equals("A") || letter.equals("E") || letter.equals("I") || letter.equals("O") || letter.equals("U")
                || letter.equals("L") || letter.equals("N") || letter.equals("S") || letter.equals("T") || letter.equals("R")) {
            return 1;
        } else if (letter.equals("D") || letter.equals("G")) {
            return 2;
        } else if (letter.equals("B") || letter.equals("C") || letter.equals("M") || letter.equals("P")) {
            return 3;
        } else if (letter.equals("F") || letter.equals("H") || letter.equals("V") || letter.equals("W") || letter.equals("Y")) {
            return 4;
        } else if (letter.equals("K")) {
            return 5;
        } else if (letter.equals("J") || letter.equals("X")) {
            return 8;
        } else if (letter.equals("Q") || letter.equals("Z")) {
            return 10;
        }
        return 0;
    }

    /**
     * Prints all tiles within bag.
     */
    public void print() {
        for (Tile tile : tiles) {
            System.out.println(tile);
        }

    }
}
