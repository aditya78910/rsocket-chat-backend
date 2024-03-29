package com.yahoo.chat.service.impl;

import com.yahoo.chat.dto.request.ChatMessageGetRequestDTO;
import com.yahoo.chat.dto.request.ChatMessageSendRequestDTO;
import com.yahoo.chat.dto.request.RecentChatBuddiesRequest;
import com.yahoo.chat.mapper.ChatMessage_ChatMessageSendDTO_Mapper;
import com.yahoo.chat.mapper.ChatRoomMapper;
import com.yahoo.chat.model.mongo.ChatMessage;
import com.yahoo.chat.model.mongo.ChatRoom;
import com.yahoo.chat.model.mongo.RecentChatRoom;
import com.yahoo.chat.model.mongo.UserChats;
import com.yahoo.chat.repository.mongo.message.ChatMessageRepository;
import com.yahoo.chat.repository.mongo.room.ChatRoomRepository;
import com.yahoo.chat.repository.mongo.userchats.UserChatsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.*;

@Slf4j
@Service
public class MongoService {

    @Value("${messages.pageSize}")
    Integer pageSize;

    ChatMessageRepository chatMessageRepository;

    ChatRoomRepository chatRoomRepository;

    UserChatsRepository userChatRepository;
    ChatMessage_ChatMessageSendDTO_Mapper chatMessage_chatMessageSendDTO_mapper;

    ChatRoomMapper chatRoomMapper;

    @Autowired
    ReactiveMongoTemplate reactiveMongoTemplate;

    public MongoService(ChatMessageRepository chatMessageRepository,
                        ChatRoomRepository chatRoomRepository,
                        UserChatsRepository userChatRepository,
                        ChatMessage_ChatMessageSendDTO_Mapper chatMessage_chatMessageSendDTO_mapper,
                        ChatRoomMapper chatRoomMapper) {
        this.chatMessageRepository = chatMessageRepository;
        this.chatRoomRepository = chatRoomRepository;
        this.userChatRepository = userChatRepository;
        this.chatMessage_chatMessageSendDTO_mapper = chatMessage_chatMessageSendDTO_mapper;
        this.chatRoomMapper = chatRoomMapper;
    }

    public Mono<ChatMessage> saveMessage(ChatMessageSendRequestDTO chatMessageSendRequestDTO,
                                         UUID messageId) {
        ChatMessage chatMessage = chatMessage_chatMessageSendDTO_mapper.toDBModel(chatMessageSendRequestDTO, messageId);

        return chatMessageRepository.save(chatMessage)
                .doOnNext((c) -> log.info("Saved successfully"));

    }

    @Transactional
    public Mono<ChatMessage> fetchChatRoomAndSaveMessage(ChatMessageSendRequestDTO chatMessageSendRequestDTO,
                                         UUID messageId){
        ChatMessage chatMessage = chatMessage_chatMessageSendDTO_mapper.toDBModel(chatMessageSendRequestDTO, messageId);
        /**
         * Each message belongs to a unique chatroom (of 2 participants as of now)
         * The chatroom id is saved with each chat message. If the chatroomId is missing
         * in the request body from UI, then it needs to be fetched from the database for these
         * participants if the chatroom is already existing. If not, a new chatroom should
         * be created.
         */

        return getOrCreateChatRoom(chatMessageSendRequestDTO.getSenderId(),
                chatMessageSendRequestDTO.getReceiver())
                .flatMap(chatRoomId -> {
                    chatMessage.setChatRoomId(chatRoomId);
                    if(true)
                       throw new RuntimeException("just for tx mgmt");
                    return chatMessageRepository.save(chatMessage);
                });

    }


    /****
     * CHeck and return the chatroom id if a chatroom already exists for the 2 participants. If
     * the chatroom does not exist,  create a new chat room and return the chat room id.
     */

    public Mono<UUID> getOrCreateChatRoom(String sender, String receiver){

        Set<String> participants = Set.of(sender, receiver);
        return chatRoomRepository.findByParticipants(participants)
                .switchIfEmpty(createChatRoom(participants, false))
                .map(ChatRoom::getId);
    }

    /**
     * Create a chatroom and insert an entry in chatroom collection. Also
     * update the UserChat collection to add the chatroomIds for each of the user.
     * @param participants
     * @param isGroupChat
     * @return
     */
    public Mono<ChatRoom> createChatRoom(Set<String> participants, boolean isGroupChat){
        return chatRoomRepository.save(chatRoomMapper.toDBModel(participants, isGroupChat))
                .doOnNext(chatRoom -> userChatRepository.updateUserChat(participants, chatRoom.getId()).subscribe());
    }

    public Flux<ChatMessage> getPreviousMessagesPaginated(ChatMessageGetRequestDTO chatMessageGetRequestDTO){

        if(!ObjectUtils.isEmpty(chatMessageGetRequestDTO.getChatRoomId())){
            return chatMessageRepository.findByChatRoomIdOrderByUpdateTsDesc(chatMessageGetRequestDTO.getChatRoomId(),
                    PageRequest.of(chatMessageGetRequestDTO.getPageNumber(), pageSize));
        }else{
            return chatRoomRepository.findByParticipants(
                            Set.of(chatMessageGetRequestDTO.getUsername1(), chatMessageGetRequestDTO.getUsername2()))
                    .flatMapMany(chatRoom -> chatMessageRepository.findByChatRoomIdOrderByUpdateTsDesc(chatRoom.getId(),
                            PageRequest.of(chatMessageGetRequestDTO.getPageNumber(), pageSize)));
        }
    }

    public Flux<ChatRoom> findRecentChatBuddies(Mono<RecentChatBuddiesRequest> recentChatBuddiesRequestMono){


        /*return recentChatBuddiesRequestMono.flatMapMany(recentChatBuddiesRequest ->

                userChatRepository.findById(recentChatBuddiesRequest.getUsername())
                .map(UserChats::getChatRooms)
                .flatMapMany(chatRoomIds ->
                        chatMessageRepository.findMostRecentMessageByChatRoomIdIn(chatRoomIds,
                                PageRequest.of(recentChatBuddiesRequest.getPageNumber(), pageSize)))
        );*/

         return recentChatBuddiesRequestMono.flatMapMany( recentChatBuddiesRequest ->

              userChatRepository.findById(recentChatBuddiesRequest.getUsername())
                .map(UserChats::getChatRooms)
                .doOnNext(chatroomids -> log.info("Chatrooms fetched {}", chatroomids))
                .flatMapMany(chatRoomIds ->
                        chatMessageRepository.findMostRecentMessageByChatRoomIdIn(
                                PageRequest.of(recentChatBuddiesRequest.getPageNumber(), pageSize),chatRoomIds))
                      .doOnNext(m -> log.info("recent message {}", m))
                 .map(RecentChatRoom::getChatRoomId)
                 .collectList()
                 .flatMapMany(recentChatRoomList -> chatRoomRepository.findAllById(recentChatRoomList))

         );
    }


}
