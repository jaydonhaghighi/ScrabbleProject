package Controllers; /**
 * This class is part of the "Scrabble" application.
 *
 * One of two controllers for the model. This controller is meant to control the state of both the board and
 * the player's hand in regards to the model. It deteects when a user drops a tile onto the board and then performs
 * actions accordingly.
 *
 * Uses the board coordinates as well to determine whether the play is considered to be a vertical play based off
 * of the drop or a horizontal play based off of the drop.
 *
 * Also has the handling for when a blank tile is dropped onto the board.
 *
 * @author Mohamed Kaddour
 * @date 2022.11.13
 * @Version 1.0
 */

import Models.Game;
import Models.Tile;
import Models.TransferableTile;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.util.Locale;

public class BoardDropTargetController extends DropTargetAdapter {

    private final DropTarget dropTarget;
    private final JLabel label;
    private final String boardCoordinates;
    private Game game;

    /**
     * Constructor that takes in the different aspects of the Views.BoardPanel view in order to properly update both
     * the board visually and also the model Models.Board.
     * @param label of type JLabel the label to update the board with.
     * @param boardCoordinates of type String indicating the board coordinates where the tile was dropped
     * @param game of type Models.Game, the model.
     * */
    public BoardDropTargetController(JLabel label, String boardCoordinates, Game game ) {
        this.label = label;
        this.boardCoordinates = boardCoordinates;
        this.game = game;

        dropTarget = new DropTarget(label, DnDConstants.ACTION_COPY,
                this, true, null);
    }

    /**
     * Helper method to determine the size of the coordinates in order to perform an effective exchange.
     * @return char
     * @param boardCoordinates the boardCoordinates to process.
     * */
    private char determineCharacterSwap(String boardCoordinates)
    {
        return boardCoordinates.charAt((boardCoordinates.length() == 3) ? 2 : 1);
    }

    /**
     * The drop listener which process the drop command from the mouse and then performs various actions on the
     * model with it. This includes checking the direction that the player intends to play with.
     *
     * This method
     * @param dropTargetDropEvent
     * */
    @Override
    public void drop(DropTargetDropEvent dropTargetDropEvent) {
        try {
            Transferable tr = dropTargetDropEvent.getTransferable();
            Tile tile = (Tile) tr.getTransferData(TransferableTile.tileFlavor);

            boolean tileIsBlank = false;

            if (dropTargetDropEvent.isDataFlavorSupported(TransferableTile.tileFlavor)) {

                Component c = dropTargetDropEvent.getDropTargetContext().getDropTarget().getComponent();

                if (c instanceof JLabel) {
                    if (!((JLabel)c).getText().equals(" ")) {
                        dropTargetDropEvent.rejectDrop();
                        return;
                    }
                }

                dropTargetDropEvent.acceptDrop(DnDConstants.ACTION_COPY);
                if (this.label.getText().equals(" ")) {
                    this.label.setText(tile.getLetter());
                    this.label.setForeground(Color.black);
                }


                if (this.game.getFirstPlayInTurn() == true)
                {
                    this.game.setStartingCoordinates(this.boardCoordinates);
                    
                }

                if ((this.game.getFirstPlayInTurn() == false) &&
                        (determineCharacterSwap(this.game.getStartingCoordinates())
                                == determineCharacterSwap(this.boardCoordinates)))
                {
                    this.game.changeStartingCoordinatesToVertical();
                }

                if (tile.getLetter().equals("_"))
                {
                    tile.setLetter(handleBlankTiles());
                    this.label.setText(tile.getLetter());
                    tileIsBlank = true;
                }

                this.game.addToRemoveTilesFromHand(tile.getLetter().charAt(0), tileIsBlank);

                dropTargetDropEvent.dropComplete(true);
                this.game.refreshHandPanelView(tile, tileIsBlank);
                return;
            }

            dropTargetDropEvent.rejectDrop();
        } catch (Exception e) {
            e.printStackTrace();
            dropTargetDropEvent.rejectDrop();
        }
    }

    /**
     * Helper method to handle blank tiles using a JOptionPane to get the user input and then setting
     * the tile letter.
     *
     * @return String with the inputted letter.
     * */
    private String handleBlankTiles()
    {
        JPanel panel = new JPanel(new GridLayout(2, 1));
        JLabel label = new JLabel("Enter a Letter Please");
        JTextField answer = new JTextField();
        panel.add(label);
        panel.add(answer);

        String input = " ";
        boolean flag = false;
        while (!flag) {
            int result = JOptionPane.showOptionDialog(null, panel, "Enter A Letter Please",
                    JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null ,
                    null, null );
            if (result == JOptionPane.OK_OPTION)
            {
                input = answer.getText().toUpperCase();
            }
            flag = (checkBlankTileInput(input.toUpperCase(Locale.ROOT))) ? true : false;
        }

        return input;
    }
    /**
     * Helper method to check whether the blank tile input is valid or not.
     * @param input String to check
     * @return boolean
     * */
    private boolean checkBlankTileInput(String input)
    {
        return (input.length() == 1) && (checkLowerCaseAndUpperCase(input.charAt(0)));
    }

    /**
     * Checks whether the letter is a valid lower case or upper case letter.
     * @param letter to check
     * @return boolean
     * */
    private boolean checkLowerCaseAndUpperCase(char letter)
    {
        return (letter >= 'A' && letter <= 'Z') || (letter >= 'a' && letter <= 'z');
    }
}