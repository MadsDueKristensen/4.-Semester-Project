package racing.player;

import racing.common.player.Player;
import racing.common.data.Entity;
import racing.common.data.GameData;
import racing.common.data.GameImage;
import racing.common.data.World;
import racing.common.data.entityparts.MovingPart;
import racing.common.data.entityparts.PositionPart;
import racing.common.services.IGamePluginService;
import racing.common.data.entityparts.ItemPart;
import racing.common.data.entityparts.ScorePart;
import racing.common.player.PlayerSPI;

public class PlayerPlugin implements IGamePluginService, PlayerSPI {

    /**
     * Player
     */
    private static Entity player;

    /**
     * Start the plugin, initilize a new player and add it to the world of
     * entities
     *
     * @param gameData
     * @param world
     */
    @Override
    public void start(GameData gameData, World world) {
        if (gameData.isGameRunning()) {
            create(gameData, world);
        }
        // // Add entities to the world
        // player = createPlayerCar(gameData);
        // world.addEntity(player);
    }

    /**
     * Helper method to create a new player car
     *
     * @param gameData
     * @return player
     */
    private Entity createPlayerCar() {
        float deacceleration = 10;
        float acceleration = 10;
        float maxSpeed = 400;
        float rotationSpeed = 3;
        float x = 500;
        float y = 800;
        float radians = 0.0f;

        Entity playerCar = new Player();
        GameImage img = new GameImage("cars/car.png", 100, 50);
        playerCar.setImage(img);
        playerCar.add(new MovingPart(deacceleration, acceleration, maxSpeed, rotationSpeed));
        playerCar.add(new PositionPart(x, y, radians));
        playerCar.add(new ScorePart());
        playerCar.add(new ItemPart());

        return playerCar;
    }


    /**
     * Remove entity from the world if the bundle is removed
     *
     * @param gameData
     * @param world
     */
    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        world.removeEntity(player);
    }

    @Override
    public PositionPart getPosition() {
        if (player != null) {
            return player.getPart(PositionPart.class);
        }
        return null;
    }

    @Override
    public Player create(GameData gameData, World world) {
        Entity player = createPlayerCar();
        world.addEntity(player);
        this.player = player;
        return (Player) player;
    }

    @Override
    public void removeAll(GameData gameData, World world) {
        this.player = null;
        for (Entity npc : world.getEntities(Player.class)) {
            world.removeEntity(npc);
        }
    }

}
