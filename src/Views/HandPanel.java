package Views; /**
 * This class is part of the "Scrabble" application.
 *
 * Models.Hand class is used to simulate the Models.Player's hand as it contains an ArrayList containing Tiles. It is also responsible
 * for interacting with this ArrayList by adding and removing Hands respectively. Also performs passive actions on
 * the Models.Hand like shuffling. Models.Hand also keeps track of recently removed and added Tiles
 *
 * @author Mohamed Kaddour
 * @date 2022.11.13
 */

import Controllers.DragGestureController;
import Models.*;

import javax.swing.*;
import java.awt.*;
import java.awt.dnd.DragSource;
import java.util.ArrayList;
import java.util.List;

public class HandPanel extends JPanel implements ScrabbleView {

    private Player player;
    private Game game;

    private List<JPanel> removedPanels = new ArrayList<>();

    /**
     * Public constructor for Views.HandPanel class
     *
     * @param game
     */
    public HandPanel(Game game)
    {
        this.setPreferredSize(new Dimension(400,30));
        this.game = game;
        game.addScrabbleView(this);
    }

    /**
     * Reinitializes all the tiles in the Views.HandPanel
     */
    private void refreshHand()
    {
        this.removeAll();
        DragGestureController dg = new DragGestureController(this.player);
        removedPanels.clear();

        for (Tile t : this.player.getHand().getHand()) {
            JPanel p = new JPanel();
            p.setBackground(new Color(Square.Multiplier.NONE.getRGB_color()));
            JLabel l = new JLabel(t.toString());
            DragSource ds = new DragSource();
            ds.createDefaultDragGestureRecognizer(l, 1, dg::dragGestureRecognized);
            l.setSize(10,15);
            l.setFont(new Font(Font.MONOSPACED, Font.BOLD,18));
            p.add(l);
            this.add(p);
        }
        this.repaint();
    }

    @Override
    public void update(ScrabbleEvent e) {
        if (e.getCurrentPlayer() != null) {
            this.player = e.getCurrentPlayer();
            refreshHand();
        }
    }

    /**
     * Removes a specified tile from the screen once it has been dropped
     * @param tile to be removed
     * @param tileIsBlank returns if the tile is blank
     * */
    public void removeTile(Tile tile, boolean tileIsBlank) {
        ABC: for (Component component : this.getComponents()) {
            JPanel panel = (JPanel) component;
            for (Component panelComponent : panel.getComponents()) {
                if (removeATile(tileIsBlank, panelComponent, tile)) {
                    this.remove(panel);
                    removedPanels.add(panel);
                    break ABC;
                }
            }
        }

        this.repaint();
    }

    /**
     * Performs a remove and returns whether it's successful or not. Handles specific blank tile cases
     *
     * @param tileIsBlank is tile is blank
     * @param panelComponent the component to be looked at.
     * @param tile the Models.Tile to be looked at
     * */
    private boolean removeATile(boolean tileIsBlank, Component panelComponent, Tile tile)
    {
        return (tileIsBlank && ((JLabel)panelComponent).getText().charAt(0) == '_') ||
                ((JLabel)panelComponent).getText().equals((tile.toString()));
    }
}
