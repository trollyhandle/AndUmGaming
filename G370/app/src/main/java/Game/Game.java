package Game;

/**
 * Board and Player manager class.
 * Author: Tyler Holland
 */
public class Game {

    public enum RESOURCES {
        BLANK   (0xffffffff), WHEAT   (0xffFFDF00), WOOD    (0xff014421),
        ORE     (0xff8A7F80), BRICK   (0xffCB4154), SHEEP   (0xff98FF98);
        private int col; RESOURCES(int color) { col =color; }
        static int getColor(int i) { switch(i) {
            case 1: return WHEAT.col; case 2: return WOOD.col;
            case 3: return ORE.col; case 4: return BRICK.col;
            case 5: return SHEEP.col; } return BLANK.col; }
        static int index(RESOURCES r) { switch(r) {
            case WHEAT: return 1; case WOOD: return 2;
            case ORE: return 3; case BRICK: return 4;
            case SHEEP: return 5; } return 0; }
    }
    // PLAYER PAINT_COLOR LOOKUP
    public enum PLAYERS {
        NONE (0xffFFFFFF)/*WHITE*/,
        ONE (0xffFF0800)/*RED*/,    TWO(0xff00FF00)/*GREEN*/,
        THREE(0xff1C1CF0)/*BLUE*/,  FOUR(0xffBF00FF)/*VIOLET*/;
        private int col; PLAYERS(int value) { col =value; }
        static int getColor(int i) { switch(i) {
            case 1: return ONE.col; case 2: return TWO.col;
            case 3: return THREE.col; case 4: return FOUR.col;
        } return NONE.col; }
    }






















}
