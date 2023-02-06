package Models;

import Models.Bag;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.Serializable;
import java.util.*;

/**
 * A board is a 16*16 table of cells, representing the board of the game Scrabble. Each cell of the board contains
 * a square with its corresponding color and multiplier value, as well as the tile placed on it, which has a letter and score.
 * The cells themselves store the value of the tile letter placed on them. Printing the board displays the state of the game.
 *
 * @author Mahtab Ameli
 * @date 2022-11-22
 * @version 2.0
 */
public class Board implements Serializable {

    /**
     * 16*16 array of String stores letters of tiles placed.
     */
    private String[][] cells;

    /**
     * Maps Tiles to corresponding String coordinates (row as digit, and column as letter).
     */
    private HashMap<String, Tile> tiles;

    /**
     * Maps Squares to their corresponding String coordinates (row as digit, and column as letter).
     */
    private HashMap<String, Square> squares;

    /**
     * True if the current move is the first play of the game.
     */
    private boolean isFirstPlay;


    /**
     * True if the current move is the first play of the game.
     */
    private boolean firstPlayValid;

    /**
     * The direction of the last word played.
     */
    private Direction direction;

    private String playedWord = "";

    /**
     * current active word's starting coordinates.
     */
    int currentStartRow = 0; int currentStartCol = 0;

    /**
     * Direction of word placement on the board
     */
    public enum Direction{HORIZONTAL, VERTICAL}

    /**
     * Type of premium square pattern for custom boards.
     */
    public enum Pattern{STANDARD, DIAMOND, TETRIS}

    /**
     * List of cell coordinates that have already been scored once.
     */
    ArrayList<String> scoredOnceList;

    /**
     * list of all new words added to board as a result of last play move.
     */
    ArrayList<String> newWords;


    /**
     * The custom pattern of the board, chosen by the user in beggining of game.
     */
    private Pattern boardPattern;


    /**
     * Constructor for the class.
     * Creates a board and initializes its fields.
     */
    public Board() {
        this.cells = new String[16][16];     // first row and col are for grid labels. the other 15*15 are for placing Squares and Tiles
        this.tiles = new HashMap<>();
        this.squares = new HashMap<>();
        this.isFirstPlay = true;
        this.firstPlayValid = false;
        this.direction = Direction.HORIZONTAL;
        this.scoredOnceList = new ArrayList<String>();
        this.newWords = new ArrayList<String>();
        this.boardPattern = Pattern.STANDARD;
        initializeBoard();                   // assign a Models.Square and a Models.Tile to each cell

    }

    /**
     * Returns array of cells of the board.
     *
     * @return the array of cells.
     */
    public String[][] getCells() {
        return cells;
    }

    /**
     * Setter for the cells array.
     *
     * @param cells the array of all cells on the board.
     */
    public void setCells(String[][] cells) {
        for (int i = 0; i < 15; i++)
        {
            for (int j = 0; j < 15; j++)
            {
                this.cells[i][j] = cells[i][j];
            }
        }
    }

    /**
     * Returns list of all tiles on the board.
     *
     * @return the tiles HashMap.
     */
    public HashMap<String, Tile> getTiles() {
        return tiles;
    }

    /**
     * Setter for the tiles' hashmap.
     *
     * @param tiles the mapping between coordinates and tiles of the board.
     */
    public void setTiles(HashMap<String, Tile> tiles) {
        this.tiles.clear();
        this.tiles.putAll(tiles);
    }

    /**
     * Getter for the squares' hashmap.
     *
     * @return the squares HashMap.
     */
    public HashMap<String, Square> getSquares() {
        return squares;
    }

    /**
     * Returns true if the current move is the first play move of the game.
     *
     * @returns true if the move is the first play.
     */
    public boolean isFirstPlay() {
        return isFirstPlay;
    }

    /**
     * Setter for the first game flag.
     *
     * @param firstPlay the boolean value for whether current move is the first play move of the game.
     */
    public void setFirstPlay(boolean firstPlay) {
        isFirstPlay = firstPlay;
    }

    /**
     * Setter for the squares' hashmap.
     *
     * @param squares the mapping between coordinates and squares of the board.
     */
    public void setSquares(HashMap<String, Square> squares) {
        this.squares.clear();
        this.squares.putAll(squares);
    }

