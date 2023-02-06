package Models;

import java.io.Serializable;

/**
 * This class creates a tile which contains a letter between A to Z
 * and a value between (1,2,3,4,5,8,10)
 *
 * @author Jaydon Haghighi
 * @version 2022.10.25
 */
public class Tile implements Serializable {
    private String letter;
    private Integer value;

    public Tile(String letter, Integer value) {
        this.letter = letter;
        this.value = value;
    }

    /**
     * @return Returns Letter of Models.Tile
     */
    public String getLetter() {
        return letter;
    }


    /**
     * @return Returns formatted string of Models.Tile
     */
    @Override
    public String toString() {
        return letter+"," + value;
    }

    /**
     * Sets blank tile to specific letter
     * @param letter Letter you want to set
     */
    public void setLetter(String letter) {
        this.letter = letter;
    }
}
