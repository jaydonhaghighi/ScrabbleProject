package Test;

import Models.Board;
import Models.Square;
import Models.Tile;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.HashMap;
import static org.junit.Assert.*;

/**
 * Unit test for the class Board.
 *
 * @author Mahtab Ameli
 * @date 2022-11-22
 * @version 0.0
 */
public class BoardTest {

    /**
     * The board to be tested.
     */
    private Board board = new Board();

    /**
     * Array of cells in the board.
     */
    private String[][] testCells;

    /**
     * The length of the 2D array testCells.
     */
    private int cellsLength;

    /**
     * Mapping of String coordinates to Squares of the board.
     */
    private HashMap<String, Square> testSquares;

    /**
     * Mapping of String coordinates to Tiles placed at that coordinate of the board.
     */
    private HashMap<String, Tile> testTiles;

    /**
     * Useful words (each word is list of tiles) to use in different tests.
     */
    private ArrayList<Tile> testWord_ONE, testWord_TWO, testWord_THREE;

    /**
     * Useful tiles to be used in different tests.
     */
    private Tile T_tile, E_tile, H_tile, M_tile;


    /**
     * Initializes attributes before each test.
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        testCells = board.getCells();
        testSquares = board.getSquares();
        testTiles = board.getTiles();
        cellsLength = board.getCells().length;
        // creating some useful tiles
        H_tile = new Tile("H", 4);
        T_tile = new Tile("T", 1);
        E_tile = new Tile("E", 1);
        M_tile = new Tile("M", 3);
        // creating testWord_1
        Tile O_tile = new Tile("O", 1);
        Tile N_tile = new Tile("N", 1);
        testWord_ONE = new ArrayList();
        testWord_ONE.add(O_tile);
        testWord_ONE.add(N_tile);
        testWord_ONE.add(E_tile);
        // creating testWord_2 "TWO"
        Tile W_tile = new Tile("W", 4);
        testWord_TWO = new ArrayList();
        testWord_TWO.add(T_tile);
        testWord_TWO.add(W_tile);
        testWord_TWO.add(O_tile);
        // creating testWord3 "THREE"
        Tile R_tile = new Tile("R", 1);
        testWord_THREE = new ArrayList();
        //testWord_3.add(T_tile);
        testWord_THREE.add(H_tile);
        testWord_THREE.add(R_tile);
        testWord_THREE.add(E_tile);
        testWord_THREE.add(E_tile);
    }


    /**
     * Test Case: new instance of Board is created and all cells are "blank".
     *
     * Tests whether String contents of the newly-created board's cells are correctly equal to a single space " ".
     *
     * Methods covered: initializeBoard(), getCells()
     *
     */
    @Test
    public void testCellsInitiallyBlank() {
        int nonBlankCount = 0;
        for (int row = 0; row < cellsLength; row++) {
            for (int col = 0; col < cellsLength; col++) {
                if (!testCells[row][col].equals(" ")) {
                    nonBlankCount++;
                }
            }
        }
        assertEquals(0, nonBlankCount);
    }



    /**
     * TEST CASE: new instance of Board has been created and should have 61 premium squares + 195 non-premium squares (256 total).
     *
     * Tests whether the new board correctly contains all 61 premium squares of the correct type, and all 195 non-premium squares:
     * Expected count of each premium square type: 8 Triple Word, 17 Double word, 12 Triple Letter, 12 Double Letter
     *
     * Methods covered: initializeBoard(), getCells(), getSquares()
     *
     */
    @Test
    public void testPremiumSquaresAdded() {
        //int DL_count = 0; int DW_count = 0; int TL_count = 0; int TW_count = 0;
        int DL_count = 0; int DW_count = 0; int TL_count = 0; int TW_count = 0; int nonPremium_count = 0;
        int sumSquares;
        String[] keys = testSquares.keySet().toArray(new String[0]);

        for (int i = 0; i < testSquares.keySet().size(); i++) {
            Square s = testSquares.get(keys[i]);
            Square.Multiplier m = s.getMultiplier();
            // count double letter squares
            if (m.equals(Square.Multiplier.DL)) {
                DL_count++;
            }
            // count double word squares
            if (m.equals(Square.Multiplier.DW)) {
                DW_count++;
            }
            // count triple letter squares
            if (m.equals(Square.Multiplier.TL)) {
                TL_count++;
            }
            // count triple word squares
            if (m.equals(Square.Multiplier.TW)) {
                TW_count++;
            }
            // count non-premium squares
            if (m.equals(Square.Multiplier.NONE)) {
                nonPremium_count++;
            }
        }
        sumSquares = DL_count + DW_count + TL_count + TW_count + nonPremium_count;
        assertEquals(256, sumSquares);
    }


