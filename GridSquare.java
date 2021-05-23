/**
 * Container for square info
 * Stores some booleans, I tried to compact into 
 * enum but didn't work. There should be a better way, could be bothered to find it
 */
public class GridSquare {
  private boolean revealed; 
  private boolean bomb;
  private boolean flagged;

  public GridSquare(boolean _bomb) {
    bomb = _bomb;
  }

  public boolean isBomb() {
    return bomb;
  }

  public boolean isRevealed() {
    return revealed;
  }

  public void reveal() {
    revealed = true;
  }

  public void flag(boolean f) {
    flagged = f;
  }

  public boolean isFlagged() {
    return flagged;
  }

}