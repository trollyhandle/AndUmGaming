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

    @Expose
    private boolean firstSettlementPlaced; //used exclusively when placing the first 2 settlements.

    public boolean getFirstSettlementPlaced()
    {
        return firstSettlementPlaced;
    }

    public void setFirstSettlementPlaced(boolean b)
    {
        firstSettlementPlaced = b;
    }

	public static final int lookup[][] =
	//	[WHEAT, WOOD, ORE, BRICK, SHEEP]
    {//	[0,1,2,3,4,5]
        {0,1,0,1,0,(int)'R'},//Road
        {1,1,0,1,1,(int)'S'},//Settlement
        {2,0,3,0,0,(int)'C'},//City
        {1,0,1,0,1,(int)'D'}};//Dev


	public Player()
    {
        resources = new int[] {2, 2, 2, 2, 2};
        cards = new int[] {0, 0, 0, 0};
        firstSettlementPlaced = false;
    }

    // TODO lots of things.. mostly coordinate with server status

    public void addResources(int[] newres) {
        for (int i = 1; i < newres.length; i++) {
            resources[i-1] += newres[i];
        }
    }

	public boolean canBuyRoad() {
        return(resources[1] >= lookup[0][1] && resources[3] >= lookup[0][3]);
	}

    	
	public boolean canBuySettlement() {
        return (resources[0] >= lookup[1][0] && resources[1] >= lookup[1][1]
                && resources[3] >= lookup[1][3] && resources[4] >= lookup[1][4]);
	}


	public boolean canBuyCity() {
        return (resources[1] >= lookup[2][1] && resources[3] >= lookup[2][3]);
	}

    
    public boolean canBuyDev()
    {
        return false;
    }



    public void buyRoad(){
        resources[1] -= lookup[0][1];
        resources[3] -= lookup[0][3];

    }


    public void buySettlement(){
        resources[0] -= lookup[1][0];
        resources[1] -= lookup[1][1];
        resources[2] -= lookup[1][3];
        resources[3] -= lookup[1][4];
    }


    public void buyCity(){
        resources[1] -= lookup[2][1];
        resources[3] -= lookup[2][3];

    }

    public void buyDev(){}

	public int getWheat()   { return resources[0]; }
	public int getWood()    { return resources[1]; }
	public int getOre()     { return resources[2]; }
	public int getBrick()   { return resources[3]; }
	public int getSheep()   { return resources[4]; }
}
