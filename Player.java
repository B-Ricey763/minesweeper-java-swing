import java.awt.event.*;

public class Player {
  private Grid grid;

  public Player(Grid g) {
    grid = g;
  }

  /**
   * Every time player clicks a button, it is forwarded to this method.
   * (The Display is also updated every time as well, it handles the actual click)
   */
  public void onClick(MouseEvent e, int r, int c, Display display) {
    // For the intial click, when nothing is revealed
    if (!grid.isGenerated()) {
      grid.generate(r, c);
      return; 
    }

    // BUTTON1 is Left click
    if (e.getButton() == MouseEvent.BUTTON1) {
      grid.reveal(r, c);
    }
    else if (e.getButton() == MouseEvent.BUTTON3) {
      grid.toggleFlag(r, c);
    }
  }

  /**
   * Awkward method to find if you've won or lost. Since you can win, lose, or just continue,
   * it could not be made into a pure function with no side effects. It notifies the display,
   * (with a ref given by dependency injection), which renders a pretty end screen.
   */
  public void checkWin(Display d) {
    boolean bombsMarked = true; // Win condition defaults to true, loop makes it false if you haven't won
    boolean bombHit = false; // Lose condition is false until it is detected
    for (int r = 0; r < grid.getRows(); r++) {
      for (int c = 0; c < grid.getCols(); c++) {
        // Opposite of win condition:
        // if a square is a bomb and not marked, you haven't won; also if it isn't revealed
        if ((grid.isBomb(r, c) && !grid.isFlagged(r, c)) || !grid.isFlagged(r, c) && !grid.isRevealed(r, c))
          bombsMarked = false;
        // Lose condition: bomb is revealed
        if (grid.isBomb(r, c) && grid.isRevealed(r, c))
          bombHit = true;
      }
    }
    
    // True for win, false for loss
    if (bombsMarked) {
      // they won
      d.endScreen(true);
    } else if (bombHit) {
      // they lost
      d.endScreen(false);
    }
  }
}