package Models;

import java.io.Serializable;

/**
 * Exception handler which will be implemented in the future milestones
 *
 * @author Jaydon Haghighi
 * @version 2022.10.25
 */
public class NotEnoughTiles extends Exception implements Serializable {

    public NotEnoughTiles(String message) {
        super(message);
    }
}
