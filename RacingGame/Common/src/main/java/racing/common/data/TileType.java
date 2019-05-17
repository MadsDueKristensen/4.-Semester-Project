package racing.common.data;

/**
 * Used to describe a tile type
 *
 */
public enum TileType {
    /**
     * Finish line
     */
    FINISHLINE(0, 1.0, 1.0,"tiles/goal.png", true),
    /**
     * Road
     */
    ROAD(1, 1.0, 1.0, "tiles/road.png", false),
    /**
     * Grass
     */
    GRASS(2, 5.0,1000, "tiles/grass.png", false),
    /**
     * Water
     */

    WATER(3, 10.0, 1500, "tiles/water.png", false),


    /**
     * Tree
     */
    TREE(4, 2.0, 1700, "tiles/tree.png", true),

    /**
     * Start
     */
    START(5, 1.0, 1.0, "tiles/start.png", false),

    /**
     * Spawn
     */
    SPAWN(6, 1.0, 1.0,"tiles/spawn.png", false);

 
    /**
     * Used to identify the tile type
     */
    private int value;

    /**
     * The type penalty
     */
    private double penalty;
    
    /**
     * The type weight
     */
    
    private double weight;

    /**
     * The path to the image, this path is relative to the OSGiLibGDX
     * src/main/resources folder
     */
    private String imagePath;

    /**
     * True if the tile is static (Has collision)
     */
    private boolean isStatic;

    private TileType(int value, double penalty, double weight, String imagePath, boolean isStatic) {
        this.value = value;
        this.penalty = penalty;
        this.weight = weight;
        this.imagePath = imagePath;
        this.isStatic = isStatic;
    }

    public int getValue() {
        return value;
    }

    public double getPenalty() {
        return penalty;
    }
    
    public double getWeight() { 
        return weight;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean isIsStatic() {
        return isStatic;
    }
}
