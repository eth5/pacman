package test.game.ecs.util;

import com.artemis.Component;
import com.artemis.World;
import test.interfaces.Action;

public class EntityConfigurator {
    public final World world;

    public EntityConfigurator(World world) {
        this.world = world;
    }

    private int buildEntityId = -1;

    public <T extends Component> EntityConfigurator begin(Class<T> type, Action.Arg1<T> config) {
        if (buildEntityId != -1) throw new IllegalStateException("Билдер неверно закончил работу!");
        buildEntityId = world.create();
        return add(type, config);
    }

    public <T extends Component> EntityConfigurator add(Class<T> type, Action.Arg1<T> config) {
        if (buildEntityId == -1) throw new IllegalStateException("Билдер нужно начать с метода begin");
        T component = createComponent(buildEntityId, type);
        config.invoke(component);
        return this;
    }

    public <T extends Component> EntityConfigurator add(Class<T> type) {
        if (buildEntityId == -1) throw new IllegalStateException("Билдер нужно начать с метода begin");
        createComponent(buildEntityId, type);
        return this;
    }

    public int end() {
        if (buildEntityId == -1) throw new IllegalStateException("Билдер нужно начать с метода begin");
        int id = buildEntityId;
        buildEntityId = -1;
        return id;
    }

    public <T extends Component> T createComponent(int entityId, Class<T> type) {
        return world.getMapper(type).create(entityId);
    }
}
