package Game;

/**
 * Test class for Game classes.
 * Author: Tyler Holland
 */
public class BoardTest {

    public static void main(String args[])
    {
        System.out.println("tests begin");
        System.out.println("creating board...");

        Board testBoard = new Board(125, new Point_XY(0,0));

        System.out.println("printing board:\n" + testBoard);

        System.out.println("tests complete");
    }

}
