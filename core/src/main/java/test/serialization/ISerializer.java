package test.serialization;

/**
 * Интерфейс сериализвтора
 * для различных имплементаций по желанию
 */

public interface ISerializer {
    <T> T createFromJson(String jasonString, Class<T> clazz);
    <T> String toJson(T object);
}
