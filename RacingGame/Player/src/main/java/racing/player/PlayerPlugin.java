package racing.player;

import racing.common.data.Entity;
import racing.common.data.GameData;
import racing.common.data.GameImage;
import racing.common.data.World;
import racing.common.data.entityparts.MovingPart;
import racing.common.data.entityparts.PositionPart;
import racing.common.services.IGamePluginService;
import java.util.UUID;


public class PlayerPlugin implements IGamePluginService {

    private Entity player;

    public PlayerPlugin() {
    }

    @Override
    public void start(GameData gameData, World world) {
        // Add entities to the world
        player = createPlayerCar(gameData);
        world.addEntity(player);
    }

    private Entity createPlayerCar(GameData gameData) {
        float deacceleration = 10;
        float acceleration = 200;
        float maxSpeed = 300;
        float rotationSpeed = 5;
        float x = gameData.getDisplayWidth() / 2;
        float y = gameData.getDisplayHeight() / 2;
        float radians = 3.1415f / 2;
        Entity playerCar = new Player();
        GameImage img = new GameImage("cars/car.png", 100, 50);
        playerCar.setImage(img);
        playerCar.add(new MovingPart(deacceleration, acceleration, maxSpeed, rotationSpeed));
        playerCar.add(new PositionPart(x, y, radians));
        UUID uuid = UUID.randomUUID();

        return playerCar;
    }

    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        world.removeEntity(player);
    }

}
