package Views;

import Models.ScrabbleEvent;

/**
 * Functional Interface to update all views (Panels) in model (Models.Game)
 *
 * @Author Mohamed Kaddour
 * @Version
 */

public interface ScrabbleView {

    /**
     * Update view method
     *
     * @param e
     */
    void update(ScrabbleEvent e);
}
