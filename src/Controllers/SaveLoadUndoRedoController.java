package Controllers;

import Models.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;

public class SaveLoadUndoRedoController implements Serializable, ActionListener {

    Game game;

    /**
     * Public constructor for SaveLoadUndoRedo controller
     *
     * @param game Instance of game
     */
    public SaveLoadUndoRedoController(Game game){
        this.game = game;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        try {
            switch (actionEvent.getActionCommand()) {
                case "save":
                    game.saveGame();
                    JOptionPane.showMessageDialog(null, "Game saved");
                    break;

                case "load":
                    game.loadGame();
                    JOptionPane.showMessageDialog(null, "Game loaded");
                    break;

                case "undo":
                    if (this.game.undoGame() == false) {
                        JOptionPane.showMessageDialog(null, "Nothing to Undo");
                    }
                    break;

                case "redo":
                    if (this.game.redoGame() == false) {
                        JOptionPane.showMessageDialog(null, "Nothing to Redo");
                    }
                    break;
            }

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
