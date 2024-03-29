package com.yahoo.chat.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory;
import org.springframework.data.mongodb.ReactiveMongoTransactionManager;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;

@Configuration
public class BeanConfig {
    @Bean
    public GenericJackson2JsonRedisSerializer jsonSerializer(){
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean
    ReactiveMongoTransactionManager reactiveMongoTransactionManager(ReactiveMongoDatabaseFactory reactiveMongoDatabaseFactory){
        return new ReactiveMongoTransactionManager(reactiveMongoDatabaseFactory);
    }
}