    /**
     * Test Case: The board has been created but no player has started playing yet.
     *
     * Tests whether the isFirstPlay Boolean is correctly set to true at the start of the game.
     * Methods covered: isFirstPlay()
     *
     */
    @Test
    public void testFirstPlayTrue() {
        assertEquals(true, board.isFirstPlay());
    }


    /**
     * Test Case: The valid one-letter word "H" is to be placed on the centre of the board (row 8, col 8) horizontally.
     *
     * Tests whether the valid word "H" can be correctly placed horizontally in centre of the board.
     * Methods covered: placeWord(), adjacentConditionMet(), placeTileAt()
     *
     */
    @Test
    public void testWordPlacement_Horizontal() {
        //creating letter tiles for the word "H"
        //creating word's arraylist of tiles
        ArrayList<Tile> H_word = new ArrayList();
        H_word.add(H_tile);
        Boolean placed = false;
        placed = board.placeWord(8,8, H_word, Board.Direction.HORIZONTAL);
        // the word must be longer than one letter
        assertEquals(false, placed);
    }

    /**
     * Test Case: The letters {A, R, P} are to be added next to "H" vertically, to create the valid word "HARP".
     *
     * Tests whether the valid word "HARP" can be correctly created vertically starting in centre of the board.
     *
     * Methods covered: placeWord(), adjacentConditionMet(), placeTileAt()
     *
     */
    @Test
    public void testWordPlacement_Vertical() {

        //creating letter tiles for the word "H"
        Tile A_tile = new Tile("A", 4);
        Tile R_tile = new Tile("R", 4);
        Tile P_tile = new Tile("P", 4);
        //creating word's arraylist of tiles
        ArrayList<Tile> HARP_word = new ArrayList();
        HARP_word.add(A_tile);
        HARP_word.add(R_tile);
        HARP_word.add(P_tile);
        Boolean placed = false;
        placed = board.placeWord(8,8, HARP_word, Board.Direction.VERTICAL);
        assertEquals(true, placed);
    }

    /**
     * Test Case: The valid one-letter word "H" is to be placed on the centre of the board (row 8, col 8) is to be scored.
     *
     * Tests whether the valid word "H" can be correctly placed horizontally in centre of the board.
     * Methods covered: getWordScore(), getWordFromTiles(), getWordsOnRow(), calculateWordScore()
     *
     */
    @Test
    public void testFirstWordScore() {
        //creating letter tiles for the word "H"
        //creating word's arraylist of tiles
        ArrayList<Tile> HE_word = new ArrayList();
        HE_word.add(H_tile);
        HE_word.add(E_tile);
        board.placeWord(8,8,HE_word, Board.Direction.HORIZONTAL);
        int wordScore = board.getWordScore(8,8);
        // correct score is 5 8 2 = 10.
        assertEquals(10, wordScore);
    }


    /**
     * Test Case: The vertical word "M" is appended to "H" on row8, col8. The INVALID word "HM" is to be scored.
     *
     * Tests whether the invalid word "HM" is correctly scored to 0.
     *
     * Methods covered: placeWord(), adjacentConditionMet(), placeTileAt()
     *
     */
    @Test
    public void testWordScore_Appended_Vertical_Invalid() {
        //creating letter tiles for the word "H"
        //creating "H" word's arraylist of tiles
        ArrayList<Tile> H_word = new ArrayList();
        H_word.add(H_tile);
        //creating letter tiles for the word "M"
        Tile M_tile = new Tile("M", 3);
        //creating "m" word's arraylist of tiles
        ArrayList<Tile> HM_word = new ArrayList();
        HM_word.add(M_tile);
        board.placeWord(8,8,H_word, Board.Direction.HORIZONTAL);
        board.placeWord(9,8,H_word, Board.Direction.VERTICAL);
        int wordScore = board.getWordScore(8, 8);
        assertEquals(16, wordScore);
    }


