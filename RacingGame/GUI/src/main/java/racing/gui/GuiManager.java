package racing.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.SkinLoader;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import java.util.HashMap;
import java.util.Map;
import racing.Core;
import racing.common.data.Entity;
import racing.common.data.GameData;
import racing.common.data.GameImage;
import racing.common.data.World;
import racing.common.data.entityparts.PositionPart;
import racing.common.map.MapSPI;
import racing.common.map.Tile;
import racing.common.npc.NPCSPI;
import racing.common.player.PlayerSPI;
import racing.common.services.IGamePluginService;
import racing.common.services.IScoreService;
import racing.gui.screen.*;
import static racing.gui.screen.GameScreen.GAME;
import static racing.gui.screen.GameScreen.GAME_OVER;
import static racing.gui.screen.GameScreen.MAP_EDITOR;
import static racing.gui.screen.GameScreen.MENU;
import static racing.gui.screen.GameScreen.START_GAME;

/**
 * This class is rendering the game using LibGDX
 */
public class GuiManager extends Game implements IGamePluginService { //implements IGamePluginService

    /**
     * The a asset manager
     */
    private AssetManager assetManager = new AssetManager();

    /**
     * The Game screens
     */
    private Map<GameScreen, BasicScreen> gameScreens;

    /**
     * Sprite batch
     */
    private SpriteBatch batch;

    /**
     * Bitmap font
     */
    private BitmapFont font;

    /**
     * Skin
     */
    private Skin skin;

    /**
     * MapSPI
     */
    private static MapSPI map;

    /**
     * NPCSPI
     */
    private static NPCSPI npc;

    /**
     * PlayerSPI
     */
    private static PlayerSPI player;

    /**
     * IScoreService
     */
    private static IScoreService score;

    /**
     * GuiManager
     */
    private static GuiManager instance;

    public GuiManager() {
        gameScreens = new HashMap<>();
        assetManager = new AssetManager();
        SkinLoader.SkinParameter p = new SkinLoader.SkinParameter("skin/uiskin.atlas");
        assetManager.load("skin/uiskin.json", Skin.class, p);
        loadImages();
        init();
        instance = this;
    }

    /**
     * Get instance of GuiManager
     *
     * @return GuiManager
     */
    public static GuiManager getInstance() {
        return instance;
    }

    @Override
    public void create() {
        batch = new SpriteBatch();
        font = new BitmapFont();
        assetManager.finishLoading();
        skin = assetManager.get("skin/uiskin.json");
        changeScreen(MENU);
    }

    /**
     * Instantiates the game
     */
    private void init() {
        Core.getInstance().getGameData().setDisplayHeight(600);
        Core.getInstance().getGameData().setDisplayWidth(800);
        LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
        cfg.title = "WoadWacing";
        cfg.width = 800;
        cfg.height = 600;
        cfg.useGL30 = false;
        cfg.resizable = true;

        new LwjglApplication(this, cfg);
    }

    @Override
    public void render() {
        super.render();
    }

    /**
     * Updates the game
     */
    public void update() {
        Core.getInstance().update();
    }

    /**
     * Draw the game
     */
    public void draw() {
        batch.begin();
        for (Entity entity : Core.getInstance().getWorld().getEntities()) {
            if (entity instanceof Tile) {
                GameImage image = entity.getImage();
                Texture tex = assetManager.get(image.getImagePath(), Texture.class);
                PositionPart p = entity.getPart(PositionPart.class);
                drawSprite(new Sprite(tex), image, p);
            }
        }
        for (Entity entity : Core.getInstance().getWorld().getEntities()) {
            if (!(entity instanceof Tile)) {
                GameImage image = entity.getImage();
                Texture tex = assetManager.get(image.getImagePath(), Texture.class);
                PositionPart p = entity.getPart(PositionPart.class);
                drawSprite(new Sprite(tex), image, p);
            }
        }
        batch.end();
    }

    /**
     * Draw the sprite
     *
     * @param s the sprite
     * @param image the image
     * @param p the positionPart
     */
    private void drawSprite(Sprite s, GameImage image, PositionPart p) {
        s.setOrigin(image.getWidth() / 2, image.getHeight() / 2);
        s.rotate((float) Math.toDegrees(p.getRadians()));
        s.setX(p.getX());
        s.setY(p.getY());
        s.setSize(image.getWidth(), image.getHeight());
        s.draw(batch);
    }

