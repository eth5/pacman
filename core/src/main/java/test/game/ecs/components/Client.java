package test.game.ecs.components;

import com.artemis.PooledComponent;
import test.game.Connection;

/**
 * Компонент хранит данные о соединении с сервером, для использования в системах
 */

public class Client extends PooledComponent {
    public Connection connection;

    @Override
    protected void reset() {
        if (connection!=null) connection.close();
        connection = null;
    }
}
