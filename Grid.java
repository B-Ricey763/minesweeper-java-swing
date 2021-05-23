import java.util.ArrayList;

public class Grid {
  private final int kSafeDist = 3; // constant; area around spawn that cannot be a bomb
  private final double kBombSpawnChance = 0.2;

  private GridSquare[][] squares;
  private boolean generated;

  public Grid(int w, int h) {
    // Invert height since row-major
    squares = new GridSquare[h][w];
  }

  public boolean isGenerated() {
    return generated;
  }

  /**
   * So what basically happens is that the first spot you click
   * is the start row & col. A 3x3 area around that spot CANNOT
   * be a bomb, giving the player a guarnteed safe area to make the game
   * less luck based. For every other square, it is random chance to decide
   * if it's a bomb or normal.
   */
  public void generate(int startRow, int startCol) {
    generated = true;
    // The start has to be a bunch of empty squares
    for (int r = 0; r < squares.length; r++) {
      for (int c = 0; c < squares[0].length; c++) {
        double dist = getDist(r, c, startRow, startCol);
        if (Math.random() < kBombSpawnChance && !(dist < kSafeDist))
          // This tile is a bomb because it passed the chance and
          // its far enough away from the start
          squares[r][c] = new GridSquare(true);
        else
          // Everything else is not a bomb
          squares[r][c] = new GridSquare(false);
      }
    }
    reveal(startRow, startCol);
  }

  /* SOME HELPER METHODS */

  public int getRows() {
    return squares.length;
  }

  public int getCols() {
    return squares[0].length;
  }

  public GridSquare get(int r, int c) {
    return squares[r][c];
  }

  public void toggleFlag(int r, int c) {
    squares[r][c].flag(!squares[r][c].isFlagged());
  }

  public boolean isFlagged(int r, int c) {
    return squares[r][c].isFlagged();
  }

  public boolean isBomb(int r, int c) {
    return squares[r][c].isBomb();
  }

  public boolean isRevealed(int r, int c) {
    return squares[r][c].isRevealed();
  }

  /**
   * Prevents ArrayOutOfBounds exceptions
   */
  public boolean withinBounds(int r, int c) {
    return (r >= 0 && r < squares.length) && (c >= 0 && c < squares[0].length);
  }

  /**
   * Simple algebra distance formula
   */
  public double getDist(int x1, int y1, int x2, int y2) {
    double x = Math.pow((x2 - x1), 2);
    double y = Math.pow((y2 - y1), 2);
    return Math.sqrt(x + y);
  }

  /**
   * Recursive square revealer. If there are no bordering squares
   * that are bordering bombs, it calls itself so all empty areas
   * can be revealed (like actual minesweeper)
   */
  public void reveal(int r, int c) {
    squares[r][c].reveal();

    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        if (i + j != 0) {
          int absRow = r + i;
          int absCol = c + j;

          if (withinBounds(absRow, absCol)
              && !isBomb(absRow, absCol)
              && !isRevealed(absRow, absCol)
              && getAdjacentBombs(r, c) == 0) {
              reveal(absRow, absCol);      
          }
        }
      }
    } 
  }

  /**
   * Finds the number of bombs directly next to
   * the square at r, c. This includes diagonals
   */
  public int getAdjacentBombs(int r, int c) {
    int num = 0;
    for (int i = -1; i <= 1; i++) {
      for (int j = -1; j <= 1; j++) {
        int absRow = r + i;
        int absCol = c + j;
        if (withinBounds(absRow, absCol) && isBomb(absRow, absCol))
          num++; 
      }
    }
    return num;
  }
}