    /**
     * Places a Models.Square on each cell and then place a Models.Tile on the Models.Square. Update the value of the cell to tile's letter.
     */
    private void initializeBoard() {
        Tile emptyTile = new Tile(" ", 0);
        Square thisSquare;
        for (int row = 0; row < cells.length; row++) {
            for (int col = 0; col < cells[row].length; col++) {
                // Each placement has a corresponding Models.Tile and a Models.Square

                thisSquare = new Square(row,col);
                String coordinates = thisSquare.getStringCoordinates();
                tiles.put(coordinates, emptyTile);   // put an empty tile on thisSquare
                squares.put(coordinates, thisSquare);   // put thisSquare on the placement corresponding to coordinates (this placement)
                cells[row][col] = (tiles.get(coordinates).getLetter());     // store the letter of the tile in this placement
            }
        }
    }

    /**
     * Updates the board's custom pattern based on user's choice in the beginning of the game.
     * @param boardPattern
     */
    public void updateBoardPattern(Pattern boardPattern) {
        this.boardPattern = boardPattern;
    }

    /**
     * Creates and returns board pattern's sml file based on user's selection.
     * @return xml file for board pattern.
     */
    private File createCustomPatternFile() {
        if (this.boardPattern.equals(Pattern.DIAMOND)) {
            return new File("src/CustomBoardXML/board_custom_diamond.xml");
        }
        else if (this.boardPattern.equals(Pattern.TETRIS)) {
            return new File("src/CustomBoardXML/board_custom_tetris.xml");
        } else {
            return new File("src/CustomBoardXML/board_standard.xml"); // board pattern is standard by default
        }

    }

