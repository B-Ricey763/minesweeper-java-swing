/*
  So I made minesweeper...
  It works like normal, but the squares are a bit different:
  - Yellow = flagged (use right click to make it)
  - Blue = number square
  - Gray = empty square

  When you hit a bomb, you die instantly, it doesn't give you the
  cute animation like google minesweeper does.

  It does not use an algorithm to generate bombs, its simply random,
  so the games are either really hard or easy. Oh well!

  There is a ton of dependency injection (when you send a reference of an instance
  to another class), which is ugly and stupid, but I don't wanna learn events in java
  so oh well.
*/

public class Main {

  /**
   * Made a method for other classes to call it once game is over
   */
  public static void startGame() {
    Grid g = new Grid(10, 10); // Change input params to change dimensions (rows, cols)
    // Dependency injection oh nooooo....
    Player p = new Player(g);
    Display d = new Display(g, p);
  }

  public static void main(String[] args) {
    startGame();
  }

}