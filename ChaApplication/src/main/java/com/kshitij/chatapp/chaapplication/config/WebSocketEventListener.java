package com.kshitij.chatapp.chaapplication.config;

import com.kshitij.chatapp.chaapplication.chat.ChatMessage;
import com.kshitij.chatapp.chaapplication.chat.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {

    private final SimpMessageSendingOperations messageTemp;

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event){
        StompHeaderAccessor headerAccessor=StompHeaderAccessor.wrap(event.getMessage());
        String userName= (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");

        if(userName != null){
            log.info("User Disconnected "+ userName);
            var chatMessage = ChatMessage.builder()
                    .sender(userName)
                    .type(MessageType.LEAVE)
                    .build();
            messageTemp.convertAndSend("/topic/public",chatMessage);

        }
    }

}
