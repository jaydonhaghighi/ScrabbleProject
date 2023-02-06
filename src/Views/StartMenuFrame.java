package Views; /**
 * The frame for the PlayerSelectorPanel class.
 *
 * @Author Jaydon Haghighi
 * @Date 2022-11-13
 * @Version 3.0
 */

import Models.Board;

import javax.swing.*;
import java.io.FileNotFoundException;

public class StartMenuFrame extends JFrame {
    int frameWidth = 400;
    int frameHeight = 400;
    private ScrabbleFrame scrabbleFrame = new ScrabbleFrame();

    /**
     * Public constructor for Views.StartMenuFrame class
     */
    public StartMenuFrame() { //TODO: Catch Exception
        super("Welcome to Scrabble!");
        this.initializeFrame();
        this.initializePanel();
        this.setVisible(true);
        this.setResizable(false);
    }

    /**
     * Initializes the frame
     */
    private void initializeFrame() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(frameWidth, frameHeight);
    }

    /**
     * Initializes the panel
     */
    private void initializePanel() {
        this.add(new SelectionPanel(this));
    }

    /**
     * Creates the players for the game
     *
     * @param playerAmount
     */
    public void createPlayers(String playerAmount, String AIAmount) {
        scrabbleFrame.createPlayers(playerAmount, AIAmount);
        this.setVisible(false);
        scrabbleFrame.setVisible(true);
    }


    /**
     * Main method
     *
     * @param args
     * @throws FileNotFoundException
     */
    public static void main(String[] args) throws FileNotFoundException {
        StartMenuFrame startMenuFrame = new StartMenuFrame();
    }

    public void updateBoardPattern(Board.Pattern boardPattern) {
        scrabbleFrame.updateBoardPattern(boardPattern);
    }
}
