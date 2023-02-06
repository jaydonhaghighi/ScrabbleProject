package Views; /**
 * This class extends JPanel and acts as the storage and functionality maintenance of the shuffle, exchange, play
 * and pass options that are implemented within the model. The view gets updated when the current player hand
 * in the case of the exchange menu.
 *
 * @author Jaydon Haghighi
 * @version 2022.11.13
 */

import Controllers.GameCommandPanelController;
import Models.Game;
import Models.Player;
import Models.ScrabbleEvent;
import Models.Tile;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class GameCommandPanel extends JPanel implements ScrabbleView {
    public static final String PLAY = "Button:" + 0 + ":" + 0;
    public static final String EXCHANGE_BUTTON = "Button:" + 0 + ":" + 1;
    public static final String SHUFFLE = "Button:" + 1 + ":" + 0;
    public static final String PASS = "Button:" + 1 + ":" + 1;
    public static final String RESET_SELECTION = "Reset Selection";
    public static final String EXCHANGE_COMMAND = "Exchange";
    private final JButton[][] buttons = new JButton[2][2];
    private Player player;
    ArrayList<Tile> hypotheticalHand = new ArrayList<>();
    JFrame exchangeFrame = new JFrame();
    JPanel tilePanel = new JPanel(new GridLayout(0, 7));
    JPanel resetButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    ArrayList<JToggleButton> tileButtons = new ArrayList<>();
    ArrayList<Tile> tilesToExchange = new ArrayList<>();

    private Game game;

    /**
     * Initializes the panel with the passed in game model.
     * @param game the Models.Game is the model.
     * */
    public GameCommandPanel(Game game) {
        this.game = game;
        this.game.addScrabbleView(this);
        this.player = new Player(10);
        this.hypotheticalHand = this.player.getHand().getHand();
        this.setPreferredSize(new Dimension(500,300));
        this.add(initializeGameCommands());
        initializeExchangeFrame();
    }

    /**
     * Initializes the game commands by setting values to the buttons and initializing the individual gameButtonPanels
     * @return JPanel with the buttons.
     * */
    private JPanel initializeGameCommands() {
        GameCommandPanelController controller = new GameCommandPanelController(this.player, this.tileButtons,
                this.tilesToExchange, this.game, this.exchangeFrame);
        this.hypotheticalHand = this.player.getHand().getHand();
        JPanel gameButtonsPanel = new JPanel(new GridLayout(2, 2));
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                JButton button = new JButton();
                button.setActionCommand("Button:" + i + ":" + j); //setting actionCommand of each button based on i and j
                button.addActionListener(controller);
                buttons[i][j] = button; //adding button to buttons
                gameButtonsPanel.add(button); //adding button to buttonPanel
            }
        }
        buttons[0][0].setText("Play");
        buttons[0][1].setText("Exchange");
        buttons[1][0].setText("Shuffle");
        buttons[1][1].setText("Pass");
        return gameButtonsPanel;
    }

    /**
     * Initializes aspects of the pop-up JFrame containing the possible tiles to be exchanged and also the select
     * buttons for each individual tile available in player's hand.
     * */
    private void initializeExchangeFrame() {
        int counter = 0;
        this.hypotheticalHand = this.player.getHand().getHand();
        GameCommandPanelController controller = new GameCommandPanelController(this.player, this.tileButtons,
                this.tilesToExchange, this.game, this.exchangeFrame);

        tilePanel.removeAll();
        this.resetButtonPanel.removeAll();
        for (Tile tile : this.hypotheticalHand) {
            JToggleButton tileButton = new JToggleButton(tile.getLetter());
            tileButton.setActionCommand("Letter:" + counter);
            tileButton.setName(String.valueOf(hypotheticalHand.get(counter)));
            tileButton.addActionListener(controller);
            tilePanel.add(tileButton);
            tileButtons.add(counter, tileButton);
            counter++;
        }
        JButton exchangeButton = new JButton(EXCHANGE_COMMAND);
        resetButtonPanel.add(exchangeButton);
        exchangeButton.addActionListener(controller);
        exchangeFrame.add(tilePanel);
        exchangeFrame.add(resetButtonPanel, BorderLayout.SOUTH);
        exchangeFrame.setSize(900, 180);
        exchangeFrame.setResizable(false);
        exchangeFrame.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
    }

    @Override
    public void update(ScrabbleEvent e) {
        if (e.getCurrentPlayer() != null) {
            this.player = e.getCurrentPlayer();
            initializeExchangeFrame();
            this.revalidate();
        }
    }
}

