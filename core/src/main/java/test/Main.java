package test;

import com.badlogic.gdx.ApplicationAdapter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import test.assets.Assets;
import test.game.Connection;
import test.game.ecs.EcsWorldBuilder;
import test.game.ecs.Game;
import test.log.Log;
import test.presentation.ConnectingScreen;
import test.presentation.IScreen;
import test.server.Server;
import test.server.messages.State;

public class Main extends ApplicationAdapter {
    public static float BLOCK_SIZE = 50;
    private IScreen screen;
    private Assets assets;
    private Viewport viewport;
    private SpriteBatch batch;
    private Game game;
    private final Queue<State> states = new Queue<>(5);
    private Server server;

    @Override
    public void create() {
        viewport = createViewPort(Gdx.graphics.getWidth(),Gdx.graphics.getWidth(), new OrthographicCamera());
        batch = new SpriteBatch();
        assets = getAssets();

        //экран-заглушка пока не подключились к серверу
        screen = new ConnectingScreen(viewport,batch);
    }

    private void onAssetsLoaded(){

        // карта соотвествий удаленных(remote) id сущностей - локальным
        // для инекции зависимостей в системы
        final IntMap<Integer> remoteToLocalEntities = new IntMap<>();

        final EcsWorldBuilder ecsWorldBuilder = new EcsWorldBuilder()
                //ижектим нужные базовые зависимости
                .dependencyInjection(
                        worldConfiguration -> {
                            worldConfiguration
                                    .register("batch", batch)
                                    .register("viewport",viewport)
                                    .register("assets", assets)
                                    .register("remoteToLocalEntities",remoteToLocalEntities)
                            ;
                        });

        server = new Server("127.0.0.1", 8080).connect(
                ctx->{
                    Log.i(this, "Connected!");
                    //и всё :)
                },
                ()->{
                    Log.i("Disconnected...");
                    if (screen instanceof Game)((Game) screen).onDisconnection();
                },
                (ctx,initialGD)->{
                    // вызывается при подключении к серверу,
                    // теперь в главном потоке инжектим нужные зависимости и запускаем игру
                    Gdx.app.postRunnable(()->{
                        ecsWorldBuilder.dependencyInjection( worldConfiguration->{
                            worldConfiguration
                                    .register("remotePlayerId", initialGD.remotePlayerId)
                                    .register("field_width", initialGD.width * BLOCK_SIZE)
                                    .register("field_height", initialGD.height * BLOCK_SIZE);
                        });

                        game = new Game(ecsWorldBuilder,states);
                        int playeId = game.createEntityForLocalPlayer();
                        remoteToLocalEntities.put(initialGD.remotePlayerId, playeId);
                        game.onConnecting(playeId,new Connection(ctx));

                        screen = game;
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
        server.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}