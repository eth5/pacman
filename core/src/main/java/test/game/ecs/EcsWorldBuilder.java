package test.game.ecs;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.WorldConfigurationBuilder;
import test.game.ecs.systems.*;
import test.game.ecs.systems.camera.CameraTargetSystem;
import test.game.ecs.systems.draw.AnimationSystem;
import test.game.ecs.systems.draw.DrawPlayerDataSystem;
import test.game.ecs.systems.draw.DrawSystem;
import test.game.ecs.systems.input.InputKeyboardSystem;
import test.game.ecs.systems.input.SendKeysSystem;
import test.game.ecs.systems.position.InterpolationSystem;
import test.game.ecs.systems.position.PositionConverterSystem;
import test.game.ecs.systems.position.DynamicPositionConverterSystem;
import test.game.ecs.systems.state.InitialSystem;
import test.game.ecs.systems.state.UpdateStateSystem;
import test.interfaces.Action;

/**
 * Класс имплементирующий создание ecs мира с нужными системами,
 * позволяет добавить в системы необходимые зависимости перед созданием
 */


public class EcsWorldBuilder {
    private final WorldConfiguration worldConfiguration;
    private boolean isBuild = false;

    public EcsWorldBuilder() {
        worldConfiguration = getDefaultEcsSystems().build();
    }

    public World build() {
        return new World(worldConfiguration);
    }

    public EcsWorldBuilder dependencyInjection(Action.Arg1<WorldConfiguration> injectRegistre) {
        injectRegistre.invoke(worldConfiguration);
        return this;
    }

    private WorldConfigurationBuilder getDefaultEcsSystems() {
        WorldConfigurationBuilder builder = new WorldConfigurationBuilder()
                .with(
                        new UpdateStateSystem(),
                        new InitialSystem(),
                        new InputKeyboardSystem(),
                        new SendKeysSystem(),

                        new PositionConverterSystem(),
                        new DynamicPositionConverterSystem(),
                        new InterpolationSystem(),

                        new AnimationSystem(),
                        new DrawSystem(),
                        new DrawPlayerDataSystem(),

                        new CameraTargetSystem(),
                        new OneFrameSystem()
                );
        return builder;
    }
}