    /**
     * Returns the multiplier type for a given square from the xml file from user's chosen custom board.
     * @param square
     * @return
     */
    public Square.Multiplier getMultiplierFromXML(Square square) {
        Square.Multiplier multi = Square.Multiplier.NONE;
        try
        {
            File file = createCustomPatternFile();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("multiplier");

            String coordinates = square.getStringCoordinates();
            String coordsSearchString = "-" + coordinates + "-";
            // iterating through nodeList of multiplier elements in xml file
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item(itr);
                Element elem = (Element) node;
                String coordinates_element = elem.getElementsByTagName("coordinates").item(0).getTextContent();

                if (coordinates_element.contains(coordsSearchString)) {
                    String multiplier_type_element = elem.getElementsByTagName("type").item(0).getTextContent();
                    if(multiplier_type_element.equals("DL")) {
                        square.setMultiplier(Square.Multiplier.DL);
                        multi = Square.Multiplier.DL;
                    }
                    else if(multiplier_type_element.equals("DW")) {
                        square.setMultiplier(Square.Multiplier.DW);
                        multi = Square.Multiplier.DW;
                    }
                    else if(multiplier_type_element.equals("TL")) {
                        square.setMultiplier(Square.Multiplier.TL);
                        multi = Square.Multiplier.TL;
                    }
                    else if(multiplier_type_element.equals("TW")) {
                        square.setMultiplier(Square.Multiplier.TW);
                        multi = Square.Multiplier.TW;
                    }
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return multi;
    }

    /**
     * Returns true if a square has the empty tile (square has not been played yet).
     * @param row the integer value of the row of given cell.
     * @param col the integer value of the column of given cell.
     * @return true if the cell has not been played yet.
     */
    private boolean cellIsBlank(int row, int col){
        // a cell is empty if it stores a single space (" ")
        if ((row < 1) || (col < 1) || (row > 16) || (col > 16)) {
            return false;
        }
        if(cells[row][col].equals(" ")) {
            return true;
        }
        return false;
    }

    public Pattern getBoardPattern() {
        return boardPattern;
    }

    /**
     * Returns coordinates on board, where AI player can place tiles.
     * @return
     */
    public HashMap<String, Boolean> getAIPlayableCoordinates(){
        HashMap<String, Boolean> playableCoordinates = new HashMap<>();

        for (int ROW = 1; ROW < cells.length - 1; ROW++) {
            for (int COL = 1; COL < cells.length - 1; COL++) {
                if(!cells[ROW][COL].equals(" ")) {
                    if(cells[ROW+1][COL].equals(" "))
                    {
                        playableCoordinates.put(getStringCoords(ROW + 1, COL), false);
                    }
                    if(cells[ROW][COL+1].equals(" "))
                    {
                        playableCoordinates.put(getStringCoords(ROW, COL+1), true);
                    }
                }
            }
        }
        return playableCoordinates;
    }

    /**
     * Places a single tile on the cell with given coordinates. Prints error messages if placement is invalid.
     * @param row the integer value of the row of given cell.
     * @param col the integer value of the column of given cell.
     * @param tile
     * @param direction
     * @return true if given tile was placed successfully.
     */
    public Boolean placeTileAt(int row, int col, Tile tile, Direction direction) {
        // row out of bounds error
        if (row < 1  || row > 15) {
            System.out.println("Cannot place tile " + tile.getLetter() + ", on invalid row: " + row
                    + ", and valid column: " + col);
            System.out.println("Valid row range is 1 to 15 inclusive.");
            return false;
        }
        // column out of bounds error
        if (col < 1  || col > 15) {

            System.out.println("ERROR: Cannot place tile " + tile.getLetter() + " on row: " + row
                    + ", and invalid column: " + col);
            System.out.println("Valid column range is 1 to 15 inclusive.");
            return false;
        }
        // successful placement
        if (cellIsBlank(row, col)) {
            //String coordinates = "" + row + Models.Square.columns.get(col);
            String coordinates = getStringCoords(row, col);
            tiles.put(coordinates,tile);
            cells[row][col] = tile.getLetter();
            return true;
        }
        // play it on next available cell based on direction
        else {
            //everytime a cell is not blank, it means it's already been scored
            this.scoredOnceList.add(getStringCoords(row,col));

            if (direction.equals(Direction.VERTICAL)) {
                if (row >= 16) {return false;}
                if (getBottomCellContent(row,col).equals(" ")) {
                    for(int ROW = row + 1; ROW < 16; ROW++) {
                        if (cellIsBlank(ROW, col)) {
                            tiles.put(getStringCoords(ROW,col),tile);
                            cells[ROW][col] = tile.getLetter();
                            return true;
                        } else {
                            //everytime a cell is not blank, it means it's already been scored
                            this.scoredOnceList.add(getStringCoords(ROW,col));
                        }
                    }
                }

            }
            else if (direction.equals(Direction.HORIZONTAL)) {
                if (col >= 16) {return false;}
                if (getRightCellContent(row,col).equals(" ")) {
                    for (int COL = col + 1; COL < 16; COL++) {

                        if (cellIsBlank(row, COL)) {
                            tiles.put(getStringCoords(row,COL),tile);
                            cells[row][COL] = tile.getLetter();
                            return true;
                        } else {
                            //everytime a cell is not blank, it means it's already been scored
                            this.scoredOnceList.add(getStringCoords(row,COL));
                        }
                    }
                }
            }
            return false;
        }

    }

    /**
     * Returns true if at least one cell adjacent to given cell is not blank (hence the word can be placed according to Scrabble rules).
     * @param row the integer value of the row of given cell.
     * @param col the integer value of the column of given cell.
     * @return true if none of the adjacent cells have been played before.
     */
    public Boolean allAdjacentsBlank(int row, int col) {
        Boolean allBlank = false;
        int blankCount = 0;
        ArrayList<String> adjacentCells = getAllAdjacentCells(row,col);
        for (String cell: adjacentCells) {
            if(cell.equals(" ")) {
                blankCount++;
            }
        }
        if (blankCount < adjacentCells.size()) {
            return false;
        }
        return true;
    }

    /**
     * Returns list of all cells adjacent to given cell, handling all cell types (corner, border, regular)
     * @param row the integer value of the row of given cell.
     * @param col the integer value of the column of given cell.
     * @return list of adjacent cells.
     */
    public ArrayList<String> getAllAdjacentCells(int row, int col) {
        ArrayList<String> adjacentCells = new ArrayList<>();
        //handle corner cells
        ArrayList<String> cornerCellCoords = new ArrayList<String>(Arrays.asList(new String[]{"1A", "1O", "15A", "15O"}));
        String thisCell = getStringCoords(row,col);
        //if this cell is a corner cell
        if (cornerCellCoords.contains(thisCell)) {
            adjacentCells = getCornersAdjacentCells(row, col);
        }
        // else if this cell is not a  corner cell, but it's on the b order row/columns of the board
        else if ((row == 1) || (row == 15) || (col == 1) || (col == 15)) {
            adjacentCells = getBordersAdjacentCells(row, col); }
        // else if the cell is a regular cell
        else if ((row > 1) && (row < 15) && (col > 1) && (col < 15)) {
            adjacentCells = getAdjacentCells(row,col);}
        else {
            System.out.println("ERROR: Invalid cell");
        }
        return adjacentCells;
    }

    /**
     * Return's square's coordinates as a string of the row as a digit, and the column as a letter.
     *
     * @param row the integer value of the row of given cell.
     * @param col the integer value of the column of given cell.
     * @return coordinates as a string with digit row and letter column.
     */
    public String getStringCoords(int row, int col) {
        String stringCoordinates = "" + row + Square.columns.get(col);
        return stringCoordinates;
    }

    /**
     * Returns the String content of the cell located above given cell
     *
     * @param row the integer value of the row of given cell.
     * @param col the integer value of the column of given cell.
     * @return string content of cell above.
     */
    private String getTopCellContent(int row, int col) {return cells[row-1][col];}

    /**
     * Returns the String content of the cell located to the right of given cell.
     *
     * @param row the integer value of the row of given cell.
     * @param col the integer value of the column of given cell.
     * @return string content of cell on the right.
     */
    private String getRightCellContent(int row, int col) {return cells[row][col+1];}

    /**
     * Returns the String content of the cell located underneath the given cell.
     *
     * @param row the integer value of the row of given cell.
     * @param col the integer value of the column of given cell.
     * @return string content of cell underneath.
     */
    private String getBottomCellContent(int row, int col) {return cells[row+1][col];}

    /**
     * Returns the String content of the cell located to the left of given cell.
     *
     * @param row the integer value of the row of given cell.
     * @param col the integer value of the column of given cell.
     * @return string content of cell on the left.
     */
    private String getLeftCellContent(int row, int col) {return cells[row][col-1];}

    /**
     * Returns a list of contents of all cells adjacent to given cell.
     * Handles ell type: regular (not in coreners or on the borders)
     *
     * @param row the integer value of the row of given cell.
     * @param col the integer value of the column of given cell.
     * @return list of contents of adjacent cells.
     */
    public ArrayList<String> getAdjacentCells(int row, int col) {
        ArrayList<String> adjacentCells = new ArrayList<>();
        adjacentCells.add(getTopCellContent(row, col));
        adjacentCells.add(getRightCellContent(row, col));
        adjacentCells.add(getBottomCellContent(row, col));
        adjacentCells.add(getLeftCellContent(row, col));
        return adjacentCells;
    }

    /**
     * Returns a list of contents of all cells adjacent to given cell.
     * Handles ell type: border (cells on the outer borders of the board)
     *
     * @param row the integer value of the row of given cell.
     * @param col the integer value of the column of given cell.
     * @return list of contents of adjacent cells.
     */
    public ArrayList<String> getBordersAdjacentCells(int row, int col) {
        ArrayList<String> adjacentCells = new ArrayList<>();
        // cell is on top row (row == 1) but not a corner cell
        if ((row == 1) && (col > 1) && (col < 15)) {
            adjacentCells.add(getRightCellContent(row, col));
            adjacentCells.add(getBottomCellContent(row, col));
            adjacentCells.add(getLeftCellContent(row, col));
        }
        // cell is on rightmost column (col == 15) but not a corner cell
        else if ((col == 15) && (row > 1) && (row < 15)) {
            adjacentCells.add(getTopCellContent(row, col));
            adjacentCells.add(getBottomCellContent(row, col));
            adjacentCells.add(getLeftCellContent(row, col));
        }
        // cell is on bottom row (row == 15) but not a corner cell
        else if ((row == 15) && (col > 1) && (col < 15)) {
            adjacentCells.add(getTopCellContent(row, col));
            adjacentCells.add(getRightCellContent(row, col));
            adjacentCells.add(getLeftCellContent(row, col));
        }
        // cell is on leftmost column (col == 1) but not a corner cell
        else if ((col == 1) && (row > 1) && (row < 15)) {
            adjacentCells.add(getTopCellContent(row, col));
            adjacentCells.add(getBottomCellContent(row, col));
            adjacentCells.add(getLeftCellContent(row, col));
        }
        return adjacentCells;
    }

    /**
     * Returns a list of contents of all cells adjacent to given cell.
     * Handles ell type: corner (cells on the 4 corners of the board).
     *
     * @param row the integer value of the row of given cell.
     * @param col the integer value of the column of given cell.
     * @return list of contents of adjacent cells.
     */
    public ArrayList<String> getCornersAdjacentCells(int row, int col) {
        ArrayList<String> adjacentCells = new ArrayList<>();
        ArrayList<String> cornerCellCoords = new ArrayList<String>(Arrays.asList(new String[]{"1A", "1O", "15A", "15O"}));
        String thisCell = getStringCoords(row,col);
        //if this cell is a corner cell
        if (cornerCellCoords.contains(thisCell)) {
            switch (thisCell) {
                case "1A":
                    adjacentCells.add(getRightCellContent(row,col));
                    adjacentCells.add(getBottomCellContent(row, col));
                    break;
                case "1O":
                    adjacentCells.add(getLeftCellContent(row,col));
                    adjacentCells.add(getBottomCellContent(row, col));
                    break;
                case "15A":
                    adjacentCells.add(getRightCellContent(row,col));
                    adjacentCells.add(getTopCellContent(row, col));
                    break;
                case "15O":
                    adjacentCells.add(getLeftCellContent(row,col));
                    adjacentCells.add(getTopCellContent(row, col));
                    break;
                default:
                    System.out.println("Error: Not a corner cell!");
            }
        }
        return adjacentCells;
    }

    /**
     * Returns true if this condition would be met after word placement:
     * At least one tile is be adjacent to an existing tile.
     *
     * @param row the integer value of the row of given cell.
     * @param col the integer value of the column of given cell.
     * @return true if at least one adjacent cell is not blank.
     */
    public Boolean adjacentConditionMet(int row, int col, ArrayList<Tile> tiles, Direction direction) {
        ArrayList<Tile> wordTiles = tiles;
        int ROW = row;
        int COL = col;
        int count = 0;

        for (Tile tile: wordTiles) {
            // check condition for tile
            if (!allAdjacentsBlank(ROW,COL)) {count++;}
            if (direction == Direction.VERTICAL) {ROW++;}
            else {COL++;}
        }
        // if at least one tile would be next to a non-blank tile, the condition is met
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * Places as many single letters on board as are valid. Returns true if placement is valid.
     * Returns false if placement was unsuccessful for that cell.
     *
     * @param row the integer value of the row of given cell.
     * @param col the integer value of the column of given cell.
     * @param tiles list of tiles of the word to be placed.
     * @param direction the direction of the word to be placed.
     * @return true if placement of given word would be valid if it were to be placed.
     */
    public Boolean placeWord(int row, int col, ArrayList<Tile> tiles, Direction direction) {

        int ROW = row;
        int COL = col;
        Boolean tilePlaced = false;
        int tilePlacedCount = 0;

        Boolean adjacentConditionMet = adjacentConditionMet(row, col, tiles, direction);
        Boolean tilesCoverCentre = tilesCoverCentre(row, col, tiles, direction);

        if ((tiles.size() <= 1) && (tilesCoverCentre)) {
            System.out.println("ERROR: Words must be at least two letters long.");
            firstPlayValid = false;
        }
        else if ((tiles.size() > 1) && (tilesCoverCentre)) {
            firstPlayValid = true;
        }
        // iterate through word tiles
        for (Tile tile: tiles) {
            tilePlaced = this.placeTileAt(ROW, COL, tile, direction);
            //if (tilePlaced) {tilePlacedCount++; }
            if (!tilePlaced) {
                break;
            }
            tilePlacedCount++;
            if (direction == Direction.VERTICAL) {  // placement direction is vertical
                ROW++;
            } else if (direction.equals(Direction.HORIZONTAL)) {    // placement direction is horizontal
                COL++;
            }
        }

        if (isFirstPlay && firstPlayValid) {
            System.out.println("First word's placement is valid.");
            getWordScore(row, col);
            return true;
        }

        else if ((adjacentConditionMet) && tilePlacedCount == tiles.size()){
            System.out.println("Placement is valid.");
            getWordScore(row, col);
            return true;
        }
        return false;
    }

    /**
     * Concatenates and returns String combination of letters from given tiles.
     *
     * @param tiles
     * @return string concatenation of letters on given tiles.
     */
    public String getWordFromTiles(ArrayList<Tile> tiles) {
        String word = "";
        ArrayList<Tile> tilesList = tiles;
        for (Tile t: tilesList) {
            word += t.getLetter();
        }
        return word;
    }

    /**
     * Returns the String letter placed on given coordinates.
     */
    public String getLetterAt(int row, int col) {
        return cells[row][col];
    }




    /**
     * Returns list of all String words on given row of board.
     *
     * @param row the integer value of the row of given cell.
     * @return list of all words on given row of board.
     */
    public ArrayList<String> getWordsOnRow(int row) {
        ArrayList<String> wordsOnRow = new ArrayList<>();
        String currentWord = "";
        String currentLetter;
        //int ROW = row;
        //iterate through columns of ROW == row
        for (int COL = 1; COL < cells.length - 1; COL++) {
            if (!cellIsBlank(row, COL)) {   // if
                currentLetter = cells[row][COL];
                currentWord += currentLetter;
                // if next cell in row is blank, store currentWord in horizontalWords list and move on to next word
                if (cellIsBlank(row, COL + 1)) {
                    //todo maybe remove?
                    if ((currentWord.length() > 1)) {
                        wordsOnRow.add(currentWord);
                        System.out.println("current word on row is: " + currentWord);
                    }
                    currentWord = ""; // set currentWord to empty string so it can store the next word in row
                }
                // handle cells on the right border of the board (col == 15)
                else {
                    if ((COL == cells.length - 2)) {
                        currentWord += cells[row][COL + 1];
                        if (currentWord.length() > 1) {
                            wordsOnRow.add(currentWord);
                            System.out.println("current word on row is: " + currentWord);
                            currentWord = ""; // set currentWord to empty string so it can store the next word in row
                        }
                    }

                }
            }
        }
        System.out.println("number of words on row: " + wordsOnRow.size());
        return wordsOnRow;
    }


    /**
     * Returns list of all String words on given column of board.
     *
     * @param col the integer value of the column of given cell.
     * @return list of all words on given column of board.
     */
    public ArrayList<String> getWordsOnCol(int col) {
        ArrayList<String> wordsOnCol = new ArrayList<>();
        String currentWord = "";
        String currentLetter;
        //int COL = col;
        //iterate through rows of COL == col
        for (int ROW = 1; ROW < cells.length - 1; ROW++) {
            if (!cellIsBlank(ROW, col)) {   // if
                currentLetter = cells[ROW][col];
                currentWord += currentLetter;
                // if next cell in row is blank, store currentWord in horizontalWords list and move on to next word
                if (cellIsBlank(ROW + 1, col)) {
                    //todo maybe remove?
                    if ((currentWord.length() > 1)) {
                        wordsOnCol.add(currentWord);
                        System.out.println("current word on col is: " + currentWord);
                    }
                    currentWord = ""; // set currentWord to empty string so it can store the next word in row
                }
                // handle cells on the bottom border of the board (row == 15)
                else {
                    if (ROW == cells.length - 2) {
                        currentWord += cells[ROW + 1][col];
                        if (currentWord.length() > 1) {
                            wordsOnCol.add(currentWord);
                            System.out.println("current word on col is: " + currentWord);
                            currentWord = ""; // set currentWord to empty string so it can store the next word in row
                        }
                    }
                }
            }
        }
        System.out.println("number of words on column: " + wordsOnCol.size());
        return wordsOnCol;
    }

    /**
     * Returns the score for given word.
     *
     * @param row the integer value of the row of given cell.
     * @param col the integer value of the column of given cell.
     * @return sum score of values of tiles.
     */
    public int getWordScore(int row, int col) {
        String wordToScore = getLongestWordFormed(row, col);
        int wordScore = calculateWordScore(wordToScore);
        int premiumScore = calculatePremiumScore(wordToScore);
        System.out.println("WORD SCORE for " + wordToScore + ": " + wordScore);
        System.out.println("PREMIUM SCORE for " + wordToScore + ": " + premiumScore);
        return premiumScore;
    }


    /**
     * Given a single square coordinate, finds all words containing that cell.
     * Returns the longest one to be scored.
     * @param row the integer value of the row of given cell.
     * @param col the integer value of the column of given cell.
     * @return
     */
    private String getLongestWordFormed(int row, int col) {
        ArrayList<String> wordsOnRow = getWordsOnRow(row); // get all horizontal words attached to this cell
        ArrayList<String> wordsOnCol = getWordsOnCol(col); // get all vertical words attached to this cell

        this.newWords.addAll(wordsOnRow);
        this.newWords.addAll(wordsOnCol);

        String longestHorizontal = "";
        String longestVertical = "";
        String longestWord = "";

        // find the longest horizontal word formed
        if (!wordsOnRow.isEmpty()) {
            for (int i = 0; i < wordsOnRow.size(); i++) {
                String currWord_Horiz = wordsOnRow.get(i);
                if (currWord_Horiz.length() > longestHorizontal.length()) {
                    longestHorizontal = currWord_Horiz;
                }
            }
        }

        // find the longest vertical word formed that covers (row,col)
        if (!wordsOnCol.isEmpty()) {
            for (int i = 0; i < wordsOnCol.size(); i++) {
                String currWord_V = wordsOnCol.get(i);
                if (currWord_V.length() > longestVertical.length()) {
                    longestVertical = currWord_V;
                }
            }
        }
        //choose longer word between vertical longest and horizontal longest
        // if there's a tie, pick horizontal by convention
        // update starting coordinates of current word
        if (longestHorizontal.length() >= longestVertical.length()) {
            longestWord = longestHorizontal;
            this.direction = Direction.HORIZONTAL;
            //updateStartingCoords(row, col, longestWord, Direction.HORIZONTAL); // update starting coordinates of current word

        } else {
            longestWord = longestVertical;
            this.direction = Direction.VERTICAL;
            //updateStartingCoords(row, col, longestWord, Direction.VERTICAL); // update starting coordinates of current word
        }
        updateStartingCoords(row, col, longestWord); // update starting coordinates of current word
        System.out.println("LONGEST WORD FORMED BY TILE: " + longestWord
                + "\nstarting at: row " + this.currentStartRow
                + " col " + this.currentStartCol);
        return longestWord;
    }


    /**
     * Updates current word's starting coordinates.
     * @param row the integer value of the row of given word.
     * @param col the integer value of the column of given word.
     */
    private void updateStartingCoords(int row, int col, String word) {
        //reset current word's starting coordinates to 0 before starting
        currentStartRow = 0;
        currentStartCol = 0;
        int COL = col;
        int ROW = row;

        if (word.length() <= 1) {
            System.out.println("ERROR: the word played is only one letter long.");
            return;
        }
        // if direction is horizontal keep checking left cell until next left is blank
        if (direction == Direction.HORIZONTAL) {
            for (COL = col; COL > 1; COL--) {

                // if left cell is blank or if current cell is on leftmost edge of the board, then current horizontal word starts at current cell
                if (getLeftCellContent(ROW, COL).equals(" ")) {
                    currentStartRow = ROW;
                    currentStartCol = COL;
                    return;
                }
                else if (COL == 2) {
                    currentStartRow = ROW;
                    currentStartCol = 1;
                    return;
                }
                // else, keep going left until find starting cell of word
            }
        }

        // if direction is vertical, keep going up in the column until find first cell of the word
        else if (direction == Direction.VERTICAL) {
            for (ROW = row; ROW > 1; ROW--) {
                if (getTopCellContent(ROW,COL).equals(" ")) {
                    currentStartRow = ROW;
                    currentStartCol = COL;
                    return;
                }
                else if (ROW == 2) {
                    currentStartRow = 1;
                    currentStartCol = COL;
                    return;
                }
            }

        }

        if (currentStartCol == 0 || currentStartRow == 0) {
            System.out.println("ERROR: NO WORDS HAVE BEEN FORMED THAT CONTAIN ROW = " + row + ", COLUMN = " + col);
        }
        return;
    }


    /**
     * Calculates score of the given word.
     *
     * @param wordToScore the String word to be scored.
     * @return int score of word.
     */
    public static int calculateWordScore(String wordToScore) {
        int letterScore = 0;
        int wordScore = 0;
        boolean lengthValid = false;

        if (wordToScore.length() > 1) {
            lengthValid = true;
        }

        if (lengthValid) {
            for (int i = 0; i < wordToScore.length(); i++) {
                letterScore = Bag.getLetterValue("" + wordToScore.charAt(i));
                wordScore += letterScore;
                //System.out.println("current character of word " + wordToScore + ": " + wordToScore.charAt(i));
            }
        } else {
            wordScore = 0;
            System.out.println("ERROR: word must be at least two letters long.");
            System.out.println("ERROR: " + wordToScore);
        }
        return wordScore;
    }

    /**
     * Returns the score for given word multiplied by corresponding premium scores' multipliers.
     *
     * @param word  the String word to score.
     * @return the multiplied score of the word.
     */
    public int calculatePremiumScore(String word) {
        int startingRow = this.currentStartRow;
        int startingCol = this.currentStartCol;
        int wordLength = word.length();
        ArrayList<Square.Multiplier> multipliers = new ArrayList<>();
        int wordScore = 0;
        int premiumScore = 0;
        Bag b = new Bag();
        boolean scoredBefore = false;

        //iterate through squares of the word and check for multiplier type
        if (direction == Direction.HORIZONTAL) {
            for (int col = startingCol; col < cells.length ; col++) {
                if (multipliers.size() >= wordLength) {break;}
                if (scoredOnceList.contains(getStringCoords(startingRow,col))) {scoredBefore = true;}
                Square.Multiplier multiplier = getMultiplierFromXML(squares.get(getStringCoords(startingRow,col)));
                if (!scoredBefore) {
                    multipliers.add(multiplier);
                }

                int letterScore = b.getLetterValue(cells[startingRow][col]);
                // only apply multipliers if the letter hasn't been scored previously
                if (!scoredBefore) {
                    if (multiplier == Square.Multiplier.DL || multiplier == Square.Multiplier.TL || multiplier == Square.Multiplier.NONE) {
                        letterScore = letterScore * multiplier.getValue();
                    }
                }
                if (multiplier == Square.Multiplier.DL || multiplier == Square.Multiplier.TL || multiplier == Square.Multiplier.NONE) {
                    letterScore = letterScore * multiplier.getValue();
                }
                wordScore += letterScore;
                System.out.println("square (" + startingRow +"," + col  +") multiplier: "+ getMultiplierFromXML(squares.get(getStringCoords(startingRow,col))));
            }
            if (multipliers.contains(Square.Multiplier.DW)) { premiumScore = wordScore * 2;}
            else if (multipliers.contains(Square.Multiplier.TW)) {premiumScore = wordScore * 3;}
            else {premiumScore = wordScore;}
        }

        //iterate through squares of the word and check for multiplier type
        if (direction == Direction.VERTICAL) {
            for (int row = startingRow; row < cells.length; row++) {
                if (multipliers.size() >= wordLength) {break;}
                if (scoredOnceList.contains(getStringCoords(row,startingCol))) {scoredBefore = true;}
                Square.Multiplier multiplier = getMultiplierFromXML(squares.get(getStringCoords(row,startingCol)));
                if (!scoredBefore) {
                    multipliers.add(multiplier);
                }

                int letterScore = b.getLetterValue(cells[row][startingCol]);

                if (!scoredBefore) {
                    if ((multiplier == Square.Multiplier.DL) || (multiplier == Square.Multiplier.TL) || (multiplier == Square.Multiplier.NONE)) {
                        letterScore = letterScore * multiplier.getValue();
                    }
                }
                if ((multiplier == Square.Multiplier.DL) || (multiplier == Square.Multiplier.TL) || (multiplier == Square.Multiplier.NONE)) {
                    letterScore = letterScore * multiplier.getValue();
                }
                wordScore += letterScore;
                System.out.println("square (" + row +"," + startingCol  +") multiplier: " + getMultiplierFromXML(squares.get(getStringCoords(row,startingCol))));
            }
            if (multipliers.contains(Square.Multiplier.DW)) { premiumScore = wordScore * 2;}
            else if (multipliers.contains(Square.Multiplier.TW)) {premiumScore = wordScore * 3;}
            else {premiumScore = wordScore;}
        }

        return premiumScore;
    }

    /**
     * Returns the String letter stored in cell at given coordinates.
     * @param row the integer value of the row of given cell.
     * @param col the integer value of the column of given cell.
     * @return the letter stored in cell at given row and col.
     */
    public String getLetterAtSquare(int row, int col)
    {
        return this.cells[row][col];
    }

    /**
     * Returns the list of all new words formed on board as a result of the last play move.
     * @return list of new words on board.
     */
    public ArrayList<String> getNewWords() {
        return this.newWords;
    }

    /**
     * Returns true if there are no tiles on the board;
     * @return
     */
    public boolean boardIsEmpty() {
        for (int row = 1; row < cells.length; row++) {
            for (int col = 1; col < cells.length; col++) {
                if (!cellIsBlank(row,col)) {return false;}
            }
        }
        System.out.println("BOARD IS EMPTY.");
        return true;
    }

    /**
     * Returns true if placement of first word on board would be valid,
     * given starting coordinates, tiles, and placement direction.
     * @param row
     * @param col
     * @param tiles
     * @param direction
     * @return
     */
    private boolean tilesCoverCentre(int row, int col, ArrayList<Tile> tiles, Direction direction) {
        if ((direction == Direction.VERTICAL) && (col == 8)) {
            for (int ROW = row; ROW < row + tiles.size(); ROW++) {
                if (ROW == 8) {
                    System.out.println("First word correctly covers the centre.");
                    return true;
                }
            }
        }
        if ((direction == Direction.HORIZONTAL) && (row == 8)) {
            for (int COL = col; COL < col + tiles.size(); COL++) {
                if (COL == 8) {
                    System.out.println("First word correctly covers the centre.");
                    return true;
                }
            }
        }
        System.out.println("ERROR: First word must cover the centre square of the board.");
        return false;
    }

    /**
     * Returns true if first play move is valid.
     * @return
     */
    public boolean isFirstPlayValid() {
        return firstPlayValid;
    }

}
