import javax.swing.*;
import java.awt.event.*;
import java.awt.Font;
import java.awt.Color;

/**
 * Used to indicate what the display should render
 * on each button
 */
enum RenderState {
  FLAGGED,
  EMPTY, 
  INDICATOR,
  HIDDEN,
  BOMB
}

/**  
 * Display is responsible for taking the array
 * from Grid and putting in in Swing UI. It also
 * captures input through the buttons.
 */
public class Display {
  public final int BTN_SIZE = 40;

  JFrame frame; // Parent GUI holding entire visuals
  JButton[][] tiles; // 2D array of button tiles (buttons for clicks)
  Grid grid; // A ref to grid instance
  Player player; // a ref to player

  public Display(Grid g, Player p) {
    grid = g;
    player = p;
    frame = new JFrame();
    // 'tiles' must conform to our grid 
    tiles = new JButton[g.getRows()][g.getCols()];
    configTiles();
    configFrame(frame);
  }

  /**
   * Initializes our JFrame and make it work
   */
  private void configFrame(JFrame frameToConfig) {
    frameToConfig.setExtendedState(JFrame.MAXIMIZED_BOTH); 
    frameToConfig.setUndecorated(true);    
    frameToConfig.setLayout(null);
    frameToConfig.setVisible(true);
  }

  /**
   * Initializes each JButton, setting up size and user input
   */
  private void configTiles() {
    for (int r = 0; r < tiles.length; r++) {
      for (int c = 0; c < tiles[0].length; c++) {
        tiles[r][c] = new JButton();
        styleBtn(tiles[r][c]);
        // Offsets each button by size to make a nice grid
        tiles[r][c].setBounds(
            r * BTN_SIZE, c * BTN_SIZE, 
            BTN_SIZE, BTN_SIZE);

        // Have to convert to final to be accepted
        // by abstract class found in createButtonListener()
        final int finalR = r;
        final int finalC = c;
        tiles[r][c].addMouseListener(inputHandler(finalR, finalC));
        frame.add(tiles[r][c]);
      }
    }
  }

  /**
   * Dispatches event to player when button on grid is clicked.
   * It returns an abstract class with basic stuff. Should use
   * just a lambda, not sure if swing supports it.
   */
  private MouseListener inputHandler(int finalR, int finalC) {
    Display ref = this;
    return new MouseListener() {
          public void mouseClicked(MouseEvent e) {
            player.onClick(e, finalR, finalC, ref); // Give it to player
            update(); // Display has to handle this
          }
          // Have to implement these so we properly override
          public void mousePressed(MouseEvent e) {}
          public void mouseReleased(MouseEvent e) {}
          public void mouseEntered(MouseEvent e) {}
          public void mouseExited(MouseEvent e) {}
        };
  }

  /**
   * Takes each state of a square on a grid and converts it
   * into colors and text to display
   */
  public void update() {
    for (int r = 0; r < tiles.length; r++) {
      for (int c = 0; c < tiles[0].length; c++) {
        RenderState state = getRenderState(r, c);
        Color bg = Color.BLACK;
        
        switch (state) {
          case EMPTY:
            bg =  Color.GRAY;
            break;
          case INDICATOR:
            bg = Color.BLUE;
            // Have to append "" to convert to string
            tiles[r][c].setText(grid.getAdjacentBombs(r, c) + "");
            break;
          case FLAGGED:
            bg = Color.ORANGE;
            break;
          case BOMB:
            bg = Color.RED;
            break;
        }
        tiles[r][c].setBackground(bg);
      }
    }

    // Update the player, giving a ref to ourself
    player.checkWin(this);
  }

  /**
   * Java Swing minimalistic styling
   */
  private void styleBtn(JButton button) {
    button.setBorderPainted(true);
    button.setFocusPainted(false);
    button.setContentAreaFilled(false);
    button.setFont(new Font("Arial", Font.PLAIN, 10));
    button.setForeground(Color.WHITE);
    button.setBackground(Color.BLACK);
    button.setOpaque(true);
  }

  /**
   * Takes a coordinate and reads some info. 
   * Returns the appropriate render state for the coord.
   */
  private RenderState getRenderState(int r, int c) {
    if (grid.isFlagged(r, c)) {
      return RenderState.FLAGGED;
    } else if (grid.isRevealed(r, c)) {
      if (grid.getAdjacentBombs(r, c) > 0) // It borders 1 or more bombs
        return RenderState.INDICATOR; // aka number square
      else if (grid.isBomb(r, c))
        return RenderState.BOMB;
      return RenderState.EMPTY;
    }
    return RenderState.HIDDEN; // Defaults to unkown
  }

  /**
   * The code is ugly but does the trick. It creates
   * a little end screen with some text, replay, and quit
   * button.
   */
  public void endScreen(boolean won) {
    // Get rid of game frame
    frame.setVisible(false);
    frame.dispose();
  
    JFrame endFrame = new JFrame();
    
    String endMessage = won ? "You won!" : "You lost!";
    JLabel label = new JLabel(endMessage);
    label.setFont(new Font("Arial", Font.PLAIN, 50));
    label.setBounds(100, 0, 300, 100); // Hard coding but I'm too lazy to make it good

    JButton replay = new JButton("Play again");
    replay.setBounds(100, 90, 300, 50); // More hard coding bleh
    replay.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // Shut everything down and start a new game
        endFrame.dispose();
        Main.startGame();
      }
    });

    JButton quit = new JButton("Quit");
    quit.setBounds(100, 150, 300, 50);
    quit.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        // This stops the program 
        endFrame.dispose();
      }
    });
    
    // Configure everything up
    endFrame.add(label);
    endFrame.add(replay);
    endFrame.add(quit);
    configFrame(endFrame);
  }
}