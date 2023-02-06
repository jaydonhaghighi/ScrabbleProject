package Models;

import java.io.Serializable;

/**
 * This class is part of the "Scrabble" application.
 *
 * Holds information of command that has been issued by the user. Holds them separately and handles if certain
 * commands are null. Also parses the direction of the placement attempt. Models.Command is used in Models.Parser where
 * the user input is individually passed into this class through the constructor to initialize the command.
 *
 * @author  Mohamed Kaddour
 * @date 2022.10.25
 */

public class Command implements Serializable {

    public final static int PLACEMENT_ATTEMPT_SIZE = 2;

    private String wordAttempt;
    private String placementAttempt;
    private String action;

    /**
     * Models.Command constructor that takes in all the different parts of the command and initializes
     * the class state.
     * @param action for the action to be done
     * @param placementAttempt for the area in which the Models.Tile will be placed
     * @param wordAttempt for the word that the player intends on playing
     * */
    public Command(String action, String wordAttempt, String placementAttempt) {
        this.wordAttempt = wordAttempt;
        this.placementAttempt = placementAttempt;
        this.action = action;
    }

    /**
     * Getter method for the word attempt part of the command
     * @return String of word attempt
     * */
    public String getWordAttempt() {
        return this.wordAttempt;
    }

    /**
     * Getter method for the placement attempt part of the command
     * @return String of the placement attempt
     * */
    public String getPlacementAttempt() {
        return this.placementAttempt;
    }

    /**
     * Getter method for the action part of the command.
     * @return String for the action
     * */
    public String getAction() {
        return this.action;
    }

    /**
     * Getter method for the placement direction that is parsed out of the placement attempt.
     * @return boolean based on the direction
     * */
    public boolean getPlacementDirection() {
        if (this.placementAttempt != null && this.wordAttempt != null) {
            return Character.isDigit(this.placementAttempt.charAt(0));
        } else {
            System.out.println("NULL PASSED IN");
        }

        return false;
    }

    /**
     * Getter method on the status of the action command.
     * @return boolean true if action is not null
     * */
    public boolean hasAction()
    {
        return (this.action != null);
    }

    /**
     * Getter method on the status of the placement command.
     * @return boolean true if placement attempt is not null
     * */
    public boolean hasPlacementAttempt() {
        return (this.placementAttempt != null);
    }

    /**
     * Getter method on the status of the word attempt command.
     * @return boolean true if the word attempt is not null
     * */
    public boolean hasWordAttempt() {
        return (this.wordAttempt != null);
    }
}