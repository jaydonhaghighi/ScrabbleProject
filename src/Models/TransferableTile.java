package Models; /**
 * This class is part of the "Scrabble" application.
 *
 * To represent a Models.Tile as a Transferable in relation to the drag and drop functionality of the board and hand.
 * Define a flavour for the Models.Tile that defines what will be transferred when dragging and dropping to the board.
 *
 * @author Mohamed Kaddour
 * @date 2022.11.13
 */

import Models.Tile;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.Serializable;

public class TransferableTile implements Transferable, Serializable {

    public static final DataFlavor tileFlavor =
            new DataFlavor(Tile.class, "Models.Tile");


    public static final DataFlavor[] supportedFlavors = {
            tileFlavor
    };

    private final Tile tile;

    /**
     * Constructor that takes in the tile to be transferred.
     * @param tile
     * */
    public TransferableTile(Tile tile) {

        this.tile = tile;
    }

    /**
     * returns the supported flavours when a drag and drop is initiated.
     * @return DataFlavor[]
     * */
    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return supportedFlavors;
    }

    /**
     * Checks if the flavor that is being drag and dropped is supported
     * @return boolean if data flavour is supported return true
     * */
    @Override
    public boolean isDataFlavorSupported(DataFlavor dataFlavor) {
        return dataFlavor.equals(tileFlavor) ||
                dataFlavor.equals(DataFlavor.stringFlavor);
    }

    /**
     * Return the transferable data when it's time to drag.
     * @return Object
     * */
    @Override
    public Object getTransferData(DataFlavor flavor) {
        return this.tile;
    }
}