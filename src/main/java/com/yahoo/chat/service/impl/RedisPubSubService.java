package com.yahoo.chat.service.impl;

import com.yahoo.chat.service.PubSubService;
import org.springframework.data.redis.connection.ReactiveSubscription;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class RedisPubSubService implements PubSubService {

    /**
     * A Prefix for a redis channel name for one to one chat messages.
     */
    private static final String SINGLE_CHANNEL_PREFIX = "sn";

    private ReactiveRedisOperations<String, Object> redisTemplate;

    public RedisPubSubService(ReactiveRedisOperations<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Mono<Long> publishSingleMessage(String channelName, Object object) {
        return redisTemplate.convertAndSend(SINGLE_CHANNEL_PREFIX + channelName, object);
    }

    @Override
    public Flux<Object> subscribeToChannel(String channelName) {
        return redisTemplate.listenTo(ChannelTopic.of(SINGLE_CHANNEL_PREFIX + channelName))
                .map(ReactiveSubscription.Message::getMessage);
    }

    @Override
    public Mono<Long> publishGroupMessage(String channelName, Object object) {
        throw new UnsupportedOperationException();
    }
}
