package com.yahoo.chat.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.convert.ReferenceResolverImpl;
import org.springframework.data.redis.serializer.*;

@Configuration
public class RedisConfig {
    @Bean
    public ReactiveRedisOperations<String, Object> reactiveRedisTemplate(LettuceConnectionFactory lettuceConnectionFactory){
        RedisSerializationContext<String, Object> serializationContext =
                RedisSerializationContext.<String,Object>newSerializationContext(RedisSerializer.string())
                .value(new Jackson2JsonRedisSerializer<>(Object.class))
                .build();
        return new ReactiveRedisTemplate(lettuceConnectionFactory, serializationContext);
    }
}
