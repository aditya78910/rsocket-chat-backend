package com.yahoo.chat.repository.mongo.message;

import com.yahoo.chat.model.mongo.ChatMessage;
import com.yahoo.chat.model.mongo.RecentChatRoom;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.util.Set;
import java.util.UUID;


public interface ChatMessageRepository extends ReactiveMongoRepository<ChatMessage, UUID> {
    Flux<ChatMessage> findByChatRoomIdOrderByUpdateTsDesc(UUID chatRoomId, PageRequest pageRequest);


    /**
     * Find the chatrooms which had the most recent message. This is paginated.
     * @param chatRoomIds
     * @param pageRequest
     * @return
     */
    @Aggregation(pipeline = {

            "{$match: { chatRoomId: { $in: ?0  }}}",
            "{$group: { _id:  '$chatRoomId', 'mostRecent':{$max:'$insertTs'} }}",
            "{$sort: {'mostRecent': -1}}"
    })
    Flux<RecentChatRoom> findMostRecentMessageByChatRoomIdIn(PageRequest pageRequest, Set<UUID> chatRoomIds);

}
