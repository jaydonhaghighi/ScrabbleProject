package Views; /**
 * Contains all information regarding the current points per player, the players turn and printing messages to the user
 *
 * @Author Akshay Vashisht
 * @Date 2022-11-13
 * @Version 1.0
 */

import Models.Game;
import Models.ScrabbleEvent;

import javax.swing.*;
import java.awt.*;

public class GameConsolePanel extends JPanel implements ScrabbleView {

    private JTextArea console;
    private Game game;
    private final static String NEWLINE = "\n";
    private final static String NEWLINE2 = "\n\n";
    private int textCount;
    private boolean firstUpdate;

    /**
     * Public constructor for class Views.GameConsolePanel
     *
     * @param game
     */
    public GameConsolePanel(Game game) {
        super();

        this.game = game;
        this.game.addScrabbleView(this);

        initializePanel();
        firstUpdate = true;
        this.add(console);
    }

    /**
     * Initializes the panel
     */
    private void initializePanel() {
        console = new JTextArea(31, 20);

        JScrollPane scrollPane = new JScrollPane(console);
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        console.setEditable(false);
        console.setLineWrap(true);
        console.setWrapStyleWord(true);
        console.setSize(new Dimension(400, 500));
        console.setBackground(Color.lightGray);
        console.setText(" Welcome to Scrabble! Scrabble is a word game for 2 to 4 Players." + NEWLINE +
                " The goal of the game is to accumulate more than or equal to 120 points."  + NEWLINE2 +
                " Press help for details. Refer to instruction manual for multiplier legend" + NEWLINE2);

        this.setPreferredSize(new Dimension(400, 500));
        this.setVisible(true);
    }

    /**
     * Appends a help message to the console when prompted
     */
    public void appendHelp()
    {
        console.append(" Scrabble is a word game with 2-4 players. The goal of the game is to accumulate more than or equal to 50 points \n" +
                "Each player starts with 7 tiles in their hand. All players have the 5 different actions \n" +
                "they can perform: Play, Exchange, Pass, Shuffle \n" +
                "To start, drag and drop the tiles you want to play to the board IN THE CORRECT WORD ORDER. Once all the tiles\n" +
                "are placed, you can press play and if the play and word is valid, the next player's turn will happen\n" +
                "You can also shuffle, pass or press exchange and select which tiles you would like to exchange\n\n");

        textCount += 8;
    }

    @Override
    public void update(ScrabbleEvent e) {
        if (textCount > 12) {
            console.setText("");
            textCount = 0;
        }
        else {
            textCount++;
        }

        if (e.getCurrentPlayer() != null) {
            console.append(" Models.Player " + (e.getCurrentPlayer().getPlayerNumber() + 1) +
                    ((e.getCurrentPlayer().isAI()) ? "(AI PLAYER)" : "") + "'s turn..." + "\n Points: " +
                    e.getCurrentPlayer().getPoints() + NEWLINE2);
        }


        if (e.isGameFinished() == true)
        {
            console.append(" GAME IS FINISHED! PLAYER " + (e.getCurrentPlayer().getPlayerNumber()+1) + " WON!" + NEWLINE2 +
            "Please close game and play again");
        }
    }
}
