package Game;

import com.google.gson.annotations.Expose;

/**
 * Player class.
 * Contains a player's data (resources, cards, etc)
 */
public class Player {

    @Expose
    private int resources[];

    private int knights_played;
    private int roads_placed;

    @Expose
    private int cards[];

	public static final int lookup[][] =
	//	[BRICK, WOOD, SHEEP, WHEAT, ORE]
    {//	[0,1,2,3,4,5]
        {1,1,0,0,0,(int)'R'},//Road
        {1,1,1,1,0,(int)'S'},//Settlement
        {0,0,0,2,3,(int)'C'},//City
        {0,0,1,1,1,(int)'D'}};//Dev


	public Player()
    {
        resources = new int[] {0, 0, 0, 0, 0};
        cards = new int[] {0, 0, 0, 0};
    }

    // TODO lots of things.. mostly coordinate with server status


	public boolean buyRoad()
    {
        if(resources[0] >= lookup[0][0] && resources[1] >= lookup[0][1]) {

            resources[0] -= lookup[0][0];
            resources[1] -= lookup[0][1];
            return true;
        }
        return false;
	}
    	
	public boolean buySettlement()
    {
        if(resources[0] >= lookup[1][0] && resources[1] >= lookup[1][1]
                && resources[2] >= lookup[1][2] && resources[3] >= lookup[1][3]) {
            resources[0] -= lookup[1][0];
            resources[1] -= lookup[1][1];
            resources[2] -= lookup[1][2];
            resources[3] -= lookup[1][3];
            return true;
        }
        return false;
	}

	public boolean buyCity()
    {
        if(resources[3] >=lookup[2][3] && resources[4] >= lookup[2][4]) {
            resources[3] -= lookup[2][3];
            resources[4] -= lookup[2][4];
            return true;
        }
        return false;
	}

    public boolean buyDev()
    {
        return false;
    }

	public int getWheat()   { return resources[0]; }
	public int getWood()    { return resources[1]; }
	public int getOre()     { return resources[2]; }
	public int getBrick()   { return resources[3]; }
	public int getSheep()   { return resources[4]; }
}
