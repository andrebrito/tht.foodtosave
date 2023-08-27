package test.foodtosave.configs;

import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@EnableCaching
@Configuration
public class RedisConfig {

    private final Long DEFAULT_TTL = 10L;

    @Autowired
    private Environment environment;

    @Bean
    public RedisCacheConfiguration defaultCacheConfiguration() {
        final long ttl = getTtl();
        return RedisCacheConfiguration.defaultCacheConfig()
                                      .entryTtl(Duration.ofSeconds(ttl))
                                      .disableCachingNullValues()
                                      .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                                              new GenericJackson2JsonRedisSerializer()));
    }

    private long getTtl() {
        final String ttlFromEnv = this.environment.getProperty("redis.ttl");
        return NumberUtils.isDigits(ttlFromEnv) ? Long.parseLong(ttlFromEnv)
                : this.DEFAULT_TTL;
    }
}
