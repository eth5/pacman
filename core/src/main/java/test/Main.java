package test;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import test.assets.Assets;
import test.client.GameClient;
import test.client.messages.State;
import test.game.GameWrapper;
import test.game.ecs.EcsWorldBuilder;
import test.game.ecs.Game;
import test.log.Log;
import test.presentation.ConnectingScreen;
import test.presentation.IScreen;

public class Main extends ApplicationAdapter {
    public static float BLOCK_SIZE = 50;
    private IScreen screen;
    private Assets assets;
    private Viewport viewport;
    private SpriteBatch batch;
    private GameWrapper gameWrapper;
    private final Queue<State> states = new Queue<>(5);
    private GameClient gameClient;
    private final String host;
    private final int port;
    public Main(String host, int port){
        this.host = host;
        this.port = port;
    }

    @Override
    public void create() {
        viewport = createViewPort(Gdx.graphics.getWidth(),Gdx.graphics.getWidth(), new OrthographicCamera());
        batch = new SpriteBatch();
        assets = getAssets();

        // экран-заглушка пока не подключились к серверу
        screen = new ConnectingScreen(viewport,batch);
        gameWrapper = new GameWrapper(new Game(states));
    }

    private EcsWorldBuilder getEcsWorldBuilder(){

        return new EcsWorldBuilder()
                // ижектим нужные базовые зависимости
                .dependencyInjection(
                        worldConfiguration -> {
                            worldConfiguration
                                    .register("batch", batch)
                                    .register("viewport",viewport)
                                    .register("assets", assets)
                            ;
                        });
    }

    private void onAssetsLoaded(){
        gameClient = new GameClient(host, port).connect(
                ctx-> gameWrapper.onConnecting(ctx.channel()),
                gameWrapper::onDisconnected,
                (ctx,initialGD)->{
                    states.clear();
                    gameWrapper.clearGame();

                    // вызывается при подключении к серверу,
                    // теперь в главном потоке инжектим нужные зависимости и запускаем игру
                    Gdx.app.postRunnable(()->{
                        EcsWorldBuilder ecsWorldBuilder = getEcsWorldBuilder().dependencyInjection(worldConfiguration -> {
                            worldConfiguration
                                    .register("remotePlayerId", initialGD.remotePlayerId)
                                    .register("field_width", initialGD.columns * BLOCK_SIZE)
                                    .register("field_height", initialGD.rows * BLOCK_SIZE);
                        });

                        gameWrapper.startGame(ecsWorldBuilder, initialGD.remotePlayerId);
                        screen = gameWrapper.screen;
                    });
                },
                // помещаем обновления стейта в очередь
                // вызывается только после к серверу
                states::addLast
        );
    }

    private Viewport createViewPort(int width, int height, OrthographicCamera camera){
        Viewport viewport = new ExtendViewport( width,height, camera);
        viewport.update(width, height, false);
        return viewport;
    }

    private Assets getAssets(){
        Assets assets = new Assets();
        assets.load("atlas.txt", TextureAtlas.class);
        return assets;
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (assets.isLoaded) screen.update(Gdx.graphics.getDeltaTime());
        else if (assets.update()) onAssetsLoaded();
	}

    @Override
    public void dispose() {
        Log.i(this,"dispose!");
		screen.dispose();
        gameClient.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}