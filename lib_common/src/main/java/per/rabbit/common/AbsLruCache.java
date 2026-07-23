package per.rabbit.common;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

public abstract class AbsLruCache<T> {

    private final Cache<String, T> cache;

    public AbsLruCache() {
        cache = Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1_000)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .recordStats()
                .build();
    }

    /**
     * 从缓存中获取
     *
     * @param key
     * @return
     */
    public T get(String key) {
        return cache.getIfPresent(key);
    }

    /**
     * 从缓存中读取或加载数据
     *
     * @param key
     * @return
     */
    public T getOrLoad(String key) {
        // 缓存不存在，执行加载逻辑
        return cache.get(key, this::loadData);
    }

    /**
     * 写入缓存
     */
    public void put(String key, T value) {
        cache.put(key, value);
    }

    /**
     * 加载数据
     */
    protected abstract T loadData(String key);
}