    /**
     * Change screen
     *
     * @param screen value representing the screen
     */
    public void changeScreen(GameScreen screen) {
        Core.getInstance().getGameData().getKeys().reset();
        switch (screen) {
            case MENU:
                if (gameScreens.get(MENU) == null) {
                    gameScreens.put(MENU, new MenuScreen());
                }
                Core.getInstance().getGameData().setGameRunning(false);
                this.setScreen(gameScreens.get(MENU));
                reset();
                break;
            case MAP_EDITOR:
                if (gameScreens.get(MAP_EDITOR) == null) {
                    gameScreens.put(MAP_EDITOR, new MapEditor());
                }
                Core.getInstance().getGameData().setGameRunning(false);
                this.setScreen(gameScreens.get(MAP_EDITOR));
                break;
            case GAME:
                if (gameScreens.get(GAME) == null) {
                    gameScreens.put(GAME, new MainScreen());
                }
                Core.getInstance().getGameData().setGameRunning(true);
                this.setScreen(gameScreens.get(GAME));
                break;
            case START_GAME:
                if (gameScreens.get(START_GAME) == null) {
                    gameScreens.put(START_GAME, new StartGameScreen());
                }
                Core.getInstance().getGameData().setGameRunning(false);
                this.setScreen(gameScreens.get(START_GAME));
                break;
            case GAME_OVER:
                if (gameScreens.get(GAME_OVER) == null) {
                    gameScreens.put(GAME_OVER, new GameOverScreen());
                }
                Core.getInstance().getGameData().setGameRunning(false);
                this.setScreen(gameScreens.get(GAME_OVER));
                break;
        }
    }

    /**
     * Stops player, enemy and map
     */
    public void reset() {
        score.reset();
        player.removeAll(Core.getInstance().getGameData(), Core.getInstance().getWorld());
        npc.removeAll(Core.getInstance().getGameData(), Core.getInstance().getWorld());
        map.removeAll(Core.getInstance().getWorld());
    }

    /**
     * Loads the images needed for the game
     */
    private void loadImages() {
        assetManager.load("default.png", Texture.class);

        //CARS
        assetManager.load("cars/car.png", Texture.class);
        assetManager.load("cars/car1.png", Texture.class);
        assetManager.load("cars/car2.png", Texture.class);
        assetManager.load("cars/car3.png", Texture.class);
        assetManager.load("cars/car4.png", Texture.class);
        assetManager.load("cars/car5.png", Texture.class);

        //TILES
        assetManager.load("tiles/road.png", Texture.class);
        assetManager.load("tiles/start.png", Texture.class);
        assetManager.load("tiles/grass.png", Texture.class);
        assetManager.load("tiles/water.png", Texture.class);
        assetManager.load("tiles/goal.png", Texture.class);
        assetManager.load("tiles/tree.png", Texture.class);
        assetManager.load("tiles/spawn.png", Texture.class);
        assetManager.load("tiles/item.png",Texture.class);

        assetManager.load("tiles/road_sel.png", Texture.class);
        assetManager.load("tiles/start_sel.png", Texture.class);
        assetManager.load("tiles/grass_sel.png", Texture.class);
        assetManager.load("tiles/water_sel.png", Texture.class);
        assetManager.load("tiles/goal_sel.png", Texture.class);
        assetManager.load("tiles/tree_sel.png", Texture.class);
        assetManager.load("tiles/spawn_sel.png", Texture.class);
        assetManager.load("tiles/item_sel.png", Texture.class);
        
        assetManager.load("items/non.png", Texture.class);
        assetManager.load("items/bullet.png", Texture.class);
        assetManager.load("items/bomb.png", Texture.class);
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public BitmapFont getFont() {
        return font;
    }

    public Skin getSkin() {
        return skin;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public MapSPI getMap() {
        return map;
    }

    public NPCSPI getNpc() {
        return npc;
    }

    public PlayerSPI getPlayer() {
        return player;
    }

    public IScoreService getScore() {
        return score;
    }

    @Override
    public void start(GameData gameData, World world) {
    }

    @Override
    public void stop(GameData gameData, World world) {
        Gdx.app.exit();
    }

    /**
     * Declarative service set map service
     *
     * @param map map service
     */
    public void setMapService(MapSPI map) {
        this.map = map;
    }

    /**
     * Declarative service remove map service
     *
     * @param map map service
     */
    public void removeMapService(MapSPI map) {
        this.map = null;
    }

    /**
     * Declarative service set NPC service
     *
     * @param npc NPC service
     */
    public void setNPCService(NPCSPI npc) {
        this.npc = npc;
    }

    /**
     * Declarative service remove npc service
     *
     * @param npc npc service
     */
    public void removeNPCService(NPCSPI npc) {
        this.npc = null;
    }

    /**
     * Declarative service set player service
     *
     * @param player player service
     */
    public void setPlayerService(PlayerSPI player) {
        this.player = player;
    }

    /**
     * Declarative service remove player service
     *
     * @param player player service
     */
    public void removePlayerService(PlayerSPI player) {
        this.player = null;
    }

    /**
     * Declarative service set score service
     *
     * @param score score service
     */
    public void setScoreService(IScoreService score) {
        this.score = score;
    }

    /**
     * Declarative service remove score service
     *
     * @param score score service
     */
    public void removeScoreService(IScoreService score) {
        this.score = null;
    }
}
