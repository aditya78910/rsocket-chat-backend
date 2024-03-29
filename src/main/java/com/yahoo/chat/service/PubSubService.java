package com.yahoo.chat.service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PubSubService {

    Mono<Long> publishSingleMessage(String channelName, Object object);

    Flux<Object> subscribeToChannel(String channelName);

    Mono<Long> publishGroupMessage(String channelName, Object object);
}
