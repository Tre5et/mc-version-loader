package dev.treset.mcdl.util.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class Caching<T> {
    private HashMap<String, CacheValue<T>> cache = new HashMap<>();
    private long durationMs = 10 * 60 * 1000;

    Caching() {
        onStartup((newCache) -> cache = newCache);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> onShutdown(cache)));
    }

    public record CacheValue<T>(T value, long expirationTime) {}

    protected T getIfValid(String key) {
        CacheValue<T> value = cache.get(key);
        if(value == null) {
            return null;
        }
        if(value.expirationTime < System.currentTimeMillis()) {
            cache.remove(key);
            return null;
        }
        return value.value;
    }
    public abstract T get(String key);

    protected void addToCache(String key, T value, Long validDuration) {
        cache.put(key, new CacheValue<>(value, System.currentTimeMillis() + (validDuration == null ? durationMs : validDuration)));
    }
    public void put(String key, T value) {
        put(key, value, durationMs);
    }
    public abstract void put(String key, T value, Long validDuration);

    public long getCacheDurationMs() {
        return durationMs;
    }

    public void setCacheDuration(long durationMs) {
        this.durationMs = durationMs;
    }

    abstract void onStartup(Consumer<HashMap<String, CacheValue<T>>> setCache);
    abstract void onShutdown(Map<String, CacheValue<T>> cache);
}
