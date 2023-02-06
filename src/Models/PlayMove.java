package Models;

import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * This class is part of the "Scrabble" application.
 *
 * The job of the Models.PlayMove class is primarily parse certain values (like coordinates) out of the
 * passed in Strings related to user inputs. It is also meant to handle exception cases like when
 * a Models.Tile is blank. Once all necessary information is parsed, it is placed on the Models.Board.
 * @author  Mohamed Kaddour
 * @date 2022.10.25
 */
public class PlayMove implements Serializable {

    public final static int PARSE_CHAR_AT_ONE = 1;
    public final static int PARSE_CHAR_AT_TWO = 2;
    public final static int PARSE_CHAR_AT_THREE = 3;
    public final static int ASCII_BASE = 64;
    public final static int DOUBLE_DIGIT_DETECTION = 3;

    private String placementAttempt;
    private Board board;
    private Board.Direction direction;
    private ArrayList<Tile> wordTiles;


    /**
     * Constructor for Models.PlayMove initializes the copy of the board (acting as a copy constructor) and also
     * initializes class state.
     * @param placementAttempt the String including the coordinates where the player wants to place.
     * @param direction boolean for the direction (horizontal, vertical)
     * @param board The board that is going to be copied.
     * @param wordTiles The ArrayList of Tiles to be printed.
     * */
    public PlayMove(String placementAttempt, ArrayList<Tile> wordTiles, Board board, boolean direction)
    {
        //Manual Cloning
        this.board = new Board();
        this.board.setCells(board.getCells());
        this.board.setSquares(board.getSquares());
        this.board.setTiles(board.getTiles());
        this.board.setFirstPlay(board.isFirstPlay());
        this.board.updateBoardPattern(board.getBoardPattern());

        this.placementAttempt = placementAttempt;
        this.direction = direction ? Board.Direction.HORIZONTAL : Board.Direction.VERTICAL;
        this.wordTiles = wordTiles;
    }

    /**
     * Parses the row value from the placementAttempt String based on the side of the String in order to detect double
     * digits.
     * @return int of the Row coordinates.
     * */
    private int parseRow()
    {
        if (this.direction == Board.Direction.HORIZONTAL)
        {
            if (this.placementAttempt.length() == DOUBLE_DIGIT_DETECTION) {
                return Integer.parseInt(placementAttempt.substring(Hand.PARSE_CHAR_AT_ZERO, PlayMove.PARSE_CHAR_AT_TWO));
            }
            else
            {
                return Integer.parseInt(String.valueOf(this.placementAttempt.charAt(Hand.PARSE_CHAR_AT_ZERO)));
            }
        }
        else
        {

            if (this.placementAttempt.length() == DOUBLE_DIGIT_DETECTION) {
                return Integer.parseInt(placementAttempt.substring(PlayMove.PARSE_CHAR_AT_ONE,
                        PARSE_CHAR_AT_THREE));
            }
            else
            {
                return Integer.parseInt(String.valueOf(this.placementAttempt.charAt(PlayMove.PARSE_CHAR_AT_ONE)));
            }
        }
    }

    /**
     * Parses the column value from the placementAttempt String based on the side of the String in order to detect double
     * digits.
     * @return int of the Column coordinates.
     * */
    private int parseColumn()
    {
        if (this.direction == Board.Direction.VERTICAL)
        {
            return (this.placementAttempt.charAt(Hand.PARSE_CHAR_AT_ZERO)) - ASCII_BASE;
        }
        else
        {
            return (this.placementAttempt.charAt(this.placementAttempt.length() == DOUBLE_DIGIT_DETECTION
                    ? PlayMove.PARSE_CHAR_AT_TWO : PlayMove.PARSE_CHAR_AT_ONE)) - ASCII_BASE;
        }
    }

    /**
     * Helper method to simply check if the passed in char is a capital letter.
     * @param c Character to be checked
     * @return boolean, true if the passed in char is a capital letter
     * */
    private boolean isLetter(char c) {
        return (c >= 'A' && c <= 'Z');
    }

    /**
     * Gets the current board score related to the recently placed word and returns it.
     * @return int as the word score
     * */
    public int getPlayedWordScore()
    {
        //return this.board.getWordScore(parseRow(), parseColumn(), this.wordTiles, this.direction);
        return this.board.getWordScore(parseRow(), parseColumn());
    }

    /**
     * Places the word on the board with the row and column where we want to place. Specify the direction as well
     * and passed in the Tiles to be placed. This method also handles blanks.
     * @return true is Tiles were successfully placed.
     * */
    public boolean placeTile()
    {
        if (this.direction != null && this.placementAttempt != null) {
            return this.board.placeWord(parseRow(), parseColumn(), this.wordTiles, this.direction);
        }
        else
        {
            return false;
        }
    }

    /**
     * Returns the updated board in the case where everything is valid.
     * @return the current board in this class
     * */
    public Board getUpdatedBoard()
    {
        return this.board;
    }

    /**
     * Returns a value if the word in the board currently are valid. Done using the board being passed in and
     * checking whether or not the words are valid.
     * @return boolean if all the passed in words are valid.
     * */
    public boolean checkWord() throws FileNotFoundException {
        WordValidator wordValidator = new WordValidator();
        ArrayList<String> allWords = board.getNewWords();
        return wordValidator.isWordsValid(allWords);
    }
}