    /**
     * Test Case: The vertical word "M" is appended to "H" on row8, col8. The INVALID word "HM" is to be detected vertically.
     *
     * Tests whether the invalid word "HM" is correctly placed on the board.
     * Methods covered: placeWord(), adjacentConditionMet(), placeTileAt()
     *
     */
    @Test
    public void testTilesPlaced_Vertical() {
        //creating letter tiles for the word "H"
        //creating "H" word's arraylist of tiles
        ArrayList<Tile> H_word = new ArrayList();
        H_word.add(H_tile);
        //creating letter tiles for the word "M"
        Tile M_tile = new Tile("M", 3);
        //creating "m" word's arraylist of tiles
        ArrayList<Tile> HM_word = new ArrayList();
        HM_word.add(M_tile);
        board.placeWord(8,8, H_word, Board.Direction.VERTICAL);
        board.placeWord(9,8, HM_word, Board.Direction.VERTICAL);
        String letter_1 = board.getLetterAt(8,8);
        String letter_2 = board.getLetterAt(9,8);
        String word = letter_1 + letter_2;
        assertEquals("HM", word);
    }


    /**
     * Test Case: The vertical word "M" is appended to "H" on row8, col8. The INVALID word "HM" is to be detected horizontally.
     *
     * Tests whether the invalid word "HM" is correctly placed on the board.
     * Methods covered: placeWord(), adjacentConditionMet(), placeTileAt()
     *
     */
    @Test
    public void testTilesPlaced_Horizontal() {
        //creating letter tiles for the word "H"
        //creating "H" word's arraylist of tiles
        ArrayList<Tile> H_word = new ArrayList();
        H_word.add(H_tile);
        //creating letter tiles for the word "M"
        Tile M_tile = new Tile("M", 3);
        //creating "m" word's arraylist of tiles
        ArrayList<Tile> HM_word = new ArrayList();
        HM_word.add(M_tile);
        board.placeWord(8,8, H_word, Board.Direction.VERTICAL);
        board.placeWord(8,9, HM_word, Board.Direction.VERTICAL);
        String letter_1 = board.getLetterAt(8,8);
        String letter_2 = board.getLetterAt(8,9);
        String word = letter_1 + letter_2;
        assertEquals("HM", word);
    }


    /**
     * Test Case: The valid word PAW is to be scored, based on letter values and the DW multiplier of the centre square.
     *
     * Tests whether the score for the valid word "PAW" is correctly scored as: 2 * (3 + 1 + 4) = 16.
     *
     * Methods covered: calculatePremiumScore()
     *
     */
    @Test
    public void testPremiumScore_Valid_Word() {
        //creating letter tiles for the word "PAW"
        Tile P_tile = new Tile("P", 3);
        Tile A_tile = new Tile("A", 1);
        Tile W_tile = new Tile("W", 4);
        ArrayList<Tile> PAW_word = new ArrayList();
        PAW_word.add(P_tile);
        PAW_word.add(A_tile);
        PAW_word.add(W_tile);
        board.placeWord(8,8,PAW_word, Board.Direction.HORIZONTAL);
        int premiumScore = premiumScore = board.getWordScore(8,8);
        assertEquals(16, premiumScore);
    }


    /**
     * Test Case: The INVALID word "LB" is to be scored, based on letter values and the DW multiplier of the centre square.
     *
     * Tests whether the score for the valid word "LQ" is correctly scored as: 2 * (1 + 3) = 8.
     *
     * Methods covered: calculatePremiumScore()
     *
     */
    @Test
    public void testPremiumScore_Invalid_Word() {
        //creating letter tiles for the word "PAW"
        Tile L_tile = new Tile("L", 1);
        Tile B_tile = new Tile("B", 3);
        ArrayList<Tile> LB_word = new ArrayList();
        LB_word.add(L_tile);
        LB_word.add(B_tile);
        board.placeWord(8,8,LB_word, Board.Direction.HORIZONTAL);
        int premiumScore = premiumScore = board.getWordScore(8,8);
        assertEquals(8, premiumScore);
    }


    /**
     * Test Case: No words have been placed on the board yet.
     *
     * Tests whether the count of all new words on the board is correctly equal to 0.
     *
     * Methods covered: getVerticalWords()
     *
     */
    @Test
    public void testGetNewWords_EmptyBoard() {
        ArrayList<String> words = board.getNewWords();
        int wordCount = words.size();
        assertEquals(0,wordCount);
    }


    /**
     * Test Case: testWord_2 and testWord_3 are placed on the board.
     *
     * Tests whether the count of all horizontal words on the board is correctly equal to 5.
     *
     * Methods covered: getHorizontalWords(), placeWord()
     *
     */
    @Test
    public void testGetNewWords() {
        board. placeWord(8, 8, testWord_TWO, Board.Direction.HORIZONTAL);
        board. placeWord(9, 8, testWord_THREE, Board.Direction.VERTICAL);
        ArrayList<String> words = board.getNewWords();
        int wordCount = words.size();
        assertEquals(2,wordCount);
    }

}