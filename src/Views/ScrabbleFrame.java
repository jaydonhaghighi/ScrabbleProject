package Views; /**
 * The main frame of Scrabble's graphical user interface.
 * The frame contains all panels that reflect the state of the game.
 *
 * @author Mahtab Ameli
 * @date 2022-11-05
 * @version 0.0
 */

import Controllers.SaveLoadUndoRedoController;
import Models.Board;
import Models.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;

public class ScrabbleFrame extends JFrame implements ActionListener {

    /**
     * Dimensions of the frame.
     */
    private final int frameWidth = 1025;
    private final int frameHeight = 1000;
    private Game game = new Game();
    private GameConsolePanel gameConsolePanel;
    /**
     * Constructor for the class.
     */
    public ScrabbleFrame() {
        super("Welcome to Scrabble!");
        this.initializeFrame();
        this.initializePanels(game);
    }


    /**
     * Initializes this frame.
     */
    private void initializeFrame() {
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setSize(frameWidth, frameHeight);
        //this.setLayout(new GridLayout(2,2)); //todo revisit later
        this.setLayout(new BorderLayout()); //todo revisit later
        //this.setVisible(true);
        this.setResizable(false);
        //this.revalidate();
    }


    /**
     * Creates and adds to this frame all component panels of the GUI.
     */
    private void initializePanels(Game game) {

        SaveLoadUndoRedoController slurC = new SaveLoadUndoRedoController(this.game);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout());

        JPanel rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout());
        JPanel southPanel = new JPanel();

        gameConsolePanel = new GameConsolePanel(game);
        BoardPanel boardPanel = new BoardPanel(game);
        HandPanel handPanel = new HandPanel(game);
        GameCommandPanel gameCommandPanel = new GameCommandPanel(game);
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Menu");
        JMenu saveLoad = new JMenu("Save/Load");
        JMenu undoRedo = new JMenu("Undo/Redo");
        JMenuItem help = new JMenuItem("Help");
        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem save = new JMenuItem("Save");
        save.setActionCommand("save");
        save.addActionListener(slurC);
        JMenuItem load = new JMenuItem("Load");
        load.setActionCommand("load");
        load.addActionListener(slurC);
        JMenuItem undo = new JMenuItem("Undo");
        undo.setActionCommand("undo");
        undo.addActionListener(slurC);
        JMenuItem redo = new JMenuItem("Redo");
        redo.setActionCommand("redo");
        redo.addActionListener(slurC);
        help.addActionListener(this);
        exit.addActionListener(this);
        menu.add(help);
        menu.add(exit);
        saveLoad.add(save);
        saveLoad.add(load);
        undoRedo.add(undo);
        undoRedo.add(redo);
        menuBar.add(menu);
        menuBar.add(saveLoad);
        menuBar.add(undoRedo);

        southPanel.add(gameCommandPanel,BorderLayout.CENTER);
        leftPanel.add(boardPanel, BorderLayout.NORTH);
        leftPanel.add(handPanel, BorderLayout.CENTER);
        leftPanel.add(gameCommandPanel, BorderLayout.SOUTH);
        rightPanel.add(gameConsolePanel, BorderLayout.CENTER);

        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.EAST);

        this.add(mainPanel);
        this.setJMenuBar(menuBar);
        this.revalidate();
    }

    /**
     * Creates the players for the game
     *
     * @param playerAmount
     */
    public void createPlayers(String playerAmount, String AIAmount) {
        game.createPlayers(playerAmount, AIAmount);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch(e.getActionCommand()) {
            case "Help":
                gameConsolePanel.appendHelp();
                break;
            case "Exit":
                this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
        }
    }

    /**
     * Updates the board pattern
     *
     * @param boardPattern
     */
    public void updateBoardPattern(Board.Pattern boardPattern) {
        game.updateBoardPattern(boardPattern);
    }
}