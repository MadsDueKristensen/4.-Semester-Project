package racing.ai.services;

import racing.ai.astar.AStar;
import racing.ai.astar.AStarNode;
import racing.common.data.Entity;
import racing.common.data.GameData;
import racing.common.data.World;
import racing.common.data.entityparts.PositionPart;
import racing.common.map.MapSPI;
import racing.common.map.Tile;
import racing.common.services.IGamePluginService;
import racing.common.ai.AISPI;
import racing.common.data.TileType;
import racing.common.data.entityparts.TilePart;

/**
 *
 * @author Victor Gram & Niclas Johansen
 */
public class AIPlugin implements IGamePluginService, AISPI {
    
    /**
     * MapSPI
     */
    private MapSPI mapSPI;

    private AStar ai;
    /**
     * Declarative service set map service
     *
     * @param map map service
     */
    public void setMapService(MapSPI map) {
        this.mapSPI = map;
    }

    /**
     * Declarative service remove map service
     *
     * @param map map service
     */
    public void removeMapService(MapSPI map) {
        this.mapSPI = null;
    }

    @Override
    public void start(GameData gameData, World world) {
    }

    @Override
    public void stop(GameData gameData, World world) {
    }
    
    @Override
    public void startAI() {
        initializeAI();
    }
    
    private void initializeAI() {
        Tile[][] map = mapSPI.getLoadedMap();
        int r = map.length;
        int c = map[0].length;
        
        AStarNode[][] nodes = new AStarNode[r][c];
        
        for(int i = 0; i < r; i++) { 
            for(int j = 0; j < c; j++) { 
                Tile pp = map[i][j];
                int[] coordinates = mapSPI.getTileXandY(pp);
                AStarNode node = new AStarNode(coordinates[0], coordinates[1]);
                nodes[i][j] = node;
            }
        }
        
        AStarNode initNode = new AStarNode((r/2), (c/2));
        AStarNode finalNode = new AStarNode(r, c);
        ai = new AStar(r, c, initNode, finalNode);
        ai.setSearchArea(nodes);
        
        
    }

    @Override
    public PositionPart findNextPosition() {
        PositionPart pp = ai.findNextPosition();
        Tile[][]map = mapSPI.getLoadedMap();
        int x = Math.round(pp.getX());
        int y = Math.round(pp.getY());
        Tile gt = map[x][y];
        return gt.getPart(PositionPart.class);
        
    }

    @Override
    public void setSourceAndTargetNodes(Entity p, World world) {
        Tile t = mapSPI.getTile(p, world);
        int[] coordinates = mapSPI.getTileXandY(t);
        int x = Math.round(coordinates[0]);
        int y = Math.round(coordinates[1]);
        
        Tile[][] map = mapSPI.getLoadedMap();
        
        
        AStarNode source = new AStarNode(x,y);
        AStarNode target = null;
        for(int i = 0; i < map.length; i++){
          for(int j = 0; j < map[0].length; j++){
            TilePart tp = map[i][j].getPart(TilePart.class);
            TileType tt = tp.getType();
            
            if(tt == TileType.FINISHLINE){
              int[] newGoalTile = mapSPI.getTileXandY(map[i][j]);
              target = new AStarNode(newGoalTile[0],newGoalTile[1]);
              System.out.println("goal:" + newGoalTile[0] + " " +  newGoalTile[1]);
            }
          }
        }

        
        
        ai.setSourceAndTargetNodes(source, target);
    }
}
