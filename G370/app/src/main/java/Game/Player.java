package Game;

/**
 * Player class.
 * Contains a player's data (resources, cards, etc)
 */
public class Player {

    private int resources[];

    private int knights_played, roads_placed;

    private int cards[];

    public Player()
    {

    }

    public int getWheat() { return resources[0]; }
    public int getWood() { return resources[1]; }
    public int getOre() { return resources[2]; }
    public int getBrick() { return resources[3]; }
    public int getSheep() { return resources[4]; }


}
