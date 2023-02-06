package Models;

import java.io.*;
import java.util.ArrayList;

/**
 * This class is part of the "Scrabble" application.
 *
 * Holds information of command that has been issued by the user. Holds them separately and handles if certain
 * commands are null. Also parses the direction of the placement attempt. Models.Command is used in Models.Parser where
 * the user input is individually passed into this class through the constructor to initialize the command.
 *
 * @author  Mohamed Kaddour
 * @date 2022.12.09
 */

public class GameState implements Serializable{

    private Game game;
    private ArrayList<GameState> undoStateHistory;
    private ArrayList<GameState> redoStateHistory;

    public final static String FILENAME_UNDO = "src/SaveStates/undo.txt";
    public final static String FILENAME_REDO = "src/SaveStates/redo.txt";
    public final static String FILENAME_SAVE = "src/SaveStates/save.txt";

    /**
     * Public constructor for class game.
     */
    public GameState(Game game, boolean undo) {
        this.game = game;
        this.undoStateHistory = new ArrayList<>();
        this.redoStateHistory = new ArrayList<>();

        if (undo) {
            updateCurrentGameHistoryUndo();
        }
        else
        {
            updateCurrentGameHistoryRedo();
        }
    }

    /**
     * Updates the Undo state history by reading the undo file
     */
    private void updateCurrentGameHistoryUndo(){
        File newFile = new File(FILENAME_UNDO);
        FileInputStream inputStream;
        ObjectInputStream ois;
        if (newFile.length() != 0) {
            try {
                inputStream = new FileInputStream(FILENAME_UNDO);
                ois = new ObjectInputStream(inputStream);
                try {
                    this.undoStateHistory = (ArrayList<GameState>) ois.readObject();
                } catch (ClassNotFoundException c) {
                    System.out.print("c");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        this.undoStateHistory.add(this);
        saveCurrentGameStateUndo();
    }

    /**
     * Saves the current game state to the undo file
     */
    private void saveCurrentGameStateUndo() {
        FileOutputStream outputStream = null;
        try {
            PrintWriter writer = new PrintWriter(FILENAME_UNDO);
            writer.print("");
        } catch (FileNotFoundException f)
        {
            System.out.print("f");
        }
        ObjectOutputStream oos = null;
        try {
            outputStream = new FileOutputStream(FILENAME_UNDO);
            oos = new ObjectOutputStream(outputStream);
            oos.writeObject(this.undoStateHistory);
        }
        catch (IOException e)
        {
            System.out.print("e2");
        }
    }

    /**
     * Updates the Redo state history by reading the redo file
     */
    private void updateCurrentGameHistoryRedo()
    {
        File newFile = new File(FILENAME_REDO);
        FileInputStream inputStream;
        ObjectInputStream ois;
        if (newFile.length() != 0) {
            try {
                inputStream = new FileInputStream(FILENAME_REDO);
                ois = new ObjectInputStream(inputStream);
                try {
                    this.redoStateHistory = (ArrayList<GameState>) ois.readObject();
                } catch (ClassNotFoundException c) {
                    System.out.print("c");
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }

        this.redoStateHistory.add(this);
        saveCurrentGameStateRedo();
    }

    /**
     * Saves the current game state to the redo file
     */
    private void saveCurrentGameStateRedo() {
        FileOutputStream outputStream;
        try {
            PrintWriter writer = new PrintWriter(FILENAME_REDO);
            writer.print("");
        } catch (FileNotFoundException f)
        {
            System.out.print("f");
        }
        ObjectOutputStream oos;
        try {
            outputStream = new FileOutputStream(FILENAME_REDO);
            oos = new ObjectOutputStream(outputStream);
            oos.writeObject(this.redoStateHistory);
        }
        catch (IOException e)
        {
            System.out.print("e2");
        }
    }

    /**
     * Getter for game
     */
    public Game getGame()
    {
        return this.game;
    }

}
